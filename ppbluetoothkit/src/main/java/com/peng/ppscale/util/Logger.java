package com.peng.ppscale.util;

import com.peng.ppscalelibrary.BuildConfig;

@SuppressWarnings("unused")
public class Logger {
    private static final String TAG = "PPScale";
    public static boolean enabled = false;

    public static OnLogCallBack onLogCallBack;

    public static void e(String msg) {
        if (onLogCallBack != null) {
            onLogCallBack.loge(TAG, msg);
        }
//        if (!enabled) return;
        android.util.Log.e(TAG, getLogHead() + msg);
    }

    public static void i(String msg) {
        if (onLogCallBack != null) {
            onLogCallBack.logi(TAG, msg);
        }
        if (!enabled) return;
        android.util.Log.i(TAG, getLogHead() + msg);
    }

    public static void d(String msg) {
        if (onLogCallBack != null) {
            onLogCallBack.logd(TAG, msg);
        }
        if (!enabled) return;
        android.util.Log.d(TAG, getLogHead() + msg);
    }

    public static void w(String msg) {
        if (onLogCallBack != null) {
            onLogCallBack.logw(TAG, msg);
        }
        if (!enabled) return;
        android.util.Log.w(TAG, getLogHead() + msg);
    }

    public static void v(String msg) {
        if (onLogCallBack != null) {
            onLogCallBack.logv(TAG, msg);
        }
        if (!enabled) return;
        android.util.Log.v(TAG, getLogHead() + msg);
    }

    public static String getLogHead() {
        return "current thread: " + Thread.currentThread().getName() + " msg:";
    }


}
