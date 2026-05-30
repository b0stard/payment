package com.example.demo.file.service

import com.example.demo.file.DownloadFile
import com.example.demo.file.FileResponse
import com.example.demo.file.RenameFileRequest
import com.example.demo.file.model.FileObject
import com.example.demo.file.repository.FileObjectRepository
import com.example.demo.outbox.event.FileEvent
import com.example.demo.outbox.service.OutboxService
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.UUID

@Service
class FileService(
    private val fileObjectRepository: FileObjectRepository,
    private val minioStorageService: MinioStorageService,
    private val outboxService: OutboxService
) {

    companion object {
        private val log = LoggerFactory.getLogger(FileService::class.java)
    }

    @Transactional
    fun upload(
        file: MultipartFile,
        currentUserId: UUID
    ): FileResponse {

        validateFile(file)

        val fileId = UUID.randomUUID()
        val originalFileName = file.originalFilename ?: "unknown"
        val contentType = file.contentType ?: "application/octet-stream"

        val storageKey = "users/$currentUserId/files/$fileId"

        minioStorageService.upload(
            storageKey = storageKey,
            inputStream = file.inputStream,
            size = file.size,
            contentType = contentType
        )

        val fileObject = fileObjectRepository.save(
            FileObject(
                id = fileId,
                ownerId = currentUserId,
                originalFileName = originalFileName,
                storageKey = storageKey,
                contentType = contentType,
                size = file.size
            )
        )

        outboxService.createFileEvent(
            FileEvent(
                fileId = fileObject.id!!,
                ownerId = currentUserId,
                fileName = fileObject.originalFileName,
                eventType = "FILE_UPLOADED"
            )
        )

        log.info(
            "File uploaded. userId={}, fileId={}, fileName={}",
            currentUserId,
            fileObject.id,
            fileObject.originalFileName
        )

        return fileObject.toResponse()
    }

    fun getMyFiles(
        currentUserId: UUID
    ): List<FileResponse> {
        return fileObjectRepository
            .findAllByOwnerIdAndDeletedAtIsNullOrderByCreatedAtDesc(currentUserId)
            .map { it.toResponse() }
    }

    fun download(
        fileId: UUID,
        currentUserId: UUID
    ): DownloadFile {

        val fileObject = getUserFile(
            fileId,
            currentUserId
        )

        val bytes = minioStorageService.download(
            fileObject.storageKey
        )

        return DownloadFile(
            fileName = fileObject.originalFileName,
            contentType = fileObject.contentType,
            bytes = bytes
        )
    }

    @Transactional
    fun rename(
        fileId: UUID,
        request: RenameFileRequest,
        currentUserId: UUID
    ): FileResponse {

        val fileObject = getUserFile(
            fileId,
            currentUserId
        )

        fileObject.originalFileName =
            request.newFileName

        val updatedFile = fileObjectRepository.save(
            fileObject
        )

        outboxService.createFileEvent(
            FileEvent(
                fileId = updatedFile.id!!,
                ownerId = currentUserId,
                fileName = updatedFile.originalFileName,
                eventType = "FILE_RENAMED"
            )
        )

        log.info(
            "File renamed. userId={}, fileId={}, newName={}",
            currentUserId,
            updatedFile.id,
            updatedFile.originalFileName
        )

        return updatedFile.toResponse()
    }

    @Transactional
    fun delete(
        fileId: UUID,
        currentUserId: UUID
    ) {

        val fileObject = getUserFile(
            fileId,
            currentUserId
        )

        fileObject.deletedAt = Instant.now()

        fileObjectRepository.save(
            fileObject
        )

        outboxService.createFileEvent(
            FileEvent(
                fileId = fileObject.id!!,
                ownerId = currentUserId,
                fileName = fileObject.originalFileName,
                eventType = "FILE_DELETED"
            )
        )

        log.info(
            "File deleted. userId={}, fileId={}",
            currentUserId,
            fileObject.id
        )
    }

    private fun getUserFile(
        fileId: UUID,
        currentUserId: UUID
    ): FileObject {

        return fileObjectRepository
            .findByIdAndOwnerIdAndDeletedAtIsNull(
                fileId,
                currentUserId
            )
            ?: throw IllegalArgumentException(
                "File not found"
            )
    }

    private fun validateFile(
        file: MultipartFile
    ) {

        if (file.isEmpty) {
            throw IllegalArgumentException(
                "File is empty"
            )
        }

        if (file.size > 50 * 1024 * 1024) {
            throw IllegalArgumentException(
                "File is too large"
            )
        }
    }

    private fun FileObject.toResponse(): FileResponse {
        return FileResponse(
            id = this.id!!,
            originalFileName = this.originalFileName,
            contentType = this.contentType,
            size = this.size,
            createdAt = this.createdAt
        )
    }
}