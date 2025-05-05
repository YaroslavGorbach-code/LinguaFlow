plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.ksp)
    id(Plugins.hilt)
}

apply {
    from("$rootDir/common-android-library.gradle")
}

android {
    namespace = "com.example.yaroslavhorach.database"
}

dependencies {
    implementation(project(Modules.domain))

    implementation(Libs.coroutines)
    implementation(Libs.roomRuntime)
    implementation(Libs.roomKtx)
    ksp(Libs.roomCompiler)

    implementation(Libs.hilt)
    ksp(Libs.hiltCompiler)
}