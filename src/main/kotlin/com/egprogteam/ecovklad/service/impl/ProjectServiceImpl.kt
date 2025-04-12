package com.egprogteam.ecovklad.service.impl

import com.egprogteam.ecovklad.controller.dto.CreateProjectRequest
import com.egprogteam.ecovklad.controller.dto.ProjectResponse
import com.egprogteam.ecovklad.controller.dto.UpdateProjectRequest
import com.egprogteam.ecovklad.domain.Project // Импортируем сущность
import com.egprogteam.ecovklad.domain.ProjectStatus
import com.egprogteam.ecovklad.domain.User // Понадобится для связи с создателем
import com.egprogteam.ecovklad.exception.ResourceNotFoundException
import com.egprogteam.ecovklad.repository.ProjectRepository // Импортируем репозиторий
import com.egprogteam.ecovklad.repository.UserRepository // Нужен для поиска создателя
import com.egprogteam.ecovklad.service.ProjectService
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull // Удобный extension для Optional
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional // Для управления транзакциями

@Service // Помечаем класс как сервис Spring
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository // Внедряем репозиторий пользователей
    // Сюда можно будет добавить EsgRatingService, NotificationService и т.д.
) : ProjectService {

    private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)

    @Transactional(readOnly = true) // Транзакция только для чтения
    override fun findAll(status: ProjectStatus?): List<ProjectResponse> {
        log.info("Finding all projects with status: {}", status)
        val projects = if (status != null) {
            projectRepository.findByStatus(status)
        } else {
            projectRepository.findAll()
        }
        return projects.map { it.toResponse() } // Конвертируем каждую сущность в DTO
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): ProjectResponse {
        log.info("Finding project by id: {}", id)
        val project = findProjectEntityById(id) // Используем внутренний метод для поиска и обработки ошибки
        return project.toResponse()
    }

    @Transactional // Транзакция на запись (создание)
    override fun create(request: CreateProjectRequest): ProjectResponse {
        log.info("Creating new project with title: {}", request.title)

        // ВАЖНО: Получение текущего пользователя
        // В реальном приложении ID пользователя нужно брать из SecurityContextHolder
        // val currentUsername = SecurityContextHolder.getContext().authentication.name
        // val creator = userRepository.findByUsername(currentUsername) ?: throw ResourceNotFoundException("User not found")
        // Пока используем ID из запроса (НЕБЕЗОПАСНО ДЛЯ ПРОДА)
        val creator = userRepository.findByIdOrNull(request.creatorUserId)
            ?: throw ResourceNotFoundException("Пользователь-создатель с ID ${request.creatorUserId} не найден")

        // TODO: Проверить, имеет ли пользователь право создавать проекты (роль PROJECT_CREATOR?)

        val project = Project(
            title = request.title,
            description = request.description,
            goalAmount = request.goalAmount,
            deadline = request.deadline,
            creator = creator, // Устанавливаем связь с сущностью User
            status = ProjectStatus.PENDING_APPROVAL // Начальный статус
            // currentAmount по умолчанию 0
        )

        // TODO: Возможно, здесь нужно инициировать начальную ESG оценку (вызвать EsgRatingService)

        val savedProject = projectRepository.save(project)
        log.info("Project created with id: {}", savedProject.id)
        return savedProject.toResponse()
    }

    @Transactional // Транзакция на запись (обновление)
    override fun update(id: Long, request: UpdateProjectRequest): ProjectResponse {
        log.info("Updating project with id: {}", id)
        val existingProject = findProjectEntityById(id)

        // ВАЖНО: Проверка прав доступа
        // Нужно убедиться, что текущий аутентифицированный пользователь является создателем этого проекта
        // или администратором. Примерная логика (потребует SecurityService/SecurityContext):
        // checkAccess(existingProject.creator.id)

        // Обновляем поля сущности из DTO
        existingProject.apply {
            title = request.title
            description = request.description
            deadline = request.deadline
            // Статус и суммы не обновляем этим методом
        }
        // Поле updatedAt обновится автоматически через @PreUpdate в BaseEntity

        val updatedProject = projectRepository.save(existingProject)
        log.info("Project updated with id: {}", updatedProject.id)
        return updatedProject.toResponse()
    }

    @Transactional // Транзакция на запись (удаление)
    override fun delete(id: Long) {
        log.info("Deleting project with id: {}", id)
        val project = findProjectEntityById(id)

        // ВАЖНО: Проверка прав доступа (аналогично update)
        // checkAccess(project.creator.id)

        // Вместо физического удаления можно менять статус:
        // project.status = ProjectStatus.CANCELLED // или DELETED
        // projectRepository.save(project)
        // Но если используем CascadeType.ALL и orphanRemoval, то удаление сработает каскадно
        projectRepository.delete(project)
        log.info("Project deleted with id: {}", id)
    }

    // --- Вспомогательные методы ---

    /**
     * Находит сущность проекта по ID или выбрасывает ResourceNotFoundException.
     */
    private fun findProjectEntityById(id: Long): Project {
        return projectRepository.findByIdOrNull(id) // Используем findByIdOrNull из Spring Data
            ?: throw ResourceNotFoundException("Проект с ID $id не найден")
    }

    /**
     * Конвертирует сущность Project в ProjectResponse DTO.
     * (Можно вынести в отдельный класс-маппер, например, с использованием MapStruct)
     */
    private fun Project.toResponse(): ProjectResponse {
        return ProjectResponse(
            id = this.id,
            title = this.title,
            description = this.description,
            goalAmount = this.goalAmount,
            currentAmount = this.currentAmount,
            deadline = this.deadline,
            status = this.status,
            creatorUserId = this.creator.id, // Возвращаем ID создателя
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            esgRatingId = this.esgRating?.id // Возвращаем ID рейтинга, если он есть
        )
    }

    // TODO: Метод для проверки прав доступа
    // private fun checkAccess(creatorId: Long) { ... }
}