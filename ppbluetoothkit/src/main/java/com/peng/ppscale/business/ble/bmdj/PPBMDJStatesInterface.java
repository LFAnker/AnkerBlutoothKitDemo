package com.peng.ppscale.business.ble.bmdj;

public interface PPBMDJStatesInterface {

    /**
     * 闭目单脚退出成功
     */
    void monitorBMDJExitSuccess();

    /**
     * 闭目单脚退出失败
     */
    void monitorBMDJExittFail();

    /**
     * 开始测量
     */
    void startTiming();

}
