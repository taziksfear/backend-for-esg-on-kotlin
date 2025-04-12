package com.egprogteam.ecovklad.exception

// Статус может быть разным (Bad Request, Internal Server Error), лучше определить в обработчике
class PaymentException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)