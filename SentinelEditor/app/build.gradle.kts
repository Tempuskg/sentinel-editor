plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.sentinel.editor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sentinel.editor"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2;LOCL2,INDEX,LICENSE,LF,NOTICE,README,TEXT,HEADER,COPYING}"
        }
    }
}

dependencies {
    // ============ ANDROIDX CORE COMPONENTS (Apache-2.0) ============
    
    // Compose BOM for synchronized versions
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Core Compose runtime
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    
    // AndroidX Activity & Lifecycle
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.4")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    androidTestImplementation("androidx.room:room-testing:2.6.1")
    
    // Coroutines for Room background processing
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    
    // Jetpack DataStore for settings (Kotlin-preferred over SharedPreferences)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Test dependencies
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // =========== GITHUB API CLIENTS (Apache-2.0) ===========
    
    // OkHttp (Apache-2.0) - Core HTTP client
    val okhttpVersion = "4.12.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    
    // Retrofit (Apache-2.0) - REST client
    val retrofitVersion = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofitVersion")
    
    // OKHttp + Retrofit adapter
    implementation("com.squareup.okhttp3:okhttp-urlconnection:$okhttpVersion")
    
    // Kotlin GitHub API placeholder version (currently unused)
    val githubApiVersion = "1.318"
    
    // =========== NETWORKING & PROTOCOLS ===========
    
    // Gson (Apache-2.0) - JSON serialization
    implementation("com.google.code.gson:gson:2.11.0")
    
    // Kotlin serialization (Apache-2.0)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    
    // =========== MARKDOWN PROCESSING ===========
    
    // Markor markdown renderer (Apache-2.0)
    implementation("io.noties.markwon:core:4.6.2")
    implementation("io.noties.markwon:linkify:4.6.2")
    implementation("io.noties.markwon:html:4.6.2")
    
    // =========== UI & MATERIAL ===========
    
    // Material Design 3 (Apache-2.0)
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.1")
    
    // Compose Material3 Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.1")
    
    // Lottie for animations (Apache-2.0)
    implementation("com.airbnb.android:lottie-compose:6.6.0")
    
    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil:2.7.0")
    
    // Accompanist for layout primitives in Compose
    implementation("com.google.accompanist:accompanist-pager:0.36.0")
    implementation("com.google.accompanist:accompanist-permissions:0.36.0")
    
    // =========== COMPOSITION & STATE ===========
    
    // Develocity (Gradle Enterprise) - for build metrics
    implementation("de.undercouch:gradle-download-task:5.6.0") // Task dependencies

    // =========== PERMISSIONS ===========
    
    // Request permissions API compose integration
    implementation("com.google.accompanist:accompanist-permissions:0.36.0")

    // =========== TESTING ===========
    
    // Coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    
    // Espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}