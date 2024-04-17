package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskSchedule
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskScheduleRepository : CoroutineCrudRepository<TaskSchedule, Int> {
    fun findAllByTaskIdAndTaskPlanId(
        taskId: Int,
        planId: Int,
    ): Flow<TaskSchedule>
}
