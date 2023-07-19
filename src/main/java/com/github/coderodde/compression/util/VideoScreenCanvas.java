package com.github.coderodde.compression.util;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class implements the video screen canvas.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 18, 2023)
 * @since 1.6 (Jul 18, 2023)
 */
public final class VideoScreenCanvas extends Canvas {

    public static final int VIDEO_SCREEN_CANVAS_WIDTH = 1200;
    public static final int VIDEO_SCREEN_CANVAS_HEIGHT = 800;
    private static final int CIRCLE_VIDEO_SHAPE_RADIUS = 100;
    
    private final GraphicsContext graphicsContext;
    private final CircleVideoShape circleVideoShape = 
            new CircleVideoShape(
                    VIDEO_SCREEN_CANVAS_WIDTH, 
                    VIDEO_SCREEN_CANVAS_HEIGHT,
                    CIRCLE_VIDEO_SHAPE_RADIUS);
    
    private final int matrixWidth;
    private final int matrixHeight;
    
    public VideoScreenCanvas() {
        super(VIDEO_SCREEN_CANVAS_WIDTH, VIDEO_SCREEN_CANVAS_HEIGHT);
        
        this.matrixWidth = VIDEO_SCREEN_CANVAS_WIDTH;
        this.matrixHeight = VIDEO_SCREEN_CANVAS_HEIGHT;
        this.graphicsContext = getGraphicsContext2D();
        
        clear();
        paintCircleVideoShape();
    }
    
    public void clear() {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, matrixWidth, matrixHeight);
    }
    
    public void paintCircleVideoShape() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillOval(
                circleVideoShape.getCenterX() - circleVideoShape.getRadius(),
                circleVideoShape.getCenterY() - circleVideoShape.getRadius(),
                circleVideoShape.getRadius() * 2,
                circleVideoShape.getRadius() * 2);
    }
    
    public CircleVideoShape getCircleVideoShape() {
        return circleVideoShape;
    }
}
