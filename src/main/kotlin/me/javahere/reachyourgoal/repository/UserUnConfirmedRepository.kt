package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.UserUnConfirmed
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserUnConfirmedRepository : CoroutineCrudRepository<UserUnConfirmed, String> {
    suspend fun findByUsername(username: String): UserUnConfirmed?
    suspend fun findByEmail(email: String): UserUnConfirmed?
    suspend fun findByToken(token: String): UserUnConfirmed?
}