package me.javahere.reachyourgoal.controller.handler

import kotlinx.coroutines.reactor.awaitSingleOrNull
import me.javahere.reachyourgoal.dto.RequestLogin
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class UserRoutesHandler(
    private val reactiveUserDetailsService: ReactiveUserDetailsService
) {

    suspend fun login(serverRequest: ServerRequest): ServerResponse {
        val user = serverRequest.awaitBody<RequestLogin>()

        val foundUserDetails = reactiveUserDetailsService
            .findByUsername(user.username)
            .awaitSingleOrNull()

        return when {
            else -> ServerResponse.status(HttpStatus.UNAUTHORIZED).buildAndAwait()
        }
    }

}