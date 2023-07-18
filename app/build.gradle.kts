plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("org.jetbrains.kotlin.kapt")
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
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
    }
    buildFeatures {
        viewBinding = true
        compose = true
        mlModelBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xjvm-default=all"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        freeCompilerArgs += "-opt-in=com.google.android.horologist.annotations.ExperimentalHorologistApi"
    }
    namespace = "com.terrencealuda.tcardio"
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("com.google.android.gms:play-services-wearable:18.0.0")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.wear:wear:1.2.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.6.0")

    // Hilt dependency injection
    /*    implementation("com.google.dagger:hilt-android:2.43.2")
        kapt("com.google.dagger:hilt-android-compiler:2.43.2")
        implementation("androidx.hilt:hilt-work:1.0.0")
        kapt("androidx.hilt:hilt-compiler:1.0.0")

        implementation("androidx.compose.ui:ui:1.2.0-alpha05")
        implementation("androidx.wear.compose:compose-material:1.3.0-alpha01")
        implementation("androidx.wear.compose:compose-foundation:1.3.0-alpha01")
        implementation("androidx.compose.ui:ui-tooling-preview:1.2.0-alpha05")
        implementation("androidx.activity:activity-compose:1.7.2")*/
    implementation("org.tensorflow:tensorflow-lite:2.9.0")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.10.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.2")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    // Datastore
    implementation(libs.datastore.preferences)

// General compose dependencies
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui.tooling.preview)

    // Compose for Wear OS Dependencies
    implementation(libs.wear.compose.material)
    implementation("androidx.compose.material:material-icons-extended:1.3.1")
    implementation(libs.androidx.wear)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.tooling)

    // Foundation is additive, so you can use the mobile version in your Wear OS app.
    implementation(libs.wear.compose.foundation)

    implementation(libs.guava)
    implementation(libs.androidx.concurrent)

    //Wear OS Compose Navigation
    implementation(libs.compose.wear.navigation)
    implementation(libs.androidx.compose.navigation)

    //Wear Health Services
    implementation(libs.androidx.health.services)


    // Lifecycle components
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.lifecycle.runtime.livedata)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.splashscreen)


    // Ongoing Activity
    implementation(libs.wear.ongoing.activity)

    // Hilt
    implementation(libs.hilt.navigation.compose)
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)

    // Hilt dependency injection
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation(libs.horologist.compose.layout)
    implementation(libs.horologist.compose.material)
    implementation(libs.horologist.health.composables)



    // Testing

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)

    //composescope
    implementation(libs.lifecycle.viewmodel.compose)

    // WorkManager
    implementation("androidx.work:work-runtime:2.8.1")
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    val appcompat_version = "1.6.1"

    implementation("androidx.appcompat:appcompat:$appcompat_version")
    // For loading and tinting drawables on older versions of the platform
    implementation("androidx.appcompat:appcompat-resources:$appcompat_version")

    // Material
    implementation("com.google.android.material:material:1.9.0")


    //room
    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.5.2")
}