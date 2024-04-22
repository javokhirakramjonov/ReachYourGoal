package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.javahere.reachyourgoal.domain.dto.TaskAndPlanDto
import me.javahere.reachyourgoal.domain.dto.TaskPlanDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskPlan
import me.javahere.reachyourgoal.domain.entity.TaskPlan
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.transformCollection
import me.javahere.reachyourgoal.repository.TaskAndPlanRepository
import me.javahere.reachyourgoal.repository.TaskPlanRepository
import me.javahere.reachyourgoal.service.TaskPlanService
import me.javahere.reachyourgoal.service.TaskService
import org.springframework.stereotype.Service

@Service
class TaskPlanServiceImpl(
    private val taskService: TaskService,
    private val taskPlanRepository: TaskPlanRepository,
    private val taskAndPlanRepository: TaskAndPlanRepository,
) : TaskPlanService {
    override suspend fun createTaskPlan(
        userId: Int,
        requestCreateTaskPlan: RequestCreateTaskPlan,
    ): TaskPlanDto {
        val taskPlan = requestCreateTaskPlan.transform(userId)

        return taskPlanRepository.save(taskPlan).transform()
    }

    override suspend fun addTaskToPlan(
        userId: Int,
        requestAddTaskToPlan: TaskAndPlanDto,
    ): TaskAndPlanDto {
        validateTaskPlanExistence(requestAddTaskToPlan.planId, userId)
        taskService.validateTaskExistence(requestAddTaskToPlan.taskId, userId)

        return taskAndPlanRepository
            .save(requestAddTaskToPlan.transform())
            .transform()
    }

    override suspend fun getTaskPlans(userId: Int): Flow<TaskPlanDto> {
        return taskPlanRepository
            .findAllByUserId(userId)
            .map(TaskPlan::transform)
    }

    override suspend fun getTasksByPlanId(
        planId: Int,
        userId: Int,
    ): Flow<TaskAndPlanDto> {
        validateTaskPlanExistence(planId, userId)

        return taskAndPlanRepository
            .findAllByPlanId(planId)
            .transformCollection()
    }

    override suspend fun updateTaskPlan(
        userId: Int,
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
        userId: Int,
        taskPlanId: Int,
    ) {
        val taskPlan = validateTaskPlanExistence(taskPlanId, userId)

        taskPlanRepository.delete(taskPlan.transform(userId))
    }

    override suspend fun validateTaskPlanExistence(
        taskPlanId: Int,
        userId: Int,
    ): TaskPlanDto {
        return taskPlanRepository
            .findById(taskPlanId)
            ?.takeIf { it.userId == userId }
            ?.transform()
            ?: throw RYGException("Task plan(id = $taskPlanId) not found for user(id = $userId)")
    }
}
