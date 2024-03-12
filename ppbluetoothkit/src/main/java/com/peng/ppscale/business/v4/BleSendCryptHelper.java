package com.peng.ppscale.business.v4;

import android.text.TextUtils;

import com.inuker.bluetooth.library.utils.ByteUtils;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class BleSendCryptHelper {

    /**
     * 加密
     *
     * @return
     */
    public static List<byte[]> encrypt(byte[] bytes) {
        String encryptHex = BleSendCrypt.getInstance().encrypt(bytes);
        String head = "C6";
        if (!TextUtils.isEmpty(encryptHex)) {
            Logger.d("encryptHex = " + encryptHex);
            List<byte[]> arrayHex = getSubContractBytes(encryptHex, head);
            if (arrayHex != null) return arrayHex;
        }
        return null;
    }

    /**
     * 数据加密
     *
     * @return
     */
    public static List<byte[]> encrypt1(byte[] bytes) {
        String encryptHex = BleSendCrypt.getInstance().encrypt1(bytes);
        String head = "C6";
        if (!TextUtils.isEmpty(encryptHex)) {
            Logger.d("encryptHex = " + encryptHex);
            List<byte[]> arrayHex = getSubContractBytes(encryptHex, head);
            if (arrayHex != null) return arrayHex;
        }
        return null;
    }

    /**
     * 发起鉴权
     *
     * @return
     */
    public static byte[] reqAuthMtu() {
        String appCodeHex = BleSendCrypt.getInstance().getAppCode();
        String head = "C0";
        if (!TextUtils.isEmpty(appCodeHex)) {
            Logger.d("appCodeHex = " + appCodeHex);
            String data = head + appCodeHex;
            Logger.d("准备发送 = " + data);
            return ByteUtils.stringToBytes(data);
        }
        return null;
    }

    /**
     * 发起鉴权
     *
     * @return
     */
    public static List<byte[]> reqAuth() {
        String appCodeHex = BleSendCrypt.getInstance().getAppCode();
        String head = "C0";
        if (!TextUtils.isEmpty(appCodeHex)) {
            Logger.d("appCodeHex = " + appCodeHex);
            List<byte[]> arrayHex = getSubContractBytes(appCodeHex, head);
            if (arrayHex != null) return arrayHex;
        }
        return null;
    }

    /**
     * 发送确认鉴权
     *
     * @return
     */
    public static List<byte[]> authToken() {
        String authCodeHex = BleSendCrypt.getInstance().getAuthToken();
        String head = "C2";
        if (!TextUtils.isEmpty(authCodeHex)) {
            Logger.d("authCodeHex = " + authCodeHex);
            List<byte[]> arrayHex = getSubContractBytes(authCodeHex, head);
            if (arrayHex != null) return arrayHex;
        }
        return null;
    }

    /**
     * 发起鉴权指令
     *
     * @return
     */
    public static List<byte[]> randomKey() {
        String randomKeyHex = BleSendCrypt.getInstance().getRandomKey();
        String head = "C4";
        if (!TextUtils.isEmpty(randomKeyHex)) {
            List<byte[]> arrayHex = getSubContractBytes(randomKeyHex, head);
            if (arrayHex != null) return arrayHex;
        }
        return null;
    }

    /**
     * 分包数据
     *
     * @param appCodeHex 数据源
     * @param head       请求头
     * @return
     */
    private static List<byte[]> getSubContractBytes(String appCodeHex, String head) {
        if (appCodeHex.length() > 0) {
            List<byte[]> arrayHex = new ArrayList<>();
            int len = appCodeHex.length();
            int total = (int) (appCodeHex.length() / 30) + (appCodeHex.length() % 30 > 0 ? 1 : 0);
            int num = 0;
            for (int i = 0; i < total; i++) {
                String dataBody = head
                        + ByteUtil.decimal2Hex(total)
                        + ByteUtil.decimal2Hex(num)
                        + ByteUtil.decimal2Hex(len / 2)
                        + appCodeHex.substring(i * 30, Math.min(len, (i + 1) * 30));

                byte xorValue = ByteUtil.getXorValue(ByteUtils.stringToBytes(dataBody));
                String xorHex = String.format("%02X", xorValue);
                String data = dataBody + xorHex;
                num++;
                arrayHex.add(ByteUtils.stringToBytes(data));
                Logger.d("分包数据 = " + data);
            }
            return arrayHex;
        }
        return null;
    }


}
