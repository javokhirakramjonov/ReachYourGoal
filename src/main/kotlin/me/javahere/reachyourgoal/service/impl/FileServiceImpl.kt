package me.javahere.reachyourgoal.service.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.collect
import kotlinx.coroutines.withContext
import me.javahere.reachyourgoal.service.FileService
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths

@Service
class FileServiceImpl : FileService {
    override suspend fun createFile(
        path: String,
        fileName: String,
        filePart: FilePart,
    ): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val file = Paths.get(path, fileName).toFile()
                file.createNewFile()
                filePart.transferTo(file).collect {}
                true
            } catch (e: Exception) {
                // TODO handle it
                false
            }
        }

    override suspend fun getFileByName(
        path: String,
        fileName: String,
    ): File? =
        withContext(Dispatchers.IO) {
            val file = File(path, fileName)
            if (file.exists()) file else null
        }

    override suspend fun deleteFileByName(
        taskFilePath: String,
        fileName: String,
    ) {
        runCatching { File(taskFilePath, fileName).delete() }
    }
}
