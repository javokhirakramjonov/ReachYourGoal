package me.javahere.reachyourgoal.domain.entity

import me.javahere.reachyourgoal.domain.Role
import me.javahere.reachyourgoal.domain.Transformable
import me.javahere.reachyourgoal.domain.dto.UserDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Table(name = "users")
data class User(
    @Id
    @Column("id")
    val id: Int = 0,
    @Column("first_name")
    val firstname: String,
    @Column("last_name")
    val lastname: String,
    @Column("username")
    @JvmField
    val username: String,
    @Column("email")
    val email: String,
    @Column("password")
    @JvmField
    val password: String,
    @Column("is_account_expired")
    val isAccountExpired: Boolean = false,
    @Column("is_account_locked")
    val isAccountLocked: Boolean = false,
    @Column("is_credentials_expired")
    val isCredentialsExpired: Boolean = false,
    @Column("is_enabled")
    @JvmField
    val isEnabled: Boolean = true,
    @Column("role")
    val role: Role,
    @Column("is_confirmed")
    val isConfirmed: Boolean = false,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : UserDetails, Transformable<UserDto> {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority(role.name))

    override fun getPassword() = password

    override fun getUsername() = username

    override fun isAccountNonExpired() = isAccountExpired

    override fun isAccountNonLocked() = isAccountLocked.not()

    override fun isCredentialsNonExpired() = isCredentialsExpired.not()

    override fun isEnabled() = isEnabled

    override fun transform(): UserDto {
        return UserDto(
            id = id,
            firstName = firstname,
            lastName = lastname,
            email = email,
            username = username,
            isConfirmed = isConfirmed,
        )
    }
}
