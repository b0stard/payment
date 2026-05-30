package com.example.demo.file.service

import io.minio.GetObjectArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class MinioStorageService(
    private val minioClient: MinioClient,

    @Value("\${minio.bucket}")
    private val bucket: String
) {

    fun upload(
        storageKey: String,
        inputStream: InputStream,
        size: Long,
        contentType: String
    ) {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(storageKey)
                .stream(inputStream, size, -1)
                .contentType(contentType)
                .build()
        )
    }

    fun download(storageKey: String): ByteArray {
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucket)
                .`object`(storageKey)
                .build()
        ).use { stream ->
            stream.readBytes()
        }
    }

    fun delete(storageKey: String) {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucket)
                .`object`(storageKey)
                .build()
        )
    }
}