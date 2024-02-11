package me.javahere.reachyourgoal.controller.handler

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.dto.request.validator.RequestTaskCreateValidator
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionType
import me.javahere.reachyourgoal.security.jwt.JwtService
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.util.toUUID
import me.javahere.reachyourgoal.util.validateAndThrow
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.awaitMultipartData
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import java.io.FileInputStream
import java.util.UUID

@Component
class TaskRoutesHandler(
    private val taskService: TaskService,
    private val jwtService: JwtService,
) {
    private fun getUserId(serverRequest: ServerRequest): UUID {
        val accessToken =
            serverRequest
                .headers()
                .header(HttpHeaders.AUTHORIZATION)
                .firstOrNull()
                .toString()

        val decodedJWT = jwtService.decodeAccessToken(accessToken)

        val userId = decodedJWT.issuer.toUUID()

        return userId
    }

    suspend fun createTask(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val task = serverRequest.awaitBody(RequestTaskCreate::class)

        val requestTaskCreateValidator = RequestTaskCreateValidator()

        requestTaskCreateValidator.validateAndThrow(task)

        val createdTask = taskService.createTask(task, userId)

        return ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(createdTask)
    }

    suspend fun getTaskById(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = serverRequest.pathVariable("taskId").toUUID()

        val task = taskService.getTaskById(taskId, userId)

        return ServerResponse.ok().bodyValueAndAwait(task)
    }

    suspend fun getAllTasks(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val allTasks = taskService.getAllTasks(userId)

        return ServerResponse.ok().bodyAndAwait(allTasks)
    }

    suspend fun getAllTaskAttachments(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = serverRequest.pathVariable("taskId").toUUID()

        val attachments = taskService.getAllTaskAttachments(userId, taskId)

        return ServerResponse.ok().bodyAndAwait(attachments)
    }

    suspend fun downloadTaskAttachmentById(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = serverRequest.pathVariable("taskId").toUUID()
        val attachmentId = serverRequest.pathVariable("attachmentId").toUUID()

        val (filename, attachment) =
            taskService.getTaskAttachmentById(
                userId = userId,
                taskId = taskId,
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
        val userId = getUserId(serverRequest)
        val taskId = serverRequest.pathVariable("taskId").toUUID()

        val attachmentNotFoundException = RYGException(RYGExceptionType.NOT_FOUND, "Attachment not found")

        val attachment = (serverRequest.awaitMultipartData().toSingleValueMap()["file"] as? FilePart) ?: throw attachmentNotFoundException

        val attachmentState = taskService.createTaskAttachment(userId, taskId, attachment)

        return ServerResponse.ok().bodyValueAndAwait(attachmentState)
    }

    suspend fun deleteTaskAttachmentById(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = serverRequest.pathVariable("taskId").toUUID()
        val attachmentId = serverRequest.pathVariable("attachmentId").toUUID()

        taskService.deleteTaskAttachmentById(userId, taskId, attachmentId)

        return ServerResponse.noContent().buildAndAwait()
    }
}
