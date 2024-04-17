package me.javahere.reachyourgoal.domain.dto

import me.javahere.reachyourgoal.domain.id.UserId

class UserDto(
    val id: UserId,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val isConfirmed: Boolean,
)
