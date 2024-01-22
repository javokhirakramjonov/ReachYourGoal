package me.javahere.reachyourgoal.controller.routers

import me.javahere.reachyourgoal.controller.handler.TaskRoutesHandler
import me.javahere.reachyourgoal.util.EMPTY
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskRoutes {

    @RouterOperations(
        RouterOperation(
            path = "/api/v1/tasks/{taskId}/attachments/{attachmentId}",
        )
    )
    @Bean
    fun taskRouter(taskRoutesHandler: TaskRoutesHandler) = coRouter {
        "/api/v1/tasks".nest {
            GET(String.EMPTY, taskRoutesHandler::getAllTasks)
            POST(String.EMPTY, taskRoutesHandler::createTask)

            "/{taskId}".nest {
                GET(String.EMPTY, taskRoutesHandler::getTaskById)

                "/attachments".nest {
                    GET(String.EMPTY, taskRoutesHandler::getTaskAttachmentsByTaskId)
                    GET("/{attachmentId}", taskRoutesHandler::downloadAttachmentByTaskIdAndAttachmentId)
                    POST("/upload", taskRoutesHandler::uploadTaskAttachments)
                    DELETE("/{attachmentId}", taskRoutesHandler::deleteAttachmentByAttachmentId)
                }
            }

        }
    }
}