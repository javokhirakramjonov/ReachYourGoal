package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.datasource.UserDataSource
import me.javahere.reachyourgoal.domain.ConfirmationToken
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.dto.UserDto
import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.dto.request.RequestUpdateEmail
import me.javahere.reachyourgoal.exception.ExceptionGroup
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionType.*
import me.javahere.reachyourgoal.localize.MessagesEnum
import me.javahere.reachyourgoal.security.jwt.JwtService
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.EXPIRE_CONFIRMATION_TOKEN
import me.javahere.reachyourgoal.service.EmailService
import me.javahere.reachyourgoal.service.UserService
import me.javahere.reachyourgoal.util.getMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.support.ResourceBundleMessageSource
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
    private val emailService: EmailService,
    private val messageSource: ResourceBundleMessageSource,
) : UserService, ReactiveUserDetailsService {
    private val invalidConfirmToken = ExceptionGroup(RYGException(INVALID_CONFIRM_TOKEN))
    private val confirmTokenExpired = ExceptionGroup(RYGException(CONFIRM_TOKEN_EXPIRED))

    override suspend fun register(user: RequestRegister) {
        val isUsernameAvailable = checkAndCleanupUsernameAvailability(user.username)
        val isEmailAvailable = checkAndCleanupEmailAvailability(user.email)

        val exceptions = mutableListOf<RYGException>()

        if (!isUsernameAvailable) exceptions += RYGException(USERNAME_IS_NOT_AVAILABLE)
        if (!isEmailAvailable) exceptions += RYGException(EMAIL_IS_NOT_AVAILABLE)

        if (exceptions.isNotEmpty()) throw ExceptionGroup(exceptions)

        val token = jwtService.generateAccessToken(user.username, EXPIRE_CONFIRMATION_TOKEN, emptyArray())

        val newPassword = passwordEncoder.encode(user.password)

        val createdUser =
            userDataSource.createUser(
                user.transform().copy(password = newPassword),
            )

        val expireDate = System.currentTimeMillis() + EXPIRE_CONFIRMATION_TOKEN

        userDataSource.createConfirmationToken(
            ConfirmationToken(
                token = token,
                expireDate = expireDate,
                userId = createdUser.id!!,
            ),
        )

        val confirmationLink = "$baseUrl/auth/confirm?token=$token"

        emailService.sendRegisterConfirmEmail(
            user.email,
            confirmationLink,
            Date(expireDate),
        )
    }

    override suspend fun confirmRegister(token: String): UserDto {
        val confirmationToken = validateAndGetConfirmationToken(token)

        val user = userDataSource.retrieveUserById(confirmationToken.userId) ?: throw invalidConfirmToken

        val confirmedUser = userDataSource.updateUser(user.copy(isConfirmed = true))

        return confirmedUser.transform()
    }

    override suspend fun confirmNewEmail(token: String): UserDto {
        val confirmationToken = validateAndGetConfirmationToken(token)

        val newEmail = jwtService.decodeAccessToken(token)?.subject ?: throw invalidConfirmToken

        val user = userDataSource.retrieveUserById(confirmationToken.userId) ?: throw invalidConfirmToken

        val confirmedUser = userDataSource.updateUser(user.copy(email = newEmail))

        return confirmedUser.transform()
    }

    private suspend fun validateAndGetConfirmationToken(token: String): ConfirmationToken {
        val confirmationToken = userDataSource.retrieveConfirmationTokenByToken(token) ?: throw invalidConfirmToken
        val decodedJWT = jwtService.decodeAccessToken(token) ?: throw invalidConfirmToken

        if (!jwtService.isValidExpireDate(decodedJWT)) throw confirmTokenExpired

        return confirmationToken
    }

    override suspend fun findUserById(userId: UUID): UserDto {
        val errorMessageArguments = arrayOf(userId)
        val errorMessage =
            messageSource.getMessage(
                MessagesEnum.USER_NOT_FOUND_FOR_ID_EXCEPTION.key,
                *errorMessageArguments,
            )

        return userDataSource.retrieveUserById(userId)?.transform() ?: throw ExceptionGroup(
            RYGException(
                NOT_FOUND,
                errorMessage,
            ),
        )
    }

    override suspend fun findUserByEmail(email: String): UserDto {
        val errorMessageArguments = arrayOf(email)
        val errorMessage =
            messageSource.getMessage(
                MessagesEnum.USER_NOT_FOUND_FOR_EMAIL_EXCEPTION.key,
                *errorMessageArguments,
            )

        return userDataSource.retrieveUserByEmail(email)?.transform() ?: throw ExceptionGroup(
            RYGException(
                NOT_FOUND,
                errorMessage,
            ),
        )
    }

    override suspend fun findUserByUsername(username: String): UserDto {
        val errorMessageArguments = arrayOf(username)
        val errorMessage =
            messageSource.getMessage(
                MessagesEnum.USER_NOT_FOUND_FOR_USERNAME_EXCEPTION.key,
                *errorMessageArguments,
            )

        return userDataSource.retrieveUserByUsername(username)?.transform() ?: throw ExceptionGroup(
            RYGException(NOT_FOUND, errorMessage),
        )
    }

    override fun findByUsername(username: String): Mono<UserDetails> =
        mono {
            val user: User =
                userDataSource.retrieveUserByUsername(username) ?: throw ExceptionGroup(
                    RYGException(BAD_CREDENTIALS),
                )

            if (!user.isConfirmed) throw ExceptionGroup(RYGException(EMAIL_NOT_CONFIRMED))

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
        retrieveUser: suspend (String) -> User?,
    ): Boolean {
        val user = retrieveUser(identifier)?.transform() ?: return true

        if (user.isConfirmed) return false

        val confirmationToken =
            userDataSource.retrieveConfirmationTokenByUserId(user.id) ?: return run {
                userDataSource.deleteUserById(user.id)
                true
            }

        val decodedJWT =
            jwtService
                .decodeAccessToken(confirmationToken.token)
                ?: throw invalidConfirmToken

        return if (!jwtService.isValidExpireDate(decodedJWT)) {
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
        val userNotFoundErrorMessageArguments = arrayOf(userId.toString())
        val userNotFoundErrorMessage =
            messageSource.getMessage(
                MessagesEnum.USER_NOT_FOUND_FOR_ID_EXCEPTION.key,
                *userNotFoundErrorMessageArguments,
            )

        val foundUser =
            userDataSource.retrieveUserById(userId) ?: throw ExceptionGroup(
                RYGException(
                    NOT_FOUND,
                    userNotFoundErrorMessage,
                ),
            )

        if (username != null) {
            val usernameExistsErrorMessageArguments = arrayOf(username)
            val usernameExistsErrorMessage =
                messageSource.getMessage(
                    MessagesEnum.USERNAME_ALREADY_EXISTS_EXCEPTION.key,
                    *usernameExistsErrorMessageArguments,
                )

            val userWithUsername = userDataSource.retrieveUserByUsername(username)

            if (userWithUsername != null && userWithUsername.id != userId) {
                throw ExceptionGroup(
                    RYGException(ALREADY_EXISTS, usernameExistsErrorMessage),
                )
            }
        }

        val newFirstName = firstName ?: foundUser.firstname
        val newLastName = lastName ?: foundUser.lastname
        val newUsername = username ?: foundUser.username

        val updatedUser =
            userDataSource.updateUser(
                foundUser.copy(
                    firstname = newFirstName,
                    lastname = newLastName,
                    username = newUsername,
                ),
            )

        return updatedUser.transform()
    }

    override suspend fun updateEmail(request: RequestUpdateEmail) {
        val emailAssignedToCurrentUserErrorMessageArguments = arrayOf(request.newEmail)
        val emailAssignedToCurrentUserErrorMessage =
            messageSource.getMessage(
                MessagesEnum.EMAIL_ALREADY_ASSIGNED_TO_CURRENT_USER_EXCEPTION.key,
                *emailAssignedToCurrentUserErrorMessageArguments,
            )

        val user = findUserById(request.userId)

        if (user.email == request.newEmail) {
            throw ExceptionGroup(RYGException(ALREADY_EXISTS, emailAssignedToCurrentUserErrorMessage))
        }

        val userWithEmail = userDataSource.retrieveUserByEmail(request.newEmail)
        if (userWithEmail != null && userWithEmail.id != request.userId) {
            val emailExistsErrorMessageArguments = arrayOf(request.newEmail)
            val emailExistsErrorMessage =
                messageSource.getMessage(
                    MessagesEnum.EMAIL_ALREADY_EXISTS_EXCEPTION.key,
                    *emailExistsErrorMessageArguments,
                )

            throw ExceptionGroup(RYGException(ALREADY_EXISTS, emailExistsErrorMessage))
        }

        val token = jwtService.generateAccessToken(request.newEmail, EXPIRE_CONFIRMATION_TOKEN, emptyArray())

        val expireDate = System.currentTimeMillis() + EXPIRE_CONFIRMATION_TOKEN

        userDataSource.deleteAllConfirmationTokensByUserId(user.id)

        userDataSource.createConfirmationToken(
            ConfirmationToken(
                token = token,
                expireDate = expireDate,
                userId = user.id,
            ),
        )

        val confirmationLink = "$baseUrl/auth/confirmNewEmail?token=$token"

        emailService.sendUpdateEmailConfirmEmail(
            request.newEmail,
            confirmationLink,
            Date(expireDate),
        )
    }

    override suspend fun deleteUserById(userId: UUID) {
        userDataSource.deleteUserById(userId)
    }
}
