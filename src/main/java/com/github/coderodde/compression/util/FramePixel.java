package com.github.coderodde.compression.util;

/**
 *
 * @author rodio
 */
public final class FramePixel {
    
    private final int x;
    private final int y;
    private final VideoScreenCanvas.PixelColor color;
    
    public FramePixel(int x, int y, VideoScreenCanvas.PixelColor color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public VideoScreenCanvas.PixelColor getColor() {
        return color;
    }
}
