package me.javahere.reachyourgoal.util

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource

fun ResourceBundleMessageSource.getMessage(
    key: String,
    vararg args: Any,
): String {
    val currentLocale = LocaleContextHolder.getLocale()
    val errorMessage = getMessage(key, args, currentLocale)
    return errorMessage
}
