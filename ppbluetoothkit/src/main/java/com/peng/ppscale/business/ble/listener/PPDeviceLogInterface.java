package com.peng.ppscale.business.ble.listener;

/**
 * 设备日志
 */
public interface PPDeviceLogInterface {

    void syncLogStart();

    void syncLoging(int progress);

    void syncLogEnd(String logFilePath);

}
