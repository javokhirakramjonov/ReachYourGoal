package me.javahere.reachyourgoal.datasource.impl

import me.javahere.reachyourgoal.datasource.UserDataSource
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.repository.UserRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserDataSourceImpl(
    private val userRepository: UserRepository
) : UserDataSource {
    override suspend fun createUser(user: User): User {
        return userRepository.save(user)
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

    override suspend fun updateUser(user: User): User {
        return userRepository.save(user)
    }

    override suspend fun deleteUserById(userId: UUID) {
        userRepository.deleteById(userId)
    }
}