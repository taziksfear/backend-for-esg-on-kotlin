package com.egprogteam.ecovklad.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// --- Data Transfer Objects (DTO) для Пользователей и Аутентификации ---

@Schema(description = "Данные для регистрации нового пользователя")
data class RegistrationRequest(

    @field:NotBlank(message = "Имя пользователя не может быть пустым")
    @field:Size(min = 3, max = 50, message = "Длина имени пользователя от 3 до 50 символов")
    @Schema(description = "Уникальное имя пользователя (логин)", example = "john_doe")
    val username: String,

    @field:NotBlank(message = "Email не может быть пустым")
    @field:Email(message = "Некорректный формат Email")
    @field:Size(max = 100, message = "Максимальная длина Email 100 символов")
    @Schema(description = "Адрес электронной почты пользователя (должен быть уникальным)", example = "john.doe@example.com")
    val email: String,

    @field:NotBlank(message = "Пароль не может быть пустым")
    @field:Size(min = 8, max = 100, message = "Длина пароля от 8 до 100 символов")
    @Schema(description = "Пароль пользователя (будет хешироваться)", example = "Str0ngP@ssw0rd")
    val password: String,

    // Можно добавить другие поля, например, firstName, lastName, если нужно
    @field:Size(max = 100, message = "Максимальная длина имени 100 символов")
    @Schema(description = "Имя пользователя (опционально)", example = "John", required = false)
    val firstName: String? = null,

    @field:Size(max = 100, message = "Максимальная длина фамилии 100 символов")
    @Schema(description = "Фамилия пользователя (опционально)", example = "Doe", required = false)
    val lastName: String? = null
)

@Schema(description = "Данные для входа пользователя в систему")
data class LoginRequest(
    @field:NotBlank(message = "Email или имя пользователя не могут быть пустыми")
    @Schema(description = "Email или имя пользователя для входа", example = "john.doe@example.com")
    val login: String, // Поле может содержать email или username

    @field:NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "Пароль пользователя", example = "Str0ngP@ssw0rd")
    val password: String
)

@Schema(description = "Ответ с токеном после успешной аутентификации")
data class AuthResponse(
    @Schema(description = "Токен доступа (JWT)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val accessToken: String,

    @Schema(description = "Тип токена", example = "Bearer")
    val tokenType: String = "Bearer",

    // Можно добавить время жизни токена или информацию о пользователе
    @Schema(description = "Время жизни токена в секундах", example = "3600")
    val expiresIn: Long? = null,

    @Schema(description = "Краткая информация об аутентифицированном пользователе")
    val user: UserSummaryResponse? = null // Используем UserSummaryResponse
)

@Schema(description = "Краткое представление пользователя для API ответов")
data class UserSummaryResponse(
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    val id: Long,

    @Schema(description = "Имя пользователя (логин)", example = "john_doe")
    val username: String,

    @Schema(description = "Email пользователя", example = "john.doe@example.com")
    val email: String,

    @Schema(description = "Имя пользователя", example = "John")
    val firstName: String?,

    @Schema(description = "Фамилия пользователя", example = "Doe")
    val lastName: String?

    // Не включаем пароль и другую чувствительную информацию
    // Можно добавить роли пользователя, если они есть
    // val roles: List<String>
)

// Можно также создать более подробный UserResponse для профиля пользователя, если нужно