package com.egprogteam.ecovklad.service.impl

import com.egprogteam.ecovklad.controller.dto.*
import com.egprogteam.ecovklad.domain.Role
import com.egprogteam.ecovklad.domain.User
import com.egprogteam.ecovklad.exception.*
import com.egprogteam.ecovklad.repository.UserRepository
import com.egprogteam.ecovklad.service.AuthService
import com.egprogteam.ecovklad.service.EmailService
import com.egprogteam.ecovklad.service.JwtTokenProvider
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.*

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val emailService: EmailService
) : AuthService {

    private val log = LoggerFactory.getLogger(AuthServiceImpl::class.java)
    private val CODE_EXPIRATION_MINUTES = 5L

    @Transactional
    override fun register(request: RegistrationRequest): UserSummaryResponse {
        log.info("Registering new user with email: {}", request.email)

        if (userRepository.existsByEmail(request.email)) {
            throw ConflictException("Пользователь с таким email уже существует")
        }

        if (userRepository.existsByUsername(request.username)) {
            throw ConflictException("Пользователь с таким именем уже существует")
        }

        val verificationCode = generateRandomCode()
        val user = User(
            username = request.username,
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            firstName = request.firstName,
            lastName = request.lastName,
            roles = mutableSetOf(Role.USER),
            emailVerificationCode = verificationCode,
            verificationCodeExpiry = OffsetDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES)
        )

        emailService.sendVerificationCode(user.email, verificationCode)
        val savedUser = userRepository.save(user)

        log.info("User registered successfully with id: {}", savedUser.id)
        return savedUser.toSummaryResponse()
    }

    @Transactional
    override fun verifyEmail(code: String): AuthResponse {
        val user = userRepository.findByEmailVerificationCode(code)
            ?: throw BadRequestException("Неверный код подтверждения")

        if (user.verificationCodeExpiry!!.isBefore(OffsetDateTime.now())) {
            throw BadRequestException("Срок действия кода истек")
        }

        user.emailVerified = true
        user.emailVerificationCode = null
        user.verificationCodeExpiry = null
        userRepository.save(user)

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(user.email, null)
        )

        return generateAuthResponse(authentication, user)
    }

    override fun login(request: LoginRequest): AuthResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.login, request.password)
        )

        val user = userRepository.findByEmail(authentication.name)
            ?: throw ResourceNotFoundException("Пользователь не найден")

        if (!user.emailVerified) {
            throw UnauthorizedException("Email не подтвержден")
        }

        return generateAuthResponse(authentication, user)
    }

    override fun loginWithCode(request: LoginWithCodeRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw ResourceNotFoundException("Пользователь не найден")

        if (user.emailVerificationCode != request.code) {
            throw BadRequestException("Неверный код подтверждения")
        }

        if (user.verificationCodeExpiry!!.isBefore(OffsetDateTime.now())) {
            throw BadRequestException("Срок действия кода истек")
        }

        user.emailVerificationCode = null
        user.verificationCodeExpiry = null
        userRepository.save(user)

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(user.email, null)
        )

        return generateAuthResponse(authentication, user)
    }

    @Transactional
    override fun resendVerificationCode(email: String) {
        val user = userRepository.findByEmail(email)
            ?: throw ResourceNotFoundException("Пользователь не найден")

        if (user.emailVerified) {
            throw BadRequestException("Email уже подтвержден")
        }

        val newCode = generateRandomCode()
        user.emailVerificationCode = newCode
        user.verificationCodeExpiry = OffsetDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES)
        userRepository.save(user)

        emailService.sendVerificationCode(email, newCode)
    }

    private fun generateAuthResponse(authentication: Authentication, user: User): AuthResponse {
        SecurityContextHolder.getContext().authentication = authentication
        val token = jwtTokenProvider.generateToken(authentication)
        
        return AuthResponse(
            accessToken = token,
            user = user.toSummaryResponse()
        )
    }

    private fun generateRandomCode(): String {
        return (100000..999999).random().toString()
    }

    override fun loginWithCode(request: LoginWithCodeRequest): AuthResponse {
    val user = userRepository.findByEmail(request.email)
        ?: throw ResourceNotFoundException("Пользователь не найден")

    if (user.emailVerificationCode != request.code) {
        throw BadRequestException("Неверный код подтверждения")
    }

    if (user.verificationCodeExpiry!!.isBefore(OffsetDateTime.now())) {
        throw BadRequestException("Срок действия кода истек")
    }

    // Очищаем код после успешного входа
    user.emailVerificationCode = null
    user.verificationCodeExpiry = null
    userRepository.save(user)

    // Создаем аутентификацию и токен
    val authentication = authenticationManager.authenticate(
        UsernamePasswordAuthenticationToken(user.email, null)
    )

    return generateAuthResponse(authentication, user)
}

private fun generateAuthResponse(authentication: Authentication, user: User): AuthResponse {
    val token = jwtTokenProvider.generateToken(authentication)
    return AuthResponse(
        accessToken = token,
        user = user.toSummaryResponse()
    )
}

    private fun User.toSummaryResponse(): UserSummaryResponse {
        return UserSummaryResponse(
            id = this.id,
            username = this.username,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName
        )
    }
}