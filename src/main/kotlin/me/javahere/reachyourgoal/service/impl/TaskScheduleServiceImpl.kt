package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskSchedules
import me.javahere.reachyourgoal.domain.dto.request.RequestDeleteTaskSchedules
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateTaskSchedules
import me.javahere.reachyourgoal.domain.entity.TaskSchedule
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import me.javahere.reachyourgoal.domain.id.TaskScheduleId
import me.javahere.reachyourgoal.domain.id.UserId
import me.javahere.reachyourgoal.repository.TaskScheduleRepository
import me.javahere.reachyourgoal.service.TaskPlanService
import me.javahere.reachyourgoal.service.TaskScheduleService
import me.javahere.reachyourgoal.service.TaskService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskScheduleServiceImpl(
    private val taskService: TaskService,
    private val taskScheduleRepository: TaskScheduleRepository,
    private val taskPlanService: TaskPlanService,
) : TaskScheduleService {
    @Transactional
    override suspend fun createTaskSchedules(
        userId: UserId,
        requestCreateTaskSchedules: RequestCreateTaskSchedules,
    ): Flow<TaskScheduleDto> {
        taskService.validateTaskExistence(requestCreateTaskSchedules.taskId, userId)
        taskPlanService.validateTaskPlanExistence(requestCreateTaskSchedules.planId, userId)

        val taskSchedules =
            requestCreateTaskSchedules
                .taskDates
                .map {
                    TaskSchedule(
                        taskId = requestCreateTaskSchedules.taskId.value,
                        taskPlanId = requestCreateTaskSchedules.planId,
                        taskDate = it,
                    )
                }

        return taskScheduleRepository
            .saveAll(taskSchedules)
            .map(TaskSchedule::transform)
    }

    override suspend fun getTaskSchedules(
        userId: UserId,
        taskId: TaskId,
        planId: TaskPlanId,
    ): Flow<TaskScheduleDto> {
        taskService.validateTaskExistence(taskId, userId)
        taskPlanService.validateTaskPlanExistence(planId, userId)

        return taskScheduleRepository
            .findAllByTaskIdAndTaskPlanId(taskId.value, planId.value)
            .map(TaskSchedule::transform)
    }

    @Transactional
    override suspend fun updateTaskSchedules(
        userId: UserId,
        requestUpdateTaskSchedules: RequestUpdateTaskSchedules,
    ): Flow<TaskScheduleDto> {
        return coroutineScope {
            requestUpdateTaskSchedules
                .taskSchedules
                .asFlow()
                .map { taskSchedule ->
                    async {
                        val taskScheduleEntity =
                            validateTaskScheduleExistence(
                                userId,
                                taskSchedule.scheduleId,
                            )
                                .transform()
                                .copy(
                                    taskStatus = taskSchedule.taskStatus,
                                )

                        taskScheduleRepository
                            .save(taskScheduleEntity)
                            .transform()
                    }
                }
                .map { it.await() }
        }
    }

    @Transactional
    override suspend fun deleteTaskSchedules(
        userId: UserId,
        requestDeleteTaskSchedules: RequestDeleteTaskSchedules,
    ) {
        coroutineScope {
            requestDeleteTaskSchedules
                .taskScheduleIds
                .map { taskSchedule ->
                    async {
                        val taskScheduleEntity =
                            validateTaskScheduleExistence(
                                userId,
                                taskSchedule,
                            )

                        taskScheduleRepository.deleteById(taskScheduleEntity.scheduleId.value)
                    }
                }
                .awaitAll()
        }
    }

    override suspend fun validateTaskScheduleExistence(
        userId: UserId,
        taskScheduleId: TaskScheduleId,
    ): TaskScheduleDto {
        return taskScheduleRepository
            .findById(taskScheduleId.value)
            ?.takeIf { foundTaskSchedule ->
                taskService.validateTaskExistence(TaskId(foundTaskSchedule.taskId), userId)
                taskPlanService.validateTaskPlanExistence(foundTaskSchedule.taskPlanId, userId)
                true
            }
            ?.transform()
            ?: throw RYGException("Task schedule(id = $taskScheduleId) not found for user(id = $userId)")
    }
}
