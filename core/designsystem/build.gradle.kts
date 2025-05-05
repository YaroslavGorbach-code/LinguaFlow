plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.compose)
}

apply {
    from("$rootDir/common-android-library-compose.gradle")
}

android {
    namespace = "com.example.yaroslavhorach.designsystem"
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
}