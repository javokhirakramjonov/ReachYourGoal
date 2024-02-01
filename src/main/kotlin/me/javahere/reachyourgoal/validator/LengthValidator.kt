package me.javahere.reachyourgoal.validator

class LengthValidator(
    private val range: IntRange,
    private val errorMessage: String? = null,
) : Validator<String> {
    override fun validate(input: String): List<String> {
        if (input.length in range) return emptyList()

        val defaultErrorMessage = "Length of $input is not between ${range.first} and ${range.last}"

        return listOf(errorMessage ?: defaultErrorMessage)
    }
}
