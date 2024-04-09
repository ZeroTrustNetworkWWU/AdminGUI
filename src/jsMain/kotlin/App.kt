package com.frontend

import react.*
import react.dom.*
import kotlinx.html.*
import kotlinx.html.js.*
import kotlinx.css.*
import kotlinx.browser.document
import styled.*
import org.w3c.dom.HTMLStyleElement

fun main() {
    val styleElement = document.createElement("style") as HTMLStyleElement
    styleElement.innerHTML = """
        body {
            margin: 0;
            padding: 0;
            background-color: lightgray;
        }
    """.trimIndent()
    document.head!!.appendChild(styleElement)

    render(document.getElementById("root")) {
        child(App)
    }
}

val App = functionalComponent<RProps> {
    val (selectedComponent, setSelectedComponent) = useState<ReactElement?>(null)
    val (selectedButton, setSelectedButton) = useState<String>("Dashboard")

    useEffect(emptyList()) {
        setSelectedComponent(child(Dashboard))
    }

    console.log(document.cookie)

    val normalColor = Color("#4CAF50") // Normal green
    val selectedColor = Color("#388E3C") // Darker green when selected
    val hoverColor = Color("#45a049") // Green when hovered over

    fun RBuilder.styledButtonWithState(name: String, component: () -> ReactElement) {
        styledButton {
            css {
                display = Display.block
                width = LinearDimension("100%")
                marginBottom = LinearDimension("10px")
                backgroundColor = if (selectedButton == name) selectedColor else normalColor
                border = "none" // No border
                color = Color.white // White text
                padding(15.px, 32.px) // Padding
                textAlign = TextAlign.center // Center-aligned text
                display = Display.inlineBlock
                fontSize = LinearDimension("16px")
                margin(4.px, 2.px) // Margin
                cursor = Cursor.pointer // Cursor pointer on hover
                borderRadius = LinearDimension("12px") // Rounded corners

                hover {
                    backgroundColor = hoverColor
                }
            }
            attrs.onClickFunction = { 
                setSelectedComponent(component())
                setSelectedButton(name)
            }
            +name
        }
    }

    styledDiv {
        css {
            display = Display.flex
            backgroundColor = Color.lightGray
        }
        styledDiv {
            css {
                flexBasis = FlexBasis("15%")
                padding(16.px)
                textAlign = TextAlign.left
                backgroundColor = Color.darkGray
                height = LinearDimension("100vh")
            }
            styledButtonWithState("Dashboard", { child(Dashboard) })
            styledButtonWithState("Users", { child(Users) })
            styledButtonWithState("Roles", { child(Roles) })
            styledButtonWithState("Logs", { child(Logs) })
        }
        styledDiv {
            css {
                flexBasis = FlexBasis("85%")
                padding(16.px)
            }
            selectedComponent?.let { child(it) }
        }
    }
}