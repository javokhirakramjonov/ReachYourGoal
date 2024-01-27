package me.javahere.reachyourgoal.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "task_assignments")
class TaskAssignment(
    @Id
    @Column("id")
    val id: UUID? = null,
    @Column("task_id")
    val taskId: UUID,
    @Column("task_date_id")
    val taskDateId: UUID,
)
