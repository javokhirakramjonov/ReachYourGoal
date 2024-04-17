package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedule
import me.javahere.reachyourgoal.domain.dto.request.RequestGetTaskSchedule
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskScheduleId
import me.javahere.reachyourgoal.domain.id.UserId
import me.javahere.reachyourgoal.domain.transformCollection
import me.javahere.reachyourgoal.repository.TaskScheduleRepository
import me.javahere.reachyourgoal.service.TaskScheduleService
import me.javahere.reachyourgoal.service.TaskService
import org.springframework.stereotype.Service

@Service
class TaskScheduleServiceImpl(
    private val taskService: TaskService,
    private val taskScheduleRepository: TaskScheduleRepository,
) : TaskScheduleService {
    override suspend fun createTaskSchedule(
        userId: UserId,
        taskId: TaskId,
        requestCreateTaskSchedule: RequestCreateTaskSchedule,
    ): Flow<TaskScheduleDto> {
        taskService.validateTaskExistence(taskId, userId)

        val taskSchedule = requestCreateTaskSchedule.transform(taskId)

        return taskScheduleRepository.saveAll(taskSchedule).transformCollection()
    }

    override suspend fun getTaskScheduleForTaskAndPeriod(
        userId: UserId,
        taskId: TaskId,
        taskSchedule: RequestGetTaskSchedule,
    ): Flow<TaskScheduleDto> {
        taskService.validateTaskExistence(taskId, userId)

        return taskScheduleRepository.findAllByTaskIdAndTaskDateTimeBetween(
            taskId,
            taskSchedule.fromDateTime,
            taskSchedule.toDateTime,
        ).transformCollection()
    }

    override suspend fun updateTaskSchedule(
        userId: UserId,
        taskScheduleDto: TaskScheduleDto,
    ): TaskScheduleDto {
        validateTaskScheduleExistence(taskScheduleDto.scheduleId, userId)

        val newTaskSchedule = taskScheduleDto.transform()

        return taskScheduleRepository.save(newTaskSchedule).transform()
    }

    override suspend fun deleteTaskScheduleByTaskIdAndPeriod(
        userId: UserId,
        taskId: TaskId,
        taskSchedule: RequestCreateTaskSchedule,
    ) {
        taskService.validateTaskExistence(taskId, userId)

        val dateTimes = taskSchedule.transform(taskId).map { it.taskDateTime }

        taskScheduleRepository.deleteAllByTaskIdAndTaskDateTimeIn(taskId, dateTimes)
    }

    override suspend fun validateTaskScheduleExistence(
        taskScheduleId: TaskScheduleId,
        userId: UserId,
    ): TaskScheduleDto {
        val taskSchedule =
            taskScheduleRepository.findById(taskScheduleId)
                ?: throw RYGException("TaskSchedule(id = $taskScheduleId) not found for user(id = $userId)")

        taskService.validateTaskExistence(taskSchedule.taskId, userId)

        return taskSchedule.transform()
    }
}
