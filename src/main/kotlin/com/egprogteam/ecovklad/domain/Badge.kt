package com.egprogteam.ecovklad.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
@Table(name = "badges")
class Badge(

    @Column(nullable = false, unique = true, length = 100)
    @field:NotBlank @field:Size(max = 100)
    var name: String, // Например, "Спаситель планеты", "Социальный герой"

    @Column(nullable = false, length = 500)
    @field:NotBlank @field:Size(max = 500)
    var description: String, // Описание условия получения

    @Column(length = 255)
    var iconUrl: String? = null // URL иконки ачивки

    // Связь с пользователями через UserBadge (одна ачивка -> много записей UserBadge)
    // @OneToMany(mappedBy = "badge", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    // var userBadges: MutableSet<UserBadge> = mutableSetOf()
    // Эту сторону связи можно не указывать, если она не нужна для запросов

) : BaseEntity()