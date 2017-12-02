//
// Created by linus.yang on 2017/12/2.
//
#ifndef VIDEOTOOLS_FFTRANSCODE_H
#define VIDEOTOOLS_FFTRANSCODE_H

#include <jni.h>
#include <android/log.h>

extern "C" {
#include "libavformat/avformat.h"
};

extern "C"
JNIEXPORT void JNICALL
Java_com_video_transCode_TransCodeActivity_initFFMPEG(JNIEnv *jniEnv, jobject *obj);

extern "C"
JNIEXPORT void JNICALL
Java_com_video_transCode_TransCodeActivity_startTransCode(JNIEnv *jniEnv, jobject *obj,
                                                          jstring inputPath, jstring outPath);

char *Jstring2CStr(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("UTF-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte *ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1); //new char[alen+1];
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);

    return rtn;
}


#endif //VIDEOTOOLS_FFTRANSCODE_H

