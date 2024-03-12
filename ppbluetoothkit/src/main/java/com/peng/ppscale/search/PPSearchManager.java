package com.peng.ppscale.search;

import com.peng.ppscale.business.ble.listener.PPBleStateInterface;
import com.peng.ppscale.business.ble.listener.PPSearchDeviceInfoInterface;

public class PPSearchManager {

    private BleSearchDelegate searchDelegate;

    public PPSearchManager() {
        searchDelegate = BleSearchDelegate.getInstance();
    }

    /**
     * 扫描周围蓝牙秤列表
     */
    public void startSearchDeviceList(int scanTimes, PPSearchDeviceInfoInterface searchDeviceInfoInterface, PPBleStateInterface bleStateInterface) {
        BleSearchDelegate.DURATION_TIME = scanTimes;
        searchDelegate.startSearchBluetoothScale(searchDeviceInfoInterface, bleStateInterface);
    }

    /**
     * 停止搜索
     */
    public void stopSearch() {
        searchDelegate.stopSearch();
    }


    public boolean isSearching() {
        return searchDelegate.isSearching();
    }

}
