package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskInPlan
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskInPlanRepository : CoroutineCrudRepository<TaskInPlan, Int> {
    fun findAllByPlanId(planId: Int): Flow<TaskInPlan>

    suspend fun findByTaskIdAndPlanId(
        taskId: Int,
        planId: Int,
    ): TaskInPlan?

    suspend fun deleteByTaskIdAndPlanId(
        taskId: Int,
        planId: Int,
    )
}
