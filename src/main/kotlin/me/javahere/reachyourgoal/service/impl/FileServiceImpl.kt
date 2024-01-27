package me.javahere.reachyourgoal.service.impl

import me.javahere.reachyourgoal.exception.ExceptionResponse
import me.javahere.reachyourgoal.exception.ReachYourGoalException
import me.javahere.reachyourgoal.exception.ReachYourGoalExceptionType
import me.javahere.reachyourgoal.service.FileService
import org.springframework.stereotype.Service
import java.io.File

@Service
class FileServiceImpl : FileService {
    override suspend fun createFile(
        path: String,
        fileName: String,
        fileBytes: ByteArray,
    ): Boolean {
        return runCatching {
            val dir = File(path)

            if (!dir.exists()) dir.mkdirs()

            File(path, fileName).writeBytes(fileBytes)

            true
        }.getOrDefault(false)
    }

    override suspend fun getFileByName(
        path: String,
        fileName: String,
    ): File {
        return try {
            File(path, fileName)
        } catch (e: Exception) {
            throw ExceptionResponse(ReachYourGoalException(ReachYourGoalExceptionType.NOT_FOUND))
        }
    }

    override suspend fun deleteFileByName(
        taskFilePath: String,
        fileName: String,
    ) {
        runCatching { File(taskFilePath, fileName).delete() }
    }
}
