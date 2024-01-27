package me.javahere.reachyourgoal.dto.request

import me.javahere.reachyourgoal.domain.Role
import me.javahere.reachyourgoal.domain.User
import me.javahere.reachyourgoal.util.Transformable

data class RequestRegister(
    val firstname: String,
    val lastname: String,
    val username: String,
    val email: String,
    val password: String,
) : Transformable<User> {
    override fun transform(): User =
        User(
            id = null,
            firstname = firstname,
            lastname = lastname,
            username = username,
            email = email,
            password = password,
            role = Role.USER,
        )
}
