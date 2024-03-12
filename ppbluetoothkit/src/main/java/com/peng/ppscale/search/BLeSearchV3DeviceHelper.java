package com.peng.ppscale.search;

import com.peng.ppscale.business.device.DeviceManager;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;

import java.util.Arrays;
import java.util.List;

public class BLeSearchV3DeviceHelper {

    //直流
    static List<String> calcuteTypeDirectArray = Arrays.asList("01", "02", "20", "21", "40", "42", "51", "52");
    //交流
    static List<String> calcuteTypeAlternateArray = Arrays.asList("03", "04", "05", "06", "07", "22", "23", "24", "25", "26", "27", "41", "43", "53", "54");
    //0.1精度
    static List<String> AccuracyTypePoint01Array = Arrays.asList("01", "02", "03", "04", "05", "06", "07");
    //0.05精度
    static List<String> AccuracyTypePoint05Array = Arrays.asList("20", "21", "22", "23", "24", "25", "26", "27", "40", "41", "42", "43");
    //0.01精度
    static List<String> AccuracyTypePoint001Array = Arrays.asList("51", "52", "53", "54");
    //带历史数据的列表
    static List<String> FuncTypePointHistoryArray = Arrays.asList("02", "04", "05", "07", "21", "23", "24", "26", "42", "43", "52", "54");
    //安全模式
    static List<String> FuncTypePointSafeArray = Arrays.asList("06", "07", "25", "26");
    //KG
    static List<String> UnitTypeKGArray = Arrays.asList("01", "02", "03", "04", "05", "06");
    //LB
    static List<String> UnitTypeLBArray = Arrays.asList("02", "04", "05", "06");
    //太阳能供电
    static List<String> PowerTypeSolarArray = Arrays.asList("40", "41", "42", "43");
    //直连
    static List<String> DirectConnnectArray = Arrays.asList("24", "41", "43");

