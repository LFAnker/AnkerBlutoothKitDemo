package com.peng.ppscale.business.ble.listener

import com.peng.ppscale.vo.PPBodyBaseModel
import com.peng.ppscale.vo.PPBodyFatModel
import com.peng.ppscale.vo.PPDeviceModel

/**
 * 过程数据
 */
interface PPDataChangeListener {
    /**
     * 监听过程数据
     *
     * @param bodyBaseModel
     * @param deviceModel
     */
    fun monitorProcessData(bodyBaseModel: PPBodyBaseModel?) {}

    /**
     * 锁定数据
     *
     * @param bodyBaseModel
     */
    fun monitorLockData(bodyBaseModel: PPBodyBaseModel?) {}

    /**
     * 心率测量完成
     */
    fun monitorHeartRateData(heartRate: Int, isHeartRating: Boolean) {}

    /**
     * 宠物或婴儿重量锁定
     */
    fun monitorBabyPetLockData(bodyBaseModel: PPBodyBaseModel?) = {}

    /**
     * 测量失败
     */
    fun monitorDataFail(bodyBaseModel: PPBodyBaseModel?, deviceModel: PPDeviceModel?){}

    /**
     * 阻抗测量状态，state 0 测量中  1 测量完成  2 测量失败
     */
    fun onImpedanceFatting() {}

    /**
     * 秤端计算的数据，在使用时要设置UserModel, 只在
     *
     * @param bodyFatModel
     */
    fun monitorLockDataByCalculateInScale(bodyFatModel: PPBodyFatModel?) {}

    /**
     * 超重
     */
    fun monitorOverWeight() {}

    /**
     * 设备关机回调，目前只在Torre设备上生效
     */
    fun onDeviceShutdown() {}

    /**
     * 历史数据发生改变,目前只在Torre设备上生效
     */
    fun onHistoryDataChange() {}
}
