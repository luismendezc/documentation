package com.oceloti.lemc.custom_lint_rules.softwaredesign

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.oceloti.lemc.custom_lint_rules.softwaredesign.issues.StringReturnMethodNamingIssue
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod

class SoftwareDesignRuleVisitor(private val context: JavaContext): UElementHandler() {
    private val stringMethodChecker = StringReturnMethodNamingChecker()

    override fun visitMethod(node: UMethod) {
        if (stringMethodChecker.violatesNamingConvention(node)) {
            val msg = StringReturnMethodNamingIssue.explanation
            reportIssue(StringReturnMethodNamingIssue.issue, node, msg)
        }
    }

    private fun reportIssue(issue: Issue, node: UElement, message: String) {
        context.report(
            issue = issue,
            location = context.getNameLocation(node),
            message = message,
        )
    }

}