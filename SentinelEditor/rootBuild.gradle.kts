// Root build.gradle.kts for SentinelEditor
// Main project configuration file
// License: Apache 2.0 via com.sentinel.editor

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}

// Enable AAPT2 vector drawable compatibility
android {
    if (files("gradle.properties").exists()) {
        val properties = java.util.Properties()
        try {
            properties.load(java.io.FileInputStream("gradle.properties"))
            if (properties.getProperty("vectorDrawables.useSupportLibrary", "true") == "true") {
                vectorDrawables {
                    useSupportLibrary = properties.getProperty("vectorDrawables.useSupportLibrary", "true").toBoolean()
                }
            }
        } catch (e: Exception) {
            // Silently ignore
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootDir.resolve("build"))
}
