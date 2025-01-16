package com.oceloti.lemc.custom_lint_rules.softwaredesign

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.JavaContext
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod

class SoftwareDesignIssueDetector : Detector(), Detector.UastScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UMethod::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler =
        SoftwareDesignRuleVisitor(context)
}