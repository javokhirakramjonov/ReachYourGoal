package me.javahere.reachyourgoal.dao

import me.javahere.reachyourgoal.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDate
import java.util.UUID

interface UserDao : CoroutineCrudRepository<User, UUID> {
    suspend fun findByUsername(username: String): User?

    suspend fun findByEmail(email: String): User?

    suspend fun deleteUsersByCreatedAtBefore(date: LocalDate)
}
