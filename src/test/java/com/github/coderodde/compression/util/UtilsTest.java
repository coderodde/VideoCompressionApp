package com.github.coderodde.compression.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void testComputeNumberOfBitsToStore() {
        assertEquals(1, Utils.computeNumberOfBitsToStore(1));
        assertEquals(2, Utils.computeNumberOfBitsToStore(2));
        assertEquals(2, Utils.computeNumberOfBitsToStore(3));
        assertEquals(3, Utils.computeNumberOfBitsToStore(4));
        assertEquals(3, Utils.computeNumberOfBitsToStore(7));
        assertEquals(4, Utils.computeNumberOfBitsToStore(8));
        assertEquals(4, Utils.computeNumberOfBitsToStore(15));
        assertEquals(5, Utils.computeNumberOfBitsToStore(16));
        assertEquals(8, Utils.computeNumberOfBitsToStore(255));
        assertEquals(9, Utils.computeNumberOfBitsToStore(256));
    }
}
