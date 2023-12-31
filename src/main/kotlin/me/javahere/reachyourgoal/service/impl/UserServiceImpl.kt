package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.datasource.UserDataSource
import me.javahere.reachyourgoal.domain.ConfirmationToken
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.dto.UserDto
import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.dto.request.RequestUpdateEmail
import me.javahere.reachyourgoal.exception.ExceptionResponse
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import me.javahere.reachyourgoal.exception.ReachYourGoalExceptionType.*
import me.javahere.reachyourgoal.security.jwt.JwtService
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.EXPIRE_CONFIRMATION_TOKEN
import me.javahere.reachyourgoal.service.EmailService
import me.javahere.reachyourgoal.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

typealias UserForSecurity = org.springframework.security.core.userdetails.User

@Service
class UserServiceImpl(
    @Value("\${app.base-url}") private val baseUrl: String,
    private val userDataSource: UserDataSource,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val emailService: EmailService
) : UserService, ReactiveUserDetailsService {

    private val tokenException = ExceptionResponse(ReachYourGoalException(INVALID_CONFIRM_TOKEN))

    override suspend fun registerUser(user: RequestRegister) {
        val isUsernameAvailable = checkAndCleanupUsernameAvailability(user.username)
        val isEmailAvailable = checkAndCleanupEmailAvailability(user.email)

        val exceptions = mutableListOf<ReachYourGoalException>()

        if (!isUsernameAvailable) exceptions += ReachYourGoalException(USERNAME_IS_NOT_AVAILABLE)
        if (!isEmailAvailable) exceptions += ReachYourGoalException(EMAIL_IS_NOT_AVAILABLE)

        if (exceptions.isNotEmpty()) throw ExceptionResponse(exceptions)

        val token = jwtService.accessToken(user.username, EXPIRE_CONFIRMATION_TOKEN, emptyArray())

        val newPassword = passwordEncoder.encode(user.password)

        val createdUser = userDataSource.createUser(
            user
                .transform()
                .copy(password = newPassword)
        )

        val expireDate = System.currentTimeMillis() + EXPIRE_CONFIRMATION_TOKEN

        userDataSource.createConfirmationToken(
            ConfirmationToken(
                token = token,
                expireDate = expireDate,
                userId = createdUser.id!!
            )
        )

        val confirmationLink = "$baseUrl/auth/confirm?token=$token"

        emailService.sendRegisterConfirmEmail(
            user.email,
            confirmationLink,
            Date(expireDate)
        )
    }

    override suspend fun confirmRegister(token: String): UserDto {
        val confirmationToken = validateAndGetConfirmationToken(token)

        val user = userDataSource.retrieveUserById(confirmationToken.userId) ?: throw tokenException

        val confirmedUser = userDataSource.updateUser(user.copy(isConfirmed = true))

        return confirmedUser.transform()
    }

    override suspend fun confirmNewEmail(token: String): UserDto {
        val confirmationToken = validateAndGetConfirmationToken(token)

        val newEmail = jwtService.getSubject(token)

        val user = userDataSource.retrieveUserById(confirmationToken.userId) ?: throw tokenException

        val confirmedUser = userDataSource.updateUser(user.copy(email = newEmail))

        return confirmedUser.transform()
    }

    private suspend fun validateAndGetConfirmationToken(token: String): ConfirmationToken {
        val confirmationToken = userDataSource.retrieveConfirmationTokenByToken(token) ?: throw tokenException
        val expireTime = confirmationToken.expireDate
        val currentTime = System.currentTimeMillis()

        if (expireTime < currentTime) throw tokenException

        return confirmationToken
    }

    override suspend fun findUserById(userId: UUID): UserDto {
        return userDataSource
            .retrieveUserById(userId)
            ?.transform()
            ?: throw ExceptionResponse(ReachYourGoalException(NOT_FOUND, "No user found with that userId: $userId"))
    }

    override suspend fun findUserByEmail(email: String): UserDto {
        return userDataSource
            .retrieveUserByEmail(email)
            ?.transform()
            ?: throw ExceptionResponse(ReachYourGoalException(NOT_FOUND, "No user found with that email: $email"))
    }

    override suspend fun findUserByUsername(username: String): UserDto {
        return userDataSource
            .retrieveUserByUsername(username)
            ?.transform()
            ?: throw ExceptionResponse(ReachYourGoalException(NOT_FOUND, "No user found with that username: $username"))
    }

    override fun findByUsername(username: String): Mono<UserDetails> = mono {
        val user: User =
            userDataSource.retrieveUserByUsername(username)
                ?: throw ExceptionResponse(ReachYourGoalException(BAD_CREDENTIALS))

        if (!user.isConfirmed) throw ExceptionResponse(ReachYourGoalException(EMAIL_NOT_CONFIRMED))

        val authorities: List<GrantedAuthority> = user.authorities

        UserForSecurity(user.email, user.password, authorities)
    }

    private suspend fun checkAndCleanupUsernameAvailability(username: String): Boolean {
        return checkAndCleanupAvailability(username) { userDataSource.retrieveUserByUsername(it) }
    }

    private suspend fun checkAndCleanupEmailAvailability(email: String): Boolean {
        return checkAndCleanupAvailability(email) { userDataSource.retrieveUserByEmail(it) }
    }

    private suspend fun checkAndCleanupAvailability(
        identifier: String,
        retrieveUser: suspend (String) -> User?
    ): Boolean {
        val user = retrieveUser(identifier) ?: return true

        if (user.isConfirmed) return false

        val confirmationToken = userDataSource.retrieveConfirmationTokenByUserId(user.id!!) ?: return run {
            userDataSource.deleteUserById(user.id)
            true
        }

        val expireTime = jwtService.decodeAccessToken(confirmationToken.token).expiresAt.time
        val currentTime = System.currentTimeMillis()

        return if (expireTime < currentTime) {
            userDataSource.deleteUserById(user.id)
            true
        } else {
            false
        }
    }

    override suspend fun updateUser(
        userId: UUID,
        firstName: String?,
        lastName: String?,
        username: String?,
    ): UserDto {
        val foundUser = userDataSource.retrieveUserById(userId)
            ?: throw ExceptionResponse(ReachYourGoalException(NOT_FOUND, "No user found with that userId: $userId"))

        if (username != null) {
            val userWithUsername = userDataSource.retrieveUserByUsername(username)

            if (userWithUsername != null && userWithUsername.id != userId)
                throw ExceptionResponse(ReachYourGoalException(ALREADY_EXISTS, "username: $username is already exists"))
        }

        val newFirstName = firstName ?: foundUser.firstname
        val newLastName = lastName ?: foundUser.lastname
        val newUsername = username ?: foundUser.username

        val updatedUser = userDataSource.updateUser(
            foundUser.copy(
                firstname = newFirstName,
                lastname = newLastName,
                username = newUsername
            )
        )

        return updatedUser.transform()
    }

    override suspend fun updateEmail(request: RequestUpdateEmail) {
        val user = findUserById(request.userId)

        if (user.email == request.newEmail)
            throw ExceptionResponse(ReachYourGoalException(ALREADY_EXISTS, "The user has already the email"))

        val token = jwtService.accessToken(request.newEmail, EXPIRE_CONFIRMATION_TOKEN, emptyArray())

        val expireDate = System.currentTimeMillis() + EXPIRE_CONFIRMATION_TOKEN

        userDataSource.deleteAllConfirmationTokensByUserId(user.id)

        userDataSource.createConfirmationToken(
            ConfirmationToken(
                token = token,
                expireDate = expireDate,
                userId = user.id
            )
        )

        val confirmationLink = "$baseUrl/auth/confirmNewEmail?token=$token"

        emailService.sendUpdateEmailConfirmEmail(
            request.newEmail,
            confirmationLink,
            Date(expireDate)
        )
    }

    override suspend fun deleteUserById(userId: UUID) {
        userDataSource.deleteUserById(userId)
    }

}
