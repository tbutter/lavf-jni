package com.blubb.lavf;

import java.nio.ByteBuffer;

public class AVImage {
	public ByteBuffer buffer;
	public int offset0;
	public int offset1;
	public int offset2;
	public int offset3;
	public int linesize0;
	public int linesize1;
	public int linesize2;
	public int linesize3;
	public int w;
	public int h;
	public int pix_fmt;
	private boolean disposed = false;

	private AVImage() {
	}

	public static AVImage allocate(int w, int h, int pix_fmt) {
		AVImage img = new AVImage();
		img.w = w;
		img.h = h;
		img.pix_fmt = pix_fmt;
		LAVFNative.INSTANCE.av_alloc_image(img);
		if (img.buffer == null) {
			throw new RuntimeException("could not allocate image");
		}
		return img;
	}

	public void copyTo(AVImage imgto) {
		LAVFNative.INSTANCE.copyImageTo(this, imgto);
	}

	public void dispose() {
		finalize();
	}

	public void finalize() {
		if(disposed) return;
		disposed = true;
		LAVFNative.INSTANCE.av_free_image(buffer);
	}
}
