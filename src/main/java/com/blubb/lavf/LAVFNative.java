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

	native void avformat_close_input(long fmt_ctx);

	native int av_find_best_stream(long fmt_ctx, int type);

	native long avcodec_find_decoder(long fmt_ctx, int streamidx);

	native long avcodec_alloc_context3(long codec);

	native int avcodec_stream_to_context(long fmt_ctx, int streamidx, long codec_ctx);
	
	native int avcodec_open2(long codec_ctx, long codec);

	native String avcodec_name(long codec_ctx);

	native void avcodec_free_context(long codec_ctx);
}
