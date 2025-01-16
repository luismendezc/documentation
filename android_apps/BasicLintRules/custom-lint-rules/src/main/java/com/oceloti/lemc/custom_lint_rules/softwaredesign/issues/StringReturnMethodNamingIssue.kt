package com.oceloti.lemc.custom_lint_rules.softwaredesign.issues

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.oceloti.lemc.custom_lint_rules.softwaredesign.SoftwareDesignIssueDetector

object StringReturnMethodNamingIssue : AbstractSoftwareDesignIssue() {
    override val id = "StringReturnMethodNamingIssue"
    override val priority = 6
    override val description = "Methods returning String must end with 'Once'."
    override val explanation = """
        For consistency, methods that return String must have names ending with 'Once'.
        Example: `getUserNameOnce` instead of `getUserName`.
    """
    override val category = Category.CORRECTNESS
    override val severity = Severity.WARNING
    override val issue = Issue.create(
        id = id,
        briefDescription = description,
        explanation = explanation,
        category = category,
        priority = priority,
        severity = severity,
        implementation = Implementation(
            SoftwareDesignIssueDetector::class.java,
            Scope.JAVA_FILE_SCOPE
        )
    )
}