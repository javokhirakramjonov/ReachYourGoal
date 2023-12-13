package me.javahere.reachyourgoal.controller.routers

import me.javahere.reachyourgoal.controller.handler.UserRoutesHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class UserRouters {

    @Bean
    fun userRoutes(userRoutesHandler: UserRoutesHandler) = coRouter {
        "/auth".nest {
            POST("/register", userRoutesHandler::register)
        }
    }
}