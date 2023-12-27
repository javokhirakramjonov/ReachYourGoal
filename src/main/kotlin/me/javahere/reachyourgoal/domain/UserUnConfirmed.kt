package me.javahere.reachyourgoal.domain

import me.javahere.reachyourgoal.dto.request.RequestRegister
import me.javahere.reachyourgoal.util.Transformable
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "users_un_confirmed")
data class UserUnConfirmed(
    @Id
    @Column("id")
    val id: UUID? = null,
    @Column("first_name")
    val firstname: String,
    @Column("last_name")
    val lastname: String,
    @Column("username")
    val username: String,
    @Column("email")
    val email: String,
    @Column("password")
    val password: String,
    @Column("token")
    val token: String
) : Transformable<RequestRegister> {
    override fun transform(): RequestRegister {
        return RequestRegister(
            firstname = firstname,
            lastname = lastname,
            username = username,
            email = email,
            password = password
        )
    }
}