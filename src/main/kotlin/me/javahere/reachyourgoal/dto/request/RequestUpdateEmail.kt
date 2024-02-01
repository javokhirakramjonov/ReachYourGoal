package me.javahere.reachyourgoal.dto.request

import java.util.UUID

class RequestUpdateEmail(
    val userId: UUID,
    val newEmail: String,
)
