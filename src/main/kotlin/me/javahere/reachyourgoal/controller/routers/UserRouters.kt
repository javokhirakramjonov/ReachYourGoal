package me.javahere.reachyourgoal.controller.routers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.javahere.reachyourgoal.controller.handler.UserRoutesHandler
import me.javahere.reachyourgoal.dto.UserDto
import me.javahere.reachyourgoal.dto.request.RequestRegister
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class UserRouters {

    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/auth/register",
            produces = arrayOf(MediaType.APPLICATION_JSON_VALUE),
            method = arrayOf(RequestMethod.POST),
            beanClass = UserRoutesHandler::class,
            beanMethod = "register",
            operation = Operation(
                operationId = "register",
                requestBody = RequestBody(
                    content = arrayOf(
                        Content(
                            schema = Schema(
                                implementation = RequestRegister::class
                            )
                        )
                    )
                ),
                responses = arrayOf(
                    ApiResponse(
                        responseCode = "200",
                        description = "Successful operation",
                        content = arrayOf(
                            Content(
                                schema = Schema(
                                    implementation = UserDto::class
                                )
                            )
                        )
                    ),
                    ApiResponse(
                        responseCode = "409",
                        description = "Email or Username is already exists"
                    )
                )
            )
        )
    )
    fun userRoutes(userRoutesHandler: UserRoutesHandler) = coRouter {
        "/auth".nest {
            POST("/register", userRoutesHandler::register)
        }
    }
}