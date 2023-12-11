package me.javahere.reachyourgoal.service

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.repository.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {

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
}
