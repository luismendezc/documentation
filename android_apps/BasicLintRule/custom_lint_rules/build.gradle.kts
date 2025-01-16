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
    val lintVersion = "31.5.2"

    compileOnly("com.android.tools.lint:lint-api:$lintVersion")
    testImplementation("com.android.tools.lint:lint-api:$lintVersion")
    testImplementation("com.android.tools.lint:lint-tests:$lintVersion")
    testImplementation("junit:junit:4.13.2")
}

tasks.withType<Jar> {
    manifest {
        attributes["Lint-Registry-v2"] = "com.oceloti.lemc.custom_lint_rules.CustomIssueRegistry"
    }
}