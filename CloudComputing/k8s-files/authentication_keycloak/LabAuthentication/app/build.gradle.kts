plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  kotlin("plugin.serialization") version "2.1.0"
}

android {
  namespace = "com.oceloti.lemc.labauthentication"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.oceloti.lemc.labauthentication"
    minSdk = 28
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    manifestPlaceholders.putAll(
      mapOf(
        "appAuthRedirectScheme" to "com.oceloti.lemc.labauthentication"
      )
    )

  }

  signingConfigs {
    create("release") {
      storeFile = file("release-key.jks")
      storePassword = "admin123"
      keyAlias = "keyAlias"
      keyPassword = "admin123"
    }
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    isCoreLibraryDesugaringEnabled = true
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  buildFeatures {
    compose = true
  }
}

dependencies {
  coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

  // Added libraries for the project
  implementation(project.dependencies.platform(libs.koin.bom))
  implementation(libs.koin.core)
  implementation(libs.koin.android)
  implementation(libs.koin.compose)



//  implementation(libs.appauth)

  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
  implementation("androidx.core:core-splashscreen:1.0.1")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
  implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")
  implementation("com.nimbusds:nimbus-jose-jwt:9.47")
  implementation("org.bitbucket.b_c:jose4j:0.9.6")

  implementation(libs.browser)
  implementation("androidx.navigation:navigation-compose:2.8.4")



  // ----------------------------------------

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))

  //also added
  implementation(libs.androidx.lifecycle.runtime.compose)
  // --------------------
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)


  implementation(project(":design-uilemc"))
}