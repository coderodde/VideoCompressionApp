package com.github.coderodde.compression.util;

/**
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 18, 2023)
 * @since 1.6 (Jul 18, 2023)
 */
public final class CircleVideoShape {
    
    private final int radius;
    private int centerX;
    private int centerY;
    
    public CircleVideoShape(int radius, int centerX, int centerY) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
    }
    
    public int getRadius() {
        return radius;
    }
    
    public int getCenterX() {
        return centerX;
    }
    
    public int getCenterY() {
        return centerY;
    }
    
    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }
    
    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
}
