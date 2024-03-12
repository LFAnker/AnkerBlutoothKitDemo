package com.peng.ppscale.business.torre;

import android.text.TextUtils;

import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.DateUtil;
import com.peng.ppscale.util.ImageUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.util.UnitUtil;
import com.peng.ppscale.util.UserUtil;
import com.peng.ppscale.vo.PPUserModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorreHelper {

    public static int normalMtuLen = 244;
    public static int userIdMormalLen = 64;
    public static int memberIdMormalLen = 64;

    public static int SEND_TAG_NORMAL = -1;//默认 此状态才能触发下一步操作，非默认状态不能进行下一步操作
    public static int SEND_TAG_LOG = 1;//读取日志 非默认状态不能进行下一步操作
    public static int SEND_TAG_HISTORY = 2;//默认 非默认状态不能进行下一步操作
    public static int SEND_TAG_WIFI_LIST = 4;//默认 非默认状态不能进行下一步操作
    public static int SEND_TAG_INDEX_ACQUISITION = 6;//指标获取 Index acquisition
    public static int SEND_TAG_USER_LIST = 7;//获取已存储用户ID
    public static int SEND_TAG_MATHCH_USER = 8;//获取设备匹配用户ID
    public static int SEND_TAG_CONFIG_WIFI_INFO = 9;//获取设备配网信息
    public static int SEND_TAG_ALL_HISTORY = 10;//获取设备全部历史数据
    public static int SEND_TAG_USER_HISTORY = 11;//获取设备指定用户的全部历史数据

    public static int CONFIG_WIFI_TAG_SSID = 1;
    public static int CONFIG_WIFI_TAG_PASSWORD = 2;
    public static int CONFIG_WIFI_TAG_DOMAIN = 3;

    //F2发送数据或接收数据的时间，该时间用于过滤掉心跳包，防止在F2交互时由于心跳导致的数据发送异常
    public static long lastSendOrReceiveDataTime = 0;

    public static String getTime(String hex) {
        String time1 = hex.substring(0, 2);
        String time2 = hex.substring(2, 4);
        String time3 = hex.substring(4, 6);
        String time4 = hex.substring(6, 8);

        long timeS = ByteUtil.hexToTenL(time4 + time3 + time2 + time1);

        Date date = new Date();
        date.setTime(timeS * 1000L);
        return DateUtil.formatDatetime(date);
    }

    public static long getTimeL(String hex) {
        String time1 = hex.substring(0, 2);
        String time2 = hex.substring(2, 4);
        String time3 = hex.substring(4, 6);
        String time4 = hex.substring(6, 8);
        long timeS = ByteUtil.hexToTenL(time4 + time3 + time2 + time1) * 1000L;
        return timeS;
    }

    /**
     * 体重转换
     */
    public static double getWeightKg(String reciveData) {
        String weightDataHigh = reciveData.substring(0, 2);
        String weightDataLow = reciveData.substring(2, 4);
        return (double) ByteUtil.hexToTen(weightDataLow + weightDataHigh) / 100.0;
    }

    /**
     * 这个重量是100倍重量
     *
     * @param reciveData
     * @return
     */
    public static int getWeightKgInt(String reciveData) {
        String weightDataHigh = reciveData.substring(0, 2);
        String weightDataLow = reciveData.substring(2, 4);
        return ByteUtil.hexToTen(weightDataLow + weightDataHigh);
    }

    public static String weightKgToHex(double weight) {
        String hex;
        int iWeight = (int) ((weight + 0.005) * 100);
        hex = ByteUtil.hexToLittleEndianMode(ByteUtil.longToHexAndSpecLen(iWeight, 4));
        return hex;
    }

    /**
     * 单位转换
     *
     * @param reciveData
     * @return
     */
    public static PPUnitType getPpUnitType(String reciveData) {
        String scaleUnit = reciveData.substring(12, 14);
        return UnitUtil.unitTorre2PPUnit(ByteUtil.hexToTen(scaleUnit));
    }

    //Impedance example
    public static int getImpedance(String reciveData) {
        String impedanceHigh = reciveData.substring(0, 2);
        String impedanceMid1 = reciveData.substring(2, 4);
        String impedanceMid2 = reciveData.substring(4, 6);
        String impedanceLow = reciveData.substring(6, 8);
        return ByteUtil.hexToTen(impedanceLow + impedanceMid2 + impedanceMid1 + impedanceHigh);
    }

    public static int getHeartRate(String heartRateHex) {
        if (!heartRateHex.equals("00") && !heartRateHex.equals("FF")) {
            return ByteUtil.hexToTen(heartRateHex);
        } else {
            return 0;
        }
    }

    /**
     * @param uid
     * @param memberId
     * @return
     */
    public static String getUseridHex(String uid, String memberId) {
        String uidHex = "";
        if (TextUtils.isEmpty(uid)) {
            uidHex = TorreDeviceManager.normalMemberId;
        } else {
            uidHex = ByteUtil.autoPadZero(ByteUtil.stringToHexString(uid), userIdMormalLen * 2);
        }
        String memberHex;
        if (TextUtils.isEmpty(memberId)) {
            memberHex = TorreDeviceManager.normalMemberId;
        } else {
            memberHex = ByteUtil.autoPadZero(ByteUtil.stringToHexString(memberId), userIdMormalLen * 2);
        }
        return String.format("%s%s", uidHex, memberHex);
    }

    /**
     * 删除用户时使用
     *
     * @param uid
     * @param memberId
     * @return
     */
    public static String getDeleteSendUseridHex(String uid, String memberId) {
        String uidHex = "";
        if (TextUtils.isEmpty(uid)) {
            uidHex = TorreDeviceManager.normalDeleteUId;
        } else {
            uidHex = ByteUtil.autoPadZero(ByteUtil.stringToHexString(uid), userIdMormalLen * 2);
        }
        String memberHex;
        if (TextUtils.isEmpty(memberId)) {
            memberHex = TorreDeviceManager.deleteNormalMemberId;
        } else {
            memberHex = ByteUtil.autoPadZero(ByteUtil.stringToHexString(memberId), userIdMormalLen * 2);
        }
        return String.format("%s%s", uidHex, memberHex);
    }


    static String pattern = "\\.\\d{1}9{3}\\d*"; // 匹配数字 "\\.\\d{1}9{3}\\d*";


    /**
     * 组装用户数据
     *
     * @param userModel
     * @return
     */
    public static String getSyncUserInfoHex(PPUserModel userModel) {
        Logger.d("syncUserInfo 组装用户信息");
        Logger.d("syncUserInfo user  = " + userModel.toString());
        String userIdHex = TorreHelper.getUseridHex(userModel.userID, userModel.memberID);
        Logger.v("syncUserInfo userIdHex = " + userIdHex);
        String ageHex = ByteUtil.decimal2Hex(userModel.age);
        Logger.v("syncUserInfo ageHex = " + ageHex);
        String sexHex = ByteUtil.decimal2Hex(UserUtil.getEnumSex(userModel.sex));
        Logger.v("syncUserInfo sexHex = " + sexHex);
        String heightHex = ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(userModel.userHeight), 4));
        Logger.v("syncUserInfo heightHex = " + heightHex);
        String athleteModeHex = userModel.isAthleteMode ? "01" : "00";
        Logger.v("syncUserInfo athleteModeHex = " + athleteModeHex);
        int weight = (int) ((userModel.weightKg + 0.005) * 100);
        String weightHex = ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(weight), 4));
        Logger.v("syncUserInfo weightHex = " + weightHex);
        String avatarHex = ByteUtil.decimal2Hex(userModel.deviceHeaderIndex);//头像
        Logger.v("syncUserInfo avatarHex = " + avatarHex);
        String targetWeightHex = getTargetWeightHex(userModel.targetWeight);
        Logger.v("syncUserInfo targetWeightHex = " + targetWeightHex);
        String ideaWeightHex = getTargetWeightHex(userModel.ideaWeight);
        Logger.v("syncUserInfo ideaWeightHex = " + ideaWeightHex);
        double[] userWeightArray = userModel.userWeightArray;
        long[] userWeightTimeArray = userModel.userWeightTimeArray;
        int dataLen = Math.min(userWeightArray == null ? 0 : userWeightArray.length, 7);
        String sevenDayDataNum = ByteUtil.decimal2Hex(dataLen);
        Logger.v("syncUserInfo sevenDayDataNum = " + sevenDayDataNum);
        StringBuilder sevenDayData = new StringBuilder();//近七天重量 14字节
        StringBuilder sevenDayDataTimeHex = new StringBuilder();//近七天重量 28字节
        if (userWeightArray != null && dataLen > 0) {
            for (int i = 0; i < dataLen; i++) {
                int iWeight = (int) ((userWeightArray[i] + 0.005) * 100);
                long time = userWeightTimeArray[i] / 1000;
                if (time < 0) {
                    time = System.currentTimeMillis();
                }
                Logger.v("syncUserInfo i:" + i + " iWeight:" + iWeight + " time:" + time);
                sevenDayData.append(ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(iWeight), 4)));
                sevenDayDataTimeHex.append(ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(time), 8)));
            }
        }
        sevenDayData = new StringBuilder(ByteUtil.autoPadZero(sevenDayData.toString(), 28));
        sevenDayDataTimeHex = new StringBuilder(ByteUtil.autoPadZero(sevenDayDataTimeHex.toString(), 56));
        Logger.v("syncUserInfo sevenDayData = " + sevenDayData);
        Logger.v("syncUserInfo sevenDayDataTimeHex = " + sevenDayDataTimeHex);
        String userName = userModel.userName;
        String userNameHexStr = "";
        if (!TextUtils.isEmpty(userName)) {
            String userNameBinaryStr = ImageUtil.bitmapToBinary(userName);
            userNameHexStr = ByteUtil.binaryToHexString(userNameBinaryStr);
        }
        //用户名称字模数据，像素大小为104 * 32，字模大小为800byte，不满800byte剩余数据填0
        String userNickName = ByteUtil.autoPadZero(userNameHexStr, 1600);

        String userDataHex = userIdHex + ageHex + sexHex +
                heightHex + athleteModeHex +
                weightHex + avatarHex + targetWeightHex + ideaWeightHex +
                sevenDayDataNum + sevenDayData + sevenDayDataTimeHex + userNickName;
        Logger.d("syncUserInfo 组装用户信息完成 len = " + userDataHex.length() / 2 + "   " + "userDataHex = " + userDataHex);
        return userDataHex;
    }

    private static String getTargetWeightHex(double userModel) {
        BigDecimal targetWeightA = new BigDecimal(String.valueOf(userModel));
        BigDecimal targetWeightB = new BigDecimal("100");
        BigDecimal targetWeightC = targetWeightA.multiply(targetWeightB);
        int targetWeight = targetWeightC.intValue();
        String targetWeightHex = ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(targetWeight), 4));
        return targetWeightHex;
    }


}
