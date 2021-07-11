package com.blubb.lavf;

public class AVFormat implements AutoCloseable {
	long fmt_ctx;

	public AVFormat(String filename) {
		fmt_ctx = LAVFNative.INSTANCE.avformat_open_input(filename);
	}

	public int bestVideoStream() {
		return LAVFNative.INSTANCE.av_find_best_stream(fmt_ctx, 0);
	}

	public int bestAudioStream() {
		return LAVFNative.INSTANCE.av_find_best_stream(fmt_ctx, 1);
	}

	public boolean readPacket(AVPacket pkt) {
		pkt.unref();
		return LAVFNative.INSTANCE.av_read_frame(fmt_ctx, pkt.packet_ptr);
	}

	public boolean seek(long ts) {
		return LAVFNative.INSTANCE.av_seek_frame(fmt_ctx, ts);
	}


	public void close() {
		finalize();
	}

	@Override
	public void finalize() {
		if(fmt_ctx == 0) return;
		LAVFNative.INSTANCE.avformat_close_input(fmt_ctx);
		fmt_ctx = 0;
	}
}
