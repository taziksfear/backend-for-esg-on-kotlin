plugins {
    id("org.springframework.boot") version "3.2.0" // Должен быть первым!
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    // Отключаем обычный jar, оставляем только bootJar
    jar {
        enabled = false
    }
    
    // Указываем главный класс для bootJar
    bootJar {
        manifest {
            attributes["Start-Class"] = "org.example.app.App"
        }
    }
}