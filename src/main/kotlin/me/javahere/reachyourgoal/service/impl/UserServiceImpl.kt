package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.domain.dto.UserDto
import me.javahere.reachyourgoal.domain.dto.request.RequestRegister
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateEmail
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionGroup
import me.javahere.reachyourgoal.exception.RYGExceptionType.ALREADY_EXISTS
import me.javahere.reachyourgoal.exception.RYGExceptionType.BAD_CREDENTIALS
import me.javahere.reachyourgoal.exception.RYGExceptionType.EMAIL_IS_NOT_AVAILABLE
import me.javahere.reachyourgoal.exception.RYGExceptionType.EMAIL_NOT_CONFIRMED
import me.javahere.reachyourgoal.exception.RYGExceptionType.INVALID_CONFIRM_TOKEN
import me.javahere.reachyourgoal.exception.RYGExceptionType.NOT_FOUND
import me.javahere.reachyourgoal.exception.RYGExceptionType.USERNAME_IS_NOT_AVAILABLE
import me.javahere.reachyourgoal.localize.MessagesEnum
import me.javahere.reachyourgoal.repository.UserRepository
import me.javahere.reachyourgoal.security.jwt.JwtService
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.EXPIRE_CONFIRMATION_TOKEN
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.JWT_EXTRA_KEY_1
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.JWT_EXTRA_KEY_2
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.JWT_KEY_PAIR_CONFIRM_NEW_EMAIL
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.JWT_KEY_PAIR_CONFIRM_REGISTER
import me.javahere.reachyourgoal.service.EmailService
import me.javahere.reachyourgoal.service.UserService
import me.javahere.reachyourgoal.util.getMessage
import me.javahere.reachyourgoal.util.toUUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.Date
import java.util.UUID

typealias UserForSecurity = org.springframework.security.core.userdetails.User

