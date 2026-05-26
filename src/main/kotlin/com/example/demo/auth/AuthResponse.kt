package com.example.demo.auth

data class AuthResponse(
    val accessToken: String,
    val tokenType: String = "Bearer"
)