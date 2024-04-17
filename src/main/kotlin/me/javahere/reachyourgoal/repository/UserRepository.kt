package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Int> {
    suspend fun findByUsername(username: String): User?

    suspend fun findByEmail(email: String): User?
}
