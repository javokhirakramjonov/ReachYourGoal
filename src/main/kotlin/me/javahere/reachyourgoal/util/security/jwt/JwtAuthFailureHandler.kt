package me.javahere.reachyourgoal.util.security.jwt

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthFailureHandler : ServerAuthenticationFailureHandler {
    override fun onAuthenticationFailure(
        webFilterExchange: WebFilterExchange,
        exception: AuthenticationException?,
    ): Mono<Void> {
        val response = webFilterExchange.exchange.response

        response.statusCode = HttpStatus.UNAUTHORIZED

        return response.setComplete()
    }
}
