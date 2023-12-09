package me.javahere.reachyourgoal.security

import me.javahere.reachyourgoal.domain.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            authorizeExchange {
                authorize("/auth/**", permitAll)
                authorize("/api/**", hasAuthority(Role.USER.name))
                authorize("/admin/**", hasAuthority(Role.ADMIN.name))
            }
        }
    }
}