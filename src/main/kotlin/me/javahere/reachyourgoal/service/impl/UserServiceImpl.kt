package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.datasource.UserDataSource
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.dto.UserDto
import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import me.javahere.reachyourgoal.exception.ReachYourGoalExceptionType
import me.javahere.reachyourgoal.service.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserServiceImpl(
    @Qualifier("userDataSourceImpl") private val userDataSource: UserDataSource,
    private val passwordEncoder: PasswordEncoder
) : UserService, ReactiveUserDetailsService {

    override suspend fun registerUser(user: RequestRegister): UserDto {
        val userWithEncodedPassword = user.copy(
            password = passwordEncoder.encode(user.password)
        )

        val createdUser = userDataSource.createUser(userWithEncodedPassword.transform())

        return createdUser.transform()
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

    override suspend fun isUsernameExists(username: String): Boolean {
        val userWithUsername = userDataSource.retrieveUserByUsername(username)

        return userWithUsername != null
    }

    override suspend fun isEmailExists(email: String): Boolean {
        val userWithEmail = userDataSource.retrieveUserByEmail(email)

        return userWithEmail != null
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
                throw ReachYourGoalException(exceptionType = ReachYourGoalExceptionType.AlreadyExists("username: $username is already exists"))
        }

        val newFirstName = firstName ?: foundUser.firstName
        val newLastName = lastName ?: foundUser.lastName
        val newUsername = username ?: foundUser.username

        val updatedUser = userDataSource.updateUser(
            foundUser.copy(
                firstName = newFirstName,
                lastName = newLastName,
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
