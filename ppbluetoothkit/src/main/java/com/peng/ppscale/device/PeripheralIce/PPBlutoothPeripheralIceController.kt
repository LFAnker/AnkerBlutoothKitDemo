package com.peng.ppscale.device.PeripheralIce

import com.inuker.bluetooth.library.Code
import com.inuker.bluetooth.library.model.BleGattProfile
import com.peng.ppscale.PPBlutoothKit
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiInfoInterface
import com.peng.ppscale.business.ble.connect.ConnectHelper
import com.peng.ppscale.business.ble.listener.*
import com.peng.ppscale.business.device.PPUnitType
import com.peng.ppscale.business.ota.OnOTAStateListener
import com.peng.ppscale.business.state.PPBleWorkState
import com.peng.ppscale.business.torre.LFTimerTask
import com.peng.ppscale.business.torre.LFTimerTask.RunCallBack
import com.peng.ppscale.business.v4.ProtocalV4DataHelper
import com.peng.ppscale.business.v4.V4Delegate
import com.peng.ppscale.business.v4.V4DelegateHelper
import com.peng.ppscale.device.PPBlutoothPeripheralBaseController
import com.peng.ppscale.util.Logger
import com.peng.ppscale.vo.PPDeviceModel

/**
 * 对应的协议: 4.x
 * 连接类型:连接
 * 设备类型 人体秤
 */
class PPBlutoothPeripheralIceController : PPBlutoothPeripheralBaseController() {

    init {
        V4Delegate.getInstance().bindBleClient(PPBlutoothKit.bluetoothClient)
    }

    override fun onConnectResponse(code: Int, bleGattProfile: BleGattProfile?) {
        Logger.d("$tag connect device onResponse code:$code deviceMac:${deviceModel?.deviceMac}")
        if (code == Code.REQUEST_SUCCESS) {
            V4Delegate.getInstance().bindDevice(deviceModel)
            ConnectHelper.monitoV4Response(bleGattProfile, V4Delegate.getInstance())
        } else {
            bleStateInterface?.monitorBluetoothWorkState(PPBleWorkState.PPBleWorkStateConnectFailed, deviceModel)
        }
    }

    override fun startConnect(deviceModel: PPDeviceModel, bleStateInterface: PPBleStateInterface?) {
        V4Delegate.getInstance().setBleStateInterface(bleStateInterface)
        super.startConnect(deviceModel, bleStateInterface)
    }

    fun disWifi(sendResultCallBack: PPBleSendResultCallBack?) {
        super.disConnect()
    }

    fun registDataChangeListener(dataChangeListener: PPDataChangeListener) {
        ProtocalV4DataHelper.instance?.setDataChangeListener(dataChangeListener)
    }

    fun readDeviceInfo(deviceInfoInterface: PPDeviceInfoInterface?) {
        V4Delegate.getInstance().readDeviceInfoFromCharacter(deviceInfoInterface)
    }

    /**
     * 普通体脂秤同步时间
     */
    fun syncTime(sendResultCallBack: PPBleSendResultCallBack?) {
        V4Delegate.getInstance().syncTime(sendResultCallBack)
    }

    fun syncUnit(userUnit: PPUnitType?, sendResultCallBack: PPBleSendResultCallBack?) {
        V4Delegate.getInstance().syncUnit(userUnit, sendResultCallBack)
    }

    /**
     * 获取历史数据
     */
    fun getHistory(historyDataInterface: PPHistoryDataInterface) {
        V4Delegate.getInstance().getHistory(historyDataInterface)
    }

    fun getTime() {
        V4Delegate.getInstance().getTime()
    }

    fun deleteHistoryData(bleSendListener: PPBleSendResultCallBack?) {
        V4Delegate.getInstance().deleteHistory(bleSendListener)
    }

    /**
     * 测值模式
     *
     * @param state 0打开 1关闭
     */
    fun controlImpendance(state: Int, modeChangeInterface: PPTorreDeviceModeChangeInterface?) {
        V4Delegate.getInstance().controlImpendance(state, modeChangeInterface)
    }

    fun getImpendanceState(modeChangeInterface: PPTorreDeviceModeChangeInterface?) {
        V4Delegate.getInstance().getImpendanceState(modeChangeInterface)
    }

    /**
     * @param  state   心率 0打开 1关闭
     */
    fun controlHeartRate(state: Int, modeChangeInterface: PPTorreDeviceModeChangeInterface?) {
        V4Delegate.getInstance().controlHeartRate(state, modeChangeInterface)
    }

    /**
     * 获取心率状态
     */
    fun getHeartRateState(modeChangeInterface: PPTorreDeviceModeChangeInterface?) {
        V4Delegate.getInstance().getHeartRateState(modeChangeInterface)
    }

    /**
     * 切换婴儿模式
     *
     * @param mode 00使能抱婴模式 01退出抱婴模式
     */
    fun exitBaby() {
        V4Delegate.getInstance().switchBaby(1, null)
    }

    /**
     * 切换婴儿模式
     *
     * @param mode 00使能抱婴模式 01退出抱婴模式
     */
    fun startBaby() {
        V4Delegate.getInstance().switchBaby(0, null)
    }

    /**
     * 切换婴儿模式
     *
     * @param mode 00使能抱婴模式 01退出抱婴模式
     */
    fun startPet() {
        V4Delegate.getInstance().switchPet(0, null)
    }

