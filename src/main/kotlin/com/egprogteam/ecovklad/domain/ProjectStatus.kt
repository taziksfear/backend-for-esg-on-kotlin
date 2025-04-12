package com.egprogteam.ecovklad.domain

enum class ProjectStatus {
    PENDING_APPROVAL, // Ожидает проверки администратором
    ACTIVE,           // Активен, идет сбор средств
    FUNDED,           // Средства собраны
    COMPLETED,        // Проект успешно завершен
    REJECTED,         // Отклонен администратором
    CANCELLED         // Отменен создателем или по другим причинам
}