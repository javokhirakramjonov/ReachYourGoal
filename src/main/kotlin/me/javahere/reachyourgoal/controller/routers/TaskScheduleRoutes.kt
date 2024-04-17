package me.javahere.reachyourgoal.controller.routers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.javahere.reachyourgoal.controller.handler.task.TaskScheduleRoutesHandler
import me.javahere.reachyourgoal.controller.routers.TaskPlanRoutes.Companion.TASK_PLAN_ID
import me.javahere.reachyourgoal.controller.routers.TaskRoutes.Companion.TASK_ID
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedules
import me.javahere.reachyourgoal.domain.dto.request.RequestDeleteTaskSchedules
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateTaskSchedules
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskScheduleRoutes(
    private val taskScheduleRoutesHandler: TaskScheduleRoutesHandler,
) {
    @Bean
    fun taskSchedulesOpenApi(): GroupedOpenApi {
        val paths = arrayOf("/task-schedules/**")
        return GroupedOpenApi
            .builder()
            .group("task-schedules")
            .pathsToMatch(*paths)
            .build()
    }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "createTaskSchedule",
                summary = "creates task schedule",
                requestBody =
                    RequestBody(
                        content = [
                            Content(
                                schema = Schema(implementation = RequestCreateTaskSchedules::class),
                            ),
                        ],
                    ),
                responses = [
                    ApiResponse(
                        responseCode = "201",
                        content = [
                            Content(
                                array = ArraySchema(schema = Schema(implementation = TaskScheduleDto::class)),
                            ),
                        ],
                    ),
                ],
            ),
    )
    fun createTaskSchedules() =
        coRouter {
            POST("/task-schedules", taskScheduleRoutesHandler::createTaskSchedules)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getTaskSchedule",
                summary = "get scheduled dates of task",
                parameters = [
                    Parameter(name = TASK_PLAN_ID, `in` = ParameterIn.QUERY),
                    Parameter(name = TASK_ID, `in` = ParameterIn.QUERY),
                ],
                responses = [
                    ApiResponse(
                        responseCode = "200",
                        content = [
                            Content(
                                array = ArraySchema(schema = Schema(implementation = TaskScheduleDto::class)),
                            ),
                        ],
                    ),
                ],
            ),
    )
    fun getTaskSchedules() =
        coRouter {
            GET("/task-schedules", taskScheduleRoutesHandler::getTaskSchedules)
        }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "deleteTaskSchedules",
                summary = "delete task schedules",
                requestBody =
                    RequestBody(
                        content = [
                            Content(
                                schema = Schema(implementation = RequestDeleteTaskSchedules::class),
                            ),
                        ],
                    ),
                responses = [
                    ApiResponse(responseCode = "204"),
                ],
            ),
    )
    fun deleteTaskSchedules() =
        coRouter {
            DELETE("/task-schedules", taskScheduleRoutesHandler::deleteTaskSchedules)
        }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "updateTaskSchedule",
                summary = "update task schedule",
                requestBody =
                    RequestBody(
                        content = [
                            Content(
                                array =
                                    ArraySchema(
                                        schema = Schema(implementation = RequestUpdateTaskSchedules::class),
                                    ),
                            ),
                        ],
                    ),
                responses = [
                    ApiResponse(
                        content = [
                            Content(
                                array =
                                    ArraySchema(
                                        schema = Schema(implementation = TaskScheduleDto::class),
                                    ),
                            ),
                        ],
                        responseCode = "200",
                    ),
                ],
            ),
    )
    fun updateTaskSchedules() =
        coRouter {
            PUT("/task-schedules", taskScheduleRoutesHandler::updateTaskSchedules)
        }
}
