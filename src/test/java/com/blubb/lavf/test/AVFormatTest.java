package com.blubb.lavf.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.blubb.lavf.AVFormat;
import com.blubb.lavf.AVPacket;

public class AVFormatTest {
	@Test
	public void testOpen() throws Exception {
		AVFormat avformat = AVFormat.openInput("build/ForBiggerFun.mp4");
		assertEquals(1, avformat.bestAudioStream());
		assertEquals(0, avformat.bestVideoStream());
		avformat.close();
	}

	@Test
	public void testPackets() throws Exception {
		AVFormat avformat = AVFormat.openInput("build/ForBiggerFun.mp4");
		AVPacket p = AVPacket.allocate();
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(0, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(2002, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(4004, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(6006, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(8008, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(10010, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(12012, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(14014, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(16016, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(18018, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(20020, p.getPts());
		avformat.readPacket(p);
		assertEquals(0, p.getStreamIdx());
		assertEquals(22022, p.getPts());
		avformat.readPacket(p);
		assertEquals(1, p.getStreamIdx());
		assertEquals(0, p.getPts());
		int audiopackets = 0;
		for (int i = 0; i < 100; i++) {
			avformat.readPacket(p);
			if (p.getStreamIdx() == 1)
				audiopackets++;
		}
		assertEquals(67, audiopackets);
		avformat.close();
	}
}
