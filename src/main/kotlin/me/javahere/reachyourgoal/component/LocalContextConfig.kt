package me.javahere.reachyourgoal.component

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.server.WebFilter
import java.util.Locale

@Configuration
class LocalContextConfig {
    companion object {
        private const val LOCALE_KEY = "locale"
    }

    @Bean
    fun localeContextWebFilter(): WebFilter {
        return WebFilter { exchange, chain ->
            val query = exchange.request.uri.query

            val locale = query?.let(::getLocaleFromQueryParam)

            locale?.let(LocaleContextHolder::setLocale)

            chain.filter(exchange)
        }
    }

    private fun getLocaleFromQueryParam(query: String): Locale? {
        val (_, lan) =
            query
                .split("&")
                .map { it.split("=") }
                .firstOrNull { (key) -> key == LOCALE_KEY }
                ?: return null

        return runCatching { Locale.of(lan) }.getOrNull()
    }

    @Bean
    fun messageSource(): ResourceBundleMessageSource {
        val messageSource = ResourceBundleMessageSource()

        messageSource.setBasenames("messages/messages")
        messageSource.setDefaultLocale(Locale.of("en"))
        messageSource.setDefaultEncoding("UTF-8")

        return messageSource
    }
}
