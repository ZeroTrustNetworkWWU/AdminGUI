package com.api

import kotlinx.serialization.Serializable
import kotlin.js.JsName

@Serializable
data class User(
    @JsName("email") val email: String,
    @JsName("name") val name: String,
    @JsName("password") val password: String,
    @JsName("role") val role: String,
    @JsName("lastActive") val lastLogin: String,
)

@Serializable
data class Role(
    @JsName("name") val name: String,
    @JsName("permissions") val permissions: List<String>
)