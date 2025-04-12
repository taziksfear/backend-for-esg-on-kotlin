package com.egprogteam.ecovklad.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Value("\${app.version:1.0.0}") // Можно вынести версию в application.properties
    private lateinit var appVersion: String

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("ЭкоВклад API")
                    .version(appVersion)
                    .description(
                        """
                        API для краудфандинговой ESG платформы "ЭкоВклад".
                        Позволяет управлять проектами, пользователями, инвестициями и отслеживать ESG-влияние.
                        """.trimIndent() // Используем trimIndent для многострочного описания
                    )
                    .license(
                        License()
                            .name("Apache 2.0") // Укажите вашу лицензию, если она есть
                            .url("http://springdoc.org") // Ссылка на детали лицензии
                    )
                // .servers(listOf(Server().url("http://localhost:8080").description("Dev Server")))
                // .components(Components().addSecuritySchemes("bearerAuth",
                //     SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                // .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
            )
    }
}