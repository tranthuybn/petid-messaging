//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package vn.supperapp.apigw.messaging.utils;

import java.io.ByteArrayOutputStream;

public class Hex {
    private byte[] m_value = null;
    public static final String ERROR_ODD_NUMBER_OF_DIGITS = "Odd number of digits in hex string";
    public static final String ERROR_BAD_CHARACTER_IN_HEX_STRING = "Bad character or insufficient number of characters in hex string";
    private static final int[] DEC = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    public Hex() {
    }

    public byte[] getBytes() {
        if (this.m_value == null) {
            return new byte[0];
        } else {
            byte[] bytes = new byte[this.m_value.length];
            System.arraycopy(this.m_value, 0, bytes, 0, this.m_value.length);
            return bytes;
        }
    }

    public String toString() {
        return encode(this.m_value);
    }

    public boolean equals(Object object) {
        String s1 = object.toString();
        String s2 = this.toString();
        return s1.equals(s2);
    }

    public static String encode(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);

        for(int i = 0; i < bytes.length; ++i) {
            sb.append(convertDigit(bytes[i] >> 4));
            sb.append(convertDigit(bytes[i] & 15));
        }

        return sb.toString();
    }

    public static int convert2Int(byte[] hex) {
        if (hex.length < 4) {
            return 0;
        } else if (DEC[hex[0]] < 0) {
            throw new IllegalArgumentException("Bad character or insufficient number of characters in hex string");
        } else {
            int len = DEC[hex[0]];
            len <<= 4;
            if (DEC[hex[1]] < 0) {
                throw new IllegalArgumentException("Bad character or insufficient number of characters in hex string");
            } else {
                len += DEC[hex[1]];
                len <<= 4;
                if (DEC[hex[2]] < 0) {
                    throw new IllegalArgumentException("Bad character or insufficient number of characters in hex string");
                } else {
                    len += DEC[hex[2]];
                    len <<= 4;
                    if (DEC[hex[3]] < 0) {
                        throw new IllegalArgumentException("Bad character or insufficient number of characters in hex string");
                    } else {
                        len += DEC[hex[3]];
                        return len;
                    }
                }
            }
        }
    }

    private static char convertDigit(int value) {
        value &= 15;
        return value >= 10 ? (char)(value - 10 + 97) : (char)(value + 48);
    }
}
