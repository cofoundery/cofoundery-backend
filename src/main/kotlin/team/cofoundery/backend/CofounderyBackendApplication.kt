package team.cofoundery.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CofounderyBackendApplication

fun main(args: Array<String>) {
    runApplication<CofounderyBackendApplication>(*args)
}

const val BASE_PACKAGE = "team.cofoundery.backend"
