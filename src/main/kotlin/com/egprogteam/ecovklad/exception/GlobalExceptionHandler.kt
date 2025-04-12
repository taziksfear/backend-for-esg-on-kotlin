package com.egprogteam.ecovklad.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException // Для ошибок авторизации
import org.springframework.web.bind.MethodArgumentNotValidException // Для ошибок валидации DTO
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice // Делает этот класс глобальным обработчиком исключений для @RestController
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    // Обработчик для ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(
        ex: ResourceNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("Resource not found: {}", ex.message)
        val status = HttpStatus.NOT_FOUND
        val errorResponse = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.getDescription(false).substringAfter("uri=") // Получаем путь запроса
        )
        return ResponseEntity(errorResponse, status)
    }

    // Обработчик для BadRequestException
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(
        ex: BadRequestException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("Bad request: {}", ex.message)
        val status = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.getDescription(false).substringAfter("uri=")
        )
        return ResponseEntity(errorResponse, status)
    }

    // Обработчик для ConflictException
    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(
        ex: ConflictException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("Conflict detected: {}", ex.message)
        val status = HttpStatus.CONFLICT
        val errorResponse = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            path = request.getDescription(false).substringAfter("uri=")
        )
        return ResponseEntity(errorResponse, status)
    }

     // Обработчик для PaymentException
    @ExceptionHandler(PaymentException::class)
    fun handlePaymentException(
        ex: PaymentException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        log.error("Payment processing error: {}", ex.message, ex.cause) // Логируем с причиной
        // Решаем, какой статус вернуть - часто это ошибка на нашей стороне или стороне шлюза
        val status = HttpStatus.INTERNAL_SERVER_ERROR // Или BAD_REQUEST, в зависимости от контекста
        val errorResponse = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = "Ошибка обработки платежа: ${ex.message}", // Можно дать общее сообщение клиенту
            path = request.getDescription(false).substringAfter("uri=")
        )
        return ResponseEntity(errorResponse, status)
    }

    // Обработчик для ошибок валидации DTO (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("Validation failed: {}", ex.message)
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        val status = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = "Ошибка валидации данных",
            path = request.getDescription(false).substringAfter("uri="),
            validationErrors = errors // Добавляем детали ошибок валидации
        )
        return ResponseEntity(errorResponse, status)
    }

    // Обработчик для ошибок парсинга тела запроса
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("Failed to read request body: {}", ex.message)
        val status = HttpStatus.BAD_REQUEST
        val errorResponse = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = "Некорректный формат тела запроса",
            path = request.getDescription(false).substringAfter("uri=")
        )
        return ResponseEntity(errorResponse, status)
    }

     // Обработчик для ошибок авторизации (доступ запрещен)
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: AccessDeniedException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        log.warn("Access denied: {}", ex.message)
        val status = HttpStatus.FORBIDDEN
        val errorResponse = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = "Доступ запрещен", // Обычно не показываем детали
            path = request.getDescription(false).substringAfter("uri=")
        )
        return ResponseEntity(errorResponse, status)
    }

    // Общий обработчик для всех остальных исключений (Fallback)
    @ExceptionHandler(Exception::class)
    fun handleAllOtherExceptions(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        log.error("An unexpected error occurred: {}", ex.message, ex) // Логируем полное исключение
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val errorResponse = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = "Внутренняя ошибка сервера. Пожалуйста, попробуйте позже.", // Общее сообщение для клиента
            path = request.getDescription(false).substringAfter("uri=")
        )
        return ResponseEntity(errorResponse, status)
    }
}