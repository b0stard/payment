package com.example.demo.config

import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig(

    @Value("\${minio.endpoint}")
    private val endpoint: String,

    @Value("\${minio.access-key}")
    private val accessKey: String,

    @Value("\${minio.secret-key}")
    private val secretKey: String,

    @Value("\${minio.bucket}")
    private val bucket: String
) {

    @Bean
    fun minioClient(): MinioClient {
        val client = MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build()

        val exists = client.bucketExists(
            BucketExistsArgs.builder()
                .bucket(bucket)
                .build()
        )

        if (!exists) {
            client.makeBucket(
                MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build()
            )
        }

        return client
    }
}