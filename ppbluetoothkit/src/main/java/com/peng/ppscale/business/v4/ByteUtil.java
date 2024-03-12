package com.peng.ppscale.business.v4;

import android.text.TextUtils;


import com.peng.ppscale.util.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ByteUtil {

    /*16进制字符串转10进制int*/
    public static int hexToTen(String hex) {
        if (TextUtils.isEmpty(hex)) {
            return 0;
        }
        return Integer.valueOf(hex, 16);
    }

    /*ElectronicScale异或检验*/
    public static byte[] getXorForElectronicScale(byte[] datas) {

        byte[] bytes = new byte[datas.length + 1];
        byte temp = datas[1];
        bytes[0] = datas[0];
        bytes[1] = datas[1];
        for (int i = 2; i < datas.length; i++) {
            bytes[i] = datas[i];
            temp ^= datas[i];
        }
        bytes[datas.length] = temp;
        return bytes;
    }

    /*异或检验*/
    public static byte[] getXor(byte[] datas) {

        byte[] bytes = new byte[datas.length + 1];
        byte temp = datas[0];
        bytes[0] = datas[0];
        for (int i = 1; i < datas.length; i++) {
            bytes[i] = datas[i];
            temp ^= datas[i];
        }
        bytes[datas.length] = temp;
        return bytes;
    }

    /*异或检验*/
    public static byte getXorValue(byte[] datas) {

        byte[] bytes = new byte[datas.length + 1];
        byte temp = datas[0];
        bytes[0] = datas[0];
        for (int i = 1; i < datas.length; i++) {
            bytes[i] = datas[i];
            temp ^= datas[i];
        }
//        bytes[datas.length] = temp;
        return temp;
    }

    /*异或检验*/
    public static boolean isXorValue(String checkString, String xorString) {


        byte[] bytes = ByteUtils.stringToBytes(checkString);
        byte temp = bytes[0];
        for (int i = 1; i < bytes.length; i++) {
            temp ^= bytes[i];
        }
        return String.format("%02X", temp).equals(xorString);

    }

    public static String decimal2Hex(int decimal) {
        String hex = "";
        String letter;
        int number;
        for (int i = 0; i < 9; i++) {

            number = decimal % 16;
            decimal = decimal / 16;
            switch (number) {

                case 10:
                    letter = "A";
                    break;
                case 11:
                    letter = "B";
                    break;
                case 12:
                    letter = "C";
                    break;
                case 13:
                    letter = "D";
                    break;
                case 14:
                    letter = "E";
                    break;
                case 15:
                    letter = "F";
                    break;
                default:
                    letter = String.valueOf(number);
            }
            hex = letter + hex;
            if (decimal == 0) {
                break;
            }
        }
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        return hex;
    }

    public static String decimal2Hex_2byte(int decimal) {
        String s = decimal2Hex(decimal);
        if (s.length() <= 2) {
            s = "00" + s;
        }
        return s;
    }

    public static String autoPadZero(String hex, int maxLen) {
        StringBuffer stringBuffer = new StringBuffer();
        if (hex.length() < maxLen) {
            stringBuffer.append(hex);
            for (int i = 0; i < maxLen - hex.length(); i++) {
                stringBuffer.append("0");
            }
            return stringBuffer.toString();
        } else if (hex.length() > maxLen) {
            return hex.substring(0, maxLen);
        }
//        return stringBuffer.toString();
        return hex;
    }

    /**
     * 字符串转换为16进制字符串
     */
    public static String stringToHexString(String s) {
        byte[] bytes = s.getBytes();
        String str = "";
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            str += hex.toUpperCase();
        }
        return str;
    }

    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                Logger.d(e.getMessage());
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
        } catch (Exception e1) {
            Logger.d(e1.getMessage());
        }
        return s;
    }

    public static String byteArrayToStr(byte[] buffer) {
        try {
            int length = 0;
            for (int i = 0; i < buffer.length; ++i) {
                if (buffer[i] == 0) {
                    length = i;
                    break;
                }
            }
            return new String(buffer, 0, length, "UTF-8");
        } catch (Exception e) {

            return "";
        }
    }


    /**
     * 将byte[]数组转换成16进制字符。一个byte生成两个字符，长度对应1:2
     *
     * @param bytes，输入byte[]数组
     * @return 16进制字符
     */
    public static String byte2Hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        // 遍历byte[]数组，将每个byte数字转换成16进制字符，再拼接起来成字符串
        for (int i = 0; i < bytes.length; i++) {
            // 每个byte转换成16进制字符时，bytes[i] & 0xff如果高位是0，输出将会去掉，所以+0x100(在更高位加1)，再截取后两位字符
            builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return builder.toString();
    }

    /**
     * 16进制字符串转为字节数组
     */

    public static byte[] hexToByte(String hex) {
        /**
         *先去掉16进制字符串的空格
         */
        hex = hex.replace(" ", "");

        /**
         *字节数组长度为16进制字符串长度的一半
         */
        int byteLength = hex.length() / 2;
        byte[] bytes = new byte[byteLength];
        int m = 0;
        int n = 0;
        for (int i = 0; i < byteLength; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intHex = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
            bytes[i] = (byte) intHex;
        }
        return bytes;

    }

    /**
     * 将int 转为两个字节的byte 小端模式
     *
     * @param value
     * @return
     */
    public static byte[] intToTwoBytes(int value) {
        byte[] src = new byte[2];
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int 转为4个字节的byte 小端模式
     *
     * @param value
     * @return
     */
    public static byte[] intToFourBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }


    /**
     * 将long 转为4个字节的byte 小端模式
     *
     * @param value
     * @return
     */
    public static byte[] longToFourBytes(long value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将4个字节的16进制小端模式转为long
     *
     * @param value
     * @return
     */
    public static long fourByteHexToLong(String value) {
        if (value.length() != 8) {
            return 0;
        }
        String value1 = value.substring(0, 2);
        String value2 = value.substring(2, 4);
        String value3 = value.substring(4, 6);
        String value4 = value.substring(6, 8);
        String newValue = value4 + value3 + value2 + value1;
        return Long.parseLong(newValue, 16);
    }

    /**
     * 将2个字节的16进制小端模式转为int
     *
     * @param value
     * @return
     */
    public static int twoByteHexToInt(String value) {
        if (value.length() != 4) {
            return 0;
        }
        String value1 = value.substring(0, 2);
        String value2 = value.substring(2, 4);
        String newValue =value2 + value1;
        return Integer.parseInt(newValue, 16);
    }

    /**
     * 字符串转换为Ascii
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();

    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     *
     * @param hex Byte字符串(Byte之间无分隔符
     * @return 对应的字符串
     * @author xxs
     */
    public static String hexToAscii(String hex) {
        String hexStr = "";
        String str = "0123456789ABCDEF"; //16进制能用到的所有字符 0-15
        for (int i = 0; i < hex.length(); i++) {
            String s = hex.substring(i, i + 1);
            if (s.equals("a") || s.equals("b") || s.equals("c") || s.equals("d") || s.equals("e") || s.equals("f")) {
                s = s.toUpperCase().substring(0, 1);
            }
            hexStr += s;
        }

        char[] hexs = hexStr.toCharArray();//toCharArray() 方法将字符串转换为字符数组。
        int length = (hexStr.length() / 2);//1个byte数值 -> 两个16进制字符
        byte[] bytes = new byte[length];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * 2;//两个16进制字符 -> 1个byte数值
            n = str.indexOf(hexs[position]) * 16;
            n += str.indexOf(hexs[position + 1]);
            // 保持二进制补码的一致性 因为byte类型字符是8bit的  而int为32bit 会自动补齐高位1  所以与上0xFF之后可以保持高位一致性
            //当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，&0xff可以将高的24位置为0，低8位保持原样，这样做的目的就是为了保证二进制数据的一致性。
            bytes[i] = (byte) (n & 0xff);
        }
        String name = "";
        try {
            name = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Logger.d(e.getMessage());
        }

        return name;
    }


    public static String hexToString(String hex) {
        /*兼容带有\x的十六进制串*/
        hex = hex.replace("\\x", "");
        char[] data = hex.toCharArray();
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("字符个数应该为偶数");
        }
        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f |= toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return new String(out);
    }

    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    //b为传入的字节，i为第几位（范围0-7），如要获取bit0，则i=0
    public static int getBit(byte b, int i) {
        int bit = (int) ((b >> i) & 0x1);
        return bit;
    }

    /**
     * 根据mtu拆包
     *
     * @param userDataHex
     * @param mtu
     * @return
     */
    public static List<byte[]> subAccordToMTU(String userDataHex, int mtu) {
        if (userDataHex == null || userDataHex.isEmpty()) {
            return new ArrayList<>();
        }
        mtu = mtu - 2;
        mtu = mtu * 2;
        int dataNum = userDataHex.length() / mtu + (userDataHex.length() % mtu == 0 ? 0 : 1);

        List<byte[]> dataList = new ArrayList<>();
        for (int i = 0; i < dataNum; i++) {
            String item;
            if (i == dataNum - 1) {
                item = String.format("00%s%s", ByteUtil.decimal2Hex(i), userDataHex.substring(i * mtu));
            } else {
                item = String.format("00%s%s", ByteUtil.decimal2Hex(i), userDataHex.substring(i * mtu, (i + 1) * mtu));
            }
            byte[] bytes = ByteUtils.stringToBytes(item);
            dataList.add(bytes);
        }
        return dataList;
    }


    static String[] hexStr = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    /**
     * 二进制字符转16进制字符
     *
     * @param
     * @return
     */
    public static String binaryToHexString(String binary) {
        //00101000000000000000000111111111111
        int length = binary.length();
        int temp = length % 4;
        // 每四位2进制数字对应一位16进制数字
        // 补足4位
        if (temp != 0) {
            for (int i = 0; i < 4 - temp; i++) {
                binary = "0" + binary;
            }
        }
        // 重新计算长度
        length = binary.length();
        StringBuilder sb = new StringBuilder();
        // 每4个二进制数为一组进行计算
        for (int i = 0; i < length / 4; i++) {
            int num = 0;
            // 将4个二进制数转成整数
            for (int j = i * 4; j < i * 4 + 4; j++) {
                num <<= 1;// 左移
                num |= (binary.charAt(j) - '0');    // 或运算
            }
            // 直接找到该整数对应的16进制
            sb.append(hexStr[num]);
        }
        return sb.toString();
    }

}
