package me.javahere.reachyourgoal.domain

import jakarta.persistence.*
import java.util.*

@Entity(name = "tasks")
data class Task(
    @Id
    @Column(name = "id")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name")
    val name: String,
    @Column(name = "description")
    val description: String? = null,
    @Column(name = "spent_time")
    val spentTime: Long = 0L,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    val status: TaskStatus = TaskStatus.NOT_STARTED,

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE]
    )
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "task_assignments",
        joinColumns = [JoinColumn(name = "task_id")],
        inverseJoinColumns = [JoinColumn(name = "task_date_id")]
    )
    @Column(name = "dates")
    val dates: Set<TaskDate> = emptySet(),
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "task"
    )
    @Column(name = "attachments")
    val attachments: Set<TaskAttachment> = emptySet()
)