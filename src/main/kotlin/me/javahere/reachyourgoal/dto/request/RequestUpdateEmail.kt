package me.javahere.reachyourgoal.dto.request

import java.util.*

class RequestUpdateEmail(
    val userId: UUID,
    val newEmail: String,
)
