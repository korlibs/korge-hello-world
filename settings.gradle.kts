pluginManagement {
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

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")  // Enable "projects. ..." in dependencies block

include(":shared")
include(":jvmApp")
include(":androidApp")
