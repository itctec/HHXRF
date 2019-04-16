#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_itc_ink_hhxrf_hardware_HardwareControl_stringFromJNI(JNIEnv *env, jobject instance) {
    // TODO
    std::string hello = "C++ Interface";
    return env->NewStringUTF(hello.c_str());
}