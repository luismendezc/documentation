package com.oceloti.lemc.custom_lint_rules.softwaredesign

import org.jetbrains.uast.UMethod

class StringReturnMethodNamingChecker {
    /**
     * Checks if a method returns String but does not end with 'Once'.
     */
    internal fun violatesNamingConvention(node: UMethod): Boolean {
        val returnType = node.returnType?.canonicalText
        val methodName = node.name

        // Check if method returns String and doesn't end with 'Once'
        return returnType == "java.lang.String" && !methodName.endsWith("Once")
    }
}