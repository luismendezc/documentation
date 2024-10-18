plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.lab_testing_coroutines"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.lab_testing_coroutines"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

val koin_version = "4.0.0"
val mockk_version = "1.13.13"

dependencies {
    // Koin for Kotlin
    implementation ("io.insert-koin:koin-core:$koin_version")
    // Koin main features for Android
    implementation ("io.insert-koin:koin-android:$koin_version")

    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")

    // Koin for unit tests
    testImplementation ("io.insert-koin:koin-test:$koin_version")
    androidTestImplementation ("io.insert-koin:koin-test:$koin_version")
    // Koin for JUnit 4
    testImplementation ("io.insert-koin:koin-test-junit4:$koin_version")
    androidTestImplementation ("io.insert-koin:koin-test-junit4:$koin_version")

    //mockK
    testImplementation ("io.mockk:mockk:$mockk_version")
    androidTestImplementation("io.mockk:mockk-android:$mockk_version")

    testImplementation("androidx.arch.core:core-testing:2.2.0")




}