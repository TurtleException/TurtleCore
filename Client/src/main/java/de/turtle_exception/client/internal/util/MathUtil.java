package de.turtle_exception.client.internal.util;

public class MathUtil {
    private MathUtil() { }

    public static long bytesToLong(byte[] bytes) {
        return bytesToLong(bytes, 0);
    }

    public static long bytesToLong(byte[] bytes, int index) {
        long l = 0L;
        for (int i = 0; i < Long.BYTES; i++) {
            l <<= Byte.SIZE;
            l |= (bytes[index + i] & 0xFF);
        }
        return l;
    }

    public static byte[] longToBytes(long l) {
        byte[] arr = new byte[Long.BYTES];
        for (int i = Long.BYTES - 1; i >= 0; i--) {
            arr[i] = (byte)(l & 0xFF);
            l >>= Byte.SIZE;
        }
        return arr;
    }
}
