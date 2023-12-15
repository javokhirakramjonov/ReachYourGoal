package me.javahere.reachyourgoal.security.jwt

import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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
        try {
            val auth = UsernamePasswordAuthenticationToken(
                jwtService.getUsername(token),
                null,
                jwtService.getRoles(token)
            )
            val context: Context = ReactiveSecurityContextHolder.withAuthentication(auth)
            return chain.filter(exchange).contextWrite(context)
        } catch (e: Exception) {
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.clearContext())
        }
    }

    companion object {
        fun ServerWebExchange.jwtAccessToken(): String? =
            request.headers.getFirst(AUTHORIZATION)?.substringAfter("Bearer ")
    }
}
