package com.github.coderodde.compression.video.app;

import com.github.coderodde.compression.util.VideoScreenCanvas;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class VideoCompressionApp extends Application {
    
    static final int FRAMES_PER_SECOND = 25;
    static final int VIDEO_DURATION_SECONDS = 10;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("VideoCompressionApp 1.6 - by coderodde");
        showBeginRecordingHint();
        
        VideoScreenCanvas videoScreenCanvas = new VideoScreenCanvas();
        videoScreenCanvas.paintCircleVideoShape();
        
        EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int x = (int) mouseEvent.getX();
                int y = (int) mouseEvent.getY();
                videoScreenCanvas.clear();
                videoScreenCanvas.getCircleVideoShape().setCenterX(x);
                videoScreenCanvas.getCircleVideoShape().setCenterY(y);
                videoScreenCanvas.paintCircleVideoShape();
            }
        };
        
        videoScreenCanvas.addEventHandler(
                MouseEvent.MOUSE_DRAGGED, 
                mouseEventHandler);
        
        stage.setResizable(false);
        StackPane root = new StackPane();
        root.getChildren().add(videoScreenCanvas);
        stage.setScene(new Scene(root));
        stage.show();
        
        VideoRecordingThread videoRecordingThreadNoCompression = 
                new VideoRecordingThread(
                        videoScreenCanvas, 
                        VideoRecordingThread
                                .VideoCompressionAlgorithm
                                .NO_COMPRESSION);
        
        VideoRecordingThread videoRecordingThreadNaiveCompression = 
                new VideoRecordingThread(
                        videoScreenCanvas, 
                        VideoRecordingThread
                                .VideoCompressionAlgorithm
                                .NAIVE_COMPRESSOR);
        
        // Start recording.
        videoRecordingThreadNoCompression.start();
        videoRecordingThreadNaiveCompression.start();
        
        VideoCoordinatorThread videoCoordinatorThread = 
                new VideoCoordinatorThread(
                        videoScreenCanvas,
                        videoRecordingThreadNoCompression,
                        videoRecordingThreadNaiveCompression);
        
        videoCoordinatorThread.start();
    }
    
    private static void showBeginRecordingHint() {
        Alert beginInfoAlert = new Alert(AlertType.INFORMATION);
        beginInfoAlert.setTitle("Beginning recording");
        
        beginInfoAlert.setHeaderText(
                "Press OK and you will have 10 seconds to record the video.");
        
        beginInfoAlert.setContentText(
                "During the 10 seconds of recording, you can move " + 
                "the black circle by dragging it with the mouse.");
        
        beginInfoAlert.showAndWait();
    }
}
