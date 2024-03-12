package com.peng.ppscale.vo

import com.peng.ppscale.business.device.DeviceManager
import com.peng.ppscale.business.torre.TorreHelper
import com.peng.ppscale.vo.PPScaleDefine.*
import java.io.Serializable

class PPDeviceModel : Serializable {
    var deviceMac: String//设备mac设备唯一标识
    var deviceName: String //设备蓝牙名称，设备名称标识

    /**
     * 电量 -1标识不支持 >0为有效值
     */
    var devicePower = -1

    /**
     * 蓝牙信号强度
     */
    var rssi = 0

    /**
     * 固件版本号
     */
    var firmwareVersion: String? = null

    /**
     * 硬件版本号
     */
    var hardwareVersion: String? = null

    /**
     * 制造商
     */
    var manufacturerName: String? = null

    /**
     * 软件版本号
     */
    var softwareVersion: String? = null

    /**
     * 序列号
     */
    var serialNumber: String? = null

    /**
     * 时区编号
     */
    var modelNumber: String? = null

    //计算版本
    var calculateVersion: String? = null

    /**
     * 设备类型
     *
     * @see PPScaleDefine.PPDeviceType
     */
    @JvmField
    var deviceType = PPScaleDefine.PPDeviceType.PPDeviceTypeUnknow

    /**
     * 协议模式
     *
     * @see PPScaleDefine.PPDeviceProtocolType
     */
    @JvmField
    var deviceProtocolType = PPDeviceProtocolType.PPDeviceProtocolTypeUnknow

    /**
     * 计算方式
     *
     * @see PPScaleDefine.PPDeviceCalcuteType
     */
    @JvmField
    var deviceCalcuteType = PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate

    /**
     * 精度
     *
     * @see PPScaleDefine.PPDeviceAccuracyType
     */
    @JvmField
    var deviceAccuracyType = PPDeviceAccuracyType.PPDeviceAccuracyTypePoint01

    /**
     * 供电模式
     *
     * @see PPScaleDefine.PPDevicePowerType
     */
    @JvmField
    var devicePowerType = PPDevicePowerType.PPDevicePowerTypeBattery

    /**
     * 设备连接类型，用于必须直连的状态
     *
     * @see PPScaleDefine.PPDeviceConnectType
     */
    @JvmField
    var deviceConnectType = PPDeviceConnectType.PPDeviceConnectTypeDirect

    /**
     * 功能类型，可多功能叠加
     *
     * @see PPScaleDefine.PPDeviceFuncType
     */
    @JvmField
    var deviceFuncType = 0

    /**
     * 支持的单位
     *
     * @see PPScaleDefine.PPDeviceUnitType
     */
    @JvmField
    var deviceUnitType = ""

    /**
     * 协议单包的长度
     */
    @JvmField
    var mtu = TorreHelper.normalMtuLen

    /**
     * 光照强度，设备为光能充电款时生效
     */
    var illumination = -1

    //广播数据长度
    var advLength = 0

    //mac起始位置
    var macAddressStart = 0

    //设备ID-服务器返回的设备列表中id对应
    var deviceSettingId = 0

    var imgUrl: String? = null

    /**
     * 设备分组类型
     */
//    var peripheralType: PPDevicePeripheralType? = getDevicePeripheralType()

    constructor(deviceMac: String, deviceName: String) {
        this.deviceMac = deviceMac
        this.deviceName = deviceName
    }

    constructor(deviceMac: String, deviceName: String, batteryPower: Int) {
        this.deviceMac = deviceMac
        this.deviceName = deviceName
        devicePower = batteryPower
    }

    fun getDevicePeripheralType(): PPDevicePeripheralType {
        if (this.deviceProtocolType == PPDeviceProtocolType.PPDeviceProtocolTypeAnker149) {
            return PPDevicePeripheralType.PeripheralIce
        }
        return PPDevicePeripheralType.PeripheralIce
    }

    override fun toString(): String {
        if (devicePowerType == PPDevicePowerType.PPDevicePowerTypeSolar) {
            return "PPDeviceModel{deviceMac=${deviceMac}\n" +
                    ", deviceName=$deviceName \n" +
                    ", devicePower=$devicePower \n" +
                    ", rssi=$rssi\n" +
                    ", firmwareVersion=$firmwareVersion\n" +
                    ", hardwareVersion=$hardwareVersion\n" +
                    ", serialNumber=$serialNumber\n" +
                    ", modelNumber=$modelNumber\n" +
                    ", deviceType=$deviceType\n" +
                    ", deviceProtocolType=$deviceProtocolType\n" +
                    ", deviceCalcuteType=$deviceCalcuteType\n" +
                    ", deviceAccuracyType=$deviceAccuracyType\n" +
                    ", devicePowerType=$devicePowerType\n" +
                    ", deviceConnectType=$deviceConnectType\n" +
                    ", deviceFuncType=$deviceFuncType\n" +
                    ", deviceUnitType=$deviceUnitType\n" +
                    ", illumination=$illumination\n" +
                    ", deviceSettingId=$deviceSettingId\n" +
                    ", imgUrl=$imgUrl\n" +
                    ", peripheralType=${getDevicePeripheralType()}"
        } else if (deviceProtocolType == PPDeviceProtocolType.PPDeviceProtocolTypeTorre) {
            return "PPDeviceModel{deviceMac=${deviceMac}\n" +
                    ", deviceName=${deviceName} \n" +
                    ", devicePower=$devicePower \n" +
                    ", rssi=$rssi\n" +
                    ", firmwareVersion=$firmwareVersion\n" +
                    ", hardwareVersion=$hardwareVersion\n" +
                    ", serialNumber=$serialNumber\n" +
                    ", modelNumber=$modelNumber\n" +
                    ", deviceType=$deviceType\n" +
                    ", deviceProtocolType=$deviceProtocolType\n" +
                    ", deviceCalcuteType=$deviceCalcuteType\n" +
                    ", deviceAccuracyType=$deviceAccuracyType\n" +
                    ", devicePowerType=$devicePowerType\n" +
                    ", deviceConnectType=$deviceConnectType\n" +
                    ", deviceFuncType=$deviceFuncType\n" +
                    ", deviceUnitType=$deviceUnitType\n" +
                    ", mtu=$mtu\n" +
                    ", deviceSettingId=$deviceSettingId\n" +
                    ", imgUrl=$imgUrl\n" +
                    ", peripheralType=${getDevicePeripheralType()}"
        } else {
            return "PPDeviceModel{deviceMac=${deviceMac}\n" +
                    ", deviceName=$deviceName \n" +
                    ", devicePower=$devicePower \n" +
                    ", rssi=$rssi\n" +
                    ", firmwareVersion=$firmwareVersion\n" +
                    ", hardwareVersion=$hardwareVersion\n" +
                    ", serialNumber=$serialNumber\n" +
                    ", modelNumber=$modelNumber\n" +
                    ", deviceType=$deviceType\n" +
                    ", deviceProtocolType=$deviceProtocolType\n" +
                    ", deviceCalcuteType=$deviceCalcuteType\n" +
                    ", deviceAccuracyType=$deviceAccuracyType\n" +
                    ", devicePowerType=$devicePowerType\n" +
                    ", deviceConnectType=$deviceConnectType\n" +
                    ", deviceFuncType=$deviceFuncType\n" +
                    ", deviceUnitType=$deviceUnitType\n" +
                    ", deviceSettingId=$deviceSettingId\n" +
                    ", imgUrl=$imgUrl\n" +
                    ", peripheralType=${getDevicePeripheralType()}"
        }
    }
}
