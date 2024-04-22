package me.javahere.reachyourgoal.component

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    private companion object {
        const val INFO_TITLE = "Reach Your Goal API"
        const val VERSION_API = "1.0"
        const val CONTACT_NAME = "Javokhir"
        const val CONTACT_EMAIL = "javokhir.akramjonov@gmail.com"
        const val CONTACT_URL = "https://www.linkedin.com/in/javokhirakramjonov"
        const val KEY_BEARER_AUTHENTICATION = "Bearer Authentication"
        const val GROUP_ALL = "all"
    }

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .addServersItem(Server().url("/"))
            .addSecurityItem(SecurityRequirement().addList(KEY_BEARER_AUTHENTICATION))
            .components(
                Components()
                    .addSecuritySchemes(
                        KEY_BEARER_AUTHENTICATION,
                        createAPIKeyScheme(),
                    ),
            )
            .info(
                Info()
                    .title(INFO_TITLE)
                    .version(VERSION_API)
                    .contact(
                        Contact()
                            .name(CONTACT_NAME)
                            .email(CONTACT_EMAIL)
                            .url(CONTACT_URL),
                    ),
            )
    }

    private fun createAPIKeyScheme(): SecurityScheme {
        return SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer")
    }

    @Bean
    fun allOpenApi(): GroupedOpenApi {
        val paths = arrayOf("/**")
        return GroupedOpenApi
            .builder()
            .group(GROUP_ALL)
            .pathsToMatch(*paths)
            .addOperationCustomizer { operation, _ ->
                val securityItem = SecurityRequirement().addList("Bearer Authentication")
                operation.addSecurityItem(securityItem)
                operation
            }
            .build()
    }
}
