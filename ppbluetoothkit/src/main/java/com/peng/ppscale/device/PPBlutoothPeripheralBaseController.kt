package com.peng.ppscale.device

import android.bluetooth.BluetoothAdapter
import android.os.Build
import com.inuker.bluetooth.library.Constants
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener
import com.inuker.bluetooth.library.connect.options.BleConnectOptions
import com.inuker.bluetooth.library.connect.response.BleConnectResponse
import com.inuker.bluetooth.library.model.BleGattProfile
import com.inuker.bluetooth.library.search.SearchRequest
import com.inuker.bluetooth.library.search.SearchResult
import com.inuker.bluetooth.library.search.response.SearchResponse
import com.peng.ppscale.PPBlutoothKit
import com.peng.ppscale.PPBlutoothKit.bluetoothClient
import com.peng.ppscale.business.ble.listener.PPBleStateInterface
import com.peng.ppscale.business.ble.send.BleSendManager
import com.peng.ppscale.business.state.PPBleSwitchState
import com.peng.ppscale.business.state.PPBleWorkState
import com.peng.ppscale.business.torre.TorreDeviceManager
import com.peng.ppscale.search.BleSearchDelegate
import com.peng.ppscale.search.BleSearchHelper
import com.peng.ppscale.search.DeviceFilterHelper
import com.peng.ppscale.util.Logger
import com.peng.ppscale.vo.PPDeviceModel
import com.peng.ppscale.vo.PPScaleDefine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

abstract class PPBlutoothPeripheralBaseController : BleConnectStatusListener() {

    open var bleStateInterface: PPBleStateInterface? = null
    open var deviceModel: PPDeviceModel? = null
    private var bleSendManager: BleSendManager? = null
    var address: String? = ""

    val tag = this.javaClass.simpleName

    var executorService: ExecutorService? = Executors.newSingleThreadExecutor()

    var lastConnectTime: Long = 0//用于控制蓝牙连接，防止频繁触发连接

    /**********************************************蓝牙扫描*****************************************************************/

