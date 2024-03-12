package com.peng.ppscale.vo

import com.peng.ppscale.business.device.PPUnitType

open class PPBodyBaseModel {

    //体重 重量放大了100倍
    @JvmField
    var weight = 0

    //4电极算法阻抗
    @JvmField
    var impedance: Long = 0

    var zTwoLegsDeCode:Float = 0f//4电极脚对脚明文阻抗值(Ω)

//    //4电极脚对脚明文阻抗值(Ω), 范围200.0 ~ 1200.0
//    var ppZTwoLegs = 0

    //设备信息
    @JvmField
    var deviceModel: PPDeviceModel? = null

    //用户信息
    @JvmField
    var userModel: PPUserModel? = null

    //心率是否测量中
    @JvmField
    var isHeartRating = false

//    //本次测量是否结束
//    var isEnd = true

    //设备单位 重量单位 默认kg
    @JvmField
    var unit: PPUnitType? = null

    //心率
    @JvmField
    var heartRate = 0

    // 是否超载
    @JvmField
    var isOverload = false

    // 是否是正数
    @JvmField
    var isPlus = true

    //formate yyyy-MM-dd HH:mm:ss
    @JvmField
    var dateStr = ""

    //数据归属 torre协议用
    @JvmField
    var memberId = ""

    /****************************8电极阻抗*****************************************/

    //100KHz左手阻抗加密值(下位机上传值)
    @JvmField
    var z100KhzLeftArmEnCode: Long = 0

    //100KHz左腳阻抗加密值(下位机上传值)
    @JvmField
    var z100KhzLeftLegEnCode: Long = 0

    //100KHz右手阻抗加密值(下位机上传值)
    @JvmField
    var z100KhzRightArmEnCode: Long = 0

    //100KHz右腳阻抗加密值(下位机上传值)
    @JvmField
    var z100KhzRightLegEnCode: Long = 0

    //100KHz軀幹阻抗加密值(下位机上传值)
    @JvmField
    var z100KhzTrunkEnCode: Long = 0

    //20KHz左手阻抗加密值(下位机上传值)
    @JvmField
    var z20KhzLeftArmEnCode: Long = 0

    //20KHz左腳阻抗加密值(下位机上传值)
    @JvmField
    var z20KhzLeftLegEnCode: Long = 0

    //20KHz右手阻抗加密值(下位机上传值)
    @JvmField
    var z20KhzRightArmEnCode: Long = 0

    //20KHz右腳阻抗加密值(下位机上传值)
    @JvmField
    var z20KhzRightLegEnCode: Long = 0

    //20KHz軀幹阻抗加密值(下位机上传值)
    @JvmField
    var z20KhzTrunkEnCode: Long = 0

    var z100KhzLeftArmDeCode: Float = 0f // 100KHz左手阻抗解密值(下位机上传值)
    var z100KhzLeftLegDeCode: Float = 0f // 100KHz左腳阻抗解密值(下位机上传值)
    var z100KhzRightArmDeCode: Float = 0f // 100KHz右手阻抗解密值(下位机上传值)
    var z100KhzRightLegDeCode: Float = 0f // 100KHz右腳阻抗解密值(下位机上传值)
    var z100KhzTrunkDeCode: Float = 0f // 100KHz軀幹阻抗解密值(下位机上传值)
    var z20KhzLeftArmDeCode: Float = 0f // 20KHz左手阻抗解密值(下位机上传值)
    var z20KhzLeftLegDeCode: Float = 0f // 20KHz左腳阻抗解密值(下位机上传值)
    var z20KhzRightArmDeCode: Float = 0f // 20KHz右手阻抗解密值(下位机上传值)
    var z20KhzRightLegDeCode: Float = 0f // 20KHz右腳阻抗解密值(下位机上传值)
    var z20KhzTrunkDeCode: Float = 0f // 20KHz軀幹阻抗解密值(下位机上传值)

    fun getPpWeightKg(): Float {
        return this.weight / 100.0f;
    }

    fun resetBofyFat() {
        //右手
        z20KhzRightArmEnCode = 0
        z100KhzRightArmEnCode = 0
        //左手
        z20KhzLeftArmEnCode = 0
        z100KhzLeftArmEnCode = 0
        //躯干
        z20KhzTrunkEnCode = 0
        z100KhzTrunkEnCode = 0
        //右脚
        z20KhzRightLegEnCode = 0
        z100KhzRightLegEnCode = 0
        //左脚
        z20KhzLeftLegEnCode = 0
        z100KhzLeftLegEnCode = 0
        impedance = 0
        heartRate = 0
        zTwoLegsDeCode = 0f
        z100KhzLeftArmDeCode = 0f
        z100KhzLeftLegDeCode = 0f
        z100KhzRightArmDeCode = 0f
        z100KhzRightLegDeCode = 0f
        z100KhzTrunkDeCode = 0f
        z20KhzLeftArmDeCode = 0f
        z20KhzLeftLegDeCode = 0f
        z20KhzRightArmDeCode = 0f
        z20KhzRightLegDeCode = 0f
        z20KhzTrunkDeCode = 0f
    }

