package me.javahere.reachyourgoal.service

import me.javahere.reachyourgoal.domain.dto.UserDto
import me.javahere.reachyourgoal.domain.dto.request.RequestRegister
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateEmail
import java.util.UUID

interface UserService {
    suspend fun register(user: RequestRegister)

    suspend fun confirmRegister(token: String): UserDto

    suspend fun confirmNewEmail(token: String): UserDto

    suspend fun findUserById(userId: UUID): UserDto

    suspend fun findUserByEmail(email: String): UserDto

    suspend fun findUserByUsername(username: String): UserDto

    suspend fun updateUser(
        userId: UUID,
        firstName: String? = null,
        lastName: String? = null,
    ): UserDto

    suspend fun updateEmail(request: RequestUpdateEmail)

    suspend fun deleteUserById(userId: UUID)
}
