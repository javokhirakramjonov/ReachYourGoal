package me.javahere.reachyourgoal.security.jwt

import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import me.javahere.reachyourgoal.exception.ReachYourGoalExceptionType
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
            authentication.principal ?: throw ReachYourGoalException(ReachYourGoalExceptionType.UnAuthorized)

        when (principal) {
            is User -> {
                val roles = principal.authorities.map { it.authority }.toTypedArray()

                val accessToken = jwtService.accessToken(principal.username, EXPIRE_ACCESS_TOKEN, roles)
                val refreshToken = jwtService.refreshToken(principal.username, EXPIRE_REFRESH_TOKEN, roles)

                val exchange =
                    webFilterExchange.exchange ?: throw ReachYourGoalException(ReachYourGoalExceptionType.UnAuthorized)

                with(exchange.response.headers) {
                    setBearerAuth(accessToken)
                    set("Refresh-Token", refreshToken)
                }

            }

            else -> throw ReachYourGoalException(ReachYourGoalExceptionType.UnAuthenticated)
        }

        return@mono null
    }
}
