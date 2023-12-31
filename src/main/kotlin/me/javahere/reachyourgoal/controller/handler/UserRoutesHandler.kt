package me.javahere.reachyourgoal.controller.handler

import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.dto.request.RequestUpdateEmail
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

    suspend fun updateEmail(serverRequest: ServerRequest): ServerResponse {
        val requestUpdateEmail = serverRequest.awaitBody(RequestUpdateEmail::class)

        userService.updateEmail(requestUpdateEmail)

        return ServerResponse
            .ok()
            .bodyValueAndAwait("Confirmation link has been sent and please confirm the new email.")
    }

    suspend fun confirm(serverRequest: ServerRequest): ServerResponse {
        val token = serverRequest.queryParam("token").get()

        val confirmedUser = userService.confirmRegister(token)

        return ServerResponse
            .ok()
            .bodyValueAndAwait(confirmedUser)
    }

    suspend fun confirmNewEmail(serverRequest: ServerRequest): ServerResponse {
        val token = serverRequest.queryParam("token").get()

        val confirmedUser = userService.confirmNewEmail(token)

        return ServerResponse
            .ok()
            .bodyValueAndAwait(confirmedUser)
    }

}