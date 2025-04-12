package com.egprogteam.ecovklad.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Email // Валидация здесь не обязательна, но может быть полезна
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(
    name = "users",
    // Уникальные ограничения на уровне таблицы
    uniqueConstraints = [
        UniqueConstraint(name = "uk_user_username", columnNames = ["username"]),
        UniqueConstraint(name = "uk_user_email", columnNames = ["email"])
    ]
)
class User(

    @Column(nullable = false, unique = true, length = 50)
    @field:NotBlank @field:Size(min = 3, max = 50)
    var username: String,

    @Column(nullable = false, unique = true, length = 100)
    @field:NotBlank @field:Email @field:Size(max = 100)
    var email: String,

    @Column(nullable = false)
    @field:NotBlank // Пароль не должен быть пустым, но размер проверять здесь необязательно
    var passwordHash: String, // Храним хеш пароля

    @Column(length = 100)
    @field:Size(max = 100)
    var firstName: String? = null,

    @Column(length = 100)
    @field:Size(max = 100)
    var lastName: String? = null,

    @Column(nullable = false)
    var emailVerified: Boolean = false,
    
    @Column(length = 6)
    var emailVerificationCode: String? = null,
    
    @Column
    var verificationCodeExpiry: OffsetDateTime? = null

    @Enumerated(EnumType.STRING) // Храним роли как строки
    @ElementCollection(fetch = FetchType.EAGER) // Роли загружаются вместе с пользователем
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "role", nullable = false)
    var roles: MutableSet<Role> = mutableSetOf(Role.USER), // По умолчанию - USER

    @Column(nullable = false)
    var enabled: Boolean = true, // Флаг активности пользователя (для банов и т.д.)

    // --- Связи ---

    // Проекты, созданные этим пользователем (один пользователь -> много проектов)
    @OneToMany(mappedBy = "creator", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    // mappedBy = "creator" -> поле 'creator' в сущности Project отвечает за эту связь
    // cascade = CascadeType.ALL -> операции (persist, merge, remove) применяются и к связанным проектам
    // orphanRemoval = true -> если проект удаляется из этой коллекции, он удаляется из БД
    // fetch = FetchType.LAZY -> проекты не загружаются автоматически при загрузке пользователя
    var createdProjects: MutableList<Project> = mutableListOf(),

    // Инвестиции, сделанные этим пользователем (один пользователь -> много инвестиций)
    @OneToMany(mappedBy = "investor", cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    var investments: MutableList<Investment> = mutableListOf(),

    // Ачивки пользователя (много пользователей -> много ачивок)
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var userBadges: MutableSet<UserBadge> = mutableSetOf()

) : BaseEntity() // Наследуемся от BaseEntity для получения id, createdAt, updatedAt