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
import me.javahere.reachyourgoal.controller.routers.TaskRoutes.Companion.TASK_ID
import me.javahere.reachyourgoal.domain.dto.TaskInPlanDto
import me.javahere.reachyourgoal.domain.dto.TaskPlanDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskInPlan
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
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "addTaskToPlan",
                summary = "add task to plan",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = RequestCreateTaskInPlan::class))],
                    ),
                responses = [
                    ApiResponse(
                        description = "added task to plan",
                        responseCode = "201",
                        content = [Content(schema = Schema(implementation = TaskInPlanDto::class))],
                    ),
                ],
            ),
    )
    fun addTaskToPlan() =
        coRouter {
            POST("/task-plans/tasks", taskPlanRoutesHandler::addTaskToPlan)
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
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getTaskPlanById",
                summary = "get task plan by id",
                parameters = [Parameter(name = TASK_PLAN_ID, `in` = ParameterIn.PATH)],
                responses = [
                    ApiResponse(
                        description = "task plan by id",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = TaskPlanDto::class))],
                    ),
                ],
            ),
    )
    fun getTaskPlanById() =
        coRouter {
            GET("/task-plans/{$TASK_PLAN_ID}", taskPlanRoutesHandler::getTaskPlanById)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getTasksByPlanId",
                summary = "get tasks by plan id",
                parameters = [Parameter(name = TASK_PLAN_ID, `in` = ParameterIn.PATH)],
                responses = [
                    ApiResponse(
                        description = "tasks by plan id",
                        responseCode = "200",
                        content = [Content(array = ArraySchema(schema = Schema(implementation = TaskInPlanDto::class)))],
                    ),
                ],
            ),
    )
    fun getTasksByPlanId() =
        coRouter {
            GET("/task-plans/{$TASK_PLAN_ID}/tasks", taskPlanRoutesHandler::getTasksByPlanId)
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

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "updateTaskInPlan",
                summary = "update task in plan",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = RequestCreateTaskInPlan::class))],
                    ),
                responses = [
                    ApiResponse(
                        description = "updated task and plan",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = TaskInPlanDto::class))],
                    ),
                ],
            ),
    )
    fun updateTaskInPlan() =
        coRouter {
            PUT("/task-plans/tasks", taskPlanRoutesHandler::updateTaskInPlan)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "deleteTaskFromPlan",
                summary = "delete task from plan",
                parameters = [
                    Parameter(name = TASK_PLAN_ID, `in` = ParameterIn.PATH),
                    Parameter(name = TASK_ID, `in` = ParameterIn.PATH),
                ],
                responses = [
                    ApiResponse(
                        description = "deleted task and plan",
                        responseCode = "200",
                    ),
                ],
            ),
    )
    fun deleteTaskFromPlan() =
        coRouter {
            DELETE("/task-plans/{$TASK_PLAN_ID}/tasks/{$TASK_ID}", taskPlanRoutesHandler::deleteTaskFromPlan)
        }
}
