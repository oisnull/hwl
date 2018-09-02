package com.hwl.beta.utils;

/**
 * Created by Administrator on 2018/1/14.
 */


import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Wongxming E-mail: Wongxming@eoemobile.com
 * @version 1.0
 */
public final class MD5 {
    private static final String LOG_TAG = "MD5";
    private static final String ALGORITHM = "MD5";

    private static char sHexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private static MessageDigest sDigest;

    static {
        try {
            sDigest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "Get MD5 Digest failed.");
        }
    }

    private MD5() {
    }

    final public static String encode(String source) {
        byte[] btyes = source.getBytes();
        byte[] encodedBytes = sDigest.digest(btyes);

        return hexString(encodedBytes);
    }

    public static String hexString(byte[] source) {
        if (source == null || source.length <= 0) {
            return "";
        }

        final int size = source.length;
        final char str[] = new char[size * 2];
        int index = 0;
        byte b;
        for (int i = 0; i < size; i++) {
            b = source[i];
            str[index++] = sHexDigits[b >>> 4 & 0xf];
            str[index++] = sHexDigits[b & 0xf];
        }
        return new String(str);
    }

    public static void main(String[] args) {
        // MD5 ("") = d41d8cd98f00b204e9800998ecf8427e
        System.out.println(MD5.encode(""));
        // MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661
        System.out.println(MD5.encode("a"));
        // MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72
        System.out.println(MD5.encode("abc"));
        System.out.println(MD5.encode("15623701413"));
    }
}
