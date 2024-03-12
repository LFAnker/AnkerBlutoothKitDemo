package com.peng.ppscale.business.ble.listener;

import com.peng.ppscale.vo.LFFoodScaleGeneral;
import com.peng.ppscale.vo.PPDeviceModel;

public class FoodScaleDataChangeListener {

    /**
     * 监听过程数据
     *
     * @param deviceModel
     */
   public void processData(LFFoodScaleGeneral foodScaleGeneral, PPDeviceModel deviceModel){}

    /*锁定数据*/
    public void lockedData(LFFoodScaleGeneral foodScaleGeneral, PPDeviceModel deviceModel){}

    public void historyData(LFFoodScaleGeneral foodScaleGeneral, PPDeviceModel deviceModel, String yyyyMMdd, boolean isEnd){}

    /*当前的设备信息*/
//    void deviceInfo(BleDeviceModel deviceModel);


}
