package me.javahere.reachyourgoal.domain.dto.request.validator

import me.javahere.reachyourgoal.domain.dto.request.RequestCreateTask
import me.javahere.reachyourgoal.validator.LengthValidator
import me.javahere.reachyourgoal.validator.VALID_TASK_DESCRIPTION_LENGTH
import me.javahere.reachyourgoal.validator.VALID_TASK_NAME_LENGTH
import me.javahere.reachyourgoal.validator.Validator
import org.springframework.stereotype.Component

@Component
class RequestTaskCreateValidator : Validator<RequestCreateTask> {
    override fun validate(input: RequestCreateTask): List<String> {
        val nameLengthValidator = LengthValidator(VALID_TASK_NAME_LENGTH)
        val descriptionLengthValidator = LengthValidator(VALID_TASK_DESCRIPTION_LENGTH)

        val errors =
            listOf(
                nameLengthValidator.validate(input.name).map { "task name: $it" },
                descriptionLengthValidator.validate(input.description.orEmpty()).map { "task description: $it" },
            ).flatten()

        return errors
    }
}
