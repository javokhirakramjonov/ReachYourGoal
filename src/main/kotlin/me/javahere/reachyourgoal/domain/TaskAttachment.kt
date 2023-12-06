package me.javahere.reachyourgoal.domain

import jakarta.persistence.*
import java.util.*

@Entity(name = "task_attachments")
data class TaskAttachment(
    @Id
    @Column(name = "id")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name")
    val name: String,
    @Column(name = "address")
    val address: String,

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE]
    )
    @JoinColumn(name = "task_id")
    val task: Task
)