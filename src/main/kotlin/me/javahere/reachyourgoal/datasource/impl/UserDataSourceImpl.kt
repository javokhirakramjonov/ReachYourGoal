package me.javahere.reachyourgoal.datasource.impl

import me.javahere.reachyourgoal.datasource.UserDataSource
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.domain.UserUnConfirmed
import me.javahere.reachyourgoal.repository.UserRepository
import me.javahere.reachyourgoal.repository.UserUnConfirmedRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserDataSourceImpl(
    private val userRepository: UserRepository,
    private val userUnConfirmedRepository: UserUnConfirmedRepository
) : UserDataSource {
    override suspend fun createUser(user: User): User {
        return userRepository.save(user)
    }

    override suspend fun createUnConfirmedUser(user: UserUnConfirmed): UserUnConfirmed {
        return userUnConfirmedRepository.save(user)
    }

    override suspend fun retrieveUserById(userId: UUID): User? {
        return userRepository.findById(userId)
    }

    override suspend fun retrieveUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    override suspend fun retrieveUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override suspend fun retrieveUnConfirmedUserByUsername(username: String): UserUnConfirmed? {
        return userUnConfirmedRepository.findByUsername(username)
    }

    override suspend fun retrieveUnConfirmedUserByEmail(email: String): UserUnConfirmed? {
        return userUnConfirmedRepository.findByEmail(email)
    }

    override suspend fun retrieveUnConfirmedUserByToken(token: String): UserUnConfirmed? {
        return userUnConfirmedRepository.findByToken(token)
    }

    override suspend fun updateUser(user: User): User {
        return userRepository.save(user)
    }

    override suspend fun deleteUserById(userId: UUID) {
        userRepository.deleteById(userId)
    }
}