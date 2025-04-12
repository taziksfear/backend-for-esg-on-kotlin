package com.egprogteam.ecovklad.service

import com.egprogteam.ecovklad.controller.dto.AuthResponse
import com.egprogteam.ecovklad.controller.dto.LoginRequest
import com.egprogteam.ecovklad.controller.dto.RegistrationRequest
import com.egprogteam.ecovklad.controller.dto.UserSummaryResponse

interface AuthService {

    /**
     * Регистрирует нового пользователя.
     * @param registrationRequest Данные для регистрации.
     * @return Данные зарегистрированного пользователя.
     * @throws ConflictException если пользователь с таким username или email уже существует.
     */
    fun register(registrationRequest: RegistrationRequest): UserSummaryResponse

    /**
     * Аутентифицирует пользователя и выдает токен доступа.
     * @param loginRequest Данные для входа (логин/email и пароль).
     * @return AuthResponse с токеном доступа и информацией о пользователе.
     * @throws org.springframework.security.core.AuthenticationException если аутентификация не удалась.
     */
    fun login(loginRequest: LoginRequest): AuthResponse

    // TODO: Методы для refresh token, password reset и т.д.
}