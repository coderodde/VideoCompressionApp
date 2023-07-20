package com.github.coderodde.compression.util;

import java.util.Arrays;

/**
 * This class implements a bit array builder for storing the video frames.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 18, 2023)
 * @since 1.6 (Jul 18, 2023)
 */
public final class BitArrayBuilder {

    private static final int DEFAULT_LONG_ARRAY_CAPACITY = 50_000;
    
    /**
     * This array stores the actual bits.
     */
    private long[] bitArray;
    
    /**
     * This field specifies how many bits there are in this builder.
     */
    private int size;
    
    /**
     * Constructs a bit array builder with default capacity.
     */
    public BitArrayBuilder() {
        this(DEFAULT_LONG_ARRAY_CAPACITY * Long.SIZE);
    }
    
    /**
     * Constructs a bit array builder capable of holding 
     * {@code initialNumberOfBits} bits.
     * 
     * @param initialNumberOfBits the initial requested capacity.
     */
    public BitArrayBuilder(int initialNumberOfBits) {
        this.bitArray = new long[getInitialNumberOfLongs(initialNumberOfBits)];
    }
    
    /**
     * Returns the number of bits stored in this builder.
     * 
     * @return the number of bits.
     */
    public int size() {
        return size;
    }
    
    /**
     * Appends {@code length} least-significant bits from {@code bitsToAppend} 
     * to this bit array builder.
     * 
     * @param bitsToAppend the {@code long} value holding the bits.
     * @param length       the number of least-significant bits to append.
     */
    public void appendBits(long bitsToAppend, int length) {
        expandTableIfNeeded(size + length);
        
        for (int bitIndex = 0; bitIndex < length; bitIndex++) {
            appendBit(((bitsToAppend >> bitIndex) & 0x1L) != 0);
        }
    }
    
    /**
     * Reads {@code length} bits starting from index {@code index}.
     * 
     * @param index  the leftmost index of the bit range to read.
     * @param length the length of the bit range to read.
     * @return the {@code length} bits.
     */
    public long readBits(int index, int length) {
        long ret = 0L;
        
        for (int i = index, bitIndex = 0; i < index + length; i++, bitIndex++) {
            boolean bit = readBit(i);
            
            if (bit) {
                ret |= (1 << bitIndex);
            }
        }
        
        return ret;
    }
    
    /**
     * Reads the {@code index}th bit.
     * 
     * @param index the index of the bit to read.
     * @return a bit, {@code true} for the 1 and {@code false} for the 0.
     */
    private boolean readBit(int index) {
        int longIndex = index / Long.SIZE;
        long dataLong = bitArray[longIndex];
        index %= Long.SIZE;
        long mask = 1L << index;
        return (dataLong & mask) != 0;
    }
    
    /**
     * Returns the shortest byte array capable of holding all the bits in this
     * bit array builder.
     * 
     * @return the byte content of this builder.
     */
    public byte[] getBytes() {
        int numberOfBytes = size / Byte.SIZE;
        int remainder = size % Byte.SIZE;
        int byteArrayLength = numberOfBytes + (remainder != 0 ? 1 : 0);
        byte[] bytes = new byte[byteArrayLength];
        
        for (int byteIndex = 0; byteIndex < bytes.length; byteIndex++) {
            bytes[byteIndex] = getDataByte(byteIndex);
        }
        
        return bytes;
    }
    
    /**
     * Returns the {@code byteIndex}th byte.
     * 
     * @param byteIndex the byte index.
     * @return the byte value at index {@code byteIndex}.
     */
    private byte getDataByte(int byteIndex) {
        long longData = bitArray[byteIndex / Long.BYTES];
        byteIndex %= Long.BYTES;
        
        switch (byteIndex) {
            case 0:
                return (byte)(longData);
                
            case 1:
                return (byte)((longData >> 8) & 0xFF);
                
            case 2:
                return (byte)((longData >> 16) & 0xFF);
                
            case 3:
                return (byte)((longData >> 24) & 0xFF);
                
            case 4:
                return (byte)((longData >> 32) & 0xFF);
                
            case 5:
                return (byte)((longData >> 40) & 0xFF);
                
            case 6:
                return (byte)((longData >> 48) & 0xFF);
                
            case 7:
                return (byte)((longData >> 56) & 0xFF);
                
            default:
                throw new IllegalStateException("Should not get here ever.");
        }
    }
    
    /**
     * Implements the actual appending of a bit to the tail of this bit array
     * builder.
     * 
     * @param bit the bit to append. If is {@code true}, a 1-bit is appended,
     *            and 0-bit otherwise.
     */
    private void appendBit(boolean bit) {
        int longIndex = size / Long.SIZE;
        int longBitsIndex = size - longIndex * Long.SIZE;
        long mask = 0x1L << longBitsIndex;
        
        if (bit) {
            bitArray[longIndex] |= mask;
        } else {
            bitArray[longIndex] &= ~mask;
        }
        
        size++;
    }
    
    /**
     * Computes the initial number of {@code long} values sufficient to hold 
     * {@code initialNumberOfBits} bits.
     * 
     * @param initialNumberOfBits the number of bits to accommodate.
     * @return the number of longs needed to store all the bits.
     */
    private int getInitialNumberOfLongs(int initialNumberOfBits) {
        int longs = initialNumberOfBits / Long.SIZE;
        int remainder = initialNumberOfBits % Long.SIZE;
        return longs + (remainder != 0 ? 1 : 0);
    }
    
    /**
     * Doubles the capacity of the actual bit array.
     * 
     * @param requestedCapacity the requested capacity.
     */
    private void expandTableIfNeeded(int requestedCapacity) {
        if (requestedCapacity > bitArray.length * Long.SIZE) {
            long[] newBitArray = 
                    Arrays.copyOf(
                            bitArray, 
                            Math.max(bitArray.length * 2, requestedCapacity));
            
            int sizeToCopy = size / Long.SIZE + (size % Long.SIZE == 0 ? 0 : 1);
            System.arraycopy(bitArray, 0, newBitArray, 0, sizeToCopy);
            bitArray = newBitArray;
        }
    }
}
