package me.javahere.reachyourgoal.controller.validator

import me.javahere.reachyourgoal.domain.dto.request.RequestRegister
import me.javahere.reachyourgoal.validator.LengthValidator
import me.javahere.reachyourgoal.validator.PatternValidator
import me.javahere.reachyourgoal.validator.VALID_EMAIL_PATTERN
import me.javahere.reachyourgoal.validator.VALID_FIRSTNAME_LENGTH
import me.javahere.reachyourgoal.validator.VALID_LASTNAME_LENGTH
import me.javahere.reachyourgoal.validator.VALID_NAME_PATTERN
import me.javahere.reachyourgoal.validator.VALID_PASSWORD_LENGTH
import me.javahere.reachyourgoal.validator.VALID_USERNAME_LENGTH
import me.javahere.reachyourgoal.validator.VALID_USERNAME_PATTERN
import me.javahere.reachyourgoal.validator.Validator
import org.springframework.stereotype.Component

@Component
class RequestRegisterValidator : Validator<RequestRegister> {
    override fun validate(input: RequestRegister): List<String> {
        val usernameValidator = PatternValidator(VALID_USERNAME_PATTERN)
        val usernameLengthValidator = LengthValidator(VALID_USERNAME_LENGTH)
        val emailValidator = PatternValidator(VALID_EMAIL_PATTERN)
        val firstnameValidator = PatternValidator(VALID_NAME_PATTERN)
        val firstnameLengthValidator = LengthValidator(VALID_FIRSTNAME_LENGTH)
        val lastnameValidator = PatternValidator(VALID_NAME_PATTERN)
        val lastnameLengthValidator = LengthValidator(VALID_LASTNAME_LENGTH)
        val passwordValidator = LengthValidator(VALID_PASSWORD_LENGTH)

        val errors =
            listOf(
                usernameValidator.validate(input.username).map { "username: $it" },
                usernameLengthValidator.validate(input.username).map { "username: $it" },
                emailValidator.validate(input.email).map { "email: $it" },
                firstnameValidator.validate(input.firstname).map { "firstname: $it" },
                firstnameLengthValidator.validate(input.firstname).map { "firstname: $it" },
                lastnameValidator.validate(input.lastname).map { "lastname: $it" },
                lastnameLengthValidator.validate(input.lastname).map { "lastname: $it" },
                passwordValidator.validate(input.password).map { "password: $it" },
            ).flatten()

        return errors
    }
}
