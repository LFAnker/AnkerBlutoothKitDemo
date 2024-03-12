package com.peng.ppscale.business.ble.listener

import com.peng.ppscale.vo.PPDeviceModel

/**
 * 恢复出厂设备
 */
interface PPDeviceSetInfoInterface {
    /**
     * 恢复出厂成功
     */
    fun monitorResetStateSuccess(){}
    fun monitorResetStateFail(){}
    fun monitorLightValueChange(light: Int){}
    fun monitorLightReviseSuccess(){}
    fun monitorLightReviseFail(){}
}