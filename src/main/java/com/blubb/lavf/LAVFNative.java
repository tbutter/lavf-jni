package com.blubb.lavf;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class LAVFNative {
	static {
		try {
			File tmpdir = Files.createTempDirectory("lavfjni").toFile();
			File tmpfile = new File(tmpdir, "liblavf.so");
			try (InputStream is = LAVFNative.class.getResourceAsStream("/liblavf.so")) {
				Files.copy(is, tmpfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			System.load(tmpfile.getAbsolutePath());
			tmpfile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final LAVFNative INSTANCE = new LAVFNative();

	private LAVFNative() {
	}

	// return pointer to fmt_ctx
	// calls avformat_open_input and avformat_fin_stream_info
	native long avformat_open_input(String filename);

	native long avformat_allocate(String format);

	native void avformat_close_input(long fmt_ctx);

	native int av_find_best_stream(long fmt_ctx, int type);

	native long avcodec_find_decoder(long fmt_ctx, int streamidx);

	native long avcodec_alloc_context3(long codec);

	native int avcodec_stream_to_context(long fmt_ctx, int streamidx, long codec_ctx);
	
	native int avcodec_open2(long codec_ctx, long codec);

	native int avcodec_ctx_intfield(long codec_ctx, int field);

	native String avcodec_name(long codec_ctx);

	native void avcodec_free_context(long codec_ctx);

	native void av_alloc_image(AVImage image);

	native long alloc_packet();

	native void free_packet(long packet_ptr);

	native boolean av_read_frame(long fmt_ctx, long packet_ptr);

	native boolean av_seek_frame(long fmt_ctx, long ts);

	native void av_packet_unref(long packet_ptr);

	native long packet_getfield(long packet_ptr, int i);

	native long alloc_frame();

	native void av_frame_unref(long frame_ptr);

	native boolean avcodec_send_packet(long codec_ctx, long packet_ptr);

	native int avcodec_receive_frame(long codec_ctx, long frame_ptr);

	native void copy_frame_to_image(long frame_ptr, AVImage image);

	native void copyImageTo(AVImage avImage, AVImage imgto);

	native long streamts_to_basets(long fmt_ctx, long ts);

	native long av_add_video_stream(long fmt_ctx, int codecId, int width, int height, int bitrate, int frametime, int pixfmt);

    native int av_open_video(long fmt_ctx, long stream);
}