    /**
     * LFSmart Scale
     *
     * @param data
     * @param deviceModel
     */
    public static void createSmartScaleDevice(String data, PPDeviceModel deviceModel) {
        createPpDeviceTypeV3(data, deviceModel);
        String functionType = data.substring(2, 4);
        //计算方式
        PPScaleDefine.PPDeviceCalcuteType calcuteType = getCalcuteType(data, functionType);
        //精度
        PPScaleDefine.PPDeviceAccuracyType accuracyType = getAccuracyType(data, functionType);
        //供电类型
        PPScaleDefine.PPDevicePowerType powerType = getPowerType(data, functionType);
        //功能类型
        int funcType = getFuncType(data, deviceModel, functionType);
        //单位
        String unitTypeHex = data.substring(4, 6);
//        int unitType = getUnitType(data, unitTypeHex);
        if (!DeviceManager.DeviceList.DeviceListPureBroadCastScale.contains(deviceModel.getDeviceName())) {
            if (DirectConnnectArray.contains(functionType) || data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {
                deviceModel.deviceConnectType = PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeDirect;
            } else {
                deviceModel.deviceConnectType = PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeBroadcastOrDirect;
            }
        } else {
            deviceModel.deviceConnectType = PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeBroadcast;
        }
        deviceModel.deviceCalcuteType = calcuteType;
        deviceModel.deviceAccuracyType = accuracyType;
        deviceModel.devicePowerType = powerType;
        deviceModel.deviceFuncType = funcType;
        deviceModel.deviceUnitType = "";
    }

    private static PPScaleDefine.PPDeviceType createPpDeviceTypeV3(String data, PPDeviceModel deviceModel) {
        PPScaleDefine.PPDeviceType ppDeviceType = null;
        if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)) {
            ppDeviceType = PPScaleDefine.PPDeviceType.PPDeviceTypeCF;
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE)) {
            ppDeviceType = PPScaleDefine.PPDeviceType.PPDeviceTypeCE;
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {
            ppDeviceType = PPScaleDefine.PPDeviceType.PPDeviceTypeCA;
        } else {
            ppDeviceType = PPScaleDefine.PPDeviceType.PPDeviceTypeCF;
        }
        deviceModel.deviceType = ppDeviceType;
        return ppDeviceType;
    }

    /**
     * 解析秤功能
     *
     * @param functionType
     * @return
     */
    private static int getUnitType(String data, String functionType) {
        int type = 0;
        if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {
            if (UnitTypeKGArray.contains(functionType)) {
                type = PPScaleDefine.PPDeviceUnitType.PPDeviceUnitTypeKG.getType();
            }
            if (UnitTypeLBArray.contains(functionType)) {
                type += PPScaleDefine.PPDeviceUnitType.PPDeviceUnitTypeLB.getType();
            }
            if (functionType.equals("04")) {
                type += PPScaleDefine.PPDeviceUnitType.PPDeviceUnitTypeST.getType();
            }
            if (functionType.equals("03") || functionType.equals("06")) {
                type += PPScaleDefine.PPDeviceUnitType.PPDeviceUnitTypeJin.getType();
            }
            if (functionType.equals("05") || functionType.equals("06")) {
                type += PPScaleDefine.PPDeviceUnitType.PPDeviceUnitTypeSTLB.getType();
            }
        } else {
            if (UnitTypeKGArray.contains(functionType)) {
                type = PPScaleDefine.PPDeviceUnitType.PPDeviceUnitTypeKG.getType();
            }
            if (UnitTypeLBArray.contains(functionType)) {
                type += PPScaleDefine.PPDeviceUnitType.PPDeviceUnitTypeLB.getType();
            }
            if (functionType.equals("04")) {
                type += PPScaleDefine.PPDeviceUnitType.PPDeviceUnitTypeST.getType();
            }
            if (functionType.equals("03") || functionType.equals("06")) {
                type += PPScaleDefine.PPDeviceUnitType.PPDeviceUnitTypeJin.getType();
            }
            if (functionType.equals("05") || functionType.equals("06")) {
                type += PPScaleDefine.PPDeviceUnitType.PPDeviceUnitTypeSTLB.getType();
            }
        }
        return type;
    }

    /**
     * 解析秤功能
     *
     * @param deviceModel
     * @param functionType
     * @return
     */
    private static int getFuncType(String data, PPDeviceModel deviceModel, String functionType) {
        int type = 0;
        if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)) {
            type = PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWeight.getType();
            if (functionType.equals("05") || functionType.equals("24") || functionType.equals("27") || functionType.equals("41")) {//心率
                type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHeartRate.getType();
            }
            if (FuncTypePointHistoryArray.contains(functionType)) {//历史
                type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType();
            }
            if (FuncTypePointSafeArray.contains(functionType)) {//安全模式
                type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeSafe.getType();
            }
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE)) {
            type = PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWeight.getType();
            if (functionType.equals("02") || functionType.equals("21")) {//历史
                type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType();
            }
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {

        }
        //非秤端计算的秤都支持抱婴模式
        if (!data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {
            type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeBaby.getType();
        }
        if (DeviceManager.DeviceList.DeviceListBleConfigWifi.contains(deviceModel.getDeviceName())) {
            type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWifi.getType();
        }
        return type;
    }

    private static PPScaleDefine.PPDevicePowerType getPowerType(String data, String functionType) {
        if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)) {
            if (PowerTypeSolarArray.contains(functionType)) {
                return PPScaleDefine.PPDevicePowerType.PPDevicePowerTypeSolar;
            } else {
                return PPScaleDefine.PPDevicePowerType.PPDevicePowerTypeBattery;
            }
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE)) {
            if (functionType.equals("40")) {
                return PPScaleDefine.PPDevicePowerType.PPDevicePowerTypeSolar;
            } else {
                return PPScaleDefine.PPDevicePowerType.PPDevicePowerTypeBattery;
            }
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {
            return PPScaleDefine.PPDevicePowerType.PPDevicePowerTypeCharge;
        }
        return PPScaleDefine.PPDevicePowerType.PPDevicePowerTypeUnknow;
    }

    private static PPScaleDefine.PPDeviceAccuracyType getAccuracyType(String data, String functionType) {
        if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)) {
            if (AccuracyTypePoint01Array.contains(functionType)) {
                return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint01;
            } else if (AccuracyTypePoint05Array.contains(functionType)) {
                return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint005;
            } else if (AccuracyTypePoint001Array.contains(functionType)) {
                return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint001;
            } else {
                return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypeUnknow;
            }
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE)) {
            if (functionType.equals("01") || functionType.equals("02")) {
                return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint01;
            } else if (functionType.equals("20") || functionType.equals("21") || functionType.equals("40")) {
                return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint005;
            } else {
                return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypeUnknow;
            }
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {
            if (functionType.equals("01")) {
                return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePointG;
            } else {
                return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint01G;
            }
        }
        return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypeUnknow;
    }

    private static PPScaleDefine.PPDeviceCalcuteType getCalcuteType(String data, String functionType) {
        if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)) {
            if (calcuteTypeDirectArray.contains(functionType)) {
                return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeDirect;
            } else if (calcuteTypeAlternateArray.contains(functionType)) {
                return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate;
            } else {
                return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeUnknow;
            }
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE)) {
            return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNeedNot;
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {
            return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNeedNot;
        }
        return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeUnknow;
    }


}
