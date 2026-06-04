package com.example.demo.auth.controller

import com.example.demo.auth.dto.AuthResponse
import com.example.demo.auth.dto.LoginRequest
import com.example.demo.auth.dto.RegisterRequest
import com.example.demo.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @RequestBody @Valid request: RegisterRequest
    ): AuthResponse {
        return authService.register(request)
    }
    @PostMapping("/login")
    fun login(
        @RequestBody @Valid request: LoginRequest
    ): AuthResponse {
        return authService.login(request)
    }
}