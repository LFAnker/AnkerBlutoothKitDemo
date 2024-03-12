package com.peng.ppscale.business.protocall;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.peng.ppscale.business.ble.listener.BleDataStateInterface;
import com.peng.ppscale.business.ble.listener.PPDataChangeListener;
import com.peng.ppscale.business.ble.listener.PPDeviceInfoInterface;
import com.peng.ppscale.business.ble.listener.PPHistoryDataInterface;
import com.peng.ppscale.business.ble.listener.ProtocalFilterImpl;
import com.peng.ppscale.business.ble.send.BleSendDelegate;
import com.peng.ppscale.business.device.DeviceManager;
import com.peng.ppscale.business.device.PPDeviceType;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.business.torre.ProtocalTorreDeviceHelper;
import com.peng.ppscale.business.torre.TorreDelegate;
import com.peng.ppscale.business.v4.ProtocalV4DataHelper;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.DateUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.util.UnitUtil;
import com.peng.ppscale.vo.PPBodyBaseModel;
import com.peng.ppscale.vo.PPBodyFatModel;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;
import com.peng.ppscale.vo.PPUserModel;

public class ProtocalDelegate {

    private final String TAG = ProtocalDelegate.class.getSimpleName();

    private final Handler handler;
    //    private PPProcessDateInterface processDateInterface;
//    private PPLockDataInterface lockDataInterface;
    private PPDataChangeListener dataChangeListener;
    private PPHistoryDataInterface historyDataInterface;
    private BleDataStateInterface bleDataStateInterface;
    private PPDeviceInfoInterface deviceInfoInterface;
    private PPUserModel userModel;
    private String lastReciveData = "";
    private long lastTimes = 0;//用于过滤相同数据，但是又不是同一组数据3s之外
    private String lastReciveData11 = "";
    private String lastHistoryData;//异常数据缓存
    private float lockWeightKg;
    private long lockImpedance;
    int myWeightKgInt;
    long myImpedance;

    public static long ABNORMAL_HISTORY_INTERVAL_TIME = 1577808000000L;//历史数据处理2020-01-01之前老数据，起因秤端会有出厂时间下的历史数据，App过滤
    public static long ABNORMAL_HISTORY_INTERVAL_TIME_TORRE = 1675353600000L;//历史数据处理2023-02-03之前老数据，起因秤端会有出厂时间下的历史数据，App过滤

    public ProtocalDelegate() {
        handler = new Handler(Looper.getMainLooper());
        myImpedance = 0;
        myWeightKgInt = 0;
    }

    public void protocoFilter(String reciveData, PPDeviceModel deviceModel) {
        if (!lastReciveData.equals(reciveData) || System.currentTimeMillis() - lastTimes > 2500) {
            lastTimes = System.currentTimeMillis();
            Logger.d("lastReciveData---------  " + lastReciveData + " reciveData---------  " + reciveData);
            lastReciveData = reciveData;
            analyticalData(reciveData, deviceModel);
        }
    }

    public void setSerialNumber(PPDeviceModel deviceModel) {
        if (deviceInfoInterface != null) {
//            deviceInfoInterface.serialNumber(deviceModel);
        }
    }

    public void setModelNumber(PPDeviceModel deviceModel) {
        if (deviceInfoInterface != null) {
            deviceInfoInterface.readDeviceInfoComplete(deviceModel);
        }
    }

