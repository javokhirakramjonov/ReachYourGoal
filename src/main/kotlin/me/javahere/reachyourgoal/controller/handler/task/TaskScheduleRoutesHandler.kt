package me.javahere.reachyourgoal.controller.handler.task

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactive.awaitFirst
import me.javahere.reachyourgoal.controller.routers.TaskRoutes.Companion.TASK_ID
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedule
import me.javahere.reachyourgoal.domain.dto.request.RequestGetTaskSchedule
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.service.TaskScheduleService
import me.javahere.reachyourgoal.util.extensions.RouteHandlerUtils
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class TaskScheduleRoutesHandler(
    private val objectMapper: ObjectMapper,
    private val taskScheduleService: TaskScheduleService,
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

    suspend fun createTaskSchedule(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                routeHandlerUtils.getUserId(serverRequest),
                routeHandlerUtils.getQueryParamOrThrow(serverRequest, TASK_ID).toInt(),
            )

        val taskSchedule = getTaskScheduleObject(serverRequest)

        val createdTaskSchedule = taskScheduleService.createTaskSchedule(userId, taskId, taskSchedule)

        return ServerResponse.ok().bodyAndAwait(createdTaskSchedule)
    }

    suspend fun getTaskScheduleForPeriod(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                routeHandlerUtils.getUserId(serverRequest),
                routeHandlerUtils.getQueryParamOrThrow(serverRequest, TASK_ID).toInt(),
            )

        val period = serverRequest.awaitBody(RequestGetTaskSchedule::class)

        val taskSchedule = taskScheduleService.getTaskScheduleForTaskAndPeriod(userId, taskId, period)

        return ServerResponse.ok().bodyAndAwait(taskSchedule)
    }

    suspend fun deleteTaskSchedule(serverRequest: ServerRequest): ServerResponse {
        val (userId, taskId) =
            listOf(
                routeHandlerUtils.getUserId(serverRequest),
                routeHandlerUtils.getQueryParamOrThrow(serverRequest, TASK_ID).toInt(),
            )

        val period = serverRequest.awaitBody(RequestCreateTaskSchedule::class)

        taskScheduleService.deleteTaskScheduleByTaskIdAndPeriod(userId, taskId, period)

        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun updateTaskSchedule(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val requestUpdateTaskSchedule = serverRequest.awaitBody(TaskScheduleDto::class)

        val updatedTaskSchedule = taskScheduleService.updateTaskSchedule(userId, requestUpdateTaskSchedule)

        return ServerResponse.ok().bodyValueAndAwait(updatedTaskSchedule)
    }
}
