package com.peng.ppscale.calcute

import com.peng.ppscale.vo.PPBodyBaseModel
import com.peng.ppscale.vo.PPBodyFatModel
import com.peng.ppscale.vo.PPScaleDefine

object CalculateManager {

    fun calculateData(bodyBaseModel: PPBodyBaseModel, bodyFatModel: PPBodyFatModel) {
        val deviceModel = bodyBaseModel.deviceModel
        if (deviceModel != null && deviceModel.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate4_0) {
            //4电极双脚新算法
            CalculateHelper4Leg2.calcuteTypeAlternateTwoLegs2(bodyBaseModel, bodyFatModel)
        } else if (deviceModel != null && deviceModel.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate4_1) {
            //4电极双频算法
            CalculateHelper4TwoChannel.calcuteTypeAlternate4TwoChannel(bodyBaseModel, bodyFatModel)
        } else if (deviceModel != null && deviceModel.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8_0) {
            //8电极算法 bhProduct = 4
            CalculateHelper8.calcuteTypeAlternate8(bodyFatModel, 4)
        } else if (deviceModel != null && deviceModel.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8_1) {
            //8电极算法 bhProduct = 3
            CalculateHelper8.calcuteTypeAlternate8(bodyFatModel, 3)
        } else if (deviceModel != null && deviceModel.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8_2) {
            //8电极算法 bhProduct = 0
            CalculateHelper8.calcuteTypeAlternate8(bodyFatModel, 0)
        } else if (deviceModel != null && deviceModel.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8_3) {
            //8电极算法 bhProduct =5 --CF577_N1
            CalculateHelper8.calcuteTypeAlternate8(bodyFatModel, 5)
        } else if (deviceModel != null && deviceModel.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8) {
            if (bodyBaseModel.z20KhzRightArmEnCode > 0 && bodyBaseModel.z100KhzRightArmEnCode <= 0) {
                bodyBaseModel.impedance = bodyBaseModel.z20KhzRightArmEnCode
                //4电极双手算法
                CalculateHelper8.calcuteTypeAlternateTwoArms(bodyBaseModel, bodyFatModel)
            } else {
                //8电极算法 bhProduct = 1
                CalculateHelper8.calcuteTypeAlternate8(bodyFatModel, 1)
            }
        } else {
            //4电极双脚阻抗
            CalculateHelper4.calcuteTypeAlternateTwoLegs(bodyBaseModel, bodyFatModel)
        }
    }


}
