package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.entity.User
import me.javahere.reachyourgoal.domain.id.UserId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, UserId> {
    suspend fun findByUsername(username: String): User?

    suspend fun findByEmail(email: String): User?
}
