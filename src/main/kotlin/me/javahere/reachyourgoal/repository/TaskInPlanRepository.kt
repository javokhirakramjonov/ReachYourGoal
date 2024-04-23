package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskInPlan
import me.javahere.reachyourgoal.domain.entity.TaskInPlanId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskInPlanRepository : CoroutineCrudRepository<TaskInPlan, TaskInPlanId> {
    fun findAllByPlanId(planId: Int): Flow<TaskInPlan>
}
