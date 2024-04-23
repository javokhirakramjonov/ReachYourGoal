package me.javahere.reachyourgoal.controller.handler.task

import me.javahere.reachyourgoal.controller.routers.TaskPlanRoutes.Companion.TASK_PLAN_ID
import me.javahere.reachyourgoal.domain.dto.TaskPlanDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskAndPlan
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskPlan
import me.javahere.reachyourgoal.service.TaskPlanService
import me.javahere.reachyourgoal.util.extensions.RouteHandlerUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class TaskPlanRoutesHandler(
    private val taskPlanService: TaskPlanService,
    private val routeHandlerUtils: RouteHandlerUtils,
) {
    suspend fun createTaskPlan(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskPlan = serverRequest.awaitBody(RequestCreateTaskPlan::class)

        val createdTaskPlan = taskPlanService.createTaskPlan(userId, taskPlan)

        return ServerResponse
            .status(HttpStatus.CREATED)
            .bodyValueAndAwait(createdTaskPlan)
    }

    suspend fun addTaskToPlan(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskAndPlan = serverRequest.awaitBody(RequestCreateTaskAndPlan::class)

        val addedTaskToPlan = taskPlanService.addTaskToPlan(userId, taskAndPlan)

        return ServerResponse
            .status(HttpStatus.CREATED)
            .bodyValueAndAwait(addedTaskToPlan)
    }

    suspend fun getTaskPlans(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskPlans = taskPlanService.getTaskPlans(userId)

        return ServerResponse.ok().bodyAndAwait(taskPlans)
    }

    suspend fun getTaskPlanById(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)
        val taskPlanId = serverRequest.pathVariable(TASK_PLAN_ID).toInt()

        val taskPlan = taskPlanService.validateTaskPlanExistence(taskPlanId, userId)

        return ServerResponse.ok().bodyValueAndAwait(taskPlan)
    }

    suspend fun getTasksByPlanId(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)
        val planId = serverRequest.pathVariable(TASK_PLAN_ID).toInt()

        val tasksByPlanId = taskPlanService.getTasksByPlanId(planId, userId)

        return ServerResponse.ok().bodyAndAwait(tasksByPlanId)
    }

    suspend fun updateTaskPlan(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskPlan = serverRequest.awaitBody(TaskPlanDto::class)

        val updatedTaskPlan = taskPlanService.updateTaskPlan(userId, taskPlan)

        return ServerResponse.ok().bodyValueAndAwait(updatedTaskPlan)
    }

    suspend fun deleteTaskPlan(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskPlanId = serverRequest.pathVariable(TASK_PLAN_ID).toInt()

        taskPlanService.deleteTaskPlan(userId, taskPlanId)

        return ServerResponse.noContent().buildAndAwait()
    }
}
