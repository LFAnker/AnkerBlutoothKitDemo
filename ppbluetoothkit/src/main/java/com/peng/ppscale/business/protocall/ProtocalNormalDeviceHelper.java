package com.peng.ppscale.business.protocall;

import static com.peng.ppscale.vo.PPScaleDefineKt.LF_X_L_16;
import static com.peng.ppscale.vo.PPScaleDefineKt.LF_X_L_17;
import static com.peng.ppscale.vo.PPScaleDefineKt.LF_X_L_20;

import com.peng.ppscale.business.ble.PPScaleHelper;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.util.UnitUtil;
import com.peng.ppscale.vo.PPBodyBaseModel;
import com.peng.ppscale.vo.PPBodyFatModel;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;
import com.peng.ppscale.vo.PPUserModel;
import com.peng.ppscale.calcute.CalculateHelper4;

import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ProtocalNormalDeviceHelper {

    //weight
    public static double getWeightKg(String reciveData) {
        String weightDataHigh = reciveData.substring(6, 8);
        String weightDataLow = reciveData.substring(8, 10);
        return (double) ByteUtil.hexToTen(weightDataLow + weightDataHigh) / 100.0;
    }

    //weight
    public static int getWeightG(String reciveData) {
        String weightDataHigh = reciveData.substring(6, 8);
        String weightDataLow = reciveData.substring(8, 10);
        return (int) ByteUtil.hexToTen(weightDataLow + weightDataHigh);
    }

    //Impedance example
    public static int getImpedance(String reciveData) {
        try {
            String impedanceHigh = reciveData.substring(10, 12);
            String impedanceMid = reciveData.substring(12, 14);
            String impedanceLow = reciveData.substring(14, 16);
            return ByteUtil.hexToTen(impedanceLow + impedanceMid + impedanceHigh);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @param reciveData
     * @param startLen   截取7个字节的时间的起始位置， 根据不同的协议起始位置不同，
     * @return
     */
    public static String getV4Clock(String reciveData, int startLen) {
        String dateStr = reciveData.substring(startLen, startLen + 14);
        int year = ByteUtil.hexToTen(dateStr.substring(0, 2) + dateStr.substring(2, 4));
        int mounth = ByteUtil.hexToTen(dateStr.substring(4, 6));
        int day = ByteUtil.hexToTen(dateStr.substring(6, 8));
        int hour = ByteUtil.hexToTen(dateStr.substring(8, 10));
        int minite = ByteUtil.hexToTen(dateStr.substring(10, 12));
        int secound = ByteUtil.hexToTen(dateStr.substring(12, 14));

        // 设备信息
        String clock = year + "-";
        if (mounth < 10) clock += "0";
        clock += mounth + "-";
        if (day < 10) clock += "0";
        clock += day + " ";
        if (hour < 10) clock += "0";
        clock += hour + ":";
        if (minite < 10) clock += '0';
        clock += minite + ":";
        if (secound < 10) clock += '0';
        clock += secound;
        Logger.d("时间转换前： clock：" + clock);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = dateFormat.parse(clock);
            // 时间戳转日期字符串
            String pattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            clock = sdf.format(date);
            Logger.d("时间转换后： clock：" + clock);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return clock;
    }

    public static String getClock(String reciveData, PPDeviceModel deviceModel) {
        int year = ByteUtil.hexToTen(reciveData.substring(22, 24) + reciveData.substring(24, 26));
        int mounth = ByteUtil.hexToTen(reciveData.substring(26, 28));
        int day = ByteUtil.hexToTen(reciveData.substring(28, 30));
        int hour = ByteUtil.hexToTen(reciveData.substring(30, 32));
        int minite = ByteUtil.hexToTen(reciveData.substring(32, 34));
        int secound = ByteUtil.hexToTen(reciveData.substring(34, 36));

        // 设备信息
        String clock = year + "-";
        if (mounth < 10) clock += "0";
        clock += mounth + "-";
        if (day < 10) clock += "0";
        clock += day + " ";
        if (hour < 10) clock += "0";
        clock += hour + ":";
        if (minite < 10) clock += '0';
        clock += minite + ":";
        if (secound < 10) clock += '0';
        clock += secound;
        if (deviceModel != null
                && deviceModel.deviceProtocolType == PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeV2
                && PPScaleHelper.INSTANCE.isFuncTypeWifi(deviceModel.deviceFuncType)) {
            Logger.d("时间转换前： clock：" + clock);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date date = dateFormat.parse(clock);
                // 时间戳转日期字符串
                String pattern = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                clock = sdf.format(date);
                Logger.d("时间转换后： clock：" + clock);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
//        //yyy-MM-dd HH:mm:ss
//        if (ConnectManager.getInstance().isUtc0()) {
//            long utcTimes = DateUtil.stringToLong(clock);
//            long differens = DateUtil.diffSeconds();
//            Logger.d("UTC-0 拿到的秤端历史时间：clock = " + clock + " utcTimes = " + utcTimes + " 时区差值： differens = " + differens);
//            Date date = new Date();
//            date.setTime(utcTimes + differens);
//            clock = DateUtil.formatDatetime(date);
//            Logger.d("UTC-0 转换成当前手机时间： time = " + date.getTime() + " clock = " + clock);
//        }
        return clock;
    }

    public static PPUnitType getPpUnitType(String reciveData, PPDeviceModel deviceModel) {
        String scaleUnit = reciveData.substring(16, 18);
        return UnitUtil.getUnitType(ByteUtil.hexToTen(scaleUnit));
    }

    public static String unPack(String reciveData) {
        //18字节历史数据
        //CF 0000 DC0500000000 00 16 07E2090E0E3A28
        //秤端计算
        //CF 041EAA 0202009C16018702026A06DC0F

        //CF 9015 960A00045000 00 82

        String startStr11 = reciveData.substring(0, 18);
        String endStr11 = reciveData.substring(22, 40);
        if (startStr11.contains(endStr11)) {
            return reciveData.substring(0, 22);
        }
        String startStr18 = reciveData.substring(0, 4);
        String endStr18 = reciveData.substring(36, 40);
        if (startStr18.contains(endStr18)) {
            return reciveData.substring(0, 36);
        }
        return reciveData;
    }

    /**
     * 秤端计算
     *
     * @param reciveData
     * @param userModel
     * @param deviceModel
     * @return
     */
    @Nullable
    public static PPBodyFatModel getCalcuteInScaleBodyFatModel(String reciveData, PPUserModel userModel, PPDeviceModel deviceModel) {
//        reciveData = "CF0012B400E800321300CA01028C0397D517D100";
        //CF0012B400E800321300CA01028C03970D54B500
        String weightDataLow = reciveData.substring(8, 10);
        String weightDataHigh = reciveData.substring(10, 12);
//            if ((weightDataHigh + weightDataLow).equalsIgnoreCase("FFFF")) return;
        final float weightKg = ByteUtil.hexToTen(weightDataLow + weightDataHigh) / 10.0f;

        float bmi = (float) (weightKg / Math.pow((userModel.userHeight / 100.0f), 2.0f));

        String fatDataLow = reciveData.substring(12, 14);
        String fatDataHigh = reciveData.substring(14, 16);
        float bodyfatPercentage = ByteUtil.hexToTen(fatDataLow + fatDataHigh) / 10.0f;

        String boneData = reciveData.substring(16, 18);
        float boneKg = ByteUtil.hexToTen(boneData) / 10.0f;

        String muscleDataLow = reciveData.substring(18, 20);
        String muscleDataHigh = reciveData.substring(20, 22);
        float muscleKg = ByteUtil.hexToTen(muscleDataLow + muscleDataHigh) / 10.0f;

        String VFLData = reciveData.substring(22, 24);
        int VFL = ByteUtil.hexToTen(VFLData);

        String waterPercentageDataLow = reciveData.substring(24, 26);
        String waterPercentageDataHigh = reciveData.substring(26, 28);
        float waterPercentage = ByteUtil.hexToTen(waterPercentageDataLow + waterPercentageDataHigh) / 10.0f;

        String BMRDataLow = reciveData.substring(28, 30);
        String BMRDataHigh = reciveData.substring(30, 32);
        int BMR = ByteUtil.hexToTen(BMRDataLow + BMRDataHigh);
        if (weightKg <= 0) return null;
        int impedance = 0;
        if (reciveData.length() == 40 &&
                deviceModel.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeInScale) {
            StringBuffer buffer = new StringBuffer();
            String impedanceHigh = reciveData.substring(32, 34);
            String impedanceMid = reciveData.substring(34, 36);
            String impedanceLow = reciveData.substring(36, 38);
            buffer.append(impedanceLow);
            buffer.append(impedanceMid);
            buffer.append(impedanceHigh);
            impedance = ByteUtil.hexToTen(buffer.toString());
        }
        PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
        bodyBaseModel.weight = (int) (weightKg * 100);
        bodyBaseModel.impedance = impedance;
        bodyBaseModel.userModel = userModel;
        bodyBaseModel.deviceModel = deviceModel;
        bodyBaseModel.unit = PPUnitType.Unit_KG;

        PPBodyFatModel bodyFatModel = new PPBodyFatModel(bodyBaseModel);
//        final PPBodyFatModel bodyFatModel = new PPBodyFatModel(weightKg, impedance, userModel, deviceModel, PPUnitType.Unit_KG);
        bodyFatModel.setPpBMI(bmi);
        bodyFatModel.setPpFat(bodyfatPercentage);
        bodyFatModel.setPpBoneKg(boneKg);
        bodyFatModel.setPpMuscleKg(muscleKg);
        bodyFatModel.setPpVisceralFat(VFL);
        bodyFatModel.setPpWaterPercentage(waterPercentage);
        bodyFatModel.setPpBMR(BMR);
        if (impedance > 0) {
            bodyFatModel.setPpSDKVersion(LF_X_L_20);
        } else {
            bodyFatModel.setPpSDKVersion(LF_X_L_16);
        }
        CalculateHelper4.INSTANCE.calcuteTypeInScale(bodyFatModel);
        Logger.d("liyp_ " + bodyFatModel.toString());
        return bodyFatModel;
    }


    /**
     * @param reciveData
     * @param userModel
     * @param deviceModel
     * @return
     */
    @Nullable
    public static PPBodyFatModel get17BodyFatModel(String reciveData, PPUserModel userModel, PPDeviceModel deviceModel) {
        String weightDataLow = reciveData.substring(8, 10);
        String weightDataHigh = reciveData.substring(10, 12);
        final float weightKg = ByteUtil.hexToTen(weightDataLow + weightDataHigh) / 10.0f;

        float bmi = (float) (weightKg / Math.pow((userModel.userHeight / 100.0f), 2.0f));

        String fatDataLow = reciveData.substring(12, 14);
        String fatDataHigh = reciveData.substring(14, 16);
        float bodyfatPercentage = ByteUtil.hexToTen(fatDataLow + fatDataHigh) / 10.0f;

        String boneData = reciveData.substring(16, 18);
        float boneKg = ByteUtil.hexToTen(boneData) / 10.0f;

        String muscleDataLow = reciveData.substring(18, 20);
        String muscleDataHigh = reciveData.substring(20, 22);
        float muscleKg = ByteUtil.hexToTen(muscleDataLow + muscleDataHigh) / 10.0f;

        String VFLData = reciveData.substring(22, 24);
        int VFL = ByteUtil.hexToTen(VFLData);

        String waterPercentageDataLow = reciveData.substring(24, 26);
        String waterPercentageDataHigh = reciveData.substring(26, 28);
        float waterPercentage = ByteUtil.hexToTen(waterPercentageDataLow + waterPercentageDataHigh) / 10.0f;

        String BMRDataLow = reciveData.substring(28, 30);
        String BMRDataHigh = reciveData.substring(30, 32);
        int BMR = ByteUtil.hexToTen(BMRDataLow + BMRDataHigh);

        String bodyAgeData = reciveData.substring(32, 34);
        int bodyAge = ByteUtil.hexToTen(bodyAgeData);

        if (weightKg <= 0) return null;
        PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
        bodyBaseModel.weight = (int) (weightKg * 100);
        bodyBaseModel.impedance = 0;
        bodyBaseModel.userModel = userModel;
        bodyBaseModel.deviceModel = deviceModel;
        bodyBaseModel.unit = PPUnitType.Unit_KG;
        PPBodyFatModel bodyFatModel = new PPBodyFatModel(bodyBaseModel);
//        final PPBodyFatModel bodyFatModel = new PPBodyFatModel(weightKg, impedance, userModel, deviceModel, PPUnitType.Unit_KG);
        bodyFatModel.setPpBMI(bmi);
        bodyFatModel.setPpFat(bodyfatPercentage);
        bodyFatModel.setPpBoneKg(boneKg);
        bodyFatModel.setPpMuscleKg(muscleKg);
        bodyFatModel.setPpVisceralFat(VFL);
        bodyFatModel.setPpWaterPercentage(waterPercentage);
        bodyFatModel.setPpBMR(BMR);
        bodyFatModel.setPpBodyAge(bodyAge);
        bodyFatModel.setPpSDKVersion(LF_X_L_17);
        CalculateHelper4.INSTANCE.calcuteTypeInScale(bodyFatModel);
        Logger.d("liyp_ " + bodyFatModel.toString());
        return bodyFatModel;
    }


}
