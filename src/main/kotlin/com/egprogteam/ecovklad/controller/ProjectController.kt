package com.egprogteam.ecovklad.controller

import com.egprogteam.ecovklad.controller.dto.CreateProjectRequest
import com.egprogteam.ecovklad.controller.dto.ProjectResponse
import com.egprogteam.ecovklad.controller.dto.UpdateProjectRequest
import com.egprogteam.ecovklad.domain.ProjectStatus
import com.egprogteam.ecovklad.service.ProjectService // Импортируем интерфейс сервиса
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid // Для включения валидации DTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
// import org.springframework.security.access.prepost.PreAuthorize // Для авторизации (позже)

@RestController
@RequestMapping("/api/v1/projects") // Базовый путь для всех эндпоинтов этого контроллера
@Tag(name = "Projects", description = "API для управления ESG проектами")
class ProjectController(
    // Внедряем зависимость через конструктор - рекомендуемый способ в Spring/Kotlin
    private val projectService: ProjectService
) {

    @Operation(
        summary = "Получить список проектов",
        description = "Возвращает список всех проектов. Можно фильтровать по статусу.",
        responses = [
            ApiResponse(responseCode = "200", description = "Список проектов успешно получен")
        ]
    )
    @GetMapping
    fun getAllProjects(
        @Parameter(description = "Фильтр по статусу проекта (опционально)")
        @RequestParam(required = false) status: ProjectStatus?
    ): ResponseEntity<List<ProjectResponse>> {
        // Делегируем вызов сервису
        val projects = projectService.findAll(status)
        return ResponseEntity.ok(projects) // Возвращаем список и статус 200 OK
    }

    @Operation(
        summary = "Получить проект по ID",
        responses = [
            ApiResponse(responseCode = "200", description = "Проект найден"),
            ApiResponse(responseCode = "404", description = "Проект не найден", content = [Content()]) // Пустой контент для 404
        ]
    )
    @GetMapping("/{id}")
    fun getProjectById(
        @Parameter(description = "Уникальный идентификатор проекта") @PathVariable id: Long
    ): ResponseEntity<ProjectResponse> {
        val project = projectService.findById(id)
        // Обработка 404 Not Found будет происходить через GlobalExceptionHandler, если сервис выбросит ResourceNotFoundException
        return ResponseEntity.ok(project)
    }

    @Operation(
        summary = "Создать новый проект",
        description = "Создает новый проект со статусом PENDING_APPROVAL. Требует аутентификации.",
        responses = [
            ApiResponse(responseCode = "201", description = "Проект успешно создан"),
            ApiResponse(responseCode = "400", description = "Неверные данные запроса (ошибка валидации)", content = [Content()])
            // ApiResponse(responseCode = "401", description = "Требуется аутентификация", content = [Content()]) // Позже
        ]
    )
    @PostMapping
    // @PreAuthorize("isAuthenticated()") // Пример защиты эндпоинта (позже)
    fun createProject(
        @Parameter(description = "Данные для создания проекта") @Valid @RequestBody request: CreateProjectRequest
    ): ResponseEntity<ProjectResponse> {
        // @Valid включает валидацию DTO на основе аннотаций
        val createdProject = projectService.create(request)
        // Возвращаем созданный проект и статус 201 Created
        // В идеале, нужно добавить заголовок 'Location' с URI нового ресурса
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject)
    }

    @Operation(
        summary = "Обновить существующий проект",
        description = "Позволяет создателю обновить информацию о своем проекте. Требует аутентификации и авторизации.",
        responses = [
            ApiResponse(responseCode = "200", description = "Проект успешно обновлен"),
            ApiResponse(responseCode = "400", description = "Неверные данные запроса", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Проект не найден", content = [Content()]),
            // ApiResponse(responseCode = "401", description = "Требуется аутентификация", content = [Content()]), // Позже
            // ApiResponse(responseCode = "403", description = "Доступ запрещен (не владелец)", content = [Content()]) // Позже
        ]
    )
    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or @projectSecurityService.isOwner(#id, principal.username)") // Пример сложной авторизации (позже)
    fun updateProject(
        @Parameter(description = "ID обновляемого проекта") @PathVariable id: Long,
        @Parameter(description = "Новые данные для проекта") @Valid @RequestBody request: UpdateProjectRequest
    ): ResponseEntity<ProjectResponse> {
        val updatedProject = projectService.update(id, request)
        return ResponseEntity.ok(updatedProject)
    }

    @Operation(
        summary = "Удалить проект",
        description = "Позволяет создателю или администратору удалить проект (обычно это смена статуса на DELETED). Требует аутентификации и авторизации.",
        responses = [
            ApiResponse(responseCode = "204", description = "Проект успешно удален (или помечен как удаленный)"),
            ApiResponse(responseCode = "404", description = "Проект не найден", content = [Content()]),
            // ApiResponse(responseCode = "401", description = "Требуется аутентификация", content = [Content()]), // Позже
            // ApiResponse(responseCode = "403", description = "Доступ запрещен", content = [Content()]) // Позже
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Статус 204 No Content для успешного удаления без тела ответа
    // @PreAuthorize("hasRole('ADMIN') or @projectSecurityService.isOwner(#id, principal.username)") // Пример (позже)
    fun deleteProject(
        @Parameter(description = "ID удаляемого проекта") @PathVariable id: Long
    ) {
        projectService.delete(id)
    }

    // --- Другие возможные эндпоинты для проектов ---
    // Например:
    // POST /api/v1/projects/{id}/approve - Одобрить проект (для админа)
    // POST /api/v1/projects/{id}/reject - Отклонить проект (для админа)
    // GET /api/v1/users/{userId}/projects - Получить проекты конкретного пользователя
}