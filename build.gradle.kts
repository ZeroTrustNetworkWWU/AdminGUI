plugins {
    kotlin("multiplatform") version "1.9.22"
    id("io.ktor.plugin") version "2.3.8"
    kotlin("plugin.serialization") version "1.9.22"
    application
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }
    js {
        browser {
            binaries.executable()
        }
    }
    sourceSets {
        val ktorVersion = "2.3.8"
        val kotlinVersion = "1.9.22"
        val kotlinWrapperVersion = "5.3.0-pre.202-kotlin-1.5.0"

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
            }
        }

        val jsMain by getting {
        dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.11.0")
            implementation(npm("react", "17.0.2"))
            implementation(npm("react-dom", "17.0.2"))
            implementation(npm("react-is", "16.8.0"))
            implementation(npm("@babel/core", "7.15.5"))
            implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.202-kotlin-1.5.0")
            implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.202-kotlin-1.5.0")
            implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:$kotlinWrapperVersion")
            
        }
    }
    }
}


application {
    mainClass.set("com.server.ServerKt")
}