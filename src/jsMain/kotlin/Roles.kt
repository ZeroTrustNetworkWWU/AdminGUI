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

val Roles = functionalComponent<RProps> {
    val api = API()
    val (role, setRole) = useState("")
    val (permissions, setPermissions) = useState(listOf<String>())
    val (editing, setEditing) = useState(false)
    val (currentIndex, setCurrentIndex) = useState<Int?>(null)
    val (roles, setRoles) = useState(listOf<Role>())

    useEffectWithCleanup(dependencies = listOf()) {
        val job = GlobalScope.launch {
            setRoles(api.listRoles())
        }
        return@useEffectWithCleanup { job.cancel() }
    }

    fun commitRoles() {
        GlobalScope.launch {
            api.updateRoles(roles)
        }
    }

    fun refreshRoles() {
        GlobalScope.launch {
            setRoles(api.listRoles())
        }
    }

    fun clearInput() {
        setRole("")
    }

    fun addRole(role: String, permisions: List<String>) {
        val newRole = Role(role, permisions)
        setRoles(roles + newRole)
        clearInput()
    }

    fun updateRole(role: String, permisions: List<String>) {
        val updatedRole = Role(role, permisions)    
        currentIndex?.let { index ->
            val updatedRoles = roles.toMutableList()
            updatedRoles[index] = updatedRole
            setRoles(updatedRoles)
        }
        clearInput()
        setEditing(false)
        setCurrentIndex(null)
    }

    fun deleteRole(index: Int) {
        val updatedRoles = roles.toMutableList()
        updatedRoles.removeAt(index)
        setRoles(updatedRoles)
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
            h2 { +"Roles" }
            for ((index, role) in roles.withIndex()) {
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
                        p { +"Role: ${role.name}" }
                        p { +"Permissions: ${role.permissions.joinToString(", ")}" }
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
                                    setRole(role.name)
                                    setPermissions(role.permissions)
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
                                    deleteRole(index)
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
            h2 { +if (editing) "Update Role" else "Add Role" }
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
            styledInput(InputType.text) {
                css {
                    margin(10.px)
                    padding(10.px)
                    borderRadius = 5.px
                    border = "none"
                }
                attrs {
                    placeholder = "Permissions (comma-separated)"
                    value = permissions.joinToString(", ")
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        setPermissions(target.value.split(", ").map { it.trim() })
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
                            updateRole(role, permissions)
                        } else {
                            addRole(role, permissions)
                        }
                    }
                }
                +if (editing) "Update Role" else "Add Role"
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
                        refreshRoles()
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
                        commitRoles()
                    }
                }
                +"Commit"
            }
        }
    }
}