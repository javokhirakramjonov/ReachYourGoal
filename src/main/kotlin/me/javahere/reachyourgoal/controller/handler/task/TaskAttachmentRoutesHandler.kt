package me.javahere.reachyourgoal.controller.handler.task

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.javahere.reachyourgoal.controller.routers.TaskAttachmentRoutes.Companion.ATTACHMENT_PARAMETER_NAME
import me.javahere.reachyourgoal.controller.routers.TaskAttachmentRoutes.Companion.TASK_ATTACHMENT_ID
import me.javahere.reachyourgoal.controller.routers.TaskRoutes.Companion.TASK_ID
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.service.TaskAttachmentService
import me.javahere.reachyourgoal.util.extensions.RouteHandlerUtils
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitMultipartData
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import java.io.FileInputStream

@Component
class TaskAttachmentRoutesHandler(
    private val taskAttachmentService: TaskAttachmentService,
    private val routeHandlerUtils: RouteHandlerUtils,
) {
    suspend fun getAllTaskAttachments(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                routeHandlerUtils.getUserId(serverRequest),
                routeHandlerUtils.getQueryParamOrThrow(serverRequest, TASK_ID).toInt(),
            )

        val attachments = taskAttachmentService.getAllTaskAttachmentsByTaskId(taskId, userId)

        return ServerResponse.ok().bodyAndAwait(attachments)
    }

    suspend fun downloadTaskAttachmentById(serverRequest: ServerRequest): ServerResponse {
        val (userId, attachmentId) =
            listOf(
                routeHandlerUtils.getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ATTACHMENT_ID).toInt(),
            )

        val (filename, attachment) =
            taskAttachmentService.getTaskAttachmentById(
                userId = userId,
                attachmentId = attachmentId,
            )

        val toDownload =
            InputStreamResource(
                withContext(Dispatchers.IO) {
                    FileInputStream(attachment)
                },
            )

        return ServerResponse
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(attachment.length())
            .bodyValueAndAwait(toDownload)
    }

    suspend fun uploadTaskAttachment(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                routeHandlerUtils.getUserId(serverRequest),
                routeHandlerUtils.getQueryParamOrThrow(serverRequest, TASK_ID).toInt(),
            )

        val attachmentNotFoundException = RYGException("Attachment is not found")

        val attachment =
            (
                serverRequest
                    .awaitMultipartData()
                    .toSingleValueMap()[ATTACHMENT_PARAMETER_NAME] as? FilePart
            ) ?: throw attachmentNotFoundException

        val attachmentState = taskAttachmentService.createTaskAttachment(taskId, attachment, userId)

        return ServerResponse.ok().bodyValueAndAwait(attachmentState)
    }

    suspend fun deleteTaskAttachmentById(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)
        val attachmentId = serverRequest.pathVariable(TASK_ATTACHMENT_ID).toInt()

        taskAttachmentService.deleteTaskAttachmentById(attachmentId, userId)

        return ServerResponse.noContent().buildAndAwait()
    }
}
