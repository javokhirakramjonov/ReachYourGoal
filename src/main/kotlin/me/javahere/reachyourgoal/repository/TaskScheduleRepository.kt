package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.TaskSchedule
import me.javahere.reachyourgoal.domain.id.TaskId
import me.javahere.reachyourgoal.domain.id.TaskScheduleId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime

interface TaskScheduleRepository : CoroutineCrudRepository<TaskSchedule, TaskScheduleId> {
    fun findAllByTaskIdAndTaskDateTimeBetween(
        taskId: TaskId,
        fromDateTime: LocalDateTime,
        toDateTime: LocalDateTime,
    ): Flow<TaskSchedule>

    suspend fun deleteAllByTaskIdAndTaskDateTimeIn(
        taskId: TaskId,
        taskDateTimes: List<LocalDateTime>,
    )
}
