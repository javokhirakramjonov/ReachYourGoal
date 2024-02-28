package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.User
import java.time.LocalDate
import java.util.UUID

interface UserRepository {
    suspend fun addUser(user: User): User

    suspend fun getUserById(userId: UUID): User?

    suspend fun getUserByUsername(username: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun updateUser(user: User): User

    suspend fun deleteUserById(userId: UUID)

    suspend fun deleteUnconfirmedUsersBefore(date: LocalDate)
}
