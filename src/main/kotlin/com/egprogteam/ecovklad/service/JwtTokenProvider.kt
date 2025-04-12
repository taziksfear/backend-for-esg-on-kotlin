package com.egprogteam.ecovklad.service

import org.springframework.security.core.Authentication

// Интерфейс для компонента, отвечающего за работу с JWT токенами
interface JwtTokenProvider {

    /**
     * Генерирует JWT токен для аутентифицированного пользователя.
     * @param authentication Объект Authentication, содержащий детали пользователя.
     * @return Строка с JWT токеном.
     */
    fun generateToken(authentication: Authentication): String

    // TODO: Методы для валидации токена, извлечения username из токена и т.д.
    // fun validateToken(token: String): Boolean
    // fun getUsernameFromToken(token: String): String
}