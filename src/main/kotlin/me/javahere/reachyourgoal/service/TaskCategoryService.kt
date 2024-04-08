package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskCategoryDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskCategory

interface TaskCategoryService {
    suspend fun createTaskCategory(
        requestCreateTaskCategory: RequestCreateTaskCategory,
        userId: Int,
    ): TaskCategoryDto

    suspend fun getAllCategoriesByUserId(userId: Int): Flow<TaskCategoryDto>

    suspend fun deleteCategoryById(
        categoryId: Int,
        userId: Int,
    )

    suspend fun updateCategory(
        taskCategoryDto: TaskCategoryDto,
        userId: Int,
    ): TaskCategoryDto

    suspend fun getCategoryById(
        categoryId: Int,
        userId: Int,
    ): Flow<TaskCategoryDto>

    suspend fun createDefaultCategoryForUser(userId: Int)

    suspend fun validateTaskCategoryExistence(
        taskCategoryId: Int,
        userId: Int,
    ): TaskCategoryDto
}