    /**
     * 切换婴儿模式
     *
     * @param mode 00使能抱婴模式 01退出抱婴模式
     */
    fun exitPet() {
        V4Delegate.getInstance().switchPet(1, null)
    }

    /**
     * 启动配网
     */
    fun configWifiStart(configWifiInfoInterface: PPConfigWifiInfoInterface) {
        V4Delegate.getInstance().configWifiStart(configWifiInfoInterface)
    }

    /**
     * 下发配网code、uid、服务器域名
     *
     * @param code
     * @param uid
     * @param url
     * @param configWifiInfoInterface
     */
    fun configNewCodeUidUrl(code: String, uid: String, url: String, configWifiInfoInterface: PPConfigWifiInfoInterface?) {
        V4Delegate.getInstance().configNewCodeUidUrl(code, uid, url, configWifiInfoInterface)
    }

    /**
     * 下发域名证书
     */
    fun configDomainCertificate(domainCertificate: String, configWifiInfoInterface: PPConfigWifiInfoInterface?) {
        V4Delegate.getInstance().configDomainCertificate(domainCertificate, configWifiInfoInterface)
    }

    /**
     * 删除WiFi参数
     *
     * @param configWifiInfoInterface
     */
    fun configDeleteWifi(configWifiInfoInterface: PPConfigWifiInfoInterface?) {
        V4Delegate.getInstance().configDeleteWifi(configWifiInfoInterface)
    }

    /**
     * 查询WiFi参数
     */
    fun getWifiInfo(configWifiInfoInterface: PPConfigWifiInfoInterface?) {
        V4Delegate.getInstance().getWifiInfo(configWifiInfoInterface)
    }

    /**
     * 更新WiFi参数(配网)-路由器名称
     *
     * @param ssid
     * @param configWifiInfoInterface
     */
    fun configUpdateWifiSSID(ssid: String?, configWifiInfoInterface: PPConfigWifiInfoInterface?) {
        V4Delegate.getInstance().configUpdateWifiSSID(ssid, configWifiInfoInterface)
    }

    /**
     * 更新WiFi参数(配网)-路由器密码
     *
     * @param pwd
     * @param configWifiInfoInterface
     */
    fun configUpdateWifiPassword(pwd: String?, configWifiInfoInterface: PPConfigWifiInfoInterface?) {
        V4Delegate.getInstance().configUpdateWifiPassword(pwd, configWifiInfoInterface)
    }

    /**
     * 更新WiFi参数(配网)-结束
     *
     * @param configWifiInfoInterface
     */
    fun startConnectRouter(configWifiInfoInterface: PPConfigWifiInfoInterface?) {
        V4Delegate.getInstance().configUpdateWifiFinish(configWifiInfoInterface)
    }

    /**
     * 获取wifi列表
     */
    fun getWifiList(configWifiInfoInterface: PPConfigWifiInfoInterface) {
        V4Delegate.getInstance().getWifiList(configWifiInfoInterface)
    }

    /**
     * 退出配网
     */
    fun exitConfigWifi() {
        V4Delegate.getInstance().exitConfigWifi()
    }

    /**
     * 秤亮灯指令
     */
    fun openScaleLight() {
        V4Delegate.getInstance().openScaleLight()
    }

    //获取设备绑定状态
    fun getDeviceBindState() {
        V4Delegate.getInstance().getDeviceBindState()
    }

    //获取设备Token 状态
    fun getDeviceTokenState() {
        V4Delegate.getInstance().getDeviceTokenState()
    }

    //准备更新Token
    fun prepareUpdateToken() {
        V4Delegate.getInstance().prepareUpdateToken()
    }

    fun sendResetDevice(setInfoInterface: PPDeviceSetInfoInterface?) {
        V4Delegate.getInstance().resetDevice(setInfoInterface)
    }

    fun sendDeleteHistoryData(bleSendListener: PPBleSendResultCallBack?) {
        V4Delegate.getInstance().deleteHistory(bleSendListener)
    }

    //设备模式查询
    fun getCurrDeviceModel() {
        V4Delegate.getInstance().getCurrDeviceModel()
    }


    //获取当前wifi Rssi
    fun getCurrWifiRSSI() {
        V4Delegate.getInstance().getCurrWifiRSSI()
    }

    /**
     * 恢复出厂
     */
    fun resetDevice(ppDeviceSetInfoInterface: PPDeviceSetInfoInterface?) {
        V4Delegate.getInstance().resetDevice(ppDeviceSetInfoInterface)
    }

    /**
     * 读取设备电量
     */
    fun readDeviceBattery(ppDeviceInfoInterface: PPDeviceInfoInterface?) {
        V4Delegate.getInstance().readDeviceBattery(ppDeviceInfoInterface)
    }

    //配网心跳包
    fun startKeepAlive() {
        Logger.d("$tag startKeepAlive")
        LFTimerTask.getInstance().startDelay()
        LFTimerTask.getInstance().setRunCallBack(runCallBack)
    }

    fun stopKeepAlive() {
        Logger.d("$tag stopKeepAlive")
        LFTimerTask.getInstance().stopDelay()
    }

    var runCallBack = RunCallBack {
        Logger.v("$tag RunCallBack keepAlive")
        V4Delegate.getInstance().keepAlive()
    }


}
