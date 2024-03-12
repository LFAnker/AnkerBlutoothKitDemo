package com.peng.ppscale.business.v4.auth;


import android.text.TextUtils;


import com.peng.ppscale.business.v4.ByteUtil;
import com.peng.ppscale.business.v4.ByteUtils;
import com.peng.ppscale.util.Logger;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * Copyright (C), 2017-2021, Anker
 * FileName: BLEAuthentication
 *
 * @author ken.luo
 * Date: 2021/10/13 20:51
 * Description: 蓝牙鉴权
 * History:
 * 创建者 : ken.luo
 * <author>     <time>     <version>      <desc>
 * 作者姓名      修改时间      版本号          描述
 */
public class BLEAuthentication {

    private static class SingletonHolder {
        private static final BLEAuthentication INSTANCE = new BLEAuthentication();
    }

    public static BLEAuthentication getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public BLEAuthentication() {

    }


//    public static void main(String[] args) {
////        AuthenRequest.getInstance().bleMac = "";
////        BLEAuthentication bean = new BLEAuthentication(authenRequest);
////        bean.startAuthen();
//
//        // 测试异或
//        byte data = DataTansforHelper.getXor("123789456".getBytes());
//
//        System.out.println(Arrays.toString("AGDE1REDF3G5rerwerDG".getBytes()));
//
//        System.out.println(data);
//
//    }

    public boolean needAuth;

    /**
     * 开始执行鉴权操作
     *
     * @param needAuth true:需要授权
     */
    public void startAuthen(boolean needAuth) {
        Logger.d("开始进行鉴权  =======================" + System.currentTimeMillis());
        this.needAuth = needAuth;
        if (needAuth) {
            step1CreateSecretKey();
            step3RandomUuidEncrypt();
            step4GenerateCommodList();
        }
    }

    public void throwStateException() {
        throw new IllegalStateException("data illegal");
    }

    /**
     * APP先将设备的蓝牙Mac进行md5 hash，会转换成16byte(128bit)的数据，此数据作为之后加密的secretKey
     */
    public void step1CreateSecretKey() {
        //设备蓝牙Mac
        String bleMac = AuthenRequest.getInstance().bleMac;
        Logger.d("BleAuth  开始鉴权 蓝牙地址 =" + AuthenRequest.getInstance().bleMac);
        if (bleMac == null) {
            throwStateException();
            return;
        }
        String data = bleMac.replace(":", "");
        String result = MyTextUtils.getMD5(data.toUpperCase());
        if (result != null) {
            AuthenResponse.getInstance().secretKey = result.toLowerCase();
        }
        Logger.d("BleAuth  开始鉴权 secretKey =" + AuthenResponse.getInstance().secretKey);
    }


    /**
     * APP生成一组random UUID字符，"AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA"，
     * 中间有四个 '-' 隔开，其中每个字符的取值为 '0' ~ '9' , 'A' ~ 'F' 字符（共36个字节）
     * RandomUuid 加密
     */
    public void step3RandomUuidEncrypt() {
        try {
            AuthenResponse.getInstance().randomUuid = getUUID();
            String data = AuthenResponse.getInstance().randomUuid;
//            String data = "CCCCCCCC-CCCC-CCCC-CCCC-CCCCCCCCCCCC";
            String key = AuthenResponse.getInstance().secretKey;

            Logger.d(" C0 开始鉴权 randomUuid =" + data + "   secretKey=" + key);

//            String encryptRandomUuid = LefuEncryptUtil.encrypt(data, key, LefuConstants.iv);
            String encryptRandomUuid = AesCBC.encrypt(data, key);
            AuthenResponse.getInstance().encryptRandomUuid = encryptRandomUuid;
            Logger.d("开始鉴权 encryptRandomUuid =" + encryptRandomUuid);
        } catch (Exception e) {
            Logger.d(e.getMessage());
        }
    }


    /**
     * 第三步发起鉴权
     * APP发起新鉴权KEY
     * 18~21
     */
    public void thirdAuth() {
        AuthenResponse.getInstance().C5Uuid = "";
        AuthenResponse.getInstance().encryptC5Uuid = "";
        String uuid = getUUID();
        AuthenResponse.getInstance().appNewKey = MyTextUtils.getMD5(uuid);

        String key = AuthenResponse.getInstance().secretKey;
        String encryptRandomUuid = AesCBC.encrypt(uuid, key);
        if (!TextUtils.isEmpty(encryptRandomUuid) && encryptRandomUuid != null) {
            AuthenResponse.getInstance().commondThirdList = getSubContractBytes(encryptRandomUuid, "C4");
        }
    }

    public String getUUID() {
        String uuid = UUID.randomUUID().toString().toLowerCase();
        return uuid.substring(0, 15);
    }

    public void authAgain() {
        try {
            String data = AuthenResponse.getInstance().getComposeUuid();
            String key = AuthenResponse.getInstance().secretKey;
            Logger.d("BleAuth  开始鉴权 randomUuid =" + data + "   secretKey=" + key);
//            String encryptRandomUuid = LefuEncryptUtil.encrypt(data, key, LefuConstants.iv);
            String encryptRandomUuid = AesCBC.encrypt(data, key);
            AuthenResponse.getInstance().encryptComposeUuid = encryptRandomUuid;
            AuthenResponse.getInstance().commondComposeList = getSubContractBytes(AuthenResponse.getInstance().encryptComposeUuid, "C2");

            Logger.d("BleAuth  再次鉴权 encryptComposeUuid =" + encryptRandomUuid);
        } catch (Exception e) {
            Logger.d(e.getMessage());
        }
    }


