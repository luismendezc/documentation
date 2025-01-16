package com.oceloti.lemc.custom_lint_rules

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod

class StringReturnOnceDetector : Detector(), SourceCodeScanner {
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

    companion object {
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

}