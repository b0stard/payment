package com.example.demo.auth.service

import com.example.demo.auth.AuthResponse
import com.example.demo.auth.LoginRequest
import com.example.demo.auth.RegisterRequest
import com.example.demo.security.JwtTokenProvider
import com.example.demo.user.model.User
import com.example.demo.user.repository.UserRepository
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
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("User not found")
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Passwords do not match")
        }
        val token = jwtTokenProvider.generateToken(user.id!!)
        return AuthResponse(
            accessToken = token
        )
    }
}