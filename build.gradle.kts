plugins {
    // We need to define here all koltin multiplatform target related plugins
    // But we must not applya them on root level
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.lint) apply false
//    alias(libs.plugins.korge) apply false
}

allprojects {
    repositories {
        mavenLocal()
        maven {
            name = "Central Portal Snapshots"
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            content {
                // Only consume org.korge.korlibs snapshots
                includeGroup("org.korge.korlibs")
            }
        }
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

group = "org.korge.application"
version = 1.0
