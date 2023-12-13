package me.javahere.reachyourgoal.service

import me.javahere.reachyourgoal.dto.UserDto
import me.javahere.reachyourgoal.dto.request.RequestRegister
import org.springframework.security.core.userdetails.ReactiveUserDetailsService

interface UserService : ReactiveUserDetailsService {
    suspend fun isUsernameExists(username: String): Boolean
    suspend fun isEmailExists(email: String): Boolean
    suspend fun registerUser(user: RequestRegister)
    suspend fun findUserByUsername(username: String): UserDto?
}