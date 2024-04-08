package me.javahere.reachyourgoal.repository

import kotlinx.coroutines.flow.Flow
import me.javahere.reachyourgoal.domain.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime

interface UserRepository : CoroutineCrudRepository<User, Int> {
    fun findByUsername(username: String): Flow<User>

    fun findByEmail(email: String): Flow<User>

    suspend fun deleteByCreatedAtBefore(date: LocalDateTime)
}
