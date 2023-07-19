package com.github.coderodde.compression.video.app;

import com.github.coderodde.compression.util.VideoScreenCanvas;
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
        stage.setTitle("VideoCompressionApp 1.6");
        showBeginRecordingHint();
        
        VideoScreenCanvas videoScreenCanvas = new VideoScreenCanvas();
        videoScreenCanvas.paintCircleVideoShape();
        
        videoScreenCanvas.addEventHandler(
                MouseEvent.MOUSE_DRAGGED, 
                (MouseEvent mouseEvent) -> {
                    
            int x = (int) mouseEvent.getX();
            int y = (int) mouseEvent.getY();
            videoScreenCanvas.clear();
            videoScreenCanvas.getCircleVideoShape().setCenterX(x);
            videoScreenCanvas.getCircleVideoShape().setCenterY(y);
            videoScreenCanvas.paintCircleVideoShape();
            System.out.println("x = " + x + ", y = " + y);
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(videoScreenCanvas);
        stage.setScene(new Scene(root));
        stage.show();
        
        VideoRecordingThread videoRecordingThread = 
                new VideoRecordingThread(
                        videoScreenCanvas, 
                        VideoRecordingThread
                                .VideoCompressionAlgorithm
                                .NO_COMPRESSION);
        
        videoRecordingThread.start();
//        
//        try {
//            videoRecordingThread.join();
//        } catch (InterruptedException ex) {
//            
//        }
//        
//        System.out.println(
//                "No compression bit array size: " + 
//                        videoRecordingThread.getBitArrayBuilder().size());
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
