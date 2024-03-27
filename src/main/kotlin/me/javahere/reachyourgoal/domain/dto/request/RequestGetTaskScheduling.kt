package me.javahere.reachyourgoal.domain.dto.request

import java.time.LocalDateTime

class RequestGetTaskScheduling(
    val fromDateTime: LocalDateTime,
    val toDateTime: LocalDateTime,
)
