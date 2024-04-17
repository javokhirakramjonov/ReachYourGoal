package me.javahere.reachyourgoal.domain.id

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.PostgresDialect

@Configuration
class IdConverters {
    @Bean
    fun registerCustomIdsConversions(): R2dbcCustomConversions {
        val customConverters =
            listOf(
                UserId.serializer,
                UserId.deserializer,
                TaskId.serializer,
                TaskId.deserializer,
                TaskAttachmentId.serializer,
                TaskAttachmentId.deserializer,
                TaskTagId.serializer,
                TaskTagId.deserializer,
                TaskScheduleId.serializer,
                TaskScheduleId.deserializer,
                TaskPlanId.serializer,
                TaskPlanId.deserializer,
            )
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, customConverters)
    }
}
