plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  id("com.oceloti.lemc.methodlintrule")
  id("com.autonomousapps.build-health")
}

android {
  namespace = "com.oceloti.lemc.methodlintrule"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.oceloti.lemc.methodlintrule"
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
    compose = true
  }
}


dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
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

  implementation("com.squareup.retrofit2:retrofit:2.9.0")
}

// Custom task to run the transitive dependency check
tasks.register("runTransitiveDependencyCheck") {
  group = "verification"
  description = "Runs the transitive dependency check task."

  dependsOn("checkUsedTransitiveDependencies")  // Links to the existing plugin task

  doLast {
    println("✅ Finished running the Transitive Dependency Checker.")
  }
}

tasks.register("runBuildHealth") {
  group = "verification"
  description = "Runs the buildHealth task for dependency analysis."

  dependsOn("buildHealth") // Links to the existing buildHealth task.

  doLast {
    println("✅ Finished running the buildHealth task.")
  }
}
