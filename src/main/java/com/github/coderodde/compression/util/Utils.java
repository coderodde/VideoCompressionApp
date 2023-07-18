package com.github.coderodde.compression.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 18, 2023)
 * @since 1.6 (Jul 18, 2023)
 */
public final class Utils {
    
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            
        }
    }
    
    public static void sleep(long milliseconds, int nanoseconds) {
        try {
            Thread.sleep(milliseconds, nanoseconds);
        } catch (InterruptedException ex) {
            
        }
    }
    
    public static void sleep(SleepDuration sleepDuration) {
        try {
            Thread.sleep(sleepDuration.millisecondsPart, 
                         sleepDuration.nanosecondsPart);
        } catch (InterruptedException ex) {
            
        }
    }
    
    public static int computeNumberOfBitsToStore(int maximumValue) {
        int bits = 0;
        
        while (maximumValue != 0) {
            maximumValue /= 2;
            bits++;
        }
        
        return bits;
    }
    
    public static long getThreadSleepCallDuration() {
        long start = System.nanoTime();
        sleep(0L);
        long duration = System.nanoTime() - start;
        return Math.max(duration, 0L);
    }
    
    public static SleepDuration getFrameSleepDuration(int framesPerSecond) {
        long sleepCallDuration = getThreadSleepCallDuration();
        long durationNanoseconds = 1_000_000_000 / framesPerSecond 
                                                 - sleepCallDuration;
        
        long durationMilliseconds = durationNanoseconds / 1_000_000;
        long durationNanosecondsPart = 
                durationNanoseconds - durationMilliseconds * 1_000_000;
        
        return new SleepDuration(durationMilliseconds, 
                                 (int) durationNanosecondsPart);
    }
    
    public static final class SleepDuration {
        public final long millisecondsPart;
        public final int nanosecondsPart;
        
        SleepDuration(long milliseconds, int nanoseconds) {
            this.millisecondsPart = milliseconds;
            this.nanosecondsPart = nanoseconds;
        }
    }
}
