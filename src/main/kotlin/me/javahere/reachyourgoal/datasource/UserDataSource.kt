package me.javahere.reachyourgoal.datasource

import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.domain.UserUnConfirmed
import java.util.*

interface UserDataSource {

    suspend fun createUser(user: User): User

    suspend fun createUnConfirmedUser(user: UserUnConfirmed): UserUnConfirmed

    suspend fun retrieveUserById(userId: UUID): User?
    suspend fun retrieveUserByUsername(username: String): User?
    suspend fun retrieveUserByEmail(email: String): User?

    suspend fun retrieveUnConfirmedUserByUsername(username: String): UserUnConfirmed?
    suspend fun retrieveUnConfirmedUserByEmail(email: String): UserUnConfirmed?
    suspend fun retrieveUnConfirmedUserByToken(token: String): UserUnConfirmed?

    suspend fun updateUser(user: User): User

    suspend fun deleteUserById(userId: UUID)
}