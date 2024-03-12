package com.peng.ppscale.business.v4

import android.content.Context
import com.inuker.bluetooth.library.beacon.BeaconItem
import com.inuker.bluetooth.library.utils.ByteUtils
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiInfoInterface
import com.peng.ppscale.business.ble.listener.PPDataChangeListener
import com.peng.ppscale.business.ble.listener.PPDeviceLogInterface
import com.peng.ppscale.business.ble.listener.PPDeviceSetInfoInterface
import com.peng.ppscale.business.ble.listener.PPHistoryDataInterface
import com.peng.ppscale.business.ble.listener.PPTorreDeviceModeChangeInterface
import com.peng.ppscale.business.ble.listener.ProtocalFilterImpl
import com.peng.ppscale.business.ota.OnOTAStateListener
import com.peng.ppscale.business.protocall.ProtocalDelegate
import com.peng.ppscale.business.protocall.ProtocalNormalDeviceHelper
import com.peng.ppscale.business.torre.TorreHelper
import com.peng.ppscale.business.v4.BleSendCrypt.OnEncryptDataAnalysisCallBak
import com.peng.ppscale.business.v4.DataReveiveHelper.parseNewBodyFatData
import com.peng.ppscale.util.ByteUtil
import com.peng.ppscale.util.DateUtil
import com.peng.ppscale.util.FileUtilCallBack
import com.peng.ppscale.util.FileUtilsKotlin.createFileAndWrite
import com.peng.ppscale.util.Logger
import com.peng.ppscale.vo.PPBodyBaseModel
import com.peng.ppscale.vo.PPDeviceModel
import com.peng.ppscale.vo.PPWifiModel
import com.peng.ppscalelibrary.BuildConfig
import java.util.Arrays

class ProtocalV4DataHelper private constructor() {
    private var stringBuffer: StringBuffer? = null
    private val numberList: MutableList<String>? = ArrayList() //存包序号，防止重复接收数据
    private val wifiModels: MutableList<PPWifiModel> = ArrayList() //存包序号，防止重复接收数据
    var logFilePath: String? = null
    private var lastTimes: Long = 0 //用于过滤相同数据，但是又不是同一组数据3s之外
    private var configWifiInfoInterface: PPConfigWifiInfoInterface? = null
    private var torreDeviceLogInterface: PPDeviceLogInterface? = null
    private var dataChangeListener: PPDataChangeListener? = null
    private var deviceSetInfoInterface: PPDeviceSetInfoInterface? = null
    private var historyDataInterface: PPHistoryDataInterface? = null
    private var otaStateListener: OnOTAStateListener? = null
    var modeChangeInterface: PPTorreDeviceModeChangeInterface? = null
    var baseModel: PPBodyBaseModel? = null
    var context: Context? = null
    private var logLen: Long = 0

    //    private String lastReciveData = "";
    var lastReciveData: ByteArray? = null
    fun init(context: Context?) {
        this.context = context
    }

    private fun modeChange(reciveData: String) {
        if (modeChangeInterface != null) {
            if (reciveData.equals("55FD41BCAA", ignoreCase = true)) {
                //孕妇模式失效
                modeChangeInterface?.controlImpendanceCallBack(1, 0)
            } else if (reciveData.equals("55FD38C5AA", ignoreCase = true)) {
                //孕妇模式生效
                modeChangeInterface?.controlImpendanceCallBack(1, 0)
            } else if (reciveData.equals("55FD44B9AA", ignoreCase = true)) {
                //心率测试生效
                modeChangeInterface?.readHeartRateStateCallBack(1, 0)
            } else if (reciveData.equals("55FD43BEAA", ignoreCase = true)) {
                //心率测试不生效
                modeChangeInterface?.readHeartRateStateCallBack(1, 0)
            } else if (reciveData.startsWith("FDA0")) {
                val impendanceHex = reciveData.substring(4, 6) //安全模式 0-失效，支持测脂。1-使能 不支持测脂
                val impendanceState = ByteUtil.hexToTen(impendanceHex)
                modeChangeInterface?.controlImpendanceCallBack(2, impendanceState)
                val HeartRateHex = reciveData.substring(6, 8) //心率禁用 0-失效 支持心率测试 1-使能 不支持心率测试
                val HeartRateState = ByteUtil.hexToTen(HeartRateHex)
                modeChangeInterface?.readHeartRateStateCallBack(2, HeartRateState)
            }
        }
    }

