package com.oceloti.lemc.custom_lint_rules

import com.android.tools.lint.client.api.LintTomlLiteralValue
import com.android.tools.lint.client.api.LintTomlMapValue
import com.android.tools.lint.client.api.LintTomlParser
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UImportStatement
import org.jetbrains.uast.UMethod
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.EnumSet

@Suppress("UnstableApiUsage")
class DependencyImportDetector : Detector(), SourceCodeScanner {
/*
    companion object {
        val ISSUE: Issue = Issue.create(
            id = "InvalidDependencyImport",
            briefDescription = "Non-direct dependency import detected",
            explanation = """
                This rule ensures that all imported classes are part of the direct dependencies declared 
                in the Gradle or TOML files. If a class belongs to a transitive dependency and is not explicitly 
                declared, it will be flagged as a warning.
            """,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                DependencyImportDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE, Scope.GRADLE_FILE)
            )
        )
    }

    private val declaredDependencies = mutableSetOf<String>()
    private val resolvedDependencies = mutableSetOf<String>()

    override fun beforeCheckEachProject(context: Context) {
        println("🔍 [Lint] Starting dependency analysis...")

        context.project.dir.parentFile.walkTopDown().forEach { file ->
            if (file.name == "libs.versions.toml") {
                println("📂 [Lint] Parsing TOML file: ${file.absolutePath}")
                parseTomlDependencies(file)
            }
        }

        println("📦 [Lint] Declared Dependencies: $declaredDependencies")

        collectResolvedDependencies(context)

        println("📦 [Lint] Resolved Dependencies: $resolvedDependencies")
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitImportStatement(node: UImportStatement) {
                val importText = node.importReference?.asRenderString() ?: return

                println("🔎 [Lint] Checking import: $importText")

                if (isTransitiveDependency(importText)) {
                    println("⚠️ [Lint] Transitive dependency detected: $importText")
                    context.report(
                        ISSUE,
                        node,
                        context.getLocation(node),
                        "The import '$importText' is from a transitive dependency and is not declared directly."
                    )
                } else {
                    println("✅ [Lint] Valid import: $importText")
                }
            }
        }
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UMethod::class.java)
    }

    /** ✅ Parse declared dependencies from libs.versions.toml */
    private fun parseTomlDependencies(file: File) {
        val parser = LintTomlParser()
        val tomlDocument = parser.parse(file, file.readText())

        val libraries = tomlDocument.getValue("libraries") as? LintTomlMapValue ?: return

        libraries.getMappedValues().forEach { (_, library) ->
            if (library is LintTomlMapValue) {
                val group = (library["group"] as? LintTomlLiteralValue)?.getActualValue()?.toString()?.trim()
                val name = (library["name"] as? LintTomlLiteralValue)?.getActualValue()?.toString()?.trim()

                if (group != null && name != null) {
                    declaredDependencies.add("$group.$name")
                    declaredDependencies.add("$group:$name")
                    println("✅ [Lint] Declared dependency: $group:$name")
                }
            }
        }
    }

    /** ✅ Collect all resolved dependencies using Gradle command */
    private fun collectResolvedDependencies(context: Context) {
        try {
            val projectRootDir = context.project.dir.parentFile
            val gradlewPath = File(projectRootDir, "gradlew").absolutePath

            println("⚙️ [Lint] Running Gradle command in: ${projectRootDir.absolutePath}")
            println("📝 [Lint] Command: $gradlewPath app:dependencies --configuration releaseCompileClasspath")

            val process = ProcessBuilder(gradlewPath, "app:dependencies", "--configuration", "releaseCompileClasspath")
                .directory(projectRootDir)
                .redirectErrorStream(true)
                .start()

            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                val dependencyPattern = Regex("""\s*\\---\s+([a-zA-Z0-9\.\-_]+):([a-zA-Z0-9\.\-_]+):(.+)""")
                val matchResult = dependencyPattern.find(line ?: "")

                matchResult?.let {
                    val group = it.groupValues[1]
                    val name = it.groupValues[2]
                    resolvedDependencies.add("$group.$name")
                    resolvedDependencies.add("$group:$name")
                }
            }

            val exitCode = process.waitFor()
            if (exitCode != 0) {
                println("❌ [Lint] Gradle command failed with exit code $exitCode")
            }

        } catch (e: Exception) {
            println("❌ [Lint] Error collecting resolved dependencies: ${e.message}")
        }
    }

    /** 🔎 Check if an import is a transitive dependency */
    private fun isTransitiveDependency(importText: String): Boolean {
        // ✅ Ignore standard libraries and project-local imports
        if (importText.startsWith("android.") || importText.startsWith("java.") || importText.startsWith("kotlin.")) {
            return false
        }

        if (importText.startsWith("com.oceloti.lemc.basiclintrules")) {
            return false
        }

        // 🔄 Map dependencies to possible package names
        val declaredPackages = declaredDependencies.flatMap { mapToPossiblePackages(it) }.toSet()
        val resolvedPackages = resolvedDependencies.flatMap { mapToPossiblePackages(it) }.toSet()

        // ✅ If resolved but not declared, it's transitive
        val isTransitive = resolvedPackages.any { importText.startsWith(it) } &&
                declaredPackages.none { importText.startsWith(it) }

        if (isTransitive) {
            println("⚠️ [Lint] Transitive dependency detected: $importText")
        }

        return isTransitive
    }

    /** 🔄 Map dependency to possible package structures */
    private fun mapToPossiblePackages(dependency: String): List<String> {
        val normalized = dependency.replace(":", ".")
        val parts = normalized.split(".")
        return listOf(
            normalized,                // Full group:name
            parts.take(3).joinToString("."), // Group + artifact
            parts.getOrNull(0) ?: "",  // Root package
            parts.getOrNull(1) ?: ""   // Sub-package
        ).filter { it.isNotEmpty() }
    }



*/
}
