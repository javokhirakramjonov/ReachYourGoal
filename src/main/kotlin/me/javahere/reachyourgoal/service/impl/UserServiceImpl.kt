package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.datasource.UserDataSource
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.dto.UserDto
import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.service.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
    @Qualifier("userDataSourceImpl")
    private val userDataSource: UserDataSource,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun findByUsername(username: String): Mono<UserDetails> = mono {
        val user: User = userDataSource.retrieveUserByUsername(username)
            ?: throw BadCredentialsException("Invalid Credentials")

        val authorities: List<GrantedAuthority> = user.authorities

        org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            authorities
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

    override suspend fun registerUser(user: RequestRegister): UserDto {
        val userWithEncodedPassword = user.copy(
            password = passwordEncoder.encode(user.password)
        )

        val createdUser = userDataSource.createUser(userWithEncodedPassword.transform())

        return createdUser.transform()
    }

    override suspend fun findUserByEmail(email: String): UserDto? {
        return userDataSource.retrieveUserByEmail(email)?.transform()
    }

}
