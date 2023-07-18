package com.github.coderodde.compression.util;

import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class BitArrayBuilderTest {
    
    @Test
    public void building() {
        BitArrayBuilder builder = new BitArrayBuilder(100);
        builder.appendBits(0b11001001, 8);
        builder.appendBits(0b110, 4);
        builder.appendBits(0x8000_0000_0000_0000L, 64);
        byte[] bytes = builder.getBytes();
        
        for (byte b : bytes) {
            System.out.println(Integer.toBinaryString(b & 0xFF));
        }
        
        System.out.println();
        
        byte[] expectedBytes = { 
            (byte) 0b11001001, 
                   0b110, 
                   0, 0,
                   0, 0, 
                   0, 0, 
                   0, 
                   0b1000 
        };
        
        assertTrue(Arrays.equals(expectedBytes, bytes));
    }
}
