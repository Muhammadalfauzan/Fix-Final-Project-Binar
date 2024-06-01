package com.example.finalprojectbinar.model

data class ErrorResponse(
    val status: String,
    val code: Int,
    val message: String,
    val error: String?
)