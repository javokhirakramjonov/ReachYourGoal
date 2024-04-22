package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskAndPlan
import me.javahere.reachyourgoal.domain.entity.TaskAndPlanId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskAndPlanRepository : CoroutineCrudRepository<TaskAndPlan, TaskAndPlanId> {
    fun findAllByPlanId(planId: Int): Flow<TaskAndPlan>
}
