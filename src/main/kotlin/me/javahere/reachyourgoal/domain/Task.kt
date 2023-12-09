package me.javahere.reachyourgoal.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "tasks")
data class Task(
    @Id
    @Column("id")
    val id: UUID? = null,

    @Column("name")
    val name: String,
    @Column("description")
    val description: String? = null,
    @Column("spent_time")
    val spentTime: Long = 0L,
    @Column("status")
    val status: TaskStatus = TaskStatus.NOT_STARTED,

    @Column("user_id")
    val userId: UUID,
)