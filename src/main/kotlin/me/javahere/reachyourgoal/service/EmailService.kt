package me.javahere.reachyourgoal.service

import java.util.*

interface EmailService {

    fun sendRegisterConfirmEmail(
        email: String,
        confirmationLink: String,
        expireDateTime: Date
    )

}