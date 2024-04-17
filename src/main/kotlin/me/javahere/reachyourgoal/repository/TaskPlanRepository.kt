package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskPlan
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskPlanRepository : CoroutineCrudRepository<TaskPlan, Int> {
    fun findAllByUserId(userId: Int): Flow<TaskPlan>
}
