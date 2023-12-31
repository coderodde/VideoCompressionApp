package com.github.coderodde.compression.util;

/**
 * This class contains various utility methods.
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
    
    /**
     * Returns the minimum number of bits sufficient to store the value
     * {@code maximumValue}.
     * 
     * @param maximumValue the integer value we wish to store.
     * @return number of bits sufficient to store the input value.
     */
    public static int computeNumberOfBitsToStore(int maximumValue) {
        if (maximumValue == 0) {
            return 1;
        }
            
        int bits = 0;
        
        while (maximumValue != 0) {
            maximumValue /= 2;
            bits++;
        }
        
        return bits;
    }
    
    /**
     * Returns the number of nanoseconds which it takes to call the 
     * {@link Trhead.sleep}.
     * 
     * @return the number of nanoseconds. 
     */
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