    /**
     * RandomUuid 加密
     */
    public void step4GenerateCommodList() {
        AuthenResponse.getInstance().commondList = getSubContractBytes(AuthenResponse.getInstance().encryptRandomUuid, "C0");

//        byte[] commondData = AuthenResponse.getInstance().encryptRandomUuid.getBytes();
//        List<byte[]> commondList = new ArrayList<>();
//
////        String temp = "RRRRRTAAAAAAAAAAA";
////        byte[] commondData = temp.getBytes();
//        //数据包count
//        int yu = commondData.length / 15;
//        int shang = commondData.length % 15;
//        int count = 0;
//        if (shang > 0) {
//            count = yu + 1;
//        }
//
//
//        int laveLeng = commondData.length;
//        for (int i = 0; i < count; i++) {
//            laveLeng -= 15;
//            // 有效数据数组长度
//            byte arrLength = 0;
//            if (laveLeng < 0) {
//                arrLength = (byte) (15 + laveLeng);
//            } else {
//                arrLength = 15;
//                byte[] data = {(byte) 0xC0, (byte) count, (byte) i, (byte) (15)};
//            }
//
//            // 有效数据
//            byte[] appCode = new byte[arrLength];
//            System.arraycopy(commondData, i * 15, appCode, 0, arrLength);
//
//            byte[] data = new byte[5 + arrLength];
//            data[0] = (byte) 0xC0;
//            data[1] = (byte) count;
//            data[2] = (byte) i;
//            data[3] = (byte) 0xC0;
//            System.arraycopy(appCode, 0, data, 4, appCode.length);
//
//            byte[] xorData = new byte[4 + arrLength];
//            System.arraycopy(data, 0, xorData, 0, data.length - 1);
//            byte check = DataTansforHelper.getXor(xorData);
//            data[data.length - 1] = check;
//
//            commondList.add(data);
//        }
//
//
//
//
//        AuthenResponse.getInstance().commondList = commondList;

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


    private int getRandomIndex() {
        return (int) (Math.random() * 26 % 26);
    }

    /**
     * 获取数据成功
     */
    public boolean encryptDeviceCode(String encryptDeviceCode) {
        if (!TextUtils.isEmpty(encryptDeviceCode)) {
            String totalStr = encryptDeviceCode.substring(2, 4);
            int total = ByteUtil.hexToTen(totalStr);
            int packageNum = ByteUtil.hexToTen(encryptDeviceCode.substring(4, 6));
            String data = encryptDeviceCode.substring(8, encryptDeviceCode.length() - 2);
            if (packageNum == 0) {
                AuthenResponse.getInstance().encryptDeviceUuid = data;
            } else {
                AuthenResponse.getInstance().encryptDeviceUuid += data;
            }
            if (packageNum == total - 1) {
                AuthenResponse.getInstance().deviceUuid = AesCBC.decrypt(AuthenResponse.getInstance().encryptDeviceUuid, AuthenResponse.getInstance().secretKey);
                BLEAuthentication.getInstance().authAgain();

                Logger.d("BleAuth  deviceUuid=" + AuthenResponse.getInstance().deviceUuid);
                return true;
            }
        }
        return false;
    }

    /**
     * 解析第三次 C5返回的数据
     */
    public boolean encryptDeviceThird(String encryptDeviceCode) {
        if (!TextUtils.isEmpty(encryptDeviceCode)) {
            String totalStr = encryptDeviceCode.substring(2, 4);
            int total = ByteUtil.hexToTen(totalStr);
            int packageNum = ByteUtil.hexToTen(encryptDeviceCode.substring(4, 6));
            String data = encryptDeviceCode.substring(8, encryptDeviceCode.length() - 2);
            if (packageNum == 0) {
                AuthenResponse.getInstance().encryptC5Uuid = data;
            } else {
                AuthenResponse.getInstance().encryptC5Uuid += data;
            }
            if (packageNum == total - 1) {
                String sourceData = AuthenResponse.getInstance().encryptC5Uuid;

                AuthenResponse.getInstance().C5Uuid = AesCBC.decrypt(AuthenResponse.getInstance().encryptC5Uuid, AuthenResponse.getInstance().appNewKey);
                String d1 = AuthenResponse.getInstance().getComposeUuid();
                String d2 = AuthenResponse.getInstance().C5Uuid;

                if (d1.equalsIgnoreCase(d2)) {
                    Logger.d("鉴权成功  ==" + System.currentTimeMillis());
                } else {
                    Logger.d("鉴权失败  ==" + System.currentTimeMillis());
                }

                Logger.d("旧 UUID=" + d1 + "新 UUID=" + d2);

                return true;
            }
        }
        return false;
    }

    /**
     * 获取蓝牙返回数据,并进行拼接解析
     */
    public String encryptBleData(String encryptDeviceCode) {
        if (!TextUtils.isEmpty(encryptDeviceCode)) {
            String totalStr = encryptDeviceCode.substring(2, 4);
            int total = ByteUtil.hexToTen(totalStr);
            int packageNum = ByteUtil.hexToTen(encryptDeviceCode.substring(4, 6));
            String data = encryptDeviceCode.substring(8, encryptDeviceCode.length() - 2);
            if (packageNum == 0) {
                AuthenRequest.getInstance().reciverTempC6 = data;
            } else {
                AuthenRequest.getInstance().reciverTempC6 += data;
            }
            if (packageNum == total - 1) {
//                String result = AesCBC.decryptHex(AuthenRequest.getInstance().reciverTempC6, AuthenResponse.getInstance().appNewKey);
                String result = AuthenRequest.getInstance().reciverTempC6;
                AuthenRequest.getInstance().reciverTempC6 = "";
                return result;
            }
        }
        return null;
    }
}
