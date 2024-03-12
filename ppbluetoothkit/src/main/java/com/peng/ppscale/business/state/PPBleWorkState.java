package com.peng.ppscale.business.state;

public enum PPBleWorkState {


    /**
     * 扫描中
     */
    PPBleWorkStateSearching,

    /**
     * 扫描超时
     */
    PPBleWorkSearchTimeOut,
    /**
     * 扫描失败
     */
    PPBleWorkSearchFail,

    /**
     * 停止扫描
     */
    PPBleStateSearchCanceled,

    /**
     * 设备连接中
     */
    PPBleWorkStateConnecting,

    /**
     * 设备已连接
     */
    PPBleWorkStateConnected,
    /**
     * 连接失败
     */
    PPBleWorkStateConnectFailed,

    /**
     * 设备已断开
     */
    PPBleWorkStateDisconnected,

    /**
     * 可写
     */
    PPBleWorkStateWritable

}
