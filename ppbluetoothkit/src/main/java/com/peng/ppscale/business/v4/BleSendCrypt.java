package com.peng.ppscale.business.v4;

import android.text.TextUtils;


import com.peng.ppscale.business.v4.auth.AuthenResponse;
import com.peng.ppscale.business.v4.auth.BLEAuthentication;
import com.peng.ppscale.util.Logger;

import java.util.UUID;

public class BleSendCrypt {

    private String secretKey;
    private String newKey;
    private String currentKey;//连接前期是secretKey, 连接后是newKey

    private static volatile BleSendCrypt instance = null;
    private String deviceCodeEncrypt; //设备端生成的UUId
    private String deviceCode;
    private String tokenEncrypt; //总的UUId
    private String dataEncrypt; //蓝牙数据传输
    private String appCode;

    private String authCode; //鉴权token
    OnAuthListener onAuthListener;

    private String data; //用于缓存每条数据，在读取到新数据时清空
    private String headStr;
    private OnEncryptDataAnalysisCallBak onEncryptDataAnalysisCallBak;

    private BleSendCrypt() {
    }

    public static BleSendCrypt getInstance() {
        if (instance == null) {
            synchronized (BleSendCrypt.class) {
                if (instance == null) {
                    instance = new BleSendCrypt();
                }
            }
        }
        return instance;
    }

    public String encrypt(byte[] bytes) {
        if (bytes != null) {
            String byte2Hex = ByteUtil.byte2Hex(bytes);
            Logger.d("byte2Hex = " + byte2Hex);
            String encryptHex = AesCBC.encrypt(byte2Hex, currentKey);
            Logger.d("encryptHex = " + encryptHex);
            return encryptHex;
        }
        return "";
    }

    public String encrypt1(byte[] bytes) {
        if (bytes != null) {
            String encryptHex = AesCBC.encryptHex(bytes, currentKey);
            return encryptHex;
        }
        return "";
    }

    /**
     * 1、APP先將設備藍芽Mac進行md5 hash,會轉換成16byte(128bit)的字串，此字串作為之後加解密的secretKey
     *
     * @param macAddress
     */
    public void createSecretKey(String macAddress) {
        macAddress = macAddress.replace(":", "");
//        macAddress = "493180184a88";
        if (!TextUtils.isEmpty(macAddress)) {
//            byte[] macMd5 = MD5Utils.MD5_12(macAddress);
            byte[] macMd5 = Md5Util.md5Bytes(macAddress);
            secretKey = ByteUtil.byte2Hex(macMd5);
            Logger.d("createSecretKey macAddress = " + macAddress + " secretKey = " + secretKey);
            currentKey = secretKey;
            newKey = "";
        }
    }

    /**
     * 2、
     * ➢ APP產生一組random UUID ，格式為"AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA"，中間四個-隔開，其餘每格亂數產生0-F的字符
     * ➢ APP將亂數UUID用secretKey進行CBC演算法加密，之後再encode成base 64
     * ➢ 將此加密後的字串經由藍芽傳給設備
     * C0
     */
    public String getAppCode() {
        if (!TextUtils.isEmpty(secretKey)) {
            appCode = UUID.randomUUID().toString();
//            appCode = "CCCCCCCC-CCCC-CCCC-CCCC-CCCCCCCCCCCC";
            Logger.d("randomUUID appCode = " + appCode);
            String cryptAppCode = AesCBC.encrypt(appCode, currentKey);
            return cryptAppCode;
        }
        return "";
    }

