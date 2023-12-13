package me.javahere.reachyourgoal.controller.handler

import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.service.impl.UserServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class UserRoutesHandler(
    private val userService: UserServiceImpl
) {

    suspend fun register(serverRequest: ServerRequest): ServerResponse {
        val user = serverRequest.awaitBody(RequestRegister::class)

        return when {
            userService.isUsernameExists(user.username) -> ServerResponse.status(HttpStatus.CONFLICT)
                .bodyValueAndAwait("Username is already exists")

            userService.isEmailExists(user.email) -> ServerResponse.status(HttpStatus.CONFLICT)
                .bodyValueAndAwait("Username is already exists")

            else -> {
                userService.registerUser(user)

                ServerResponse.ok().buildAndAwait()
            }
        }
    }

}