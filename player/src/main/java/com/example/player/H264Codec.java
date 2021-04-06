package com.example.player;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.view.Surface;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class H264Codec implements Runnable {
    private MediaCodec mediaCodec;
    private Surface surface;
    private byte[] data;

    public H264Codec(Surface surface, byte[] data) {
        this.surface = surface;
        this.data = data;
        try {
            mediaCodec = MediaCodec.createDecoderByType("video/avc");
            MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", 368, 384);
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
            mediaCodec.configure(mediaFormat, surface, null, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        ByteBuffer[] byteBuffers = mediaCodec.getInputBuffers();
        int startIndex = 0;
        int nextFrameStart;
        int totalSize = data.length;
        while (true) {
            if (startIndex > totalSize) {
                break;
            }
            nextFrameStart = findByFrame(data, startIndex);
            int index = mediaCodec.dequeueInputBuffer(10000);
            if (index > 0) {
                ByteBuffer byteBuffer = byteBuffers[index];
                byteBuffer.clear();
                byteBuffer.put(data, startIndex, nextFrameStart - startIndex);
                //通知dsp新藕片解码
                mediaCodec.queueInputBuffer(index, 0, nextFrameStart - startIndex, 0, 0);
                startIndex = nextFrameStart;
            } else {
                continue;
            }
            //解码后取出数据
//            mediaCodec.dequeueOutputBuffer()
        }

    }

    public void start() {
        mediaCodec.start();
        new Thread(this).start();
    }

    //找到一帧的起点
    private int findByFrame(byte[] data, int start) {
        int total = data.length;
        for (int i = start; i < total; i++) {
            if (data[i] == 0x00 && data[i + 1] == 0x00 && data[i + 2] == 0x00 && data[i + 3] == 0x001) {
                return i;
            }
        }
        return -1;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
