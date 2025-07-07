plugins {
    id(Plugins.kotlinAndroid)
    id(Plugins.androidApplication)
    id(Plugins.compose)
    id(Plugins.ksp)
    id(Plugins.hilt)
}
android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = AppConfig.javaCompatibilityVersion
        targetCompatibility = AppConfig.javaCompatibilityVersion
    }
    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }
}

dependencies {
    implementation(project(Modules.data))
    implementation(project(Modules.coreDesignSystem))
    implementation(project(Modules.featureHome))
    implementation(project(Modules.featureGames))
    implementation(project(Modules.featureProfile))
    implementation(project(Modules.featureExercises))

    implementation(Libs.activityCompose)
    implementation(Libs.systemUiController)
    implementation(Libs.splashScreen)
    implementation(Libs.ads)
    implementation(Libs.billing)
    implementation(Libs.billingKts)

    implementation(Libs.hilt)
    implementation(Libs.hiltNavigationCompose)
    ksp(Libs.hiltCompiler)
}