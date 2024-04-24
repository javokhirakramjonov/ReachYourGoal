package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.javahere.reachyourgoal.domain.dto.TaskInPlanDto
import me.javahere.reachyourgoal.domain.dto.TaskPlanDto
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskInPlan
import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTaskPlan
import me.javahere.reachyourgoal.domain.entity.TaskPlan
import me.javahere.reachyourgoal.domain.exception.RYGException
import me.javahere.reachyourgoal.repository.TaskInPlanRepository
import me.javahere.reachyourgoal.repository.TaskPlanRepository
import me.javahere.reachyourgoal.service.TaskPlanService
import me.javahere.reachyourgoal.service.TaskService
import org.springframework.stereotype.Service

@Service
class TaskPlanServiceImpl(
    private val taskService: TaskService,
    private val taskPlanRepository: TaskPlanRepository,
    private val taskInPlanRepository: TaskInPlanRepository,
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
        requestAddTaskToPlan: RequestCreateTaskInPlan,
    ): TaskInPlanDto {
        validateTaskPlanExistence(requestAddTaskToPlan.planId, userId)
        val task = taskService.validateTaskExistence(requestAddTaskToPlan.taskId, userId)

        return taskInPlanRepository
            .save(requestAddTaskToPlan.transform())
            .let {
                TaskInPlanDto(
                    task = task,
                    planId = it.planId,
                    selectedWeekDays = it.selectedWeekDays,
                )
            }
    }

    override suspend fun getTaskPlans(userId: Int): Flow<TaskPlanDto> {
        return taskPlanRepository
            .findAllByUserId(userId)
            .map(TaskPlan::transform)
    }

    override suspend fun getTasksByPlanId(
        planId: Int,
        userId: Int,
    ): Flow<TaskInPlanDto> {
        validateTaskPlanExistence(planId, userId)

        return taskInPlanRepository
            .findAllByPlanId(planId)
            .map { taskInPlan ->
                val task = taskService.validateTaskExistence(taskInPlan.taskId, userId)

                TaskInPlanDto(
                    task = task,
                    planId = taskInPlan.planId,
                    selectedWeekDays = taskInPlan.selectedWeekDays,
                )
            }
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

    override suspend fun deleteTaskFromPlan(
        userId: Int,
        taskPlanId: Int,
        taskId: Int,
    ) {
        validateTaskPlanExistence(taskPlanId, userId)
        taskService.validateTaskExistence(taskId, userId)

        taskInPlanRepository.deleteByTaskIdAndPlanId(taskId, taskPlanId)
    }

    override suspend fun updateTaskInPlan(
        userId: Int,
        taskInPlan: RequestCreateTaskInPlan,
    ): TaskInPlanDto {
        validateTaskPlanExistence(taskInPlan.planId, userId)
        val task = taskService.validateTaskExistence(taskInPlan.taskId, userId)

        val taskInPlanEntity =
            taskInPlanRepository
                .findByTaskIdAndPlanId(taskInPlan.taskId, taskInPlan.planId)
                ?.copy(selectedWeekDays = taskInPlan.selectedWeekDays)
                ?: throw RYGException("Task(id = ${taskInPlan.taskId}) not found in plan(id = ${taskInPlan.planId})")

        return taskInPlanRepository
            .save(taskInPlanEntity)
            .let {
                TaskInPlanDto(
                    task = task,
                    planId = it.planId,
                    selectedWeekDays = it.selectedWeekDays,
                )
            }
    }
}
