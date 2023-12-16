package me.javahere.reachyourgoal.controller.handler

import me.javahere.reachyourgoal.configuration.exception.HttpExceptionFactory
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.service.UserService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import java.util.*

@Component
class TaskRoutesHandler(
    private val userService: UserService,
    private val taskService: TaskService
) {

    private suspend fun getUserId(serverRequest: ServerRequest): UUID {
        val email = serverRequest.awaitPrincipal()?.name ?: throw HttpExceptionFactory.unauthorized()

        val user = userService.findUserByEmail(email) ?: throw HttpExceptionFactory.unauthorized()

        return user.id
    }

    suspend fun createTask(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val task = serverRequest.awaitBody(RequestTaskCreate::class)

        val createdTask = taskService.createTask(task, userId)

        return ServerResponse.ok().bodyValueAndAwait(createdTask)
    }

    suspend fun getTaskById(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val id = UUID.fromString(serverRequest.pathVariable("id"))
        val task = taskService.getTaskByTaskIdAndUserId(id, userId)

        return if (task != null)
            ServerResponse.ok().bodyValueAndAwait(task)
        else
            ServerResponse.notFound().buildAndAwait()
    }

    suspend fun getAllTasks(serverRequest: ServerRequest): ServerResponse {
        val userId = getUserId(serverRequest)

        val allTasks = taskService.getAllTasksByUserId(userId)

        return ServerResponse.ok().bodyAndAwait(allTasks)
    }

}