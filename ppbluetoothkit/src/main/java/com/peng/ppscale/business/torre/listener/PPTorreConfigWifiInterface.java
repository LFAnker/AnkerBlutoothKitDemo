package com.peng.ppscale.business.torre.listener;

import com.peng.ppscale.business.ble.configWifi.PPConfigStateMenu;
import com.peng.ppscale.vo.PPWifiModel;

import java.util.List;

public class PPTorreConfigWifiInterface {

    /**
     * 配网状态
     *
     * @param configStateMenu
     * @param resultCode
     */
    public void configResult(PPConfigStateMenu configStateMenu, String resultCode) {
    }

    /**
     * Wifi列表回调
     *
     * @param wifiModels 返回的Wifi信息列表
     */
    public void monitorWiFiListSuccess(List<PPWifiModel> wifiModels) {
    }

    /**
     * 读取设备的SSID
     *
     * @param ssid
     * @param state 0 成功 1失败
     */
    public void readDeviceSsidCallBack(String ssid, int state) {
    }

    /**
     * 读取wifimac
     *
     * @param wifiMac
     */
    public void readDeviceWifiMacCallBack(String wifiMac) {

    }


    /**
     * 配网状态
     * 0x00：未配网（设备端恢复出厂或APP解除设备配网后状态）
     * 0x01：已配网（APP已配网状态）
     *
     * @param state
     */
    public void configWifiState(int state) {
    }

}
