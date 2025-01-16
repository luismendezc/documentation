package com.oceloti.lemc.custom_lint_rules

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test


class StringReturnOnceDetectorTest {

    @Test
    fun testMethodWithoutOnce() {
        lint().files(
            kotlin(
                """
                package test.pkg

                class TestClass {
                    fun fetchData(): String {
                        return "data"
                    }
                }
                """
            ).indented()
        )
            .issues(StringReturnOnceDetector.ISSUE)
            .run()
            .expect("""
                src/test/pkg/TestClass.kt:4: Warning: Method 'fetchData' returns String but does not end with 'Once'. [StringReturnOnce]
                    fun fetchData(): String {
                        ~~~~~~~~~
                0 errors, 1 warnings
            """
            )
            .expectFixDiffs("""
                Autofix for src/test/pkg/TestClass.kt line 4: Rename method to end with 'Once':
                @@ -4 +4
                -     fun fetchData(): String {
                +     fun fetchDataOnce(): String {
            """)
    }

    @Test
    fun testMethodWithOnce() {
        lint().files(
            kotlin(
                """
                package test.pkg

                class TestClass {
                    fun fetchDataOnce(): String {
                        return "data"
                    }
                }
                """
            ).indented()
        )
            .issues(StringReturnOnceDetector.ISSUE)
            .run()
            .expectClean()
    }
}