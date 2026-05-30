package com.example.demo.file.repository

import com.example.demo.file.model.FileObject
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface FileObjectRepository : JpaRepository<FileObject, UUID> {

    fun findByIdAndOwnerIdAndDeletedAtIsNull(
        id: UUID,
        ownerId: UUID
    ): FileObject?

    fun findAllByOwnerIdAndDeletedAtIsNullOrderByCreatedAtDesc(
        ownerId: UUID
    ): List<FileObject>
}