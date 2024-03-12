package com.peng.ppscale.business.ble.listener;

import com.peng.ppscale.vo.PPDeviceModel;

/**
 * 设备信息回调
 */
public class PPDeviceInfoInterface {

    public void onLightIntensityChange(int intensity) {
    }

    public void readDeviceInfoComplete(PPDeviceModel deviceModel) {
    }

    public void readDevicePower(int power) {

    }

}