    private fun analyticalCF(reciveData: ByteArray, deviceModel: PPDeviceModel) {
        if (reciveData.size == 16 && reciveData[2] == 0x00.toByte()) {
            //实时数据
            if (lastReciveData != null && Arrays.equals(lastReciveData, reciveData) && System.currentTimeMillis() - lastTimes < 2500) {
                lastTimes = System.currentTimeMillis()
                return
            }
            lastReciveData = reciveData
            if (baseModel == null) {
                baseModel = PPBodyBaseModel()
            }
            baseModel?.deviceModel = deviceModel
            baseModel?.let {
                parseNewBodyFatData(reciveData, it, dataChangeListener)
            }
            baseModel = null
        }
    }

    fun analysiEncryptData(value: ByteArray, currentDevice: PPDeviceModel) {
        if (value[0] == 0xCF.toByte()) {
            analyticalCF(value, currentDevice)
        } else {
            BleSendCrypt.getInstance().setOnEncryptDataAnalysisCallBak(onEncryptDataAnalysisCallBak)
            BleSendCrypt.getInstance().decrypt(value)
        }
    }

    private fun protocoDataF4(data: ByteArray) {
        when (data[0]) {
            // 体脂数据
            0xCF.toByte() -> {
                if (data.size > 3) {
                    if (data[2] == 0x01.toByte()) {
                        //历史数据
                        DataReveiveHelper.parseNewHistoryData(data, historyDataInterface)
                    }
                }
            }
            // 获取设备绑定状态
            0x0F.toByte() -> {
                if (data.size == 3) {
                    if (data[2] == 0x00.toByte()) {
//                        LiveDataBus.sendLiveBean(NetLiveDataConst.GET_DEVICE_UNBIND_STATE, 0)
                    }
                }
            }

            0xF2.toByte() -> {
                if (data[2] == 0x03.toByte()) {
                    //历史数据同步完成
                    historyDataInterface?.monitorHistoryEnd()
                } else if (data[2] == 0x01.toByte()) {
                    //删除历史数据成功
                    historyDataInterface?.historyDeleteResult(true)
                }
//                else if (data[2] == 0x02.toByte()) {
//                    //删除历史数据失败
//                }
            }
            //--------- 配网流程开始 回复------//
            //配网异常上报
            0xE2.toByte() -> {
                if (data.size == 7) {
                    configWifiInfoInterface?.monitorConfigFail(data[3], data[4], com.peng.ppscale.business.v4.ByteUtil.byte2Hex(data))
                }
            }

            //配网结果
            0x06.toByte() -> {
                if (data[2] == 0x00.toByte()) {
                    //配网成功
                    configWifiInfoInterface?.monitorConfigResult(true)
                } else if (data[2] == 0x01.toByte()) {
                    //配网失败
                    configWifiInfoInterface?.monitorConfigResult(false)
                } else if (data[2] == 0x02.toByte()) {
                    Logger.d("$TAG startConfigWifi WifiConfigStep_Start")
                    //开始配网
                    configWifiInfoInterface?.monitorConfigStep(WifiConfigStep.WifiConfigStep_Start, true)
                }
            }
            // 回复下发wifi名称
            0x0A.toByte() -> {
                if (data.size == 3) {
                    configWifiInfoInterface?.monitorConfigStep(WifiConfigStep.WifiConfigStep_SSID, true)
                }
            }
            //回复下发wifi密码
            0x0B.toByte() -> {
                if (data.size == 3) {
                    configWifiInfoInterface?.monitorConfigStep(WifiConfigStep.WifiConfigStep_PWD, true)
                }
            }

            //回复下发配网code、uid、服务器域名
            0xF8.toByte() -> {
                if (data[2] == 0x00.toByte()) {
                    //成功
                    configWifiInfoInterface?.monitorConfigStep(WifiConfigStep.WifiConfigStep_SendCodeUidDomain, true)
                } else if (data[2] == 0x01.toByte()) {
                    //失败，需要重新发送配网code
                    configWifiInfoInterface?.monitorConfigStep(WifiConfigStep.WifiConfigStep_SendCodeUidDomain, false)
                }
            }

            //回复下发域名证书
            0xF7.toByte() -> {
                if (data[2] == 0x00.toByte()) {
                    //成功
//                    configWifiInfoInterface?.monitorConfigStep(WifiConfigStep.WifiConfigStep_DomainCertificate, true)
                    Logger.d("configWifi send Domain Certificate data success")
                    V4Delegate.getInstance().configFinish()
                } else if (data[2] == 0x01.toByte()) {
                    //失败
                    Logger.e("configWifi send Domain Certificate data error")
                    configWifiInfoInterface?.monitorConfigStep(WifiConfigStep.WifiConfigStep_DomainCertificate, false)
                }
            }

            //回复下发证书结束
            0xF6.toByte() -> {
                if (data[2] == 0x00.toByte()) {
                    //成功
                    Logger.d("configWifi send Domain Certificate result success")
                    configWifiInfoInterface?.monitorConfigStep(WifiConfigStep.WifiConfigStep_DomainCertificate, true)
                } else if (data[2] == 0x01.toByte()) {
                    //失败
                    Logger.e("configWifi send Domain Certificate result fail")
                    configWifiInfoInterface?.monitorConfigStep(WifiConfigStep.WifiConfigStep_DomainCertificate, false)
                }
            }

            //回复成功连接上网路（wifi 账号，密码正确）
            0X0E.toByte() -> {
                if (data.size == 3) {
                    configWifiInfoInterface?.monitorConfigStep(WifiConfigStep.WifiConfigStep_ConnectToRouter, true)
                }
            }

            // 解码错误，需要重新执行发送流程
            0x0C.toByte() -> {
                // 1.重新执行配网流程
                // 2.重新执行wifi参数更新流程
            }

            //回复删除WiFi参数
            0xF4.toByte() -> {
                if (data[2] == 0x00.toByte()) {
                    //成功

                } else if (data[2] == 0x01.toByte()) {
                    //失败
                }
            }

            //返回WiFi列表
            0x07.toByte() -> {
                DataReveiveHelper.parseGetWifiList(data, configWifiInfoInterface)
            }

            else -> {
                DataReveiveHelper.parseCmdData(data)
            }

        }
    }

