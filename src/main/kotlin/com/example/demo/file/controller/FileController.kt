package com.example.demo.file.controller

import com.example.demo.file.FileResponse
import com.example.demo.file.RenameFileRequest
import com.example.demo.file.service.FileService
import jakarta.validation.Valid
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/api/files")
class FileController(
    private val fileService: FileService
) {

    @PostMapping(
        "/upload",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun upload(
        @RequestParam("file")
        file: MultipartFile,

        authentication: Authentication
    ): FileResponse {

        val currentUserId =
            UUID.fromString(authentication.name)

        return fileService.upload(
            file = file,
            currentUserId = currentUserId
        )
    }

    @GetMapping
    fun getMyFiles(
        authentication: Authentication
    ): List<FileResponse> {

        val currentUserId =
            UUID.fromString(authentication.name)

        return fileService.getMyFiles(
            currentUserId
        )
    }

    @GetMapping("/{fileId}/download")
    fun download(
        @PathVariable
        fileId: UUID,
        authentication: Authentication
    ): ResponseEntity<ByteArrayResource> {

        val currentUserId =
            UUID.fromString(authentication.name)

        val file = fileService.download(
            fileId = fileId,
            currentUserId = currentUserId
        )

        return ResponseEntity.ok()
            .contentType(
                MediaType.parseMediaType(
                    file.contentType
                )
            )
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"${file.fileName}\""
            )
            .body(
                ByteArrayResource(file.bytes)
            )
    }

    @PatchMapping("/{fileId}/rename")
    fun rename(
        @PathVariable
        fileId: UUID,

        @Valid
        @RequestBody
        request: RenameFileRequest,

        authentication: Authentication
    ): FileResponse {

        val currentUserId =
            UUID.fromString(authentication.name)

        return fileService.rename(
            fileId = fileId,
            request = request,
            currentUserId = currentUserId
        )
    }

    @DeleteMapping("/{fileId}")
    fun delete(
        @PathVariable
        fileId: UUID,
        authentication: Authentication
    ) {

        val currentUserId =
            UUID.fromString(authentication.name)

        fileService.delete(
            fileId = fileId,
            currentUserId = currentUserId
        )
    }
}