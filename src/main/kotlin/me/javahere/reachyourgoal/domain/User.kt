package me.javahere.reachyourgoal.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Table(name = "users")
data class User(
    @Id
    @Column("id")
    val id: UUID? = null,
    @Column("first_name")
    val firstName: String,
    @Column("last_name")
    val lastName: String,
    @Column("username")
    @JvmField
    val username: String,
    @Column("email")
    val email: String,
    @Column("password")
    @JvmField
    val password: String,
    @Column("is_account_expired")
    val isAccountExpired: Boolean,
    @Column("is_account_locked")
    val isAccountLocked: Boolean,
    @Column("is_credentials_expired")
    val isCredentialsExpired: Boolean,
    @Column("is_enabled")
    @JvmField
    val isEnabled: Boolean,
    @Column("role")
    val role: Role
) : UserDetails {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority(role.name))

    override fun getPassword() = password

    override fun getUsername() = username

    override fun isAccountNonExpired() = isAccountExpired

    override fun isAccountNonLocked() = isAccountLocked.not()

    override fun isCredentialsNonExpired() = isCredentialsExpired.not()

    override fun isEnabled() = isEnabled
}