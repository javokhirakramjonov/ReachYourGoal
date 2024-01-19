package me.javahere.reachyourgoal.service.impl

import me.javahere.reachyourgoal.service.FileService
import org.springframework.stereotype.Service
import java.io.File

@Service
class FileServiceImpl : FileService {
    override suspend fun createFile(path: String, fileName: String, fileBytes: ByteArray): Boolean {
        return runCatching {
            val dir = File(path)

            if (!dir.exists()) dir.mkdirs()

            File(path, fileName).writeBytes(fileBytes)

            true
        }.getOrDefault(false)
    }
}