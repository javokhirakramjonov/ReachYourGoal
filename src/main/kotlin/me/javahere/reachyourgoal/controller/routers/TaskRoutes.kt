package me.javahere.reachyourgoal.controller.routers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import me.javahere.reachyourgoal.controller.handler.TaskRoutesValidator
import me.javahere.reachyourgoal.dto.TaskAttachmentDto
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskRoutes(
    private val taskRoutesValidator: TaskRoutesValidator,
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
            POST("/tasks", taskRoutesValidator::validateAndProcessCreateTask)
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
            GET("/tasks", taskRoutesValidator::validateAndProcessGetAllTasks)
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
            GET("/tasks/{taskId}", taskRoutesValidator::validateAndProcessGetTaskById)
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
            GET("/tasks/{taskId}/attachments", taskRoutesValidator::validateAndProcessGetAllTaskAttachments)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "downloadAttachmentByTaskIdAndAttachmentId",
                summary = "get task attachment by taskId and attachmentId",
                parameters = [
                    Parameter(name = "taskId", `in` = ParameterIn.PATH),
                    Parameter(name = "attachmentId", `in` = ParameterIn.PATH),
                ],
                responses = [
                    ApiResponse(
                        description = "task attachment",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = TaskAttachmentDto::class))],
                    ),
                ],
            ),
    )
    fun downloadAttachmentByTaskIdAndAttachmentId() =
        coRouter {
            GET("/tasks/{taskId}/attachments/{attachmentId}", taskRoutesValidator::validateAndProcessDownloadTaskAttachmentById)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "uploadTaskAttachments",
                summary = "upload attachments for task",
                parameters = [
                    Parameter(name = "taskId", `in` = ParameterIn.PATH),
                    Parameter(
                        name = "files",
                        `in` = ParameterIn.QUERY,
                        content = [
                            Content(
                                mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                schema = Schema(implementation = MultipartFile::class),
                            ),
                        ],
                    ),
                ],
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
            POST("/tasks/{taskId}/attachments/upload", taskRoutesValidator::validateAndProcessUploadTaskAttachment)
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
            DELETE("/tasks/{taskId}/attachments/{attachmentId}", taskRoutesValidator::validateAndProcessDeleteTaskAttachmentById)
        }
}
