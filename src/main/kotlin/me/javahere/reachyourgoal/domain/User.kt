package me.javahere.reachyourgoal.domain

import jakarta.persistence.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity(name = "users")
data class User(
    @Id
    @Column(name = "id")
    val id: UUID,

    @Column(name = "first_name")
    val firstName: String,
    @Column(name = "last_name")
    val lastName: String,
    @Column(name = "username")
    @JvmField
    val username: String,
    @Column(name = "email")
    val email: String,
    @Column(name = "password")
    @JvmField
    val password: String,
    @Column(name = "is_account_expired")
    val isAccountExpired: Boolean,
    @Column(name = "is_account_locked")
    val isAccountLocked: Boolean,
    @Column(name = "is_credentials_expired")
    val isCredentialsExpired: Boolean,
    @Column(name = "is_enabled")
    @JvmField
    val isEnabled: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
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