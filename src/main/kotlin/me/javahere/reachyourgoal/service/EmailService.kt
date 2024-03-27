package me.javahere.reachyourgoal.service

import java.util.Date

interface EmailService {
    fun sendRegisterConfirmEmail(
        email: String,
        confirmationLink: String,
        expireDateTime: Date,
    )

    fun sendUpdateEmailConfirmEmail(
        email: String,
        confirmationLink: String,
        expireDateTime: Date,
    )
}
