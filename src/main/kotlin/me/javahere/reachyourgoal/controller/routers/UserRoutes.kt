package me.javahere.reachyourgoal.controller.routers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.javahere.reachyourgoal.controller.handler.user.UserRoutesHandler
import me.javahere.reachyourgoal.domain.dto.UserDto
import me.javahere.reachyourgoal.domain.dto.request.RequestLogin
import me.javahere.reachyourgoal.domain.dto.request.RequestRegister
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateEmail
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class UserRoutes(
    private val userRoutesHandler: UserRoutesHandler,
) {
    companion object {
        const val TOKEN_PARAMETER = "token"
    }

    @Bean
    fun authOpenApi(): GroupedOpenApi {
        val paths = arrayOf("/auth/**")
        return GroupedOpenApi
            .builder()
            .group("auth")
            .pathsToMatch(*paths)
            .build()
    }

    @Bean
    @Profile("dev")
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "register",
                summary = "register user",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = RequestRegister::class))],
                    ),
                responses = [
                    ApiResponse(
                        description = "confirmation token",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = String::class))],
                    ),
                ],
            ),
    )
    fun registerDevelopMode() =
        coRouter {
            POST("/auth/register", userRoutesHandler::registerDevelopMode)
        }

    @Bean
    @Profile("prod")
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "register",
                summary = "register user",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = RequestRegister::class))],
                    ),
            ),
    )
    fun registerProductionMode() =
        coRouter {
            POST("/auth/register", userRoutesHandler::registerProductionMode)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "confirmRegister",
                summary = "confirm registration",
                parameters = [Parameter(name = TOKEN_PARAMETER, description = "token provided in email")],
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        description = "confirmed User",
                        content = [Content(schema = Schema(implementation = UserDto::class))],
                    ),
                ],
            ),
    )
    fun confirmRegister() =
        coRouter {
            PUT("/auth/confirmRegister", userRoutesHandler::confirmRegister)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "updateEmail",
                summary = "update user's email",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = RequestUpdateEmail::class))],
                    ),
            ),
    )
    fun updateEmail() =
        coRouter {
            POST("/auth/updateEmail", userRoutesHandler::updateEmail)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "confirmNewEmail",
                summary = "confirm new email",
                parameters = [Parameter(name = TOKEN_PARAMETER, description = "token provided in new email")],
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        description = "confirmed User",
                        content = [Content(schema = Schema(implementation = UserDto::class))],
                    ),
                ],
            ),
    )
    fun confirmNewEmail() =
        coRouter {
            PUT("/auth/confirmNewEmail", userRoutesHandler::confirmNewEmail)
        }

    @Bean
    @RouterOperation(
        operation =
            Operation(
                operationId = "refreshAccessToken",
                summary = "refresh access token",
                parameters = [Parameter(name = "Refresh-Token", `in` = ParameterIn.HEADER)],
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        headers = [Header(name = "Authorization", description = "fresh access token")],
                    ),
                ],
            ),
    )
    fun refreshAccessToken() =
        coRouter {
            GET("/auth/refreshAccessToken", userRoutesHandler::refreshAccessToken)
        }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "login",
                summary = "login",
                requestBody =
                    RequestBody(
                        content = [
                            Content(
                                schema = Schema(implementation = RequestLogin::class),
                            ),
                        ],
                    ),
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        headers = [
                            Header(name = "Authorization", description = "fresh access token"),
                            Header(name = "Refresh-Token", description = "fresh refresh token"),
                        ],
                    ),
                ],
            ),
    )
    fun login() =
        coRouter {
            POST("/auth/login") {
                ServerResponse.ok().buildAndAwait()
            }
        }
}
