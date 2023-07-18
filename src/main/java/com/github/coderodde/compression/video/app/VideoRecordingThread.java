package com.github.coderodde.compression.video.app;

import com.github.coderodde.compression.util.BitArrayBuilder;
import com.github.coderodde.compression.util.Utils;
import com.github.coderodde.compression.util.Utils.SleepDuration;
import com.github.coderodde.compression.util.VideoScreenCanvas;
import com.github.coderodde.compression.util.VideoScreenCanvas.PixelColor;
import javafx.application.Platform;

/**
 *
 * @author rodio
 */
public final class VideoRecordingThread extends Thread {
    
    public enum VideoCompressionAlgorithm {
        NO_COMPRESSION,
        NAIVE_COMPRESSOR;
    }
    
    private final int framesToRecord;
    private final VideoScreenCanvas videoScreenCanvas;
    private final VideoCompressionAlgorithm compressionAlgorithm;
    private BitArrayBuilder bitArrayBuilder;
    
    public VideoRecordingThread(
            VideoScreenCanvas videoScreenCanvas,
            VideoCompressionAlgorithm compressionAlgorithm) {
        
        this.videoScreenCanvas = videoScreenCanvas;
        
        this.framesToRecord = VideoCompressionApp.FRAMES_PER_SECOND *
                              VideoCompressionApp.VIDEO_DURATION_SECONDS;
        
        this.compressionAlgorithm = compressionAlgorithm;
    }
    
    @Override
    public void run() {
        switch (compressionAlgorithm) {
            case NO_COMPRESSION:
                recordWithNoCompression();
                return;
                
            case NAIVE_COMPRESSOR:
                recordWithNaiveCompression();
                return;
        }
        
        throw new IllegalStateException(
                "Unknown compression algorithm: " + compressionAlgorithm);
    }
    
    public BitArrayBuilder getBitArrayBuilder() {
        return bitArrayBuilder;
    }
    
    private void recordWithNoCompression() {
        Platform.runLater(() -> {
            SleepDuration sleepDuration = 
                    Utils.getFrameSleepDuration(
                            VideoCompressionApp.FRAMES_PER_SECOND);

            int bitArrayBuilderCapacity = 
                    VideoCompressionApp.FRAMES_PER_SECOND * 
                    VideoCompressionApp.VIDEO_DURATION_SECONDS * 
                    VideoScreenCanvas.VIDEO_SCREEN_CANVAS_WIDTH *
                    VideoScreenCanvas.VIDEO_SCREEN_CANVAS_HEIGHT;

            bitArrayBuilder = new BitArrayBuilder(bitArrayBuilderCapacity);

            for (int frameIndex = 0; 
                    frameIndex < framesToRecord; 
                    frameIndex++) {
                
                Utils.sleep(sleepDuration);
                System.out.println("Frame = " + frameIndex);

                PixelColor[][] pixelMatrix = videoScreenCanvas.getPixels();

                for (PixelColor[] pixelMatrixRow : pixelMatrix) {
                    for (PixelColor pixelColor : pixelMatrixRow) {
                        bitArrayBuilder.appendBits(
                                pixelColor == PixelColor.WHITE ? 0L : 1L,
                                1);
                    }
                }
            }
        });
    }
    
    private void recordWithNaiveCompression() {
        
    }
}