    public void protocoBMDJFilter(byte[] value) {
        final String reciveData = ByteUtil.byteToString(value);
        if (!lastReciveData.equals(reciveData)) {
            lastReciveData = reciveData;
            Logger.d("bmdj = " + reciveData);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ProtocalBMDJDeviceHelper.getInstance().bmScaleStatusProtocol(reciveData);
                }
            });
        }
    }

    //HeartRateScale
    //CF 5CC0 000000000000 00 53  //C05C > 12000 说明是心率秤
    //CF FE95 0D079E96E700 00 41
    //CF 0000 0D0700000000 01 C4
    //18字节历史数据
    //CF 5CC0 000000000000 00 53 07E2090E0E3A28
    //CF 0000 DC0500000000 00 16 07E2090E0E3A28

    //HealthScale
    //CFE821 0A0ACFCA1000 00 13
    //CF0000 140a00000000 01 D0

    //Adore
    //CF A816 1C071314D500 00 B8
    //CF 0000 1C0700000000 01 D5
    //18字节历史数据
    //CF 0000 DC0500000000 00 16 07E2090E0E3A28

    //EnergyScale
    //CF9015 960A00045000 00 82
    //CF0000 960A00000000 01 52

    //BMScale
    //CFDA16 DA07ACA74C00 00 99
    //CF0000 DA0700000000 01 13

    //ElectronicScale
    //CF 0399AA 011C00320A010001035205C5
    //send
    //FE000000B41901AC

    private void analyticalData(String reciveData, PPDeviceModel deviceModel) {
        Logger.d(TAG + " analyticalData () reciveData = " + reciveData);
        //粘包
        if (reciveData.length() == 40) {
            reciveData = ProtocalNormalDeviceHelper.unPack(reciveData);
        }
        //11字节
        if (reciveData.equals("F200")) {
            //历史数据结束标志
            clearHistoryData(deviceModel);
        } else if (reciveData.equals("F100")) {

        }
        if (!reciveData.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)
            && !reciveData.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE)) {
            return;
        }
        Logger.d(TAG + " analyticalData reciveData len = " + reciveData.length());
        if (reciveData.length() == 22) {
//            if (lastReciveData11 == null || lastReciveData11.isEmpty() || !lastReciveData11.equals(reciveData)) {
//                lastReciveData11 = reciveData;
                if (deviceModel.deviceProtocolType == PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeV2) {
                    analyticalDataFat(reciveData, deviceModel);
                } else {
                    analyticalDataFatV3(reciveData, deviceModel);
                }
//            }
//        } else if (reciveData.length() == 32 && deviceModel.deviceCalcuteType
//                == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeInScale) {
//            //秤端计算
//            analysisDataCalcuteInScale(reciveData, deviceModel);
        } else if (reciveData.length() == 34) {
            analysisData17CalcuteInScale(reciveData, deviceModel);
//        } else if (reciveData.length() == 40 &&
//                DeviceManager.DeviceList.DeviceListCalcuteInScale.contains(deviceModel.getDeviceName())) {
//            analysisDataCalcuteInScale(reciveData, deviceModel);
        } else if (reciveData.length() == 36 || reciveData.length() == 40) {
            //18字节历史数据
            analysisDataHistory(reciveData, deviceModel);
        }

    }

    public void wifiBodyDataAnalyticalData(String reciveData, PPDeviceModel deviceModel) {
        Logger.d("wifiBodyDataAnalyticalData lastReciveData:  " + lastReciveData + " reciveData: " + reciveData);
        if (!lastReciveData.equals(reciveData) || System.currentTimeMillis() - lastTimes > 2500) {
            lastTimes = System.currentTimeMillis();
            lastReciveData = reciveData;
            //11字节
            if (reciveData.equals("F200")) {
                //历史数据结束标志
                clearHistoryData(deviceModel);
            } else if (reciveData.equals("F100")) {

            } else if (reciveData.length() == 22) {
                if (!lastReciveData11.equals(reciveData)) {
                    lastReciveData11 = reciveData;
                    if (deviceModel.deviceProtocolType == PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeV2) {
                        analyticalDataFat(reciveData, deviceModel);
                    } else {
                        analyticalDataFatV3(reciveData, deviceModel);
                    }
                }
            } else {
                //单独处理wifi成历史数据问题
                if (reciveData.length() == 36 || reciveData.length() == 40) {
                    //18字节历史数据
                    analysisDataHistory(reciveData, deviceModel);
                } else {
                    //异常数据，缓存
                    if (TextUtils.isEmpty(lastHistoryData)) {
                        lastHistoryData = reciveData;
                    } else {
                        reciveData += lastReciveData;
                        lastHistoryData = "";
                    }
                    if (reciveData.length() == 36 || reciveData.length() == 40) {
                        //18字节历史数据
                        analysisDataHistory(reciveData, deviceModel);
                    }
                }
            }
        }
    }

    /**
     * 解析11字节常规数据 V3.0协议
     *
     * @param reciveData  ble data
     * @param deviceModel
     */
    private void analyticalDataFatV3(String reciveData, final PPDeviceModel deviceModel) {
        if (reciveData.length() > 22) {
            reciveData = reciveData.substring(0, 22);
        }
        String dataType = reciveData.substring(18, 20);
        double weightKg = 0.0;
        int weightKgInt = 0;
        String heartOrUnit = "00";
        if (!dataType.equals("03")) {
            weightKg = ProtocalNormalDeviceHelper.getWeightKg(reciveData);
            weightKgInt = ProtocalNormalDeviceHelper.getWeightG(reciveData);
        } else {
            heartOrUnit = reciveData.substring(6, 8);
        }

        final PPUnitType unitType = ProtocalNormalDeviceHelper.getPpUnitType(reciveData, deviceModel);


        //00： 表示锁定数据
        //01： 表示过程数据
        //02： 表示心率秤锁定数据，并且心率测量中
        //03： 表示心率秤心率测试结束
        //04： 表示有心率值的历史数据
        //20-29： 表示用户组 P0-P9
        //备注： 此字节除为 01 表示过程数据， 其他位数都表示锁定数据。

        PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
        bodyBaseModel.impedance = 0;
        bodyBaseModel.deviceModel = deviceModel;
        bodyBaseModel.userModel = userModel;
        bodyBaseModel.unit = unitType;
        bodyBaseModel.weight = weightKgInt;
        Logger.d(TAG + " analyticalDataFatV3 dataType: " + dataType + " weightKgInt:" + weightKgInt + " weightKg:" + weightKg);
        if (dataType.equals("01")) {
            onProcessData(deviceModel, bodyBaseModel);
        } else {
            long finalImpedance = 0L;
            if (deviceModel.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeDirect) {
                finalImpedance = ProtocalNormalDeviceHelper.getImpedance(reciveData) / 10;
            } else if (deviceModel.deviceCalcuteType != PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNeedNot) {
                finalImpedance = ProtocalNormalDeviceHelper.getImpedance(reciveData);
            }
            if (weightKg > 0) {
                myWeightKgInt = weightKgInt;
                myImpedance = finalImpedance;
            }
            Logger.d("analyticalDataFatV3 myImpedance = " + myImpedance + " myWeightKgInt:" + myWeightKgInt);
            bodyBaseModel.impedance = myImpedance;
            bodyBaseModel.weight = myWeightKgInt;
            switch (dataType) {
                case "00":
                    onLockDataHearRateDevice(bodyBaseModel);
                    resetCacheLockWeight();
                    break;
                case "02":
                    bodyBaseModel.isHeartRating = true;
                    onLockDataHearRateDevice(bodyBaseModel);
                    break;
                case "03": {
                    int heartRate = ByteUtil.hexToTen(heartOrUnit);
                    bodyBaseModel.heartRate = heartRate;
                    onLockDataHearRateDevice(bodyBaseModel);
                    resetCacheLockWeight();
                }
                break;
                case "04": {
                    int heartRate = ByteUtil.hexToTen(heartOrUnit);
                    bodyBaseModel.heartRate = heartRate;
                    onLockDataHearRateDevice(bodyBaseModel);
                }
                break;
            }
        }

    }

    private void onProcessData(final PPDeviceModel deviceModel, final PPBodyBaseModel ppBodyBaseModel) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (dataChangeListener != null) {
                    dataChangeListener.monitorProcessData(ppBodyBaseModel);
                }
            }
        });
    }

    private void analyticalDataFat(String reciveData, final PPDeviceModel deviceModel) {

        if (reciveData.length() > 22) {
            reciveData = reciveData.substring(0, 22);
        }

        String preStr = reciveData.substring(0, 2);
        final PPUnitType unitType = ProtocalNormalDeviceHelper.getPpUnitType(reciveData, deviceModel);

        final double weightKg = ProtocalNormalDeviceHelper.getWeightKg(reciveData);
        final int weightKgInt = ProtocalNormalDeviceHelper.getWeightG(reciveData);

        final String signLocked = reciveData.substring(18, 20);

        if (preStr.equalsIgnoreCase(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)) {
            int finalImpedance = 0;
            if (signLocked.equalsIgnoreCase("00")) {
                finalImpedance = ProtocalNormalDeviceHelper.getImpedance(reciveData);
            } else if (signLocked.equalsIgnoreCase("A0")) {
                //直流秤
                finalImpedance = ProtocalNormalDeviceHelper.getImpedance(reciveData);
                if (finalImpedance > 1200) {
                    finalImpedance = finalImpedance / 10;
                }
            }
            String impedanceOrHeartRateHigh = reciveData.substring(2, 4);
            String impedanceOrHeartRateLow = reciveData.substring(4, 6);

            final int impedanceOrHeartRate = ByteUtil.hexToTen(impedanceOrHeartRateLow + impedanceOrHeartRateHigh);

            Logger.d("impedanceOrHeartRate = " + impedanceOrHeartRate + " heartRateHex = " + impedanceOrHeartRateLow + impedanceOrHeartRateHigh);

            final String finalReciveData = reciveData;
            final int finalImpedance1 = finalImpedance;

            final PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
            bodyBaseModel.impedance = finalImpedance;
            bodyBaseModel.deviceModel = deviceModel;
            bodyBaseModel.userModel = userModel;
            bodyBaseModel.unit = unitType;
            bodyBaseModel.weight = weightKgInt;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (signLocked.equals("01")) {
                        //过程数据
                        if (dataChangeListener != null) {
                            dataChangeListener.monitorProcessData(bodyBaseModel);
                        }

                        if (impedanceOrHeartRate > 12000) {
                            if (weightKg > 0) {
                                myWeightKgInt = weightKgInt;
                            }
                            bodyBaseModel.weight = myWeightKgInt;
                            if (finalImpedance1 > 0) {
                                myImpedance = finalImpedance1;
                            }
                            bodyBaseModel.impedance = myImpedance;
                            String heartRateSign = finalReciveData.substring(4, 6);
                            int hex = ByteUtil.hexToTen(heartRateSign);
                            if ((hex & 0x80) == 0x80) {
                                //正在测量心率
                                bodyBaseModel.isHeartRating = true;
                                if (dataChangeListener != null) {
//                                    deviceModel.setDeviceType(PPDeviceType.PPDeviceTypeBodyFat);

//                                    PPDeviceModel deviceModel = new PPDeviceModel(deviceMac, deviceName, PPDeviceType.PPDeviceTypeBodyFat, DeviceManager.getScaleType(deviceName));
                                    cacheLockWeightKg(bodyBaseModel);
                                    dataChangeListener.monitorLockData(bodyBaseModel);
                                }
                            }
                        }
                    } else if (signLocked.equalsIgnoreCase("A0")) {
                        bodyBaseModel.impedance = finalImpedance1;
                        onLockDataHearRateDevice(bodyBaseModel);
                    } else {
                        //锁定数据
                        Logger.e("ppScale_ lockData weightKg = " + weightKg + " myWeightKg = " + myWeightKgInt);
                        if (impedanceOrHeartRate > 12000) {
                            if (weightKg > 0) {
                                myWeightKgInt = weightKgInt;
                            }
                            bodyBaseModel.weight = myWeightKgInt;
                            if (finalImpedance1 > 0) {
                                myImpedance = finalImpedance1;
                            }
                            bodyBaseModel.impedance = myImpedance;
                            String heartRateSign = finalReciveData.substring(4, 6);

                            int hex = ByteUtil.hexToTen(heartRateSign);
                            if ((hex & 0xC0) == 0xC0) {
                                if (myWeightKgInt > 0) {
                                    //停止测量
                                    bodyBaseModel.isHeartRating = false;
                                    String heartRateStr = finalReciveData.substring(2, 4);
                                    int heartRate = ByteUtil.hexToTen(heartRateStr);
                                    bodyBaseModel.heartRate = heartRate;
//                                    if (bleDataStateInterface != null) {
//                                        bleDataStateInterface.connectDevice(PPDeviceType.PPDeviceTypeHearRate);
//                                    }
                                    if (dataChangeListener != null) {
                                        cacheLockWeightKg(bodyBaseModel);
                                        dataChangeListener.monitorLockData(bodyBaseModel);
                                    }
                                }
                                myWeightKgInt = 0;
                                myImpedance = 0;
                            } else if ((hex & 0x80) == 0x80) {
                                //正在测量心率
//                                bodyBaseModel.setHeartRateEnd(false);
                                bodyBaseModel.isHeartRating = true;
                                if (dataChangeListener != null) {
                                    cacheLockWeightKg(bodyBaseModel);
                                    dataChangeListener.monitorLockData(bodyBaseModel);
                                }
                            } else {
                                if (bleDataStateInterface != null) {
                                    bleDataStateInterface.connectDevice(PPDeviceType.PPDeviceTypeHearRate);
                                }
                                if (myWeightKgInt > 0) {
//                                    bodyBaseModel.setHeartRateEnd(true);
                                    bodyBaseModel.isHeartRating = false;
                                    if (dataChangeListener != null) {
                                        cacheLockWeightKg(bodyBaseModel);
                                        dataChangeListener.monitorLockData(bodyBaseModel);
                                    }
                                } else {
//                                    bodyBaseModel1.setHeartRateEnd(true);
                                    bodyBaseModel.isHeartRating = false;
                                    if (dataChangeListener != null) {
                                        cacheLockWeightKg(bodyBaseModel);
                                        dataChangeListener.monitorLockData(bodyBaseModel);
                                    }
                                }
                                resetCacheLockWeight();
                            }
                        } else {
                            bodyBaseModel.impedance = finalImpedance1;
                            onLockDataHearRateDevice(bodyBaseModel);
                        }
                    }
                }
            });
        } else if (preStr.equalsIgnoreCase(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
                    bodyBaseModel.impedance = 0;
                    bodyBaseModel.deviceModel = deviceModel;
                    bodyBaseModel.userModel = userModel;
                    bodyBaseModel.unit = unitType;
                    bodyBaseModel.weight = weightKgInt;
                    if (signLocked.equals("01")) {
                        //过程数据
                        if (dataChangeListener != null) {
                            dataChangeListener.monitorProcessData(bodyBaseModel);
                        }
                    } else {
                        //锁定数据
                        Logger.d("ppScale_ weight scale lockData weightKg = " + weightKg);
                        if (bleDataStateInterface != null) {
                            bleDataStateInterface.connectDevice(PPDeviceType.PPDeviceTypeWeight);
                        }
                        if (dataChangeListener != null) {
                            cacheLockWeightKg(bodyBaseModel);
                            dataChangeListener.monitorLockData(bodyBaseModel);
                        }
                    }
                }
            });
        }

