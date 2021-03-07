package com.blubb.lavf;

public class AVCodec implements AutoCloseable {
	private long codec_ctx;
	private long codec;

	public AVCodec(long codec) {
		this.codec = codec;
		codec_ctx = LAVFNative.INSTANCE.avcodec_alloc_context3(codec);
	}

	public void setCodecParameters(AVFormat avf, int streamidx) {
		if(LAVFNative.INSTANCE.avcodec_stream_to_context(avf.fmt_ctx, streamidx, codec_ctx) > 0)
			throw new RuntimeException("could not call stream to context");
	}

	public void open() {
		if (LAVFNative.INSTANCE.avcodec_open2(codec_ctx, codec) < 0)
			throw new RuntimeException("error opening codec");
	}

	public static AVCodec fromStream(AVFormat avf, int streamidx) {
		long codec = LAVFNative.INSTANCE.avcodec_find_decoder(avf.fmt_ctx, streamidx);
		if (codec == 0)
			throw new RuntimeException("did not find decoder");
		AVCodec avcodec = new AVCodec(codec);
		avcodec.setCodecParameters(avf, streamidx);
		avcodec.open();
		return avcodec;
	}

	public String getName() {
		return LAVFNative.INSTANCE.avcodec_name(codec_ctx);
	}

	public int getWidth() {
		return LAVFNative.INSTANCE.avcodec_ctx_intfield(codec_ctx, 1);
	}

	public int getHeight() {
		return LAVFNative.INSTANCE.avcodec_ctx_intfield(codec_ctx, 2);
	}

	public int getPixFmt() {
		return LAVFNative.INSTANCE.avcodec_ctx_intfield(codec_ctx, 3);
	}

	public int getSampleFmt() {
		return LAVFNative.INSTANCE.avcodec_ctx_intfield(codec_ctx, 4);
	}

	public int getChannels() {
		return LAVFNative.INSTANCE.avcodec_ctx_intfield(codec_ctx, 5);
	}

	public boolean sendPacket(AVPacket p) {
		return LAVFNative.INSTANCE.avcodec_send_packet(codec_ctx, p.packet_ptr);
	}

	public int receiveFrame(AVFrame frame) {
		frame.unref();
		return LAVFNative.INSTANCE.avcodec_receive_frame(codec_ctx, frame.frame_ptr);
	}

	public void close() {
		finalize();
	}

	@Override
	public void finalize() {
		if (codec_ctx == 0)
			return;
		LAVFNative.INSTANCE.avcodec_free_context(codec_ctx);
		codec_ctx = 0;
	}
}
