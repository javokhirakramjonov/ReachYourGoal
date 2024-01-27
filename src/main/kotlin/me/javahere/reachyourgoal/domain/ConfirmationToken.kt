package me.javahere.reachyourgoal.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "confirmation_tokens")
class ConfirmationToken(
    @Id
    @Column("id")
    val id: UUID? = null,
    @Column("token")
    val token: String,
    @Column("expire_date")
    val expireDate: Long,
    @Column("user_id")
    val userId: UUID,
)
