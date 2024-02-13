package com.example

import com.example.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(CORS) {
        allowHost("localhost:5173")
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Delete)
    }
    configureRouting()
    configureSerialization()
}
