package com.peng.ppscale.business.v4

import com.peng.ppscale.util.Logger
import java.util.Calendar
import java.util.TimeZone
import kotlin.experimental.xor

object V4DelegateHelper {

    /**
     * sync time to device
     */
    fun syncTime(): List<ByteArray>? {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        //获取系统的日期
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val data = ByteArray(7)
        data[0] = (year shr 8 and 0xFF).toByte()
        data[1] = (year and 0xFF).toByte()
        data[2] = BytesUtil.intToByte(month)
        data[3] = BytesUtil.intToByte(day)
        data[4] = BytesUtil.intToByte(hour)
        data[5] = BytesUtil.intToByte(minute)
        data[6] = BytesUtil.intToByte(second)
        val command = assemblyCommand(T9148Cmd.T9148_SYNC_TIME, data)

        val dataHex = ByteUtil.byte2Hex(command)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun getTime(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_GET_TIME)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun recoverDevice(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_RECOVER_DEVICE)
        return getHeadC6SubContractBytes(dataHex)

    }

    fun heartRateState(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_HEART_RATE_STATE)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun openHeartRate(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_HEART_RATE_OPEN)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun closeHeartRate(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_HEART_RATE_CLOSE)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun setUnit(unit: Int): List<ByteArray>? {
        val syncUnit = T9148Cmd.T9148_SYNC_UNIT
        syncUnit[3] = unit.toByte()

        val dataHex = ByteUtil.byte2Hex(checkSum(syncUnit))
        return getHeadC6SubContractBytes(dataHex)
    }

    fun openSafeMode(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(checkSum(T9148Cmd.T9148_OPEN_SAFE_MODE))
        return getHeadC6SubContractBytes(dataHex)
    }

    fun closeSafeMode(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(checkSum(T9148Cmd.T9148_CLOSE_SAFE_MODE))
        return getHeadC6SubContractBytes(dataHex)
    }


    fun enterHoldBabyMode(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(checkSum(T9148Cmd.T9148_ENTER_HOLD_BABY_MODE))
        return getHeadC6SubContractBytes(dataHex)
    }

    fun exitHoldBabyMode(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(checkSum(T9148Cmd.T9148_EXIT_HOLD_BABY_MODE))
        return getHeadC6SubContractBytes(dataHex)
    }

    /**
     * 进入宠物模式
     */
    fun enterPetMode(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(checkSum(T9148Cmd.T9148_ENTER_PET_MODE))
        return getHeadC6SubContractBytes(dataHex)
    }

    /**
     * 退出宠物模式
     */
    fun exitPetMode(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(checkSum(T9148Cmd.T9148_EXIT_PET_MODE))
        return getHeadC6SubContractBytes(dataHex)
    }


    fun deleteHistory(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_DELETE_HISTORY)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun getHistory(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_SYNC_HISTORY_DATA)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun configWifiStart(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_CONFIG_WIFI_START)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun configNewCodeUidUrl(code: String, uid: String, url: String): List<ByteArray>? {
        val data = code + uid + url
        Logger.d(V4Delegate.getInstance().TAG + " configNewCodeUidUrl is $data")
        val dataHex = ByteUtil.decimal2Hex(code.length) + ByteUtil.stringToHexString(code) +
                ByteUtil.decimal2Hex(uid.length) + ByteUtil.stringToHexString(uid) +
                ByteUtil.decimal2Hex(url.length) + ByteUtil.stringToHexString(url)
        Logger.d(V4Delegate.getInstance().TAG + " configNewCodeUidUrl is newDataHex $dataHex")
        return  getSubContractBytes(dataHex, "F8")
    }

    fun configCodeUidUrl(code: String, uid: String, url: String): List<ByteArray>? {
        val data = code + uid + url
        Logger.d(V4Delegate.getInstance().TAG + "configCodeUidUrl is $data")
        val dataHex = ByteUtil.stringToHexString(data)
        Logger.d(V4Delegate.getInstance().TAG + "configCodeUidUrl is dataHex $dataHex")
        return getSubContractBytes(dataHex, "F8")
    }

