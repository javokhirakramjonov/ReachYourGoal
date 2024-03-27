package me.javahere.reachyourgoal.controller.validator

import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateEmail
import me.javahere.reachyourgoal.validator.PatternValidator
import me.javahere.reachyourgoal.validator.VALID_EMAIL_PATTERN
import me.javahere.reachyourgoal.validator.Validator
import org.springframework.stereotype.Component

@Component
class RequestUpdateEmailValidator : Validator<RequestUpdateEmail> {
    override fun validate(input: RequestUpdateEmail): List<String> {
        val emailValidator = PatternValidator(VALID_EMAIL_PATTERN)

        val errors =
            listOf(
                emailValidator.validate(input.newEmail).map { "email: $it" },
            ).flatten()

        return errors
    }
}
