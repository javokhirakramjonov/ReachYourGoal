package me.javahere.reachyourgoal.domain.dto.request.validator

import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.validator.LengthValidator
import me.javahere.reachyourgoal.validator.VALID_TASK_DESCRIPTION_LENGTH
import me.javahere.reachyourgoal.validator.VALID_TASK_NAME_LENGTH
import me.javahere.reachyourgoal.validator.Validator

class RequestTaskCreateValidator : Validator<RequestCreateTask> {
    override fun validate(input: RequestCreateTask): List<String> {
        val nameLengthValidator = LengthValidator(VALID_TASK_NAME_LENGTH)
        val descriptionLengthValidator = LengthValidator(VALID_TASK_DESCRIPTION_LENGTH)

        val errors =
            listOf(
                nameLengthValidator.validate(input.name),
                descriptionLengthValidator.validate(input.description.orEmpty()),
            ).flatten()

        return errors
    }
}
