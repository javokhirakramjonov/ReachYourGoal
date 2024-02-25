package me.javahere.reachyourgoal.domain

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class ScheduledTaskId(
    taskId: UUID,
    taskDate: LocalDate,
    time: LocalTime,
)
