# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ------------------
# -----------------------------------------------
# 1) Deal with 'StringConcatFactory' warnings:
# (But prefer using coreLibraryDesugaring over this)
-dontwarn java.lang.invoke.StringConcatFactory
-keep class java.lang.invoke.StringConcatFactory { *; }

# -----------------------------------------------
# 2) Keep your public interface so the host can still use it
-keep interface com.oceloti.lemc.designlemc.LemcSDK {
    *;
}

-keep public class com.oceloti.lemc.designlemc.LemcSDK$AuthParams { *; }
-keep public class com.oceloti.lemc.designlemc.LemcSDK$Config { *; }
-keep enum com.oceloti.lemc.designlemc.LemcSDK$Environment { *; }
-keep public interface com.oceloti.lemc.designlemc.LemcMessages { *; }
-keep public interface com.oceloti.lemc.designlemc.LemcMessageObservable { *; }
-keep public interface com.oceloti.lemc.designlemc.LemcMessageSubscriber { *; }
-keep public interface com.oceloti.lemc.designlemc.LemcDisposable { *; }


# Keep the entire LemcSDK$Companion class:
-keep class com.oceloti.lemc.designlemc.LemcSDK$Companion {
    <methods>;
    <fields>;
}
