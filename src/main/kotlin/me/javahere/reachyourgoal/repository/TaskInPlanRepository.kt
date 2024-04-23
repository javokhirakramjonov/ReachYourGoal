package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskInPlan
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskInPlanRepository : CoroutineCrudRepository<TaskInPlan, Long> {
    fun findAllByPlanId(planId: Int): Flow<TaskInPlan>

    suspend fun deleteByTaskIdAndPlanId(
        taskId: Int,
        planId: Int,
    )
}
