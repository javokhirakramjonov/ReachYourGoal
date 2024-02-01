package me.javahere.reachyourgoal.controller.handler

import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.dto.request.RequestUpdateEmail
import me.javahere.reachyourgoal.dto.request.validator.RequestRegisterValidator
import me.javahere.reachyourgoal.dto.request.validator.RequestUpdateEmailValidator
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionType
import me.javahere.reachyourgoal.localize.MessagesEnum
import me.javahere.reachyourgoal.security.jwt.JwtService
import me.javahere.reachyourgoal.service.UserService
import me.javahere.reachyourgoal.util.getMessage
import me.javahere.reachyourgoal.util.validateAndThrow
import org.springframework.context.support.ResourceBundleMessageSource
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
    private val messageSource: ResourceBundleMessageSource,
) {
    suspend fun register(serverRequest: ServerRequest): ServerResponse {
        val user = serverRequest.awaitBody(RequestRegister::class)

        val requestRegisterValidator = RequestRegisterValidator()

        requestRegisterValidator.validateAndThrow(user)

        userService.register(user)

        return ServerResponse
            .ok()
            .bodyValueAndAwait(MessagesEnum.REGISTRATION_EMAIL_CONFIRMATION_SENT.key)
    }

    suspend fun updateEmail(serverRequest: ServerRequest): ServerResponse {
        val requestUpdateEmail = serverRequest.awaitBody(RequestUpdateEmail::class)

        val requestEmailValidator = RequestUpdateEmailValidator()

        requestEmailValidator.validateAndThrow(requestUpdateEmail)

        userService.updateEmail(requestUpdateEmail)

        return ServerResponse
            .ok()
            .bodyValueAndAwait(messageSource.getMessage(MessagesEnum.NEW_EMAIL_CONFIRMATION_SENT.key))
    }

    suspend fun confirmRegister(serverRequest: ServerRequest): ServerResponse {
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

    suspend fun refreshAccessToken(serverRequest: ServerRequest): ServerResponse {
        val refreshTokenNotFoundException =
            RYGException(
                RYGExceptionType.NOT_FOUND,
                messageSource.getMessage(MessagesEnum.REFRESH_TOKEN_NOT_FOUND_EXCEPTION.key),
            )

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
