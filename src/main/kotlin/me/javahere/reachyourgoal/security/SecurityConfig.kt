package me.javahere.reachyourgoal.security

import me.javahere.reachyourgoal.domain.Role
import me.javahere.reachyourgoal.security.jwt.JwtService
import me.javahere.reachyourgoal.security.jwt.JwtTokenReactFilter
import me.javahere.reachyourgoal.service.impl.UserServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.codec.json.AbstractJackson2Decoder
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers


@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        jwtService: JwtService,
        jwtAuthenticationFilter: AuthenticationWebFilter
    ): SecurityWebFilterChain = http {

        csrf { disable() }
        formLogin { disable() }
        logout { disable() }

        authorizeExchange {
            authorize("/", permitAll)
            authorize("/auth/**", permitAll)
            authorize("/api/**", hasAuthority(Role.USER.name))
            authorize("/admin/**", hasAuthority(Role.ADMIN.name))
            authorize(anyExchange, permitAll)
        }

        addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        addFilterAt(JwtTokenReactFilter(jwtService), SecurityWebFiltersOrder.AUTHORIZATION)
    }

    @Bean
    fun authenticationWebFilter(
        manager: ReactiveAuthenticationManager,
        jwtConverter: ServerAuthenticationConverter,
        successHandler: ServerAuthenticationSuccessHandler,
        failureHandler: ServerAuthenticationFailureHandler
    ): AuthenticationWebFilter = AuthenticationWebFilter(manager).apply {
        setRequiresAuthenticationMatcher {
            ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/auth/login").matches(it)
        }
        setServerAuthenticationConverter(jwtConverter)

        setAuthenticationSuccessHandler(successHandler)
        setAuthenticationFailureHandler(failureHandler)

        setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance())
    }

    @Bean
    fun jacksonDecoder(): AbstractJackson2Decoder = Jackson2JsonDecoder()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun reactiveAuthenticationManager(
        userService: UserServiceImpl,
        passwordEncoder: PasswordEncoder
    ): ReactiveAuthenticationManager =
        UserDetailsRepositoryReactiveAuthenticationManager(userService).apply {
            setPasswordEncoder(passwordEncoder)
        }
}