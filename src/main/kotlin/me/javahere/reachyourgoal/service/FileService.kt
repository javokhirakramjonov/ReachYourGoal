package me.javahere.reachyourgoal.service

interface FileService {

    suspend fun createFile(path: String, fileName: String, fileBytes: ByteArray): Boolean
}