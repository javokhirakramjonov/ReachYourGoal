package me.javahere.reachyourgoal.controller.handler

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.withContext
import me.javahere.reachyourgoal.controller.validator.RequestTaskCreateValidator
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedule
import me.javahere.reachyourgoal.domain.dto.request.RequestGetTaskSchedule
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateTaskStatus
import me.javahere.reachyourgoal.exception.RYGException
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
import org.springframework.web.reactive.function.BodyExtractors
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
    private val objectMapper: ObjectMapper,
    private val requestTaskCreateValidator: RequestTaskCreateValidator,
) {
    companion object {
        const val TASK_ID = "taskId"
        const val ATTACHMENT_ID = "attachmentId"
    }

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

    private suspend fun getTaskScheduleObject(serverRequest: ServerRequest): RequestCreateTaskSchedule {
        val requestBody =
            serverRequest
                .body(BodyExtractors.toMono(String::class.java))
                .awaitFirst()

        val requestCreateTaskSchedule =
            sequenceOf(
                runCatching {
                    objectMapper.readValue(
                        requestBody,
                        RequestCreateTaskSchedule.CreateTaskDateSchedule::class.java,
                    )
                },
                runCatching {
                    objectMapper.readValue(
                        requestBody,
                        RequestCreateTaskSchedule.CreateTaskDatesSchedule::class.java,
                    )
                },
                runCatching {
                    objectMapper.readValue(
                        requestBody,
                        RequestCreateTaskSchedule.CreateTaskWeekDatesSchedule::class.java,
                    )
                },
            ).firstNotNullOfOrNull { it.getOrNull() } ?: throw RYGException("Invalid schedule type")

        return requestCreateTaskSchedule
    }

    suspend fun createTask(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val task = serverRequest.awaitBody(RequestCreateTask::class)

        requestTaskCreateValidator.validateAndThrow(task)

        val createdTask = taskService.createTask(task, userId)

        return ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(createdTask)
    }

    suspend fun getTaskById(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toUUID(),
            )

        val task = taskService.getTaskById(taskId, userId)

        return ServerResponse.ok().bodyValueAndAwait(task)
    }

    suspend fun deleteTaskById(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toUUID(),
            )

        taskService.deleteTaskById(userId, taskId)

        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun getAllTasks(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val allTasks = taskService.getAllTasks(userId)

        return ServerResponse.ok().bodyAndAwait(allTasks)
    }

    suspend fun getAllTaskAttachments(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toUUID(),
            )

        val attachments = taskService.getAllTaskAttachments(userId, taskId)

        return ServerResponse.ok().bodyAndAwait(attachments)
    }

    suspend fun downloadTaskAttachmentById(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId, attachmentId) =
            listOf(
                getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toUUID(),
                serverRequest.pathVariable(ATTACHMENT_ID).toUUID(),
            )

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
        val (userId, taskId) =
            listOf(
                getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toUUID(),
            )

        val attachmentNotFoundException = RYGException("Attachment is not found")

        val attachment = (serverRequest.awaitMultipartData().toSingleValueMap()["file"] as? FilePart) ?: throw attachmentNotFoundException

        val attachmentState = taskService.createTaskAttachment(userId, taskId, attachment)

        return ServerResponse.ok().bodyValueAndAwait(attachmentState)
    }

    suspend fun deleteTaskAttachmentById(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toUUID(),
            )
        val attachmentId = serverRequest.pathVariable(ATTACHMENT_ID).toUUID()

        taskService.deleteTaskAttachmentById(userId, taskId, attachmentId)

        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun scheduleTask(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toUUID(),
            )

        val requestTaskSchedule = getTaskScheduleObject(serverRequest)

        val taskSchedule = taskService.addTaskSchedule(userId, taskId, requestTaskSchedule)

        return ServerResponse.ok().bodyAndAwait(taskSchedule)
    }

    suspend fun getTaskSchedule(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toUUID(),
            )

        val requestTaskSchedule = serverRequest.awaitBody(RequestGetTaskSchedule::class)

        val taskSchedule = taskService.getTaskSchedule(userId, taskId, requestTaskSchedule)

        return ServerResponse.ok().bodyAndAwait(taskSchedule)
    }

    suspend fun deleteTaskSchedule(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toUUID(),
            )

        val requestTaskSchedule = getTaskScheduleObject(serverRequest)

        taskService.deleteTaskSchedule(userId, taskId, requestTaskSchedule)

        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun updateTaskStatus(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val requestUpdateTaskStatus = serverRequest.awaitBody(RequestUpdateTaskStatus::class)

        val updatedTaskSchedule = taskService.updateTaskStatus(userId, requestUpdateTaskStatus)

        return ServerResponse.ok().bodyValueAndAwait(updatedTaskSchedule)
    }
}
