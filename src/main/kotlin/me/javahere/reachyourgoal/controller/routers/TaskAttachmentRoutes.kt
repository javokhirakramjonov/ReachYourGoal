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
import me.javahere.reachyourgoal.controller.handler.task.TaskAttachmentRoutesHandler
import me.javahere.reachyourgoal.controller.routers.TaskRoutes.Companion.TASK_ID
import me.javahere.reachyourgoal.domain.dto.TaskAttachmentDto
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskAttachmentRoutes(
    private val taskAttachmentRoutesHandler: TaskAttachmentRoutesHandler,
) {
    companion object {
        const val TASK_ATTACHMENT_ID = "attachmentId"
        const val ATTACHMENT_PARAMETER_NAME = "file"
    }

    @Bean
    fun taskAttachmentsOpenApi(): GroupedOpenApi {
        val paths = arrayOf("/task-attachments/**")
        return GroupedOpenApi
            .builder()
            .group("task-attachments")
            .pathsToMatch(*paths)
            .build()
    }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getTaskAttachmentsByTaskId",
                summary = "get task attachments by taskId",
                parameters = [Parameter(name = TASK_ID, `in` = ParameterIn.QUERY)],
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
            GET("/task-attachments", taskAttachmentRoutesHandler::getAllTaskAttachments)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE],
        operation =
            Operation(
                operationId = "downloadAttachmentByTaskIdAndAttachmentId",
                summary = "get task attachment by taskId and attachmentId",
                parameters = [
                    Parameter(name = TASK_ATTACHMENT_ID, `in` = ParameterIn.PATH),
                ],
            ),
    )
    fun downloadAttachmentByTaskIdAndAttachmentId() =
        coRouter {
            GET("/task-attachments/{$TASK_ATTACHMENT_ID}", taskAttachmentRoutesHandler::downloadTaskAttachmentById)
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
                    Parameter(name = TASK_ID, `in` = ParameterIn.QUERY),
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
                                            Schema(implementation = FilePart::class),
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
            POST("/task-attachments/upload", taskAttachmentRoutesHandler::uploadTaskAttachment)
        }

    @Bean
    @RouterOperation(
        operation =
            Operation(
                operationId = "deleteAttachmentByAttachmentId",
                summary = "delete task attachment by attachmentId",
                parameters = [
                    Parameter(name = TASK_ATTACHMENT_ID, `in` = ParameterIn.PATH),
                ],
                responses = [ApiResponse(responseCode = "204")],
            ),
    )
    fun deleteAttachmentByAttachmentId() =
        coRouter {
            DELETE("/task-attachments/{$TASK_ATTACHMENT_ID}", taskAttachmentRoutesHandler::deleteTaskAttachmentById)
        }
}
