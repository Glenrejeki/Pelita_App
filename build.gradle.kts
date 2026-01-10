// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Kotlin 2.0 + Compose plugin
    alias(libs.plugins.kotlin.compose) apply false

    // Kotlin Serialization
    alias(libs.plugins.kotlin.serialization) apply false

    // Hilt
    alias(libs.plugins.hilt.android) apply false

    // Kapt
    alias(libs.plugins.kotlin.kapt) apply false
}
