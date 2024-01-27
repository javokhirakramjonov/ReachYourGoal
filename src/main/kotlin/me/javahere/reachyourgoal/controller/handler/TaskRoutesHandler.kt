package me.javahere.reachyourgoal.controller.handler

import kotlinx.coroutines.reactive.awaitSingle
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionType
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.service.UserService
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import java.util.*

@Component
class TaskRoutesHandler(
    private val userService: UserService,
    private val taskService: TaskService,
) {
    private suspend fun getUserId(serverRequest: ServerRequest): UUID {
        val email =
            serverRequest.awaitPrincipal()?.name
                ?: throw RYGException(RYGExceptionType.UN_AUTHENTICATED)

        val user = userService.findUserByEmail(email)

        return user.id
    }

    suspend fun createTask(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val task = serverRequest.awaitBody(RequestTaskCreate::class)

        val createdTask = taskService.createTask(task, userId)

        return ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(createdTask)
    }

    suspend fun getTaskById(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val id = UUID.fromString(serverRequest.pathVariable("taskId"))

        val task = taskService.getTaskById(id, userId)

        return ServerResponse.ok().bodyValueAndAwait(task)
    }

    suspend fun getAllTasks(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val allTasks = taskService.getAllTasks(userId)

        return ServerResponse.ok().bodyAndAwait(allTasks)
    }

    suspend fun getAllTaskAttachments(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = UUID.fromString(serverRequest.pathVariable("taskId"))

        val attachments = taskService.getAllTaskAttachments(userId, taskId)

        return ServerResponse.ok().bodyAndAwait(attachments)
    }

    suspend fun downloadTaskAttachmentById(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = UUID.fromString(serverRequest.pathVariable("taskId"))
        val attachmentId = UUID.fromString(serverRequest.pathVariable("attachmentId"))

        val attachment =
            taskService.getTaskAttachmentById(
                userId = userId,
                taskId = taskId,
                attachmentId = attachmentId,
            )

        val toDownload = FileSystemResource(attachment)

        return ServerResponse
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + attachment.getName())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(attachment.length())
            .bodyValueAndAwait(toDownload)
    }

    suspend fun uploadTaskAttachment(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = UUID.fromString(serverRequest.pathVariable("taskId"))

        val data = serverRequest.awaitMultipartData()

        val attachment = data["file"]?.firstOrNull()

        val (fileName, content) = (attachment as FilePart).let { it.name() to it.content().awaitSingle() }

        val attachmentState = taskService.createTaskAttachment(userId, taskId, fileName, content)

        return ServerResponse.ok().bodyValueAndAwait(attachmentState)
    }

    suspend fun deleteTaskAttachmentById(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = UUID.fromString(serverRequest.pathVariable("taskId"))
        val attachmentId = UUID.fromString(serverRequest.pathVariable("attachmentId"))

        taskService.deleteTaskAttachmentById(userId, taskId, attachmentId)

        return ServerResponse.noContent().buildAndAwait()
    }
}
