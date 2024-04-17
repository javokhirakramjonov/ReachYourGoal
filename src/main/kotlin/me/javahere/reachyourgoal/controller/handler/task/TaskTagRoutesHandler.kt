package me.javahere.reachyourgoal.controller.handler.task

import me.javahere.reachyourgoal.controller.routers.TaskRoutes.Companion.TASK_ID
import me.javahere.reachyourgoal.controller.routers.TaskTagRoutes.Companion.TASK_TAG_ID
import me.javahere.reachyourgoal.domain.dto.TaskTagDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskTag
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskTagId
import me.javahere.reachyourgoal.service.TaskTagService
import me.javahere.reachyourgoal.util.extensions.RouteHandlerUtils
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class TaskTagRoutesHandler(
    private val taskTagService: TaskTagService,
    private val routeHandlerUtils: RouteHandlerUtils,
) {
    suspend fun createTaskTag(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskTag = serverRequest.awaitBody(RequestCreateTaskTag::class)

        val createdTaskTag = taskTagService.createTaskTag(taskTag, userId)

        return ServerResponse.ok().bodyValueAndAwait(createdTaskTag)
    }

    suspend fun getAllTagsByUserId(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val tags = taskTagService.getAllTagsByUserId(userId)

        return ServerResponse.ok().bodyAndAwait(tags)
    }

    suspend fun updateTag(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskTag = serverRequest.awaitBody(TaskTagDto::class)

        val updatedTaskTag = taskTagService.updateTag(taskTag, userId)

        return ServerResponse.ok().bodyValueAndAwait(updatedTaskTag)
    }

    suspend fun deleteTagById(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val tagId =
            serverRequest
                .pathVariable(TASK_TAG_ID)
                .toInt()
                .let(::TaskTagId)

        taskTagService.deleteTagById(tagId, userId)

        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun deleteAllTagsByUserId(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        taskTagService.deleteAllTagsByUserId(userId)

        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun attachTagToTask(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskId =
            routeHandlerUtils
                .getQueryParamOrThrow(serverRequest, TASK_ID)
                .toInt()
                .let(::TaskId)
        val tagId =
            routeHandlerUtils
                .getQueryParamOrThrow(serverRequest, TASK_TAG_ID)
                .toInt()
                .let(::TaskTagId)

        taskTagService.attachTagToTask(taskId, tagId, userId)

        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun detachTagFromTask(serverRequest: ServerRequest): ServerResponse {
        val userId = routeHandlerUtils.getUserId(serverRequest)

        val taskId =
            routeHandlerUtils
                .getQueryParamOrThrow(serverRequest, TASK_ID)
                .toInt()
                .let(::TaskId)

        val tagId =
            routeHandlerUtils
                .getQueryParamOrThrow(serverRequest, TASK_TAG_ID)
                .toInt()
                .let(::TaskTagId)

        taskTagService.detachTagFromTask(taskId, tagId, userId)

        return ServerResponse.noContent().buildAndAwait()
    }
}
