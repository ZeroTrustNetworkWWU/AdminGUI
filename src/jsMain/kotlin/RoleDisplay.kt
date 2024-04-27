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
import kotlinx.serialization.Serializable


@JsExport
@Serializable
data class RoleDisplayProps(
    val role: Role,
    val index: Int
) : RProps


val RoleDisplay = functionalComponent<RoleDisplayProps> { props ->
    styledDiv {
        css {
            height = 100.pct
            display = Display.flex
            flexDirection = FlexDirection.row
            justifyContent = JustifyContent.left
            alignItems = Align.stretch
            width = 80.pct
        }
        styledDiv {
            css {
                backgroundColor = Color.darkGray
                padding(vertical = 10.px, horizontal = 20.px)
                borderRadius = 10.px
                margin(10.px)
                flex(0.0)
            }
            styledH3 {
                +"${props.role.name}"
            }
        }
        styledDiv {
            css {
                backgroundColor = Color.darkGray
                padding(10.px)
                borderRadius = 10.px
                margin(10.px)
                flex(1.0)
            }
            styledTable {
                css {
                    width = 100.pct
                }
                styledTr {
                    styledTh {
                        css {
                            width = 50.pct
                            textAlign = TextAlign.center
                        }
                        +"Permission"
                    }
                    styledTh {
                        css {
                            width = 50.pct
                            textAlign = TextAlign.center
                        }
                        +"Paths"
                    }
                }
                props.role.permissions.forEach { permission ->
                    styledTr {
                        styledTd {
                            css {
                                textAlign = TextAlign.center
                            }
                            +permission.path
                        }
                        styledTd {
                            css {
                                textAlign = TextAlign.center
                            }
                            +permission.methods.joinToString(", ")
                        }
                    }
                }
            }
        }
    }
}