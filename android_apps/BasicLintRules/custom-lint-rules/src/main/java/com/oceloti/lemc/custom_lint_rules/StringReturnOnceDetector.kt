package com.oceloti.lemc.custom_lint_rules

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.UElement

/**
 * Detects methods returning String that do not end with 'Once'.
 */
class StringReturnOnceDetector : Detector(), SourceCodeScanner {

    companion object {
        /**
         * Defines the lint issue for incorrect method naming.
         */
        val ISSUE: Issue = Issue.create(
            id = "StringReturnOnce",
            briefDescription = "Methods returning String should end with 'Once'",
            explanation = "All methods returning a String should end with 'Once' for consistency.",
            category = Category.CORRECTNESS,
            priority = 7,
            severity = Severity.WARNING,
            implementation = Implementation(
                StringReturnOnceDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UMethod::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitMethod(node: UMethod) {
                val returnType = node.returnType?.canonicalText ?: return
                val methodName = node.name

                if (returnType == "java.lang.String" && !methodName.endsWith("Once")) {
                    val renameFix = fix().replace()
                        .name("Rename method to end with 'Once'")
                        .sharedName("Rename all such methods to end with 'Once'")
                        .text(methodName)
                        .with("${methodName}Once")
                        .autoFix()
                        .reformat(true)
                        .build()

                    context.report(
                        ISSUE,
                        node,
                        context.getNameLocation(node),
                        "Method '$methodName' returns String but does not end with 'Once'.",
                        renameFix
                    )
                }
            }
        }
    }
}
