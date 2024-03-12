package com.anker.ppblutoothkit.device.torre

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.anker.ppblutoothkit.R
import com.anker.ppblutoothkit.device.instance.PPBlutoothPeripheralIceInstance
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiInfoInterface
import com.peng.ppscale.business.v4.WifiConfigStep
import com.peng.ppscale.util.Logger
import com.peng.ppscale.vo.PPDeviceModel
import com.peng.ppscale.vo.PPWifiModel

/**
 *    @author :
 *    e-mail :
 *    date   : 2023/4/3 14:45
 *    desc   : 设备的wifi列表
 */
class PeripheralTorreSearchWifiListActivity : Activity() {

    private var mLoadAnimaLL: LinearLayout? = null
    private var mLoadAnimaIV: ImageView? = null
    private var mDeviceWifiListRV: RecyclerView? = null
    private var mWifiRefreshSB: Button? = null
    private val mDeviceSearchWifAdapter: WifiListAdapter = WifiListAdapter()

    private val TAG = PeripheralTorreSearchWifiListActivity::class.java.simpleName

    //旋转动画
    var mRotateAnimator: ObjectAnimator? = null

    companion object {
        var deviceModel: PPDeviceModel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_search_wifi_layout)
        initView()
    }

    /**
     * 初始化控件
     */
    fun initView() {
        mLoadAnimaLL = findViewById(R.id.mLoadAnimaLL)
        mLoadAnimaIV = findViewById(R.id.mLoadAnimaIV)
        mDeviceWifiListRV = findViewById(R.id.mDeviceWifiListRV)
        mWifiRefreshSB = findViewById(R.id.mWifiRefreshSB)

        mWifiRefreshSB?.setOnClickListener {
            getWifiList()
        }
        getWifiList()
        mDeviceWifiListRV?.adapter = mDeviceSearchWifAdapter
        mDeviceSearchWifAdapter.setOnClickInItemLisenter(object : WifiListAdapter.OnItemClickViewInsideListener {
            override fun onItemClickViewInside(position: Int, v: View?) {
                cloneRotateAnimator()
                PeripheralTorreConfigWifiActivity.deviceModel = deviceModel
                PeripheralTorreConfigWifiActivity.ssid = mDeviceSearchWifAdapter.users.get(position).ssid ?: ""
                startActivity(Intent(this@PeripheralTorreSearchWifiListActivity, PeripheralTorreConfigWifiActivity::class.java))
                finish()
            }
        })
    }

    /**
     * 旋转动画
     */
    protected fun showRotateAnimator(view: View?) {
        mRotateAnimator = ObjectAnimator.ofFloat(
            view,
            "rotation",
            0f,
            360f
        )
        mRotateAnimator!!.setAutoCancel(true)
        mRotateAnimator!!.duration = 2000
        mRotateAnimator!!.repeatCount = 9999
        mRotateAnimator!!.interpolator = LinearInterpolator()
        mRotateAnimator!!.start()
    }

    /**
     * 关闭旋转动效
     */
    protected fun cloneRotateAnimator() {
        mRotateAnimator?.clone()
        mRotateAnimator?.cancel()
        mRotateAnimator = null
    }

    /**
     * 启动配网
     */
    private fun getWifiList() {
        mLoadAnimaLL?.visibility = View.VISIBLE
        showRotateAnimator(mLoadAnimaIV)
        //读取附近的wifi列表
        PPBlutoothPeripheralIceInstance.instance.controller?.getWifiList(configWifiInfoInterface)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        PPBlutoothPeripheralIceInstance.instance.controller?.exitConfigWifi()
    }

    val configWifiInfoInterface = object : PPConfigWifiInfoInterface {

        override fun monitorWiFiListSuccess(wifiModels: MutableList<PPWifiModel>?) {
            cloneRotateAnimator()
            mDeviceSearchWifAdapter.users = wifiModels?.toMutableList() ?: mutableListOf()
//                mDeviceSearchWifAdapter.notifyDataSetChanged()
            mLoadAnimaLL?.visibility = View.GONE
            mWifiRefreshSB?.visibility = View.VISIBLE
        }

        override fun monitorWiFiListFail(state: Int?) {
            Logger.e("$TAG monitorWiFiListFail state:$state")
        }


    }

}