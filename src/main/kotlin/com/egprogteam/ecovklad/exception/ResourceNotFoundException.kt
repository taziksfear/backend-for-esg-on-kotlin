package com.egprogteam.ecovklad.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

// Можно использовать @ResponseStatus для автоматического маппинга на HTTP статус,
// но мы будем делать это централизованно в GlobalExceptionHandler для большей гибкости.
// @ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String) : RuntimeException(message)