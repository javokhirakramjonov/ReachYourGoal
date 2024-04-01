package me.javahere.reachyourgoal.domain.dto.request

import java.time.LocalDateTime

class RequestGetTaskSchedule(
    val fromDateTime: LocalDateTime,
    val toDateTime: LocalDateTime,
)
