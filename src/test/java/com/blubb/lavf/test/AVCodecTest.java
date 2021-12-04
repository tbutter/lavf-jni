package com.blubb.lavf.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import org.junit.jupiter.api.Test;

import com.blubb.lavf.AVCodec;
import com.blubb.lavf.AVFormat;
import com.blubb.lavf.AVFrame;
import com.blubb.lavf.AVImage;
import com.blubb.lavf.AVPacket;
import com.blubb.lavf.AVStream;

public class AVCodecTest {
	public static String SHAsum(ByteBuffer convertme) throws NoSuchAlgorithmException {
		convertme.rewind();
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		Formatter formatter = new Formatter();
		md.update(convertme);
		byte[] hash = md.digest();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String ret = formatter.toString();
		formatter.close();
		return ret;
	}

	@Test
	public void testOpen() throws Exception {
		AVFormat avformat = AVFormat.openInput("build/ForBiggerFun.mp4");
		AVCodec codec = AVCodec.fromStream(avformat, 0);
		assertEquals("h264", codec.getName());
		assertEquals(1280, codec.getWidth());
		assertEquals(720, codec.getHeight());
		assertEquals(0, codec.getPixFmt());
		codec.close();
		avformat.close();
	}

	@Test
	public void testDecode() throws Exception {
		AVFormat avformat = AVFormat.openInput("build/ForBiggerFun.mp4");
		AVCodec codec = AVCodec.fromStream(avformat, 0);
		AVPacket pkt = AVPacket.allocate();
		AVImage img = AVImage.allocate(1280, 720, 0); // YUV420
		AVFrame frame = AVFrame.allocate();
		assertEquals("3eab55c6cb0fc9d92600ea15adf8633dea3b976e", SHAsum(img.buffer));
		avformat.readPacket(pkt);
		codec.sendPacket(pkt);
		int ret = codec.receiveFrame(frame);
		assertEquals(0, ret);
		frame.copyToImage(img);
		assertEquals("a368ff9a8ce6ed9ece752a6b2007d1c46e9cfd8b", SHAsum(img.buffer));
		AVImage imgrgba = AVImage.allocate(1280, 720, 26); // RGBA
		assertEquals("2b875a069ab089575279e58e85346d56d1025713", SHAsum(imgrgba.buffer));
		img.copyTo(imgrgba);
		assertEquals("bbe691c2dd42324e64caf7464ecf261ff599dab6", SHAsum(imgrgba.buffer));
		avformat.close();
	}

	@Test
	public void testEncode() throws Exception {
		AVFormat avformat = AVFormat.openInput("build/ForBiggerFun.mp4");
		AVCodec codec = AVCodec.fromStream(avformat, 0);
		AVPacket pkt = AVPacket.allocate();
		AVImage img = AVImage.allocate(1280, 720, 0); // YUV420
		AVFrame frame = AVFrame.allocate();
		// TODO assertEquals("3eab55c6cb0fc9d92600ea15adf8633dea3b976e", SHAsum(img.buffer));
		avformat.readPacket(pkt);
		codec.sendPacket(pkt);
		int ret = codec.receiveFrame(frame);
		assertEquals(0, ret);
		frame.copyToImage(img);
		AVFormat output = AVFormat.allocate("matroska");
		AVStream videostream = output.addVideoStream(AVCodec.getCodecId("MJPEG"), 1280, 720, 5_000_000, 20, 0);
		output.openVideoStream(videostream);
	}
}
