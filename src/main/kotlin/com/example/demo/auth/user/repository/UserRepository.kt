package com.example.demo.auth.user.repository


import com.example.demo.auth.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}