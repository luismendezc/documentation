import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.android

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.serialization)
  id("maven-publish")
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "com.oceloti.lemc.designlemc"
  compileSdk = 34

  defaultConfig {
    minSdk = 28

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      // IMPORTANT: Turn on minification/obfuscation
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

    }
    debug {
      // Debug mode typically has minify off
      isMinifyEnabled = false
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

  publishing {
    singleVariant("release") {
      // do not call withSourcesJar() if you don't want sources
      // do not call withJavadocJar() if you don't want javadocs
    }
  }
}

dependencies {
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
  val nav_version = "2.8.5"

  // Jetpack Compose Integration
  implementation("androidx.navigation:navigation-compose:$nav_version")

  // Views/Fragments Integration
  implementation("androidx.navigation:navigation-fragment:$nav_version")
  implementation("androidx.navigation:navigation-ui:$nav_version")

  // Feature module support for Fragments
  implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

  // Testing Navigation
  androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

  // JSON serialization library, works with the Kotlin serialization plugin.
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")


  implementation("com.squareup.retrofit2:converter-gson:2.9.0")

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
  tasks.withType<Jar>().configureEach {
    if (name == "sourcesJar" || name == "javaSourcesJar") {
      enabled = false
    }
  }
}

afterEvaluate {
  publishing {
    publications {
      create("release", MavenPublication::class.java) {
        from(components["release"])
        groupId = "com.oceloti.lemc"
        artifactId = "designlemc"
        version = "0.0.8"
      }
      named<MavenPublication>("release") {
        // Remove any "sources" artifacts from being published
        artifacts.removeAll { it.classifier == "sources" }
      }
    }
  }
}

tasks.register("publishRelease") {
  dependsOn("publishReleasePublicationToMavenLocal")
}


