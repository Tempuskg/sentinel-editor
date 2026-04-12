// Settings configuration
rootProject.name = "SentinelEditor"

include(":app")
include(":core:database")
include(":core:network")
include(":ui:markdown")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://dl.google.com/dl/android/maven2/")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        // Maven repo for GitHub Kotlin SDK
        maven("https://pkgs.dev.azure.com/kghh/_packaging/kghh/Kotlin/maven/v1")
    }
}