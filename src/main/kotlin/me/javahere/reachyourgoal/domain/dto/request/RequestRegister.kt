package me.javahere.reachyourgoal.domain.dto.request

import me.javahere.reachyourgoal.domain.Role
import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.entity.User

class RequestRegister(
    val firstname: String,
    val lastname: String,
    val username: String,
    val email: String,
    val password: String,
) : Transformable<User> {
    override fun transform(): User =
        User(
            firstname = firstname,
            lastname = lastname,
            username = username,
            email = email,
            password = password,
            role = Role.USER,
        )
}
