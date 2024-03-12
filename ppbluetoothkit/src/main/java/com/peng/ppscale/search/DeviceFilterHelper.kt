package com.peng.ppscale.search

import com.inuker.bluetooth.library.search.SearchResult
import com.peng.ppscale.PPBlutoothKit
import com.peng.ppscale.util.ByteUtil
import com.peng.ppscale.util.Logger
import com.peng.ppscale.vo.DeviceConfigVo
import com.peng.ppscale.vo.PPDeviceModel
import com.peng.ppscale.vo.PPScaleDefine

object DeviceFilterHelper {

    var advDataStr = ""

    var deviceModelMap: MutableMap<String, PPDeviceModel> = HashMap()

    fun getDeviceModel(searchResult: SearchResult): PPDeviceModel? {
        val deviceName: String = searchResult.getName()
        val deviceMac: String = searchResult.getAddress()
        if (Logger.enabled) {
            if (searchResult.scanRecord?.isNotEmpty() == true) {
                val brocastData = ByteUtil.byteToString(searchResult.scanRecord)
                Logger.v("deviceName:$deviceName deviceMac:$deviceMac brocastData:$brocastData")
            }
        } else {
            Logger.v("deviceName:$deviceName deviceMac:$deviceMac")
        }
        if (deviceModelMap.containsKey(deviceMac)) {
            val deviceModel = deviceModelMap[deviceMac]
            deviceModel?.let {
                if (searchResult.scanRecord?.isNotEmpty() == true) {
                    protocalAdvData(deviceModel, searchResult.scanRecord)
                    deviceModel.rssi = searchResult.rssi
                    deviceModelMap[deviceMac] = it
                }
            }
            return deviceModel
        } else {
            var deviceModel: PPDeviceModel? = null
            val nameFullMatchDeviceList: MutableList<DeviceConfigVo> = ArrayList()//蓝牙名称全匹配
            val nameSemiDeviceDeviceList: MutableList<DeviceConfigVo> = ArrayList()//蓝牙名称半匹配

            PPBlutoothKit.deviceConfigVos?.forEach {
                if (deviceName.equals(it.deviceName, true)) {
                    nameFullMatchDeviceList.add(it)
                }
                if (deviceName.contains(it.deviceName)) {
                    nameSemiDeviceDeviceList.add(it)
                }
            }
            var configVoList =
                if (nameFullMatchDeviceList.isEmpty()) nameSemiDeviceDeviceList else nameFullMatchDeviceList

            configVoList.forEach { configVo ->
                Logger.d("deviceName:${configVo.getDeviceName()} sign:${configVo.sign} deviceProtocolType:${configVo.deviceProtocolType} advLength:${configVo.advLength} macAddressStart:${configVo.macAddressStart}")
                if (configVo.deviceProtocolType == PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeV3.getType()) {
                    if (searchResult.scanRecord?.isNotEmpty() == true) {
                        val broadcastData =
                            BleSearchBroadcastHelper.analysiBroadcastDataNormal(searchResult.scanRecord)
                        val advData = broadcastData.beacondata
                        val advDataHex = ByteUtil.byteToString(advData)
                        Logger.d("DeviceFilterHelper getDeviceModel V3 advData:$advDataHex " + "macAddressStart:${configVo.macAddressStart} " + "advLength")
                        if (advData.size == configVo.advLength) {
                            val outData = ByteUtil.subByteArray(
                                advData,
                                configVo.macAddressStart + 1,
                                configVo.macAddressStart + 12
                            )
                            val mSign = ByteUtil.byteToString(ByteUtil.subByteArray(outData, 0, 3))
                            Logger.d("DeviceFilterHelper getDeviceModel sign:${configVo.sign} mSign:$mSign")
                            if (mSign.equals(configVo.sign)) {
                                deviceModel = DeviceConfigVoToPPDeviceModel(configVo, searchResult)
//                                    deviceModel?.devicePower = advData[configVo.macAddressStart + 12].toInt()
                            }
                        }
                    } else {

                    }
                } else {
                    deviceModel = DeviceConfigVoToPPDeviceModel(configVo, searchResult)
                    val broadcastData = BleSearchBroadcastHelper.analysiBroadcastDataNormal(searchResult.scanRecord)
                    val advData = broadcastData.beacondata
                    if (advData != null && advData.isNotEmpty()) {
                        val algorithmDiffer = advData[advData.size - 1] //算法差异标识
                        Logger.d("算法差异:algorithmDiffer=$algorithmDiffer algorithmDiffer去整型${algorithmDiffer.toInt()}")
//                        1000
//                       &0000 1000
//                        ------------
//                        0000 1000
                        if (algorithmDiffer.toInt() and 0x08 == 0x08) {//bit3=1 ->0x08       bit4->0x10
                            deviceModel?.deviceCalcuteType = PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8_3
                        }
                        if (algorithmDiffer.toInt() and 0x10 == 0x10) {//bit3=1 ->0x08       bit4->0x10

                        }
                    }
                    Logger.d("DeviceFilterHelper getDeviceModel ${deviceModel.toString()}")
                }
            }

            deviceModel?.let {
                if (searchResult.scanRecord?.isNotEmpty() == true) {
                    protocalAdvData(deviceModel, searchResult.scanRecord)
                }
                deviceModel?.rssi = searchResult.rssi
                deviceModelMap.put(deviceMac, it)
            }
            return deviceModel
        }
    }

