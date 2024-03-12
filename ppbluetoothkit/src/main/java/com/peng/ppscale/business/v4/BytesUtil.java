//
// * Copyright © 2015-2018 Anker Innovations Technology Limited All Rights Reserved.
// * The program and materials is not free. Without our permission, any use, including but not
// limited to reproduction, retransmission, communication, display, mirror, download,
// modification, is expressly prohibited. Otherwise, it will be pursued for legal liability.

//
package com.peng.ppscale.business.v4;


import com.peng.ppscale.util.Logger;

/**
 * Created by ocean on 2018/1/12.
 */

public class BytesUtil {


    private BytesUtil() {
    }

    /**
     * byte array to hex string
     *
     * @param bytes
     * @param length
     * @return
     */
    public static String bytesToHexString(byte[] bytes, int length) {
        if (bytes == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * byte array to hex string
     *
     * @param bytes
     */
    public static String bytesToHexString(byte[] bytes) {
        return bytesToHexString(bytes, bytes.length);
    }

    public static String byte2BitString(byte by) {
        StringBuilder sb = new StringBuilder();
        sb.append((by >> 7) & 0x1)
                .append((by >> 6) & 0x1)
                .append((by >> 5) & 0x1)
                .append((by >> 4) & 0x1)
                .append((by >> 3) & 0x1)
                .append((by >> 2) & 0x1)
                .append((by >> 1) & 0x1)
                .append((by) & 0x1);
        return sb.toString();
    }

    /**
     * byte to hex string
     *
     * @param data
     * @return
     */
    public static String byteToHexString(byte data) {

        StringBuilder stringBuilder = new StringBuilder();
        int v = data & 0xFF;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * decode hex string to string
     *
     * @param bytes
     * @return
     */
    public static String decodeHexString(String bytes) {

        int length = bytes.length() / 2;
        byte[] data = new byte[length];

        for (int i = 0; i < length; i++) {

            int index = i * 2;
            int v = Integer.parseInt(bytes.substring(index, index + 2), 16);
            data[i] = (byte) v;
        }

        return new String(data);
    }

    public static int byteToInt(byte data) {

        return data & 0xFF;
    }
    public static byte intToByte(int data) {

        return (byte) (data & 0xFF);
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static short getShort(byte[] buf, boolean bBigEnding) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 2) {
            throw new IllegalArgumentException("byte array size > 2 !");
        }
        short r = 0;
        if (bBigEnding) {
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0x00ff);
            }
        } else {
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x00ff);
            }
        }

        return r;
    }

    public static short getShort(byte low, byte hight) {
        short r = 0;
        r |= (hight & 0x00ff);
        r <<= 8;
        r |= (low & 0x00ff);

        return r;
    }

    public static byte[] cutOutBytes(byte[] data1, int startPostion, int length) {
        if (startPostion < 0) {
            startPostion = 0;
        }
        if (length <= 0) {
            return null;
        }

        byte[] data2 = new byte[length];
        System.arraycopy(data1, startPostion, data2, 0, length);
        Logger.v("cutOutBytes data1.length " + data1.length + "  data2.length " + data2.length);
        return data2;
    }

}
