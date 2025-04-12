package com.egprogteam.ecovklad.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

@Schema(description = "Данные для входа по коду подтверждения")
data class LoginWithCodeRequest(
    @field:Email
    @Schema(description = "Email пользователя", example = "user@example.com")
    val email: String,
    
    @field:Size(min = 6, max = 6, message = "Код должен содержать 6 цифр")
    @Schema(description = "6-значный код подтверждения", example = "123456")
    val code: String
)