#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_bazel_NativeGreeter_getNativeInfo(JNIEnv* env, jobject thiz) {
    return (*env)->NewStringUTF(env, "From C: native ABI is running 😎");
}