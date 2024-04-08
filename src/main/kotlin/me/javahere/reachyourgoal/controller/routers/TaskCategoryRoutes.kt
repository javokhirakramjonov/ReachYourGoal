package me.javahere.reachyourgoal.controller.routers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import me.javahere.reachyourgoal.controller.handler.task.TaskCategoryRoutesHandler
import me.javahere.reachyourgoal.domain.dto.TaskCategoryDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskCategory
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TaskCategoryRoutes(
    private val taskCategoryRoutesHandler: TaskCategoryRoutesHandler,
) {
    companion object {
        const val TASK_CATEGORY_ID = "categoryId"
    }

    @Bean
    fun taskCategoryOpenApi(): GroupedOpenApi {
        val paths = arrayOf("/task-categories/**")
        return GroupedOpenApi
            .builder()
            .group("task-categories")
            .pathsToMatch(*paths)
            .build()
    }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "createTaskCategory",
                summary = "create task category",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = RequestCreateTaskCategory::class))],
                    ),
                responses = [
                    ApiResponse(
                        description = "created task category",
                        responseCode = "201",
                        content = [Content(schema = Schema(implementation = TaskCategoryDto::class))],
                    ),
                ],
            ),
    )
    fun createTaskCategory() =
        coRouter {
            POST("/task-categories", taskCategoryRoutesHandler::createTaskCategory)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getTaskCategoriesByUserId",
                summary = "get task categories by userId",
                responses = [
                    ApiResponse(
                        description = "task categories",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = TaskCategoryDto::class))],
                    ),
                ],
            ),
    )
    fun getTaskCategoriesByUserId() =
        coRouter {
            GET("/task-categories", taskCategoryRoutesHandler::getAllCategoriesByUserId)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "deleteTaskCategory",
                summary = "delete task category",
                parameters = [
                    Parameter(name = TASK_CATEGORY_ID, `in` = ParameterIn.PATH),
                ],
                responses = [
                    ApiResponse(
                        description = "deleted task category",
                        responseCode = "204",
                    ),
                ],
            ),
    )
    fun deleteTaskCategory() =
        coRouter {
            DELETE("/task-categories/{$TASK_CATEGORY_ID}", taskCategoryRoutesHandler::deleteCategoryById)
        }

    @Bean
    @RouterOperation(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "updateTaskCategory",
                summary = "update task category",
                requestBody =
                    RequestBody(
                        content = [Content(schema = Schema(implementation = TaskCategoryDto::class))],
                    ),
                responses = [
                    ApiResponse(
                        description = "updated task category",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = TaskCategoryDto::class))],
                    ),
                ],
            ),
    )
    fun updateTaskCategory() =
        coRouter {
            PUT("/task-categories", taskCategoryRoutesHandler::updateCategory)
        }

    @Bean
    @RouterOperation(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        operation =
            Operation(
                operationId = "getTaskCategoryById",
                summary = "get task category by id",
                parameters = [
                    Parameter(name = TASK_CATEGORY_ID, `in` = ParameterIn.PATH),
                ],
                responses = [
                    ApiResponse(
                        description = "task category",
                        responseCode = "200",
                        content = [Content(schema = Schema(implementation = TaskCategoryDto::class))],
                    ),
                ],
            ),
    )
    fun getTaskCategoryById() =
        coRouter {
            GET("/task-categories/{$TASK_CATEGORY_ID}", taskCategoryRoutesHandler::getCategoryById)
        }
}
