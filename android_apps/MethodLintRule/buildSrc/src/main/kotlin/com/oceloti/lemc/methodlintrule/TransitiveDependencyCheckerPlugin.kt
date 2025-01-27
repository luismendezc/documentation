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

    // 6. Scan for unauthorized imports in source files
    val unauthorizedImports = mutableListOf<Pair<File, Int>>() // File and line numbers of unauthorized imports
    val disallowedImportsRegex = Regex("import\\s+[^\\s]+") // Match all import statements

    for (file in sourceFiles) {
      file.readLines().forEachIndexed { index, line ->
        val match = disallowedImportsRegex.find(line)
        if (match != null) {
          val importedClass = match.value.removePrefix("import ").trim()
          val declaredDependenciesGroups = declaredDeps.map { it.substringBefore(":") }
          val resolvedDependenciesGroups = resolvedDeps.map { it.substringBefore(":") }

          // If the import does not belong to explicitly declared or resolved dependencies
          if (!declaredDependenciesGroups.any { importedClass.startsWith(it) } &&
            !resolvedDependenciesGroups.any { importedClass.startsWith(it) }) {
            unauthorizedImports.add(file to index + 1)
            println("❗ Unauthorized import detected in file: ${file.path} at line ${index + 1}: $importedClass")
          }
        }
      }
    }

    // 7. Fail the build if illegal transitive dependencies or unauthorized imports are detected
    if (illegalTransitivesInUse.isNotEmpty() || unauthorizedImports.isNotEmpty()) {
      val message = buildString {
        append("❌ Build failed due to the following issues:\n")
        if (illegalTransitivesInUse.isNotEmpty()) {
          append("Used external transitive dependencies detected without explicit declaration:\n")
          append(illegalTransitivesInUse.joinToString("\n"))
          append("\n")
        }
        if (unauthorizedImports.isNotEmpty()) {
          append("Unauthorized imports found:\n")
          append(unauthorizedImports.joinToString("\n") { (file, line) -> "${file.path}:$line" })
        }
      }
      throw RuntimeException(message)
    }

    println("✅ No unexpected used external transitive dependencies or unauthorized imports found.")
  }
}

class TransitiveDependencyCheckerPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.tasks.register("checkUsedTransitiveDependencies", CheckUsedTransitiveDependenciesTask::class.java).configure {
      group = "verification"
      description = "Detects external transitive dependencies and unauthorized imports that are actually used in the code."
    }
  }
}
