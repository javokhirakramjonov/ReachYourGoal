package me.javahere.reachyourgoal.domain.dto.request

import java.util.UUID

class RequestUpdateEmail(
    val userId: UUID,
    val newEmail: String,
)
