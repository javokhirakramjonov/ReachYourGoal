package me.javahere.reachyourgoal.service

import me.javahere.reachyourgoal.dto.UserDto
import me.javahere.reachyourgoal.dto.request.RequestRegister
import java.util.*

interface UserService {
    suspend fun registerUser(user: RequestRegister)
    suspend fun confirm(token: String): UserDto

    suspend fun findUserById(userId: UUID): UserDto
    suspend fun findUserByEmail(email: String): UserDto
    suspend fun findUserByUsername(username: String): UserDto

    suspend fun updateUser(
        userId: UUID,
        firstName: String? = null,
        lastName: String? = null,
        username: String? = null,
    ): UserDto

    suspend fun updateEmail(
        userId: UUID,
        newEmail: String
    )

    suspend fun confirmUpdateEmail(token: String, newEmail: String): UserDto

    suspend fun deleteUserById(userId: UUID)
}