package me.javahere.reachyourgoal.controller.handler

import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.exception.RYGException
import me.javahere.reachyourgoal.exception.RYGExceptionType
import me.javahere.reachyourgoal.service.UserService
import me.javahere.reachyourgoal.util.toUUID
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import java.util.*

@Component
class TaskRoutesValidator(
    private val userService: UserService,
    private val taskRoutesHandler: TaskRoutesHandler,
) {
    private suspend fun getUserId(serverRequest: ServerRequest): UUID {
        val email =
            serverRequest.awaitPrincipal()?.name
                ?: throw RYGException(RYGExceptionType.UN_AUTHENTICATED)

        val user = userService.findUserByEmail(email)

        return user.id
    }

    suspend fun validateAndProcessCreateTask(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val task = serverRequest.awaitBody(RequestTaskCreate::class)

        return taskRoutesHandler.createTask(userId, task)
    }

    suspend fun validateAndProcessGetTaskById(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)
        val taskId = serverRequest.pathVariable("taskId").toUUID()

        return taskRoutesHandler.getTaskById(taskId, userId)
    }

    suspend fun validateAndProcessGetAllTasks(serverRequest: ServerRequest): ServerResponse {
    }

    suspend fun validateAndProcessGetAllTaskAttachments(serverRequest: ServerRequest): ServerResponse {
    }

    suspend fun validateAndProcessDownloadTaskAttachmentById(serverRequest: ServerRequest): ServerResponse {
    }

    suspend fun validateAndProcessUploadTaskAttachment(serverRequest: ServerRequest): ServerResponse {
    }

    suspend fun validateAndProcessDeleteTaskAttachmentById(serverRequest: ServerRequest): ServerResponse {
    }
}
