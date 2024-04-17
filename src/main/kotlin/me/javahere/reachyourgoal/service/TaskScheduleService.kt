package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedules
import me.javahere.reachyourgoal.domain.dto.request.RequestDeleteTaskSchedules
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateTaskSchedules

interface TaskScheduleService {
    suspend fun createTaskSchedules(
        userId: Int,
        requestCreateTaskSchedules: RequestCreateTaskSchedules,
    ): Flow<TaskScheduleDto>

    suspend fun getTaskSchedules(
        userId: Int,
        taskId: Int,
        planId: Int,
    ): Flow<TaskScheduleDto>

    suspend fun updateTaskSchedules(
        userId: Int,
        requestUpdateTaskSchedules: RequestUpdateTaskSchedules,
    ): Flow<TaskScheduleDto>

    suspend fun deleteTaskSchedules(
        userId: Int,
        requestDeleteTaskSchedules: RequestDeleteTaskSchedules,
    )

    suspend fun validateTaskScheduleExistence(
        userId: Int,
        taskScheduleId: Int,
    ): TaskScheduleDto
}
