package com.egprogteam.ecovklad.domain

import jakarta.persistence.*
import java.time.OffsetDateTime

@MappedSuperclass // Указывает, что это базовый класс, его поля будут включены в сущности-наследники
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Генерация ID на стороне БД (для PostgreSQL)
    @Column(updatable = false) // ID не должен обновляться
    var id: Long = 0 // Используем var для совместимости с JPA, но инициализируем значением по умолчанию
) {
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: OffsetDateTime // Используем lateinit, так как значение будет установлено перед сохранением

    @Column(nullable = false)
    lateinit var updatedAt: OffsetDateTime // Используем lateinit

    @PrePersist // Метод выполнится перед первым сохранением (INSERT)
    fun onPrePersist() {
        val now = OffsetDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate // Метод выполнится перед обновлением (UPDATE)
    fun onPreUpdate() {
        updatedAt = OffsetDateTime.now()
    }

    // Переопределение equals и hashCode для корректной работы с JPA сущностями
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntity) return false // Проверяем тип
        // Если id еще не присвоен (сущность новая), сравниваем по ссылке
        // Если id присвоен, сравниваем по id
        if (id == 0L || other.id == 0L) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        // Используем id для hashCode, если он присвоен, иначе полагаемся на стандартный hashCode
        return if (id != 0L) id.hashCode() else super.hashCode()
    }
}