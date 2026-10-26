pluginManagement {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots")
            content {
                // Only consume KorGE snapshots
                includeGroupByRegex("org\\.korge\\..*")
            }
        }
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "korge-hello-world"

include("game")
