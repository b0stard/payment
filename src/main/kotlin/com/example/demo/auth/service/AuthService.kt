package com.example.demo.auth.service

import com.example.demo.auth.dto.AuthResponse
import com.example.demo.auth.dto.LoginRequest
import com.example.demo.auth.dto.RegisterRequest
import com.example.demo.security.JwtTokenProvider
import com.example.demo.auth.user.model.User
import com.example.demo.auth.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already in use")
        }
        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password)
        )
        val savedUser = userRepository.save(user)
        val token = jwtTokenProvider.generateToken(savedUser.id!!)
        return AuthResponse(
            accessToken = token
        )
    }

    @Transactional
    fun login(request: LoginRequest): AuthResponse {

        println("STEP 1")

        val user = userRepository.findByEmail(
            request.email
        ) ?: throw IllegalArgumentException(
            "User not found"
        )

        println("STEP 2")

        val isPasswordCorrect =
            passwordEncoder.matches(
                request.password,
                user.password
            )

        println("STEP 3")

        if (!isPasswordCorrect) {
            throw IllegalArgumentException(
                "Invalid credentials"
            )
        }

        println("STEP 4")

        val token =
            jwtTokenProvider.generateToken(
                user.id!!
            )

        println("STEP 5")

        return AuthResponse(
            accessToken = token
        )
    }
}