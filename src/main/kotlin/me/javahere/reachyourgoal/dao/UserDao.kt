package me.javahere.reachyourgoal.dao

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime
import java.util.UUID

interface UserDao : CoroutineCrudRepository<User, UUID> {
    fun findByUsername(username: String): Flow<User?>

    fun findByEmail(email: String): Flow<User?>

    suspend fun deleteByCreatedAtBefore(date: LocalDateTime)
}
