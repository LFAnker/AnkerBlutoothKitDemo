package com.peng.ppscale.search;

import com.peng.ppscale.business.device.DeviceManager;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;

public class BleSearchTorreDeviceHelper {

    public static PPDeviceModel createTorreDevice(byte[] advData, PPDeviceModel deviceModel) {
        Logger.v("Torre createTorreDevice");
        if (advData != null && advData.length > 0) {
            Logger.d("torre advDataStr:" + ByteUtil.byteToString(advData));
        }
        deviceModel.deviceProtocolType = PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeTorre;
        deviceModel.deviceConnectType = PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeDirect;
        deviceModel.devicePowerType = PPScaleDefine.PPDevicePowerType.PPDevicePowerTypeCharge;
        if (advData != null && advData.length > 0) {
            int batteryPower = advData[0];
            if (batteryPower > 0 && batteryPower <= 100) {
                deviceModel.setDevicePower(batteryPower);
            }
        }
        deviceModel.deviceAccuracyType = BleSearchV2DeviceHelper.getAccuracyType(deviceModel);

        deviceModel.deviceFuncType = getFuncType(advData, deviceModel);
        if (deviceModel.getDeviceName().equals(DeviceManager.CF568_CF577) ||
                deviceModel.getDeviceName().equals(DeviceManager.CF568_CF577_FUTULA) ||
                deviceModel.getDeviceName().equals(DeviceManager.CF568_CF577_MIX)) {
            deviceModel.deviceCalcuteType = PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8;
        } else if (deviceModel.getDeviceName().equals(DeviceManager.CF568_TM_315)) {
            deviceModel.deviceCalcuteType = PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNormal;
        } else {
            deviceModel.deviceCalcuteType = PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate;
        }
        deviceModel.deviceType = PPScaleDefine.PPDeviceType.PPDeviceTypeCF;
        return deviceModel;
    }

    public static PPDeviceModel createAnkerDevice(byte[] advData, PPDeviceModel deviceModel) {
        Logger.v("Anker createAnkerDevice");
        if (advData != null && advData.length > 0) {
            Logger.d("Anker advDataStr:" + ByteUtil.byteToString(advData));
        }
        deviceModel.deviceProtocolType = PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeAnker149;
        deviceModel.deviceConnectType = PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeDirect;
        deviceModel.devicePowerType = PPScaleDefine.PPDevicePowerType.PPDevicePowerTypeBattery;
//        if (advData != null && advData.length > 0) {
//            int batteryPower = advData[0];
//            if (batteryPower > 0 && batteryPower <= 100) {
//                deviceModel.setDevicePower(batteryPower);
//            }
//        }
        deviceModel.deviceAccuracyType = BleSearchV2DeviceHelper.getAccuracyType(deviceModel);
        deviceModel.deviceFuncType = getAnkerFuncType(advData, deviceModel);
        deviceModel.deviceCalcuteType = PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNormal;
        deviceModel.deviceType = PPScaleDefine.PPDeviceType.PPDeviceTypeCF;
        return deviceModel;
    }

    /**
     * 解析秤功能
     *
     * @param deviceModel
     * @return
     */
    private static int getAnkerFuncType(byte[] advData, PPDeviceModel deviceModel) {

        int type = PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWeight.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeFat.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHeartRate.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeSafe.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeBaby.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWifi.getType();
        return type;
    }

    /**
     * 解析秤功能
     *
     * @param deviceModel
     * @return
     */
    private static int getFuncType(byte[] advData, PPDeviceModel deviceModel) {

        int type = PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWeight.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeFat.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHeartRate.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeSafe.getType();

        type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeBaby.getType();
        //bit0：WiFi功能组件：0：无WiFi组件，1：有WiFi组件
        //bit1：日本jis标准：0：不支持，1：支持
        if (advData != null && advData.length > 0) {
            byte funcType = advData[advData.length - 1];
            if ((funcType & 0x01) == 0x01) {
                type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWifi.getType();
            }
        } else {
            if (!DeviceManager.DeviceList.torreListNoWifi.contains(deviceModel.getDeviceName())) {
                type += PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWifi.getType();
            }
        }
        return type;
    }


}
