package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import me.javahere.reachyourgoal.domain.dto.TaskCategoryDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskCategory
import me.javahere.reachyourgoal.domain.entity.TaskCategory
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.transformCollection
import me.javahere.reachyourgoal.repository.TaskCategoryRepository
import me.javahere.reachyourgoal.service.TaskCategoryService
import org.springframework.stereotype.Service

@Service
class TaskCategoryServiceImpl(
    private val taskCategoryRepository: TaskCategoryRepository,
) : TaskCategoryService {
    companion object {
        private const val DEFAULT_CATEGORY_NAME = "Default"
    }

    override suspend fun validateTaskCategoryExistence(
        taskCategoryId: Int,
        userId: Int,
    ): TaskCategoryDto {
        return getCategoryById(taskCategoryId, userId)
            .firstOrNull()
            ?: throw RYGException("Task category(id = $taskCategoryId) not found for user(userId = $userId)")
    }

    override suspend fun createTaskCategory(
        requestCreateTaskCategory: RequestCreateTaskCategory,
        userId: Int,
    ): TaskCategoryDto {
        val taskCategory = requestCreateTaskCategory.transform(userId)

        return taskCategoryRepository.save(taskCategory).transform()
    }

    override suspend fun getAllCategoriesByUserId(userId: Int): Flow<TaskCategoryDto> {
        return taskCategoryRepository
            .findAllByUserId(userId)
            .transformCollection()
    }

    override suspend fun deleteCategoryById(
        categoryId: Int,
        userId: Int,
    ) {
        taskCategoryRepository.deleteByIdAndUserId(categoryId, userId)
    }

    override suspend fun updateCategory(
        taskCategoryDto: TaskCategoryDto,
        userId: Int,
    ): TaskCategoryDto {
        validateTaskCategoryExistence(taskCategoryDto.id, userId)

        val taskCategory = taskCategoryDto.transform(userId)

        return taskCategoryRepository.save(taskCategory).transform()
    }

    override suspend fun getCategoryById(
        categoryId: Int,
        userId: Int,
    ): Flow<TaskCategoryDto> {
        return taskCategoryRepository.findByIdAndUserId(categoryId, userId).transformCollection()
    }

    override suspend fun createDefaultCategoryForUser(userId: Int) {
        val taskCategory =
            TaskCategory(
                name = DEFAULT_CATEGORY_NAME,
                userId = userId,
            )

        taskCategoryRepository.save(taskCategory)
    }
}
