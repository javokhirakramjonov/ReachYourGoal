package me.javahere.reachyourgoal.domain.id

import org.springframework.core.convert.converter.Converter

@JvmInline
value class UserId(val value: Int = 0) {
    companion object {
        private class Serializer : Converter<UserId, Int> {
            override fun convert(source: UserId): Int = source.value
        }

        private class Deserializer : Converter<Int, UserId> {
            override fun convert(source: Int): UserId = UserId(source)
        }

        val serializer: Converter<UserId, Int> = Serializer()
        val deserializer: Converter<Int, UserId> = Deserializer()
    }
}

@JvmInline
value class TaskId(val value: Int = 0) {
    companion object {
        private class Serializer : Converter<UserId, Int> {
            override fun convert(source: UserId): Int = source.value
        }

        private class Deserializer : Converter<Int, UserId> {
            override fun convert(source: Int): UserId = UserId(source)
        }

        val serializer: Converter<UserId, Int> = Serializer()
        val deserializer: Converter<Int, UserId> = Deserializer()
    }
}

@JvmInline
value class TaskAttachmentId(val value: Int = 0) {
    companion object {
        private class Serializer : Converter<UserId, Int> {
            override fun convert(source: UserId): Int = source.value
        }

        private class Deserializer : Converter<Int, UserId> {
            override fun convert(source: Int): UserId = UserId(source)
        }

        val serializer: Converter<UserId, Int> = Serializer()
        val deserializer: Converter<Int, UserId> = Deserializer()
    }
}

@JvmInline
value class TaskTagId(val value: Int = 0) {
    companion object {
        private class Serializer : Converter<UserId, Int> {
            override fun convert(source: UserId): Int = source.value
        }

        private class Deserializer : Converter<Int, UserId> {
            override fun convert(source: Int): UserId = UserId(source)
        }

        val serializer: Converter<UserId, Int> = Serializer()
        val deserializer: Converter<Int, UserId> = Deserializer()
    }
}

@JvmInline
value class TaskScheduleId(val value: Int = 0) {
    companion object {
        private class Serializer : Converter<UserId, Int> {
            override fun convert(source: UserId): Int = source.value
        }

        private class Deserializer : Converter<Int, UserId> {
            override fun convert(source: Int): UserId = UserId(source)
        }

        val serializer: Converter<UserId, Int> = Serializer()
        val deserializer: Converter<Int, UserId> = Deserializer()
    }
}

@JvmInline
value class TaskPlanId(val value: Int = 0) {
    companion object {
        private class Serializer : Converter<UserId, Int> {
            override fun convert(source: UserId): Int = source.value
        }

        private class Deserializer : Converter<Int, UserId> {
            override fun convert(source: Int): UserId = UserId(source)
        }

        val serializer: Converter<UserId, Int> = Serializer()
        val deserializer: Converter<Int, UserId> = Deserializer()
    }
}
