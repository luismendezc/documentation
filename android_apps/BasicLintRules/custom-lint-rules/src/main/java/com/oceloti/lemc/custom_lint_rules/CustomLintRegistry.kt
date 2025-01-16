package com.oceloti.lemc.custom_lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.oceloti.lemc.custom_lint_rules.softwaredesign.issues.StringReturnMethodNamingIssue


@Suppress("unused")
class CustomLintRegistry : IssueRegistry() {
    override val issues = listOf(
        StringReturnOnceDetector.ISSUE
    )

    override val api: Int = CURRENT_API
    override val minApi: Int = 14


    // ✅ Add this vendor information
    override val vendor = Vendor(
        vendorName = "Oceloti",
        feedbackUrl = "https://github.com/oceloti/issues",
        contact = "support@oceloti.com"
    )
}
