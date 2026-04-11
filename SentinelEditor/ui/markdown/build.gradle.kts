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
}

dependencies {
    implementation(project(":core:network"))
    
    // Markwon (Markdown rendering)
    val markwonVersion = project.findProperty("markwonVersion")?.toString() ?: "4.6.6"
    implementation("io.noties.markwon:core:$markwonVersion")
    implementation("io.noties.markwon:ext-autolink:$markwonVersion")
    implementation("io.noties.markwon:ext-gfm:$markwonVersion")
    implementation("io.noties.markwon:html:$markwonVersion")
    implementation("io.noties.markwon:image-glide:$markwonVersion")
    implementation("io.noties.markwon:ext-spoiler:$markwonVersion")
    implementation("io.noties.markwon:ext-smartypants:$markwonVersion")
    implementation("io.noties.markwon:ext-strikethrough:$markwonVersion")
    implementation("io.noties.markwon:ext-superscript:$markwonVersion")
    implementation("io.noties.markwon:ext-subscript:$markwonVersion")
    
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