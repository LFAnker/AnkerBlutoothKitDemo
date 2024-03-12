package com.peng.ppscale.search;

import com.peng.ppscale.business.ble.listener.PPBleStateInterface;
import com.peng.ppscale.business.ble.listener.PPSearchDeviceInfoInterface;

/**
 * 蓝牙扫描代理
 */
public interface BleSearchInterface {

    /**
     * 开始扫描
     */
    void startSearchBluetoothScale(PPSearchDeviceInfoInterface searchDeviceInfoInterface, PPBleStateInterface bleStateInterface);

    /**
     * 停止搜索
     */
    void stopSearch();

}
