package com.anker.ppblutoothkit.devicelist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.anker.ppblutoothkit.R
import com.anker.ppblutoothkit.device.PeripheralIceActivity
import com.peng.ppscale.business.ble.listener.PPBleStateInterface
import com.peng.ppscale.business.ble.listener.PPSearchDeviceInfoInterface
import com.peng.ppscale.business.state.PPBleSwitchState
import com.peng.ppscale.business.state.PPBleWorkState
import com.peng.ppscale.search.PPSearchManager
import com.peng.ppscale.util.Logger
import com.peng.ppscale.vo.PPDeviceModel
import com.peng.ppscale.vo.PPScaleDefine

class ScanDeviceListActivity : Activity() {
    var ppScale: PPSearchManager? = null
    var isOnResume = false //页面可见时再重新发起扫描
    private var adapter: DeviceListAdapter? = null
    var deviceModels = ArrayList<PPDeviceModel>()
    private var tv_starts: TextView? = null
    var lastTimes: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)
        tv_starts = findViewById(R.id.tv_starts)
        tv_starts?.setOnClickListener(View.OnClickListener { reStartScan() })
        adapter = DeviceListAdapter(this, R.layout.activity_scan_list_item, deviceModels)
        val listView = findViewById<View>(R.id.list_View) as ListView
        listView.adapter = adapter

        adapter!!.setOnClickInItemLisenter { position, view ->
            onStartDeviceSetPager(position)
        }
    }

    private fun reStartScan() {
        ppScale?.stopSearch()
        tv_starts?.text = getString(R.string.start_scan)
        startScanDeviceList()
    }

    private fun onStartDeviceSetPager(position: Int) {
        val deviceModel = adapter!!.getItem(position) as PPDeviceModel?
        if (deviceModel != null) {
            val intent = Intent(this@ScanDeviceListActivity, PeripheralIceActivity::class.java)
            PeripheralIceActivity.deviceModel = deviceModel
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        isOnResume = false
        ppScale?.stopSearch()
    }

    /**
     * Get around bluetooth scale devices
     */
    fun startScanDeviceList() {
        if (ppScale == null) {
            ppScale = PPSearchManager()
        }
        //You can dynamically set the scan time in ms
        ppScale!!.startSearchDeviceList(300000, searchDeviceInfoInterface, bleStateInterface)
    }

    /**
     * 重新扫描
     */
    fun delayScan() {
        Handler(mainLooper).postDelayed({
            if (isOnResume) {
                startScanDeviceList()
            }
        }, 1000)
    }

    override fun onResume() {
        super.onResume()
        isOnResume = true
        delayScan()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (ppScale != null) {
            ppScale!!.stopSearch()
        }
    }

    var searchDeviceInfoInterface = PPSearchDeviceInfoInterface { ppDeviceModel, data ->

        /**
         *
         * @param ppDeviceModel 设备对象
         * @param data  广播数据
         */
        if (ppDeviceModel != null) {
            var deviceModel: PPDeviceModel? = null
            for (i in deviceModels.indices) {
                val model = deviceModels[i]
                if (model.deviceMac == ppDeviceModel.deviceMac) {
                    model.rssi = ppDeviceModel.rssi
                    deviceModel = model
                    deviceModels[i] = model
                }
            }
            if (deviceModel == null) {
                deviceModels.add(ppDeviceModel)
            }
            if (System.currentTimeMillis() - lastTimes > 500) {
                lastTimes = System.currentTimeMillis()
                adapter!!.notifyDataSetChanged()
            }
        }
    }
    var bleStateInterface: PPBleStateInterface = object : PPBleStateInterface() {
        /**
         * 蓝牙扫描和连接状态回调
         * @param ppBleWorkState 蓝牙状态标识
         * @param deviceModel 设备对象
         */
        override fun monitorBluetoothWorkState(
            ppBleWorkState: PPBleWorkState?,
            deviceModel: PPDeviceModel?
        ) {
            Logger.d("ScanDeviceListActivity bleStateInterface ppBleWorkState:$ppBleWorkState")
            ppBleWorkState?.let {
                if (ppBleWorkState == PPBleWorkState.PPBleStateSearchCanceled) {
                    Logger.d(getString(R.string.stop_scanning))
                    tv_starts!!.text = getString(R.string.bluetooth_status) + getString(R.string.stop_scanning)
                } else if (ppBleWorkState == PPBleWorkState.PPBleWorkSearchTimeOut) {
                    Logger.d(getString(R.string.scan_timeout))
                    tv_starts!!.text = getString(R.string.bluetooth_status) + getString(R.string.scan_timeout)
                } else if (ppBleWorkState == PPBleWorkState.PPBleWorkStateSearching) {
                    Logger.d(getString(R.string.scanning))
                    tv_starts!!.text = getString(R.string.bluetooth_status) + getString(R.string.scanning)
                } else {
                }
            }

        }

        /**
         * 系统蓝牙状态回调
         * @param ppBleSwitchState 系统蓝牙状态标识
         */
        override fun monitorBluetoothSwitchState(ppBleSwitchState: PPBleSwitchState) {
            if (ppBleSwitchState == PPBleSwitchState.PPBleSwitchStateOff) {
                Logger.e(getString(R.string.system_bluetooth_disconnect))
                Toast.makeText(
                    this@ScanDeviceListActivity,
                    getString(R.string.system_bluetooth_disconnect),
                    Toast.LENGTH_SHORT
                ).show()
                tv_starts!!.text =
                    getString(R.string.bluetooth_status) + getString(R.string.system_bluetooth_disconnect)
            } else if (ppBleSwitchState == PPBleSwitchState.PPBleSwitchStateOn) {
                delayScan()
                Logger.d(getString(R.string.system_blutooth_on))
                Toast.makeText(
                    this@ScanDeviceListActivity,
                    getString(R.string.system_blutooth_on),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}