package com.github.coderodde.compression.video.app;

import com.github.coderodde.compression.util.Utils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Potilaskone
 */
public final class VideoCoordinatorThread extends Thread {
    
    private static final long ITERATION_SLEEP_DURATION = 100L;
    
    private final VideoRecordingThread nonCompressiveVideoRecordingThread;
    private final VideoRecordingThread naiveCompressingVideoRecordingThread;
    
    public VideoCoordinatorThread(
            VideoRecordingThread nonCompressiveVideoRecordingThread,
            VideoRecordingThread naiveCompressingVideoRecordingThread) {
        
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
            
            alert.showAndWait();
        });
    }
}