// UI markdown rendering module
// Contains composables for markdown preview, editor, diff view
// License: Apache 2.0 via com.sentinel.editor

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.sentinel.ui.markdown"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = project.findProperty("composeCompilerVersion")?.toString() ?: "1.5.14"
    }
}

dependencies {
    implementation(project(":core:network"))
    
    // Compose BOM
    val composeBomVersion = project.findProperty("composeBomVersion")?.toString() ?: "2024.12.01"
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.activity:activity-compose:1.9.3")

    // Markwon (Markdown rendering)
    val markwonVersion = project.findProperty("markwonVersion")?.toString() ?: "4.6.2"
    implementation("io.noties.markwon:core:$markwonVersion")
    implementation("io.noties.markwon:html:$markwonVersion")
    implementation("io.noties.markwon:linkify:$markwonVersion")
    
    // Coil
    val coilVersion = project.findProperty("coilVersion")?.toString() ?: "2.7.0"
    implementation("io.coil-kt:coil-compose:$coilVersion")
    implementation("io.coil-kt:coil:$coilVersion")
    
    // Lottie
    val lottieVersion = project.findProperty("lottieVersion")?.toString() ?: "6.6.0"
    implementation("com.airbnb.android:lottie-compose:$lottieVersion")
    
    // Accompanist
    val accompanistPermissionsVersion = project.findProperty("accompanistPermissionsVersion")?.toString() ?: "0.36.0"
    implementation("com.google.accompanist:accompanist-permissions:$accompanistPermissionsVersion")
    
    // Navigation
    val navigationVersion = project.findProperty("navigationVersion")?.toString() ?: "2.8.4"
    implementation("androidx.navigation:navigation-compose:$navigationVersion")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
}