package org.example.app

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    @GetMapping("/")
    fun home(): Map<String, String> {
        return mapOf(
            "message" to "АССАЛАМ АЛЕЙКУИ БРАТ!",
            "status" to "работает",
            "docs" to "http://localhost:8080/swagger-ui.html"
        )
    }
}