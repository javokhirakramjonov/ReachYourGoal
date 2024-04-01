package me.javahere.reachyourgoal.dao

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.TaskSchedule
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime
import java.util.UUID

interface TaskScheduleDao : CoroutineCrudRepository<TaskSchedule, Long> {
    fun findByTaskIdAndTaskDateTimeBetween(
        taskId: UUID,
        fromDateTime: LocalDateTime,
        toDateTime: LocalDateTime,
    ): Flow<TaskSchedule>

    suspend fun deleteAllByTaskIdAndTaskDateTimeIn(
        taskId: UUID,
        taskDateTimes: List<LocalDateTime>,
    )

    fun findAllByTaskId(taskId: UUID): Flow<TaskSchedule>
}
