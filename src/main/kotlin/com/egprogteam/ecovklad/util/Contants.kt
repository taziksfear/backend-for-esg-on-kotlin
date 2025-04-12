package com.egprogteam.ecovklad.util

object Constants {
    // API related constants
    object API {
        const val BASE_PATH = "/api/v1"
        const val DEFAULT_PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 100
    }

    // Security related constants
    object Security {
        const val TOKEN_PREFIX = "Bearer "
        const val HEADER_STRING = "Authorization"
        const val ROLES_CLAIM = "roles"
        const val ADMIN_ROLE = "ADMIN"
        const val USER_ROLE = "USER"
        const val PROJECT_CREATOR_ROLE = "PROJECT_CREATOR"
    }

    // Project status constants
    object ProjectStatus {
        const val MIN_TITLE_LENGTH = 3
        const val MAX_TITLE_LENGTH = 255
        const val MIN_DESCRIPTION_LENGTH = 50
        const val MAX_DESCRIPTION_LENGTH = 5000
        const val MIN_GOAL_AMOUNT = 1000.0
    }

    // User related constants
    object User {
        const val MIN_USERNAME_LENGTH = 3
        const val MAX_USERNAME_LENGTH = 50
        const val MIN_PASSWORD_LENGTH = 8
        const val MAX_PASSWORD_LENGTH = 100
    }

    // ESG Rating constants
    object ESGRating {
        const val MIN_SCORE = 0
        const val MAX_SCORE = 100
    }

    // Date/time formats
    object DateTime {
        const val DATE_FORMAT = "yyyy-MM-dd"
        const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    }

    // Error messages
    object ErrorMessages {
        const val RESOURCE_NOT_FOUND = "Requested resource not found"
        const val ACCESS_DENIED = "You don't have permission to access this resource"
        const val VALIDATION_ERROR = "Validation failed for request"
    }
}