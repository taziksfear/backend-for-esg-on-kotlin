package com.egprogteam.ecovklad.domain

import jakarta.persistence.*
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "projects")
class Project(

    @Column(nullable = false, length = 255)
    @field:NotBlank @field:Size(max = 255)
    var title: String,

    @Column(nullable = false, length = 5000)
    @field:NotBlank @field:Size(max = 5000)
    var description: String,

    @Column(nullable = false, precision = 19, scale = 2) // precision - общее кол-во цифр, scale - кол-во после запятой
    @field:NotNull @field:DecimalMin(value = "0.0", inclusive = false) // Сумма должна быть > 0
    var goalAmount: BigDecimal,

    @Column(nullable = false, precision = 19, scale = 2)
    @field:NotNull @field:DecimalMin(value = "0.0")
    var currentAmount: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    @field:NotNull @field:FutureOrPresent // Дедлайн не в прошлом
    var deadline: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var status: ProjectStatus = ProjectStatus.PENDING_APPROVAL,

    @Column(length = 255)
    var imageUrl: String, // URL основного изображения
    
    @Column(length = 500)
    var shortDescription: String, // Краткое описание для карточки
    
    @OneToMany(mappedBy = "project", cascade = [CascadeType.ALL], orphanRemoval = true)
    var articles: MutableList<Article> = mutableListOf(),
    
    @ManyToMany
    @JoinTable(
        name = "project_admins",
        joinColumns = [JoinColumn(name = "project_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var admins: MutableSet<User> = mutableSetOf()

    // --- Связи ---

    // Создатель проекта (много проектов -> один пользователь)
    @ManyToOne(fetch = FetchType.LAZY) // Проекты знают своего создателя
    @JoinColumn(name = "creator_user_id", nullable = false) // Внешний ключ в таблице projects
    var creator: User,

    // Рейтинг ESG (один проект -> один рейтинг) - может быть null, если еще не оценен
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "esg_rating_id", referencedColumnName = "id") // Связь по внешнему ключу esg_rating_id
    var esgRating: EsgRating? = null,

    // Инвестиции в этот проект (один проект -> много инвестиций)
    @OneToMany(mappedBy = "project", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var investments: MutableList<Investment> = mutableListOf()

    

) : BaseEntity()