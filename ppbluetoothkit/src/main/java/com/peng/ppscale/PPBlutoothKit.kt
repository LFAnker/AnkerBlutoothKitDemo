package com.peng.ppscale

import android.content.Context
import com.inuker.bluetooth.library.BluetoothClient
import com.inuker.bluetooth.library.utils.BluetoothLog
import com.inuker.bluetooth.library.utils.BluetoothUtils
import com.lefu.gson.Gson
import com.lefu.gson.reflect.TypeToken
import com.peng.ppscale.data.PPBodyDetailModel
import com.peng.ppscale.key.AppKey
import com.peng.ppscale.util.Logger
import com.peng.ppscale.util.OnLogCallBack
import com.peng.ppscale.util.ReadJsonUtils
import com.peng.ppscale.vo.DeviceConfigVo

object PPBlutoothKit {

    val TAG = PPBlutoothKit::class.java.simpleName

    var bluetoothClient: BluetoothClient? = null             //蓝牙操作类

    var deviceConfigVos: MutableList<DeviceConfigVo>? = null

    /**
     * 从本地Device.json中获取设备配置的
     */
    fun initSdk(context: Context) {
        Logger.d("$TAG initSdk")
        if (bluetoothClient == null) {
            bluetoothClient = BluetoothClient(context)
        }
        PPBodyDetailModel.context = context
    }

    /**
     * 设置debug模式 默认false
     *
     * @param isDebug
     */
    fun setDebug(isDebug: Boolean) {
        Logger.e("SDK debug:$isDebug")
        Logger.enabled = isDebug
        BluetoothLog.setDebug(isDebug)
    }

    fun setDebugLogCallBack(onLogCallBack: OnLogCallBack?) {
        Logger.onLogCallBack = onLogCallBack
    }

    fun isBluetoothOpened(): Boolean {
        return BluetoothUtils.isBluetoothEnabled()
    }

    fun openBluetooth(): Boolean {
        return BluetoothUtils.openBluetooth()
    }


}