@Service
class UserServiceImpl(
    @Value("\${app.base-url}") private val baseUrl: String,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val emailService: EmailService,
    private val messageSource: ResourceBundleMessageSource,
) : UserService, ReactiveUserDetailsService {
    private val invalidConfirmToken = RYGException(INVALID_CONFIRM_TOKEN)

    override suspend fun register(user: RequestRegister) {
        val isUsernameAvailable = userRepository.getUserByUsername(user.username) == null
        val isEmailAvailable = userRepository.getUserByEmail(user.email) == null

        val exceptions = mutableListOf<RYGException>()

        if (!isUsernameAvailable) exceptions += RYGException(USERNAME_IS_NOT_AVAILABLE)
        if (!isEmailAvailable) exceptions += RYGException(EMAIL_IS_NOT_AVAILABLE)

        if (exceptions.isNotEmpty()) throw RYGExceptionGroup(exceptions)

        val newPassword = passwordEncoder.encode(user.password)

        val userWithNewPassword = user.transform().copy(password = newPassword)

        val createdUser = userRepository.addUser(userWithNewPassword).transform()

        val token =
            jwtService.generateAccessToken(
                createdUser.id.toString(),
                user.username,
                EXPIRE_CONFIRMATION_TOKEN,
                emptyArray(),
                JWT_KEY_PAIR_CONFIRM_REGISTER,
            )

        val expireDate = Date(System.currentTimeMillis() + EXPIRE_CONFIRMATION_TOKEN)

        val confirmationLink = "$baseUrl/auth/confirm?token=$token"

        emailService.sendRegisterConfirmEmail(
            user.email,
            confirmationLink,
            expireDate,
        )
    }

    override suspend fun confirmRegister(token: String): UserDto {
        val decodedToken = jwtService.decodeAccessToken(token)

        val key = decodedToken.getClaim(JWT_EXTRA_KEY_1).asString()

        if (key != JWT_KEY_PAIR_CONFIRM_REGISTER) throw invalidConfirmToken

        val userId = decodedToken.issuer.toUUID()

        val user = userRepository.getUserById(userId) ?: throw invalidConfirmToken

        val confirmedUser = userRepository.updateUser(user.copy(isConfirmed = true))

        return confirmedUser.transform()
    }

    override suspend fun confirmNewEmail(token: String): UserDto {
        val decodedToken = jwtService.decodeAccessToken(token)

        val key = decodedToken.getClaim(JWT_EXTRA_KEY_2).asString()

        if (key != JWT_KEY_PAIR_CONFIRM_NEW_EMAIL) throw invalidConfirmToken

        val userId = decodedToken.issuer.toUUID()
        val newEmail = decodedToken.getClaim(JWT_EXTRA_KEY_1).asString()

        val user = userRepository.getUserById(userId) ?: throw invalidConfirmToken

        val confirmedUser = userRepository.updateUser(user.copy(email = newEmail))

        return confirmedUser.transform()
    }

    override suspend fun findUserById(userId: UUID): UserDto {
        val errorMessageArguments = arrayOf(userId)
        val errorMessage =
            messageSource.getMessage(
                MessagesEnum.USER_NOT_FOUND_FOR_ID_EXCEPTION.key,
                *errorMessageArguments,
            )

        val userNotFoundException = RYGException(NOT_FOUND, errorMessage)

        return userRepository
            .getUserById(userId)
            ?.transform()
            ?: throw userNotFoundException
    }

    override suspend fun findUserByEmail(email: String): UserDto {
        val errorMessageArguments = arrayOf(email)
        val errorMessage =
            messageSource.getMessage(
                MessagesEnum.USER_NOT_FOUND_FOR_EMAIL_EXCEPTION.key,
                *errorMessageArguments,
            )

        val userNotFoundException = RYGException(NOT_FOUND, errorMessage)

        return userRepository.getUserByEmail(email)?.transform() ?: throw userNotFoundException
    }

    override suspend fun findUserByUsername(username: String): UserDto {
        val errorMessageArguments = arrayOf(username)
        val errorMessage =
            messageSource.getMessage(
                MessagesEnum.USER_NOT_FOUND_FOR_USERNAME_EXCEPTION.key,
                *errorMessageArguments,
            )

        val userNotFoundException = RYGException(NOT_FOUND, errorMessage)

        return userRepository.getUserByUsername(username)?.transform() ?: throw userNotFoundException
    }

    override fun findByUsername(username: String): Mono<UserDetails> =
        mono {
            val user: User =
                userRepository.getUserByUsername(username) ?: throw RYGException(BAD_CREDENTIALS)

            if (!user.isConfirmed) throw RYGException(EMAIL_NOT_CONFIRMED)

            val authorities: List<GrantedAuthority> = user.authorities

            UserForSecurity(user.username, user.password, authorities)
        }

    override suspend fun updateUser(
        userId: UUID,
        firstName: String?,
        lastName: String?,
    ): UserDto {
        val userNotFoundErrorMessageArguments = arrayOf(userId.toString())
        val userNotFoundErrorMessage =
            messageSource.getMessage(
                MessagesEnum.USER_NOT_FOUND_FOR_ID_EXCEPTION.key,
                *userNotFoundErrorMessageArguments,
            )

        val userNotFoundException = RYGException(NOT_FOUND, userNotFoundErrorMessage)

        val foundUser =
            userRepository.getUserById(userId) ?: throw userNotFoundException

        val newFirstName = firstName ?: foundUser.firstname
        val newLastName = lastName ?: foundUser.lastname

        val updatedUser =
            userRepository.updateUser(
                foundUser.copy(
                    firstname = newFirstName,
                    lastname = newLastName,
                ),
            )

        return updatedUser.transform()
    }

    override suspend fun updateEmail(request: RequestUpdateEmail) {
        val user = findUserById(request.userId)

        if (user.email == request.newEmail) {
            val emailAssignedToCurrentUserErrorMessageArguments = arrayOf(request.newEmail)
            val emailAssignedToCurrentUserErrorMessage =
                messageSource.getMessage(
                    MessagesEnum.EMAIL_ALREADY_ASSIGNED_TO_CURRENT_USER_EXCEPTION.key,
                    *emailAssignedToCurrentUserErrorMessageArguments,
                )
            throw RYGException(ALREADY_EXISTS, emailAssignedToCurrentUserErrorMessage)
        }

        val userWithEmail = userRepository.getUserByEmail(request.newEmail)
        if (userWithEmail != null) {
            val emailExistsErrorMessageArguments = arrayOf(request.newEmail)
            val emailExistsErrorMessage =
                messageSource.getMessage(
                    MessagesEnum.EMAIL_ALREADY_EXISTS_EXCEPTION.key,
                    *emailExistsErrorMessageArguments,
                )

            throw RYGException(ALREADY_EXISTS, emailExistsErrorMessage)
        }

        val token =
            jwtService.generateAccessToken(
                user.id.toString(),
                user.username,
                EXPIRE_CONFIRMATION_TOKEN,
                emptyArray(),
                request.newEmail,
                JWT_KEY_PAIR_CONFIRM_NEW_EMAIL,
            )

        val expireDate = Date(System.currentTimeMillis() + EXPIRE_CONFIRMATION_TOKEN)

        val confirmationLink = "$baseUrl/auth/confirmNewEmail?token=$token"

        emailService.sendUpdateEmailConfirmEmail(
            request.newEmail,
            confirmationLink,
            expireDate,
        )
    }

    override suspend fun deleteUserById(userId: UUID) {
        userRepository.deleteUserById(userId)
    }
}
