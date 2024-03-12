package com.peng.ppscale.business.ble.bmdj;

public interface PPBMDJDataInterface {

    /**
     * 测量过程数据
     *
     * @param standTime
     */
    void monitorBMDJStandTime(int standTime);

    /**
     * 测量完成数据
     *
     * @param standTime
     */
    void monitorBMDJMeasureEnd(int standTime);

}
