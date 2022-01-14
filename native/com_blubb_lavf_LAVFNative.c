#include "com_blubb_lavf_LAVFNative.h"

#include <libavformat/avformat.h>
#include <libavutil/imgutils.h>
#include <libavutil/samplefmt.h>
#include <libavutil/timestamp.h>
#include <libswscale/swscale.h>

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_avformat_1open_1input(
    JNIEnv *env, jobject obj, jstring filename) {
    AVFormatContext *fmt_ctx = NULL;
    const char *path;

    path = (*env)->GetStringUTFChars(env, filename, NULL);
    if (avformat_open_input(&fmt_ctx, path, NULL, NULL) < 0) {
        fprintf(stderr, "Could not open source file %s\n", path);
        exit(1);
    }

    /* retrieve stream information */
    if (avformat_find_stream_info(fmt_ctx, NULL) < 0) {
        fprintf(stderr, "Could not find stream information\n");
        exit(1);
    }
    return (jlong)fmt_ctx;
}

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_av_1stream_1get_1duration
  (JNIEnv *env, jobject obj, jlong ctx) {
    AVFormatContext *fmt_ctx = (AVFormatContext *)ctx;
    return (jlong)fmt_ctx->duration;
}

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_avformat_1allocate(
    JNIEnv *env, jobject obj, jstring format) {
    AVFormatContext *fmt_ctx;

    const char *formatstr;
    int ret = 0;

    formatstr = (*env)->GetStringUTFChars(env, format, NULL);
    ret = avformat_alloc_output_context2(&fmt_ctx, NULL, formatstr, NULL);
    return (jlong)fmt_ctx;
}

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_streamts_1to_1basets(
    JNIEnv *env, jobject obj, jlong ctx, jlong sts) {
    AVFormatContext *fmt_ctx = (AVFormatContext *)ctx;
    AVRational time_base = fmt_ctx->streams[0]->time_base;
    return av_rescale_q(sts, time_base, AV_TIME_BASE_Q);
}

JNIEXPORT void JNICALL Java_com_blubb_lavf_LAVFNative_avformat_1close_1input(
    JNIEnv *env, jobject obj, jlong ctx) {
    AVFormatContext *fmt_ctx = (AVFormatContext *)ctx;
    avformat_close_input(&fmt_ctx);
}

JNIEXPORT jint JNICALL Java_com_blubb_lavf_LAVFNative_av_1find_1best_1stream(
    JNIEnv *env, jobject obj, jlong ctx, jint type) {
    AVFormatContext *fmt_ctx = (AVFormatContext *)ctx;
    return av_find_best_stream(fmt_ctx, type, -1, -1, NULL, 0);
}

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1find_1decoder(
    JNIEnv *env, jobject obj, jlong fmt_ctxl, jint streamidx) {
    AVFormatContext *fmt_ctx = (AVFormatContext *)fmt_ctxl;

    AVStream *st;
    AVCodec *dec = NULL;

    st = fmt_ctx->streams[streamidx];
    dec = avcodec_find_decoder(st->codecpar->codec_id);
    return (jlong)dec;
}

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1alloc_1context3(
    JNIEnv *env, jobject obj, jlong decl) {
    AVCodec *dec = (AVCodec *)decl;
    AVCodecContext *dec_ctx = NULL;

    dec_ctx = avcodec_alloc_context3(dec);
    return (jlong)dec_ctx;
}

JNIEXPORT jint JNICALL
Java_com_blubb_lavf_LAVFNative_avcodec_1stream_1to_1context(
    JNIEnv *env, jobject obj, jlong fmt_ctxl, jint streamidx, jlong codec_ctx) {
    AVStream *st;
    AVFormatContext *fmt_ctx = (AVFormatContext *)fmt_ctxl;
    AVCodecContext *dec_ctx = (AVCodecContext *)codec_ctx;

    st = fmt_ctx->streams[streamidx];
    return avcodec_parameters_to_context(dec_ctx, st->codecpar);
}

