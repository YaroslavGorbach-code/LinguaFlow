import Plugins.ksp

plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.compose)
    id(Plugins.kotlinXserealisation)
    id(Plugins.hilt)
    id(Plugins.ksp)
}

apply {
    from("$rootDir/common-android-library-compose.gradle")
}

android {
    namespace = "com.korop.yaroslavhorach.designsystem"
}

dependencies {
    api(project(Modules.domain))
    api(project(Modules.coreCommon))

    api(Libs.coreKtx)
    api(Libs.appCompat)
    api(Libs.composeFoundation)
    api(Libs.composeLayout)
    api(Libs.composeRuntime)
    api(Libs.composeUtil)
    api(Libs.composeMaterial3)
    api(Libs.composeMaterial)
    api(Libs.composePreview)
    api(Libs.navigationCompose)
    api(Libs.hiltNavigationCompose)
    api(Libs.lifecycleCompose)
    api(Libs.lifecycle)
    api(Libs.lottie)
    api(Libs.kotlinSerealistion)

    ksp(Libs.hiltCompiler)
    implementation(Libs.hilt)
    implementation(Libs.hiltNavigationCompose)
}