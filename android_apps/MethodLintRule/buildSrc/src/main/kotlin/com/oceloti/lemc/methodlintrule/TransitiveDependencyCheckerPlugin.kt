package com.oceloti.lemc.methodlintrule

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class CheckUsedTransitiveDependenciesTask : DefaultTask() {

  @TaskAction
  fun checkDependencies() {
    // ✅ Allow only Android and Kotlin standard dependencies
    val allowedGroupPrefixes = listOf(
      "androidx.",            // Android Jetpack libraries
      "com.google.android.",  // Google Android components
      "org.jetbrains.kotlin", // Kotlin standard libraries
      "com.android."          // Android Gradle plugins/tools
    )

    // ✅ Filter resolvable runtime configurations only (ignore test configurations)
    //!config.name.contains("test", ignoreCase = true)
    val resolvableConfigurations = project.configurations.filter { config ->
      config.isCanBeResolved &&
          config.name.contains("runtimeClasspath", ignoreCase = true)
    }

    // 1. Collect explicitly declared dependencies (implementation, api)
    val declaredDeps = project.configurations
      .filter { config ->
        config.name in listOf("implementation", "api", "compileOnly", "runtimeOnly", "testImplementation", "androidTestImplementation", "debugImplementation")
      }
      .flatMap { config -> config.dependencies }
      .map { "${it.group}:${it.name}" }
      .toSet()

    // 2. Collect all resolved dependencies (including transitives)
    val resolvedDeps = resolvableConfigurations
      .flatMap { config -> config.resolvedConfiguration.resolvedArtifacts }
      .map { "${it.moduleVersion.id.group}:${it.name}" }
      .toSet()

    // 3. Find transitives: resolved but not declared
    val transitives = resolvedDeps - declaredDeps

    // 4. Scan source code for usage of transitives
    val sourceFiles = project.fileTree("src/main/java") {
      include("**/*.java", "**/*.kt")
    }

    val usedTransitively = mutableSetOf<String>()

    // 4. Scan source code for usage of transitives and track file + line number
    for (dep in transitives) {
      val depName = dep.split(":")[1]
      for (file in sourceFiles) {
        file.readLines().forEachIndexed { index, line ->
          if (line.contains(depName)) {
            usedTransitively.add(dep)
            println("❗ Found usage of $dep in file: ${file.path} at line ${index + 1}")
          }
        }
      }
    }

    // 5. Filter out allowed dependencies
    val illegalTransitivesInUse = usedTransitively.filterNot { dep ->
      allowedGroupPrefixes.any { prefix -> dep.startsWith(prefix) }
    }

    // 6. Fail the build if illegal external dependencies are in use
    if (illegalTransitivesInUse.isNotEmpty()) {
      throw RuntimeException("❌ Used external transitive dependencies detected without explicit declaration:\n${illegalTransitivesInUse.joinToString("\n")}")
    }

    println("✅ No unexpected used external transitive dependencies found.")
  }
}

class TransitiveDependencyCheckerPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.tasks.register("checkUsedTransitiveDependencies", CheckUsedTransitiveDependenciesTask::class.java).configure {
      group = "verification"
      description = "Detects external transitive dependencies that are actually used in the code."
    }
  }
}
