package com.egprogteam.ecovklad

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableAsync // Enable asynchronous method execution
@EnableScheduling // Enable scheduled tasks
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args).apply {
        // You can add initialization logic here if needed
        println("\n=========================================")
        println("ESG Crowdfunding Platform Started Successfully!")
        println("=========================================\n")
    }
}