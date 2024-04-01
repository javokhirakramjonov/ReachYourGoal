package me.javahere.reachyourgoal.domain

import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import me.javahere.reachyourgoal.util.Transformable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "task_schedules")
data class TaskSchedule(
    @Id
    @Column("id")
    val scheduleId: Long = 0,
    @Column("task_id")
    val taskId: UUID,
    @Column("task_date_time")
    val taskDateTime: LocalDateTime,
    @Column("task_status")
    val taskStatus: TaskStatus = TaskStatus.NOT_STARTED,
) : Transformable<TaskScheduleDto> {
    override fun transform(): TaskScheduleDto {
        return TaskScheduleDto(
            scheduleId = scheduleId,
            taskId = taskId,
            taskDateTime = taskDateTime,
            taskStatus = taskStatus,
        )
    }
}
