package com.anker.ppblutoothkit.device.torre

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.anker.ppblutoothkit.R
import com.anker.ppblutoothkit.device.instance.PPBlutoothPeripheralIceInstance
import com.anker.ppblutoothkit.okhttp.DataTask
import com.anker.ppblutoothkit.okhttp.NetUtil
import com.anker.ppblutoothkit.okhttp.RetCallBack
import com.anker.ppscale.db.dao.DBManager
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiInfoInterface
import com.peng.ppscale.business.v4.WifiConfigStep
import com.peng.ppscale.util.Logger
import com.peng.ppscale.vo.PPDeviceModel


class PeripheralTorreConfigWifiActivity : Activity() {

    var configResultTV: TextView? = null

    var etWifiKey: EditText? = null
    var code: String? = ""//Retrieve from the server side
    var domain: String? = ""
    var pwd = ""
    val TAG = PeripheralTorreConfigWifiActivity::class.java.simpleName
    val uid = "5f8f68f8faae9f03fe3fc6c6cf3241657d77387d"

    companion object {
        var ssid = ""
        var deviceModel: PPDeviceModel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_config_torre)

        findViewById<TextView>(R.id.etWifiName)?.text = ssid
        etWifiKey = findViewById<EditText>(R.id.etWifiKey)

        configResultTV = findViewById<TextView>(R.id.configResultTV)

