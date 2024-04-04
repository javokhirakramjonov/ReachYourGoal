package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.transformCollection
import me.javahere.reachyourgoal.repository.TaskRepository
import me.javahere.reachyourgoal.service.TaskAttachmentService
import me.javahere.reachyourgoal.service.TaskCategoryService
import me.javahere.reachyourgoal.service.TaskService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskServiceImpl(
    private val taskCategoryService: TaskCategoryService,
    private val taskRepository: TaskRepository,
    @Lazy
    private val taskAttachmentService: TaskAttachmentService,
) : TaskService {
    override suspend fun createTask(
        requestCreateTask: RequestCreateTask,
        userId: Int,
    ): TaskDto {
        taskCategoryService.validateTaskCategoryExistence(requestCreateTask.categoryId, userId)

        return taskRepository
            .save(requestCreateTask.transform())
            .transform()
    }

    override suspend fun getTaskById(
        taskId: Int,
        userId: Int,
    ): TaskDto {
        return validateTaskExistence(taskId, userId)
    }

    override suspend fun getAllTasksByCategoryId(
        categoryId: Int,
        userId: Int,
    ): Flow<TaskDto> {
        taskCategoryService.validateTaskCategoryExistence(categoryId, userId)

        return taskRepository
            .findAllByCategoryId(categoryId)
            .transformCollection()
    }

    override suspend fun updateTask(
        task: TaskDto,
        userId: Int,
    ): TaskDto {
        validateTaskExistence(task.id, userId)

        return taskRepository
            .save(task.transform())
            .transform()
    }

    @Transactional(rollbackFor = [RYGException::class])
    override suspend fun deleteTaskById(
        taskId: Int,
        userId: Int,
    ) {
        validateTaskExistence(taskId, userId)

        taskRepository.deleteById(taskId)

        taskAttachmentService.deleteAllTaskAttachmentsByTaskId(taskId, userId)
    }

    override suspend fun validateTaskExistence(
        taskId: Int,
        userId: Int,
    ): TaskDto {
        val task =
            taskRepository
                .findById(taskId)
                ?: throw RYGException("Task(id = $taskId) not found for user(userId = $userId)")

        taskCategoryService.validateTaskCategoryExistence(task.categoryId, userId)

        return task.transform()
    }
}
