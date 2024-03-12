package com.peng.ppscale.business.v4

import android.text.TextUtils
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiAppleStateMenu
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiInfoInterface
import com.peng.ppscale.business.ble.listener.PPDataChangeListener
import com.peng.ppscale.business.ble.listener.PPHistoryDataInterface
import com.peng.ppscale.business.v4.auth.AuthenRequest
import com.peng.ppscale.util.Logger
import com.peng.ppscale.util.UnitUtil
import com.peng.ppscale.vo.PPBodyBaseModel
import com.peng.ppscale.vo.PPWifiModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DataReveiveHelper {

    fun parseNewBodyFatData(data: ByteArray, baseModel: PPBodyBaseModel, dataChangeListener: PPDataChangeListener?) {
        val TAG = "DataReveiveHelper "
        //重量
        var weight = (data[7].toInt() and 0xFF) shl 8 or (data[6].toInt() and 0xFF)
        val unit = data[11].toInt()
        Logger.d(TAG + "unit is  ${unit}")

        val unitType = UnitUtil.getUnitType(unit)
        baseModel.unit = unitType

        baseModel.weight = weight

        when (data[12]) {
            0x00.toByte() -> {
                Logger.d("蓝牙通信数据接收 重量稳定数据:" + ByteUtil.byte2Hex(data) + " 体重:" + weight / 100f)
                //稳定的体重数据
                //加密阻抗
                val encryptImpedance =
                    ((data[10].toInt() and 0xFF) shl 16) or ((data[9].toInt() and 0xFF) shl 8) or ((data[8].toInt() and 0xFF))

                val five7Int = ByteUtil.getBit(data[5], 7)
                val five6Int = ByteUtil.getBit(data[5], 6)

                if (five7Int == 1 && five6Int == 1) {
                    //心率测量完成
                    val heartRate = data[4].toInt()
                    baseModel.heartRate = heartRate
                    baseModel.isHeartRating = false

                    Logger.d("NewHasDeviceFrag 心率测量完成$heartRate")
                    dataChangeListener?.monitorHeartRateData(heartRate, false)
                } else if (five7Int == 1 && five6Int == 0) {
                    //正在测心率
                    Logger.d("NewHasDeviceFrag 心率测量开始")
                    baseModel.isHeartRating = true
                    //心率测量中
                    dataChangeListener?.monitorHeartRateData(0, true)
                } else {
                    //明文阻抗
                    val impedance = (data[5].toInt() and 0xFF) shl 8 or (data[4].toInt() and 0xFF)
//                    val unit = data[11].toInt()
//                    val dataStr = "$weight,$encryptImpedance,$impedance,$unit"
                    Logger.d("NewHasDeviceFrag dataStr impedance:$encryptImpedance zTwoLegsDeCode:$impedance")
                    baseModel.impedance = encryptImpedance.toLong()
                    baseModel.zTwoLegsDeCode = impedance.toFloat()

                    Logger.d("NewHasDeviceFrag 体重/体脂测量完成")
                    dataChangeListener?.monitorLockData(baseModel)
                }
            }

            0x01.toByte() -> {
                Logger.d("蓝牙通信数据接收 重量动态数据:" + ByteUtil.byte2Hex(data) + " 体重:" + weight / 100f)
                //动态的体重数据
                dataChangeListener?.monitorProcessData(baseModel)
            }

            0x02.toByte() -> {
                //超重
                dataChangeListener?.monitorOverWeight()
            }

            0x04.toByte() -> {
                // 抱婴或宠物模式 稳定数据
                dataChangeListener?.monitorBabyPetLockData(baseModel)
            }
        }
    }

    fun parseCmdData(data: ByteArray) {
        when (data[0]) {
            0xF1.toByte() -> {
                if (data.size == 4 && data[3] == 0x00.toByte()) {
                    // 时间同步成功
//                    LocalBroadcastUtil.sendData(ActionConst.ACTION_SYNC_TIME_T9148, true)
//                    V4Delegate.getInstance().syncTime()
                } else if (data.size == 10 && data[2] == 0x02.toByte()) {
                    //查看时间
                    val year_t1 = ((data[4].toInt() and 0xff) shl 8) or (data[3].toInt() and 0xff)
                    val month_t1 = data[5]
                    val day_t1 = data[6]
                    val hour_t1 = data[7]
                    val min_t1 = data[8]
                    val sec_t1 = data[9]
                    val t1 = "$year_t1-$month_t1-$day_t1 $hour_t1:$min_t1:$sec_t1"
//                    LocalBroadcastUtil.sendData(ActionConst.ACTION_GET_TIME_T9147, getTime)
                }
            }
            // 恢复出厂设置
            0xF9.toByte() -> {
            }

            0xFA.toByte() -> {
                //回复写SN
                if (data[2] == 0x00.toByte()) {
                    //成功
                }
            }
            //回复心率开关
            0xFB.toByte() -> {
                //心率开关
                when (data[2]) {
                    0x00.toByte() -> {
                        //心率测量打开
                    }

                    0x01.toByte() -> {
                        //心率测量关闭
                    }

                    0x12.toByte() -> {
                        //心率测量状态为开
                    }

                    0x22.toByte() -> {
                        //心率测量状态为关
                    }
                }
            }
            //回复设备的一些功能
            0XFD.toByte() -> {
                when (data[2]) {
                    0x00.toByte() -> {
                        if (data[3] == 0x00.toByte()) {
                            //单位切换成功
//                            EufylifeObserverManager.notifyAll(ObserverType.TYPE_SYNC_UNIT, null)
                        }
                    }

                    0x34.toByte() -> {
                        if (data[3] == 0x00.toByte()) {
                            //标定成功
                        }
                    }

                    0x37.toByte() -> {
                        if (data[3] == 0x00.toByte()) {
                            //用户组信息设置成功
                        }
                    }

                    0x38.toByte() -> {
                        if (data[3] == 0x00.toByte()) {
                            //安全模式 成功
                        }
                    }

                    0x39.toByte() -> {
                        if (data[3] == 0x00.toByte()) {
                            //工厂模式 成功
                        }
                    }

                    0x3A.toByte() -> {
                        if (data[3] == 0x00.toByte()) {
                            //用户模式 成功
                        }
                    }

                    0x3B.toByte() -> {
                        if (data[3] == 0x00.toByte()) {
                            //进入抱婴模式 成功
                        }
                    }

                    0x3C.toByte() -> {
                        if (data[3] == 0x00.toByte()) {
                            //退出抱婴模式 成功
                        }
                    }
                }
            }

            //回复device_id
            0x08.toByte() -> {

            }

            //回复product_code
            0x09.toByte() -> {

            }

            // 获取设备token 状态
            0xB0.toByte() -> {
                if (data.size == 3) {
                    //只处理Token过期的状态
                    if (data[2] == 0x02.toByte()) {
//                        LiveDataBus.sendLiveBean(NetLiveDataConst.GET_DEVICE_TOKEN_EXPIRED_STATE, 0)
                    }
                }
            }

            // 获取准备更新Token状态
            0xB1.toByte() -> {
                if (data.size == 3) {
                    //处理成功的情况
                    if (data[2] == 0x00.toByte()) {
//                        LiveDataBus.sendLiveBean(NetLiveDataConst.GET_DEVICE_PREPARE_UPDATE, 0)
                    }
                }
            }


//            0xF3.toByte() -> {
//                //断开蓝牙
//                LocalBroadcastUtil.sendData(ActionConst.ACTION_DISCONNECT_BLUETOOTH_T9146, true)
//
//            }
//            0xF4.toByte() -> {
//                //获得设备信息
//            }
//            0xF5.toByte() -> {
//                //同步单位成功
//                EufylifeObserverManager.notifyAll(ObserverType.TYPE_SYNC_UNIT, null)
//            }


        }

    }

    fun parseConfigWifiData(data: ByteArray) {

    }

    fun parseNewHistoryData(data: ByteArray, historyDataInterface: PPHistoryDataInterface?) {
        //历史数据
        //重量
        val weight = (data[7].toInt() and 0xFF) shl 8 or (data[6].toInt() and 0xFF)
        //加密阻抗
        val encryptImpedance =
            ((data[10].toInt() and 0xFF) shl 16) or ((data[9].toInt() and 0xFF) shl 8) or ((data[8].toInt() and 0xFF))
        //明文阻抗
        val impedance = (data[5].toInt() and 0xFF) shl 8 or (data[4].toInt() and 0xFF)
        // 称重单位 0x00-KG ；0x01-lb；0x02-ST_lb；0x03-斤；0x04-g；0x05-lb:oz；0x06-oz；0x07-ml(water)；0x08-ml(milk)；0x09-fl.oz(water)；0x0A-fl.oz(water)；0x0B-ST
        val unit = data[11].toInt()

        val year = (data[14].toInt() and 0xFF) shl 8 or (data[15].toInt() and 0xFF)
        val month = data[16].toInt()
        val day = data[17].toInt()
        val hour = data[18].toInt()
        val min = data[19].toInt()
        val second = data[20].toInt()

        val fatMode = data[8].toInt() shr 4

        val utcTime = dateToStamp(year, month, day, hour, min, second).toInt()
        val time = utcTimeToCurrTime(utcTime * 1000L)

        val bodyBaseModel = PPBodyBaseModel()
        bodyBaseModel.weight = weight
        bodyBaseModel.impedance = encryptImpedance.toLong()
        bodyBaseModel.zTwoLegsDeCode = impedance.toFloat()
        bodyBaseModel.dateStr = time.toString()

        historyDataInterface?.monitorHistoryData(bodyBaseModel, time.toString())

    }

    fun dateToStamp(year: Int, month: Int, day: Int, hour: Int, min: Int, second: Int): Long {
        var s = "$year/$month/$day/$hour/$min/$second"
        val date = dateFormat.parse(s)
        return Math.round(date.time / 1000.toDouble())
    }

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd/HH/mm/ss", Locale.US)

    fun utcTimeToCurrTime(timestamps: Long): Int {
        //将utc时间 转为当前时区的时间
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamps
        val zoneOffset = calendar.get(Calendar.ZONE_OFFSET)
        val dstOffset = calendar.get(Calendar.DST_OFFSET)
        calendar.add(Calendar.MILLISECOND, (zoneOffset + dstOffset))
        return (calendar.timeInMillis / 1000).toInt()
    }

    /**
     * 获取蓝牙返回数据,并进行拼接解析
     */
    fun parseGetWifiList(data: ByteArray, configWifiInfoInterface: PPConfigWifiInfoInterface?) {
        val encryptDeviceCode = ByteUtil.byte2Hex(data)
        if (!TextUtils.isEmpty(encryptDeviceCode)) {
            val totalStr = encryptDeviceCode.substring(4, 6)
            val total = ByteUtil.hexToTen(totalStr)
            val packageNum = ByteUtil.hexToTen(encryptDeviceCode.substring(6, 8))
            val data = encryptDeviceCode.substring(12, encryptDeviceCode.length - 2)
            if (packageNum == 0) {
                AuthenRequest.getInstance().reciverTempWifi = data
            } else {
                AuthenRequest.getInstance().reciverTempWifi += data
            }
            if (packageNum == total - 1) {
                val result = AuthenRequest.getInstance().reciverTempWifi
                AuthenRequest.getInstance().reciverTempWifi = ""
                handleWifiList(result, configWifiInfoInterface)
            }
        }
    }


    private fun handleWifiList(result: String, configWifiInfoInterface: PPConfigWifiInfoInterface?) {
        Logger.d("T9148CmdDispatcher-》handleWifiList" + result)
        val byteList = ByteUtil.hexToByte(result)
        val wifiList = ArrayList<String>()
        var wifiBytes = ArrayList<Byte>()
        val wifiInfoList = ArrayList<PPWifiModel>()
        for (byte in byteList) {
            if (0x00.toByte() == byte) {
                wifiList.add(ByteUtil.byte2Hex(wifiBytes.toByteArray()))
                wifiBytes.clear()
            } else {
                wifiBytes.add(byte)
            }
        }

        if (wifiList.isNotEmpty()) {
            wifiList.forEach {
                val wifiInfo = PPWifiModel()
                val signalSize = it.subSequence(0, 2).toString()
                wifiInfo.sign = Integer.parseInt(signalSize, 16)
                wifiInfo.sign = -wifiInfo.sign
                wifiInfo.bssid = (ByteUtil.hexToAscii(it.subSequence(2, 36).toString()))
                val wifiName = it.subSequence(36, it.length).toString()
                wifiInfo.ssid = ByteUtil.hexToAscii(wifiName)
                Logger.d("T9148CmdDispatcher parse is " + wifiInfo.toString())
                if (wifiInfo.ssid.isNotEmpty()) {
                    wifiInfoList.add(wifiInfo)
                }
            }

            if (wifiInfoList.isNotEmpty()) {
                val newWifiList = moveRepeatWifi(wifiInfoList)
                configWifiInfoInterface!!.monitorWiFiListSuccess(newWifiList)
            }

        }
    }

    private fun moveRepeatWifi(list: ArrayList<PPWifiModel>): ArrayList<PPWifiModel> {
        val wifiInfoList = ArrayList<PPWifiModel>()
        list.forEach { originIndex ->
            var isAdd = true
            wifiInfoList.forEach { newIndex ->
                if (newIndex.ssid == originIndex.ssid) {
                    isAdd = false
                }
            }
            if (isAdd) {
                wifiInfoList.add(originIndex)
            }
        }

        return wifiInfoList
    }

}