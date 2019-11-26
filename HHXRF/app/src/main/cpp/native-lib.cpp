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
    std::string resultStr = "From Native Routine Analysis Function";

    char line[N];
    FILE *fp;
    string cmd = "cd /data/XRS && export LD_LIBRARY_PATH=/data/XRS && ./FPSPDllTest XrsCmdFileOptions_RoutineAnalysis.txt";
    // system call
    const char *sysCommand = cmd.data();
    if ((fp = popen(sysCommand, "r")) == NULL) {
        resultStr = "error";
    }

    while (fgets(line, sizeof(line)-1, fp) != NULL){
        cout << line << endl;
        resultStr.append(line);
    }
    pclose(fp);

    return env->NewStringUTF(resultStr.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_itc_ink_hhxrf_hardware_HardwareControl_nativeEnergyCalibration(JNIEnv *env, jclass clazz) {
    // TODO: implement nativeEnergyCalibration()
    std::string resultStr = "From Native Energy Calibration Function";

    char line[N];
    FILE *fp;
    string cmd = "cd /data/XRS && export LD_LIBRARY_PATH=/data/XRS && ./FPSPDllTest XrsCmdFileOptions_EnergyCalibration.txt";
    // system call
    const char *sysCommand = cmd.data();
    if ((fp = popen(sysCommand, "r")) == NULL) {
        resultStr = "error";
    }

    while (fgets(line, sizeof(line)-1, fp) != NULL){
        cout << line << endl;
        resultStr.append(line);
    }
    pclose(fp);

    return env->NewStringUTF(resultStr.c_str());
}