    fun configDomainCertificate(domainCertificate: String): List<ByteArray>? {
        val data = domainCertificate
        val dataHex = ByteUtil.stringToHexString(data)
        return getSubContractBytes(dataHex, "F7")
    }

    fun configFinish(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_CONFIG_FINISH)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun configDeleteWifi(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_CONFIG_DELETE_WIFI)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun configQueryWifi(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_CONFIG_QUERY_WIFI)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun configUpdateWifiName(wifiName: String): List<ByteArray>? {
        val data = wifiName
        val dataHex = ByteUtil.stringToHexString(data)
        return getSubContractBytes(dataHex, "0A")
    }

    fun configUpdateWifiPassword(passWord: String): List<ByteArray>? {
        val data = passWord
        val dataHex = ByteUtil.stringToHexString(data)
        return getSubContractBytes(dataHex, "0B")
    }

    fun configUpdateWifiFinish(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_CONFIG_UPDATE_WIFI_FINISH)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun configGetWifiList(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_CONFIG_GET_WIFI_LIST)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun cancelConfigNet(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_CANCEL_CONFIG)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun openScaleLight(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_OPEN_LIGHT)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun configNetHeart(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_CONFIG_NET_HEART)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun startOta(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_OTA_START)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun getCurrDeviceOtaState(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_DEVICE_OTA_STATE)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun getCurrWifiRSSI(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_GET_CURR_WIFI_RSSI)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun sendUserInfo(memberId: String, level: Int? = 0, sex: Int, age: Int, height: Int, weight: Int): List<ByteArray>? {
        var userInfo = ""
        userInfo = "37" +
                "18" +
                memberId.replace("-", "") +
                ByteUtil.decimal2Hex(level!!) +
                ByteUtil.decimal2Hex(sex) +
                ByteUtil.decimal2Hex(age) +
                ByteUtils.byteToString(ByteUtil.intToTwoBytes(height)) +
                ByteUtils.byteToString(ByteUtil.intToTwoBytes(weight))
        Logger.d("sendUserInfo-->userInfo " + userInfo)
        val byteData = ByteUtil.hexToByte(userInfo)
        val dataHex = ByteUtil.byte2Hex(checkSum(byteData))
        Logger.d("sendUserInfo-->userInfo result " + dataHex)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun getDeviceBindState(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_DEVICE_BIND_STATE)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun getDeviceTokenState(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_DEVICE_TOKEN_STATE)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun prepareUpdateToken(): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(T9148Cmd.T9148_DEVICE_PREPARE_UPDATE_TOKEN)
        return getHeadC6SubContractBytes(dataHex)
    }

    fun updateToken(expires_in: Int, access_token: String, refresh_token: String): List<ByteArray>? {
        val dataHex = ByteUtil.byte2Hex(ByteUtil.intToTwoBytes(expires_in)) +
                ByteUtil.decimal2Hex(access_token.length) + ByteUtil.stringToHexString(access_token) +
                ByteUtil.decimal2Hex(refresh_token.length) + ByteUtil.stringToHexString(refresh_token)
        Logger.d(V4Delegate.getInstance().TAG + "configCodeUidUrl is newDataHex $dataHex")
        return getSubContractBytes(dataHex, "B2")
    }

    //获取设备模式，不用走鉴权加密的逻辑//
    fun getCurrDeviceModel(): ByteArray {
        return checkSum(T9148Cmd.T9148_DEVICE_MODE)
    }

    fun factoryMode(): ByteArray {
        return checkSum(T9148Cmd.T9148_DEVICE_FACTORY_MODE)
    }

    fun userMode(): ByteArray {
        return checkSum(T9148Cmd.T9148_DEVICE_USER_MODE)
    }
    //获取设备模式，不用走鉴权加密的逻辑//

