package me.javahere.reachyourgoal.security.jwt

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.configuration.exception.HttpExceptionFactory
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthFailureHandler : ServerAuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange, exception: AuthenticationException?
    ): Mono<Void> = mono {
        val exchange = webFilterExchange.exchange ?: throw HttpExceptionFactory.unauthorized()

        with(exchange.response) {
            statusCode = UNAUTHORIZED
            setComplete().awaitFirstOrNull()
        }
    }
}