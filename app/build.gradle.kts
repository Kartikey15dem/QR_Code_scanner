plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.camerax"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.camerax"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.camera.core)
    implementation (libs.camera.camera2)
// If you want to additionally use the CameraX Lifecycle library
    implementation (libs.camera.lifecycle)
// If you want to additionally use the CameraX VideoCapture library
    implementation (libs.camera.video)
// If you want to additionally use the CameraX View class
    implementation (libs.camera.view)
// If you want to additionally add CameraX ML Kit Vision Integration
    implementation (libs.camera.mlkit.vision)
// If you want to additionally use the CameraX Extensions library
    implementation (libs.camera.extensions)
    implementation (libs.barcode.scanning)
    implementation (libs.play.services.mlkit.barcode.scanning)
    implementation (libs.play.services.location)
    implementation (libs.lottie)

}