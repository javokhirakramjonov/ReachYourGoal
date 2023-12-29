package me.javahere.reachyourgoal.service.impl

import me.javahere.reachyourgoal.service.EmailService
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
    private val templateEngine: TemplateEngine
) : EmailService {
    override fun sendRegisterConfirmEmail(email: String, confirmationLink: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(email)
        helper.setSubject("Confirmation Email")

        val context = Context()
        context.setVariable("confirmationLink", confirmationLink)
        val htmlContent = templateEngine.process("confirmationEmail", context)

        helper.setText(htmlContent, true)

        mailSender.send(message)
    }
}