package me.javahere.reachyourgoal.dao

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.TaskScheduling
import me.javahere.reachyourgoal.domain.TaskSchedulingId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDate
import java.util.UUID

interface TaskSchedulingDao : CoroutineCrudRepository<TaskScheduling, TaskSchedulingId> {
    fun findByTaskIdAndTaskDateBetween(
        taskId: UUID,
        fromDate: LocalDate,
        toDate: LocalDate,
    ): Flow<TaskScheduling>
}
