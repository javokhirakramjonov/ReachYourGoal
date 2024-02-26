package me.javahere.reachyourgoal.domain

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Table(name = "scheduled_tasks")
class ScheduledTask(
    @Column("task_id")
    val taskId: UUID,
    @Column("task_date")
    val taskDate: LocalDate,
    @Column("task_time")
    val taskTime: LocalTime? = null,
    @Column("task_status")
    val taskStatus: TaskStatus = TaskStatus.NOT_STARTED,
)
