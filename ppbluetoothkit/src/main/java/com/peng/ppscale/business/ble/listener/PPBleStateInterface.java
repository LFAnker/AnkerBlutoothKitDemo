package com.peng.ppscale.business.ble.listener;

import com.peng.ppscale.business.state.PPBleSwitchState;
import com.peng.ppscale.business.state.PPBleWorkState;
import com.peng.ppscale.vo.PPDeviceModel;

/**
 * 蓝牙相关状态
 */
public class PPBleStateInterface {

    /**
     * 蓝牙扫描和连接状态监听
     *
     * @param ppBleWorkState
     */
    public void monitorBluetoothWorkState(PPBleWorkState ppBleWorkState, PPDeviceModel deviceModel) {
    }

    /**
     * 蓝牙开关状态回调
     *
     * @param ppBleSwitchState
     */
    public void monitorBluetoothSwitchState(PPBleSwitchState ppBleSwitchState) {

    }

    /**
     * 只支持Torre：Torre设备MTU请求成功后返回
     *
     * @param deviceModel
     */
    public void monitorMtuChange(PPDeviceModel deviceModel) {

    }


}
