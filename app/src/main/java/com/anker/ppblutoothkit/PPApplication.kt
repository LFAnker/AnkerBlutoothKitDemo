package com.anker.ppblutoothkit

import android.app.Application
import com.anker.ppblutoothkit.util.LogUtils
import com.anker.ppblutoothkit.util.log.MyQueueLinkedUtils
import com.anker.ppscale.db.dao.DBManager
import com.peng.ppscale.PPBlutoothKit
import com.peng.ppscale.util.OnLogCallBack

class PPApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //数据库初始化
        DBManager.initGreenDao(this)
        //SP缓存
        SettingManager.get(this)
        //日志写入文件
        MyQueueLinkedUtils.start(this)
        PPBlutoothKit.setDebugLogCallBack(object : OnLogCallBack() {
            override fun logd(s: String?, s1: String?) {
                s1?.let { LogUtils.writeBluetoothLog(it) }
            }

            override fun logv(s: String?, s1: String?) {
//                LogUtils.INSTANCE.writeBluetoothLog(s1);
            }

            override fun logw(s: String?, s1: String?) {
                s1?.let { LogUtils.writeBluetoothLog(it) }
            }

            override fun loge(s: String?, s1: String?) {
                s1?.let { LogUtils.writeBluetoothLog(it) }
            }
        })
        /*********************以下内容为SDK的配置项***************************************/
        //SDK日志打印控制，true会打印
        PPBlutoothKit.setDebug(BuildConfig.DEBUG)
        /**
         * SDK 初始化
         */
        PPBlutoothKit.initSdk(this)

    }


}