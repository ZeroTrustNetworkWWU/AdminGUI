package com.frontend

import react.*
import react.dom.*
import kotlinx.html.*
import kotlinx.html.js.*
import kotlinx.css.*
import styled.*
import org.w3c.dom.HTMLInputElement
import com.api.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.browser.document

import com.api.ZTNAPI as API

val Users = functionalComponent<RProps> {
    val api = API()
    val (name, setName) = useState("")
    val (role, setRole) = useState("")
    val (email, setEmail) = useState("")
    val (password, setPassword) = useState("")
    val (editing, setEditing) = useState(false)
    val (currentIndex, setCurrentIndex) = useState<Int?>(null)
    val (users, setUsers) = useState(listOf<User>())

    useEffectWithCleanup(dependencies = listOf()) {
        val job = GlobalScope.launch {
            setUsers(api.listUsers())
        }
        return@useEffectWithCleanup { job.cancel() }
    }

    fun commitUsers() {
        GlobalScope.launch {
            api.updateUsers(users)
        }
    }

    fun refreshUsers() {
        GlobalScope.launch {
            setUsers(api.listUsers())
        }
    }

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
                        backgroundColor = Color.lightGrey
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
            fun CSSBuilder.commonInputStyle() {
                margin(10.px)
                padding(10.px)
                borderRadius = 5.px
                border = "none"
                backgroundColor = Color.lightGray.withAlpha(0.5)
            }

            styledInput(InputType.text) {
                css {
                    commonInputStyle()
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
                    commonInputStyle()
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
                    commonInputStyle()
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
                    commonInputStyle()
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
                        refreshUsers()
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
                        commitUsers()
                    }
                }
                +"Commit"
            }
        }
    }
}