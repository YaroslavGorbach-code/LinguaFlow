// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(Plugins.androidApplication) version "8.7.2" apply false
    id(Plugins.androidLibrary) version "8.7.2" apply false
    id(Plugins.kotlinAndroid) version "2.1.10" apply false
    id(Plugins.kotlinJvm) version "2.1.10" apply false
    id(Plugins.compose) version "2.0.0" apply false
    id(Plugins.hilt) version Versions.hilt apply false
    id(Plugins.ksp) version "2.0.21-1.0.27" apply false
}