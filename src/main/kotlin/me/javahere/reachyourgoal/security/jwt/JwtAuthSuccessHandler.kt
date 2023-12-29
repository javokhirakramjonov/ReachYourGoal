package me.javahere.reachyourgoal.security.jwt

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.exception.ExceptionResponse
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import me.javahere.reachyourgoal.exception.ReachYourGoalExceptionType.UN_AUTHENTICATED
import me.javahere.reachyourgoal.exception.ReachYourGoalExceptionType.UN_AUTHORIZED
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.EXPIRE_ACCESS_TOKEN
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.EXPIRE_REFRESH_TOKEN
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthSuccessHandler(
    private val jwtService: JwtService,
) : ServerAuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange, authentication: Authentication
    ): Mono<Void> = mono {
        val principal =
            authentication.principal ?: throw ExceptionResponse(ReachYourGoalException(UN_AUTHORIZED))

        when (principal) {
            is User -> {
                val roles = principal.authorities.map { it.authority }.toTypedArray()

                val accessToken = jwtService.accessToken(principal.username, EXPIRE_ACCESS_TOKEN, roles)
                val refreshToken = jwtService.refreshToken(principal.username, EXPIRE_REFRESH_TOKEN, roles)

                val exchange =
                    webFilterExchange.exchange ?: throw ExceptionResponse(ReachYourGoalException(UN_AUTHORIZED))

                with(exchange.response.headers) {
                    setBearerAuth(accessToken)
                    set("Refresh-Token", refreshToken)
                }

            }

            else -> throw ExceptionResponse(ReachYourGoalException(UN_AUTHENTICATED))
        }

        return@mono null
    }
}
