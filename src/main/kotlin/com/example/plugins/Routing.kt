package com.example.plugins

import com.example.User
import com.example.UserRepository.users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        route("/users") {
            get("/") {
                call.respondText("Hello, world!")
            }
            get("/all") {
                call.respond(users)
            }
            get("/{id}") {
                val userId = call.parameters["id"]?.toIntOrNull()
                if (userId != null) {
                    val user = users.find { it.user_id == userId }
                    if (user != null) {
                        call.respond(user)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User with ID $userId not found")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                }
            }
            post("/") {
                val user = call.receive<User>()
                user.user_id = users.size + 1
                users.add(user)
                call.respond(HttpStatusCode.Created, user)
            }
            put("/{id}") {
                val userId = call.parameters["id"]?.toIntOrNull()
                if (userId != null) {
                    val user = call.receive<User>()
                    val existingUser = users.find { it.user_id == userId }
                    if (existingUser != null) {
                        existingUser.user_name = user.user_name
                        existingUser.email = user.email
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User with ID $userId not found")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                }
            }

            delete("/{id}") {
                val userId = call.parameters["id"]?.toIntOrNull()
                if (userId != null) {
                    val user = users.find { it.user_id == userId }
                    if (user != null) {
                        users.remove(user)
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User with ID $userId not found")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                }
            }
        }
    }
}