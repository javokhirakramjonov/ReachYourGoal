package me.javahere.reachyourgoal.validator

class PatternValidator(
    private val pattern: Regex,
    private val errorMessage: String? = null,
) : Validator<String> {
    override fun validate(input: String): List<String> {
        if (pattern.matches(input)) return emptyList()

        val defaultErrorMessage = "$input didn't match for pattern(${pattern.pattern})."

        return listOf(errorMessage ?: defaultErrorMessage)
    }
}
