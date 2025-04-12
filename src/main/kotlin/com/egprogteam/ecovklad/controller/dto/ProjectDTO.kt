package com.egprogteam.ecovklad.controller.dto

import com.egprogteam.ecovklad.domain.ProjectStatus // Импортируем Enum статуса
import io.swagger.v3.oas.annotations.media.Schema // Для документации Swagger
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

// --- Data Transfer Objects (DTO) для Проектов ---

@Schema(description = "Данные для создания нового проекта")
data class CreateProjectRequest(
    @field:NotBlank(message = "Название проекта не может быть пустым")
    @field:Size(min = 3, max = 255, message = "Длина названия должна быть от 3 до 255 символов")
    @Schema(description = "Название проекта", example = "Посадка деревьев в парке 'Лесной'")
    val title: String,

    @field:NotBlank(message = "Описание проекта не может быть пустым")
    @field:Size(min = 50, max = 5000, message = "Длина описания должна быть от 50 до 5000 символов")
    @Schema(description = "Подробное описание цели, методов и ожидаемого результата проекта", example = "Цель проекта - посадить 100 новых деревьев...")
    val description: String,

    @field:NotNull(message = "Целевая сумма для сбора не может быть пустой")
    @field:DecimalMin(value = "1000.0", message = "Минимальная целевая сумма: 1000")
    @Schema(description = "Сумма, необходимая для реализации проекта", example = "50000.00")
    val goalAmount: BigDecimal,

    @field:NotNull(message = "Дата окончания сбора средств не может быть пустой")
    @field:Future(message = "Дата окончания сбора должна быть в будущем") // Проверяем, что дата в будущем
    @Schema(description = "Дата, до которой необходимо собрать средства", example = "2025-12-31")
    val deadline: LocalDate,

    // Примечание: creatorUserId в реальном приложении будет браться из контекста безопасности (аутентифицированного пользователя),
    // а не передаваться явно в DTO. Пока оставим для простоты.
    @field:NotNull(message = "ID создателя проекта обязателен")
    @Schema(description = "Идентификатор пользователя, создающего проект (временно, будет из SecurityContext)", example = "1")
    val creatorUserId: Long
)

@Schema(description = "Данные для обновления существующего проекта")
data class UpdateProjectRequest(
    // Поля для обновления могут отличаться от полей создания.
    // Например, целевую сумму или создателя менять нельзя. Статус меняется отдельно.

    @field:NotBlank(message = "Название проекта не может быть пустым")
    @field:Size(min = 3, max = 255, message = "Длина названия должна быть от 3 до 255 символов")
    @Schema(description = "Новое название проекта", example = "Обновленная Посадка деревьев в парке 'Лесной'")
    val title: String,

    @field:NotBlank(message = "Описание проекта не может быть пустым")
    @field:Size(min = 50, max = 5000, message = "Длина описания должна быть от 50 до 5000 символов")
    @Schema(description = "Новое подробное описание проекта", example = "Обновленная цель проекта - посадить 150 новых деревьев...")
    val description: String,

    @field:NotNull(message = "Дата окончания сбора средств не может быть пустой")
    @field:Future(message = "Дата окончания сбора должна быть в будущем")
    @Schema(description = "Новая дата, до которой необходимо собрать средства", example = "2026-01-31")
    val deadline: LocalDate
)

@Schema(description = "Представление проекта для ответа API")
data class ProjectResponse(
    @Schema(description = "Уникальный идентификатор проекта", example = "1")
    val id: Long,

    @Schema(description = "Название проекта", example = "Посадка деревьев в парке 'Лесной'")
    val title: String,

    @Schema(description = "Подробное описание проекта", example = "Цель проекта - посадить 100 новых деревьев...")
    val description: String,

    @Schema(description = "Целевая сумма для сбора", example = "50000.00")
    val goalAmount: BigDecimal,

    @Schema(description = "Текущая собранная сумма", example = "15250.50")
    val currentAmount: BigDecimal,

    @Schema(description = "Дата окончания сбора средств", example = "2025-12-31")
    val deadline: LocalDate,

    @Schema(description = "Текущий статус проекта", example = "ACTIVE")
    val status: ProjectStatus,

    @Schema(description = "Идентификатор пользователя-создателя", example = "1")
    val creatorUserId: Long, // В будущем можно заменить на UserShortResponse DTO

    @Schema(description = "Дата и время создания проекта")
    val createdAt: OffsetDateTime,

    @Schema(description = "Дата и время последнего обновления проекта")
    val updatedAt: OffsetDateTime,

    // В будущем можно добавить информацию о рейтинге ESG
    @Schema(description = "Идентификатор связанного ESG рейтинга (если есть)", example = "10", nullable = true)
    val esgRatingId: Long? = null // В будущем можно заменить на EsgRatingResponse DTO
)