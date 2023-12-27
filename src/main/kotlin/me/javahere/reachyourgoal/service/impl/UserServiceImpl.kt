package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.datasource.UserDataSource
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.dto.UserDto
import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import me.javahere.reachyourgoal.exception.ReachYourGoalExceptionType
import me.javahere.reachyourgoal.security.jwt.JwtService
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.EXPIRE_ACCESS_TOKEN
import me.javahere.reachyourgoal.service.EmailService
import me.javahere.reachyourgoal.service.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

@Service
class UserServiceImpl(
    @Value("\${app.base-url}") private val baseUrl: String,
    @Qualifier("userDataSourceImpl") private val userDataSource: UserDataSource,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val emailService: EmailService
) : UserService, ReactiveUserDetailsService {

    override suspend fun registerUser(user: RequestRegister) {
        val isUsernameAvailable = isUsernameAvailable(user.username)

        if (!isUsernameAvailable) throw ReachYourGoalException(ReachYourGoalExceptionType.UsernameIsNotAvailable)

        val isEmailAvailable = isEmailAvailable(user.email)

        if (!isEmailAvailable) throw ReachYourGoalException(ReachYourGoalExceptionType.EmailIsNotAvailable)

        val token = jwtService.accessToken(user.username, EXPIRE_ACCESS_TOKEN, emptyArray())

        val newPassword = passwordEncoder.encode(user.password)

        userDataSource.createUnConfirmedUser(
            user
                .copy(password = newPassword)
                .transformToUserUnConfirmed(token)
        )

        val confirmationLink = "$baseUrl/auth/confirm?token=$token"

        emailService.sendRegisterConfirmEmail(user.email, confirmationLink)
    }

    override suspend fun confirm(token: String): UserDto {
        val tokenException = ReachYourGoalException(ReachYourGoalExceptionType.NotFound("No data found for this token"))

        val username = jwtService.getUsername(token)
        val expireDate = jwtService.decodeAccessToken(token).claims["exp"]?.asLong() ?: throw tokenException

        val currentTime = Duration.ofMillis(System.currentTimeMillis()).toSeconds()

        if (expireDate < currentTime) throw tokenException

        val unConfirmedUser = userDataSource.retrieveUnConfirmedUserByToken(token) ?: throw ReachYourGoalException(
            ReachYourGoalExceptionType.InvalidConfirmToken
        )

        if (unConfirmedUser.username != username) throw tokenException

        val requestRegister = unConfirmedUser.transform()

        val confirmedUser = userDataSource.createUser(requestRegister.transform())

        return confirmedUser.transform()
    }

    override suspend fun findUserById(userId: UUID): UserDto {
        return userDataSource
            .retrieveUserById(userId)
            ?.transform()
            ?: throw ReachYourGoalException(ReachYourGoalExceptionType.NotFound("No user found with that userId: $userId"))
    }

    override suspend fun findUserByEmail(email: String): UserDto {
        return userDataSource
            .retrieveUserByEmail(email)
            ?.transform()
            ?: throw ReachYourGoalException(ReachYourGoalExceptionType.NotFound("No user found with that email: $email"))
    }

    override suspend fun findUserByUsername(username: String): UserDto {
        return userDataSource
            .retrieveUserByUsername(username)
            ?.transform()
            ?: throw ReachYourGoalException(ReachYourGoalExceptionType.NotFound("No user found with that username: $username"))
    }

    override fun findByUsername(username: String): Mono<UserDetails> = mono {
        val user: User =
            userDataSource.retrieveUserByUsername(username)
                ?: throw ReachYourGoalException(ReachYourGoalExceptionType.BadCredentials)

        val authorities: List<GrantedAuthority> = user.authorities

        org.springframework.security.core.userdetails.User(
            user.email, user.password, authorities
        )
    }

    override suspend fun isUsernameAvailable(username: String): Boolean {
        return listOfNotNull(
            userDataSource.retrieveUserByUsername(username),
            userDataSource.retrieveUnConfirmedUserByUsername(username)
        ).isEmpty()
    }

    override suspend fun isEmailAvailable(email: String): Boolean {
        return listOfNotNull(
            userDataSource.retrieveUserByEmail(email),
            userDataSource.retrieveUnConfirmedUserByEmail(email)
        ).isEmpty()
    }

    override suspend fun updateUser(
        userId: UUID,
        firstName: String?,
        lastName: String?,
        username: String?,
    ): UserDto {
        val foundUser = userDataSource.retrieveUserById(userId)
            ?: throw ReachYourGoalException(ReachYourGoalExceptionType.NotFound("No user found with that userId: $userId"))

        if (username != null) {
            val userWithUsername = userDataSource.retrieveUserByUsername(username)

            if (userWithUsername != null && userWithUsername.id != userId)
                throw ReachYourGoalException(ReachYourGoalExceptionType.AlreadyExists("username: $username is already exists"))
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

    override suspend fun updateEmail(userId: UUID, newEmail: String): UserDto {
        TODO()
    }

    override suspend fun deleteUserById(userId: UUID) {
        userDataSource.deleteUserById(userId)
    }

}
