package com.peng.ppscale.business.ble

import com.peng.ppscale.vo.PPDeviceModel
import com.peng.ppscale.vo.PPScaleDefine

object PPScaleHelper {


    /**
     * 是否支持历史数据
     *
     * @param device
     * @return
     */
    fun isSupportHistoryData(deviceFuncType: Int?): Boolean {
        return if (deviceFuncType != null) {
            (deviceFuncType and PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType()
                    == PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType())
        } else {
            false
        }
    }

    /**
     * 是否支持Wifi
     *
     * @param device
     * @return
     */
    fun isFuncTypeWifi(deviceFuncType: Int?): Boolean {
        return if (deviceFuncType != null) {
            (deviceFuncType and PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWifi.getType()
                    == PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWifi.getType())
        } else {
            false
        }
    }

    fun isFuncTypeTwoBrocast(deviceFuncType: Int?): Boolean {
        return if (deviceFuncType != null) {
            (deviceFuncType and PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeBidirectional.getType()
                    == PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeBidirectional.getType())
        } else {
            false
        }
    }

    /**
     * 是否支持Wifi
     *
     * @param device
     * @return
     */
    fun isFat(deviceFuncType: Int?): Boolean {
        return if (deviceFuncType != null) {
            (deviceFuncType and PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeFat.getType()
                    == PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeFat.getType())
        } else {
            false
        }
    }

}
