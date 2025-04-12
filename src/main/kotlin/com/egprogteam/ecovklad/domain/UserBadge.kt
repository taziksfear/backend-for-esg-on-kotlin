package com.egprogteam.ecovklad.domain

import jakarta.persistence.*
import java.time.OffsetDateTime

@Entity
@Table(
    name = "user_badges",
    // Уникальный ключ, чтобы пользователь не мог получить одну и ту же ачивку дважды
    uniqueConstraints = [UniqueConstraint(name = "uk_user_badge", columnNames = ["user_id", "badge_id"])]
)
class UserBadge(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    var badge: Badge,

    @Column(nullable = false, updatable = false)
    var awardedAt: OffsetDateTime = OffsetDateTime.now() // Дата получения ачивки

    // Используем собственный ID для этой таблицы связи, а не композитный ключ
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    // Переопределение equals и hashCode важно для сущностей со связями
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserBadge

        // Сравниваем по ID, если они не 0, иначе по ссылкам на связанные сущности
        if (id != 0L && other.id != 0L) return id == other.id
        if (user.id == 0L || other.user.id == 0L || badge.id == 0L || other.badge.id == 0L) return this === other
        return user.id == other.user.id && badge.id == other.badge.id
    }

    override fun hashCode(): Int {
        // Используем ID, если он есть, иначе комбинацию hashCode связанных сущностей
        return if (id != 0L) id.hashCode() else (31 * user.hashCode() + badge.hashCode())
    }
)