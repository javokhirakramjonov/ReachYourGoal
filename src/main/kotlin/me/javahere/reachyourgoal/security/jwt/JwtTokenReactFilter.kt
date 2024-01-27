package me.javahere.reachyourgoal.security.jwt

import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.util.context.Context

class JwtTokenReactFilter(
	private val jwtService: JwtService
) : WebFilter {

	override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
		val token = exchange.jwtAccessToken() ?: return chain.filter(exchange)

		val decodedToken = jwtService
			.decodeAccessToken(token)
			?: return chain
				.filter(exchange)
				.contextWrite(ReactiveSecurityContextHolder.clearContext())

		val username = decodedToken.subject
		val roles = decodedToken
			.getClaim("role")
			.asList(String::class.java)
			.map { SimpleGrantedAuthority(it) }

		val auth = UsernamePasswordAuthenticationToken(
			username,
			null,
			roles
		)
		val context: Context = ReactiveSecurityContextHolder.withAuthentication(auth)

		return chain.filter(exchange).contextWrite(context)
	}

	private fun ServerWebExchange.jwtAccessToken(): String? = request
		.headers
		.getFirst(AUTHORIZATION)
		?.substringAfter("Bearer ")
}
