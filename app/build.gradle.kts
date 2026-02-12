plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.teammaravillaapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.teammaravillaapp"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:5131/\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:5131/\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // -------------------------
    // AndroidX Core + Lifecycle
    // -------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // -------------------------
    // Compose (BOM + UI)
    // -------------------------
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.compose.runtime.saveable)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // -------------------------
    // Navigation
    // -------------------------
    implementation(libs.androidx.navigation.compose)

    // -------------------------
    // DataStore (Preferences)
    // -------------------------
    implementation(libs.androidx.datastore.preferences)

    // -------------------------
    // Hilt (DI)
    // -------------------------
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // -------------------------
    // Retrofit / OkHttp
    // -------------------------
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.okhttp.logging)

    // -------------------------
    // Coil (Images)
    // -------------------------
    implementation(libs.coil.compose)

    // -------------------------
    // Room (DB)
    // -------------------------
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // -------------------------
    // CameraX
    // -------------------------
    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.view)

    // -------------------------
    // Crop (uCrop)
    // -------------------------
    implementation(libs.ucrop)

    // -------------------------
    // Otros
    // -------------------------
    implementation(libs.volley)
    implementation(libs.androidx.benchmark.traceprocessor)

    // -------------------------
    // Tests
    // -------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}