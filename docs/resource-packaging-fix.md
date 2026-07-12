# Resource Packaging Fix for Android

## Problem
Resources from `shared/src/commonMain/resources/` (like `korge.png`) were not being packaged into the Android APK, causing runtime errors when the app tried to load them.

## Solution
Updated `shared/build.gradle.kts` to configure the `androidMain` source set to include resources from `commonMain/resources`.

### Changes Made

**File: `shared/build.gradle.kts`**

Added the following configuration to the `sourceSets` block:

```kotlin
androidMain {
    resources.srcDirs("src/commonMain/resources")
}
```

This tells the Android build system to include all resources from the `commonMain/resources` directory when building the Android library module, which then gets packaged into the final APK.

## Verification

The fix was verified by:
1. Cleaning the project: `./gradlew clean`
2. Building the Android app: `./gradlew :androidApp:assembleDebug`
3. Checking the APK contents: `unzip -l androidApp/build/outputs/apk/debug/androidApp-debug.apk | grep korge.png`

The `korge.png` file is now present in the APK at the root level, which is where KorGE's `resourcesVfs["korge.png"]` expects to find it.

## How to Use

1. Build and install the APK on your Android device:
   ```bash
   ./gradlew :androidApp:installDebug
   ```

2. Or manually install the APK:
   ```bash
   adb install androidApp/build/outputs/apk/debug/androidApp-debug.apk
   ```

## Adding More Resources

Any files you place in `shared/src/commonMain/resources/` will now automatically be packaged into:
- Android APK (at root level)
- JVM JAR (in resources)
- Other platforms as configured

Just place your assets in `shared/src/commonMain/resources/` and reference them using `resourcesVfs["filename"]` in your KorGE code.

