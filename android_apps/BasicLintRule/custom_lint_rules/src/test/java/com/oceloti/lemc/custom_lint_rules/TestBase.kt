package com.oceloti.lemc.custom_lint_rules

import org.junit.Before
import java.io.File

abstract class TestBase {
    lateinit var androidSdk: File

    @Before
    fun setup() {
        val androidHomePath = System.getenv("ANDROID_HOME")
        val androidSdkPath = System.getenv("ANDROID_SDK")
        androidSdk =
            if (androidHomePath != null) {
                File(androidHomePath)
            } else {
                File(androidSdkPath)
            }
    }
}