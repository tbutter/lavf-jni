#include "com_blubb_lavf_LAVFNative.h"
#include <libavutil/imgutils.h>
#include <libavutil/samplefmt.h>
#include <libavutil/timestamp.h>
#include <libavformat/avformat.h>

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_avformat_1open_1input(JNIEnv *env, jobject obj, jstring filename)
{
	AVFormatContext *fmt_ctx = NULL;
	const char *path;

	path = (*env)->GetStringUTFChars(env, filename, NULL);
	if (avformat_open_input(&fmt_ctx, path, NULL, NULL) < 0)
	{
		fprintf(stderr, "Could not open source file %s\n", path);
		exit(1);
	}

	/* retrieve stream information */
	if (avformat_find_stream_info(fmt_ctx, NULL) < 0)
	{
		fprintf(stderr, "Could not find stream information\n");
		exit(1);
	}
	return (jlong)fmt_ctx;
}

JNIEXPORT void JNICALL Java_com_blubb_lavf_LAVFNative_avformat_1close_1input(JNIEnv *env, jobject obj, jlong ctx)
{
	AVFormatContext *fmt_ctx = (AVFormatContext *)ctx;
	avformat_close_input(&fmt_ctx);
}

JNIEXPORT jint JNICALL Java_com_blubb_lavf_LAVFNative_av_1find_1best_1stream(JNIEnv *env, jobject obj, jlong ctx, jint type)
{
	AVFormatContext *fmt_ctx = (AVFormatContext *)ctx;
	return av_find_best_stream(fmt_ctx, type, -1, -1, NULL, 0);
}

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1find_1decoder(JNIEnv *env, jobject obj, jlong fmt_ctxl, jint streamidx)
{
	AVFormatContext *fmt_ctx = (AVFormatContext *)fmt_ctxl;

	AVStream *st;
	AVCodec *dec = NULL;

	st = fmt_ctx->streams[streamidx];
	dec = avcodec_find_decoder(st->codecpar->codec_id);
	return (jlong)dec;
}

JNIEXPORT jlong JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1alloc_1context3(JNIEnv *env, jobject obj, jlong decl)
{
	AVCodec *dec = (AVCodec *)decl;
	AVCodecContext *dec_ctx = NULL;

	dec_ctx = avcodec_alloc_context3(dec);
	return (jlong)dec_ctx;
}

JNIEXPORT jint JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1stream_1to_1context(JNIEnv *env, jobject obj, jlong fmt_ctxl, jint streamidx, jlong codec_ctx)
{
	AVStream *st;
	AVFormatContext *fmt_ctx = (AVFormatContext *)fmt_ctxl;
	AVCodecContext *dec_ctx = (AVCodecContext *)codec_ctx;

	st = fmt_ctx->streams[streamidx];
	return avcodec_parameters_to_context(dec_ctx, st->codecpar);
}

JNIEXPORT jint JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1open2(JNIEnv *env, jobject obj, jlong codec_ctx, jlong codec)
{
	AVDictionary *opts = NULL;
	AVCodecContext *dec_ctx = (AVCodecContext *)codec_ctx;
	AVCodec *dec = (AVCodec *)codec;

	return avcodec_open2(dec_ctx, dec, &opts);
}

JNIEXPORT jstring JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1name(JNIEnv *env, jobject obj, jlong codec_ctx) {
	AVCodecContext *dec_ctx = (AVCodecContext *)codec_ctx;
	return (*env)->NewStringUTF(env, dec_ctx->codec->name);
}

JNIEXPORT void JNICALL Java_com_blubb_lavf_LAVFNative_avcodec_1free_1context(JNIEnv *env, jobject obj, jlong codec_ctx)
{
	AVCodecContext *dec_ctx = (AVCodecContext *)codec_ctx;

	avcodec_free_context(&dec_ctx);
}