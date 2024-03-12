package com.peng.ppscale.business.ble.listener;

import com.peng.ppscale.vo.PPDeviceModel;

public interface PPSearchDeviceInfoInterface {

    /**
     * 扫描设备,返回单个设备信息，给外部做搜素列表使用
     *
     * @param deviceModel 设备对象
     */
    void onSearchDevice(PPDeviceModel deviceModel, String data);

}
