import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.cyclonedx.bom") version "1.10.0"
}
group = "com.oceloti.lemc"
version = "1.0.0"

android {
    namespace = "com.oceloti.lemc.labtestcoroutinesflows"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.oceloti.lemc.labtestcoroutinesflows"
        minSdk = 26
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
    }
}

dependencies {
    // Koin for Kotlin
    implementation (libs.koin.core)
    // Koin main features for Android
    implementation (libs.koin.android)

    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Coroutines
    testImplementation(libs.kotlinx.coroutines.test)

    // Koin for unit tests
    testImplementation (libs.koin.test)
    androidTestImplementation (libs.koin.test)
    // Koin for JUnit 4
    testImplementation (libs.koin.test.junit4)
    androidTestImplementation (libs.koin.test.junit4)

    //mockK
    testImplementation (libs.mockk)
    androidTestImplementation(libs.mockk.android)

    // turbine
    testImplementation(libs.turbine)

    testImplementation(libs.androidx.core.testing)
}
