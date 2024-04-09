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
import kotlinx.serialization.json.Json

import com.api.*

fun main() {
    embeddedServer(Netty, 5007) {
        module()
    }.start(wait = true)

}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        intercept(ApplicationCallPipeline.Call) {
            println("Request: ${call.request.uri}")
        }

        // Serve any files in the React app's build directory
        staticFiles("/", dir=File("build/dist/js/productionExecutable"), index="index.html")
    }
}