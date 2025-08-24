package com.pixelpeak.SkillHub.features.auth

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String? = null,
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)