package com.egprogteam.ecovklad.domain

// Возможные роли пользователей в системе
enum class Role {
    USER,          // Обычный пользователь (инвестор)
    PROJECT_CREATOR, // Пользователь, который может создавать проекты (может быть совмещена с USER)
    ADMIN          // Администратор платформы
}