package com.blubb.lavf;

public class AVFormat implements AutoCloseable {
	long fmt_ctx;
	boolean isInput;

	private AVFormat(long fmt_ctx, boolean isInput) {
		this.fmt_ctx = fmt_ctx;
		this.isInput = isInput;
	}

	public static AVFormat openInput(String filename) {
		return new AVFormat(LAVFNative.INSTANCE.avformat_open_input(filename), true);
	}

	public static AVFormat allocate(String format) {
		AVFormat fmt = new AVFormat(LAVFNative.INSTANCE.avformat_allocate(format), false);
		if (fmt.fmt_ctx == 0)
			throw new IllegalArgumentException("unknown format " + format);
		return fmt;
	}

	public AVStream addVideoStream(int codecId, int width, int height, int bitrate, int frametime, int pixfmt) {
		return new AVStream(
				LAVFNative.INSTANCE.av_add_video_stream(fmt_ctx, codecId, width, height, bitrate, frametime, pixfmt));
	}

	public boolean openVideoStream(AVStream stream) {
		LAVFNative.INSTANCE.av_open_video(fmt_ctx, stream.avstream);
		return false; // FIXME
	}

	public int bestVideoStream() {
		return LAVFNative.INSTANCE.av_find_best_stream(fmt_ctx, 0);
	}

	public int bestAudioStream() {
		return LAVFNative.INSTANCE.av_find_best_stream(fmt_ctx, 1);
	}

	public double getVideoRotation(int idx) {
		return LAVFNative.INSTANCE.av_get_video_rotation(fmt_ctx, idx);
	}

	public long getDuration() {
		return LAVFNative.INSTANCE.av_stream_get_duration(fmt_ctx);
	}

	public boolean readPacket(AVPacket pkt) {
		pkt.unref();
		return LAVFNative.INSTANCE.av_read_frame(fmt_ctx, pkt.packet_ptr);
	}

	public boolean seek(long ts, boolean backwards) {
		return LAVFNative.INSTANCE.av_seek_frame(fmt_ctx, ts, backwards);
	}

	public long streamts_to_basets(long ts) {
		// converts stream ts to ts with base 1_000_000
		return LAVFNative.INSTANCE.streamts_to_basets(fmt_ctx, ts);
	}

	public void close() {
		finalize();
	}

	@Override
	public void finalize() {
		if (fmt_ctx == 0)
			return;
		if (isInput)
			LAVFNative.INSTANCE.avformat_close_input(fmt_ctx);
		fmt_ctx = 0;
	}
}
