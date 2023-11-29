plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
}
apply(from = "../shared_dependencies.gradle")

android {
    namespace = "com.neotica.core"
    compileSdk = 33

    defaultConfig {
        minSdk = 27

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    //DataStore
    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    //Coroutines
    val kotlinVersion = "1.6.4"
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinVersion")

    //room
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    ksp("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-paging:2.6.0")

    //camera
    val cameraVersion = "1.3.0"
    api("androidx.camera:camera-camera2:$cameraVersion")
    api("androidx.camera:camera-lifecycle:$cameraVersion")
    api("androidx.camera:camera-view:$cameraVersion")

}