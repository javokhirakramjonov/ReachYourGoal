package me.javahere.reachyourgoal.security.jwt

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.domain.dto.request.RequestLogin
import me.javahere.reachyourgoal.domain.dto.request.validator.RequestLoginValidator
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionType
import me.javahere.reachyourgoal.util.validateAndThrow
import org.springframework.core.ResolvableType
import org.springframework.http.MediaType
import org.springframework.http.codec.json.AbstractJackson2Decoder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtToAuthConverter(
    private val jacksonDecoder: AbstractJackson2Decoder,
) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange): Mono<Authentication> =
        mono {
            val loginRequest: RequestLogin = getUsernameAndPassword(exchange) ?: throw RYGException(RYGExceptionType.BAD_REQUEST)

            val requestLoginValidator = RequestLoginValidator()

            requestLoginValidator.validateAndThrow(loginRequest)

            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        }

    private suspend fun getUsernameAndPassword(exchange: ServerWebExchange): RequestLogin? {
        val dataBuffer = exchange.request.body
        val type = ResolvableType.forClass(RequestLogin::class.java)

        return jacksonDecoder
            .decodeToMono(dataBuffer, type, MediaType.APPLICATION_JSON, mapOf())
            .onErrorResume { Mono.empty<RequestLogin>() }
            .cast(RequestLogin::class.java)
            .awaitFirstOrNull()
    }
}
