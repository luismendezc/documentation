package com.oceloti.lemc.custom_lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.CURRENT_API

class CustomIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(StringReturnOnceDetector.ISSUE)

    override val api: Int = CURRENT_API

    // ✅ Vendor information added
    override val vendor: Vendor = Vendor(
        vendorName = "Oceloti Lint Rules",
        feedbackUrl = "https://github.com/oceloti/issues",
        contact = "support@oceloti.com"
    )
}