    override fun toString(): String {
        if (deviceModel?.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8
            || deviceModel?.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8_0
            || deviceModel?.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8_1
            || deviceModel?.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8_2
            || deviceModel?.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8_3
        ) {
            return "PPBodyBaseModel(weight=$weight," + "\n" +
                    "deviceName=${deviceModel?.deviceName}," + "\n" +
                    "deviceCalcuteType=${deviceModel?.deviceCalcuteType}," + "\n" +
                    "deviceAccuracyType=${deviceModel?.deviceAccuracyType}," + "\n" +
                    "deviceProtocolType=${deviceModel?.deviceProtocolType}," + "\n" +
                    "userModel=$userModel, " + "\n" +
                    "isHeartRating=$isHeartRating," + "\n" +
//                    "isEnd=$isEnd, " + "\n" +
                    "unit=$unit," + "\n" +
                    "heartRate=$heartRate," + "\n" +
                    "isOverload=$isOverload, " + "\n" +
                    "isPlus=$isPlus," + "\n" +
                    "dateStr='$dateStr'," + "\n" +
                    "memberId='$memberId'," + "\n" +
                    "z100KhzLeftArmEnCode=$z100KhzLeftArmEnCode," + "\n" +
                    "z100KhzLeftLegEnCode=$z100KhzLeftLegEnCode," + "\n" +
                    "z100KhzRightArmEnCode=$z100KhzRightArmEnCode," + "\n" +
                    "z100KhzRightLegEnCode=$z100KhzRightLegEnCode," + "\n" +
                    "z100KhzTrunkEnCode=$z100KhzTrunkEnCode," + "\n" +
                    "z20KhzLeftArmEnCode=$z20KhzLeftArmEnCode," + "\n" +
                    "z20KhzLeftLegEnCode=$z20KhzLeftLegEnCode," + "\n" +
                    "z20KhzRightArmEnCode=$z20KhzRightArmEnCode," + "\n" +
                    "z20KhzRightLegEnCode=$z20KhzRightLegEnCode," + "\n" +
                    "z20KhzTrunkEnCode=$z20KhzTrunkEnCode," + "\n" +
                    "z100KhzLeftArmDeCode=$z100KhzLeftArmDeCode," + "\n" +
                    "z100KhzLeftLegDeCode=$z100KhzLeftLegDeCode," + "\n" +
                    "z100KhzRightArmDeCode=$z100KhzRightArmDeCode," + "\n" +
                    "z100KhzRightLegDeCode=$z100KhzRightLegDeCode," + "\n" +
                    "z100KhzTrunkDeCode=$z100KhzTrunkDeCode," + "\n" +
                    "z20KhzLeftArmDeCode=$z20KhzLeftArmDeCode," + "\n" +
                    "z20KhzLeftLegDeCode=$z20KhzLeftLegDeCode," + "\n" +
                    "z20KhzRightArmDeCode=$z20KhzRightArmDeCode," + "\n" +
                    "z20KhzRightLegDeCode=$z20KhzRightLegDeCode," + "\n" +
                    "z20KhzTrunkDeCode=$z20KhzTrunkDeCode)"
        } else {
            return "PPBodyBaseModel(weight=$weight," + "\n" +
                    "impedance=$impedance, " + "\n" +
                    "zTwoLegsDeCode=$zTwoLegsDeCode, " + "\n" +
                    "deviceName=${deviceModel?.deviceName}," + "\n" +
                    "deviceCalcuteType=${deviceModel?.deviceCalcuteType}," + "\n" +
                    "deviceAccuracyType=${deviceModel?.deviceAccuracyType}," + "\n" +
                    "deviceProtocolType=${deviceModel?.deviceProtocolType}," + "\n" +
                    "userModel=$userModel, " + "\n" +
                    "isHeartRating=$isHeartRating," + "\n" +
//                    "isEnd=$isEnd, " + "\n" +
                    "unit=$unit," + "\n" +
                    "heartRate=$heartRate," + "\n" +
                    "isOverload=$isOverload, " + "\n" +
                    "isPlus=$isPlus," + "\n" +
                    "dateStr=$dateStr," + "\n" +
                    "memberId=$memberId)"
        }
    }
}
