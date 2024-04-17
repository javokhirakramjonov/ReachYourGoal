package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.id.UserId

class RequestUpdateEmail(
    val userId: UserId,
    val newEmail: String,
)
