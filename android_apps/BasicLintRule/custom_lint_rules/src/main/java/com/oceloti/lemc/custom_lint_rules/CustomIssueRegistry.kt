package com.oceloti.lemc.custom_lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

@Suppress("unused")
class CustomIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(StringReturnOnceDetector.ISSUE)

    override val api: Int = CURRENT_API

    override val vendor: Vendor = Vendor(
        vendorName = "Luis Lint Rules",
        feedbackUrl = "https://google.com",
        contact = "luis@oceloti.com"
    )

}