package com.example.routes

import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.keyValueRouting() {
    route("/key-values") {
        get {
            call.respond(keyValueStorage)
        }
        get("{key?}") {
            val key = call.parameters["key"] ?: return@get call.respondText(
                "Missing key",
                status = HttpStatusCode.BadRequest
            )
            val keyValue = keyValueStorage[key] ?: return@get call.respondText(
                "No keyValue with key $key",
                status = HttpStatusCode.NotFound
            )
            call.respond(keyValue)
        }
        post {
            val keyValue = call.receive<KeyValue>()
            if (keyValue.key.contains(" ")) {
                return@post call.respondText("Key cannot contain spaces", status = HttpStatusCode.BadRequest)
            }
            if (keyValue.key == "" || keyValue.value == "") {
                return@post call.respondText("Key and value are required", status = HttpStatusCode.BadRequest)
            }
            keyValueStorage[keyValue.key] = keyValue
            call.respondText("KeyValue stored correctly", status = HttpStatusCode.OK)
        }
        delete("{key?}") {
            val key = call.parameters["key"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (keyValueStorage.containsKey(key)) {
                keyValueStorage.remove(key)
                call.respondText("KeyValue removed correctly", status = HttpStatusCode.OK)
            } else {
                call.respondText("No keyValue with key $key", status = HttpStatusCode.NotFound)
            }
        }
    }
}