        findViewById<Button>(R.id.tvNext).setOnClickListener {
            if (etWifiKey?.text != null) {
                pwd = etWifiKey?.text.toString()
            }
            //注意：域名在您自己的App中需要换成你App自己的服务器域名，并确保服务器已完成Wifi体脂秤相关功能开发，
            //https://xinzhiyun.feishu.cn/docs/doccnvyxdXw8injIz3ZfxRjBtTc?from=from_copylink
            domain = NetUtil.SCALE_DOMAIN
            configResultTV?.text = getString(R.string.start_config_net)
            domain?.let {
                if (it.contains("http://")) {
                    domain = domain?.replace("http://", "")
                }
            }
            getCode()
        }

    }

    fun getCode() {

        val mapHeads: MutableMap<String, String?> = HashMap()
        mapHeads["Content-Type"] = "application/json"
        mapHeads["uid"] = uid
        mapHeads["token"] = "vBiOqJYJQyWIww-VIElSOg"
        mapHeads["category"] = "Health"

        val requestBodyContent = "{" +
                "\"client_id\": \"wifi_scale\"," +
                "\"client_secret\": \"ICXQIBAAKBgQCcoakXBN\"," +
                "\"device_id\": \"eufyt9149cfe801044272\"," +
                "\"product_code\": \"eufy T9149\"" +
                "}"

        DataTask.postString(NetUtil.GET_SCALE_CONFIG, requestBodyContent, mapHeads, object : RetCallBack<BindCodeVo>(BindCodeVo::class.java) {

            override fun onResponse(response: BindCodeVo?, p1: Int) {
                response?.let {
                    if (response.code.isNullOrEmpty().not()) {
                        code = response.code
                        /**
                         * 启动配网
                         *  @see PPConfigWifiInfoInterface#monitorConfigStep(WifiConfigStep, Boolean)
                         * @see WifiConfigStep.WifiConfigStep_Start
                         */
                        PPBlutoothPeripheralIceInstance.instance.controller?.configWifiStart(configWifiInfoInterface)
                    } else {
                        val content = if (TextUtils.isEmpty(response.message)) getString(R.string.config_wifi_fail) else response.message
                        Toast.makeText(this@PeripheralTorreConfigWifiActivity, content, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError(p0: okhttp3.Call?, p1: java.lang.Exception?, p2: Int) {
                Toast.makeText(this@PeripheralTorreConfigWifiActivity, R.string.config_wifi_fail, Toast.LENGTH_SHORT).show()
            }
        })

    }

    val configWifiInfoInterface = object : PPConfigWifiInfoInterface {

        override fun monitorConfigStep(step: WifiConfigStep, isSuccess: Boolean) {
            Logger.d("$TAG monitorConfigStep step:${step.name} isSuccess:$isSuccess")
            when (step) {
                WifiConfigStep.WifiConfigStep_Start -> {
                    code?.let { it1 ->
                        domain?.let { it2 ->
                            Logger.d("$TAG configNewCodeUidUrl: code:$code uid:$uid domain:$domain")
                            /**
                             * 下发配网code、uid、服务器域名
                             * 如果失败，需要重新发送配网code, code需要与服务器协商一致，确保code能正常使用
                             *
                             * @see PPConfigWifiInfoInterface#monitorConfigStep(WifiConfigStep, Boolean)
                             * @see WifiConfigStep.WifiConfigStep_SendCodeUidDomain
                             */
                            PPBlutoothPeripheralIceInstance.instance.controller?.configNewCodeUidUrl(it1, uid, it2, this)
                        }
                    }
                }

                WifiConfigStep.WifiConfigStep_SendCodeUidDomain -> {
                    /**
                     * 下发配网code、uid、服务器域名
                     * 如果失败，需要重新发送配网code, code需要与服务器协商一致，确保code能正常使用
                     *
                     */
                    if (isSuccess) {
                        Logger.d("$TAG monitorConfigStep configNewCodeUidUrl isSuccess:$isSuccess")
                        /**
                         * 开始下发域名证书
                         * @see WifiConfigStep.WifiConfigStep_DomainCertificate
                         */
                        PPBlutoothPeripheralIceInstance.instance.controller?.configDomainCertificate(NetUtil.CERTIFICATE, this)
                    } else {
                        Logger.e("$TAG monitorConfigStep configNewCodeUidUrl isSuccess:$isSuccess")
                    }
                }

                WifiConfigStep.WifiConfigStep_DomainCertificate -> {
                    /**
                     * 下发域名证书回调
                     */
                    if (isSuccess) {
                        Logger.d("$TAG monitorConfigStep configDomainCertificate isSuccess:$isSuccess")
                        /**
                         * 开始下发SSID
                         * @see WifiConfigStep.WifiConfigStep_SSID
                         */
                        PPBlutoothPeripheralIceInstance.instance.controller?.configUpdateWifiSSID(ssid, this)
                    } else {
                        Logger.e("$TAG monitorConfigStep configDomainCertificate isSuccess:$isSuccess")
                    }
                }

                WifiConfigStep.WifiConfigStep_SSID -> {
                    /**
                     * 下发SSID回调
                     */
                    if (isSuccess) {
                        Logger.d("$TAG monitorConfigStep configUpdateWifiSSID isSuccess:$isSuccess")
                        /**
                         * 开始下发密码
                         * @see WifiConfigStep.WifiConfigStep_PWD
                         */
                        PPBlutoothPeripheralIceInstance.instance.controller?.configUpdateWifiPassword(pwd, this)
                    } else {
                        Logger.e("$TAG monitorConfigStep configUpdateWifiSSID isSuccess:$isSuccess")
                    }
                }

                /**
                 * 下发密码回调
                 */
                WifiConfigStep.WifiConfigStep_PWD -> {
                    if (isSuccess) {
                        Logger.d("$TAG monitorConfigStep configUpdateWifiPassword isSuccess:$isSuccess")
                        /**
                         * WiFi参数下发结束，开始连接网络
                         * @see WifiConfigStep_ConnectToRouter
                         *
                         */
                        PPBlutoothPeripheralIceInstance.instance.controller?.startConnectRouter(this)
                    } else {
                        Logger.e("$TAG monitorConfigStep configUpdateWifiPassword isSuccess:$isSuccess")
                    }
                }

                /**
                 * 连接路由器网络回调
                 */
                WifiConfigStep.WifiConfigStep_ConnectToRouter -> {
                    if (isSuccess) {
                        Logger.d("$TAG monitorConfigStep startConnectRouter isSuccess:$isSuccess")
                        //等待monitorConfigResult返回结果，如果中途失败请查看 monitorConfigFail

                    } else {
                        Logger.e("$TAG monitorConfigStep ConnectRouter fail")
                        configResultTV?.text = "ConnectRouter fail"
                    }
                }

            }
        }

        override fun monitorConfigResult(isSuccess: Boolean) {
            if (isSuccess) {
                configResultTV?.text = "Config wifi Success"
                Toast.makeText(this@PeripheralTorreConfigWifiActivity, R.string.config_wifi_success, Toast.LENGTH_SHORT).show()
                deviceModel?.let {
                    val device = DBManager.manager().getDevice(deviceModel?.deviceMac)
                    if (device != null) {
                        device.ssid = ssid
                        DBManager.manager().updateDevice(device)
                    }
                }
            } else {
                configResultTV?.text = "Config wifi fail"
            }
        }

        override fun monitorModifyServerDomainSuccess() {
            addPrint("monitorModifyServerDomainSuccess")
            configResultTV?.text = "monitorModifyServerDomainSuccess"
            if (!TextUtils.isEmpty(ssid)) {
                if (etWifiKey?.text != null) {
                    pwd = etWifiKey?.text.toString()
                }
            } else {
                configResultTV?.text = "ssid is null"
                Logger.e("configwifi monitorModifyServerDomainSuccess onConfigResultFail ssid is null")
            }
        }

        /**
         * @param configStep 当前进行到的步骤，  WIFI准备配网 - 0x11；下发device id - 0x12；下发product code - 0x13；配网开始指令 - 0x14；接收域名、uid - 0x15；接收证书 - 0x16；证书接收完成 - 0x17；WIFI list-  0x18；接收SSID - 0x19；接收password - 0x1A；配网完成指令 - 0x1B；删除WIFI参数 - 0x1C；连接路由器-0x25
         * @param errorType 超时 - 0x01；接收到wifi错误码 - 0x02；接收到云端返回res_code错误码 - 0x0d
         * @param resultCode 接收到的数据
         */
        override fun monitorConfigFail(configStep: Byte, errorType: Byte, resultCode: String?) {
            configResultTV?.text = "Config wifi fail"
            addPrint("configStep:$configStep errorType:$errorType resultCode:$resultCode")
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        PPBlutoothPeripheralIceInstance.instance.controller?.stopKeepAlive()
        PPBlutoothPeripheralIceInstance.instance.controller?.exitConfigWifi()

    }

    fun addPrint(msg: String) {
        Logger.d("msg:$msg")
    }

}