JNIEXPORT jint JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1open2(
    JNIEnv *env, jobject obj, jlong codec_ctx, jlong codec) {
    AVDictionary *opts = NULL;
    AVCodecContext *dec_ctx = (AVCodecContext *)codec_ctx;
    AVCodec *dec = (AVCodec *)codec;

    return avcodec_open2(dec_ctx, dec, &opts);
}

JNIEXPORT jstring JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1name(
    JNIEnv *env, jobject obj, jlong codec_ctx) {
    AVCodecContext *dec_ctx = (AVCodecContext *)codec_ctx;
    return (*env)->NewStringUTF(env, dec_ctx->codec->name);
}

JNIEXPORT jint JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1ctx_1intfield(
    JNIEnv *env, jobject obj, jlong codec_ctx, jint field) {
    AVCodecContext *dec_ctx = (AVCodecContext *)codec_ctx;
    switch (field) {
        case 1:
            return dec_ctx->width;
        case 2:
            return dec_ctx->height;
        case 3:
            return dec_ctx->pix_fmt;
        case 4:
            return dec_ctx->sample_fmt;
        case 5:
            return dec_ctx->channels;
        case 6:
            return dec_ctx->sample_rate;
    }
    return 0;
}

JNIEXPORT void JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1free_1context(
    JNIEnv *env, jobject obj, jlong codec_ctx) {
    AVCodecContext *dec_ctx = (AVCodecContext *)codec_ctx;

    avcodec_free_context(&dec_ctx);
}

jint getIntField(JNIEnv *env, jobject obj, const char *name) {
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID field = (*env)->GetFieldID(env, cls, name, "I");
    return (*env)->GetIntField(env, obj, field);
}

void setIntField(JNIEnv *env, jobject obj, const char *name, jint value) {
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID field = (*env)->GetFieldID(env, cls, name, "I");
    (*env)->SetIntField(env, obj, field, value);
}

JNIEXPORT void JNICALL Java_com_blubb_lavf_LAVFNative_av_1alloc_1image(
    JNIEnv *env, jobject that, jobject img) {
    jclass cls = (*env)->GetObjectClass(env, img);
    jint w = getIntField(env, img, "w");
    jint h = getIntField(env, img, "h");
    jint pix_fmt = getIntField(env, img, "pix_fmt");
    uint8_t *video_dst_data[4] = {NULL};
    int video_dst_linesize[4];
    int video_dst_bufsize;
    video_dst_bufsize =
        av_image_alloc(video_dst_data, video_dst_linesize, w, h, pix_fmt, 1);
    jobject buf =
        (*env)->NewDirectByteBuffer(env, video_dst_data[0], video_dst_bufsize);
    jfieldID bufferid =
        (*env)->GetFieldID(env, cls, "buffer", "Ljava/nio/ByteBuffer;");
    (*env)->SetObjectField(env, img, bufferid, buf);
    setIntField(env, img, "offset0", 0);
    setIntField(env, img, "offset1",
                video_dst_data[1] ? video_dst_data[1] - video_dst_data[0] : -1);
    setIntField(env, img, "offset2",
                video_dst_data[2] ? video_dst_data[2] - video_dst_data[0] : -1);
    setIntField(env, img, "offset3",
                video_dst_data[3] ? video_dst_data[3] - video_dst_data[0] : -1);
    setIntField(env, img, "linesize0", video_dst_linesize[0]);
    setIntField(env, img, "linesize1", video_dst_linesize[1]);
    setIntField(env, img, "linesize2", video_dst_linesize[2]);
    setIntField(env, img, "linesize3", video_dst_linesize[3]);
}

