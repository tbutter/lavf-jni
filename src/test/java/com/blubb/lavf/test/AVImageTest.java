package com.blubb.lavf.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.blubb.lavf.AVImage;

public class AVImageTest {
	@Test
	public void testAllocate() throws Exception {
		AVImage img = AVImage.allocate(256, 256, 0);
		assertEquals(0, img.offset0);
		assertEquals(65536, img.offset1);
		assertEquals(81920, img.offset2);
		assertEquals(0, img.offset3);
		assertEquals(256, img.linesize0);
		assertEquals(128, img.linesize1);
		assertEquals(128, img.linesize2);
		assertEquals(0, img.linesize3);
		assertEquals(256*256*3/2, img.buffer.capacity());
	}
}
