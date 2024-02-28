package me.javahere.reachyourgoal.domain.dto.request.validator

import me.javahere.reachyourgoal.domain.dto.request.RequestUpdateEmail
import me.javahere.reachyourgoal.validator.PatternValidator
import me.javahere.reachyourgoal.validator.VALID_EMAIL_PATTERN
import me.javahere.reachyourgoal.validator.Validator

class RequestUpdateEmailValidator : Validator<RequestUpdateEmail> {
    override fun validate(input: RequestUpdateEmail): List<String> {
        val emailValidator = PatternValidator(VALID_EMAIL_PATTERN)

        val errors =
            listOf(
                emailValidator.validate(input.newEmail),
            ).flatten()

        return errors
    }
}
