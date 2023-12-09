package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.TaskAttachment
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface TaskAttachmentRepository : CoroutineCrudRepository<TaskAttachment, UUID>