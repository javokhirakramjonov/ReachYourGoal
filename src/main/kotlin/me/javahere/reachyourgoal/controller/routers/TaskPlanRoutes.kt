package me.javahere.reachyourgoal.controller.routers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.javahere.reachyourgoal.controller.handler.task.TaskPlanRoutesHandler
import me.javahere.reachyourgoal.domain.dto.TaskPlanDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskPlan
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskPlanRoutes(
    private val taskPlanRoutesHandler: TaskPlanRoutesHandler,
) {
    companion object {
        const val TASK_PLAN_ID = "taskPlanId"
    }

    @Bean
    fun taskPlansOpenApi(): GroupedOpenApi {
        val paths = arrayOf("/task-plans/**")
        return GroupedOpenApi
            .builder()
            .group("task plans")
            .pathsToMatch(*paths)
            .build()
    }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "createTaskPlan",
                summary = "create task plan",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = RequestCreateTaskPlan::class))],
                    ),
                responses = [
                    ApiResponse(
                        description = "created task",
                        responseCode = "201",
                        content = [Content(schema = Schema(implementation = TaskPlanDto::class))],
                    ),
                ],
            ),
    )
    fun createTaskPlan() =
        coRouter {
            POST("/task-plans", taskPlanRoutesHandler::createTaskPlan)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getTaskPlans",
                summary = "get task plans",
                responses = [
                    ApiResponse(
                        description = "task plans",
                        responseCode = "200",
                        content = [Content(array = ArraySchema(schema = Schema(implementation = TaskPlanDto::class)))],
                    ),
                ],
            ),
    )
    fun getTaskPlans() =
        coRouter {
            GET("/task-plans", taskPlanRoutesHandler::getTaskPlans)
        }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "updateTaskPlan",
                summary = "update task plan",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = TaskPlanDto::class))],
                    ),
                responses = [
                    ApiResponse(
                        description = "updated task plan",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = TaskPlanDto::class))],
                    ),
                ],
            ),
    )
    fun updateTaskPlan() =
        coRouter {
            PUT("/task-plans", taskPlanRoutesHandler::updateTaskPlan)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "deleteTaskPlan",
                summary = "delete task plan",
                parameters = [Parameter(name = TASK_PLAN_ID, `in` = ParameterIn.PATH)],
                responses = [
                    ApiResponse(
                        description = "deleted task plan",
                        responseCode = "200",
                    ),
                ],
            ),
    )
    fun deleteTaskPlan() =
        coRouter {
            DELETE("/task-plans/{$TASK_PLAN_ID}", taskPlanRoutesHandler::deleteTaskPlan)
        }
}
