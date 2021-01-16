package com.blubb.lavf.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.blubb.lavf.AVCodec;
import com.blubb.lavf.AVFormat;

public class AVCodecTest {
	@Test
	public void testOpen() throws Exception {
		AVFormat avformat = new AVFormat("build/ForBiggerFun.mp4");
		AVCodec codec = AVCodec.fromStream(avformat, 0);
		assertEquals("h264", codec.getName());
		codec.close();
		avformat.close();
	}
}
