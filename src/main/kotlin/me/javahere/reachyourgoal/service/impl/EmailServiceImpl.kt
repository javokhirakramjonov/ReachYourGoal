package me.javahere.reachyourgoal.service.impl

import me.javahere.reachyourgoal.service.EmailService
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.Date

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
    private val templateEngine: TemplateEngine,
) : EmailService {
    override fun sendRegisterConfirmEmail(
        email: String,
        confirmationLink: String,
        expireDateTime: Date,
    ) {
        val context = Context()
        context.setVariable("confirmationLink", confirmationLink)
        context.setVariable("expireDateTime", expireDateTime)
        val htmlContent = templateEngine.process("confirmationRegistrationEmail", context)

        sendEmail(
            to = email,
            subject = "Confirmation email",
            content = htmlContent,
        )
    }

    override fun sendUpdateEmailConfirmEmail(
        email: String,
        confirmationLink: String,
        expireDateTime: Date,
    ) {
        val context = Context()
        context.setVariable("confirmationLink", confirmationLink)
        context.setVariable("expireDateTime", expireDateTime)
        val htmlContent = templateEngine.process("confirmationEmailUpdateEmail", context)

        sendEmail(
            to = email,
            subject = "Confirmation email",
            content = htmlContent,
        )
    }

    private fun sendEmail(
        to: String,
        subject: String,
        content: String,
        htmlEnabled: Boolean = true,
    ) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(content, htmlEnabled)

        mailSender.send(message)
    }
}
