package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskInPlanDto
import me.javahere.reachyourgoal.domain.dto.TaskPlanDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskInPlan
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskPlan

interface TaskPlanService {
    suspend fun createTaskPlan(
        userId: Int,
        requestCreateTaskPlan: RequestCreateTaskPlan,
    ): TaskPlanDto

    suspend fun addTaskToPlan(
        userId: Int,
        requestAddTaskToPlan: RequestCreateTaskInPlan,
    ): TaskInPlanDto

    suspend fun getTaskPlans(userId: Int): Flow<TaskPlanDto>

    suspend fun getTasksByPlanId(
        planId: Int,
        userId: Int,
    ): Flow<TaskInPlanDto>

    suspend fun updateTaskPlan(
        userId: Int,
        taskPlanDto: TaskPlanDto,
    ): TaskPlanDto

    suspend fun deleteTaskPlan(
        userId: Int,
        taskPlanId: Int,
    )

    suspend fun validateTaskPlanExistence(
        taskPlanId: Int,
        userId: Int,
    ): TaskPlanDto

    suspend fun deleteTaskFromPlan(
        userId: Int,
        taskPlanId: Int,
        taskId: Int,
    )

    suspend fun updateTaskInPlan(
        userId: Int,
        taskInPlan: RequestCreateTaskInPlan,
    ): TaskInPlanDto
}
