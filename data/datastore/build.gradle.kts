import com.google.protobuf.gradle.proto

plugins {
    id(Plugins.protobuf) version "0.9.4"
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.ksp)
    id(Plugins.hilt)
}

apply {
    from("$rootDir/common-android-library.gradle")
}

android {
    namespace = "com.example.yaroslavhorach.datastore"
    sourceSets {
        getByName("main") {
            proto {srcDir("src/main/proto") }
        }
    }
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.23.4"
    }
    generateProtoTasks {
        all().configureEach {
            builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}
dependencies {
    implementation(Libs.datastorePreferences)
    implementation(Libs.protobufJavalite)
    implementation(Libs.protobufKotlinlite)
    implementation(project(Modules.coreCommon))
    implementation(project(Modules.domain))
    implementation(Libs.coroutines)
    implementation(Libs.hilt)
    ksp(Libs.hiltCompiler)
}
