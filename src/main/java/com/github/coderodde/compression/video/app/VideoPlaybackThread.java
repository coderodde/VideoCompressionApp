package com.github.coderodde.compression.video.app;

import com.github.coderodde.compression.util.BitArrayBuilder;
import com.github.coderodde.compression.util.Utils;
import com.github.coderodde.compression.util.VideoScreenCanvas;
import static com.github.coderodde.compression.video.app.VideoRecordingThread.VideoCompressionAlgorithm.*;
import javafx.scene.paint.Color;

/**
 * This class implements the video playback thread.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 20, 2023)
 * @since 1.6 (Jul 20, 2023)
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
                playbackWithNaiveCompressor();
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
            
            // Load framePixels with new pixels:
            loadFramePixels(frameBitsStartIndex);
            
            draw(framePixels);
            
            // Advance towards the next frame:
            frameBitsStartIndex += frameBitLength;
            Utils.sleep(sleepDuration);
        }
    }
    
    private void loadFramePixels(int frameBitStartIndex) {
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
    }
    
    private void draw(Color[][] framePixels) {
        videoScreenCanvas.drawFrame(
                videoScreenCanvas.convertFramePixelsToImage(framePixels));
    }
    
    private void playbackWithNaiveCompressor() {
        long sleepDuration = 1000L / VideoCompressionApp.FRAMES_PER_SECOND;
        
        int frameBitLength = VideoScreenCanvas.VIDEO_SCREEN_CANVAS_HEIGHT *
                             VideoScreenCanvas.VIDEO_SCREEN_CANVAS_WIDTH;
        
        // Draw the initial frame:
        loadFramePixels(0);
        draw(framePixels);
        
        Color[][] previousPixels = framePixels;
        
        BitIndexHolder bitIndexHolder = new BitIndexHolder();
        bitIndexHolder.bitIndex = frameBitLength;
        
        for (int frameIndex = 1; 
                frameIndex < VideoCompressionApp.FRAMES_PER_SECOND * 
                             VideoCompressionApp.VIDEO_DURATION_SECONDS; 
                frameIndex++) {
            
            // Get the next frame::
            Color[][] nextPixels = 
                    loadNextPixels(
                            previousPixels, 
                            bitIndexHolder);
            
            draw(nextPixels);
            
            previousPixels = nextPixels;
            Utils.sleep(sleepDuration);
        }   
    }
    
    private Color[][] loadNextPixels(Color[][] previousPixels,
                                     BitIndexHolder bitIndexHolder) {
        // Compute the minimum number of bits sufficient to represent the
        // integer no larger than the number of pixels in a frame:
        int bitsInNumberOfPixels = 
                Utils.computeNumberOfBitsToStore(
                        VideoScreenCanvas.VIDEO_SCREEN_CANVAS_HEIGHT *
                        VideoScreenCanvas.VIDEO_SCREEN_CANVAS_WIDTH);
        
        // Compute the minimum number of bits sufficient to represent any valid
        // pixel X-coordinate:
        int bitsInXCoordinate = 
                Utils.computeNumberOfBitsToStore(
                        VideoScreenCanvas.VIDEO_SCREEN_CANVAS_WIDTH);
    
        // Compute the minimum number of bits sufficient to represent any valid
        // pixel Y-coordinate:
        int bitsInYCoordinate = 
                Utils.computeNumberOfBitsToStore(
                        VideoScreenCanvas.VIDEO_SCREEN_CANVAS_HEIGHT);
        
        // Read the number of pixels that changed between the previous and
        // next frames:
        int numberOfChangedPixels = 
                (int) bitArrayBuilder.readBits(
                        bitIndexHolder.bitIndex,
                        bitsInNumberOfPixels);
        
        bitIndexHolder.bitIndex += bitsInNumberOfPixels;
        Color[][] nextPixels = 
                new Color[VideoScreenCanvas.VIDEO_SCREEN_CANVAS_HEIGHT]
                         [VideoScreenCanvas.VIDEO_SCREEN_CANVAS_WIDTH];
        
        for (int pixelIndex = 0; 
                pixelIndex < numberOfChangedPixels; 
                pixelIndex++) {
            // Read the X-coordinate of the pixel that changed between previous
            // and next frames:
            int x = 
                    (int) bitArrayBuilder.readBits(
                            bitIndexHolder.bitIndex, 
                            bitsInXCoordinate);
            
            // Advance towards the Y-coordinate:
            bitIndexHolder.bitIndex += bitsInXCoordinate;
            
            // Read the Y-coordinate of the pixel that changed between previous
            // and next frames:
            int y = 
                    (int) bitArrayBuilder.readBits(
                            bitIndexHolder.bitIndex,
                            bitsInYCoordinate);
            
            // Advance towards the next pixel:
            bitIndexHolder.bitIndex += bitsInYCoordinate;
            Color previousPixelColor = previousPixels[y][x];
            Color nextPixelColor = flipColor(previousPixelColor);
            nextPixels[y][x] = nextPixelColor;
        }
        
        return nextPixels;
    }
    
    private static Color flipColor(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }
    
    private static class BitIndexHolder {
        int bitIndex;
    } 
}
