package com.github.coderodde.compression.video.app;

import com.github.coderodde.compression.util.BitArrayBuilder;
import com.github.coderodde.compression.util.CircleVideoShape;
import static com.github.coderodde.compression.video.app.VideoRecordingThread.VideoCompressionAlgorithm.*;

/**
 *
 * @author rodio
 */
public final class VideoPlaybackThread extends Thread {
    
    private final CircleVideoShape circleVideoShape;
    private final VideoRecordingThread.VideoCompressionAlgorithm algorithm;
    private final BitArrayBuilder bitArrayBuilder;
    
    public VideoPlaybackThread(
            CircleVideoShape circleVideoShape,
            VideoRecordingThread.VideoCompressionAlgorithm algorithm,
            BitArrayBuilder bitArrayBuilder) {
        this.circleVideoShape = circleVideoShape;
        this.algorithm = algorithm;
        this.bitArrayBuilder = bitArrayBuilder;
    }
    
    @Override
    public void run() {
        switch (algorithm) {
            case NO_COMPRESSION:
                playbackWithNoCompression();
                return;
                
            case NAIVE_COMPRESSOR:
                
                return;
                
            default:
                throw new IllegalStateException(
                        "Unknown playback algorithm: " + algorithm);
        }
    }
    
    private void playbackWithNoCompression() {
        long totalSleepNanos = 
                1_000_000_000L / VideoCompressionApp.FRAMES_PER_SECOND;
        
        long sleepSeconds = totalSleepNanos / 1_000_000L;
        int sleepNanosInt = (int)(totalSleepNanos - sleepSeconds);
    }
}
