package me.javahere.reachyourgoal.service

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedule
import me.javahere.reachyourgoal.domain.dto.request.RequestGetTaskSchedule
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskScheduleId
import me.javahere.reachyourgoal.domain.id.UserId

interface TaskScheduleService {
    suspend fun createTaskSchedule(
        userId: UserId,
        taskId: TaskId,
        requestCreateTaskSchedule: RequestCreateTaskSchedule,
    ): Flow<TaskScheduleDto>

    suspend fun getTaskScheduleForTaskAndPeriod(
        userId: UserId,
        taskId: TaskId,
        taskSchedule: RequestGetTaskSchedule,
    ): Flow<TaskScheduleDto>

    suspend fun updateTaskSchedule(
        userId: UserId,
        taskScheduleDto: TaskScheduleDto,
    ): TaskScheduleDto

    suspend fun deleteTaskScheduleByTaskIdAndPeriod(
        userId: UserId,
        taskId: TaskId,
        taskSchedule: RequestCreateTaskSchedule,
    )

    suspend fun validateTaskScheduleExistence(
        taskScheduleId: TaskScheduleId,
        userId: UserId,
    ): TaskScheduleDto
}
