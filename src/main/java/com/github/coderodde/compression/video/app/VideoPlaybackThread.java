package com.github.coderodde.compression.video.app;

import com.github.coderodde.compression.util.BitArrayBuilder;
import com.github.coderodde.compression.util.Utils;
import com.github.coderodde.compression.util.VideoScreenCanvas;
import static com.github.coderodde.compression.video.app.VideoRecordingThread.VideoCompressionAlgorithm.*;
import javafx.scene.paint.Color;

/**
 *
 * @author rodio
 */
public final class VideoPlaybackThread extends Thread {
    
    private final VideoScreenCanvas videoScreenCanvas;
    private final VideoRecordingThread.VideoCompressionAlgorithm algorithm;
    private final BitArrayBuilder bitArrayBuilder;
    private final Color[][] framePixels = 
            new Color[VideoScreenCanvas.VIDEO_SCREEN_CANVAS_HEIGHT]
                     [VideoScreenCanvas.VIDEO_SCREEN_CANVAS_WIDTH];
    
    public VideoPlaybackThread(
            VideoScreenCanvas videoScreenCanvas,
            VideoRecordingThread.VideoCompressionAlgorithm algorithm,
            BitArrayBuilder bitArrayBuilder) {
        
        this.videoScreenCanvas = videoScreenCanvas;
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
        long sleepDuration = 1000L / VideoCompressionApp.FRAMES_PER_SECOND;
        
        int frameBitsStartIndex = 0;
        int frameBitLength = VideoScreenCanvas.VIDEO_SCREEN_CANVAS_HEIGHT *
                             VideoScreenCanvas.VIDEO_SCREEN_CANVAS_WIDTH;
        
        for (int frameIndex = 0;
                frameIndex < VideoCompressionApp.FRAMES_PER_SECOND *
                             VideoCompressionApp.VIDEO_DURATION_SECONDS; 
                frameIndex++) {
            
            Color[][] framePixels = getFramePixels(frameBitsStartIndex);
            
            draw(framePixels);
            
            frameBitsStartIndex += frameBitLength;
            Utils.sleep(sleepDuration);
        }
    }
    
    private Color[][] getFramePixels(int frameBitStartIndex) {
        int bitIndex = frameBitStartIndex;
        
        for (int y = 0; y < framePixels.length; y++) {
            for (int x = 0; x < framePixels[0].length; x++, bitIndex++) {
                long bit = bitArrayBuilder.readBits(bitIndex, 1);
                
                if (bit == 1L) {
                    framePixels[y][x] = Color.BLACK;
                } else {
                    framePixels[y][x] = Color.WHITE;
                }
            }
        }
        
        return framePixels;
    }
    
    private void draw(Color[][] framePixels) {
        videoScreenCanvas.drawFrame(
                videoScreenCanvas.convertFramePixelsToImage(framePixels));
    }
}
