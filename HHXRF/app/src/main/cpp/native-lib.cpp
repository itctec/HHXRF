#include <jni.h>
#include <string>
#include<cstdlib>
#include<cstdio>
#include<cstring>
#include<iostream>
#include<algorithm>
using namespace std;

const int N = 500;

extern "C"
JNIEXPORT jstring JNICALL
Java_itc_ink_hhxrf_hardware_HardwareControl_stringFromJNI(JNIEnv *env, jclass clazz) {
    // TODO
    std::string hello = "C++ Interface";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_itc_ink_hhxrf_hardware_HardwareControl_nativeRoutineAnalysis(JNIEnv *env, jclass clazz) {
    // TODO: implement jniFunctionTest()
    std::string hello = "From Native Function";

    char line[N];
    FILE *fp;
    string cmd = "cd /data/XRS && export LD_LIBRARY_PATH=/data/XRS && ./FPSPDllTest xx_XrsCmdFileOptions.txt";
    // system call
    const char *sysCommand = cmd.data();
    if ((fp = popen(sysCommand, "r")) == NULL) {
        hello = "error";
    }

    while (fgets(line, sizeof(line)-1, fp) != NULL){
        cout << line << endl;
        hello.append(line);
    }
    pclose(fp);

    return env->NewStringUTF(hello.c_str());
}