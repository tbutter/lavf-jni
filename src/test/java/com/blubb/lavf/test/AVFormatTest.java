package com.blubb.lavf.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blubb.lavf.AVFormat;

public class AVFormatTest {
	@Test
	public void testOpen() throws Exception {
		AVFormat avformat = new AVFormat("build/ForBiggerFun.mp4");
		assertEquals(1, avformat.bestAudioStream());
		assertEquals(0, avformat.bestVideoStream());
		avformat.close();
	}
}
