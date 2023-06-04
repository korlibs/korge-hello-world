pluginManagement {
    repositories {
        mavenLocal(); mavenCentral(); google(); gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") }
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental") }
    }
}

plugins {
    //id("com.soywiz.kproject.settings") version "0.0.1-SNAPSHOT"
    id("com.soywiz.kproject.settings") version "0.3.2-dev-1.9.20-2914"
    //id("com.soywiz.kproject.settings") version "0.3.1"
}

kproject("./deps")
