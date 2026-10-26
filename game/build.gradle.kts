plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.korge)
}

korge {
    id = "com.sample.demo"

    targetIos()
}

kotlin {
    applyDefaultHierarchyTemplate()

    jvm {
    }

//    android {
//        compileSdk = libs.versions.compileSdk.get().toInt()
//        minSdk = libs.versions.minSdk.get().toInt()
//        androidResources.enable = true
//    }

//    js {
//        browser {
//            compilerOptions {
//                target.set("es2015")
//            }
//        }
//    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.korge)
//            implementation(libs.fleks)
//            implementation(libs.kaml)
//            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
//        jvmMain
    }
}

// The korge plugin registers `runJvm` (with the required --add-opens) itself.
// The block below only adds extras: silence JDK 25+ restricted native access
// warnings (JNA, etc.) for every JavaExec task.
tasks.withType<JavaExec> {
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

tasks.register<Jar>("jvmFatJar") {
    group = "build"
    description = "Packages a standalone fat JAR for the JVM application"
    archiveClassifier.set("all")

    val jvmTarget = kotlin.targets.getByName("jvm") as org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
    val mainCompilation = jvmTarget.compilations.getByName("main")

    // Set the Main-Class attribute in the MANIFEST.MF
    manifest {
        attributes["Main-Class"] = "MainKt"
    }

    // Include the project outputs and all runtime dependencies
    from(mainCompilation.output.allOutputs)
    val runtimeFiles = mainCompilation.runtimeDependencyFiles
    from(runtimeFiles.map { if (it.isDirectory) it else zipTree(it) })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
