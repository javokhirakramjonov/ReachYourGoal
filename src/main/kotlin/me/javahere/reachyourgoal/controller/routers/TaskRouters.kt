package me.javahere.reachyourgoal.controller.routers

import me.javahere.reachyourgoal.controller.handler.TaskRoutesHandler
import me.javahere.reachyourgoal.util.EMPTY
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskRouters {

    @Bean
    fun taskRoutes(taskRoutesHandler: TaskRoutesHandler) = coRouter {
        "/api/v1/tasks".nest {
            GET(String.EMPTY, taskRoutesHandler::getAllTasks)
            GET("/{id}", taskRoutesHandler::getTaskById)
            POST(String.EMPTY, taskRoutesHandler::createTask)
        }
    }
}