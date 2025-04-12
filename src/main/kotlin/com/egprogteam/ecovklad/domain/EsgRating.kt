package com.egprogteam.ecovklad.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.time.OffsetDateTime

@Entity
@Table(name = "esg_ratings")
class EsgRating(

    // Связь с проектом (рейтинг принадлежит одному проекту)
    // mappedBy указывает, что управление связью (внешний ключ) находится в Project
    @OneToOne(mappedBy = "esgRating", fetch = FetchType.LAZY)
    var project: Project?, // Может быть null временно при создании рейтинга

    @field:Min(0) @field:Max(100) // Пример шкалы оценки 0-100
    @Column(nullable = false)
    var environmentScore: Int = 0, // Экология

    @field:Min(0) @field:Max(100)
    @Column(nullable = false)
    var socialScore: Int = 0,      // Социальная ответственность

    @field:Min(0) @field:Max(100)
    @Column(nullable = false)
    var governanceScore: Int = 0,  // Управление

    @Column(length = 5000)
    var assessmentNotes: String? = null, // Комментарии оценщика

    @Column(nullable = false)
    var assessedAt: OffsetDateTime = OffsetDateTime.now() // Дата оценки

    // Можно добавить поле для итоговой оценки (например, буква A, B, C или число)
    // var overallGrade: String? = null

) : BaseEntity()