package me.javahere.reachyourgoal.dto

import java.util.UUID

class UserDto(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val isConfirmed: Boolean,
)
