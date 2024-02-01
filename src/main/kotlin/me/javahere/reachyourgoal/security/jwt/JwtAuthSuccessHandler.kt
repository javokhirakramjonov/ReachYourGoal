package me.javahere.reachyourgoal.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.dto.response.ResponseLogin
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionType.UN_AUTHENTICATED
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.EXPIRE_ACCESS_TOKEN
import me.javahere.reachyourgoal.security.jwt.JwtService.Companion.EXPIRE_REFRESH_TOKEN
import me.javahere.reachyourgoal.service.UserService
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthSuccessHandler(
    private val userService: UserService,
    private val objectMapper: ObjectMapper,
    private val jwtService: JwtService,
) : ServerAuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange,
        authentication: Authentication,
    ): Mono<Void> =
        mono {
            val principal =
                authentication.principal ?: throw RYGException(UN_AUTHENTICATED)

            when (principal) {
                is User -> {
                    val roles =
                        principal
                            .authorities
                            .map(GrantedAuthority::getAuthority)
                            .toTypedArray()

                    val userId = userService.findUserByUsername(principal.username).id.toString()

                    val accessToken = jwtService.generateAccessToken(userId, principal.username, EXPIRE_ACCESS_TOKEN, roles)
                    val refreshToken = jwtService.generateRefreshToken(userId, principal.username, EXPIRE_REFRESH_TOKEN, roles)

                    val responseLogin = ResponseLogin(accessToken, refreshToken)

                    val response = webFilterExchange.exchange.response

                    response.headers.contentType = MediaType.APPLICATION_JSON

                    val monoData = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(responseLogin))

                    response.writeWith(Mono.just(monoData)).awaitFirst()
                }

                else -> throw RYGException(UN_AUTHENTICATED)
            }

            return@mono null
        }
}
