package com.peng.ppscale.search;

import com.peng.ppscale.business.device.DeviceManager;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;

public class BleSearchFoodScaleHelper {

    public static void createFoodScaleDevice(PPDeviceModel deviceModel) {
//        deviceModel.setDeviceConnectAbled(!DeviceManager.DeviceList.DeviceListPureBroadCastScale.contains(deviceModel.getDeviceName()));
        deviceModel.deviceConnectType = (!DeviceManager.DeviceList.DeviceListPureBroadCastScale.contains(deviceModel.getDeviceName()) ?
                PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeDirect : PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeBroadcast);
        deviceModel.devicePowerType = PPScaleDefine.PPDevicePowerType.PPDevicePowerTypeBattery;
        deviceModel.setDevicePower(-1);
        deviceModel.deviceAccuracyType = BleSearchV2DeviceHelper.getAccuracyFoodType(deviceModel);
        deviceModel.deviceCalcuteType = PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNeedNot;
        if (DeviceManager.DeviceList.smartV3DeviceList.contains(deviceModel.getDeviceName())) {
            deviceModel.deviceProtocolType = PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeV3;
        } else {
            deviceModel.deviceProtocolType = PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeV2;
        }
        deviceModel.deviceType = PPScaleDefine.PPDeviceType.PPDeviceTypeCA;
    }


}