    /**
     * 解密设备端数据，并合并数据，然后分发逻辑
     * currentKey 连接前期是secretKey, 连接后是newKey
     * <p>
     * 数据分发，拼装完成后的完整的数据
     *
     * @param value
     */
    public void decrypt(byte[] value) {
        String encryptData = String.format("%s", ByteUtils.byteToString(value));
        if (!TextUtils.isEmpty(encryptData)) {
            Logger.e("BleAuth  FFF4 data:" + encryptData);
            String currentHeadStr = encryptData.substring(0, 2);
            switch (currentHeadStr) {
                case "C1":
//                    encryptDeviceCode(encryptData);
                    boolean code = BLEAuthentication.getInstance().encryptDeviceCode(encryptData);
                    if (code) {
                        if (this.onAuthListener != null) {
                            this.onAuthListener.startAuthToken();
                        }
                    }
                    break;
                case "C3":
//                    encryptTokenResponse(encryptData);
                    if (!TextUtils.isEmpty(encryptData) && encryptData.contains("C301000100")) {
                        Logger.d("BleSendCrypt Authentication successful");
                        if (onAuthListener != null) {
                            onAuthListener.onAuthResult(true);
                        }
                    } else {
                        Logger.e("BleSendCrypt Authentication fail");
                        if (onAuthListener != null) {
                            onAuthListener.onAuthResult(false);
                        }
                    }
                    break;
                case "C5":
                    verifyEncryptToken(encryptData);
                    break;
                case "C6":
//                    encryptDataOther(encryptData);
                    String bleData = BLEAuthentication.getInstance().encryptBleData(encryptData);
                    if (bleData != null) {
                        Logger.d("FFF4 bleData:" + bleData);
                        if (onEncryptDataAnalysisCallBak != null) {
                            byte[] bytes = com.peng.ppscale.util.ByteUtil.stringToBytes(bleData);
                            onEncryptDataAnalysisCallBak.onDataAnlysis(bytes);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void encryptDataOther(String encryptData) {
        if (!TextUtils.isEmpty(encryptData)) {
            String dataHex = "";
            String totalStr = encryptData.substring(2, 4);
            int total = ByteUtil.hexToTen(totalStr);
            int packageNum = ByteUtil.hexToTen(encryptData.substring(4, 6));
            String data = encryptData.substring(8, encryptData.length() - 2);
            if (packageNum == 0) {
                this.dataEncrypt = data;
            } else {
                this.dataEncrypt += data;
            }
            if (packageNum == total - 1) {
                dataHex = AesCBC.decryptHex(dataEncrypt, currentKey);
                if (this.onEncryptDataAnalysisCallBak != null) {
//                    this.onEncryptDataAnalysisCallBak.onDataAnlysis(dataHex);
//                    this.onEncryptDataAnalysisCallBak.onDataAnlysis(dataEncrypt);
                }
            }
        }
    }

    /**
     * 3、
     * ➢ 設備產生一組random UUID ，格式為"BBBBBBBB-BBBB-BBBB-BBBB-BBBBBBBBBBBB"，中間四個-隔開，其餘每格亂數產生0-F的字符
     * ➢ 設備將亂數UUID用secretKey進行CBC演算法加密，之後再encode成base 64
     * ➢ 將此加密後的字串經由藍芽傳給APP
     * ➢ 當APP收到後，進行反向解密，decode base64，CBC解密
     * C1
     *
     * @param encryptDeviceCode
     */
    public void encryptDeviceCode(String encryptDeviceCode) {
        if (!TextUtils.isEmpty(encryptDeviceCode)) {
            String totalStr = encryptDeviceCode.substring(2, 4);
            int total = ByteUtil.hexToTen(totalStr);
            int packageNum = ByteUtil.hexToTen(encryptDeviceCode.substring(4, 6));
            String data = encryptDeviceCode.substring(8, encryptDeviceCode.length() - 2);
            if (packageNum == 0) {
                this.deviceCodeEncrypt = data;
            } else {
                this.deviceCodeEncrypt += data;
            }
            if (packageNum == total - 1) {
                this.deviceCode = AesCBC.decrypt(deviceCodeEncrypt, currentKey);
                if (this.onAuthListener != null) {
                    this.onAuthListener.startAuthToken();
                }
            }
        }
    }

    /**
     * 4、
     * ➢ 比對與先前收到的UUID是否一致
     * ➢ 成功回復APP連接成功，否則直接斷開藍芽
     * C2
     *
     * @return
     */
    public String getAuthToken() {
        if (!TextUtils.isEmpty(appCode) && !TextUtils.isEmpty(deviceCode)) {
            authCode = appCode + "_" + deviceCode;
            String cryptCode = AesCBC.encrypt(authCode, currentKey);
            return cryptCode;
        }
        return "";
    }

    /**
     * 5、
     * ➢ 当app发起鉴权收到设备端的响应，并成功解析出device_code字段后；
     * ➢ 把明文的app_code和device_code字段使用"_"合并起来，得到加密前的token字段；
     * ➢ 用aes算法，使用secretKey作为key，加密token，生成加密后的auth_token，发送给设备；
     * C3
     *
     * @param encryptData
     * @return
     */
    public void encryptTokenResponse(String encryptData) {
        if (!TextUtils.isEmpty(encryptData)) {
            String authState = encryptData.substring(8, encryptData.length() - 2);
            if (onAuthListener != null) {
                onAuthListener.onAuthResult(authState.equals("00"));
            }
        }
    }

    /**
     * 6、
     * ➢ app端產生一組random UUID，格式為"CCCCCCCC-CCCC-CCCC-CCCC-CCCCCCCCCCCC"，並用md5 hash得到128bits的key，作為後續資料傳輸的key，本地记录
     * ➢ 將此128bits的key通过secretKey加密和encode 成Base64格式，發送給設備端
     * ➢ 設備端收到app傳來加密後的new_key後，base 64 decode並用secretKey進行解密，解密後的new_key為之後資料傳輸所用的加解密使用
     * C4
     */
    public String getRandomKey() {
        if (!TextUtils.isEmpty(secretKey)) {
            String randomKey = UUID.randomUUID().toString();
//            String randomKey = "2549b8d9-0405-4ddd-9fe6-601117800463";
            Logger.d("randomKey = " + randomKey);
//            byte[] macMd5 = MD5Utils.MD5_12(randomKey);
            String cryptNewKey = AesCBC.encrypt(randomKey, currentKey);
            byte[] macMd5 = Md5Util.md5Bytes(randomKey);
            newKey = ByteUtil.byte2Hex(macMd5);
            currentKey = newKey;
            return cryptNewKey;
        }
        return "";
    }

    /**
     * 7、
     * ➢ 設備端收到app傳來加密後的new_key後，base 64 decode並用secretKey進行解密，解密後的new_key為之後資料傳輸所用的加解密使用。
     * ➢ 設備端將先前鑒權碼，”AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA_BBBBBBBB-BBBB-BBBB-BBBB-BBBBBBBBBBBB”，使用new_key進行加密，並encode成base 64，傳送給app端
     * ➢ APP端進行解密，並比對與(3)所拿到的是否一致，若不一致則斷開藍芽
     * C5
     *
     * @param encryptTokenData
     */
    public void verifyEncryptToken(String encryptTokenData) {
        if (!TextUtils.isEmpty(encryptTokenData)) {
            String token = "";
            String totalStr = encryptTokenData.substring(2, 4);
            int total = ByteUtil.hexToTen(totalStr);
            int packageNum = ByteUtil.hexToTen(encryptTokenData.substring(4, 6));
            String data = encryptTokenData.substring(8, encryptTokenData.length() - 2);
            if (packageNum == 0) {
                this.tokenEncrypt = data;
            } else {
                this.tokenEncrypt += data;
            }

            if (packageNum == total - 1) {
                Logger.d("tokenEncrypt = " + tokenEncrypt);
                token = AesCBC.decrypt(tokenEncrypt, currentKey);
                Logger.d("token = " + token + " authCode = " + authCode);
                if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(authCode)) {
                    if (onAuthListener != null) {
                        onAuthListener.onAuthKeyResult(token.equalsIgnoreCase(authCode));
                    }
                }
            }
        }

//        }
    }

    public void setOnAuthListener(OnAuthListener onAuthListener) {
        this.onAuthListener = onAuthListener;
    }

    public void setOnEncryptDataAnalysisCallBak(OnEncryptDataAnalysisCallBak onEncryptDataAnalysisCallBak) {
        this.onEncryptDataAnalysisCallBak = onEncryptDataAnalysisCallBak;
    }

    public interface OnAuthListener {

        void startAuthToken();

        /**
         * 连接成功与否
         *
         * @param isSuccess
         */
        void onAuthResult(boolean isSuccess);

        /**
         * 密钥核对成功与否
         *
         * @param isSuccess
         */
        void onAuthKeyResult(boolean isSuccess);

    }

    public interface OnEncryptDataAnalysisCallBak {
        void onDataAnlysis(byte[] data);
    }

}
