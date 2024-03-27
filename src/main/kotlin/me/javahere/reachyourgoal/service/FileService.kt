package me.javahere.reachyourgoal.service

import org.springframework.http.codec.multipart.FilePart
import java.io.File

interface FileService {
    suspend fun createFile(
        path: String,
        fileName: String,
        filePart: FilePart,
    ): Boolean

    suspend fun getFileByName(
        path: String,
        fileName: String,
    ): File?

    suspend fun deleteFileByName(
        taskFilePath: String,
        fileName: String,
    )
}
