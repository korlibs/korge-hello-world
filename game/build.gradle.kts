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

// Workaround for a gap in the korge plugin's iOS project generation: the
// generated app targets do not link UIKit, and current Xcode linkers fail with
// "Undefined symbols: _OBJC_CLASS_$_UIApplication, _UIApplicationMain, ...".
// Patch the generated project.yml (add `- sdk: UIKit.framework` next to each
// framework dependency) and re-run xcodegen, between the plugin's
// prepareKotlinNativeIosProject and the iosBuild* tasks.
val patchGeneratedIosProject = tasks.register("patchGeneratedIosProject") {
    group = "korge"
    description = "Adds the missing UIKit linkage to the generated iOS xcodegen project"
    val platformsDir = layout.buildDirectory.dir("platforms/ios")
    inputs.files(fileTree(platformsDir) { include("project.yml") }).withPropertyName("projectYml")
    outputs.files(platformsDir.map { it.file("app.xcodeproj/project.pbxproj") }).withPropertyName("pbxproj")
    // The plugin rewrites project.yml (without UIKit) on every prepare run
    outputs.upToDateWhen { false }
    doLast {
        val dir = platformsDir.get().asFile
        val yml = File(dir, "project.yml")
        if (!yml.exists()) return@doLast
        val text = yml.readText()
        var patched = text
        if (!patched.contains("UIKit")) {
            patched = Regex("(?m)^(\\s*)- framework: (\\.\\./)").replace(patched) { m ->
                "${m.groupValues[1]}- sdk: UIKit.framework\n${m.groupValues[1]}- framework: ${m.groupValues[2]}"
            }
        }
        // Embedded GameMain.framework lives in the app bundle; without a
        // runpath dyld can't resolve @rpath/GameMain.framework at launch.
        if (!patched.contains("LD_RUNPATH_SEARCH_PATHS")) {
            patched = patched.replace(
                "ENABLE_BITCODE: NO",
                "ENABLE_BITCODE: NO\n  LD_RUNPATH_SEARCH_PATHS: \$(inherited) @executable_path/Frameworks",
            )
        }
        if (patched != text) {
            yml.writeText(patched)
        }
        val xcodegen = listOf(
            File(System.getProperty("user.home"), ".korge/XcodeGen-2.42.0/.build/release/xcodegen"),
            File("/usr/local/bin/xcodegen"),
        ).firstOrNull { it.exists() } ?: error("Can't find xcodegen (looked in ~/.korge/XcodeGen-2.42.0 and /usr/local/bin)")
        val proc = ProcessBuilder(xcodegen.absolutePath)
            .directory(dir)
            .redirectErrorStream(true)
            .start()
        val output = proc.inputStream.readBytes().decodeToString()
        if (proc.waitFor() != 0) error("xcodegen failed:\n$output")
    }
}
tasks.matching { it.name.startsWith("iosBuild") }.configureEach {
    dependsOn(patchGeneratedIosProject)
    // So a regenerated project (UIKit + runpath) rebuilds the .app
    inputs.file(layout.buildDirectory.file("platforms/ios/app.xcodeproj/project.pbxproj"))
        .withPropertyName("generatedPbxproj")
}
patchGeneratedIosProject { dependsOn("prepareKotlinNativeIosProject") }

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
