import com.android.build.api.variant.AndroidComponentsExtension

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

project.extensions.configure<AndroidComponentsExtension<*, *, *>>("androidComponents") {
    onVariants { variant ->
        // Configure resources folder to be packaged as assets for Android target. This is required for Korge to work properly on Android.
        variant.sources.assets?.addStaticSourceDirectory("resources")
    }
}

kotlin {
    jvm {}

    android {
        namespace = "${rootProject.group}.shared"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        androidResources.enable = true
        androidResources.failOnMissingConfigEntry = true
        withHostTest {}
        withDeviceTest {}
    }

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
//            implementation(libs.)
        }

        // Configure local resources folder for all targets except Android, since Korge handles resources on Android differently
        jvmMain {
            resources.srcDirs("resources")
        }
    }
}

/*
korge {
	id = "com.sample.demo"

// To enable all targets at once

	//targetAll()

// To enable targets based on properties/environment variables
	//targetDefault()

// To selectively enable targets

	targetJvm()
	targetJs()
    targetWasm()
	targetDesktop()
	targetIos()
	targetAndroid()

	serializationJson()
}
*/
