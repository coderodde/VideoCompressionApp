package com.github.coderodde.compression.video.app;

/**
 *
 * @author rodio
 */
public final class VideoRecordingThread extends Thread {
    
    public enum VideoCompressionAlgorithm {
        NO_COMPRESSION,
        NAIVE_COMPRESSOR;
    }
    
    private final VideoCompressionAlgorithm compressionAlgorithm;
    
    public VideoRecordingThread(
            VideoCompressionAlgorithm compressionAlgorithm) {
        
        this.compressionAlgorithm = compressionAlgorithm;
    }
    
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
        
    }
    
    private void recordWithNaiveCompression() {
        
    }
}
