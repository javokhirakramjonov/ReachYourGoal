package me.javahere.reachyourgoal.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "task_attachments")
data class TaskAttachment(
    @Id
    @Column("id")
    val id: UUID? = null,

    @Column("name")
    val name: String,
    @Column("address")
    val address: String,

    @Column("task_id")
    val taskId: UUID,
)