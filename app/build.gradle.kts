plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "com.terrencealuda.tcardio"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.terrencealuda.tcardio"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    buildFeatures {
        viewBinding(true)
        compose(true)
        mlModelBinding(true)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xjvm-default=all"
    }
    namespace = "com.terrencealuda.tcardio"
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation "androidx.core:core-ktx:1.10.1"
    implementation "com.google.android.gms:play-services-wearable:18.0.0"
    implementation "androidx.percentlayout:percentlayout:1.0.0"
    implementation "androidx.legacy:legacy-support-v4:1.0.0"
    implementation "androidx.recyclerview:recyclerview:1.3.0"
    implementation "androidx.wear:wear:1.2.0"

    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
    implementation "androidx.fragment:fragment-ktx:1.6.0"

    // Hilt dependency injection
    implementation "com.google.dagger:hilt-android:2.43.2"
    kapt "com.google.dagger:hilt-android-compiler:2.43.2"
    implementation "androidx.hilt:hilt-work:1.0.0"
    kapt "androidx.hilt:hilt-compiler:1.0.0"

    implementation "androidx.compose.ui:ui:1.2.0-alpha05"
    implementation "androidx.wear.compose:compose-material:1.3.0-alpha01"
    implementation "androidx.wear.compose:compose-foundation:1.3.0-alpha01"
    implementation "androidx.compose.ui:ui-tooling-preview:1.2.0-alpha05"
    implementation "androidx.activity:activity-compose:1.7.2"
    implementation "org.tensorflow:tensorflow-lite:2.9.0"
    implementation "org.tensorflow:tensorflow-lite-gpu:2.10.0"
    implementation "org.tensorflow:tensorflow-lite-support:0.4.2"
    implementation "org.tensorflow:tensorflow-lite-metadata:0.1.0"
    androidTestImplementation "androidx.compose.ui:ui-test-junit4:1.2.0-alpha05"
    debugImplementation "androidx.compose.ui:ui-tooling:1.2.0-alpha05"
    debugImplementation "androidx.compose.ui:ui-test-manifest:1.2.0-alpha05"


    // Compose for Wear OS Dependencies
    implementation "androidx.wear.compose:compose-material:1.3.0-alpha01"
    implementation "androidx.compose.material:material-icons-extended:1.2.0-alpha05"

    // Wear
    implementation "androidx.wear:wear:1.2.0"

    // Health Services
    implementation "androidx.health:health-services-client:1.0.0-beta03"


    // Used to bridge between Futures and coroutines
    implementation "com.google.guava:guava:30.1.1-android"
    implementation "androidx.concurrent:concurrent-futures-ktx:1.1.0"

    // Datastore
    implementation "androidx.datastore:datastore-preferences:1.0.0"

    // Lifecycle
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
    implementation "androidx.lifecycle:lifecycle-service:2.6.1"

    // WorkManager
    implementation "androidx.work:work-runtime:2.8.1"
    implementation "androidx.work:work-runtime-ktx:2.8.1"

    // Material
    implementation "com.google.android.material:material:1.9.0"

    //composescope
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1"

    //room
    implementation "androidx.room:room-runtime:2.5.2"
    kapt "androidx.room:room-compiler:2.5.2"

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation "androidx.room:room-ktx:2.5.2"
}