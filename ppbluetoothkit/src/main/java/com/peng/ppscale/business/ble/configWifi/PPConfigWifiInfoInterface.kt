package com.peng.ppscale.business.ble.configWifi

import com.peng.ppscale.business.v4.WifiConfigStep
import com.peng.ppscale.vo.PPDeviceModel
import com.peng.ppscale.vo.PPWifiModel

/**
 * 蓝牙wifi秤的信息
 */
interface PPConfigWifiInfoInterface {
    fun monitorConfigResult(isSuccess:Boolean) {}

    fun monitorConfigStep(step: WifiConfigStep, isSuccess:Boolean) {}

    /**
     * 读取到的SSID
     *
     * @param ssid
     * @param deviceModel
     */
    fun monitorConfigSsid(ssid: String?, deviceModel: PPDeviceModel?) {}

    /**
     * 读取到的password
     *
     * @param password
     * @param deviceModel
     */
    fun monitorConfigPassword(password: String?, deviceModel: PPDeviceModel?) {}
    fun monitorModifyServerDomainSuccess() {}
    fun monitorModifyServerIpSuccess() {}

    /**
     * @param configStep 当前进行到的步骤，  WIFI准备配网 - 0x11；下发device id - 0x12；下发product code - 0x13；配网开始指令 - 0x14；接收域名、uid - 0x15；接收证书 - 0x16；证书接收完成 - 0x17；WIFI list-  0x18；接收SSID - 0x19；接收password - 0x1A；配网完成指令 - 0x1B；删除WIFI参数 - 0x1C；连接路由器-0x25
     * @param errorType 超时 - 0x01；接收到wifi错误码 - 0x02；接收到云端返回res_code错误码 - 0x0d
     * @param resultCode 接收到的数据
     */
    fun monitorConfigFail(configStep: Byte, errorType: Byte, resultCode: String?) {}

    fun monitorWiFiListSuccess(wifiModels: MutableList<PPWifiModel>?) {}

    /**
     * 默认失败 0， 电量低：1
     */
    fun monitorWiFiListFail(state: Int? = 0) {}

}