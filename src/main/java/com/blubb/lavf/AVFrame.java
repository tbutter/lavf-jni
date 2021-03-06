package com.blubb.lavf;

public class AVFrame {
	long frame_ptr;

	private AVFrame() {
	}

	public static AVFrame allocate() {
		AVFrame frame = new AVFrame();
		frame.frame_ptr = LAVFNative.INSTANCE.alloc_frame();
		return frame;
	}

	public void copyToImage(AVImage image) {
		LAVFNative.INSTANCE.copy_frame_to_image(frame_ptr, image);
	}

	public void unref() {
		LAVFNative.INSTANCE.av_frame_unref(frame_ptr);
	}
}
