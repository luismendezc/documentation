// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

configurations.all{
    resolutionStrategy.eachDependency {
        if (requested.group == "com.google.guava") {
            useVersion("31.1-jre")
            because("Dependency Analysis plugin requires Guava 31.1-jre for reachableNodes.")
        }
    }
}
