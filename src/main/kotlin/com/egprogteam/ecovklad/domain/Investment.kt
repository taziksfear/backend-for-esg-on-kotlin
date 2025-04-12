package com.egprogteam.ecovklad.domain

import jakarta.persistence.*
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

@Entity
@Table(name = "investments")
class Investment(

    // Пользователь, сделавший инвестицию (много инвестиций -> один пользователь)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_user_id", nullable = false)
    var investor: User,

    // Проект, в который сделана инвестиция (много инвестиций -> один проект)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    var project: Project,

    @Column(nullable = false, precision = 19, scale = 2)
    @field:NotNull @field:DecimalMin(value = "1.00") // Минимальная сумма инвестиции (например, 1 рубль)
    var amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var status: InvestmentStatus = InvestmentStatus.PENDING,

    // ID транзакции из платежной системы (для сверки)
    @Column(length = 255)
    var transactionId: String? = null

) : BaseEntity()