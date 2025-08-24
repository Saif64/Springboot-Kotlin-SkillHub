package com.pixelpeak.SkillHub.util

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val statusCode: Int,
    val data: T? = null,
    val error: ApiError? = null
)

data class ApiError(
    val message: String,
    val details: String? = null
)

fun <T> buildSuccessResponse(data: T, status: HttpStatus = HttpStatus.OK): ResponseEntity<ApiResponse<T>> {
    val apiResponse = ApiResponse(
        success = true,
        statusCode = status.value(),
        data = data
    )
    return ResponseEntity.status(status).body(apiResponse)
}

fun <T> buildNotFoundResponse(message: String, details: String? = null): ResponseEntity<ApiResponse<T>> {
    val apiResponse = ApiResponse<T>(
        success = false,
        statusCode = HttpStatus.NOT_FOUND.value(),
        error = ApiError(message, details)
    )
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse)
}