package me.javahere.reachyourgoal.repository

import me.javahere.reachyourgoal.domain.entity.TaskAndTag
import me.javahere.reachyourgoal.domain.entity.TaskAndTagId
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TaskAndTagRepository : CoroutineCrudRepository<TaskAndTag, TaskAndTagId>
