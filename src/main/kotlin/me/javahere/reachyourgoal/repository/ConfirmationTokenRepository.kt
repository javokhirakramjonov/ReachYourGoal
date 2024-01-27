package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.ConfirmationToken
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface ConfirmationTokenRepository : CoroutineCrudRepository<ConfirmationToken, String> {
	suspend fun findByToken(token: String): ConfirmationToken?
	suspend fun findByUserId(userId: UUID): ConfirmationToken?
	suspend fun deleteByExpireDateBefore(time: Long)
	suspend fun deleteAllByUserId(userId: UUID)
}