package com.api

import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.js.Promise
import kotlin.js.json
import org.w3c.fetch.RequestMode
import kotlinx.browser.document

import com.api.User
import com.api.Role

// Used throughout to wrap api calls and effects.
val mainScope = MainScope()

private suspend fun Promise<Response>.assertStatus() = await().apply {
    status.toInt().also {
        check(200 == it || 0 == it) {
            "Operation failed: $status  $url".also { msg ->
                console.log(msg)
                window.alert(msg)
            }
        }
    }
}

private suspend fun fetch(method: String, url: String, body: dynamic = null): Response =
    window.fetch(
        url, RequestInit(
            method = method,
            body = body,
            headers = json(
                "Content-Type" to "application/json",
                "Accept" to "application/json",
                "pragma" to "no-cache",
            )
        )
    ).assertStatus()

private suspend fun get(url: String, body: dynamic): Response =
    fetch("GET", url, JSON.stringify(body))

private suspend fun put(url: String, body: dynamic): Response  {
    console.log(JSON.stringify(body))
    return fetch("PUT", url, JSON.stringify(body))
}

private suspend fun post(url: String, body: dynamic): Response =
    fetch("POST", url, JSON.stringify(body))

private suspend fun delete(url: String): Response =
    fetch("DELETE", url)

/**
 * Serialize object from json in response.
 */
private suspend inline fun <reified T> json(response: Response): T =
    Json.decodeFromString(response.text().await())

/**
 * The API methods
 */
public class ZTNAPI {
    private val apiRoot = "http://localhost:5005/api" // Edge Node URL

    private fun addTrustData(data: dynamic): dynamic {
        // Create the _trustData object with session ID
        val trustData = js("{}")
        trustData._trustData = js("{}")
        val session = document.cookie.split(";").find { it.startsWith("sessionKey=") }?.split("=")?.get(1)
        if (session == null) {
            console.log("No session found")
            return data
        }
        trustData._trustData.session = session

        // Merge data with _trustData at the root
        val newData = js("{}")
        for (key in js("Object.keys(data)")) {
            newData[key] = data[key]
        }
        for (key in js("Object.keys(trustData)")) {
            newData[key] = trustData[key]
        }

        return newData
    }

    private suspend fun getWithTrustData(url: String): Response {
        return post(url, addTrustData(mapOf("" to "")))
    }

    private suspend fun putWithTrustData(url: String, data: dynamic): Response {
        return put(url, addTrustData(data))
    }

    private suspend fun postWithTrustData(url: String, data: dynamic): Response {
        return post(url, addTrustData(data))
    }


    // API methods 
    suspend fun listUsers(): List<User> =
        json(getWithTrustData("$apiRoot/users"))

    suspend fun updateUsers(users: List<User>) {
        putWithTrustData("$apiRoot/updateUsers", users)
    }

    suspend fun listRoles(): List<Role> =
        json(getWithTrustData("$apiRoot/roles"))

    suspend fun updateRoles(roles: List<Role>) {
        putWithTrustData("$apiRoot/updateRoles", roles)
    }
}