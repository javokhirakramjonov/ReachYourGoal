package me.javahere.reachyourgoal.repository.impl

import me.javahere.reachyourgoal.dao.UserDao
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.repository.UserRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
class UserRepositoryImpl(
    private val userDao: UserDao,
) : UserRepository {
    override suspend fun addUser(user: User): User {
        return userDao.save(user)
    }

    override suspend fun getUserById(userId: UUID): User? {
        return userDao.findById(userId)
    }

    override suspend fun getUserByUsername(username: String): User? {
        return userDao.findByUsername(username)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    override suspend fun updateUser(user: User): User {
        return userDao.save(user)
    }

    override suspend fun deleteUserById(userId: UUID) {
        userDao.deleteById(userId)
    }

    override suspend fun deleteUnconfirmedUsersBefore(date: LocalDate) {
        userDao.deleteUsersByCreatedAtBefore(date)
    }
}
