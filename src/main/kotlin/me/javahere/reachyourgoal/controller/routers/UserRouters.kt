package me.javahere.reachyourgoal.controller.routers

import me.javahere.reachyourgoal.controller.handler.UserRoutesHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class UserRouters {

    @Bean
    fun userRoutes(userRoutesHandler: UserRoutesHandler) = coRouter {
        accept(MediaType.APPLICATION_JSON).nest {
            "/auth".nest {
                POST("/register", userRoutesHandler::register)
            }
        }
    }
}