package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.dto.TaskDto
import me.javahere.reachyourgoal.dto.request.RequestTaskCreate
import me.javahere.reachyourgoal.repository.TaskRepository
import me.javahere.reachyourgoal.service.TaskService
import me.javahere.reachyourgoal.util.transformCollection
import org.springframework.stereotype.Service
import java.util.*

@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository
) : TaskService {

    override suspend fun createTask(task: RequestTaskCreate, userId: UUID): TaskDto {
        return taskRepository.save(task.transform(userId)).transform()
    }

    override suspend fun getTaskById(id: UUID, userId: UUID): TaskDto? {
        return taskRepository.findTaskByIdAndUserId(id, userId)?.transform()
    }

    override suspend fun getAllTasks(userId: UUID): Flow<TaskDto> {
        return taskRepository.findAllByUserId(userId).transformCollection()
    }

    override suspend fun updateTask(task: TaskDto): TaskDto? {
        if (getTaskById(task.id, task.userId) == null) return null

        return taskRepository.save(task.transform()).transform()
    }
}
