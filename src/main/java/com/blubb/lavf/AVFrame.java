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

	public long getPts() {
		return LAVFNative.INSTANCE.frame_pts(frame_ptr);
	}

	public void copyToImage(AVImage image) {
		LAVFNative.INSTANCE.copy_frame_to_image(frame_ptr, image);
	}

	public AVSamples copyToSamples() {
		return LAVFNative.INSTANCE.copy_frame_to_samples(frame_ptr);
	}

	public void unref() {
		LAVFNative.INSTANCE.av_frame_unref(frame_ptr);
	}
}
