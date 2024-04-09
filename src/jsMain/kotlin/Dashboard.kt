package com.frontend

import react.*
import react.dom.*
import kotlinx.html.*
import kotlinx.html.js.*

val Dashboard = functionalComponent<RProps> {
    div {
        h2 { +"Dashboard" }
        p { +"Welcome to the admin dashboard." }
    }
}