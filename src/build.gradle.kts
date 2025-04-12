import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.flywaydb.flyway")
    id("org.openapi.generator")
}

group = "com.egprogteam"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

// Dependency versions managed by Spring Dependency Management plugin
val kotlinLoggingVersion = "3.0.5"
val modelMapperVersion = "3.1.1"
val jwtVersion = "0.11.5"
val postgresqlVersion = "42.5.4"
val swaggerAnnotationsVersion = "2.2.8"
val mockkVersion = "1.13.4"

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    
    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")

    // Database
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.flywaydb:flyway-core")
    
    // Utilities
    implementation("org.modelmapper:modelmapper:$modelMapperVersion")
    implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jwtVersion")
    
    // Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation("io.swagger.core.v3:swagger-annotations:$swaggerAnnotationsVersion")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.h2database:h2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// OpenAPI Generator configuration (if you want to generate client SDKs)
openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/src/main/resources/api-spec.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("com.egprogteam.ecovklad.generated.api")
    modelPackage.set("com.egprogteam.ecovklad.generated.model")
    configOptions.set(mapOf(
        "interfaceOnly" to "true",
        "useTags" to "true",
        "useSpringBoot3" to "true"
    ))
}

// Flyway configuration
flyway {
    url = "jdbc:postgresql://localhost:5432/ecovklad"
    user = "postgres"
    password = "postgres"
    schemas = arrayOf("public")
    locations = arrayOf("classpath:db/migration")
    cleanDisabled = false
}

// Kotlin compiler options
tasks.compileKotlin {
    kotlinOptions {
        languageVersion = "1.8"
        apiVersion = "1.8"
    }
}

// For integration with JPA and Kotlin
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

// Build info configuration
springBoot {
    buildInfo {
        properties {
            // Additional build info properties can be added here
            additional = mapOf(
                "description" to "ESG Crowdfunding Platform - ЭкоВклад",
                "version" to version
            )
        }
    }
}