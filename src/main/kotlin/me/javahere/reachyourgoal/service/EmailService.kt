package me.javahere.reachyourgoal.service

interface EmailService {

    fun sendRegisterConfirmEmail(email: String, confirmationLink: String)

}