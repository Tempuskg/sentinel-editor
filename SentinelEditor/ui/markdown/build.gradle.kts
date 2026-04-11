// UI markdown rendering module
// Contains composables for markdown preview, editor, diff view
// License: Apache 2.0 via com.sentinel.editor

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
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
    implementation(project(":app"))
    implementation(project(":core:network"))
    
    // Markwon (Markdown rendering)
    val markwonVersion = "${project.property("markwonVersion").or("4.6.6")}"
    implementation("io.noties.markwon:markwon:$markwonVersion")
    implementation("io.noties.markwon:linkify:$markwonVersion")
    implementation("io.noties.markwon:html:$markwonVersion")
    implementation("io.noties.markwon:jerkson:$markwonVersion")
    
    // CommonMark
    val commonmarkVersion = "${project.property("commonmarkVersion").or("0.21.0")}"
    implementation("org.commonmark:commonmark:$commonmarkVersion")
    
    // PagedText
    val pagedtextVersion = "${project.property("pagedtextVersion").or("2.2")}"
    implementation("com.github.juan-nino:PagedText:$pagedtextVersion")
    
    // Coil
    val coilVersion = "${project.property("coilVersion").or("2.7.0")}"
    implementation("io.coil-kt:coil-compose:$coilVersion")
    implementation("io.coil-kt:coil:$coilVersion")
    
    // Lottie
    val lottieVersion = "${project.property("lottieVersion").or("6.6.0")}"
    implementation("com.airbnb.android:lottie-compose:$lottieVersion")
    
    // Accompanist
    val accompanistPermissionsVersion = "${project.property("accompanistPermissionsVersion").or("0.36.0")}"
    implementation("com.google.accompanist:accompanist-permissions:$accompanistPermissionsVersion")
    
    // Navigation
    val navigationVersion = "${project.property("navigationVersion").or("2.8.4")}"
    implementation("androidx.navigation:navigation-compose:$navigationVersion")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
}