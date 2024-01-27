package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface UserRepository : CoroutineCrudRepository<User, UUID> {
    suspend fun findByUsername(username: String): User?

    suspend fun findByEmail(email: String): User?
}
