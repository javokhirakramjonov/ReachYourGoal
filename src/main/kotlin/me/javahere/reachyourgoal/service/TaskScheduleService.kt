package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedules
import me.javahere.reachyourgoal.domain.dto.request.RequestDeleteTaskSchedules
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateTaskSchedules
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import me.javahere.reachyourgoal.domain.id.TaskScheduleId
import me.javahere.reachyourgoal.domain.id.UserId

interface TaskScheduleService {
    suspend fun createTaskSchedules(
        userId: UserId,
        requestCreateTaskSchedules: RequestCreateTaskSchedules,
    ): Flow<TaskScheduleDto>

    suspend fun getTaskSchedules(
        userId: UserId,
        taskId: TaskId,
        planId: TaskPlanId,
    ): Flow<TaskScheduleDto>

    suspend fun updateTaskSchedules(
        userId: UserId,
        requestUpdateTaskSchedules: RequestUpdateTaskSchedules,
    ): Flow<TaskScheduleDto>

    suspend fun deleteTaskSchedules(
        userId: UserId,
        requestDeleteTaskSchedules: RequestDeleteTaskSchedules,
    )

    suspend fun validateTaskScheduleExistence(
        userId: UserId,
        taskScheduleId: TaskScheduleId,
    ): TaskScheduleDto
}
