package com.server

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.http.*
import java.io.File
import kotlinx.html.*
import io.ktor.server.html.*
import io.ktor.http.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

import com.api.*

fun main() {
    println("Starting server...")
    val port = 5007
    embeddedServer(Netty, port) {
        module()
        println("Server started. Running on http://localhost:${port}")
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    var users = listOf( 
        User("Alice", "Alice54@gmail.com", "admin", "password1", "2022-01-01"), 
        User("Bobby", "BobbyBert@gmail.com", "user", "password2", "2022-01-01") 
    )
    var roles = listOf(
        Role("admin", listOf(Permission("/*", listOf("*")))),
        Role("user", listOf(Permission("api/users", listOf("POST", "GET")), Permission("api/roles", listOf("POST", "GET"))))
    )
    
    routing {
        intercept(ApplicationCallPipeline.Call) {
            println("Request: ${call.request.uri}")

        }

        // Serve any files in the React app's build directory
        staticFiles("/", dir=File("build/dist/js/productionExecutable"), index="index.html")

        // API routes
        route("/api") {
            post("/users") {
                call.respond(users)
            }

            put("/updateUsers") {
                println("Updating users")
                try {
                    val requestBody = Json.decodeFromString<Map<String, JsonElement>>(call.receiveText())
                    val usersJsonArray = requestBody["d2_1"]
                    val usersList = Json.decodeFromString<List<User>>(usersJsonArray.toString())
                    users = usersList
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    println("Error updating users: $e")
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post("/roles") {
                call.respond(roles)
            }

            put("/updateRoles") {
                println("Updating roles")
                try {
                    val requestBody = Json.decodeFromString<Map<String, JsonElement>>(call.receiveText())
                    val rolesJsonArray = requestBody["d2_1"]
                    val rolesList = Json.decodeFromString<List<Role>>(rolesJsonArray.toString())
                    roles = rolesList
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    println("Error updating roles: $e")
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}