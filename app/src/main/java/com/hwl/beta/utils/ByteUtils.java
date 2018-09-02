package com.hwl.beta.utils;

/**
 * Created by Administrator on 2018/2/4.
 */

public class ByteUtils {
    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param value 要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value) {
        byte[] byte_src = new byte[4];
        byte_src[3] = (byte) ((value & 0xFF000000) >> 24);
        byte_src[2] = (byte) ((value & 0x00FF0000) >> 16);
        byte_src[1] = (byte) ((value & 0x0000FF00) >> 8);
        byte_src[0] = (byte) ((value & 0x000000FF));
        return byte_src;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param ary    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset] & 0xFF)
                | ((ary[offset + 1] << 8) & 0xFF00)
                | ((ary[offset + 2] << 16) & 0xFF0000)
                | ((ary[offset + 3] << 24) & 0xFF000000));
        return value;
    }

    public static byte[] longToBytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static long bytesToLong(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

    public static byte[] mergeToStart(byte headByte, byte[] bodyBytes) {
        byte[] resultBytes = new byte[bodyBytes.length + 1];
        resultBytes[0] = headByte;
        for (int i = 1; i < resultBytes.length; i++) {
            resultBytes[i] = bodyBytes[i - 1];
        }
        return resultBytes;
    }

    public static byte[] splitBodyBytes(byte[] resultBytes) {
        byte[] bodyBytes = new byte[resultBytes.length - 1];
        for (int i = 0; i < bodyBytes.length; i++) {
            bodyBytes[i] = resultBytes[i + 1];
        }
        return bodyBytes;
    }

    //从start位置开始，不包含start
    //count=0 表示后面所有的数据
    public static byte[] getPositionBytes(int start, int count, byte[] bodyBytes) {
        byte[] resultBytes = null;
        if (count <= 0) {
            resultBytes = new byte[bodyBytes.length - start - count];
        } else {
            resultBytes = new byte[count];
        }
        for (int i = 0; i < resultBytes.length; i++) {
            resultBytes[i] = bodyBytes[start + i];
        }
        return resultBytes;
    }

    //从start位置开始，不包含start,1表示第一个位置
    public static void setPositionBytes(int start, byte[] addBytes, byte[] newBytes) {
        if (newBytes == null || addBytes == null || newBytes.length < addBytes.length)
            return ;

        for (int i = 0; i < addBytes.length; i++) {
            newBytes[start + i] = addBytes[i];
        }
    }
}
