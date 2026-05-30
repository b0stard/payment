package com.example.demo.file.repository

import com.example.demo.file.model.FileObject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
@Repository
interface FileObjectRepository : JpaRepository<FileObject, UUID> {

    fun findByIdAndOwnerIdAndDeletedAtIsNull(
        id: UUID,
        ownerId: UUID
    ): FileObject?

    fun findAllByOwnerIdAndDeletedAtIsNullOrderByCreatedAtDesc(
        ownerId: UUID
    ): List<FileObject>
}