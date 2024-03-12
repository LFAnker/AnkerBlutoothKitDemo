package com.peng.ppscale.business.ble.listener;

import com.peng.ppscale.vo.PPBodyBaseModel;
import com.peng.ppscale.vo.PPBodyFatModel;
import com.peng.ppscale.vo.PPDeviceModel;

/**
 * 锁定数据接口回调
 */
public class PPLockDataInterface {

    /**
     * 锁定数据
     *
     * @param bodyBaseModel
     */
    public void monitorLockData(PPBodyBaseModel bodyBaseModel, PPDeviceModel deviceModel) {
    }

    /**
     * 秤端计算的数据，在使用时要设置UserModel
     *
     * @param bodyBaseModel
     */
    public void monitorLockDataByCalculateInScale(PPBodyFatModel bodyBaseModel) {
    }

    /**
     * 超重
     */
    public void monitorOverWeight() {
    }

}
