package com.example

import com.example.models.KeyValue
import com.example.models.keyValueStorage
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.serialization.kotlinx.json.*
import kotlin.test.*

class KeyValueRouteTests {
    @Test
    fun testNotFound() = testApplication {
        val response = client.get("/missing-route")
        assertEquals(
            "",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
    @Test
    fun testGetKeyValueSuccess() = testApplication {
        keyValueStorage["test-00"] = KeyValue("test-00", "test-00-value")
        val response = client.get("/key-values")
        assertEquals(
            """{"test-00":{"key":"test-00","value":"test-00-value"}}""",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.OK, response.status)
        keyValueStorage.clear()
    }
    @Test
    fun testGetKeyValueSuccessEmpty() = testApplication {
        val response = client.get("/key-values")
        assertEquals(
            "{}",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    fun testGetKeyValueNotFound() = testApplication {
        val response = client.get("/key-values/test-00")
        assertEquals(
            "No keyValue with key test-00",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
    @Test
    fun testPostKeyValueBadRequest() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/key-values") {
            contentType(ContentType.Application.Json)
            setBody("""{"bad":"json"}""")
        }
        assertEquals(
            "",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
    @Test
    fun testPostKeyValueSuccess() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/key-values") {
            contentType(ContentType.Application.Json)
            setBody(KeyValue("test-00", "test-00-value"))
        }
        assertEquals(
            "KeyValue stored correctly",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(keyValueStorage["test-00"], KeyValue("test-00", "test-00-value"))
        keyValueStorage.clear()
    }
    @Test
    fun testDeleteKeyValueSuccess() = testApplication {
        keyValueStorage["test-00"] = KeyValue("test-00", "test-00-value")
        val response = client.delete("/key-values/test-00")
        assertEquals(
            "KeyValue removed correctly",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(keyValueStorage["test-00"], null)
        keyValueStorage.clear()
    }
    @Test
    fun testDeleteKeyValueNotFound() = testApplication {
        keyValueStorage["test-00"] = KeyValue("test-00", "test-00-value")
        val response = client.delete("/key-values/test-01")
        assertEquals(
            "No keyValue with key test-01",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals(keyValueStorage["test-00"], KeyValue("test-00", "test-00-value"))
        keyValueStorage.clear()
    }
}