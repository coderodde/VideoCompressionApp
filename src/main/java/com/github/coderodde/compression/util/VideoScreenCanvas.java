package com.github.coderodde.compression.util;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
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
    
    public void drawFrame(Image image) {
        graphicsContext.drawImage(image, 0.0, 0.0);
    }
    
    public Image convertFramePixelsToImage(Color[][] framePixels) {
        WritableImage raster = 
                new WritableImage(
                        framePixels[0].length, 
                        framePixels.length);
        
        PixelWriter pixelWriter = raster.getPixelWriter();
        
        for (int y = 0; y < framePixels.length; y++) {
            for (int x = 0; x < framePixels[0].length; x++) {
                pixelWriter.setColor(x, y, framePixels[y][x]);
            }
        }
        
        return raster;
    }
    
    public CircleVideoShape getCircleVideoShape() {
        return circleVideoShape;
    }
}
