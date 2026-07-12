plugins {
    kotlin("jvm")
    application
}

description = "Korge Game Template - JVM desktop entry point"
group = rootProject.group
version = rootProject.version

// Suppress warning on JVM application start
tasks.withType<JavaExec> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.korge)
}

application {
    mainClass.set("${rootProject.group}.JvmMain")
}
