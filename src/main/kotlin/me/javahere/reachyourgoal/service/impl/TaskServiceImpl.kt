package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.datasource.TaskDataSource
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import me.javahere.reachyourgoal.exception.ReachYourGoalExceptionType
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.util.transformCollection
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskServiceImpl(
    @Qualifier("taskDataSourceImpl") private val taskDataSource: TaskDataSource
) : TaskService {

    override suspend fun createTask(task: RequestTaskCreate, userId: UUID): TaskDto {
        return taskDataSource
            .createTask(task.transform(userId))
            .transform()
    }

    override suspend fun getTaskByTaskIdAndUserId(id: UUID, userId: UUID): TaskDto {
        return taskDataSource
            .retrieveTaskByTaskIdAndUserId(id, userId)
            ?.transform()
            ?: throw ReachYourGoalException(ReachYourGoalExceptionType.NotFound("There is no task with id: $id assigned to user with id: $userId"))
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