JNIEXPORT void JNICALL Java_com_blubb_lavf_LAVFNative_copy_1frame_1to_1image(
    JNIEnv *env, jobject obj, jlong frame_ptr, jobject img) {
    AVFrame *frame = (AVFrame *)frame_ptr;
    jclass cls = (*env)->GetObjectClass(env, img);
    jint w = getIntField(env, img, "w");
    jint h = getIntField(env, img, "h");
    jint pix_fmt = getIntField(env, img, "pix_fmt");
    jfieldID bufferid =
        (*env)->GetFieldID(env, cls, "buffer", "Ljava/nio/ByteBuffer;");
    jobject buf = (*env)->GetObjectField(env, img, bufferid);
    uint8_t *video_dst_data[4] = {NULL};
    uint8_t *bufptr = (*env)->GetDirectBufferAddress(env, buf);
    video_dst_data[0] = bufptr + getIntField(env, img, "offset0");
    video_dst_data[1] = bufptr + getIntField(env, img, "offset1");
    video_dst_data[2] = bufptr + getIntField(env, img, "offset2");
    video_dst_data[3] = bufptr + getIntField(env, img, "offset3");
    int video_dst_linesize[4];
    video_dst_linesize[0] = getIntField(env, img, "linesize0");
    video_dst_linesize[1] = getIntField(env, img, "linesize1");
    video_dst_linesize[2] = getIntField(env, img, "linesize2");
    video_dst_linesize[3] = getIntField(env, img, "linesize3");
    av_image_copy(video_dst_data, video_dst_linesize,
                  (const uint8_t **)(frame->data), frame->linesize, pix_fmt, w,
                  h);
}

JNIEXPORT jobject JNICALL
Java_com_blubb_lavf_LAVFNative_copy_1frame_1to_1samples(JNIEnv *env,
                                                        jobject obj,
                                                        jlong frame_ptr) {
    jclass samplesclass = (*env)->FindClass(env, "com/blubb/lavf/AVSamples");
    jmethodID cnstrctr = (*env)->GetMethodID(env, samplesclass, "<init>", "(III)V");
    AVFrame *frame = (AVFrame *)frame_ptr;
    jint size = 0;
    jint sample_fmt = frame->format;
    jint channels = frame->channels;

    int planar = av_sample_fmt_is_planar(sample_fmt);
    int planes = planar ? channels : 1;
    int block_align =
        av_get_bytes_per_sample(sample_fmt) * (planar ? 1 : channels);
    int planesize = channels * block_align * frame->nb_samples;
    size = planesize * planes;
    jobject ret = (*env)->NewObject(env, samplesclass, cnstrctr, size,
                                    sample_fmt, channels);
    jfieldID bufferid =
        (*env)->GetFieldID(env, samplesclass, "buffer", "Ljava/nio/ByteBuffer;");
    jobject buf = (*env)->GetObjectField(env, ret, bufferid);
    uint8_t *bufptr = (*env)->GetDirectBufferAddress(env, buf);
    for (int i = 0; i < planes; i++) {
        memcpy(bufptr + i * planesize, frame->data[i], planesize);
    }
    return ret;
}

