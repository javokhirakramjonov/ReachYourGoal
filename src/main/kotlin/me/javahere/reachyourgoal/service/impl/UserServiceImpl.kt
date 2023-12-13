package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.dto.UserDto
import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.repository.UserRepository
import me.javahere.reachyourgoal.service.UserService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun findByUsername(username: String): Mono<UserDetails> = mono {
        val user: User = userRepository.findByUsername(username)
            ?: throw BadCredentialsException("Invalid Credentials")

        val authorities: List<GrantedAuthority> = user.authorities

        org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            authorities
        )
    }

    override suspend fun isUsernameExists(username: String): Boolean {
        val userWithUsername = userRepository.findByUsername(username)

        return userWithUsername != null
    }

    override suspend fun isEmailExists(email: String): Boolean {
        val userWithEmail = userRepository.findByEmail(email)

        return userWithEmail != null
    }

    override suspend fun registerUser(user: RequestRegister) {
        val userWithEncodedPassword = user.copy(
            password = passwordEncoder.encode(user.password)
        )

        userRepository.save(userWithEncodedPassword.transform())
    }

    override suspend fun findUserByUsername(username: String): UserDto? {
        return userRepository.findByUsername(username)?.transform()
    }

}
