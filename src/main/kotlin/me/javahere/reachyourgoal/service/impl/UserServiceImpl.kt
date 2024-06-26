package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.domain.dto.UserDto
import me.javahere.reachyourgoal.domain.dto.request.RequestRegister
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateEmail
import me.javahere.reachyourgoal.domain.entity.User
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.exception.RYGExceptionGroup
import me.javahere.reachyourgoal.repository.UserRepository
import me.javahere.reachyourgoal.service.EmailService
import me.javahere.reachyourgoal.service.UserService
import me.javahere.reachyourgoal.util.security.jwt.JwtService
import me.javahere.reachyourgoal.util.security.jwt.JwtService.Companion.EXPIRE_CONFIRMATION_TOKEN
import me.javahere.reachyourgoal.util.security.jwt.JwtService.Companion.JWT_EXTRA_KEY_1
import me.javahere.reachyourgoal.util.security.jwt.JwtService.Companion.JWT_EXTRA_KEY_2
import me.javahere.reachyourgoal.util.security.jwt.JwtService.Companion.JWT_KEY_PAIR_CONFIRM_NEW_EMAIL
import me.javahere.reachyourgoal.util.security.jwt.JwtService.Companion.JWT_KEY_PAIR_CONFIRM_REGISTER
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.Date

typealias UserForSecurity = org.springframework.security.core.userdetails.User

@Service
class UserServiceImpl(
    @Value("\${app.base-url}") private val baseUrl: String,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val emailService: EmailService,
) : UserService, ReactiveUserDetailsService {
    private val invalidConfirmToken = RYGException("Invalid confirmation token")

    private suspend fun createUserAndGetConfirmationLink(user: RequestRegister): String {
        val isUsernameAvailable = userRepository.findByUsername(user.username) == null
        val isEmailAvailable = userRepository.findByEmail(user.email) == null

        val exceptions = mutableListOf<RYGException>()

        if (!isUsernameAvailable) exceptions += RYGException("Username is not available")
        if (!isEmailAvailable) exceptions += RYGException("Email is not available")

        if (exceptions.isNotEmpty()) throw RYGExceptionGroup(exceptions)

        val newPassword = passwordEncoder.encode(user.password)

        val userWithNewPassword = user.transform().copy(password = newPassword)

        val createdUser = userRepository.save(userWithNewPassword).transform()

        val token =
            jwtService.generateAccessToken(
                createdUser.id,
                user.username,
                EXPIRE_CONFIRMATION_TOKEN,
                emptyArray(),
                JWT_KEY_PAIR_CONFIRM_REGISTER,
            )

        return token
    }

    override suspend fun registerProductionMode(user: RequestRegister) {
        val token = createUserAndGetConfirmationLink(user)

        val expireDate = Date(System.currentTimeMillis() + EXPIRE_CONFIRMATION_TOKEN.inWholeMilliseconds)

        val confirmationLink = "$baseUrl/auth/confirm?token=$token"

        emailService.sendRegisterConfirmEmail(
            user.email,
            confirmationLink,
            expireDate,
        )
    }

    override suspend fun registerDevelopMode(user: RequestRegister): String {
        return createUserAndGetConfirmationLink(user)
    }

    override suspend fun confirmRegister(token: String): UserDto {
        val decodedToken = jwtService.decodeAccessToken(token)

        val key = decodedToken.getClaim(JWT_EXTRA_KEY_1).asString()

        if (key != JWT_KEY_PAIR_CONFIRM_REGISTER) throw invalidConfirmToken

        val userId = decodedToken.issuer.toInt()

        val user = userRepository.findById(userId) ?: throw RYGException("User(id = $userId) is not found")

        val confirmedUser = userRepository.save(user.copy(isConfirmed = true))

        return confirmedUser.transform()
    }

    override suspend fun confirmNewEmail(token: String): UserDto {
        val decodedToken = jwtService.decodeAccessToken(token)

        val key = decodedToken.getClaim(JWT_EXTRA_KEY_2).asString()

        if (key != JWT_KEY_PAIR_CONFIRM_NEW_EMAIL) throw invalidConfirmToken

        val userId = decodedToken.issuer.toInt()
        val newEmail = decodedToken.getClaim(JWT_EXTRA_KEY_1).asString()

        val user = userRepository.findById(userId) ?: throw invalidConfirmToken

        val confirmedUser = userRepository.save(user.copy(email = newEmail))

        return confirmedUser.transform()
    }

    override suspend fun findUserById(userId: Int): UserDto {
        return userRepository
            .findById(userId)
            ?.transform()
            ?: throw RYGException("User(id = $userId) is not found")
    }

    override suspend fun findUserByEmail(email: String): UserDto {
        return userRepository.findByEmail(email)?.transform()
            ?: throw RYGException("Email($email) is not found")
    }

    override suspend fun findUserByUsername(username: String): UserDto {
        return userRepository.findByUsername(username)?.transform()
            ?: throw RYGException("Username($username) is not found")
    }

    override fun findByUsername(username: String): Mono<UserDetails> =
        mono {
            val user: User =
                userRepository.findByUsername(username)
                    ?: throw RYGException("User($username) is not found")

            if (!user.isConfirmed) throw RYGException("Username($username) is not confirmed")

            val authorities: List<GrantedAuthority> = user.authorities

            UserForSecurity(user.username, user.password, authorities)
        }

    override suspend fun updateUser(
        userId: Int,
        firstName: String?,
        lastName: String?,
    ): UserDto {
        val foundUser =
            userRepository.findById(userId) ?: throw RYGException("User(id = $userId) is not found")

        val newFirstName = firstName ?: foundUser.firstname
        val newLastName = lastName ?: foundUser.lastname

        val updatedUser =
            userRepository.save(
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
            throw RYGException("Email(${user.email}) is already yours.")
        }

        val userWithEmail = userRepository.findByEmail(request.newEmail)

        if (userWithEmail != null) {
            throw RYGException("Email(${request.newEmail}) is not available")
        }

        val token =
            jwtService.generateAccessToken(
                user.id,
                user.username,
                EXPIRE_CONFIRMATION_TOKEN,
                emptyArray(),
                request.newEmail,
                JWT_KEY_PAIR_CONFIRM_NEW_EMAIL,
            )

        val expireDate = Date(System.currentTimeMillis() + EXPIRE_CONFIRMATION_TOKEN.inWholeMilliseconds)

        val confirmationLink = "$baseUrl/auth/confirmNewEmail?token=$token"

        emailService.sendUpdateEmailConfirmEmail(
            request.newEmail,
            confirmationLink,
            expireDate,
        )
    }

    override suspend fun deleteUserById(userId: Int) {
        userRepository.deleteById(userId)
    }
}
