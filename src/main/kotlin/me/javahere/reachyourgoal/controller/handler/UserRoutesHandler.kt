package me.javahere.reachyourgoal.controller.handler

import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.service.UserService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class UserRoutesHandler(
    private val userService: UserService
) {

    suspend fun register(serverRequest: ServerRequest): ServerResponse {
        val user = serverRequest.awaitBody(RequestRegister::class)

        userService.registerUser(user)

        return ServerResponse
            .ok()
            .bodyValueAndAwait("Registration successful! A confirmation email has been sent to your email address. Please follow the instructions to activate your account.")
    }

    suspend fun confirm(serverRequest: ServerRequest): ServerResponse {
        val token = serverRequest.queryParam("token").get()

        val confirmedUser = userService.confirm(token)

        return ServerResponse
            .ok()
            .bodyValueAndAwait(confirmedUser)
    }

}