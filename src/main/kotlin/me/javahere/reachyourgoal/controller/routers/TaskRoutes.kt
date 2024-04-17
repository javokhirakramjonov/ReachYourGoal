package me.javahere.reachyourgoal.controller.routers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.javahere.reachyourgoal.controller.handler.task.TaskRoutesHandler
import me.javahere.reachyourgoal.domain.dto.TaskDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskRoutes(
    private val taskRoutesHandler: TaskRoutesHandler,
) {
    companion object {
        const val TASK_ID = "taskId"
    }

    @Bean
    fun tasksOpenApi(): GroupedOpenApi {
        val paths = arrayOf("/tasks/**")
        return GroupedOpenApi
            .builder()
            .group("tasks")
            .pathsToMatch(*paths)
            .build()
    }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "createTask",
                summary = "create task",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = RequestCreateTask::class))],
                    ),
                responses = [
                    ApiResponse(
                        description = "created task",
                        responseCode = "201",
                        content = [Content(schema = Schema(implementation = TaskDto::class))],
                    ),
                ],
            ),
    )
    fun createTask() =
        coRouter {
            POST("/tasks", taskRoutesHandler::createTask)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getAllTasks",
                summary = "get all tasks",
                responses = [
                    ApiResponse(
                        description = "tasks",
                        responseCode = "200",
                        content = [Content(array = ArraySchema(schema = Schema(implementation = TaskDto::class)))],
                    ),
                ],
            ),
    )
    fun getAllTasks() =
        coRouter {
            GET("/tasks", taskRoutesHandler::getAllTasks)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getTaskById",
                summary = "get task by taskId",
                parameters = [Parameter(name = TASK_ID, `in` = ParameterIn.PATH)],
                responses = [
                    ApiResponse(
                        description = "task",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = TaskDto::class))],
                    ),
                ],
            ),
    )
    fun getTaskById() =
        coRouter {
            GET("/tasks/{$TASK_ID}", taskRoutesHandler::getTaskById)
        }

    @Bean
    @RouterOperation(
        operation =
            Operation(
                operationId = "deleteTaskById",
                summary = "delete task by id",
                parameters = [Parameter(name = TASK_ID, `in` = ParameterIn.PATH)],
                responses = [
                    ApiResponse(responseCode = "204"),
                ],
            ),
    )
    fun deleteTaskById() =
        coRouter {
            DELETE("/tasks/{$TASK_ID}", taskRoutesHandler::deleteTaskById)
        }
}
