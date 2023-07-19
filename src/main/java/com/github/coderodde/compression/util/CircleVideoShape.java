package com.github.coderodde.compression.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class implements the circle video shape for recording.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 18, 2023)
 * @since 1.6 (Jul 18, 2023)
 */
public final class CircleVideoShape {
    
    /**
     * The width of the canvas that contains this circle.
     */
    private final int pixelMatrixWidth;
    
    /**
     * The height of the canvas that contains this circle.
     */
    private final int pixelMatrixHeight;
    
    /**
     * The radius of this circle.
     */
    private final int radius;
    
    /**
     * The center X-coordinate.
     */
    private final AtomicInteger centerX;
    
    /**
     * The center Y-coordinate.
     */
    private final AtomicInteger centerY;
    
    /**
     * Constructs a new circle.
     * 
     * @param pixelMatrixWidth  the width of the containing canvas.
     * @param pixelMatrixHeight the height of the containing canvas.
     * @param radius            the radius of the circle.
     */
    public CircleVideoShape(
            int pixelMatrixWidth,
            int pixelMatrixHeight,
            int radius) {
        this.pixelMatrixWidth = pixelMatrixWidth;
        this.pixelMatrixHeight = pixelMatrixHeight;
        this.radius = radius;
        this.centerX = new AtomicInteger(pixelMatrixWidth / 2);
        this.centerY = new AtomicInteger(pixelMatrixHeight / 2);
    }
    
    public int getRadius() {
        return radius;
    }
    
    public int getCenterX() {
        return centerX.get();
    }
    
    public int getCenterY() {
        return centerY.get();
    }
    
    public void setCenterX(int centerX) {
        this.centerX.set(centerX);
    }
    
    public void setCenterY(int centerY) {
        this.centerY.set(centerY);
    }
    
    /**
     * Returns the entire pixel color matrix.
     * 
     * @return pixel color matrix.
     */
    public PixelColor[][] getColorMatrix() {
        PixelColor[][] pixelColorMatrix = new PixelColor[pixelMatrixHeight]
                                                        [pixelMatrixWidth];
        
        for (int y = 0; y < pixelMatrixHeight; y++) {
            for (int x = 0; x < pixelMatrixWidth; x++) {
                pixelColorMatrix[y][x] = getPixelColorAt(x, y);
            }
        }
        
        return pixelColorMatrix;
    }
    
    /**
     * Computes the pixel color of the pixel at coordinates {@code (x, y)]}.
     * 
     * @param x the X-coordinate of the pixel.
     * @param y the Y-coordinate of the pixel.
     * @return the actual color of the pixel.
     */
    private PixelColor getPixelColorAt(int x, int y) {
        int dx = x - centerX.get();
        int dy = y - centerY.get();
        int dist2 = dx * dx + dy * dy;
        double dist = Math.sqrt(dist2);
        return dist > radius ? PixelColor.WHITE : PixelColor.BLACK;
    }
}
