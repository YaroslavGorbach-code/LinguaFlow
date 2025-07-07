plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.compose)
    id(Plugins.hilt)
    id(Plugins.ksp)
}

apply {
    from("$rootDir/common-android-library-compose.gradle")
}

android {
    namespace = "com.korop.yaroslavhorach.common"
}

dependencies {
    implementation(project(Modules.domain))

    implementation(Libs.coreKtx)
    implementation(Libs.appCompat)
    implementation(Libs.viewModel)
    implementation(Libs.composeRuntime)
    implementation(Libs.composeUtil)
    implementation(Libs.activityCompose)
    implementation(Libs.composeFoundation)
    implementation(Libs.hilt)
    ksp(Libs.hiltCompiler)
}