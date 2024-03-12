package com.peng.ppscale.business.ble.listener;

import com.peng.ppscale.vo.PPScaleSendState;

public interface PPBleSendResultCallBack {

    /**
     * 历史数据
     *
     * @param sendState
     */
    void onResult(PPScaleSendState sendState);

}
