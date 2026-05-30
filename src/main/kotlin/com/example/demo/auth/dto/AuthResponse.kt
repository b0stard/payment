package com.example.demo.auth.dto

data class AuthResponse(
    val accessToken: String,
    val tokenType: String = "Bearer"
)