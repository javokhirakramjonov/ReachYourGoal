package me.javahere.reachyourgoal.dto.request.validator

import me.javahere.reachyourgoal.dto.request.RequestRegister
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
                usernameValidator.validate(input.username),
                usernameLengthValidator.validate(input.username),
                emailValidator.validate(input.email),
                firstnameValidator.validate(input.firstname),
                firstnameLengthValidator.validate(input.firstname),
                lastnameValidator.validate(input.lastname),
                lastnameLengthValidator.validate(input.lastname),
                passwordValidator.validate(input.password),
            ).flatten()

        return errors
    }
}
