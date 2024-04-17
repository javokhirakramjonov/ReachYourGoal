package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskPlanDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskPlan
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import me.javahere.reachyourgoal.domain.id.UserId

interface TaskPlanService {
    suspend fun createTaskPlan(
        userId: UserId,
        requestCreateTaskPlan: RequestCreateTaskPlan,
    ): TaskPlanDto

    suspend fun getTaskPlans(userId: UserId): Flow<TaskPlanDto>

    suspend fun updateTaskPlan(
        userId: UserId,
        taskPlanDto: TaskPlanDto,
    ): TaskPlanDto

    suspend fun deleteTaskPlan(
        userId: UserId,
        taskPlanId: TaskPlanId,
    )

    suspend fun validateTaskPlanExistence(
        taskPlanId: TaskPlanId,
        userId: UserId,
    ): TaskPlanDto
}
