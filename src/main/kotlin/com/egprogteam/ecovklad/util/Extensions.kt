package com.egprogteam.ecovklad.util

import com.egprogteam.ecovklad.controller.dto.ProjectResponse
import com.egprogteam.ecovklad.controller.dto.UserSummaryResponse
import com.egprogteam.ecovklad.domain.Project
import com.egprogteam.ecovklad.domain.User
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

// Domain to DTO extensions
fun Project.toResponse(): ProjectResponse = ProjectResponse(
    id = this.id,
    title = this.title,
    description = this.description,
    goalAmount = this.goalAmount,
    currentAmount = this.currentAmount,
    deadline = this.deadline,
    status = this.status,
    creatorUserId = this.creator.id,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    esgRatingId = this.esgRating?.id
)

fun User.toSummaryResponse(): UserSummaryResponse = UserSummaryResponse(
    id = this.id,
    username = this.username,
    email = this.email,
    firstName = this.firstName,
    lastName = this.lastName
)

// DateTime extensions
fun OffsetDateTime.toFormattedString(): String {
    return this.format(DateTimeFormatter.ofPattern(Constants.DateTime.DATE_TIME_FORMAT))
}

fun OffsetDateTime.toDateString(): String {
    return this.format(DateTimeFormatter.ofPattern(Constants.DateTime.DATE_FORMAT))
}

// Collection extensions
fun <T> Collection<T>?.emptyIfNull(): Collection<T> = this ?: emptyList()

// String extensions
fun String?.toBigDecimalOrZero(): BigDecimal = this?.toBigDecimalOrNull() ?: BigDecimal.ZERO

fun String?.nullIfBlank(): String? = if (this.isNullOrBlank()) null else this

// Security extensions
fun Collection<String>.containsAny(roles: Set<String>): Boolean {
    return this.any { roles.contains(it) }
}