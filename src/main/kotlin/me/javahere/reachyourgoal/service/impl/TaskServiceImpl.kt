package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.datasource.TaskDataSource
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.exception.*
import me.javahere.reachyourgoal.localize.MessagesEnum
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.util.getMessage
import me.javahere.reachyourgoal.util.transformCollection
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskServiceImpl(
    private val taskDataSource: TaskDataSource,
    private val messageSource: ResourceBundleMessageSource
) : TaskService {

    override suspend fun createTask(task: RequestTaskCreate, userId: UUID): TaskDto {
        return taskDataSource
            .createTask(task.transform(userId))
            .transform()
    }

    override suspend fun getTaskByTaskIdAndUserId(id: UUID, userId: UUID): TaskDto {
        val errorMessageArguments = arrayOf(id, userId)
        val errorMessage = messageSource.getMessage(
            MessagesEnum.TASK_NOT_FOUND_EXCEPTION.key,
            *errorMessageArguments
        )

        return taskDataSource
            .retrieveTaskByTaskIdAndUserId(id, userId)
            ?.transform()
            ?: throw ExceptionResponse(
                ReachYourGoalException(
                    ReachYourGoalExceptionType.NOT_FOUND,
                    errorMessage
                )
            )
    }

    override fun getAllTasksByUserId(userId: UUID): Flow<TaskDto> {
        return taskDataSource
            .retrieveAllTasksByUserId(userId)
            .transformCollection()
    }

    override suspend fun updateTask(task: TaskDto): TaskDto {
        return taskDataSource
            .updateTask(task.transform())
            .transform()
    }

    override suspend fun deleteTaskByTaskIdAndUserId(taskId: UUID, userId: UUID) {
        taskDataSource.deleteTaskByTaskIdAndUserId(taskId, userId)
    }
}
