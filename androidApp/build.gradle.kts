plugins {
    // AGP 9 provides built-in Kotlin support; the kotlin-android plugin must NOT be applied.
    alias(libs.plugins.android.application)
}

description = "Korge Game Template - Android entry point"
group = rootProject.group
version = rootProject.version

android {
    namespace = rootProject.group.toString()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = rootProject.group.toString()
        // Korge is large; minSdk >= 21 enables native multidex (method count exceeds 64K).
        minSdk = maxOf(21, libs.versions.minSdk.get().toInt())
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = 1
        versionName = version.toString()
        multiDexEnabled = true
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.korge)
}
