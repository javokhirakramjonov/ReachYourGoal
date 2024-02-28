package me.javahere.reachyourgoal.domain

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class TaskSchedulingId(
    taskId: UUID,
    taskDate: LocalDate,
    taskTime: LocalTime?,
)
