package com.api

import kotlinx.serialization.Serializable
import kotlin.js.JsName

@Serializable
data class User(
    @JsName("name") val name: String,
    @JsName("email") val email: String,
    @JsName("role") val role: String,
    @JsName("password") val password: String,
    @JsName("lastLogin") val lastLogin: String,
)

@Serializable
data class Role(
    @JsName("name") val name: String,
    @JsName("permissions") val permissions: List<Permission>
)

@Serializable
data class Permission(
    @JsName("path") val path: String,
    @JsName("methods") val methods: List<String>
)