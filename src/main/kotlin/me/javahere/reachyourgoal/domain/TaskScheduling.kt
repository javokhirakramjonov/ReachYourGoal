package me.javahere.reachyourgoal.domain

import me.javahere.reachyourgoal.domain.dto.TaskSchedulingDto
import me.javahere.reachyourgoal.util.Transformable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "task_scheduling")
data class TaskScheduling(
    @Id
    @Column("id")
    val schedulingId: Long = 0,
    @Column("task_id")
    val taskId: UUID,
    @Column("task_date_time")
    val taskDateTime: LocalDateTime,
    @Column("task_status")
    val taskStatus: TaskStatus = TaskStatus.NOT_STARTED,
) : Transformable<TaskSchedulingDto> {
    override fun transform(): TaskSchedulingDto {
        return TaskSchedulingDto(
            schedulingId = schedulingId,
            taskId = taskId,
            taskDateTime = taskDateTime,
            taskStatus = taskStatus,
        )
    }
}
