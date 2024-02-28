package me.javahere.reachyourgoal.controller.handler

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.withContext
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.dto.request.RequestTaskScheduling
import me.javahere.reachyourgoal.domain.dto.request.validator.RequestTaskCreateValidator
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
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.awaitMultipartData
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import java.io.FileInputStream
import java.time.LocalDate
import java.util.UUID

@Component
class TaskRoutesHandler(
    private val taskService: TaskService,
    private val jwtService: JwtService,
    private val objectMapper: ObjectMapper,
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

    private suspend fun getTaskSchedulingObject(serverRequest: ServerRequest): RequestTaskScheduling {
        val requestBody =
            serverRequest
                .body(BodyExtractors.toMono(String::class.java))
                .awaitFirst()

        val requestTaskScheduling =
            listOfNotNull(
                kotlin.runCatching {
                    objectMapper.readValue(
                        requestBody,
                        RequestTaskScheduling.TaskDateScheduling::class.java,
                    )
                }.getOrNull(),
                kotlin.runCatching {
                    objectMapper.readValue(
                        requestBody,
                        RequestTaskScheduling.TaskDatesScheduling::class.java,
                    )
                }.getOrNull(),
                kotlin.runCatching {
                    objectMapper.readValue(
                        requestBody,
                        RequestTaskScheduling.TaskWeekDatesScheduling::class.java,
                    )
                }.getOrNull(),
            ).firstOrNull() ?: throw RYGException(RYGExceptionType.BAD_REQUEST)

        return requestTaskScheduling
    }

    suspend fun createTask(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val task = serverRequest.awaitBody(RequestCreateTask::class)

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

    suspend fun deleteTaskById(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = serverRequest.pathVariable("taskId").toUUID()

        taskService.deleteTaskById(userId, taskId)

        return ServerResponse.noContent().buildAndAwait()
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

    suspend fun scheduleTask(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = serverRequest.pathVariable("taskId").toUUID()

        val requestTaskScheduling = getTaskSchedulingObject(serverRequest)

        val taskScheduling = taskService.addTaskScheduling(userId, taskId, requestTaskScheduling)

        return ServerResponse.ok().bodyAndAwait(taskScheduling)
    }

    suspend fun getTaskScheduling(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = serverRequest.pathVariable("taskId").toUUID()

        val queryParams =
            serverRequest
                .queryParams()
                .toSingleValueMap()

        val fromDate = queryParams["fromDate"]?.let(LocalDate::parse) ?: LocalDate.MIN
        val toDate = queryParams["toDate"]?.let(LocalDate::parse) ?: LocalDate.MAX

        val taskScheduling = taskService.getTaskScheduling(userId, taskId, fromDate, toDate)

        return ServerResponse.ok().bodyAndAwait(taskScheduling)
    }

    suspend fun deleteTaskScheduling(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = serverRequest.pathVariable("taskId").toUUID()

        val requestTaskScheduling = getTaskSchedulingObject(serverRequest)

        taskService.deleteTaskScheduling(userId, taskId, requestTaskScheduling)

        return ServerResponse.noContent().buildAndAwait()
    }
}
