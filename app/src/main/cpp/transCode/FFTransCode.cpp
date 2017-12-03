//
// Created by linus.yang on 2017/12/2.
//

#include "FFTransCode.h"

extern "C"
void Java_com_video_transCode_TransCodeActivity_initFFMPEG(JNIEnv *jniEnv, jobject *obj) {
    av_register_all();
}

extern "C"
void Java_com_video_transCode_TransCodeActivity_startTransCode(JNIEnv *env, jobject *obj,
                                                               jstring inputPath, jstring outPath) {
    //const char * iPath = env.GetStringUTFChars(inputPath, JNI_FALSE);
    char *inChar = Jstring2CStr(env, inputPath);
    char *ouChar = Jstring2CStr(env, outPath);
    //1 Open input file
    AVFormatContext *ic = NULL;
    avformat_open_input(&ic, inChar, JNI_FALSE, 0);
    if (!ic) {
        __android_log_print(ANDROID_LOG_DEBUG, "linus", "avformat_open_input failed!");
    }

    AVInputFormat *inp = ic->iformat;
    __android_log_print(ANDROID_LOG_DEBUG, "linus",
                        "input name: %s  longname : %s", inp->name,
                        inp->long_name);


    //2 create output context
    AVFormatContext *oc = NULL;
    avformat_alloc_output_context2(&oc, NULL, NULL, ouChar);
    if (!oc) {
        __android_log_print(ANDROID_LOG_DEBUG, "linus", "avformat_alloc_output_context2 filed");
    }
    AVOutputFormat *oup = oc->oformat;
    __android_log_print(ANDROID_LOG_DEBUG, "linus",
                        "out name: %s  longname : %s", oup->name,
                        oup->long_name);

    //3 add the stream
    AVStream * videoStream = avformat_new_stream(oc, NULL);
    AVStream * audioStream = avformat_new_stream(oc, NULL);

    //4 copy parameters_copy
    for(int i = 0 ; i < ic->nb_streams; i++) {
        AVStream * curStream = ic->streams[i];
        if(curStream->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            avcodec_parameters_copy(videoStream->codecpar, curStream->codecpar);
        } else if(curStream->codecpar->codec_type == AVMEDIA_TYPE_AUDIO ) {
            avcodec_parameters_copy(audioStream->codecpar, curStream->codecpar);
        }
    }

    videoStream->codecpar->codec_tag = 0;
    audioStream->codecpar->codec_tag = 0;

    //5 open out file io
    int ret = avio_open(&oc->pb, ouChar, AVIO_FLAG_WRITE);
    if(ret < 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "linus" ,"avio ope filed");
    }

    ret = avformat_write_header(oc, NULL);
    if(ret < 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "linus", "avformat_write_header failed!");
    }

    AVPacket pkt;
    for(;;) {
        int re = av_read_frame(ic, &pkt);
        if(re < 0) break;
        pkt.pts = av_rescale_q_rnd(pkt.pts,
                                   ic->streams[pkt.stream_index]->time_base,
                                   oc->streams[pkt.stream_index]->time_base,
                                   (AVRounding)(AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX)
        );
        pkt.dts = av_rescale_q_rnd(pkt.dts,
                                   ic->streams[pkt.stream_index]->time_base,
                                   oc->streams[pkt.stream_index]->time_base,
                                   (AVRounding)(AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX)
        );
        pkt.pos = -1;
        pkt.duration = av_rescale_q_rnd(pkt.duration,
                                        ic->streams[pkt.stream_index]->time_base,
                                        oc->streams[pkt.stream_index]->time_base,
                                        (AVRounding)(AV_ROUND_NEAR_INF | AV_ROUND_PASS_MINMAX)
        );
        av_write_frame(oc, &pkt);
        av_packet_unref(&pkt);
    }

    av_write_trailer(oc);
    avio_close(oc->pb);

}

