package me.javahere.reachyourgoal.controller.handler.task

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactive.awaitFirst
import me.javahere.reachyourgoal.controller.routers.TaskCategoryRoutes.Companion.TASK_CATEGORY_ID
import me.javahere.reachyourgoal.controller.routers.TaskRoutes.Companion.TASK_ID
import me.javahere.reachyourgoal.controller.validator.RequestTaskCreateValidator
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedule
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.service.TaskScheduleService
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.util.extensions.RouteHandlerUtils
import me.javahere.reachyourgoal.util.extensions.validateAndThrow
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class TaskRoutesHandler(
    private val taskService: TaskService,
    private val taskScheduleService: TaskScheduleService,
    private val objectMapper: ObjectMapper,
    private val requestTaskCreateValidator: RequestTaskCreateValidator,
    private val routeHandlerUtils: RouteHandlerUtils,
) {
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
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val task = serverRequest.awaitBody(RequestCreateTask::class)

        requestTaskCreateValidator.validateAndThrow(task)

        val createdTask = taskService.createTask(task, userId)

        return ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(createdTask)
    }

    suspend fun getTaskById(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                routeHandlerUtils.getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toInt(),
            )

        val task = taskService.getTaskById(taskId, userId)

        return ServerResponse.ok().bodyValueAndAwait(task)
    }

    suspend fun deleteTaskById(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                routeHandlerUtils.getUserId(serverRequest),
                serverRequest.pathVariable(TASK_ID).toInt(),
            )

        taskService.deleteTaskById(userId, taskId)

        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun getAllTasks(serverRequest: ServerRequest): ServerResponse {
        val (userId, categoryId) =
            listOf(
                routeHandlerUtils.getUserId(serverRequest),
                serverRequest.pathVariable(TASK_CATEGORY_ID).toInt(),
            )

        val allTasks = taskService.getAllTasksByCategoryId(categoryId, userId)

        return ServerResponse.ok().bodyAndAwait(allTasks)
    }
}
