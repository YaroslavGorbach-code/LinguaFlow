plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.ksp)
    id(Plugins.hilt)
}

apply {
    from("$rootDir/common-android-library.gradle")
}

android{
    namespace = "com.example.yaroslavhorach.data"
}

dependencies {
    api(project(Modules.coreDesignSystem))
    api(project(Modules.dataDatabase))
    api(project(Modules.dataDatastore))
    implementation(Libs.coroutines)
    implementation(Libs.hilt)
    implementation(Libs.gson)
    ksp(Libs.hiltCompiler)
}