import org.gradle.api.JavaVersion

object AppConfig {
    const val compileSdkVersion = 35
    const val targetSdkVersion = 35
    const val minSdkVersion = 24
    const val applicationId = "com.example.yaroslavhorach.lingoFlow"
    const val versionCode = 1
    const val versionName = "0.1"
    const val jvmTarget = "21"
    const val kotlinCompiler = "1.9.22"

    val javaCompatibilityVersion = JavaVersion.VERSION_21
    val kotlinCompatibilityVersion = JavaVersion.VERSION_21
}

object Plugins {
    const val androidApplication = "com.android.application"
    const val androidLibrary = "com.android.library"
    const val protobuf = "com.google.protobuf"
    const val javaLibrary = "java-library"
    const val kotlinAndroid = "org.jetbrains.kotlin.android"
    const val kotlinJvm = "org.jetbrains.kotlin.jvm"
    const val compose = "org.jetbrains.kotlin.plugin.compose"
    const val kapt_kotlin = "kotlin-kapt"
    const val hilt = "com.google.dagger.hilt.android"
    const val ksp = "com.google.devtools.ksp"
    const val serealisation = "plugin.serialization"
    const val kotlinXserealisation = "kotlinx-serialization"
}

object Modules {
    const val core = ":core"
    const val coreDesignSystem = ":core:designsystem"
    const val coreCommon = ":core:common"
    const val data = ":data"
    const val dataDatabase = ":data:database"
    const val dataDatastore = ":data:datastore"
    const val domain = ":domain"
    const val featureHome = ":feature:home"
    const val featureGames = ":feature:games"
    const val featureProfile = ":feature:profile"
    const val featureExercises = ":feature:exercises"
}

object Versions {
    const val coreKtx = "1.10.1"
    const val appCompat = "1.6.1"
    const val lifecycle = "2.6.1"
    const val compose = "1.7.8"
    const val material3 = "1.1.0"
    const val coroutines = "1.7.0"
    const val room = "2.6.1"
    const val gson = "2.13.1"
    const val datastorePrefs = "1.1.0"
    const val hiltNavigationCompose = "1.0.0"
    const val hilt = "2.51.1"
    const val systemUiController = "0.30.0"
    const val splashScreen = "1.0.1"
    const val navigationCompose = "2.9.0"
    const val kotlinSerealistion = "1.8.1"
    const val lottie = "6.6.6"
    const val composeDialogs = "1.2.0"
    const val protobuf = "4.29.2"
}

object Libs {
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.appCompat}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleCompose = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycle}"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.compose}"
    const val composeLayout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
    const val composeRuntime = "androidx.compose.runtime:runtime:${Versions.compose}"
    const val composeUtil = "androidx.compose.ui:ui-util:${Versions.compose}"
    const val composePreview = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeMaterial3 = "androidx.compose.material3:material3:${Versions.material3}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"
    const val datastorePreferences = "androidx.datastore:datastore-preferences:${Versions.datastorePrefs}"
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}"
    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:${Versions.systemUiController}"
    const val splashScreen = "androidx.core:core-splashscreen:${Versions.splashScreen}"
    const val navigationCompose = "androidx.navigation:navigation-compose:${Versions.navigationCompose}"
    const val kotlinSerealistion = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerealistion}"
    const val lottie = "com.airbnb.android:lottie-compose:${Versions.lottie}"
    const val protobufJavalite = "com.google.protobuf:protobuf-javalite:${Versions.protobuf}"
    const val protobufKotlinlite = "com.google.protobuf:protobuf-kotlin-lite:${Versions.protobuf}"
}