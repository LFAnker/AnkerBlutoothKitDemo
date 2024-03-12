package com.peng.ppscale.business.v4;

import android.security.keystore.KeyProperties;
import android.util.Base64;

import com.peng.ppscale.util.Logger;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理
 * 对原始数据进行AES加密后，在进行Base64编码转化；
 * 正确
 */
public class AesCBC {
    /*已确认
     * 加密用的Key 可以用26个字母和数字组成
     * 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private static String ivParameter = "0000000000000000";//向量iv
    static String encodingFormat = "utf-8";

    private AesCBC() {

    }

    /**
     * 密钥加密
     *
     * @param sSrc
     * @param sKey
     * @return
     */
    public static String encrypt(String sSrc, String sKey) {
        try {
            byte[] bytes = sSrc.getBytes(encodingFormat);
            return encryptHex(bytes, sKey);//此处使用BASE64做转码。
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 数据加密
     *
     * @param bytes
     * @param sKey
     * @return
     */
    public static String encryptHex(byte[] bytes, String sKey) {
        try {
            String byte2Hex1 = ByteUtil.byte2Hex(bytes);
            Logger.d("encrypt byte2Hex = " + byte2Hex1 + " sKey = " + sKey);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            byte[] keyBytes = ByteUtils.stringToBytes(sKey);
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, KeyProperties.KEY_ALGORITHM_AES);
            byte[] ivParameterBytes = ivParameter.getBytes();
            IvParameterSpec iv = new IvParameterSpec(ivParameterBytes);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(bytes);
            String byte2Hex = ByteUtil.byte2Hex(Base64.encode(encrypted, Base64.NO_WRAP));
            Logger.d("encrypt byte2Hex = " + byte2Hex);
            return byte2Hex;//此处使用BASE64做转码。
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 密钥解密
     *
     * @param sSrc
     * @param sKey
     * @return
     */
    public static String decrypt(String sSrc, String sKey) {
        try {
            Logger.d("decrypt sSrc = " + sSrc + " key = " + sKey);
            byte[] sSrcBytes = ByteUtils.stringToBytes(sSrc);

            byte[] original = getDecryptBytes(sKey, sSrcBytes);

            String data = new String(original, encodingFormat);
            Logger.d("decrypt data = " + data);
            return data;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 数据解密
     *
     * @param sSrc
     * @param sKey
     * @return
     */
    public static String decryptHex(String sSrc, String sKey) {
        try {
            Logger.d("decrypt sSrc = " + sSrc + " key = " + sKey);

//            String assciiData = ByteUtil.hexStringToString(sSrc);
//            byte[] encrypted1 = Base64.decode(assciiData, Base64.NO_WRAP);//先用base64解密

            byte[] sSrcBytes = ByteUtil.hexToByte(sSrc);

            byte[] original = getDecryptBytes(sKey, sSrcBytes);

            String byte2Hex = ByteUtil.byte2Hex(original);

            Logger.d("decrypt byte2Hex = " + byte2Hex);
            return byte2Hex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static byte[] getDecryptBytes(String sKey, byte[] sSrcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] keyBytes = ByteUtils.stringToBytes(sKey);
        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, KeyProperties.KEY_ALGORITHM_AES);
        Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] encrypted1 = Base64.decode(sSrcBytes, Base64.NO_WRAP);//先用base64解密
        return cipher.doFinal(encrypted1);
    }


}

