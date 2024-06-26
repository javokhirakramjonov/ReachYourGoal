package me.javahere.reachyourgoal.util.security.jwt

import me.javahere.reachyourgoal.util.security.jwt.JwtService.Companion.TOKEN_PREFIX
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
    private val jwtService: JwtService,
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> {
        val token = exchange.jwtAccessToken() ?: return chain.filter(exchange)

        val decodedToken =
            kotlin.runCatching {
                jwtService.decodeAccessToken(token)
            }.getOrNull()
                ?: return chain
                    .filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.clearContext())

        val username = decodedToken.subject
        val roles =
            jwtService
                .getRoles(decodedToken)
                .map(::SimpleGrantedAuthority)

        val auth =
            UsernamePasswordAuthenticationToken(
                username,
                null,
                roles,
            )
        val context: Context = ReactiveSecurityContextHolder.withAuthentication(auth)

        return chain.filter(exchange).contextWrite(context)
    }

    private fun ServerWebExchange.jwtAccessToken(): String? =
        request
            .headers
            .getFirst(AUTHORIZATION)
            ?.substringAfter(TOKEN_PREFIX)
}
