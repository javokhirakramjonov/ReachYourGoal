package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.UserId
import me.javahere.reachyourgoal.domain.transformCollection
import me.javahere.reachyourgoal.repository.TaskRepository
import me.javahere.reachyourgoal.service.TaskAttachmentService
import me.javahere.reachyourgoal.service.TaskService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    @Lazy
    private val taskAttachmentService: TaskAttachmentService,
) : TaskService {
    override suspend fun createTask(
        requestCreateTask: RequestCreateTask,
        userId: UserId,
    ): TaskDto {
        return taskRepository
            .save(requestCreateTask.transform(userId))
            .transform()
    }

    override suspend fun getTaskById(
        taskId: TaskId,
        userId: UserId,
    ): TaskDto {
        return validateTaskExistence(taskId, userId)
    }

    override suspend fun getAllTasksByUserId(userId: UserId): Flow<TaskDto> {
        return taskRepository
            .findAllByUserId(userId.value)
            .transformCollection()
    }

    override suspend fun updateTask(
        task: TaskDto,
        userId: UserId,
    ): TaskDto {
        validateTaskExistence(task.id, userId)

        return taskRepository
            .save(task.transform(userId))
            .transform()
    }

    @Transactional(rollbackFor = [RYGException::class])
    override suspend fun deleteTaskById(
        taskId: TaskId,
        userId: UserId,
    ) {
        validateTaskExistence(taskId, userId)

        taskRepository.deleteById(taskId.value)

        taskAttachmentService.deleteAllTaskAttachmentsByTaskId(taskId, userId)
    }

    override suspend fun validateTaskExistence(
        taskId: TaskId,
        userId: UserId,
    ): TaskDto {
        val task =
            taskRepository
                .findByIdAndUserId(taskId.value, userId.value)
                ?: throw RYGException("Task(id = $taskId) not found for user(userId = $userId)")

        return task.transform()
    }
}
