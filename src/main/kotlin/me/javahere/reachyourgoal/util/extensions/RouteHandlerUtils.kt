package me.javahere.reachyourgoal.util.extensions

import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.id.UserId
import me.javahere.reachyourgoal.util.security.jwt.JwtService
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class RouteHandlerUtils(
    private val jwtService: JwtService,
) {
    fun getUserId(serverRequest: ServerRequest): UserId {
        val accessToken = getAccessToken(serverRequest)

        val decodedJWT = jwtService.decodeAccessToken(accessToken)

        val userId = decodedJWT.issuer.toInt()

        return UserId(userId)
    }

    private fun getAccessToken(serverRequest: ServerRequest): String {
        return serverRequest
            .headers()
            .header(HttpHeaders.AUTHORIZATION)
            .firstOrNull()
            .toString()
    }

    fun getQueryParamOrThrow(
        serverRequest: ServerRequest,
        paramName: String,
    ): String {
        return serverRequest
            .queryParam(paramName)
            .orElseThrow { RYGException("Missing required query parameter: $paramName") }
    }
}
