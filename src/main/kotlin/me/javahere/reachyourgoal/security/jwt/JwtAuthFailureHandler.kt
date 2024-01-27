package me.javahere.reachyourgoal.security.jwt

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import me.javahere.reachyourgoal.exception.ExceptionResponse
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import me.javahere.reachyourgoal.exception.ReachYourGoalExceptionType
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
		val exchange =
			webFilterExchange.exchange
				?: throw ExceptionResponse(ReachYourGoalException(ReachYourGoalExceptionType.UN_AUTHORIZED))

		with(exchange.response) {
			statusCode = UNAUTHORIZED
			setComplete().awaitFirstOrNull()
		}
	}
}
