package me.javahere.reachyourgoal.dao

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.TaskScheduling
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime
import java.util.UUID

interface TaskSchedulingDao : CoroutineCrudRepository<TaskScheduling, Long> {
    fun findByTaskIdAndTaskDateTimeBetween(
        taskId: UUID,
        fromDateTime: LocalDateTime,
        toDateTime: LocalDateTime,
    ): Flow<TaskScheduling>

    suspend fun deleteByTaskIdAndTaskDateTimeIn(
        taskId: UUID,
        dateTimes: List<LocalDateTime>,
    )
}