JNIEXPORT void JNICALL Java_com_blubb_lavf_LAVFNative_copyImageTo(JNIEnv *env,
                                                                  jobject obj,
                                                                  jobject src,
                                                                  jobject dst) {
    jclass cls = (*env)->GetObjectClass(env, src);
    jint srcw = getIntField(env, src, "w");
    jint srch = getIntField(env, src, "h");
    jint srcpix_fmt = getIntField(env, src, "pix_fmt");
    jint dstw = getIntField(env, dst, "w");
    jint dsth = getIntField(env, dst, "h");
    jint dstpix_fmt = getIntField(env, dst, "pix_fmt");
    jfieldID bufferid =
        (*env)->GetFieldID(env, cls, "buffer", "Ljava/nio/ByteBuffer;");
    jobject srcbuf = (*env)->GetObjectField(env, src, bufferid);
    jobject dstbuf = (*env)->GetObjectField(env, dst, bufferid);
    uint8_t *video_src_data[4] = {NULL};
    uint8_t *bufptr = (*env)->GetDirectBufferAddress(env, srcbuf);
    jint offset = getIntField(env, src, "offset0");
    video_src_data[0] = offset >= 0 ? bufptr + offset : 0;
    offset = getIntField(env, src, "offset1");
    video_src_data[1] = offset >= 0 ? bufptr + offset : 0;
    offset = getIntField(env, src, "offset2");
    video_src_data[2] = offset >= 0 ? bufptr + offset : 0;
    offset = getIntField(env, src, "offset3");
    video_src_data[3] = offset >= 0 ? bufptr + offset : 0;
    int video_src_linesize[4];
    video_src_linesize[0] = getIntField(env, src, "linesize0");
    video_src_linesize[1] = getIntField(env, src, "linesize1");
    video_src_linesize[2] = getIntField(env, src, "linesize2");
    video_src_linesize[3] = getIntField(env, src, "linesize3");
    uint8_t *video_dst_data[4] = {NULL};
    bufptr = (*env)->GetDirectBufferAddress(env, dstbuf);
    offset = getIntField(env, dst, "offset0");
    video_dst_data[0] = offset >= 0 ? bufptr + offset : 0;
    offset = getIntField(env, dst, "offset1");
    video_dst_data[1] = offset >= 0 ? bufptr + offset : 0;
    offset = getIntField(env, dst, "offset2");
    video_dst_data[2] = offset >= 0 ? bufptr + offset : 0;
    offset = getIntField(env, dst, "offset3");
    video_dst_data[3] = offset >= 0 ? bufptr + offset : 0;
    int video_dst_linesize[4];
    video_dst_linesize[0] = getIntField(env, dst, "linesize0");
    video_dst_linesize[1] = getIntField(env, dst, "linesize1");
    video_dst_linesize[2] = getIntField(env, dst, "linesize2");
    video_dst_linesize[3] = getIntField(env, dst, "linesize3");

    struct SwsContext *convertCtx =
        sws_getContext(srcw, srch, srcpix_fmt, dstw, dsth, dstpix_fmt,
                       SWS_FAST_BILINEAR, NULL, NULL, NULL);
    sws_scale(convertCtx, video_src_data, video_src_linesize, 0, srch,
              video_dst_data, video_dst_linesize);
}

JNIEXPORT jlong JNICALL
Java_com_blubb_lavf_LAVFNative_alloc_1packet(JNIEnv *env, jobject obj) {
    AVPacket *pkt = av_mallocz(sizeof(AVPacket));
    av_init_packet(pkt);
    pkt->data = NULL;
    pkt->size = 0;
    return (jlong)pkt;
}

JNIEXPORT void JNICALL Java_com_blubb_lavf_LAVFNative_free_1packet(
    JNIEnv *env, jobject obj, jlong packet_ptr) {
    AVPacket *pkt = (AVPacket *)packet_ptr;
    av_free(pkt);
}

JNIEXPORT jboolean JNICALL Java_com_blubb_lavf_LAVFNative_av_1read_1frame(
    JNIEnv *env, jobject obj, jlong lfmt_ctx, jlong packet_ptr) {
    AVPacket *pkt = (AVPacket *)packet_ptr;
    AVFormatContext *fmt_ctx = (AVFormatContext *)lfmt_ctx;
    return av_read_frame(fmt_ctx, pkt) >= 0;
}

JNIEXPORT jboolean JNICALL Java_com_blubb_lavf_LAVFNative_av_1seek_1frame(
    JNIEnv *env, jobject obj, jlong lfmt_ctx, jlong ts) {
    AVFormatContext *fmt_ctx = (AVFormatContext *)lfmt_ctx;
    return av_seek_frame(fmt_ctx, -1, ts, AVSEEK_FLAG_BACKWARD) >= 0;
}

