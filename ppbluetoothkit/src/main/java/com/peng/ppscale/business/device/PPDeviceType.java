package com.peng.ppscale.business.device;

import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;

public interface PPDeviceType {

   /* PPDeviceTypeWeight >> 1
    PPDeviceTypeBodyFat >> 2
    PPDeviceTypeHearRate >> 3
    PPDeviceTypeHistory >> 4
    PPDeviceTypeBMDJ >> 5
    PPDeviceTypeCalcuteInScale >> 6
    PPDeviceTypeFoodScale >> 7*/

    int PPDeviceTypeWeight = 1;
    int PPDeviceTypeBodyFat = 1 << 1;
    int PPDeviceTypeHearRate = 1 << 2;
    int PPDeviceTypeHistory = 1 << 3;
    int PPDeviceTypeBMDJ = 1 << 4;
    int PPDeviceTypeCalcuteInScale = 1 << 5;
    int PPDeviceTypeBleConfig = 1 << 6;
    int PPDeviceTypeFoodScale = 1 << 7;

    interface Contants {
        int ALL = 0x3F;//111111
        int WEIGHT = 0x01;//0001
        int FAT = 0x03;//0011
        int FAT_HEARTRATE = 0x07;//000111
        int FAT_HISTORY = 0x0B;//001011
        int WEIGHT_HISTORY = 0x09;//001001
        int FAT_HEARTRATE_AND_HISTORY = 0x0F;//001111
        int FAT_AND_BMDJ = 0x13;//010011
        int FAT_AND_HEARTRATE_BMDJ = 0x17;//010111
        int FAT_AND_HISTORY_BMDJ = 0x1B;//011011
        int FAT_AND_HEARTRATE_HISTORY_BMDJ = 0x1F;//011111
        int FAT_AND_CALCUTE_IN_SCALE = 0x21;//100001
        int CONFIG_WIFI = 0x40; // 0100 0000
        int CONFIG_WIFI_HISTORY = 0x4B; // 0100 1001
        int CONFIG_FOOD_SCALE = 0x4B; // 1000 0000

    }

    class Scale {

        public static boolean isBMDJScale(int deviceType) {
            return (deviceType & PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeBMDJ.getType())
                    == PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeBMDJ.getType();
        }

        public static boolean isConfigWifiScale(int deviceType) {
            return (deviceType & PPDeviceType.Contants.CONFIG_WIFI) == PPDeviceType.Contants.CONFIG_WIFI;
        }


        public static boolean isWeightScale(String deviceName) {
            return DeviceManager.DeviceList.DeviceListWeight.contains(deviceName);
        }

        public static boolean isEncryptScale(String deviceName) {
            return  DeviceManager.DeviceList.DeviceListEncryptScale.contains(deviceName);
        }

    }


}
