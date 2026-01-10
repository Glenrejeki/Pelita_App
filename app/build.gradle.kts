plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // ✅ Kotlin 2.0 Compose plugin (WAJIB untuk Kotlin 2.x)
    alias(libs.plugins.kotlin.compose)

    // ✅ WAJIB kalau pakai @Serializable
    alias(libs.plugins.kotlin.serialization)

    // ✅ Hilt
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.example.pelitaapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.pelitaapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }

        // OPTIONAL: kalau mau simpan supabase url/key di BuildConfig
        // buildConfigField("String", "SUPABASE_URL", "\"https://xxxx.supabase.co\"")
        // buildConfigField("String", "SUPABASE_ANON_KEY", "\"YOUR_ANON_KEY\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // ✅ Java 17
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        // ✅ biar java.time.Instant aman di minSdk 24/25
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions { jvmTarget = "17" }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // =========================
    // Compose (pakai BOM)
    // =========================
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Icons extended (Icons.Default.*)
    implementation(libs.androidx.compose.material.icons.extended)

    // Debug tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // =========================
    // DataStore (tema)
    // =========================
    implementation(libs.androidx.datastore.preferences)

    // =========================
    // Kotlin Serialization
    // =========================
    implementation(libs.kotlinx.serialization.json)

    // =========================
    // Supabase-kt (jan-tennert)
    // =========================
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.storage) // kalau kamu install(Storage) di client

    // Supabase-kt butuh ktor engine
    implementation(libs.ktor.okhttp)

    // =========================
    // Desugaring (java.time)
    // =========================
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // =========================
    // Hilt
    // =========================
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // =========================
    // Testing
    // =========================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
