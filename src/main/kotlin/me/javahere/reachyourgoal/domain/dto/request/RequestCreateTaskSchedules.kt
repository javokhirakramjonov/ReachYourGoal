package me.javahere.reachyourgoal.domain.dto.request

import java.time.LocalDate

class RequestCreateTaskSchedules(
    val taskId: Int,
    val planId: Int,
    val taskDates: List<LocalDate>,
)
