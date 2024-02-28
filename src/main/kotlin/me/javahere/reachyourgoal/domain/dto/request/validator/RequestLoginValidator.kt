package me.javahere.reachyourgoal.domain.dto.request.validator

import me.javahere.reachyourgoal.domain.dto.request.RequestLogin
import me.javahere.reachyourgoal.validator.LengthValidator
import me.javahere.reachyourgoal.validator.PatternValidator
import me.javahere.reachyourgoal.validator.VALID_PASSWORD_LENGTH
import me.javahere.reachyourgoal.validator.VALID_USERNAME_LENGTH
import me.javahere.reachyourgoal.validator.VALID_USERNAME_PATTERN
import me.javahere.reachyourgoal.validator.Validator

class RequestLoginValidator : Validator<RequestLogin> {
    override fun validate(input: RequestLogin): List<String> {
        val usernameValidator = PatternValidator(VALID_USERNAME_PATTERN)
        val usernameLengthValidator = LengthValidator(VALID_USERNAME_LENGTH)
        val passwordLengthValidator = LengthValidator(VALID_PASSWORD_LENGTH)

        val errors =
            listOf(
                usernameValidator.validate(input.username),
                usernameLengthValidator.validate(input.username),
                passwordLengthValidator.validate(input.password),
            ).flatten()

        return errors
    }
}
