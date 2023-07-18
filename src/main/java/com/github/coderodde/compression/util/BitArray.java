package com.github.coderodde.compression.util;

/**
 * This class implements a simple array of bits.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 18, 2023)
 * @since 1.6 (Jul 18, 2023)
 */
public final class BitArray {
    
    private final long[] storage;
    
    /**
     * Constructs a new {@code BitArray} capable to store at least {@code bits}
     * bits.
     * 
     * @param bits the minimum length of the bit array in bits. 
     */
    public BitArray(int bits) {
        int bitsPerLong = Long.SIZE;
        int storageLongs = bits / bitsPerLong + bits % bitsPerLong != 0 ? 1 : 0;
        this.storage = new long[storageLongs];
    }
    
    /**
     * Sets the bit.
     * 
     * @param index the bit index.
     * @param on    the bit value.
     */
    public void setBit(int index, boolean on) {
        int bitsPerLong = Long.SIZE;
        int omitLongs = index / bitsPerLong;
        int omitBits = (index - bitsPerLong * omitLongs);
        
        if (on) {
            write1Bit(omitLongs, omitBits);
        } else {
            write0Bit(omitLongs, omitBits);
        }
    }
    
    /**
     * Reads a bit at index {@code index}.
     * 
     * @param index the bit index.
     * 
     * @return {@code true} if the {@code index}th bit is set, and {@code false}
     *         otherwise.
     */
    public boolean getBit(int index) {
        int bitsPerLong = Long.SIZE;
        int omitLongs = index / bitsPerLong;
        int omitBits = (index - bitsPerLong * omitLongs);
        return readBit(omitLongs, omitBits);
    }
    
    private void write1Bit(int omitLongs, int omitBits) {
        storage[omitLongs] = (storage[omitLongs] | (0x1L << omitBits));
    }
    
    private void write0Bit(int omitLongs, int omitBits) {
        storage[omitLongs] = (storage[omitLongs] & ~(0x1L << omitBits));
    }
    
    private boolean readBit(int omitLongs, int omitBits) {
        long storageLong = storage[omitLongs];
        long mask = 0x1L << omitBits;
        return (storageLong & mask) != 0;
    }
}
