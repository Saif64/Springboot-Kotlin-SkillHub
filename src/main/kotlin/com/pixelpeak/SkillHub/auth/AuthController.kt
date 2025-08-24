package com.pixelpeak.SkillHub.features.auth


import com.pixelpeak.SkillHub.util.ApiResponse
import com.pixelpeak.SkillHub.util.buildSuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        return buildSuccessResponse(authService.register(request))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        return buildSuccessResponse(authService.login(request))
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        return buildSuccessResponse(authService.refreshToken(request))
    }
}