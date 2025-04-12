package com.egprogteam.ecovklad.exception

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import java.time.OffsetDateTime

@Schema(description = "Стандартизированный ответ при возникновении ошибки")
@JsonInclude(JsonInclude.Include.NON_NULL) // Не включать null поля в JSON
data class ErrorResponse(
    @Schema(description = "Время возникновения ошибки", example = "2025-04-11T23:35:09.12345+02:00")
    val timestamp: OffsetDateTime = OffsetDateTime.now(),

    @Schema(description = "HTTP статус код", example = "404")
    val status: Int,

    @Schema(description = "Краткое описание статуса HTTP", example = "Not Found")
    val error: String,

    @Schema(description = "Сообщение об ошибке", example = "Проект с ID 999 не найден")
    val message: String?,

    @Schema(description = "Путь запроса, который вызвал ошибку", example = "/api/v1/projects/999")
    val path: String,

    @Schema(description = "Детали валидации (если применимо)")
    val validationErrors: Map<String, String>? = null // Для ошибок валидации DTO
)