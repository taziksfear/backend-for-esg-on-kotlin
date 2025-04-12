package com.egprogteam.ecovklad.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordUtils(private val passwordEncoder: PasswordEncoder) {

    fun encodePassword(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword)
    }

    fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }

    companion object {
        fun createDelegatingPasswordEncoder(): PasswordEncoder {
            return BCryptPasswordEncoder()
        }
    }
}