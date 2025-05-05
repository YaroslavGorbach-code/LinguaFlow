plugins {
    id(Plugins.javaLibrary)
    id(Plugins.kotlinJvm)
}

java {
    sourceCompatibility = AppConfig.javaCompatibilityVersion
    targetCompatibility = AppConfig.javaCompatibilityVersion
}

dependencies {
    implementation(Libs.coroutines)
}