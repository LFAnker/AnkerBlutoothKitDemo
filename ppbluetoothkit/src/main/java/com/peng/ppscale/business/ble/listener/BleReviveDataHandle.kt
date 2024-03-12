package com.peng.ppscale.business.ble.listener

/**
 * 处理数据接收回调，
 */
interface BleReviveDataHandle {

    fun onReciveData(byteArray: ByteArray) {}
    fun onReciveDataHex(reciveData: String) {}

}