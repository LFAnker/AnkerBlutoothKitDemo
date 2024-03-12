package com.peng.ppscale.business.ble.send

import com.inuker.bluetooth.library.BluetoothClient
import com.inuker.bluetooth.library.Code
import com.inuker.bluetooth.library.connect.response.BleWriteResponse
import com.peng.ppscale.business.ble.listener.PPBleSendResultCallBack
import com.peng.ppscale.util.ByteUtil
import com.peng.ppscale.util.Logger
import com.peng.ppscale.vo.PPScaleSendState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


object BleSendPoolManager {

    var bleClient: BluetoothClient? = null

    private var address: String = ""
    private var service: UUID? = null
    private var index = 0
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    @Volatile
    var mCommondList: List<ByteArray>? = null

    @Volatile
    var mResponseCommondList: List<ByteArray>? = null

    fun sendDataResponse(address1: String, service1: UUID, character: UUID, commond: ByteArray, sendResultCallBack: PPBleSendResultCallBack?) {
        coroutineScope.launch {
            address = address1
            service = service1
            Logger.d("sendDataResponse coroutineScope " + " UUID:" + character.toString() + " address = " + address)
            sendMessageResponse(character, commond) { pPScaleSendState ->
                coroutineScope.launch(Dispatchers.Main) {
                    sendResultCallBack?.onResult(pPScaleSendState)
                }
            }
        }
    }

    fun sendListDataResponse(address1: String, service1: UUID, character1: UUID, commondList: List<ByteArray>, sendResultCallBack: PPBleSendResultCallBack?) {
        coroutineScope.launch {
            address = address1
            service = service1
            Logger.v("sendListDataResponse coroutineScope size: " + commondList.size + " UUID:" + character1.toString() + " address = " + address)
            index = 0
            mResponseCommondList = commondList
            sendMessageListResponse(character1) { pPScaleSendState ->
                coroutineScope.launch(Dispatchers.Main) {
                    if (sendResultCallBack != null) {
                        sendResultCallBack.onResult(pPScaleSendState)
                    }
                }
            }
        }
    }

    fun sendListDataNoResponse(address1: String, service1: UUID, character: UUID, commondList: MutableList<ByteArray>, sendResultCallBack: PPBleSendResultCallBack?) {
        coroutineScope.launch {
            address = address1
            service = service1
            Logger.v("sendListDataNoResponse coroutineScope size: " + commondList.size + " UUID:" + character.toString() + " address = " + address)
            index = 0
            mCommondList = commondList
            sendMessageListNoResponse(character) { pPScaleSendState ->
                coroutineScope.launch(Dispatchers.Main) {
                    sendResultCallBack?.onResult(pPScaleSendState)
                }
            }
        }
    }

    private fun sendMessageListNoResponse(character: UUID, callBack: (pPScaleSendState: PPScaleSendState) -> Unit) {
        Logger.v("sendMessageListNoResponse list size: " + mCommondList?.size + " index = " + index + " hash:" + mCommondList.hashCode())
        if ((mCommondList?.size ?: 0) > index) {
            val bytes: ByteArray = mCommondList!!.get(index)
            Logger.v("sendMessageListNoResponse bytes size: " + bytes.size + " indexLen = " + index.times(bytes.size))
//            val writeData = ByteUtil.byteToString(bytes)
//            Logger.v("sendMessageListNoResponse writeData:" + writeData)
            bleClient?.writeNoRspDirectly(address, service, character, bytes, object : BleWriteResponse {

                override fun onResponse(code: Int) {
                    if (code == Code.REQUEST_SUCCESS) {
                        Logger.v("sendMessageListNoResponse send success ---------")
                        index++
                        coroutineScope.launch {
                            sendMessageListNoResponse(character, callBack)
                        }
                    } else {
                        Logger.e("sendMessageListNoResponse fail code = $code")
                        callBack(PPScaleSendState.PP_SEND_FAIL)
                    }
                }
            })
        } else {
            Logger.d("sendMessageListNoResponse list success ---------")
            callBack(PPScaleSendState.PP_SEND_SUCCESS)
        }
    }

    private fun sendMessageListResponse(character: UUID, callBack: (pPScaleSendState: PPScaleSendState) -> Unit) {
        if ((mResponseCommondList?.size ?: 0) > index) {
            val bytes: ByteArray = mResponseCommondList?.get(index)!!
            val writeData = ByteUtil.byteToString(bytes)
            if ((mResponseCommondList?.size ?: 0) > 4) {
                Logger.v("sendMessageListResponse writeData:" + writeData + " index = " + index)
            } else {
                Logger.d("sendMessageListResponse writeData:" + writeData + " index = " + index)
            }
            bleClient?.write(address, service, character, bytes, object : BleWriteResponse {
                override fun onResponse(code: Int) {
                    if (code == Code.REQUEST_SUCCESS) {
                        if ((mResponseCommondList?.size ?: 0) > 4) {
                            Logger.v("sendMessageListResponse send success index:" + index)
                        } else {
                            Logger.d("sendMessageListResponse send success index:" + index)
                        }
                        index++
                        sendMessageListResponse(character, callBack)
                    } else {
                        Logger.e("sendMessageListResponse fail code = $code")
                        callBack(PPScaleSendState.PP_SEND_FAIL)
                    }
                }
            })
        } else {
            Logger.d("sendMessageListResponse list success ---------")
            callBack(PPScaleSendState.PP_SEND_SUCCESS)
        }
    }

    private fun sendMessageResponse(character: UUID, commond: ByteArray, callBack: (pPScaleSendState: PPScaleSendState) -> Unit) {
        if (commond.isNotEmpty()) {
            val writeData = ByteUtil.byteToString(commond)
            Logger.d("sendMessageResponse writeData:" + writeData + " address:" + address)
            bleClient?.write(address, service, character, commond, object : BleWriteResponse {

                override fun onResponse(code: Int) {
                    if (code == Code.REQUEST_SUCCESS) {
                        Logger.d("sendMessageResponse send success ---------")
                        callBack(PPScaleSendState.PP_SEND_SUCCESS)
                    } else {
                        Logger.d("sendMessageResponse fail code = $code")
                        callBack(PPScaleSendState.PP_SEND_FAIL)
                    }
                }
            })
        } else {
            Logger.e("sendMessageResponse commond is null")
            callBack(PPScaleSendState.PP_SEND_FAIL)
        }
    }

}