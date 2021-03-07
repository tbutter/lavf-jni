package com.blubb.lavf;

public class AVPacket {
	long packet_ptr;

	private AVPacket() {
	}

	public static AVPacket allocate() {
		AVPacket pkt = new AVPacket();
		pkt.packet_ptr = LAVFNative.INSTANCE.alloc_packet();
		return pkt;
	}

	public void unref() {
		LAVFNative.INSTANCE.av_packet_unref(packet_ptr);
	}

	public int getStreamIdx() {
		return (int) LAVFNative.INSTANCE.packet_getfield(packet_ptr, 1);
	}

	public long getPts() {
		return LAVFNative.INSTANCE.packet_getfield(packet_ptr, 2);
	}
}
