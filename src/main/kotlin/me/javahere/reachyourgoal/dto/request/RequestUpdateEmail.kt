package me.javahere.reachyourgoal.dto.request

import java.util.*

data class RequestUpdateEmail(
    val userId: UUID,
    val newEmail: String,
)
