package com.github.coderodde.compression.util;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
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
    
    public enum PixelColor {
        
        WHITE(Color.WHITE),
        BLACK(Color.BLACK);
        
        private final Color actualColor;
        
        private PixelColor(Color actualColor) {
            this.actualColor = actualColor;
        }
        
        public Color getColor() {
            return actualColor;
        }
        
        public static PixelColor parsePixelColor(Color color) {
            if (color == Color.WHITE) {
                return PixelColor.WHITE;
            }
            
            if (color == Color.BLACK) {
                return PixelColor.BLACK;
            }
            
            throw new IllegalArgumentException("Unrecognized color: " + color);
        }
    }
    
    private final GraphicsContext graphicsContext;
    private final CircleVideoShape circleVideoShape = 
            new CircleVideoShape(
                    CIRCLE_VIDEO_SHAPE_RADIUS, 
                    VIDEO_SCREEN_CANVAS_WIDTH / 2, 
                    VIDEO_SCREEN_CANVAS_HEIGHT / 2);
    
    private final int matrixWidth;
    private final int matrixHeight;
    
    public VideoScreenCanvas() {
        super(VIDEO_SCREEN_CANVAS_WIDTH, VIDEO_SCREEN_CANVAS_HEIGHT);
        
        this.matrixWidth = VIDEO_SCREEN_CANVAS_WIDTH;
        this.matrixHeight = VIDEO_SCREEN_CANVAS_HEIGHT;
        this.graphicsContext = getGraphicsContext2D();
        
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0.0, 0.0, getWidth(), getHeight());
        
        addEventHandler(
                MouseEvent.MOUSE_DRAGGED, 
                (MouseEvent mouseEvent) -> {
                    circleVideoShape.setCenterX((int) mouseEvent.getX());
                    circleVideoShape.setCenterY((int) mouseEvent.getY());
                    
        });
    }
    
    public PixelColor[][] getPixels() {
        PixelColor[][] pixelMatrix = new PixelColor[matrixHeight][matrixWidth];
        
        for (int y = 0; y < matrixHeight; y++) {
            for (int x = 0; x < matrixWidth; x++) {
                pixelMatrix[y][x] = getPixelColor(x, y);
            }
        }
        
        return pixelMatrix;
    }
    
    public void paintFrame(Frame frame) {
        
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
    
    private PixelColor getPixelColor(int x, int y) {
        WritableImage writableImage = snapshot(null, null);
        Color color = writableImage.getPixelReader().getColor(x, y);
        return PixelColor.parsePixelColor(color);
    }
}
