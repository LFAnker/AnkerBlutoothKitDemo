package com.peng.ppscale.business.ble.listener;

import com.peng.ppscale.vo.PPBodyBaseModel;
import com.peng.ppscale.vo.PPDeviceModel;

/**
 * 过程数据
 */
public class PPProcessDateInterface {

    /**
     * 监听过程数据
     *
     * @param bodyBaseModel
     * @param deviceModel
     */
    public void monitorProcessData(PPBodyBaseModel bodyBaseModel, PPDeviceModel deviceModel){}

    /**
     * 阻抗测量状态，state 0 测量中  1 测量完成  2 测量失败
     */
    public void onImpedanceFatting(){}

    @Deprecated
    public void onHeartRateRating(){

    }


    public void onDeviceShutdown() {


    }
}
