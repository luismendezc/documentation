plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  // Kotlin serialization plugin for type safe routes and navigation arguments
  kotlin("plugin.serialization") version "2.0.21"
  id("kotlin-kapt")
  id("androidx.navigation.safeargs.kotlin")
}

android {
  namespace = "com.oceloti.lemc.masterapp"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.oceloti.lemc.masterapp"
    minSdk = 28
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }

  buildFeatures {
    viewBinding = true
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

  val nav_version = "2.8.7"

  // Jetpack Compose integration
  implementation("androidx.navigation:navigation-compose:$nav_version")

  // Views/Fragments integration
  implementation("androidx.navigation:navigation-fragment:$nav_version")
  implementation("androidx.navigation:navigation-ui:$nav_version")

  // Feature module support for Fragments
  implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

  // Testing Navigation
  androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

  // JSON serialization library, works with the Kotlin serialization plugin
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
  //https://developer.android.com/jetpack/androidx/releases/navigation

  // Lifecycle and ViewModel (MVVM)
  implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
  implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

  // RecyclerView
  implementation("androidx.recyclerview:recyclerview:1.3.2")

}