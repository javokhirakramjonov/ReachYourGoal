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
        private class Serializer : Converter<TaskId, Int> {
            override fun convert(source: TaskId): Int = source.value
        }

        private class Deserializer : Converter<Int, TaskId> {
            override fun convert(source: Int): TaskId = TaskId(source)
        }

        val serializer: Converter<TaskId, Int> = Serializer()
        val deserializer: Converter<Int, TaskId> = Deserializer()
    }
}

@JvmInline
value class TaskAttachmentId(val value: Int = 0) {
    companion object {
        private class Serializer : Converter<TaskAttachmentId, Int> {
            override fun convert(source: TaskAttachmentId): Int = source.value
        }

        private class Deserializer : Converter<Int, TaskAttachmentId> {
            override fun convert(source: Int): TaskAttachmentId = TaskAttachmentId(source)
        }

        val serializer: Converter<TaskAttachmentId, Int> = Serializer()
        val deserializer: Converter<Int, TaskAttachmentId> = Deserializer()
    }
}

@JvmInline
value class TaskTagId(val value: Int = 0) {
    companion object {
        private class Serializer : Converter<TaskTagId, Int> {
            override fun convert(source: TaskTagId): Int = source.value
        }

        private class Deserializer : Converter<Int, TaskTagId> {
            override fun convert(source: Int): TaskTagId = TaskTagId(source)
        }

        val serializer: Converter<TaskTagId, Int> = Serializer()
        val deserializer: Converter<Int, TaskTagId> = Deserializer()
    }
}

@JvmInline
value class TaskScheduleId(val value: Int = 0) {
    companion object {
        private class Serializer : Converter<TaskScheduleId, Int> {
            override fun convert(source: TaskScheduleId): Int = source.value
        }

        private class Deserializer : Converter<Int, TaskScheduleId> {
            override fun convert(source: Int): TaskScheduleId = TaskScheduleId(source)
        }

        val serializer: Converter<TaskScheduleId, Int> = Serializer()
        val deserializer: Converter<Int, TaskScheduleId> = Deserializer()
    }
}

@JvmInline
value class TaskPlanId(val value: Int = 0) {
    companion object {
        private class Serializer : Converter<TaskPlanId, Int> {
            override fun convert(source: TaskPlanId): Int = source.value
        }

        private class Deserializer : Converter<Int, TaskPlanId> {
            override fun convert(source: Int): TaskPlanId = TaskPlanId(source)
        }

        val serializer: Converter<TaskPlanId, Int> = Serializer()
        val deserializer: Converter<Int, TaskPlanId> = Deserializer()
    }
}
