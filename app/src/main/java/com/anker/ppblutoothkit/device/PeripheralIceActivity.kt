package com.anker.ppblutoothkit.device

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.anker.ppblutoothkit.R
import com.anker.ppblutoothkit.UserinfoActivity
import com.anker.ppblutoothkit.calculate.Calculate4ACActivitiy
import com.anker.ppblutoothkit.device.instance.PPBlutoothPeripheralIceInstance
import com.anker.ppblutoothkit.device.torre.PeripheralTorreSearchWifiListActivity
import com.anker.ppblutoothkit.util.DataUtil
import com.anker.ppblutoothkit.view.MsgDialog
import com.peng.ppscale.business.ble.PPScaleHelper
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiInfoInterface
import com.peng.ppscale.business.ble.listener.*
import com.peng.ppscale.business.state.PPBleSwitchState
import com.peng.ppscale.business.state.PPBleWorkState
import com.peng.ppscale.device.PeripheralIce.PPBlutoothPeripheralIceController
import com.peng.ppscale.util.PPUtil
import com.peng.ppscale.vo.PPBodyBaseModel
import com.peng.ppscale.vo.PPDeviceModel
import com.peng.ppscale.vo.PPScaleSendState

/**
 * 对应的协议: 4.x
 * 连接类型:连接
 * 设备类型 人体秤
 */
class PeripheralIceActivity : AppCompatActivity() {

    private var weightTextView: TextView? = null
    private var logTxt: TextView? = null
    private var device_set_connect_state: TextView? = null
    private var weightMeasureState: TextView? = null

    var controller: PPBlutoothPeripheralIceController? = PPBlutoothPeripheralIceInstance.instance.controller

    companion object {
        var deviceModel: PPDeviceModel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.peripheral_ice_layout)

        weightTextView = findViewById<TextView>(R.id.weightTextView)
        logTxt = findViewById<TextView>(R.id.logTxt)
        device_set_connect_state = findViewById<TextView>(R.id.device_set_connect_state)
        weightMeasureState = findViewById<TextView>(R.id.weightMeasureState)
        val nestedScrollViewLog = findViewById<NestedScrollView>(R.id.nestedScrollViewLog)

