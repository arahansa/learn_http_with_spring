package com.arahansa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FirstWebApplication

fun main(args: Array<String>) {
    runApplication<FirstWebApplication>(*args)
}
