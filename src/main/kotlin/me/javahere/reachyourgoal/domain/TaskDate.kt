package me.javahere.reachyourgoal.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "task_dates")
data class TaskDate(
    @Id
    val id: Int = 0,
    @Column("date")
    val date: LocalDate,
)
