package com.example

import com.example.plugins.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import kotlinx.serialization.Serializable


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

@Serializable
data class User(var user_id: Int, var user_name: String, var email: String)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
}

object UserRepository {
    val users = mutableListOf<User>()
}
