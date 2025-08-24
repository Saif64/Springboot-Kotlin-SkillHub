package com.pixelpeak.SkillHub.features.auth

import com.pixelpeak.SkillHub.configs.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService
) {

    fun register(request: RegisterRequest): AuthResponse {
        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password)
        )
        authRepository.save(user)
        val userDetails = org.springframework.security.core.userdetails.User(user.username, user.password, emptyList())
        val accessToken = jwtUtil.generateAccessToken(userDetails)
        val refreshToken = jwtUtil.generateRefreshToken(userDetails)
        return AuthResponse(accessToken, refreshToken)
    }

    fun login(request: LoginRequest): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )
        val user = authRepository.findByUsername(request.username)!!
        val userDetails = org.springframework.security.core.userdetails.User(user.username, user.password, emptyList())
        val accessToken = jwtUtil.generateAccessToken(userDetails)
        val refreshToken = jwtUtil.generateRefreshToken(userDetails)
        return AuthResponse(accessToken, refreshToken)
    }

    fun refreshToken(request: RefreshTokenRequest): AuthResponse {
        val username = jwtUtil.extractUsername(request.refreshToken)
        val userDetails = this.userDetailsService.loadUserByUsername(username)
        if (jwtUtil.isTokenValid(request.refreshToken, userDetails)) {
            val accessToken = jwtUtil.generateAccessToken(userDetails)
            return AuthResponse(accessToken, request.refreshToken)
        }
        throw Exception("Invalid refresh token")
    }
}