package com.frontend

import react.*
import react.dom.*
import kotlinx.html.*
import kotlinx.html.js.*
import kotlinx.css.*
import styled.*
import org.w3c.dom.HTMLInputElement

import com.api.*

val Users = functionalComponent<RProps> {
    val (name, setName) = useState("")
    val (role, setRole) = useState("")
    val (email, setEmail) = useState("")
    val (password, setPassword) = useState("")
    val (users, setUsers) = useState(listOf( User("email1@example.com", "Alice", "password1", "admin", "2022-01-01"), User("email2@example.com", "Bob", "password2", "user", "2022-01-01") ))
    val (editing, setEditing) = useState(false)
    val (currentIndex, setCurrentIndex) = useState<Int?>(null)

    fun clearInput() {
        setName("")
        setRole("")
        setEmail("")
        setPassword("")
    }

    fun addUser(name: String, role: String, email: String, password: String) {
        val newUser = User(email, name, password, role, "Never")
        setUsers(users + newUser)
        clearInput()
    }

    fun updateUser(name: String, role: String, email: String, password: String) {
        val updatedUser = User(email, name, password, role, "Never")
        currentIndex?.let { index ->
            val updatedUsers = users.toMutableList()
            updatedUsers[index] = updatedUser
            setUsers(updatedUsers)
        }
        clearInput()
        setEditing(false)
        setCurrentIndex(null)
    }

    fun deleteUser(index: Int) {
        val updatedUsers = users.toMutableList()
        updatedUsers.removeAt(index)
        setUsers(updatedUsers)
    }


    styledDiv {
        css {
            backgroundColor = Color.lightGray
            padding(10.px)
            margin(10.px)
        }
        styledDiv {
            css {
                backgroundColor = Color.darkGray
                padding(10.px)
                margin(10.px)
                borderRadius = 20.px
            }
            h2 { +"Users" }
            for ((index, user) in users.withIndex()) {
                styledDiv {
                    css {
                        backgroundColor = Color.white
                        padding(10.px)
                        margin(10.px)
                        borderRadius = 10.px
                        display = Display.flex
                        justifyContent = JustifyContent.spaceBetween
                    }
                    styledDiv {
                        css {
                            display = Display.flex
                            justifyContent = JustifyContent.spaceAround
                            flexGrow = 1.0
                        }
                        p { +"Name: ${user.name}" }
                        p { +"Role: ${user.role}" }
                        p { +"Email: ${user.email}" }
                    }
                    styledDiv {
                        css {
                            display = Display.flex
                            justifyContent = JustifyContent.spaceAround
                        }
                        styledButton {
                            css {
                                margin(10.px)
                                padding(10.px)
                                backgroundColor = Color.blue
                                color = Color.white
                                borderRadius = 5.px
                                border = "none"
                                hover {
                                    backgroundColor = Color.darkBlue
                                }
                            }
                            attrs {
                                onClickFunction = {
                                    setName(user.name)
                                    setRole(user.role)
                                    setEmail(user.email)
                                    setPassword(user.password)
                                    setEditing(true)
                                    setCurrentIndex(index)
                                }
                            }
                            +"Edit"
                        }
                        styledButton {
                            css {
                                margin(10.px)
                                padding(10.px)
                                backgroundColor = Color.red
                                color = Color.white
                                borderRadius = 5.px
                                border = "none"
                                hover {
                                    backgroundColor = Color.darkRed
                                }
                            }
                            attrs {
                                onClickFunction = {
                                    deleteUser(index)
                                }
                            }
                            +"Delete"
                        }
                    }
                }
            }
        }
        styledDiv {
            css {
                backgroundColor = Color.darkGray
                padding(10.px)
                margin(10.px)
                borderRadius = 20.px
            }
            h2 { +if (editing) "Update User" else "Add User" }
            styledInput(InputType.text) {
                css {
                    margin(10.px)
                    padding(10.px)
                    borderRadius = 5.px
                    border = "none"
                }
                attrs {
                    placeholder = "Name"
                    value = name
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        setName(target.value)
                    }
                }
            }
            styledInput(InputType.text) {
                css {
                    margin(10.px)
                    padding(10.px)
                    borderRadius = 5.px
                    border = "none"
                }
                attrs {
                    placeholder = "Role"
                    value = role
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        setRole(target.value)
                    }
                }
            }
            styledInput(InputType.email) {
                css {
                    margin(10.px)
                    padding(10.px)
                    borderRadius = 5.px
                    border = "none"
                }
                attrs {
                    placeholder = "Email"
                    value = email
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        setEmail(target.value)
                    }
                }
            }
            styledInput(InputType.password) {
                css {
                    margin(10.px)
                    padding(10.px)
                    borderRadius = 5.px
                    border = "none"
                }
                attrs {
                    placeholder = "Password"
                    value = password
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        setPassword(target.value)
                    }
                }
            }
            styledButton {
                css {
                    margin(10.px)
                    padding(10.px)
                    backgroundColor = Color.blue
                    color = Color.white
                    borderRadius = 5.px
                    border = "none"
                    hover {
                        backgroundColor = Color.darkBlue
                    }
                }
                attrs {
                    onClickFunction = {
                        if (editing) {
                            updateUser(name, role, email, password)
                        } else {
                            addUser(name, role, email, password)
                        }
                    }
                }
                +if (editing) "Update User" else "Add User"
            }
            styledButton {
                css {
                    margin(10.px)
                    padding(10.px)
                    backgroundColor = Color.blue
                    color = Color.white
                    borderRadius = 5.px
                    border = "none"
                    hover {
                        backgroundColor = Color.darkBlue
                    }
                }
                attrs {
                    onClickFunction = {
                        clearInput()
                        setEditing(false)
                        setCurrentIndex(null)
                    }
                }
                +"Cancel"
            }
        }
        styledDiv {
            css {
                backgroundColor = Color.darkGray
                padding(10.px)
                margin(10.px)
                borderRadius = 20.px
            }
            h2 { +"Actions" }
            styledButton {
                css {
                    margin(10.px)
                    padding(10.px)
                    backgroundColor = Color.blue
                    color = Color.white
                    borderRadius = 5.px
                    border = "none"
                    hover {
                        backgroundColor = Color.darkBlue
                    }
                }
                attrs {
                    onClickFunction = {
                        // Add Refresh logic here
                    }
                }
                +"Refresh"
            }
            styledButton {
                css {
                    margin(10.px)
                    padding(10.px)
                    backgroundColor = Color.green
                    color = Color.white
                    borderRadius = 5.px
                    border = "none"
                    hover {
                        backgroundColor = Color.darkGreen
                    }
                }
                attrs {
                    onClickFunction = {
                        // Add commit logic here
                    }
                }
                +"Commit"
            }
        }
    }
}