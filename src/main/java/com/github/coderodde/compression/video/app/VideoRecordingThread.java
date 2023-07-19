package com.github.coderodde.compression.video.app;

import com.github.coderodde.compression.util.BitArrayBuilder;
import com.github.coderodde.compression.util.PixelColor;
import com.github.coderodde.compression.util.Utils;
import com.github.coderodde.compression.util.Utils.SleepDuration;
import com.github.coderodde.compression.util.VideoScreenCanvas;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Platform;

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
    
    private void recordWithNoCompression() {
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

            Platform.runLater(() -> {
                videoScreenCanvas.clear();
                videoScreenCanvas.paintCircleVideoShape();
            });
            
            Utils.sleep(sleepDuration);

            PixelColor[][] pixelMatrix = 
                    videoScreenCanvas
                            .getCircleVideoShape()
                            .getColorMatrix();

            for (PixelColor[] pixelMatrixRow : pixelMatrix) {
                for (PixelColor pixelColor : pixelMatrixRow) {
                    bitArrayBuilder.appendBits(
                            pixelColor == PixelColor.WHITE ? 0L : 1L,
                            1);
                }
            }
        }
        
        System.out.println(
                "Bits in no compression bit array: " + bitArrayBuilder.size());
    }
    
    private void recordWithNaiveCompression() {
        SleepDuration sleepDuration = 
                Utils.getFrameSleepDuration(
                        VideoCompressionApp.FRAMES_PER_SECOND);
        
        bitArrayBuilder = new BitArrayBuilder();
        
        // Process the first frame:
        PixelColor[][] previousPixelMatrix = processFirstFrame();
        
        Utils.sleep(sleepDuration);
        
        for (int frameIndex = 1; frameIndex < framesToRecord; frameIndex++) {
            Platform.runLater(() -> {
                videoScreenCanvas.clear();
                videoScreenCanvas.paintCircleVideoShape();
            });
            
            Utils.sleep(sleepDuration);
            
            PixelColor[][] currentPixelMatrix = 
                    videoScreenCanvas.getCircleVideoShape().getColorMatrix();
            
            loadBits(previousPixelMatrix, currentPixelMatrix);
            
            previousPixelMatrix = currentPixelMatrix;
        }
        
        System.out.println(
                "Bits in naive compressor bit array: " +
                        bitArrayBuilder.size());
    }
    
    private void loadBits(PixelColor[][] previousPixelMatrix, 
                          PixelColor[][] currentPixelMatrix) {
        int matrixHeight = previousPixelMatrix.length;
        int matrixWidth = previousPixelMatrix[0].length;
        
        int matrixYBitLength =
                Utils.computeNumberOfBitsToStore(matrixHeight);
        
        int matrixXBitLength =
                Utils.computeNumberOfBitsToStore(matrixWidth);
        
        Set<Point> changedPixelPoints = new HashSet<>();
        
        for (int y = 0; y < previousPixelMatrix.length; y++) {
            for (int x = 0; x < previousPixelMatrix[0].length; x++) {
                PixelColor previousPixelColor = previousPixelMatrix[y][x];
                PixelColor currentPixelColor = currentPixelMatrix[y][x];
                
                if (previousPixelColor != currentPixelColor) {
                    changedPixelPoints.add(new Point(x, y));
                }
            }
        }
        
        int changedPixelPointsBitLength = 
                Utils.computeNumberOfBitsToStore(changedPixelPoints.size());
        
        bitArrayBuilder.appendBits(changedPixelPoints.size(),
                                   changedPixelPointsBitLength);
        
        for (Point point : changedPixelPoints) {
            bitArrayBuilder.appendBits(point.x, matrixXBitLength);
            bitArrayBuilder.appendBits(point.y, matrixYBitLength);
        }
    }
    
    private PixelColor[][] processFirstFrame() {
        PixelColor[][] pixelMatrix =
                videoScreenCanvas.getCircleVideoShape().getColorMatrix();
        
        for (int y = 0; y < pixelMatrix.length; y++) {
            for (int x = 0; x < pixelMatrix[0].length; x++) {
                if (pixelMatrix[y][x].equals(PixelColor.WHITE)) {
                    bitArrayBuilder.appendBits(0L, 1);
                } else {
                    bitArrayBuilder.appendBits(1L, 1);
                }
            }
        }
        
        return pixelMatrix;
    }
}
