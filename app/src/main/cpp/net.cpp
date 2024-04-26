#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_net_activity_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Z1AZQ2tKsdTPhmhshhE5xd75UbyXJ29y";
    return env->NewStringUTF(hello.c_str());
}