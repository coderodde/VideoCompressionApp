package com.github.coderodde.compression.video.app;

import com.github.coderodde.compression.util.Utils;
import com.github.coderodde.compression.util.Utils.SleepDuration;
import com.github.coderodde.compression.util.VideoScreenCanvas;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author rodio
 */
public final class VideoCompressionApp extends Application {
    
    private static final int FRAMES_PER_SECOND = 25;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("VideoCompressionApp 1.6");
        showBeginRecordingHint();
        
        VideoScreenCanvas videoScreenCanvas = new VideoScreenCanvas();
        videoScreenCanvas.paintCircleVideoShape();
        
        SleepDuration sleepDuration = 
                Utils.getFrameSleepDuration(FRAMES_PER_SECOND);
        
        videoScreenCanvas.addEventHandler(
                MouseEvent.MOUSE_DRAGGED,
                new EventHandler<>() {
                    
            @Override
            public void handle(MouseEvent mouseEvent) {
                int x = (int) mouseEvent.getX();
                int y = (int) mouseEvent.getY();
                videoScreenCanvas.clear();
                videoScreenCanvas.getCircleVideoShape().setCenterX(x);
                videoScreenCanvas.getCircleVideoShape().setCenterY(y);
                videoScreenCanvas.paintCircleVideoShape();
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(videoScreenCanvas);
        stage.setScene(new Scene(root));
        stage.show();
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
