package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedule
import me.javahere.reachyourgoal.domain.dto.request.RequestGetTaskSchedule
import me.javahere.reachyourgoal.domain.exception.RYGException
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
        userId: Int,
        taskId: Int,
        requestCreateTaskSchedule: RequestCreateTaskSchedule,
    ): Flow<TaskScheduleDto> {
        taskService.validateTaskExistence(taskId, userId)

        val taskSchedule = requestCreateTaskSchedule.transform(taskId)

        return taskScheduleRepository.saveAll(taskSchedule).transformCollection()
    }

    override suspend fun getTaskScheduleForTaskAndPeriod(
        userId: Int,
        taskId: Int,
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
        userId: Int,
        taskScheduleDto: TaskScheduleDto,
    ): TaskScheduleDto {
        validateTaskScheduleExistence(taskScheduleDto.scheduleId, userId)

        val newTaskSchedule = taskScheduleDto.transform()

        return taskScheduleRepository.save(newTaskSchedule).transform()
    }

    override suspend fun deleteTaskScheduleByTaskIdAndPeriod(
        userId: Int,
        taskId: Int,
        taskSchedule: RequestCreateTaskSchedule,
    ) {
        taskService.validateTaskExistence(taskId, userId)

        val dateTimes = taskSchedule.transform(taskId).map { it.taskDateTime }

        taskScheduleRepository.deleteAllByTaskIdAndTaskDateTimeIn(taskId, dateTimes)
    }

    override suspend fun validateTaskScheduleExistence(
        taskScheduleId: Int,
        userId: Int,
    ): TaskScheduleDto {
        val taskSchedule =
            taskScheduleRepository.findById(taskScheduleId)
                ?: throw RYGException("TaskSchedule(id = $taskScheduleId) not found for user(id = $userId)")

        taskService.validateTaskExistence(taskSchedule.taskId, userId)

        return taskSchedule.transform()
    }
}
