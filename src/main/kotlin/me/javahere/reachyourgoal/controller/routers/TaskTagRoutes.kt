package me.javahere.reachyourgoal.controller.routers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.javahere.reachyourgoal.controller.handler.task.TaskTagRoutesHandler
import me.javahere.reachyourgoal.controller.routers.TaskRoutes.Companion.TASK_ID
import me.javahere.reachyourgoal.domain.dto.TaskTagDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskTag
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskTagRoutes(
    private val taskTagRoutesHandler: TaskTagRoutesHandler,
) {
    companion object {
        const val TASK_TAG_ID = "taskTagId"
    }

    @Bean
    fun taskTagsOpenApi(): GroupedOpenApi {
        val paths = arrayOf("/task-tags/**")
        return GroupedOpenApi
            .builder()
            .group("task-tags")
            .pathsToMatch(*paths)
            .build()
    }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "createTaskTag",
                summary = "create task tag",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = RequestCreateTaskTag::class))],
                    ),
                responses = [
                    ApiResponse(
                        description = "created task tag",
                        responseCode = "201",
                        content = [Content(schema = Schema(implementation = TaskTagDto::class))],
                    ),
                ],
            ),
    )
    fun createTaskTag() =
        coRouter {
            POST("/task-tags", taskTagRoutesHandler::createTaskTag)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getAllTagsByUserId",
                summary = "get all tags by userId",
                responses = [
                    ApiResponse(
                        description = "all tags by userId",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = TaskTagDto::class))],
                    ),
                ],
            ),
    )
    fun getAllTagsByUserId() =
        coRouter {
            GET("/task-tags", taskTagRoutesHandler::getAllTagsByUserId)
        }

    @Bean
    @RouterOperation(
        operation =
            Operation(
                operationId = "deleteAllTagsByUserId",
                summary = "delete all tags by userId",
                responses = [
                    ApiResponse(
                        description = "all tags deleted",
                        responseCode = "204",
                    ),
                ],
            ),
    )
    fun deleteAllTagsByUserId() =
        coRouter {
            DELETE("/task-tags", taskTagRoutesHandler::deleteAllTagsByUserId)
        }

    @Bean
    @RouterOperation(
        operation =
            Operation(
                operationId = "deleteTagById",
                summary = "delete tag by id",
                parameters = [
                    Parameter(name = TASK_TAG_ID, `in` = ParameterIn.PATH),
                ],
                responses = [
                    ApiResponse(
                        description = "tag deleted",
                        responseCode = "204",
                    ),
                ],
            ),
    )
    fun deleteTagById() =
        coRouter {
            DELETE("/task-tags/{$TASK_TAG_ID}", taskTagRoutesHandler::deleteTagById)
        }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "updateTag",
                summary = "update tag",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = TaskTagDto::class))],
                    ),
                responses = [
                    ApiResponse(
                        description = "updated tag",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = TaskTagDto::class))],
                    ),
                ],
            ),
    )
    fun updateTag() =
        coRouter {
            PUT("/task-tags", taskTagRoutesHandler::updateTag)
        }

    @Bean
    @RouterOperation(
        operation =
            Operation(
                operationId = "attachTagToTask",
                summary = "attach tag to task",
                parameters = [
                    Parameter(name = TASK_ID, `in` = ParameterIn.QUERY),
                    Parameter(name = TASK_TAG_ID, `in` = ParameterIn.QUERY),
                ],
                responses = [
                    ApiResponse(
                        description = "task and tag connected",
                        responseCode = "201",
                    ),
                ],
            ),
    )
    fun attachTagToTask() =
        coRouter {
            POST("/task-tags/attach-tag-to-task", taskTagRoutesHandler::attachTagToTask)
        }

    @Bean
    @RouterOperation(
        operation =
            Operation(
                operationId = "detachTagFromTask",
                summary = "detach tag from task",
                parameters = [
                    Parameter(name = TASK_ID, `in` = ParameterIn.QUERY),
                    Parameter(name = TASK_TAG_ID, `in` = ParameterIn.QUERY),
                ],
                responses = [
                    ApiResponse(
                        description = "task and tag connected",
                        responseCode = "201",
                    ),
                ],
            ),
    )
    fun detachTagFromTask() =
        coRouter {
            DELETE("/task-tags/detach-tag-from-task", taskTagRoutesHandler::detachTagFromTask)
        }
}
