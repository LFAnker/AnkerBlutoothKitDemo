package com.peng.ppscale.business.ble.foodscale.manager;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.peng.ppscale.business.ble.listener.FoodScaleDataChangeListener;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.business.protocall.ProtocalNormalDeviceHelper;
import com.peng.ppscale.search.BLeSearchV3DeviceHelper;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.DeviceType;
import com.peng.ppscale.util.DeviceUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.vo.LFFoodScaleGeneral;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BleFoodDataProtocoManager {

    private String TAG = BleFoodDataProtocoManager.class.getSimpleName();

    double myWeightKg;
    FoodScaleDataChangeListener dataChangeListener;

    private long lastTimes = 0;//用于过滤相同数据，但是又不是同一组数据3s之外

    private String lastReciveData = "";

    public BleFoodDataProtocoManager() {
        super();
        myWeightKg = 0;
    }

    private static volatile BleFoodDataProtocoManager instance = null;

    public static BleFoodDataProtocoManager getInstance() {
        if (instance == null) {
            synchronized (BleFoodDataProtocoManager.class) {
                if (instance == null) {
                    instance = new BleFoodDataProtocoManager();
                }
            }
        }
        return instance;
    }

    /*连接后解析秤返回的数据*/
    public void analysisReciveData(String reciveData, final PPDeviceModel deviceModel) {
        if (!lastReciveData.equals(reciveData) || System.currentTimeMillis() - lastTimes > 2500) {
            lastTimes = System.currentTimeMillis();
            Logger.d(TAG + " lastReciveData---------  " + lastReciveData + " reciveData---------  " + reciveData);
            lastReciveData = reciveData;
            if (!TextUtils.isEmpty(reciveData)) {
                if (reciveData.length() == 32 || reciveData.length() == 22) {
                    electronicScaleProtocol(reciveData, deviceModel, dataChangeListener);
                } else if (reciveData.length() == 36) {
                    protocoHistory(reciveData, deviceModel, dataChangeListener);
                } else if (reciveData.length() == 4) {
                    if (reciveData.equals("F200")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                dataChangeListener.historyData(null, deviceModel, "", true);
                            }
                        });
                    }
                }
            }
        }
    }

    private void protocoHistory(String reciveData, final PPDeviceModel device, final FoodScaleDataChangeListener protocoInterface) {

        String preStr = reciveData.substring(0, 2);

        String weightDataLow = reciveData.substring(6, 8);
        String weightDataHigh = reciveData.substring(8, 10);//byte 5

        String thanZero = reciveData.substring(4, 6);
//                    int iThanZero = (hex & 0x80) == 0x80 ? 0 : 1; //0负数 1正
        int iThanZero = hexToTen(thanZero);
        iThanZero = iThanZero == 0 ? 1 : 0;
        Logger.d("iThanZero =" + iThanZero);
        final double weightAllThan = (double) hexToTen(weightDataHigh + weightDataLow);
//                    int hexThan = hexToTen("8000");
//                    double weightKg =  iThanZero== 0 ? weightAllThan - hexThan : weightAllThan;
        double weightKg = weightAllThan;
        String unit = reciveData.substring(16, 18);
        int iUnit = hexToTen(unit);

        final String clock = getClock(reciveData);

        //CA00007B000000000701B7
        final LFFoodScaleGeneral lfFoodScaleGeneral = new LFFoodScaleGeneral(weightKg, BleFoodDataProtocoHelper.electronicUnitInt11Enum(iUnit), 11, device.getDeviceName());
        lfFoodScaleGeneral.setThanZero(iThanZero);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                DeviceType.setDeviceType(DeviceUtil.getDeviceType(device.getDeviceName()));
                if (protocoInterface != null) {
                    protocoInterface.historyData(lfFoodScaleGeneral, device, clock, false);
                }
            }
        });
    }

    public boolean isLockedData(String reciveData) {
        String signLocked = reciveData.substring(18, 20);
        if (signLocked.equals("01")) {
            // 过程数据
            return false;
        } else {
            return true;
        }
    }

    public byte[] getSwitchUnitBytes(PPUnitType unitType) {
        return sendData2ElectronicScale(unitType);
    }

    public List<byte[]> getZeroBytes() {
        return sendDataZeroScale();
    }

    public static byte[] deleteAdoreHistoryData() {
        String byteStr = "F201";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    public static byte[] sendSyncHistoryData2AdoreScale() {
        String byteStr = "F200";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    /*根据ElectronicScale协议解析*/
    private void electronicScaleProtocol(String reciveData,
                                         final PPDeviceModel device,
                                         final FoodScaleDataChangeListener protocoInterface) {

        Logger.d("food electronicScaleProtocol ------- " + reciveData);
        if (reciveData.length() == 32) {
            // 1  2  3  4  5  6     7    8    9    10   11  12    13   14   15   16
            // 01 23 45 67 89 1011 1213 1415 1617 1819 2021 2223 2425 2627 2829 3031
            // CA 00 00 00 03 E8 00 00 00 00 00 00 00 00 00 00
            // CA 00 00 01 01 8C 00 000000000000000000
            String preStr = reciveData.substring(0, 2);

            //正负值00表示负01表示正
            String thanZero = reciveData.substring(6, 8);

            String weightDataHigh = reciveData.substring(8, 10);
            String weightDataLow = reciveData.substring(10, 12);
            final double weightKg = (double) hexToTen(weightDataHigh + weightDataLow);

            String unit = reciveData.substring(12, 14);
            int iUnit = 0;
            int iThanZero = 1;
            iUnit = hexToTen(unit);
            iThanZero = hexToTen(thanZero);
            final String dataType = reciveData.substring(18, 20);
            final LFFoodScaleGeneral lfFoodScaleGeneral = new LFFoodScaleGeneral(weightKg, BleFoodDataProtocoHelper.electronicUnitInt16Enum(iUnit), 16, device.getDeviceName());
            lfFoodScaleGeneral.setThanZero(iThanZero);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
//                        if (dataType.equals("01")) {
//                            protocoInterface.processData(lfFoodScaleGeneral, deviceModel);
//                        } else {
                    DeviceType.setDeviceType(DeviceUtil.getDeviceType(device.getDeviceName()));
                    protocoInterface.lockedData(lfFoodScaleGeneral, device);
//                        }
                }
            });

        } else if (reciveData.length() == 22) {
            if (device.deviceProtocolType == PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeV3) {
                if (device.deviceType == PPScaleDefine.PPDeviceType.PPDeviceTypeUnknow) {
                    BLeSearchV3DeviceHelper.createSmartScaleDevice(reciveData, device);
                }
                int weightKg = ProtocalNormalDeviceHelper.getWeightG(reciveData);
                double weight = 0.0;
                if (device.deviceAccuracyType == PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint01G) {
                    weight = (weightKg * 1.0f) / 10.0f;
                } else {
                    weight = weightKg;
                }

                String thanZero = reciveData.substring(10, 12);
//                    int iThanZero = (hex & 0x80) == 0x80 ? 0 : 1; //0负数 1正
//                int iThanZero = hexToTen(thanZero);
                Logger.d("thanZero  = " + thanZero);
                int iThanZero = hexToTen(thanZero);
                iThanZero = iThanZero == 0 ? 1 : 0;

                String unit = reciveData.substring(16, 18);
                final String dataType = reciveData.substring(18, 20);
                int iUnit = hexToTen(unit);
                final LFFoodScaleGeneral lfFoodScaleGeneral = new LFFoodScaleGeneral(weight, BleFoodDataProtocoHelper.electronicUnitInt11Enum(iUnit), 11, device.getDeviceName());
                lfFoodScaleGeneral.setThanZero(iThanZero);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        DeviceType.setDeviceType(DeviceUtil.getDeviceType(device.getDeviceName()));
                        if (protocoInterface != null) {
                            if (dataType.equals("01")) {
                                protocoInterface.processData(lfFoodScaleGeneral, device);
                            } else if (dataType.equals("00")) {
                                protocoInterface.lockedData(lfFoodScaleGeneral, device);
                            } else {
//                            protocoInterface.overWeight(deviceModel);
                            }
                        }
                    }
                });
            } else {
                String preStr = reciveData.substring(0, 2);
                String weightDataLow = reciveData.substring(6, 8);
                String weightDataHigh = reciveData.substring(8, 10);//byte 5

                String thanZero = reciveData.substring(4, 6);
//                    int iThanZero = (hex & 0x80) == 0x80 ? 0 : 1; //0负数 1正
                int iThanZero = hexToTen(thanZero);
                iThanZero = iThanZero == 0 ? 1 : 0;
                Logger.d("iThanZero =" + iThanZero);
                final double weightAllThan = (double) hexToTen(weightDataHigh + weightDataLow);
//                    int hexThan = hexToTen("8000");
//                    double weightKg =  iThanZero== 0 ? weightAllThan - hexThan : weightAllThan;
                double weightKg = weightAllThan;
                String unit = reciveData.substring(16, 18);
                final String dataType = reciveData.substring(18, 20);
                int iUnit = hexToTen(unit);
                //CA00007B000000000701B7
                final LFFoodScaleGeneral lfFoodScaleGeneral = new LFFoodScaleGeneral(weightKg, BleFoodDataProtocoHelper.electronicUnitInt11Enum(iUnit), 11, device.getDeviceName());
                lfFoodScaleGeneral.setThanZero(iThanZero);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (protocoInterface != null) {
                            DeviceType.setDeviceType(DeviceUtil.getDeviceType(device.getDeviceName()));
                            if (dataType.equals("01")) {
                                protocoInterface.processData(lfFoodScaleGeneral, device);
                            } else if (dataType.equals("00")) {
                                protocoInterface.lockedData(lfFoodScaleGeneral, device);
                            } else {
//                            protocoInterface.overWeight(deviceModel);
                            }
                        }
                    }
                });
            }

        }
    }

    public static byte[] sendData2ElectronicScale(PPUnitType unitType) {
        int unit = BleFoodDataProtocoHelper.electronicUnitEnum11Int(unitType);
        /**
         * （00：表示KG 01 表示LB
         * 02：表示ST 03 表示斤
         * 04：表示g 05 表示lb:oz
         * 06：表示oz 07 表示ml(water)
         * 08: 表示ml（milk））
         */
        String byteStr = "FD"
            + "00"
            + ByteUtil.decimal2Hex(unit)
            + "00"
            + "00"
            + "00"
            + "00"
            + "00"
            + "00"
            + "00";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        byte[] xorByte = ByteUtil.getXor(bytes);
//        List<byte[]> resultArr = new ArrayList<>();
//        resultArr.add(xorByte);
        return xorByte;
    }

    private static byte[] sendUnitData2Scale(PPUnitType unitType) {
        int unit = BleFoodDataProtocoHelper.electronicUnitEnum16Int(unitType);
        String byteStr = "FD"
            + "00"
            + ByteUtil.decimal2Hex(unit)
            + "00"
            + "00"
            + "00"
            + "00"
            + "00"
            + "00"
            + "00";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        byte[] xorByte = ByteUtil.getXor(bytes);
        return xorByte;
    }

    /**
     * 归零
     *
     * @return
     */
    private static List<byte[]> sendDataZeroScale() {
        /**
         * 32 归零
         */
        String byteStr = "FD"
            + "32"
            + "00"
            + "00"
            + "00"
            + "00"
            + "00"
            + "00"
            + "00"
            + "00";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        byte[] xorByte = ByteUtil.getXor(bytes);
        List<byte[]> resultArr = new ArrayList<>();
        resultArr.add(xorByte);
        return resultArr;
    }

    private static byte[] sendSyncTimeData2AdoreScale() {

        // 获取指定格式的时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        // 输出字符串
        String dateStr = df.format(new Date());
        String[] strArr = dateStr.split("/");
        String byteStr = "F1";
        for (String s : strArr) {
            int target = Integer.parseInt(s);
            byteStr += ByteUtil.decimal2Hex(target);
        }
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    /*16进制字符串转10进制int*/
    private static int hexToTen(String hex) {
        try {
            if (TextUtils.isEmpty(hex)) {
                return 0;
            }
            return Integer.valueOf(hex, 16);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*ElectronicScale异或检验*/
    private static byte[] getXorForElectronicScale(byte[] datas) {

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

    /**
     * yyyy-MM-DD
     *
     * @param reciveData
     * @return
     */
    private String getClock(String reciveData) {
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
        return clock;
    }

    public void registDataChangeListener(FoodScaleDataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }
}
