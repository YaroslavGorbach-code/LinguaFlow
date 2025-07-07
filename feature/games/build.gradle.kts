plugins {
    id(Plugins.ksp)
    id(Plugins.hilt)
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.compose)
    id(Plugins.kotlinXserealisation)
}

apply {
    from("$rootDir/common-android-library-compose.gradle")
}

android {
    namespace = "com.korop.yaroslavhorach.games"
}

dependencies {
    implementation(project(Modules.coreDesignSystem))
    implementation(Libs.hilt)
    implementation(Libs.hiltNavigationCompose)
    ksp(Libs.hiltCompiler)
    implementation(Libs.kotlinSerealistion)
}