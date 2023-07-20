package com.github.coderodde.compression.video.app;

import com.github.coderodde.compression.util.Utils;
import com.github.coderodde.compression.util.VideoScreenCanvas;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This thread is a fast drop-in for coordinating the playback threads.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 
 */
public final class VideoCoordinatorThread extends Thread {
    
    private static final long ITERATION_SLEEP_DURATION = 100L;
    
    private final VideoScreenCanvas videoScreenCanvas;
    private final VideoRecordingThread nonCompressiveVideoRecordingThread;
    private final VideoRecordingThread naiveCompressingVideoRecordingThread;
    
    public VideoCoordinatorThread(
            VideoScreenCanvas videoScreenCanvas,
            VideoRecordingThread nonCompressiveVideoRecordingThread,
            VideoRecordingThread naiveCompressingVideoRecordingThread) {
        
        this.videoScreenCanvas = videoScreenCanvas;
        
        this.nonCompressiveVideoRecordingThread = 
                nonCompressiveVideoRecordingThread;
        
        this.naiveCompressingVideoRecordingThread = 
                naiveCompressingVideoRecordingThread;
    }
    
    @Override
    public void run() {
        while (nonCompressiveVideoRecordingThread.isRunning() ||
               naiveCompressingVideoRecordingThread.isRunning()) {
            Utils.sleep(ITERATION_SLEEP_DURATION);
        }
        
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Before playback");
            alert.setHeaderText(
                    "After pressing OK, a recording via non-compressive " + 
                    "recording starts.");
            
            alert.setContentText("Press OK, to view non-compressed video.");
            
            alert.showAndWait();
        });
        
        VideoPlaybackThread nonCompressiveVideoPlaybackThread = 
            new VideoPlaybackThread(
                videoScreenCanvas, 
                VideoRecordingThread.VideoCompressionAlgorithm.NO_COMPRESSION,
                nonCompressiveVideoRecordingThread.getBitArrayBuilder());
        
        nonCompressiveVideoPlaybackThread.start();
        
        try {
            nonCompressiveVideoPlaybackThread.join();
        } catch (InterruptedException ex) {
            
        }
        
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Before playback");
            alert.setHeaderText(
                    "After pressing OK, a recording via naively-compressed  " + 
                    "recording starts.");
            
            alert.setContentText(
                    "Press OK, to view the video with naive compressor.");
            
            alert.showAndWait();
        });
        
        VideoPlaybackThread naiveCompressorVideoPlaybackThread = 
            new VideoPlaybackThread(
                videoScreenCanvas,  
                VideoRecordingThread.VideoCompressionAlgorithm.NO_COMPRESSION,
                nonCompressiveVideoRecordingThread.getBitArrayBuilder());
        
        naiveCompressorVideoPlaybackThread.start();
        
        try {
            naiveCompressorVideoPlaybackThread.join();
        } catch (InterruptedException ex) {
            
        }
    }
}
