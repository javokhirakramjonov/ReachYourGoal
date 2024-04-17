package me.javahere.reachyourgoal.controller.handler.user

import me.javahere.reachyourgoal.controller.routers.UserRoutes.Companion.TOKEN_PARAMETER
import me.javahere.reachyourgoal.controller.validator.RequestRegisterValidator
import me.javahere.reachyourgoal.controller.validator.RequestUpdateEmailValidator
import me.javahere.reachyourgoal.domain.dto.request.RequestRegister
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateEmail
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.service.UserService
import me.javahere.reachyourgoal.util.extensions.validateAndThrow
import me.javahere.reachyourgoal.util.security.jwt.JwtService
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class UserRoutesHandler(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val requestRegisterValidator: RequestRegisterValidator,
    private val requestUpdateEmailValidator: RequestUpdateEmailValidator,
) {
    private suspend fun getRegisterUserAndValidateOrThrow(serverRequest: ServerRequest): RequestRegister {
        val user = serverRequest.awaitBody(RequestRegister::class)

        requestRegisterValidator.validateAndThrow(user)

        return user
    }

    suspend fun registerProductionMode(serverRequest: ServerRequest): ServerResponse {
        val user = getRegisterUserAndValidateOrThrow(serverRequest)

        userService.registerProductionMode(user)

        return ServerResponse
            .ok()
            .buildAndAwait()
    }

    suspend fun registerDevelopMode(serverRequest: ServerRequest): ServerResponse {
        val user = getRegisterUserAndValidateOrThrow(serverRequest)

        val confirmationToken = userService.registerDevelopMode(user)

        return ServerResponse
            .ok()
            .bodyValueAndAwait(confirmationToken)
    }

    suspend fun updateEmail(serverRequest: ServerRequest): ServerResponse {
        val requestUpdateEmail = serverRequest.awaitBody(RequestUpdateEmail::class)

        requestUpdateEmailValidator.validateAndThrow(requestUpdateEmail)

        userService.updateEmail(requestUpdateEmail)

        return ServerResponse
            .ok()
            .buildAndAwait()
    }

    suspend fun confirmRegister(serverRequest: ServerRequest): ServerResponse {
        val token = serverRequest.queryParam(TOKEN_PARAMETER).get()

        val confirmedUser = userService.confirmRegister(token)

        return ServerResponse
            .ok()
            .bodyValueAndAwait(confirmedUser)
    }

    suspend fun confirmNewEmail(serverRequest: ServerRequest): ServerResponse {
        val token = serverRequest.queryParam(TOKEN_PARAMETER).get()

        val confirmedUser = userService.confirmNewEmail(token)

        return ServerResponse
            .ok()
            .bodyValueAndAwait(confirmedUser)
    }

    suspend fun refreshAccessToken(serverRequest: ServerRequest): ServerResponse {
        val refreshTokenNotFoundException =
            RYGException("Refresh token is not found")

        val refreshToken =
            serverRequest
                .headers()
                .firstHeader("Refresh-Token")
                ?: throw refreshTokenNotFoundException

        val newAccessToken = jwtService.refreshAccessToken(refreshToken)

        return ServerResponse
            .noContent()
            .header(AUTHORIZATION, "Bearer $newAccessToken")
            .buildAndAwait()
    }
}