    private fun protocalAdvData(deviceModel: PPDeviceModel?, scanRecord: ByteArray) {
        val broadcastData = BleSearchBroadcastHelper.analysiBroadcastDataNormal(scanRecord)
        val advData = broadcastData.beacondata
        advDataStr = ByteUtil.byteToString(advData)
        if (Logger.enabled) {
//            val advDataHex = ByteUtil.byteToString(advData)
            Logger.v("deviceName:${deviceModel?.deviceName} deviceMac:${deviceModel?.deviceMac} advData:$advDataStr")
        }
        if (deviceModel?.deviceProtocolType == PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeTorre) {
            if (advData != null && advData.isNotEmpty()) {
                val batteryPower = advData[0].toInt()
                if (batteryPower in 1..100) {
                    deviceModel.devicePower = batteryPower
                }
            }
        } else if (deviceModel?.deviceProtocolType == PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeV4) {
            //由于秤端的广播电量存在问题，所以此时不再拉取电量，放到Server里面去取电量
//            if (advData != null && advData.size > 0) {
//                val batteryPower = advData[0].toInt()
//                if (batteryPower > 0 && batteryPower <= 100) {
//                    deviceModel.devicePower = batteryPower
//                }
//            }
        } else if (deviceModel?.deviceProtocolType == PPScaleDefine.PPDeviceProtocolType.PPDeviceProtocolTypeV3) {
            //V3.0体脂秤
            //0000503DEB92EA7CCF410340060000000001CA3C
            if (advData.size >= 20) {
                val batteryPower = advData[19].toInt()
                if (batteryPower > 0 && batteryPower <= 100) {
                    deviceModel.devicePower = batteryPower
                }
            } else if (advData.size >= 18) {
                val batteryPower = advData[17].toInt()
                if (batteryPower > 0 && batteryPower <= 100) {
                    deviceModel.devicePower = batteryPower
                }
            }
        }
    }

    fun DeviceConfigVoToPPDeviceModel(
        deviceConfigVo: DeviceConfigVo,
        searchResult: SearchResult
    ): PPDeviceModel {
        val deviceModel = PPDeviceModel(searchResult.address, searchResult.name)
        deviceModel.deviceConnectType =
            enumValues<PPScaleDefine.PPDeviceConnectType>()[deviceConfigVo.deviceConnectType]
        deviceModel.deviceType = enumValues<PPScaleDefine.PPDeviceType>()[deviceConfigVo.deviceType]
        deviceModel.deviceProtocolType =
            enumValues<PPScaleDefine.PPDeviceProtocolType>()[deviceConfigVo.deviceProtocolType]
        deviceModel.deviceCalcuteType =
            enumValues<PPScaleDefine.PPDeviceCalcuteType>()[deviceConfigVo.deviceCalcuteType]
        deviceModel.deviceFuncType = deviceConfigVo.deviceFuncType
        deviceModel.devicePowerType =
            enumValues<PPScaleDefine.PPDevicePowerType>()[deviceConfigVo.devicePowerType]
        deviceModel.deviceAccuracyType =
            enumValues<PPScaleDefine.PPDeviceAccuracyType>()[deviceConfigVo.deviceAccuracyType]
        deviceModel.deviceUnitType = deviceConfigVo.deviceUnitType
        deviceModel.advLength = deviceConfigVo.advLength
        deviceModel.macAddressStart = deviceConfigVo.macAddressStart
        deviceModel.deviceSettingId = deviceConfigVo.id
        deviceModel.imgUrl =
            "https://unique.lefuenergy.com/prod/scale_img/default_no_border_black.png"
        if (deviceConfigVo.imgUrl.isNullOrEmpty().not()) {
            deviceModel.imgUrl = deviceConfigVo.imgUrl
        }
        return deviceModel
    }


}
