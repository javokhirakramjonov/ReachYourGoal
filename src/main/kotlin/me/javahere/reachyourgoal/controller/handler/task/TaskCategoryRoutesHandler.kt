package me.javahere.reachyourgoal.controller.handler.task

import me.javahere.reachyourgoal.controller.routers.TaskCategoryRoutes.Companion.TASK_CATEGORY_ID
import me.javahere.reachyourgoal.domain.dto.TaskCategoryDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskCategory
import me.javahere.reachyourgoal.service.TaskCategoryService
import me.javahere.reachyourgoal.util.extensions.RouteHandlerUtils
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class TaskCategoryRoutesHandler(
    private val taskCategoryService: TaskCategoryService,
    private val routeHandlerUtils: RouteHandlerUtils,
) {
    suspend fun createTaskCategory(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskCategory = serverRequest.awaitBody(RequestCreateTaskCategory::class)

        val createdTaskCategory = taskCategoryService.createTaskCategory(taskCategory, userId)

        return ServerResponse.ok().bodyValueAndAwait(createdTaskCategory)
    }

    suspend fun getAllCategoriesByUserId(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val categories = taskCategoryService.getAllCategoriesByUserId(userId)

        return ServerResponse.ok().bodyAndAwait(categories)
    }

    suspend fun updateCategory(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskCategory = serverRequest.awaitBody(TaskCategoryDto::class)

        val updatedTaskCategory = taskCategoryService.updateCategory(taskCategory, userId)

        return ServerResponse.ok().bodyValueAndAwait(updatedTaskCategory)
    }

    suspend fun deleteCategoryById(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val categoryId = serverRequest.pathVariable(TASK_CATEGORY_ID).toInt()

        taskCategoryService.deleteCategoryById(categoryId, userId)

        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun getCategoryById(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val categoryId = serverRequest.pathVariable(TASK_CATEGORY_ID).toInt()

        val category = taskCategoryService.getCategoryById(categoryId, userId)

        return ServerResponse.ok().bodyValueAndAwait(category)
    }
}