        logTxt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                nestedScrollViewLog.fullScroll(View.FOCUS_DOWN)
            }
        })
        setTitle("${deviceModel?.getDevicePeripheralType()}")
        addPrint("startConnect")
        controller?.registDataChangeListener(dataChangeListener)
        deviceModel?.let { it1 -> controller?.startConnect(it1, bleStateInterface) }
        initClick()

    }

    fun initClick() {
        findViewById<Button>(R.id.startConnectDevice).setOnClickListener {
            addPrint("startConnect")
            controller?.registDataChangeListener(dataChangeListener)
            deviceModel?.let { it1 -> controller?.startConnect(it1, bleStateInterface) }
        }
        findViewById<Button>(R.id.syncTime).setOnClickListener {
            addPrint("syncTime")
            controller?.syncTime(object : PPBleSendResultCallBack {
                override fun onResult(sendState: PPScaleSendState?) {
                    if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                        addPrint("syncTime send success")
                    } else {
                        addPrint("syncTime send fail")
                    }
                }
            })
        }
        findViewById<Button>(R.id.readDeviceInfo).setOnClickListener {
            addPrint("readDeviceInfo")
            controller?.readDeviceInfo(object : PPDeviceInfoInterface() {
                override fun readDeviceInfoComplete(deviceModel: PPDeviceModel?) {
                    addPrint("firmwareVersion: ${deviceModel?.firmwareVersion}")
                    addPrint("serialNumber: ${deviceModel?.serialNumber}")
                    addPrint("hardwareRevision: ${deviceModel?.hardwareVersion}")
                    addPrint("manufacturerName: ${deviceModel?.manufacturerName}")
                    addPrint("modelNumber: ${deviceModel?.modelNumber}")
                }
            })
        }
        findViewById<Button>(R.id.syncUnit).setOnClickListener {
            addPrint("syncUnit:${DataUtil.util().unit}")
            controller?.syncUnit(DataUtil.util().unit, object : PPBleSendResultCallBack {
                override fun onResult(sendState: PPScaleSendState?) {
                    if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                        addPrint("syncUnit send success")
                    } else {
                        addPrint("syncUnit send fail")
                    }
                }
            })
        }
        findViewById<Button>(R.id.syncUserHistoryData).setOnClickListener {
            addPrint("syncUserHistoryData")
            if (PPScaleHelper.isSupportHistoryData(deviceModel?.deviceFuncType)) {
                controller?.getHistory(object : PPHistoryDataInterface() {
                    override fun monitorHistoryData(bodyBaseModel: PPBodyBaseModel?, dateTime: String?) {
                        addPrint("monitorHistoryData weight: ${bodyBaseModel?.weight}" + " dateTime:$dateTime")
                    }

                    override fun monitorHistoryEnd() {
                        addPrint("monitorHistoryEnd")
                    }

                    override fun monitorHistoryFail() {
                        addPrint("monitorHistoryFail")
                    }
                })
            } else {
                addPrint("device does not support")
            }
        }
        findViewById<Button>(R.id.deleteHistory).setOnClickListener {
            addPrint("deleteHistory")
            if (PPScaleHelper.isSupportHistoryData(deviceModel?.deviceFuncType) ?: false) {
                controller?.deleteHistoryData(object : PPBleSendResultCallBack {
                    override fun onResult(sendState: PPScaleSendState?) {
                        if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                            addPrint("deleteHistory success")
                        } else {
                            addPrint("deleteHistory fail")
                        }
                    }
                })
            } else {
                addPrint("device does not support")
            }
        }
        findViewById<Button>(R.id.startConfigWifi).setOnClickListener {
            addPrint("startConfigWifi pager")
            if (PPScaleHelper.isFuncTypeWifi(deviceModel?.deviceFuncType)) {
                controller?.readDeviceBattery(object : PPDeviceInfoInterface() {
                    override fun readDevicePower(power: Int) {
                        controller?.readDeviceBattery(null)
                        addPrint("power:%$power")
                        if (power > 20) {
                            PeripheralTorreSearchWifiListActivity.deviceModel = deviceModel
                            startActivity(Intent(this@PeripheralIceActivity, PeripheralTorreSearchWifiListActivity::class.java))
                        } else {
                            addPrint("Low battery, please charge first")
                        }
                    }
                })
            } else {
                addPrint("device does not support")
            }

        }
        findViewById<Button>(R.id.getWifiInfo).setOnClickListener {
            addPrint("getWifiInfo")
            if (PPScaleHelper.isFuncTypeWifi(deviceModel?.deviceFuncType)) {
                controller?.getWifiInfo(configWifiInfoInterface)
            } else {
                addPrint("device does not support")
            }
        }
        findViewById<Button>(R.id.setUserInfo).setOnClickListener {
            addPrint("start UserInfo pager")
            startActivity(Intent(this, UserinfoActivity::class.java))
        }
        findViewById<Button>(R.id.readDeviceBattery).setOnClickListener {
            addPrint("readDeviceBattery")
            controller?.readDeviceBattery(object : PPDeviceInfoInterface() {
                override fun readDevicePower(power: Int) {
                    addPrint("power:$power")
                }
            })
        }
        findViewById<Button>(R.id.device_set_reset).setOnClickListener {
            addPrint("resetDevice")
            controller?.resetDevice(deviceSetInterface)
        }
        findViewById<Button>(R.id.startKeepAlive).setOnClickListener {
            addPrint("startKeepAlive")
            controller?.stopKeepAlive()
            controller?.startKeepAlive()
        }
        findViewById<ToggleButton>(R.id.heartRateModeToggleBtn).setOnCheckedChangeListener { buttonView, isChecked ->
            addPrint("heart rate mode isChecked:$isChecked")
            /**
             * 心率开关
             *
             * @param state  0打开 1关闭
             */
            controller?.controlHeartRate(if (isChecked) 0 else 1, modeChangeInterface)
        }
        findViewById<ToggleButton>(R.id.pregnancyModeToggleBtn).setOnCheckedChangeListener { buttonView, isChecked ->
            addPrint("maternity mode isChecked:$isChecked")
            /**
             * 孕妇模式
             *
             * @param state 1打开孕妇模式 0关闭孕妇模式
             */
            controller?.controlImpendance(if (isChecked) 1 else 0, modeChangeInterface)
        }
        findViewById<ToggleButton>(R.id.babyModeToggleBtn).setOnCheckedChangeListener { buttonView, isChecked ->
            addPrint("baby mode isChecked:$isChecked")
            /**
             * 婴儿模式
             *
             */
            if (isChecked) {
                controller?.startBaby(modeChangeInterface)
            } else {
                controller?.exitBaby(modeChangeInterface)
            }
        }
        findViewById<ToggleButton>(R.id.petModeToggleBtn).setOnCheckedChangeListener { buttonView, isChecked ->
            addPrint("pet mode isChecked:$isChecked")
            /**
             * 宠物模式
             */
            if (isChecked) {
                controller?.startPet(modeChangeInterface)
            } else {
                controller?.exitPet(modeChangeInterface)
            }
        }
    }

    val deviceSetInterface = object : PPDeviceSetInfoInterface {
        override fun monitorResetStateSuccess() {
            addPrint("monitorResetStateSuccess")
        }

        override fun monitorResetStateFail() {
            addPrint("monitorResetStateFail")
        }

    }

    val modeChangeInterface = object : PPTorreDeviceModeChangeInterface {

        /**
         * 心率开关状态
         *
         * @param type  1设置开关 2获取开关
         * @param state 0打开 1关闭
         */
        override fun readHeartRateStateCallBack(type: Int, state: Int) {
            if (type == 1) {//设置开关
                if (state == 0) {
                    addPrint("heart rate mode open success")
                } else {
                    addPrint("heart rate mode close success")
                }
            } else {//获取开关
                if (state == 0) {//0打开 1关闭
                    addPrint("heart rate state is on")
                } else {
                    addPrint("heart rate state is off")
                }
            }
        }

        override fun switchBabyModeCallBack(state: Int) {
            if (state == 0) {
                addPrint("switch Baby mode open success")
            } else {
                addPrint("switch Baby mode close success")
            }
        }

        override fun switchPetModeCallBack(state: Int) {
            if (state == 0) {
                addPrint("switch pet mode open success")
            } else {
                addPrint("switch pet mode close success")
            }
        }

        /**
         * @param
         *  state 0关闭孕妇模式 1打开孕妇模式
         */
        override fun controlImpendanceCallBack(state: Int) {
            if (state == 0) {
                addPrint("Turn off pregnant woman mode Success")
            } else {
                addPrint("Open Pregnant Women Mode Success ")
            }
        }

    }

    val dataChangeListener = object : PPDataChangeListener {

        /**
         * 监听过程数据
         *
         * @param bodyBaseModel
         * @param deviceModel
         */
        override fun monitorProcessData(bodyBaseModel: PPBodyBaseModel?) {
            val weightStr = PPUtil.getWeightValueD(bodyBaseModel?.unit, bodyBaseModel?.getPpWeightKg()?.toDouble() ?: 0.0, deviceModel!!.deviceAccuracyType.getType())
            weightTextView?.text = "process:$weightStr ${PPUtil.getWeightUnit(bodyBaseModel?.unit)}"
            weightMeasureState?.text = ""
        }

        /**
         * 锁定数据
         *
         * @param bodyBaseModel
         */
        override fun monitorLockData(bodyBaseModel: PPBodyBaseModel?) {
            val weightStr = PPUtil.getWeightValueD(bodyBaseModel?.unit, bodyBaseModel?.getPpWeightKg()?.toDouble() ?: 0.0, deviceModel!!.deviceAccuracyType.getType())
            weightTextView?.text = "lock:$weightStr ${PPUtil.getWeightUnit(bodyBaseModel?.unit)}"

            //这里要填称重用户的个人信息 Here we need to fill in the personal information of the weighing user
            val userModel = DataUtil.util().userModel
            bodyBaseModel?.userModel = userModel
            //测量到稳定重量
            addPrint("Stable weight measured weight:$weightStr")
            weightMeasureState?.text = getString(R.string.measure_complete)
            MsgDialog.init(supportFragmentManager)
                .setTitle(getString(R.string.tips))
                .setMessage(getString(R.string.is_body_fat_calculated))
                .setAnimStyle(R.style.dialog_)
                .setCancelableAll(true)
                .setNegativeButton(getString(R.string.cancel))
                .setPositiveButton(getString(R.string.confirm), View.OnClickListener {
                    DataUtil.util().bodyBaseModel = bodyBaseModel

                    //4电极交流算法  24项数据
                    val intent = Intent(this@PeripheralIceActivity, Calculate4ACActivitiy::class.java)
                    intent.putExtra("bodyDataModel", "bodyDataModel")
                    startActivity(intent)

                })
                .show()
        }

        override fun monitorHeartRateData(heartRate: Int, isHeartRating: Boolean) {
            if (isHeartRating) {
                //心率测量中
                weightMeasureState?.text = getString(R.string.heartrate_mesuring)
                addPrint("In heart rate measurement")
            } else {
                weightMeasureState?.text = getString(R.string.heart_rate_measure_completed)
                addPrint("Heart rate measurement completed heart rate:$heartRate")
            }
        }

        /**
         * 超重/overweight
         */
        override fun monitorOverWeight() {
            weightMeasureState?.text = getString(R.string.overweight)
            addPrint("overweight")
        }

    }

    val configWifiInfoInterface = object : PPConfigWifiInfoInterface {

        override fun monitorConfigSsid(ssid: String?, deviceModel: PPDeviceModel?) {
            addPrint("getWifiInfo ssid:$ssid")
        }

        override fun monitorConfigPassword(password: String?, deviceModel: PPDeviceModel?) {
            addPrint("getWifiInfo password:$password")
        }

        override fun monitorModifyServerDomainSuccess() {
            addPrint("ModifyServerDNSSuccess")
        }

    }

    fun addPrint(msg: String) {
        if (msg.isNotEmpty()) {
            logTxt?.append("$msg\n")
        }
    }

    val bleStateInterface = object : PPBleStateInterface() {
        override fun monitorBluetoothWorkState(ppBleWorkState: PPBleWorkState?, deviceModel: PPDeviceModel?) {
            if (ppBleWorkState == PPBleWorkState.PPBleWorkStateConnected) {
                device_set_connect_state?.text = getString(R.string.device_connected)
                addPrint(getString(R.string.device_connected))
            } else if (ppBleWorkState == PPBleWorkState.PPBleWorkStateConnecting) {
                device_set_connect_state?.text = getString(R.string.device_connecting)
                addPrint(getString(R.string.device_connecting))
            } else if (ppBleWorkState == PPBleWorkState.PPBleWorkStateDisconnected) {
                device_set_connect_state?.text = getString(R.string.device_disconnected)
                addPrint(getString(R.string.device_disconnected))
            } else if (ppBleWorkState == PPBleWorkState.PPBleStateSearchCanceled) {
                device_set_connect_state?.text = getString(R.string.stop_scanning)
                addPrint(getString(R.string.stop_scanning))
            } else if (ppBleWorkState == PPBleWorkState.PPBleWorkSearchTimeOut) {
                device_set_connect_state?.text = getString(R.string.scan_timeout)
                addPrint(getString(R.string.scan_timeout))
            } else if (ppBleWorkState == PPBleWorkState.PPBleWorkStateSearching) {
                device_set_connect_state?.text = getString(R.string.scanning)
                addPrint(getString(R.string.scanning))
            } else if (ppBleWorkState == PPBleWorkState.PPBleWorkStateWritable) {
                //启动保活指令
                PPBlutoothPeripheralIceInstance.instance.controller?.startKeepAlive()
                addPrint(getString(R.string.writable))
            }
        }

        override fun monitorBluetoothSwitchState(ppBleSwitchState: PPBleSwitchState?) {
            if (ppBleSwitchState == PPBleSwitchState.PPBleSwitchStateOff) {
                addPrint(getString(R.string.system_bluetooth_disconnect))
                Toast.makeText(this@PeripheralIceActivity, getString(R.string.system_bluetooth_disconnect), Toast.LENGTH_SHORT).show()
            } else if (ppBleSwitchState == PPBleSwitchState.PPBleSwitchStateOn) {
                addPrint(getString(R.string.system_blutooth_on))
                Toast.makeText(this@PeripheralIceActivity, getString(R.string.system_blutooth_on), Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        PPBlutoothPeripheralIceInstance.instance.controller?.stopKeepAlive()
        controller?.disConnect()
    }


}