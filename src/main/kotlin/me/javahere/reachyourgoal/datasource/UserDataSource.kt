package me.javahere.reachyourgoal.datasource

import me.javahere.reachyourgoal.domain.User
import java.util.*

interface UserDataSource {

    suspend fun createUser(user: User): User

    suspend fun retrieveUserById(userId: UUID): User?
    suspend fun retrieveUserByUsername(username: String): User?
    suspend fun retrieveUserByEmail(email: String): User?

    suspend fun updateUser(user: User): User

    suspend fun deleteUserById(userId: UUID)
}