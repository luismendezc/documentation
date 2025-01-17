// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("org.jetbrains.kotlin.jvm") version "2.1.0" apply false
    id ("com.autonomousapps.dependency-analysis") version "2.7.0"
}

dependencyAnalysis {
    issues {
        project(":app") {
            onUnusedDependencies {
                severity("ignore")
            }
            onUsedTransitiveDependencies {
                severity("warn")
            }
        }
    }
}

tasks.register("customBuildHealth") {
    group = "verification" // Optional
    description = "Runs the buildHealth task to analyze build performance."

    dependsOn("buildHealth")
}
