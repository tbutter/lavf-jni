package com.blubb.lavf;

import java.nio.ByteBuffer;

public class AVSamples {
    public ByteBuffer buffer;
    int sampleFormat;
    int channels;

    public AVSamples(int size, int sampleFormat, int channels) {
        buffer = ByteBuffer.allocateDirect(size);
    }
}
