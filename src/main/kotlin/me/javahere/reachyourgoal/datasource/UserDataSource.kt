package me.javahere.reachyourgoal.datasource

import me.javahere.reachyourgoal.domain.ConfirmationToken
import me.javahere.reachyourgoal.domain.User
import java.util.*

interface UserDataSource {

	suspend fun createUser(user: User): User

	suspend fun createConfirmationToken(confirmationToken: ConfirmationToken): ConfirmationToken

	suspend fun retrieveUserById(userId: UUID): User?
	suspend fun retrieveUserByUsername(username: String): User?
	suspend fun retrieveUserByEmail(email: String): User?

	suspend fun retrieveConfirmationTokenByToken(token: String): ConfirmationToken?
	suspend fun retrieveConfirmationTokenByUserId(userId: UUID): ConfirmationToken?

	suspend fun updateUser(user: User): User

	suspend fun deleteUserById(userId: UUID)

	suspend fun deleteConfirmationTokenByExpireDateBefore(time: Long)
	suspend fun deleteAllConfirmationTokensByUserId(userId: UUID)
}