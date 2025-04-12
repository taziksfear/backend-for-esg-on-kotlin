package com.egprogteam.ecovklad.domain

enum class InvestmentStatus {
    PENDING,    // Ожидает подтверждения оплаты
    SUCCESSFUL, // Оплата прошла успешно
    FAILED,     // Ошибка оплаты
    REFUNDED    // Возвращена (если применимо)
}