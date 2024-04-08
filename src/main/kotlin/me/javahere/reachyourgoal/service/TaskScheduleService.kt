package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedule
import me.javahere.reachyourgoal.domain.dto.request.RequestGetTaskSchedule

interface TaskScheduleService {
    suspend fun createTaskSchedule(
        userId: Int,
        taskId: Int,
        requestCreateTaskSchedule: RequestCreateTaskSchedule,
    ): Flow<TaskScheduleDto>

    suspend fun getTaskScheduleForTaskAndPeriod(
        userId: Int,
        taskId: Int,
        taskSchedule: RequestGetTaskSchedule,
    ): Flow<TaskScheduleDto>

    suspend fun updateTaskSchedule(
        userId: Int,
        taskScheduleDto: TaskScheduleDto,
    ): TaskScheduleDto

    suspend fun deleteTaskScheduleByTaskIdAndPeriod(
        userId: Int,
        taskId: Int,
        taskSchedule: RequestCreateTaskSchedule,
    )

    suspend fun validateTaskScheduleExistence(
        taskScheduleId: Int,
        userId: Int,
    ): TaskScheduleDto
}
