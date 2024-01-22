package me.javahere.reachyourgoal.service

import java.io.File

interface FileService {

    suspend fun createFile(path: String, fileName: String, fileBytes: ByteArray): Boolean
    suspend fun getFileByName(path: String, fileName: String): File
    suspend fun deleteFileByName(taskFilePath: String, fileName: String)
}