    val searchRequest: SearchRequest? =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SearchRequest.Builder()
                .searchBluetoothLeDevice(1000, 1000)
                .build()
        } else {
            SearchRequest.Builder()
                .searchBluetoothLeDevice(BleSearchDelegate.DURATION_TIME)
                .build()
        }

    fun startSearch(address: String, bleStateInterface: PPBleStateInterface?) {
        clearCache()
        Logger.d("$tag startSearch address:$address")
        this.address = address
        this.bleStateInterface = bleStateInterface
        bluetoothClient?.search(searchRequest, searchResponse)
    }

    fun stopSeach() {
        Logger.d("$tag stopSeach")
        bluetoothClient?.stopSearch()
    }

    val searchResponse = object : SearchResponse {

        override fun onSearchStarted() {
            Logger.d("$tag onSearchStarted")
            bleStateInterface?.monitorBluetoothWorkState(PPBleWorkState.PPBleWorkStateSearching, null)
        }

        override fun onDeviceFounded(searchResult: SearchResult?) {
            executorService?.execute {
                if (address != null && searchResult?.device?.address.equals(address)) {
                    if (searchResult?.scanRecord?.isNotEmpty() == true) {
//                        deviceModel = getDeviceModel(searchResult)
                        deviceModel = BleSearchHelper.deviceTypeByCBAdvDataManufacturerData(searchResult)
                        if (deviceModel?.deviceConnectType == PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeDirect) {
                            stopSeach()
                            Logger.d("$tag onDeviceFounded and startConnect")
                            deviceModel?.let {
                                GlobalScope.launch(Dispatchers.Main) {
                                    startConnect(it, bleStateInterface)
                                }
                            }
                        }
                        val outData = DeviceFilterHelper.advDataStr
                        Logger.d("$tag advDataStr = $outData")
                        onSearchResponse(outData)
                    } else {
                        Logger.e("searchResult?.scanRecord? is NULL")
                    }
                }
            }
        }

        override fun onSearchStopped() {
            Logger.e("$tag onSearchStopped Search TimeOut, If necessary, please restart the scan ")
            if (bleStateInterface != null) {
                bleStateInterface!!.monitorBluetoothWorkState(
                    PPBleWorkState.PPBleWorkSearchTimeOut,
                    null
                )
            }
        }

        override fun onSearchCanceled() {
            Logger.d("$tag onSearchCanceled App actively stops scanning")
            if (bleStateInterface != null) {
                bleStateInterface!!.monitorBluetoothWorkState(
                    PPBleWorkState.PPBleStateSearchCanceled,
                    null
                )
            }
        }

        override fun onSearchFail(code: Int) {
            Logger.e("$tag onSearchFail code:$code")
            if (bleStateInterface != null) {
                bleStateInterface!!.monitorBluetoothWorkState(
                    PPBleWorkState.PPBleWorkSearchFail,
                    null
                )
            }
        }

    }

    open fun onSearchResponse(outData: String?) {}


    /**********************************************蓝牙连接*****************************************************************/


    val connectOptions: BleConnectOptions? = BleConnectOptions.Builder()
        .setConnectRetry(2)
        .setConnectTimeout(7000)
        .setServiceDiscoverRetry(3)
        .setServiceDiscoverTimeout(7000)
        .build()

    open fun startConnect(
        deviceModel: PPDeviceModel,
        hex: String?,
        bleStateInterface: PPBleStateInterface?
    ) {
        onSearchResponse(hex)
        startConnect(deviceModel, bleStateInterface)
    }

    open fun startConnect(deviceModel: PPDeviceModel, bleStateInterface: PPBleStateInterface?) {
        this.deviceModel = deviceModel
        Logger.d("$tag startConnect deviceMac: ${deviceModel.deviceMac}")
        this.bleStateInterface = bleStateInterface
        val isValidate = BluetoothAdapter.checkBluetoothAddress(deviceModel.deviceMac)
        if (isValidate) {
            if (!connectState()) {
                if (System.currentTimeMillis() - lastConnectTime > 2000) {
                    lastConnectTime = System.currentTimeMillis()
                    bleStateInterface?.monitorBluetoothWorkState(PPBleWorkState.PPBleWorkStateConnecting, deviceModel)
                    bluetoothClient?.unregisterConnectStatusListener(deviceModel.deviceMac, this)
                    bluetoothClient?.registerConnectStatusListener(deviceModel.deviceMac, this)
//                    bluetoothClient?.unregisterBluetoothStateListener(bluetoothStateListener)
//                    bluetoothClient?.registerBluetoothStateListener(bluetoothStateListener)
                    Logger.e("startConnect bluetoothClient connect")
                    bluetoothClient?.connect(deviceModel.deviceMac, connectOptions, object : BleConnectResponse {

                        override fun onResponse(code: Int, bleGattProfile: BleGattProfile?) {
                            onConnectResponse(code, bleGattProfile)
                        }
                    })
                } else {
                    Logger.e("$tag startConnect 频繁连接蓝牙 被拦截")
                }
            } else {
                Logger.e("$tag startConnect PPBleWorkStateConnected")
                bleStateInterface?.monitorBluetoothWorkState(
                    PPBleWorkState.PPBleWorkStateConnected,
                    deviceModel
                )
            }
        } else {
            Logger.d("$tag 无效的mac地址： mac = " + deviceModel?.deviceMac)
        }
    }

    val bluetoothStateListener = object : BluetoothStateListener() {
        override fun onBluetoothStateChanged(state: Boolean) {
            bleStateInterface?.monitorBluetoothSwitchState(if (state) PPBleSwitchState.PPBleSwitchStateOn else PPBleSwitchState.PPBleSwitchStateOff)
        }
    }

    open fun onConnectResponse(code: Int, bleGattProfile: BleGattProfile?) {
    }

    fun disConnect() {
        lastConnectTime = 0
        Logger.d("$tag disConnect")
        bluetoothClient?.refreshCache(deviceModel?.deviceMac)
        bluetoothClient?.clearRequest(deviceModel?.deviceMac, 0)
        bluetoothClient?.disconnect(deviceModel?.deviceMac)
    }

    fun connectState(): Boolean {
        deviceModel?.let {
            val connectState = PPBlutoothKit.bluetoothClient?.getConnectStatus(deviceModel?.deviceMac)
            val isConnect: Boolean =
                connectState == Constants.STATUS_DEVICE_CONNECTED || connectState == Constants.STATUS_DEVICE_CONNECTING
            Logger.d("deviceName:${deviceModel?.deviceName} deviceMac:${deviceModel?.deviceMac} isConnect  $isConnect connectState:$connectState")
            return isConnect
        }
        return false
    }

    override fun onConnectStatusChanged(msg: String?, status: Int) {
        Logger.d("onConnectStatusChanged msg:$msg status:$status")
        if (status == Constants.STATUS_CONNECTED) {
            Logger.d("onConnectStatusChanged connect device success deviceMac = " + deviceModel?.deviceMac)
            bleStateInterface?.monitorBluetoothWorkState(
                PPBleWorkState.PPBleWorkStateConnected,
                deviceModel
            )
        } else if (status == Constants.STATUS_DISCONNECTED) {
            TorreDeviceManager.getInstance().stopKeepAlive()
            Logger.d("onConnectStatusChanged disconnect device end status = " + status + " deviceMac = " + deviceModel?.deviceMac)
            bleStateInterface?.monitorBluetoothWorkState(
                PPBleWorkState.PPBleWorkStateDisconnected,
                deviceModel
            )
        } else {
            TorreDeviceManager.getInstance().stopKeepAlive()
//            bleStateInterface?.monitorBluetoothWorkState(PPBleWorkState.PPBleWorkStateDisconnected, deviceModel)
        }
    }

    fun getBleSendManager(service: UUID?, character: UUID?): BleSendManager {
        if (bleSendManager != null) {
            bleSendManager?.setDevice(deviceModel)
            bleSendManager?.setService(service)
            bleSendManager?.setCharacter(character)
        } else {
            bleSendManager = BleSendManager.Builder()
                .setDevice(deviceModel)
                .setService(service)
                .setCharacter(character)
                .setBleClient(PPBlutoothKit.bluetoothClient)
                .build()
        }
        return bleSendManager!!
    }

    fun clearCache() {
        lastConnectTime = 0
    }

}
