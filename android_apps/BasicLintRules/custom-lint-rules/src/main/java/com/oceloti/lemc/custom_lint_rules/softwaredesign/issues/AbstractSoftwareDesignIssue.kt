package com.oceloti.lemc.custom_lint_rules.softwaredesign.issues

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.Severity

abstract class AbstractSoftwareDesignIssue {
    /**
     * The fixed id of the issue
     */
    protected abstract val id: String

    /**
     * The priority, a number from 1 to 10 with 10 being most important/severe
     */
    protected abstract val priority: Int

    /**
     * Description short summary (typically 5-6 words or less), typically describing
     * the problem rather than the fix (e.g. "Missing minSdkVersion")
     */
    protected abstract val description: String

    /**
     * A full explanation of the issue, with suggestions for how to fix it
     */
    internal abstract val explanation: String

    /**
     * The associated category, if any @see [Category]
     */
    protected abstract val category: Category

    /**
     * The default severity of the issue
     */
    protected abstract val severity: Severity

    /**
     * The issue to be used in the [CustomLintRegistry]
     */
    internal abstract val issue: Issue
}
