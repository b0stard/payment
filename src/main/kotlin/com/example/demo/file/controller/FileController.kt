package com.example.demo.file.controller

import com.example.demo.file.FileResponse
import com.example.demo.file.RenameFileRequest
import com.example.demo.file.service.FileService
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.Principal
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
        @RequestParam("file") file: MultipartFile,
        principal: Principal
    ): FileResponse {
        val currentUserId = UUID.fromString(principal.name)

        return fileService.upload(file, currentUserId)
    }

    @GetMapping
    fun getMyFiles(
        principal: Principal
    ): List<FileResponse> {
        val currentUserId = UUID.fromString(principal.name)

        return fileService.getMyFiles(currentUserId)
    }

    @GetMapping("/{fileId}/download")
    fun download(
        @PathVariable fileId: UUID,
        principal: Principal
    ): ResponseEntity<ByteArray> {
        val currentUserId = UUID.fromString(principal.name)

        val file = fileService.download(fileId, currentUserId)

        val encodedFileName = URLEncoder.encode(
            file.fileName,
            StandardCharsets.UTF_8
        )

        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"$encodedFileName\""
            )
            .contentType(MediaType.parseMediaType(file.contentType))
            .body(file.bytes)
    }

    @PatchMapping("/{fileId}/rename")
    fun rename(
        @PathVariable fileId: UUID,
        @RequestBody @Valid request: RenameFileRequest,
        principal: Principal
    ): FileResponse {
        val currentUserId = UUID.fromString(principal.name)

        return fileService.rename(fileId, request, currentUserId)
    }

    @DeleteMapping("/{fileId}")
    fun delete(
        @PathVariable fileId: UUID,
        principal: Principal
    ) {
        val currentUserId = UUID.fromString(principal.name)

        fileService.delete(fileId, currentUserId)
    }
}