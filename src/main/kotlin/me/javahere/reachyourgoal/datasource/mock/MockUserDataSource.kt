package me.javahere.reachyourgoal.datasource.mock

import me.javahere.reachyourgoal.datasource.UserDataSource
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.exception.DuplicatedElementException
import me.javahere.reachyourgoal.util.MockConstants.USER_EMAIL
import me.javahere.reachyourgoal.util.MockConstants.USER_FIRSTNAME
import me.javahere.reachyourgoal.util.MockConstants.USER_ID
import me.javahere.reachyourgoal.util.MockConstants.USER_LASTNAME
import me.javahere.reachyourgoal.util.MockConstants.USER_PASSWORD
import me.javahere.reachyourgoal.util.MockConstants.USER_ROLE
import me.javahere.reachyourgoal.util.MockConstants.USER_USERNAME
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MockUserDataSource() : UserDataSource {

    private val users = mutableListOf<User>(
        User(
            id = USER_ID,
            firstName = USER_FIRSTNAME,
            lastName = USER_LASTNAME,
            username = USER_USERNAME,
            email = USER_EMAIL,
            password = USER_PASSWORD,
            role = USER_ROLE
        )
    )

    override suspend fun createUser(user: User): User {
        val userWithId = if (user.id == null) user.copy(id = UUID.randomUUID()) else user

        if (users.any { it.id == user.id || it.username == user.username || it.email == user.email }) throw DuplicatedElementException()

        users.add(userWithId)

        return userWithId
    }

    override suspend fun retrieveUserById(userId: UUID): User? {
        return users.firstOrNull { it.id == userId }
    }

    override suspend fun retrieveUserByUsername(username: String): User? {
        return users.firstOrNull { it.username == username }
    }

    override suspend fun retrieveUserByEmail(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

    override suspend fun updateUser(user: User): User {
        val index = users.indexOfFirst { it.id == user.id && it.username == user.username && it.email == user.email }

        if (index == -1) return createUser(user)

        users[index] = user

        return user
    }

    override suspend fun deleteUserById(userId: UUID) {
        users.removeIf { it.id == userId }
    }
}