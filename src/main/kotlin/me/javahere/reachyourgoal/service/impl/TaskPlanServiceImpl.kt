package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.javahere.reachyourgoal.domain.dto.TaskPlanDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskPlan
import me.javahere.reachyourgoal.domain.entity.TaskPlan
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import me.javahere.reachyourgoal.domain.id.UserId
import me.javahere.reachyourgoal.repository.TaskPlanRepository
import me.javahere.reachyourgoal.service.TaskPlanService
import org.springframework.stereotype.Service

@Service
class TaskPlanServiceImpl(
    private val taskPlanRepository: TaskPlanRepository,
) : TaskPlanService {
    override suspend fun createTaskPlan(
        userId: UserId,
        requestCreateTaskPlan: RequestCreateTaskPlan,
    ): TaskPlanDto {
        val taskPlan = requestCreateTaskPlan.transform(userId)

        return taskPlanRepository.save(taskPlan).transform()
    }

    override suspend fun getTaskPlans(userId: UserId): Flow<TaskPlanDto> {
        return taskPlanRepository
            .findAllByUserId(userId)
            .map(TaskPlan::transform)
    }

    override suspend fun updateTaskPlan(
        userId: UserId,
        taskPlanDto: TaskPlanDto,
    ): TaskPlanDto {
        val taskPlan = validateTaskPlanExistence(taskPlanDto.id, userId)

        return taskPlanRepository
            .save(
                taskPlan
                    .transform(userId)
                    .copy(
                        name = taskPlanDto.name,
                        description = taskPlanDto.description,
                    ),
            )
            .transform()
    }

    override suspend fun deleteTaskPlan(
        userId: UserId,
        taskPlanId: TaskPlanId,
    ) {
        val taskPlan = validateTaskPlanExistence(taskPlanId, userId)

        taskPlanRepository.delete(taskPlan.transform(userId))
    }

    override suspend fun validateTaskPlanExistence(
        taskPlanId: TaskPlanId,
        userId: UserId,
    ): TaskPlanDto {
        return taskPlanRepository
            .findById(taskPlanId)
            ?.takeIf { it.userId == userId }
            ?.transform()
            ?: throw RYGException("Task plan(id = $taskPlanId) not found for user(id = $userId)")
    }
}
