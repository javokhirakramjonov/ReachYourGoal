package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.TaskScheduleDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "task_schedules")
data class TaskSchedule(
    @Id
    @Column("id")
    val scheduleId: Int = 0,
    @Column("task_id")
    val taskId: Int,
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