    /**
     * 60个字节的分包数据
     *
     * @param appCodeHex 数据源
     * @param head       请求头
     * @return
     */
    private fun getSubContractBytes(appCodeHex: String, head: String): List<ByteArray>? {
        if (appCodeHex.isNotEmpty()) {
            val arrayHex: MutableList<ByteArray> = ArrayList()
//            val cmdLength = appCodeHex.length/2 + 5 // 指令长度
            val total = (appCodeHex.length / 106) + if (appCodeHex.length % 106 > 0) 1 else 0  //总包数
            var num = 0   //包号
            val len = appCodeHex.length //数据总长度
            for (i in 0 until total) {
                val currSubData = appCodeHex.substring(i * 106, Math.min(len, (i + 1) * 106))
                val cmdLength = currSubData.length / 2 + 5
                val dataBody = (head
                        + ByteUtil.decimal2Hex(cmdLength)
                        + ByteUtil.decimal2Hex(total)
                        + ByteUtil.decimal2Hex(num)
                        + ByteUtils.byteToString(ByteUtil.intToTwoBytes(len / 2))
//                        + ByteUtil.decimal2Hex(len / 2)
                        + currSubData)
                val xorValue = ByteUtil.getXorValue(ByteUtils.stringToBytes(dataBody))
                val xorHex = String.format("%02X", xorValue)
                val data = dataBody + xorHex
                num++
                arrayHex.add(ByteUtils.stringToBytes(data))
                Logger.d("分包数据 = $data")
            }

            val list = ArrayList<ByteArray>()
            arrayHex.forEach {
                val headC6SubContractBytes = getHeadC6SubContractBytes(ByteUtil.byte2Hex(it))
                headC6SubContractBytes?.let {
                    list.addAll(it)
                }
            }
            return list
        }
        return null
    }

    /**
     * head 为C6的分包数据
     * 20个字节的分包数据
     *
     * @param appCodeHex 数据源
     * @param head       请求头
     * @return
     */
    fun getHeadC6SubContractBytes(appCodeHex: String): List<ByteArray>? {
        if (appCodeHex.isNotEmpty()) {
            val arrayHex: MutableList<ByteArray> = ArrayList()
            val len = appCodeHex.length
            val total = (appCodeHex.length / 30) + if (appCodeHex.length % 30 > 0) 1 else 0
            var num = 0
            for (i in 0 until total) {
                val dataBody = ("C6"
                        + ByteUtil.decimal2Hex(total)
                        + ByteUtil.decimal2Hex(num)
                        + ByteUtil.decimal2Hex(len / 2)
                        + appCodeHex.substring(i * 30, Math.min(len, (i + 1) * 30)))
                val xorValue = ByteUtil.getXorValue(ByteUtils.stringToBytes(dataBody))
                val xorHex = String.format("%02X", xorValue)
                val data = dataBody + xorHex
                num++
                arrayHex.add(ByteUtils.stringToBytes(data))
                Logger.d("分包数据 = $data")
            }
            return arrayHex
        }
        return null
    }

    fun assemblyCommand(header: ByteArray?, data: ByteArray?): ByteArray {
        val headerLength = header?.size ?: 0// length
        var commandSize = data?.size ?: 0
        val totalLength = commandSize + headerLength

        val result = ByteArray(totalLength)
        header?.let { System.arraycopy(it, 0, result, 0, headerLength) }

        result[headerLength] = totalLength.toByte()
        data?.let { System.arraycopy(it, 0, result, headerLength, commandSize) }
        return result
    }

    private fun checkSum(data: ByteArray): ByteArray {

        val result = ByteArray(data.size + 1)
        System.arraycopy(data, 0, result, 0, data.size)

        // checksum
        var retValue: Byte = 0
        for (b in data) {
            retValue = retValue xor b
        }

        result[result.size - 1] = retValue

        return result
    }

}

