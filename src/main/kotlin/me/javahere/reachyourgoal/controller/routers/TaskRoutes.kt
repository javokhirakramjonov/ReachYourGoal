package me.javahere.reachyourgoal.controller.routers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.SchemaProperty
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import me.javahere.reachyourgoal.controller.handler.TaskRoutesHandler
import me.javahere.reachyourgoal.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.dto.request.RequestTaskDate
import me.javahere.reachyourgoal.dto.request.RequestTaskDates
import me.javahere.reachyourgoal.dto.request.RequestTaskWeekDates
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskRoutes(
    private val taskRoutesHandler: TaskRoutesHandler,
) {
    @Bean
    fun tasksOpenApi(): GroupedOpenApi {
        val paths = arrayOf("/tasks/**")
        return GroupedOpenApi
            .builder()
            .group("tasks")
            .pathsToMatch(*paths)
            .addOperationCustomizer { operation, _ ->
                val securityItem = SecurityRequirement().addList("Bearer Authentication")

                operation.addSecurityItem(securityItem)

                operation
            }
            .build()
    }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "register",
                summary = "create task",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = RequestTaskCreate::class))],
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
                parameters = [Parameter(name = "taskId", `in` = ParameterIn.PATH)],
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
            GET("/tasks/{taskId}", taskRoutesHandler::getTaskById)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getTaskAttachmentsByTaskId",
                summary = "get task attachments by taskId",
                parameters = [Parameter(name = "taskId", `in` = ParameterIn.PATH)],
                responses = [
                    ApiResponse(
                        description = "task attachments",
                        responseCode = "200",
                        content = [Content(array = ArraySchema(schema = Schema(implementation = TaskAttachmentDto::class)))],
                    ),
                ],
            ),
    )
    fun getTaskAttachmentsByTaskId() =
        coRouter {
            GET("/tasks/{taskId}/attachments", taskRoutesHandler::getAllTaskAttachments)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE],
        operation =
            Operation(
                operationId = "downloadAttachmentByTaskIdAndAttachmentId",
                summary = "get task attachment by taskId and attachmentId",
                parameters = [
                    Parameter(name = "taskId", `in` = ParameterIn.PATH),
                    Parameter(name = "attachmentId", `in` = ParameterIn.PATH),
                ],
            ),
    )
    fun downloadAttachmentByTaskIdAndAttachmentId() =
        coRouter {
            GET("/tasks/{taskId}/attachments/{attachmentId}", taskRoutesHandler::downloadTaskAttachmentById)
        }

    @Bean
    @RouterOperation(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "uploadTaskAttachments",
                summary = "upload attachments for task",
                parameters = [
                    Parameter(name = "taskId", `in` = ParameterIn.PATH),
                ],
                requestBody =
                    RequestBody(
                        content = [
                            Content(
                                mediaType = "multipart/form-data",
                                schemaProperties = [
                                    SchemaProperty(
                                        name = "file",
                                        schema =
                                            Schema(
                                                implementation = FilePart::class,
//                                                contentMediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                            ),
                                    ),
                                ],
                            ),
                        ],
                    ),
                responses = [
                    ApiResponse(
                        description = "task attachments",
                        responseCode = "201",
                        content = [Content(array = ArraySchema(schema = Schema(implementation = Pair::class)))],
                    ),
                ],
            ),
    )
    fun uploadTaskAttachments() =
        coRouter {
            POST("/tasks/{taskId}/attachments/upload", taskRoutesHandler::uploadTaskAttachment)
        }

    @Bean
    @RouterOperation(
        operation =
            Operation(
                operationId = "deleteAttachmentByAttachmentId",
                summary = "delete task attachment by attachmentId",
                parameters = [
                    Parameter(name = "taskId", `in` = ParameterIn.PATH),
                    Parameter(name = "attachmentId", `in` = ParameterIn.PATH),
                ],
                responses = [ApiResponse(responseCode = "204")],
            ),
    )
    fun deleteAttachmentByAttachmentId() =
        coRouter {
            DELETE("/tasks/{taskId}/attachments/{attachmentId}", taskRoutesHandler::deleteTaskAttachmentById)
        }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "createScheduledTask",
                summary = "creates scheduled task",
                parameters = [
                    Parameter(name = "taskId", `in` = ParameterIn.PATH),
                ],
                requestBody =
                    RequestBody(
                        content = [
                            Content(
                                schema =
                                    Schema(
                                        oneOf = [
                                            RequestTaskDate::class,
                                            RequestTaskDates::class,
                                            RequestTaskWeekDates::class,
                                        ],
                                    ),
                            ),
                        ],
                    ),
                responses = [ApiResponse(responseCode = "201")],
            ),
    )
    fun createScheduledTask() =
        coRouter {
            POST("/tasks/{taskId}/schedule", taskRoutesHandler::scheduleTask)
        }
}
