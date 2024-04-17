package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskSchedule
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import me.javahere.reachyourgoal.domain.id.TaskScheduleId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskScheduleRepository : CoroutineCrudRepository<TaskSchedule, TaskScheduleId> {
    fun findAllByTaskIdAndTaskPlanId(
        taskId: TaskId,
        planId: TaskPlanId,
    ): Flow<TaskSchedule>
}
