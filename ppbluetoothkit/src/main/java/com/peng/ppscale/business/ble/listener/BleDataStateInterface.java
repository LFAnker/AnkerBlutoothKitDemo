package com.peng.ppscale.business.ble.listener;


public interface BleDataStateInterface {
    /*历史数据*/
    void deleteHistoryData();

    void connectDevice(int deviceType);

    void disConnect();

    void disConnectForced();

    /**
     * 终止，配网重试
     */
    void stopRetryConfig();

}
