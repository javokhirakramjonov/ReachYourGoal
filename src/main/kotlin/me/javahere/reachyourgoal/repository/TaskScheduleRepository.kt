package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskSchedule
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime

interface TaskScheduleRepository : CoroutineCrudRepository<TaskSchedule, Int> {
    fun findAllByTaskIdAndTaskDateTimeBetween(
        taskId: Int,
        fromDateTime: LocalDateTime,
        toDateTime: LocalDateTime,
    ): Flow<TaskSchedule>

    suspend fun deleteAllByTaskIdAndTaskDateTimeIn(
        taskId: Int,
        taskDateTimes: List<LocalDateTime>,
    )
}
