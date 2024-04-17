package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskPlan
import me.javahere.reachyourgoal.domain.id.TaskPlanId
import me.javahere.reachyourgoal.domain.id.UserId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskPlanRepository : CoroutineCrudRepository<TaskPlan, TaskPlanId> {
    fun findAllByUserId(userId: UserId): Flow<TaskPlan>
}
