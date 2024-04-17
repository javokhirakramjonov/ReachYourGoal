package me.javahere.reachyourgoal.service

import me.javahere.reachyourgoal.domain.dto.UserDto
import me.javahere.reachyourgoal.domain.dto.request.RequestRegister
import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateEmail
import me.javahere.reachyourgoal.domain.id.UserId

interface UserService {
    suspend fun registerProductionMode(user: RequestRegister)

    suspend fun registerDevelopMode(user: RequestRegister): String

    suspend fun confirmRegister(token: String): UserDto

    suspend fun confirmNewEmail(token: String): UserDto

    suspend fun findUserById(userId: UserId): UserDto

    suspend fun findUserByEmail(email: String): UserDto

    suspend fun findUserByUsername(username: String): UserDto

    suspend fun updateUser(
        userId: UserId,
        firstName: String? = null,
        lastName: String? = null,
    ): UserDto

    suspend fun updateEmail(request: RequestUpdateEmail)

    suspend fun deleteUserById(userId: UserId)
}
