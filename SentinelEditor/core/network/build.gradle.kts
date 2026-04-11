// Core network module for SentinelEditor
// Contains Retrofit configurations, GitHub API clients, interceptors
// License: Apache 2.0 via com.sentinel.editor

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.sentinel.core.network"
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
    
    // API generation
    buildFeatures {
        kapt = true
    }
}

dependencies {
    implementation(project(":app"))
    
    // OkHttp
    val okhttpVersion = "${project.property("okhttpVersion").or("4.12.0")}"
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation("com.squareup.okhttp3:connectivity:$okhttpVersion")
    
    // Retrofit
    val retrofitVersion = "${project.property("retrofitVersion").or("2.11.0")}"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    
    // Gson
    impl("com.google.code.gson:gson:2.11.0")
}