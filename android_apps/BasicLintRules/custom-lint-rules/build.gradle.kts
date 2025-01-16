plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("com.android.lint")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    // Define the version of the Android Lint API.
    // Adjust this version to match the one used in your Android tooling.
    val lintVersion = "31.5.2"

    // Add the lint API as a compile-only dependency.
    // This dependency is needed at compile time to develop your lint checks.
    compileOnly("com.android.tools.lint:lint-api:$lintVersion")

    // Add lint API for testing purposes.
    testImplementation("com.android.tools.lint:lint-api:$lintVersion")
    // Lint test utilities help in writing tests for your custom lint rules.
    testImplementation("com.android.tools.lint:lint-tests:$lintVersion")
    // Include JUnit for writing unit tests.
    testImplementation("junit:junit:4.13.2")
}

// Customize the JAR manifest to register your custom lint registry class.
// The lint tool uses the "Lint-Registry-v2" attribute to find and load custom lint rules.
// Replace "com.yourdomain.yourproject.lint.CustomLintRegistry" with your actual fully qualified class name.
//tasks.jar { manifest { attributes("Lint-Registry-v2" to "com.oceloti.lemc.custom_lint_rules.CustomLintRegistry") } }
tasks.withType<Jar> {
    manifest {
        attributes["Lint-Registry-v2"] = "com.oceloti.lemc.custom_lint_rules.CustomIssueRegistry"
    }
}