JNIEXPORT void JNICALL Java_com_blubb_lavf_LAVFNative_av_1packet_1unref(
    JNIEnv *env, jobject obj, jlong packet_ptr) {
    AVPacket *pkt = (AVPacket *)packet_ptr;
    av_packet_unref(pkt);
}

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_packet_1getfield(
    JNIEnv *env, jobject obj, jlong packet_ptr, jint field) {
    AVPacket *pkt = (AVPacket *)packet_ptr;
    switch (field) {
        case 1:
            return pkt->stream_index;
        case 2:
            return pkt->pts;
    }
    return -1;
}

JNIEXPORT jlong JNICALL
Java_com_blubb_lavf_LAVFNative_alloc_1frame(JNIEnv *env, jobject obj) {
    AVFrame *frame = av_frame_alloc();
    return (jlong)frame;
}

JNIEXPORT void JNICALL Java_com_blubb_lavf_LAVFNative_av_1frame_1unref(
    JNIEnv *env, jobject obj, jlong frame_ptr) {
    AVFrame *frame = (AVFrame *)frame_ptr;
    av_frame_unref(frame);
}

JNIEXPORT jboolean JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1send_1packet(
    JNIEnv *env, jobject obj, jlong codec_ptr, jlong packet_ptr) {
    AVPacket *pkt = (AVPacket *)packet_ptr;
    AVCodecContext *cctx = (AVCodecContext *)codec_ptr;
    return avcodec_send_packet(cctx, pkt) >= 0;
}

JNIEXPORT jint JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1receive_1frame(
    JNIEnv *env, jobject obj, jlong codec_ptr, jlong frame_ptr) {
    AVFrame *frame = (AVFrame *)frame_ptr;
    AVCodecContext *cctx = (AVCodecContext *)codec_ptr;
    return avcodec_receive_frame(cctx, frame);
}

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_av_1add_1video_1stream(
    JNIEnv *env, jobject obj, jlong fmt_ctx, jint codecId, jint width,
    jint height, jint bitrate, jint frametime, jint pixfmt) {
    AVFormatContext *oc = (AVFormatContext *)fmt_ctx;
    AVCodecContext *c;
    AVStream *st;

    st = avformat_new_stream(oc, 0);
    if (!st) {
        fprintf(stderr, "Could not alloc stream\n");
        return 0;
    }

    c = st->codec;
    c->codec_id = codecId;
    c->codec_type = AVMEDIA_TYPE_VIDEO;

    c->bit_rate = bitrate;
    c->width = width;
    c->height = height;

    c->time_base.den = AV_TIME_BASE;
    c->time_base.num = frametime;
    c->gop_size = 12; /* emit one intra frame every twelve frames at most */
    c->pix_fmt = pixfmt;
    /*if (oc->oformat->flags & AVFMT_GLOBALHEADER)
                    c->flags |= CODEC_FLAG_GLOBAL_HEADER;*/

    return (jlong)st;
}

JNIEXPORT jint JNICALL Java_com_blubb_lavf_LAVFNative_av_1open_1video(
    JNIEnv *env, jobject obj, jlong fmt_ctx, jlong avstream) {
    int ret;
    AVStream *st = (AVStream *)avstream;
    AVFormatContext *fmt = (AVFormatContext *)fmt_ctx;
    AVCodecContext *c = st->codec;
    printf("XXXXXXXCCCXXXXXX3 %x", c);

    AVDictionary *opt = NULL;
    AVDictionary *opt_arg = NULL;
    av_dict_copy(&opt, opt_arg, 0);
    printf("av_dcit_copy");
    /* open the codec */

    ret = 0;  // avcodec_open2(c, c->codec, &opt);
    printf("avcodec_open2");

    av_dict_free(&opt);
    if (ret < 0) {
        fprintf(stderr, "Could not open video codec: %s\n", av_err2str(ret));
        exit(1);
    }
    /* copy the stream parameters to the muxer */
    ret = avcodec_parameters_from_context(st->codecpar, c);
    if (ret < 0) {
        fprintf(stderr, "Could not copy the stream parameters\n");
        exit(1);
    }
}