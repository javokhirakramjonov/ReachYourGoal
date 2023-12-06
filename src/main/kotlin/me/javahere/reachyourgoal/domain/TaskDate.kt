package me.javahere.reachyourgoal.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity(name = "task_dates")
data class TaskDate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(name = "date")
    val date: LocalDate,

    @ManyToMany(
        fetch = FetchType.LAZY,
        mappedBy = "dates"
    )
    @Column(name = "tasks")
    val tasks: Set<Task> = emptySet()
)