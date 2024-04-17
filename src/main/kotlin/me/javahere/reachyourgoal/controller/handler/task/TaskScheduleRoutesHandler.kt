package me.javahere.reachyourgoal.controller.handler.task

import me.javahere.reachyourgoal.controller.routers.TaskPlanRoutes.Companion.TASK_PLAN_ID
import me.javahere.reachyourgoal.controller.routers.TaskRoutes.Companion.TASK_ID
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedules
import me.javahere.reachyourgoal.domain.dto.request.RequestDeleteTaskSchedules
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateTaskSchedules
import me.javahere.reachyourgoal.service.TaskScheduleService
import me.javahere.reachyourgoal.util.extensions.RouteHandlerUtils
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class TaskScheduleRoutesHandler(
    private val taskScheduleService: TaskScheduleService,
    private val routeHandlerUtils: RouteHandlerUtils,
) {
    suspend fun createTaskSchedules(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskSchedules = serverRequest.awaitBody(RequestCreateTaskSchedules::class)

        val createdTaskSchedules = taskScheduleService.createTaskSchedules(userId, taskSchedules)

        return ServerResponse.ok().bodyAndAwait(createdTaskSchedules)
    }

    suspend fun getTaskSchedules(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)
        val taskId =
            routeHandlerUtils
                .getQueryParamOrThrow(serverRequest, TASK_ID)
                .toInt()

        val taskPlanId =
            routeHandlerUtils
                .getQueryParamOrThrow(serverRequest, TASK_PLAN_ID)
                .toInt()

        val taskSchedules = taskScheduleService.getTaskSchedules(userId, taskId, taskPlanId)

        return ServerResponse.ok().bodyAndAwait(taskSchedules)
    }

    suspend fun deleteTaskSchedules(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val requestDeleteTaskSchedules = serverRequest.awaitBody(RequestDeleteTaskSchedules::class)

        taskScheduleService.deleteTaskSchedules(userId, requestDeleteTaskSchedules)

        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun updateTaskSchedules(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val requestUpdateTaskSchedules = serverRequest.awaitBody(RequestUpdateTaskSchedules::class)

        val updatedTaskSchedules = taskScheduleService.updateTaskSchedules(userId, requestUpdateTaskSchedules)

        return ServerResponse.ok().bodyValueAndAwait(updatedTaskSchedules)
    }
}
