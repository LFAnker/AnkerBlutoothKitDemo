package com.peng.ppscale.business.ota;

import android.content.Context;

public class OTAManager {

    private static volatile OTAManager instance = null;
    private final OTADelegate otaDelegate;

    private OTAManager() {
        otaDelegate = new OTADelegate();
    }

    public static OTAManager getInstance() {
        if (instance == null) {
            synchronized (OTAManager.class) {
                if (instance == null) {
                    instance = new OTAManager();
                }
            }
        }
        return instance;
    }

    /**
     * 检测升级包的版本信息
     */
    public void checkBinVersion() {

    }

    /**
     * 查询硬件版本信息
     * <p>
     * 读取 Device Information->Serial Number String 的值为：20220212
     */
    public void queryFirmwareVersionInfo() {

    }

    /**
     * 向蓝牙发起升级请求
     * context.getAssets().open(binPath)
     */
    public void startUpgradeRequest(String binPath, Context context) {
        //读取升级包 bin 文件的前 16byte
        if (binPath != null && context != null) {
            otaDelegate.startUpgradeRequest(binPath, context);
        }
    }

    public void addOnOtaStateListener(OnOTAStateListener otaStateListener) {
        otaDelegate.addOnOtaStateListener(otaStateListener);
    }

    /**
     * 退出升级模式
     */
    public void exitUpgrade() {
        otaDelegate.exitUpgrade();
    }

    public OTADelegate getOtaDelegate() {
        return otaDelegate;
    }

    public boolean isOTA() {
        return otaDelegate.isOTA;
    }
}
