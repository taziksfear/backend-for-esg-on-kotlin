package com.egprogteam.ecovklad.service

import com.egprogteam.ecovklad.controller.dto.CreateProjectRequest
import com.egprogteam.ecovklad.controller.dto.ProjectResponse
import com.egprogteam.ecovklad.controller.dto.UpdateProjectRequest
import com.egprogteam.ecovklad.domain.ProjectStatus

// Интерфейс сервиса для управления проектами
interface ProjectService {

    /**
     * Находит все проекты, опционально фильтруя по статусу.
     * @param status Опциональный статус для фильтрации.
     * @return Список проектов в формате ProjectResponse.
     */
    fun findAll(status: ProjectStatus? = null): List<ProjectResponse>

    /**
     * Находит проект по его уникальному идентификатору.
     * @param id Уникальный идентификатор проекта.
     * @return Найденный проект в формате ProjectResponse.
     * @throws ResourceNotFoundException если проект с таким ID не найден.
     */
    fun findById(id: Long): ProjectResponse

    /**
     * Создает новый проект.
     * @param request DTO с данными для создания проекта.
     * @return Созданный проект в формате ProjectResponse.
     * @throws // Могут быть исключения, связанные с правами доступа или невалидными данными (позже).
     */
    fun create(request: CreateProjectRequest): ProjectResponse

    /**
     * Обновляет существующий проект.
     * @param id ID обновляемого проекта.
     * @param request DTO с новыми данными для проекта.
     * @return Обновленный проект в формате ProjectResponse.
     * @throws ResourceNotFoundException если проект с таким ID не найден.
     * @throws // Могут быть исключения, связанные с правами доступа (позже).
     */
    fun update(id: Long, request: UpdateProjectRequest): ProjectResponse

    /**
     * Удаляет проект по ID (или меняет его статус на удаленный).
     * @param id ID удаляемого проекта.
     * @throws ResourceNotFoundException если проект с таким ID не найден.
     * @throws // Могут быть исключения, связанные с правами доступа (позже).
     */
    fun delete(id: Long)

    // --- Другие возможные методы ---
    // fun approveProject(id: Long): ProjectResponse
    // fun rejectProject(id: Long): ProjectResponse
    // fun addInvestment(projectId: Long, amount: BigDecimal, userId: Long): InvestmentResponse // Перенести в InvestmentService?
    // fun calculateProjectEsgRating(id: Long): EsgRatingResponse // Перенести в EsgRatingService?
}