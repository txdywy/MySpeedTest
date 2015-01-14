package com.num.myspeedtest.controller.utils;

import java.security.MessageDigest;

public class HashUtil {
    public static String SHA1(String s) {
        if(s == null) return "";
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(s.getBytes("iso-8859-1"), 0, s.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfByte = (data[i] >>> 4) & 0x0F;
            int twoHalves = 0;
            do {
                if ((0 <= halfByte) && (halfByte <= 9))
                    buf.append((char) ('0' + halfByte));
                else
                    buf.append((char) ('a' + (halfByte - 10)));
                halfByte = data[i] & 0x0F;
            } while(twoHalves++ < 1);
        }
        return buf.toString();
    }
}