//        else if (preStr.equalsIgnoreCase(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {
//
//            analyticalFoodScaleData(reciveData, deviceModel);
//        }
    }

    private void onLockDataHearRateDevice(final PPBodyBaseModel bodyBaseModel) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (dataChangeListener != null) {
                    cacheLockWeightKg(bodyBaseModel);
                    if (bodyBaseModel.weight > 0) {
                        Logger.e(TAG + " onLockDataHearRateDevice monitorLockData weight:" + bodyBaseModel.weight);
                        dataChangeListener.monitorLockData(bodyBaseModel);
                    } else {
                        Logger.e(TAG + " onLockDataHearRateDevice monitorDataFail weight:" + bodyBaseModel.weight);
                        dataChangeListener.monitorDataFail(bodyBaseModel, bodyBaseModel.deviceModel);
                    }
                }
            }
        });
    }


    /**
     * 解析11字节常规数据
     *
     * @param reciveData  ble data
     * @param deviceModel
     */
    private void analyticalFoodScaleData(String reciveData, final PPDeviceModel deviceModel) {

        final String signLocked = reciveData.substring(18, 20);

        //正负值00表示负01表示正

        String weightDataLow = reciveData.substring(6, 8);
        String weightDataHigh = reciveData.substring(8, 10);

//            final double weightKg = (double) ByteUtil.hexToTen(weightDataLow + weightDataHigh) / 100.0;
        final double weightKg = (double) ByteUtil.hexToTen(weightDataHigh + weightDataLow);

        String unit = reciveData.substring(16, 18);
        int iUnit = ByteUtil.hexToTen(unit);
        final PPUnitType unitType = UnitUtil.getUnitType(iUnit);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (signLocked.equals("01")) {
                    //过程数据
//                    if (processDateInterface != null) {
////                        deviceModel.setDeviceType(PPDeviceType.PPDeviceTypeWeight);
//                        PPbodyBaseModel ppBodyBaseModel = new PPbodyBaseModel(weightKg, userModel, deviceModel, unitType);
////                        ppBodyBaseModel.setThanZero(1);
//                        ppbodyBaseModel.isPlus = true;
//                        lockDataInterface.monitorLockData(ppBodyBaseModel, deviceModel);
//                    }
                } else {
                    //锁定数据
                    Logger.e("ppScale_ weight scale lockData weightKg = " + weightKg);
                        /*if (userModel == null) {
                            Logger.e("Please Bind UserModel, Try agin later");
                            return;
                        }*/
                    if (bleDataStateInterface != null) {
                        bleDataStateInterface.connectDevice(PPDeviceType.PPDeviceTypeCalcuteInScale);
                    }

                    if (dataChangeListener != null) {
//                        deviceModel.setDeviceType(PPDeviceType.PPDeviceTypeCalcuteInScale);
//                        PPDeviceModel deviceModel = new PPDeviceModel(deviceMac, deviceName, PPDeviceType.PPDeviceTypeCalcuteInScale, DeviceManager.getScaleType(deviceName));
//                        PPbodyBaseModel ppBodyBaseModel = new PPbodyBaseModel(weightKg, userModel, deviceModel, unitType);
//                        ppBodyBaseModel.setThanZero(1);
//                        ppbodyBaseModel.isPlus = true;
//                        lockDataInterface.monitorLockData(ppBodyBaseModel, deviceModel);
                    }
                }
            }
        });
    }

    private void cacheLockWeightKg(PPBodyBaseModel bodyBaseModel) {
        if (historyDataInterface != null) {
            lockWeightKg = bodyBaseModel.getPpWeightKg();
            lockImpedance = bodyBaseModel.impedance;
        }
    }

    private void resetCacheLockWeight() {
        lockWeightKg = 0.0f;
        lockImpedance = 0;
        myWeightKgInt = 0;
        myImpedance = 0;
    }

    /**
     * 频繁上称时避免重复数据
     */
    public void resetHistory() {
        Logger.d("liyp_  reset cache data");
        lastReciveData = "";
        lastReciveData11 = "";
        lockWeightKg = 0.0f;
        lockImpedance = 0;
    }

    /**
     * V2和V3公用
     * 历史数据解析
     * <p/>
     *
     * @param reciveData
     */
    private synchronized void analysisDataHistoryV3(String reciveData, final PPDeviceModel deviceModel) {

//        double weightKg = ProtocalNormalDeviceHelper.getWeightKg(reciveData);
        int weightKgInt = ProtocalNormalDeviceHelper.getWeightG(reciveData);

        String heartOrUnit = reciveData.substring(16, 18);

        final PPUnitType unitType = ProtocalNormalDeviceHelper.getPpUnitType(reciveData, deviceModel);

        String dataType = reciveData.substring(18, 20);
        //00： 表示锁定数据
        //01： 表示过程数据
        //02： 表示心率秤锁定数据，并且心率测量中
        //03： 表示心率秤心率测试结束
        //04： 表示有心率值的历史数据
        //20-29： 表示用户组 P0-P9
        //备注： 此字节除为 01 表示过程数据， 其他位数都表示锁定数据。

        int finalImpedance = 0;
        if (deviceModel.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeDirect) {
            finalImpedance = ProtocalNormalDeviceHelper.getImpedance(reciveData) / 10;
        } else {
            finalImpedance = ProtocalNormalDeviceHelper.getImpedance(reciveData);
        }

        final PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
        bodyBaseModel.impedance = finalImpedance;
        bodyBaseModel.deviceModel = deviceModel;
        bodyBaseModel.userModel = userModel;
        bodyBaseModel.unit = unitType;
        bodyBaseModel.weight = weightKgInt;

        if ("04".equals(dataType)) {
            int heartRate = ByteUtil.hexToTen(heartOrUnit);
            bodyBaseModel.heartRate = heartRate;
        }

        final String finalClock = ProtocalNormalDeviceHelper.getClock(reciveData, deviceModel);
        handler.post(new Runnable() {
            @Override
            public void run() {
                //会有一组重复数据与锁定数据重复所以过滤掉该组数据
                //yyyy-DD-mm
                if (historyDataInterface != null) {
                    long timeStamp = DateUtil.stringToLong(finalClock);
                    if (timeStamp > ABNORMAL_HISTORY_INTERVAL_TIME) {
                        if (lockWeightKg != bodyBaseModel.getPpWeightKg() || lockImpedance != bodyBaseModel.impedance) {
                            historyDataInterface.monitorHistoryData(bodyBaseModel, finalClock);
                        } else if (System.currentTimeMillis() - timeStamp > 8 * 1000) {
                            Logger.d("History data CurrentTime = " + System.currentTimeMillis() + " history clock = " + DateUtil.stringToLong(finalClock));
                            historyDataInterface.monitorHistoryData(bodyBaseModel, finalClock);
                        } else {
                            Logger.d("History data CurrentTime = " + System.currentTimeMillis() + " history clock 1 = " + DateUtil.stringToLong(finalClock));
                        }
                    } else {
                        Logger.e("History data 数据异常:" + timeStamp + " finalClock = " + finalClock);
                    }
                }
            }
        });

    }

    private synchronized void analysisDataHistory(String reciveData, final PPDeviceModel deviceModel) {
        BleSendDelegate.isSendHistoryCmd = false;

        if (deviceModel.deviceProtocolType == PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeV3) {
            analysisDataHistoryV3(reciveData, deviceModel);
        } else {
            // CF 4E 11 00 14 00 00 00 00 22 A6 C9 07 E1 08 11 15 04 30
            // CF 00 00 DC 05 00 00 00 00 00 16 07 E2 09 0E 0E 3A 28
            // CF 00 00 CE 18 00 00 00 00 00 19 07 E5 01 19 14 19 28 31 64
            //CF55C080118C872A0000EA07E4080A091405
        /*if (userModel == null) {
            Logger.e("Please Bind UserModel, Try agin later");
            return;
        }*/
            String preStr = reciveData.substring(0, 2);
            if (preStr.equalsIgnoreCase(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)
                || preStr.equalsIgnoreCase(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE)) {

                PPUnitType unitType = ProtocalNormalDeviceHelper.getPpUnitType(reciveData, deviceModel);

//                double weightKg = ProtocalNormalDeviceHelper.getWeightKg(reciveData);
                int weightKgInt = ProtocalNormalDeviceHelper.getWeightG(reciveData);

                Logger.d("historyData WeightKg = " + weightKgInt);

                int impedance = ProtocalNormalDeviceHelper.getImpedance(reciveData);

                String heartSign = reciveData.substring(4, 6);
                int heartRate = 0;
                if (heartSign.startsWith("C0")) {
                    heartRate = ByteUtil.hexToTen(reciveData.substring(2, 4));
                }

                final PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
                bodyBaseModel.impedance = impedance;
                bodyBaseModel.deviceModel = deviceModel;
                bodyBaseModel.userModel = userModel;
                bodyBaseModel.unit = unitType;
                bodyBaseModel.weight = weightKgInt;
                bodyBaseModel.heartRate = heartRate;

                final String finalClock = ProtocalNormalDeviceHelper.getClock(reciveData, deviceModel);

                Logger.d("historyData finalClock = " + finalClock);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //会有一组重复数据与锁定数据重复所以过滤掉该组数据
                        //yyyy-DD-mm
                        if (historyDataInterface != null) {
                            long timeStamp = DateUtil.stringToLong(finalClock);
                            if (timeStamp > ABNORMAL_HISTORY_INTERVAL_TIME) {
                                if (lockWeightKg != bodyBaseModel.getPpWeightKg() || lockImpedance != bodyBaseModel.impedance) {
                                    historyDataInterface.monitorHistoryData(bodyBaseModel, finalClock);
                                } else if (System.currentTimeMillis() - DateUtil.stringToLong(finalClock) > 8 * 1000) {
                                    Logger.d(" currentTime = " + System.currentTimeMillis() + " history clock = " + DateUtil.stringToLong(finalClock));
                                    historyDataInterface.monitorHistoryData(bodyBaseModel, finalClock);
                                } else {
                                    Logger.d(" currentTime = " + System.currentTimeMillis() + " history clock 1 = " + DateUtil.stringToLong(finalClock));
                                }
                            } else {
                                Logger.e("History data 数据异常:" + timeStamp + " finalClock = " + finalClock);
                            }
                        }
                    }
                });

            }
        }

    }

    private void clearHistoryData(final PPDeviceModel deviceModel) {
        handler.post(new Runnable() {
            @Override
            public void run() {
//                if (bleDataStateInterface != null) {
//                    bleDataStateInterface.deleteHistoryData();
//                }
                if (historyDataInterface != null) {
                    historyDataInterface.monitorHistoryEnd();
                }
                resetCacheLockWeight();
            }
        });
    }

    /**
     * 解析秤端计算数据
     *
     * @param reciveData
     * @param deviceModel
     */
    public void analysisDataCalcuteInScale(String reciveData, PPUserModel userModel, final PPDeviceModel deviceModel) {
        if (!lastReciveData.equals(reciveData) || System.currentTimeMillis() - lastTimes > 2500) {
            lastTimes = System.currentTimeMillis();
            Logger.d("lastReciveData---------  " + lastReciveData + " reciveData---------  " + reciveData);
            lastReciveData = reciveData;

            Logger.d("analysisDataCalcuteInScale ------- " + reciveData);

            if (userModel == null) {
                Logger.e("error Please Bind UserModel, Try agin later");
                throw new NullPointerException("UserModel is null ,Please on BleOption.class to Bind UserModel, Try agin later");
            }

            // 1  2  3  4  5  6     7    8    9    10   11  12    13   14   15   16
            // 01 23 45 67 89 1011 1213 1415 1617 1819 2021 2223 2425 2627 2829 3031
            // CF 03 99 AA 01 1C   00   32   0A   01   00   01   03   52   05   C5
            //CF 0018A801D40000000000000000000000
            //CF 0092B400B6003214009901028C0357
            //CF 0018A803100000000000000000000000
            String preStr = reciveData.substring(0, 2);
            if (preStr.equalsIgnoreCase(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)) {

                final PPBodyFatModel bodyFatModel = ProtocalNormalDeviceHelper.getCalcuteInScaleBodyFatModel(reciveData, userModel, deviceModel);
                if (bodyFatModel == null) {
                    return;
                }

//            deviceModel.setDeviceType(PPDeviceType.PPDeviceTypeCalcuteInScale);
//            deviceModel.setScaleType());

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (dataChangeListener != null) {
                            dataChangeListener.monitorLockDataByCalculateInScale(bodyFatModel);
                        }
                    }
                });
            }

        }

    }

    /**
     * 解析秤端计算数据
     *
     * @param reciveData
     * @param deviceModel
     */
    private void analysisData17CalcuteInScale(String reciveData, final PPDeviceModel deviceModel) {
        Logger.d("analysisData17CalcuteInScale ------- " + reciveData);
        // 1  2  3  4  5  6     7    8    9    10   11  12    13   14   15   16
        // 01 23 45 67 89 1011 1213 1415 1617 1819 2021 2223 2425 2627 2829 3031
        // CF 03 99 AA 01 1C   00   32   0A   01   00   01   03   52   05   C5
        //CF 0092B400B6003214009901028C0357
        //CF 0018A803100000000000000000000000
        String preStr = reciveData.substring(0, 2);
        if (preStr.equals(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)) {
            if (userModel == null) {
                Logger.e("Please Bind UserModel, Try agin later");
                return;
            }
            final PPBodyFatModel bodyFatModel = ProtocalNormalDeviceHelper.get17BodyFatModel(reciveData, userModel, deviceModel);
            if (bodyFatModel == null) {
                return;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (dataChangeListener != null) {
                        dataChangeListener.monitorLockDataByCalculateInScale(bodyFatModel);
                    }
                }
            });
        }
    }

    public void setBleDataStateInterface(BleDataStateInterface bleDataStateInterface) {
        this.bleDataStateInterface = bleDataStateInterface;
    }

    public void bindProtocalFiter(ProtocalFilterImpl protocalFilter, PPUserModel userModel) {
        if (protocalFilter != null) {
            this.historyDataInterface = protocalFilter.getHistoryDataInterface();
            this.deviceInfoInterface = protocalFilter.getDeviceInfoInterface();
            this.userModel = userModel;
            ProtocalBMDJDeviceHelper.getInstance().setInterface(protocalFilter);
            ProtocalWifiDeviceHelper.getInstance().setInterface(protocalFilter);
            ProtocalTorreDeviceHelper.getInstance().setInterface(protocalFilter);
            TorreDelegate.getInstance().setUserModel(userModel);
            ProtocalV4DataHelper.getInstance().setInterface(protocalFilter);
        }
    }

    public void setHistoryDataInterface(PPHistoryDataInterface historyDataInterface) {
        this.historyDataInterface = historyDataInterface;
    }

    public void setDataChangeListener(PPDataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

}
