package com.egprogteam.ecovklad.controller

import com.egprogteam.ecovklad.controller.dto.AuthResponse
import com.egprogteam.ecovklad.controller.dto.LoginRequest
import com.egprogteam.ecovklad.controller.dto.RegistrationRequest
import com.egprogteam.ecovklad.controller.dto.UserSummaryResponse // Импорт DTO для ответа при регистрации
import com.egprogteam.ecovklad.service.AuthService // Импортируем интерфейс сервиса аутентификации
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth") // Базовый путь для эндпоинтов аутентификации
@Tag(name = "Authentication", description = "API для регистрации и входа пользователей")
class AuthController(
    private val authService: AuthService // Внедряем сервис аутентификации
) {

    @Operation(
        summary = "Регистрация нового пользователя",
        description = "Создает новый аккаунт пользователя в системе.",
        responses = [
            ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован",
                        content = [Content(mediaType = "application/json", schema = Schema(implementation = UserSummaryResponse::class))]),
            ApiResponse(responseCode = "400", description = "Неверные данные запроса (ошибка валидации или пользователь уже существует)", content = [Content()])
        ]
    )
    @PostMapping("/register")
    fun registerUser(
        @Valid @RequestBody registrationRequest: RegistrationRequest
    ): ResponseEntity<UserSummaryResponse> {
        // Делегируем логику регистрации сервису
        val registeredUser = authService.register(registrationRequest)
        // Возвращаем данные созданного пользователя и статус 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser)
    }

    @Operation(
        summary = "Вход пользователя в систему",
        description = "Аутентифицирует пользователя и возвращает токен доступа (JWT).",
        responses = [
            ApiResponse(responseCode = "200", description = "Успешный вход",
                        content = [Content(mediaType = "application/json", schema = Schema(implementation = AuthResponse::class))]),
            ApiResponse(responseCode = "400", description = "Неверные данные запроса (ошибка валидации)", content = [Content()]),
            ApiResponse(responseCode = "401", description = "Неверные учетные данные (ошибка аутентификации)", content = [Content()])
        ]
    )
    @PostMapping("/login")
    fun loginUser(
        @Valid @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<AuthResponse> {
        // Делегируем логику аутентификации и генерации токена сервису
        val authResponse = authService.login(loginRequest)
        return ResponseEntity.ok(authResponse)
    }

    @PostMapping("/login-with-code")
    @Operation(summary = "Вход по коду подтверждения")
    fun loginWithCode(
        @RequestBody @Valid request: LoginWithCodeRequest
    ): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.loginWithCode(request))
    }

    @PostMapping("/verify")
    fun verifyEmail(
        @RequestBody request: VerifyEmailRequest
    ): ResponseEntity<MessageResponse> {
        authService.verifyEmail(request.code)
        return ResponseEntity.ok(MessageResponse("Email успешно подтвержден"))
    }

    @PostMapping("/resend-code")
    fun resendVerificationCode(
        @RequestParam email: String
    ): ResponseEntity<MessageResponse> {
        authService.resendVerificationCode(email)
        return ResponseEntity.ok(MessageResponse("Новый код отправлен на email"))
    }

    // Сюда можно добавить эндпоинты для:
    // - Обновления токена (refresh token)
    // - Сброса пароля
    // - Подтверждения email
}