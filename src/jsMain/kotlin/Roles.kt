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
    val (permissions, setPermissions) = useState(listOf<Permission>())
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
        setPermissions(listOf())
    }

    fun addRole(role: String, permissions: List<Permission>) {
        val newRole = Role(role, permissions)
        val updatedRoles = roles.toMutableList()
        updatedRoles.add(newRole)
        setRoles(updatedRoles)
    }

    fun updateRole(role: String, permissions: List<Permission>) {
        val updatedRoles = roles.toMutableList()
        updatedRoles[currentIndex!!] = Role(role, permissions)
        setRoles(updatedRoles)

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
                        backgroundColor = Color.lightGray
                        padding(10.px)
                        margin(10.px)
                        borderRadius = 10.px
                        display = Display.flex
                        flexDirection = FlexDirection.row
                        alignItems = Align.flexStart
                    }
                    child(RoleDisplay, RoleDisplayProps(role, index))

                    styledTd {
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
                                title = "Edit Role"
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
                                title = "Delete Role"
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
                    backgroundColor = Color.lightGray
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
            val emptyCount = permissions.count { it.path.isBlank() }
            if (emptyCount >= 2) {
                setPermissions(permissions.filter { it.path.isNotBlank() } + Permission("", listOf()))
            }
            
            (permissions + Permission("", listOf())).forEachIndexed { index, permission ->
                styledDiv {
                    css {
                        display = Display.flex
                    }
                    styledInput(InputType.text) {
                        css {
                            margin(10.px)
                            padding(10.px)
                            borderRadius = 5.px
                            border = "none"
                            backgroundColor = Color.lightGray.withAlpha(0.5)
                        }
                        attrs {
                            placeholder = "Permission"
                            value = permission.path
                            onChangeFunction = {
                                val target = it.target as HTMLInputElement
                                val updatedPermissions = permissions.toMutableList().apply { 
                                    if (index < size) this[index] = Permission(target.value, this[index].methods) 
                                    else add(Permission(target.value, listOf())) 
                                }
                                setPermissions(updatedPermissions)
                            }
                        }
                    }
                    styledInput(InputType.text) {
                        css {
                            margin(10.px)
                            padding(10.px)
                            borderRadius = 5.px
                            border = "none"
                            backgroundColor = Color.lightGray.withAlpha(0.25)
                            width = 50.pct
                        }
                        attrs {
                            placeholder = "Methods (comma-separated)"
                            value = permission.methods.joinToString(",")
                            onChangeFunction = {
                                val target = it.target as HTMLInputElement
                                // clean any spaces and update the input
                                val methods = target.value.split(",").map { it.trim() }
                                it.target.asDynamic().value = methods.joinToString(", ")
                                val updatedPermissions = permissions.toMutableList().apply { 
                                    if (index < size) this[index] = Permission(this[index].path, methods) 
                                    else add(Permission("", methods)) 
                                }
                                setPermissions(updatedPermissions)
                            }
                        }
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
                        clearInput()
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