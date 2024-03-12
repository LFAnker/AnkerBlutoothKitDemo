package com.peng.ppscale.search;


import com.peng.ppscale.business.device.DeviceManager;
import com.peng.ppscale.util.DeviceUtil;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;

public class BleSearchV2DeviceHelper {

    /**
     * 兼容老秤
     *
     * @param data
     * @param deviceModel
     */
    public static void createV2Device(String data, PPDeviceModel deviceModel) {
        deviceModel.deviceConnectType = getDeviceConnectType(deviceModel);
        deviceModel.deviceType = getPpDeviceTypeV2(data);
        //计算方式
        PPScaleDefine.PPDeviceCalcuteType calcuteType = getCalcuteType(data, deviceModel);
        //精度
        PPScaleDefine.PPDeviceAccuracyType accuracyType = getAccuracyType(deviceModel);
        //供电类型
        PPScaleDefine.PPDevicePowerType powerType = PPScaleDefine.PPDevicePowerType.PPDevicePowerTypeBattery;
        //功能类型
        int funcType = getFuncType(data, deviceModel);
        //单位
        deviceModel.deviceCalcuteType = calcuteType;
        deviceModel.deviceAccuracyType = accuracyType;
        deviceModel.devicePowerType = powerType;
        deviceModel.deviceFuncType = funcType;
        deviceModel.deviceUnitType = "";
    }

    public static PPScaleDefine.PPDeviceType getPpDeviceTypeV2(String data) {
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
        return ppDeviceType;
    }

    private static PPScaleDefine.PPDeviceConnectType getDeviceConnectType(PPDeviceModel deviceModel) {
        if (DeviceManager.DeviceList.DeviceListPureBroadCastScale.contains(deviceModel.getDeviceName())) {
            return PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeBroadcast;
        } else if (DeviceManager.DeviceList.DeviceListNeedConnect.contains(deviceModel.getDeviceName()) ||
                DeviceManager.DeviceList.torreList.contains(deviceModel.getDeviceName())) {
            return PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeDirect;
        } else {
            return PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeBroadcastOrDirect;
        }
    }

    /**
     * 解析秤功能
     *
     * @param deviceModel
     * @return
     */
    private static int getFuncType(String data, PPDeviceModel deviceModel) {
        int type = 0;
        if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF)) {
            type = PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWeight.getType();
            if (DeviceManager.DeviceList.DeviceListHeartRate.contains(deviceModel.getDeviceName())) {
                type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHeartRate.getType();
            }
            if (DeviceManager.HEALTH_SCALE.equals(deviceModel.getDeviceName())
                    && (deviceModel.deviceFuncType & PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType())
                    == PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType()
                    || DeviceManager.DeviceList.DeviceListHistory.contains(deviceModel.getDeviceName())) {
                type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType();
            }
            if (DeviceManager.DeviceList.DeviceListBMDJ.contains(deviceModel.getDeviceName())) {
                type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeBMDJ.getType();
            }
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE)
                || data.startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CB)) {
            type = PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWeight.getType();
            if (DeviceManager.DeviceList.DeviceListHistory.contains(deviceModel.getDeviceName())) {//历史
                type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType();
            }
        } else if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {

        }
        //非秤端计算的秤都支持抱婴模式
        if (!DeviceManager.DeviceList.DeviceListCalcuteInScale.contains(deviceModel.getDeviceName()) &&
                (type & PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWeight.getType()) == PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWeight.getType()) {
            type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeBaby.getType();
        }
        if (DeviceManager.DeviceList.DeviceListBleConfigWifi.contains(deviceModel.getDeviceName())) {
            type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWifi.getType();
        }
        return type;
    }

    public static PPScaleDefine.PPDeviceAccuracyType getAccuracyType(PPDeviceModel deviceModel) {
        if (DeviceUtil.Point2_Scale_List.contains(deviceModel.getDeviceName())) {
            return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint005;
        } else {
            return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint01;
        }
    }

    public static PPScaleDefine.PPDeviceAccuracyType getAccuracyFoodType(PPDeviceModel deviceModel) {
        if (deviceModel.getDeviceName().equals(DeviceManager.LFSc) ||
                deviceModel.getDeviceName().equals(DeviceManager.LFAdv_B232) ||
                deviceModel.getDeviceName().equals(DeviceManager.WOLO_KITCHEN) ||
                deviceModel.getDeviceName().equals(DeviceManager.INSMART_589) ||
                deviceModel.getDeviceName().equals(DeviceManager.LF_SMART_SCALE) ||
                deviceModel.getDeviceName().equals(DeviceManager.INSMART_818)) {
            return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint01G;
        } else {
            return PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePointG;
        }
    }

    private static PPScaleDefine.PPDeviceCalcuteType getCalcuteType(String data, PPDeviceModel deviceModel) {
        if (deviceModel.deviceType != PPScaleDefine.PPDeviceType.PPDeviceTypeCA) {
            if (DeviceManager.DeviceList.DeviceListCalcuteInScale.contains(deviceModel.getDeviceName())) {
                return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeInScale;
            } else if (DeviceManager.DeviceList.DeviceListDirectCurrentScale.contains(deviceModel.getDeviceName())) {
                return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeDirect;
            } else if (DeviceManager.DeviceList.DeviceListPureBroadCastScale.contains(deviceModel.getDeviceName())) {
                return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNeedNot;
            } else if (DeviceManager.DeviceList.DeviceListWeight.contains(deviceModel.getDeviceName())) {
                return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNeedNot;
            } else {
                return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate;
            }
        }
        if (deviceModel.deviceType == PPScaleDefine.PPDeviceType.PPDeviceTypeCE
                || deviceModel.deviceType == PPScaleDefine.PPDeviceType.PPDeviceTypeCB) {
            return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNeedNot;
        }
        return PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNeedNot;
    }

}
