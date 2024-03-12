package com.peng.ppscale.business.ble.listener

import com.peng.ppscale.business.device.PPUnitType
import com.peng.ppscale.vo.PPDeviceModel

/**
 * 设备信息回调
 */
 interface PPTorreDeviceModeChangeInterface {

    /**
     * 设备信息返回
     *
     * @param deviceModel
     */
    fun readDeviceInfoComplete(deviceModel: PPDeviceModel?) {}

    /**
     * 设备电量返回
     *
     * @param power
     */
    fun readDevicePower(power: Int) {}

    /**
     * 设置/获取设备单位
     *
     * @param type  1设置单位 2获取单位
     * @param state 0设置成功 1设置失败
     */
    fun readDeviceUnitCallBack(type: Int, state: Int, unitType: PPUnitType?) {}

    /**
     * 心率开关状态
     *
     * @param type  1设置开关 2获取开关
     * @param state 0打开 1关闭
     */
    fun readHeartRateStateCallBack(type: Int, state: Int) {}

    /**
     * 设置/获取演示模式状态
     * @param type  1设置开关 2获取开关
     * @param state 0：模式状态关闭  1：模式状态打开
     */
    fun demoModeSwitchCallBack(type: Int, state: Int) {}

    fun switchBabyModeCallBack(state: Int) {}

    /**
     * @param type  0x01：设置开关 0x02：获取开关
     * @param state 0x00：设置成功 0x01：设置失败
     * 0x00：阻抗测量打开 0x01：阻抗测量关闭
     */
    fun controlImpendanceCallBack(type: Int, state: Int) {}

    /**
     * 启动测量结果回调
     * 0x00：成功
     * 0x01：设备配网中，开始测量失败
     * 0x02：设备OTA中，开始测量失败
     *
     * @param state
     */
    fun startMeasureCallBack(state: Int) {}

    /**
     * 设备绑定状态
     *
     * @param type  0x01：设置  0x02：获取
     * @param state 0x00：设置成功 0x01：设置失败
     * 0x00：设备未绑定 0x01：设备已绑定
     */
    fun bindStateCallBack(type: Int, state: Int) {}
}
