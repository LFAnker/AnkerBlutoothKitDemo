package com.peng.ppscale.business.ble.listener;

import com.peng.ppscale.vo.PPBodyBaseModel;
import com.peng.ppscale.vo.PPBodyFatModel;
import com.peng.ppscale.vo.PPDeviceModel;

/**
 * 过程数据
 */
public class PPHistoryDataInterface {

    public void monitorHistoryData(PPBodyBaseModel bodyBaseModel, String dateTime) {
    }

    public void monitorHistoryEnd() {
    }

    public void monitorHistoryFail() {
    }

    public void historyDeleteResult(boolean isSuccess) {
    }



}