    fun setConfigWifiInfoInterface(configWifiInfoInterface: PPConfigWifiInfoInterface?) {
        this.configWifiInfoInterface = configWifiInfoInterface
    }

    fun setTorreDeviceLogInterface(logFilePath: String?, torreDeviceLogInterface: PPDeviceLogInterface?) {
        this.logFilePath = logFilePath
        this.torreDeviceLogInterface = torreDeviceLogInterface
    }

    fun setResetDeviceInterface(deviceSetInfoInterface: PPDeviceSetInfoInterface?) {
        this.deviceSetInfoInterface = deviceSetInfoInterface
    }

    fun setHistoryDataInterface(historyDataInterface: PPHistoryDataInterface?) {
        this.historyDataInterface = historyDataInterface
    }

    fun setOtaStateListener(otaStateListener: OnOTAStateListener?) {
        this.otaStateListener = otaStateListener
    }

    fun setInterface(protocalFilter: ProtocalFilterImpl) {
        historyDataInterface = protocalFilter.historyDataInterface
        deviceSetInfoInterface = protocalFilter.deviceSetInfoInterface
        configWifiInfoInterface = protocalFilter.configWifiInfoInterface
        V4Delegate.getInstance().setDeviceSetInfoInterface(deviceSetInfoInterface)
    }

    fun clearWifiModels() {
        wifiModels.clear()
    }

    fun setDataChangeListener(dataChangeListener: PPDataChangeListener?) {
        this.dataChangeListener = dataChangeListener
    }

    fun setTorreDeviceModeChangeInterface(modeChangeInterface: PPTorreDeviceModeChangeInterface?) {
        this.modeChangeInterface = modeChangeInterface
    }

    var onEncryptDataAnalysisCallBak = OnEncryptDataAnalysisCallBak { data -> protocoDataF4(data) }

    companion object {
        @JvmStatic
        @Volatile
        var instance: ProtocalV4DataHelper? = null
            get() {
                if (field == null) {
                    synchronized(ProtocalV4DataHelper::class.java) {
                        if (field == null) {
                            field = ProtocalV4DataHelper()
                        }
                    }
                }
                return field
            }
            private set
        private val TAG = ProtocalV4DataHelper::class.java.simpleName
        fun parseBeacon(bytes: ByteArray): List<BeaconItem?> {
            val items: ArrayList<BeaconItem?> = ArrayList()
            var item: BeaconItem?
            var i = 0
            while (i < bytes.size) {
                item = parse(bytes, i)
                if (item == null) {
                    break
                }
                items.add(item)
                i += item.len + 2
            }
            return items
        }

        fun parse(bytes: ByteArray, startIndex: Int): BeaconItem? {
            var item: BeaconItem? = null
            if (bytes.size - startIndex >= 2) {
                val length = bytes[startIndex + 1].toInt()
                if (length > 0) {
                    val type = bytes[startIndex]
                    val firstIndex = startIndex + 2
                    if (firstIndex < bytes.size) {
                        item = BeaconItem()
                        var endIndex = firstIndex + length - 1
                        if (endIndex >= bytes.size) {
                            endIndex = bytes.size - 1
                        }
                        item.type = type.toInt() and 255
                        item.len = length
                        item.bytes = ByteUtils.getBytes(bytes, firstIndex, endIndex)
                    }
                }
            }
            return item
        }
    }
}