//
// Created by linus.yang on 2017/12/2.
//

#include "FFTransCode.h"

extern "C"
void Java_com_video_transCode_TransCodeActivity_initFFMPEG(JNIEnv * jniEnv, jobject * obj) {
    av_register_all();
}

extern "C"
void Java_com_video_transCode_TransCodeActivity_startTransCode(JNIEnv * env, jobject * obj, jstring inputPath, jstring outPath) {
    //const char * iPath = env.GetStringUTFChars(inputPath, JNI_FALSE);
   char * inChar = Jstring2CStr(env, inputPath);
   char * ouChar = Jstring2CStr(env, outPath);
   //1 Open input file
    AVFormatContext * ic = NULL;
    avformat_open_input(&ic, inChar, JNI_FALSE, 0);
    if(!ic) {
        __android_log_print(ANDROID_LOG_DEBUG, "linus" ,"avformat_open_input failed!");
    }

    //2 create output context
    AVFormatContext * oc = NULL;
    avformat_alloc_output_context2(&oc, NULL, NULL, ouChar);
    if(!oc) {
        __android_log_print(ANDROID_LOG_DEBUG, "linus", "avformat_alloc_output_context2 filed");
    }
    __android_log_print(ANDROID_LOG_DEBUG, "linus", " sss %d", oc);

}

