package com.peng.ppscale.vo

import com.besthealth.bhBodyComposition.BhErrorType
import com.peng.ppscale.data.PPBodyDetailStandard
import com.peng.ppscale.data.fetchPPBodyParam_BodyFat_StandartArray
import com.peng.ppscale.data.fetchPPBodyParam_Mus_StandartArray
import com.peng.ppscale.util.Logger
import com.peng.ppscale.util.UserUtil

object BodyFatCalcuteHelper {

    //肥胖等级
    @JvmStatic
    fun bodyFatGrade(bmi: Float): PPBodyFatGrade {
        return when {
            bmi <= 18.5 -> PPBodyFatGrade.PPBodyGradeFatThin
            bmi <= 25 -> PPBodyFatGrade.PPBodyGradeFatStandard
            bmi <= 30 -> PPBodyFatGrade.PPBodyGradeFatOverwight
            bmi < 35 -> PPBodyFatGrade.PPBodyGradeFatOne
            bmi <= 40 -> PPBodyFatGrade.PPBodyGradeFatTwo
            else -> PPBodyFatGrade.PPBodyGradeFatThree
        }
    }

    //健康评估
    @JvmStatic
    fun healthScore(htBodyScore: Int): PPBodyHealthAssessment {
        return when {
            htBodyScore <= 60 -> PPBodyHealthAssessment.PPBodyAssessment1
            htBodyScore <= 70 -> PPBodyHealthAssessment.PPBodyAssessment2
            htBodyScore <= 80 -> PPBodyHealthAssessment.PPBodyAssessment3
            htBodyScore <= 90 -> PPBodyHealthAssessment.PPBodyAssessment4
            else -> PPBodyHealthAssessment.PPBodyAssessment5
        }
    }

    /**
     * 处理体脂率 新的计算公式不再做此处理
     *
     * @param ppBodyfatPercentage
     * @return
     */
    @JvmStatic
    fun filterBodyfatPercentage(ppBodyfatPercentage: Float, userGender: PPUserGender, deviceCalcuteType: PPScaleDefine.PPDeviceCalcuteType): Float {
        var fatFix = 0
        return if (ppBodyfatPercentage <= 0) {
            0.0f
        } else {
            Logger.d("filterBodyfatPercentage deviceCalcuteType: " + deviceCalcuteType.getType())
            if (deviceCalcuteType.getType() == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeNormal.getType()
                || deviceCalcuteType.getType() == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate4_1.getType()) {
                fatFix = 0
            } else {
                fatFix = if (UserUtil.getEnumSex(userGender) == 0) {
                    if (ppBodyfatPercentage < 14) {
                        0
                    } else if (ppBodyfatPercentage < 21) {
                        1
                    } else if (ppBodyfatPercentage < 25) {
                        2
                    } else if (ppBodyfatPercentage < 32) {
                        3
                    } else {
                        4
                    }
                } else {
                    if (ppBodyfatPercentage < 6) {
                        0
                    } else if (ppBodyfatPercentage < 14) {
                        1
                    } else if (ppBodyfatPercentage < 18) {
                        2
                    } else if (ppBodyfatPercentage < 26) {
                        3
                    } else {
                        4
                    }
                }
            }
//            if (deviceModel != null && !TextUtils.isEmpty(deviceModel.getDeviceName()) &&
//                    DeviceManager.DeviceList.DeviceListCalcuteInScale.contains(deviceModel.getDeviceName())) {
//                return ppBodyfatPercentage;
//            } else {
            val subValue = ppBodyfatPercentage - fatFix
            Logger.d("filterBodyfatPercentage fatFix : " + fatFix + " subValue: " + subValue)
            Math.max(subValue, 5.0f)
            //            }
        }
    }

    @JvmStatic
    fun calculateHTErrorType(bhErrorType: BhErrorType): BodyFatErrorType {
        //    NONE,
        //    AGE,
        //    HEIGHT,
        //    WEIGHT,
        //    SEX,
        //    PEOPLE_TYPE,
        //    IMPEDANCE_TWO_LEGS,
        //    IMPEDANCE_TWO_ARMS,
        //    IMPEDANCE_LEFT_BODY,
        //    IMPEDANCE_LEFT_ARM,
        //    IMPEDANCE_RIGHT_ARM,
        //    IMPEDANCE_LEFT_LEG,
        //    IMPEDANCE_RIGHT_LEG,
        //    IMPEDANCE_TRUNK;
        var ppBodyfatErrorType: BodyFatErrorType = BodyFatErrorType.PP_ERROR_TYPE_NONE
        when {
            bhErrorType == BhErrorType.NONE -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_NONE
            }
            bhErrorType == BhErrorType.AGE -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_AGE
            }
            bhErrorType == BhErrorType.HEIGHT -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_HEIGHT
            }
            bhErrorType == BhErrorType.WEIGHT -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_WEIGHT
            }
            bhErrorType == BhErrorType.SEX -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_SEX
            }
            bhErrorType == BhErrorType.PEOPLE_TYPE -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_PEOPLE_TYPE
            }
            bhErrorType == BhErrorType.IMPEDANCE_TWO_LEGS -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_TWO_LEGS
            }
            bhErrorType == BhErrorType.IMPEDANCE_TWO_ARMS -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_TWO_ARMS
            }
            bhErrorType == BhErrorType.IMPEDANCE_LEFT_BODY -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_LEFT_BODY
            }
            bhErrorType == BhErrorType.IMPEDANCE_LEFT_ARM -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_LEFT_ARM
            }
            bhErrorType == BhErrorType.IMPEDANCE_RIGHT_ARM -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_RIGHT_ARM
            }
            bhErrorType == BhErrorType.IMPEDANCE_LEFT_LEG -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_LEFT_LEG
            }
            bhErrorType == BhErrorType.IMPEDANCE_RIGHT_LEG -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_RIGHT_LEG
            }
            bhErrorType == BhErrorType.IMPEDANCE_TRUNK -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_TRUNK
            }
        }
        return ppBodyfatErrorType
    }

    @JvmStatic
    fun calculateHTErrorType(bhErrorType: com.besthealth.bh1BodyComposition.BhErrorType): BodyFatErrorType {
        //    NONE,
        //    AGE,
        //    HEIGHT,
        //    WEIGHT,
        //    SEX,
        //    PEOPLE_TYPE,
        //    IMPEDANCE_TWO_LEGS,
        //    IMPEDANCE_TWO_ARMS,
        //    IMPEDANCE_LEFT_BODY,
        //    IMPEDANCE_LEFT_ARM,
        //    IMPEDANCE_RIGHT_ARM,
        //    IMPEDANCE_LEFT_LEG,
        //    IMPEDANCE_RIGHT_LEG,
        //    IMPEDANCE_TRUNK;
        var ppBodyfatErrorType: BodyFatErrorType = BodyFatErrorType.PP_ERROR_TYPE_NONE
        when {
            bhErrorType.ordinal == BhErrorType.NONE.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_NONE
            }
            bhErrorType.ordinal == BhErrorType.AGE.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_AGE
            }
            bhErrorType.ordinal == BhErrorType.HEIGHT.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_HEIGHT
            }
            bhErrorType.ordinal == BhErrorType.WEIGHT.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_WEIGHT
            }
            bhErrorType.ordinal == BhErrorType.SEX.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_SEX
            }
            bhErrorType.ordinal == BhErrorType.PEOPLE_TYPE.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_PEOPLE_TYPE
            }
            bhErrorType.ordinal == BhErrorType.IMPEDANCE_TWO_LEGS.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_TWO_LEGS
            }
            bhErrorType.ordinal == BhErrorType.IMPEDANCE_TWO_ARMS.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_TWO_ARMS
            }
            bhErrorType.ordinal == BhErrorType.IMPEDANCE_LEFT_BODY.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_LEFT_BODY
            }
            bhErrorType.ordinal == BhErrorType.IMPEDANCE_LEFT_ARM.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_LEFT_ARM
            }
            bhErrorType.ordinal == BhErrorType.IMPEDANCE_RIGHT_ARM.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_RIGHT_ARM
            }
            bhErrorType.ordinal == BhErrorType.IMPEDANCE_LEFT_LEG.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_LEFT_LEG
            }
            bhErrorType.ordinal == BhErrorType.IMPEDANCE_RIGHT_LEG.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_RIGHT_LEG
            }
            bhErrorType.ordinal == BhErrorType.IMPEDANCE_TRUNK.ordinal -> {
                ppBodyfatErrorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_TRUNK
            }
        }
        return ppBodyfatErrorType
    }

    @JvmStatic
    fun bodyDetailTypeWithFatRate(fatModel: PPBodyFatModel): PPBodyDetailType {
        PPBodyDetailStandard.initWithBodyFatModel(fatModel)
        val fat: Float = fatModel.ppFat
        val mus: Float = fatModel.ppMuscleKg
        val standardMus1 = PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray()
        val standardMus = fatModel.ppMuscleKgList
        val standardFat = PPBodyDetailStandard.fetchPPBodyParam_BodyFat_StandartArray()
        val xValue = calculateCurrentStandardWithValue(mus, standardArray = standardMus ?: standardMus1)
        val yValue = calculateCurrentStandardWithValue(fat, standardArray = standardFat)

        return when {
            xValue == 0 && yValue == 0 -> PPBodyDetailType.LF_BODY_TYPE_THIN
            xValue == 0 && (yValue == 1 || yValue == 2) -> PPBodyDetailType.LF_BODY_TYPE_LACK_EXERCISE
            xValue == 0 && (yValue == 3 || yValue == 4) -> PPBodyDetailType.LF_BODY_TYPE_OBESE_FAT
            xValue == 1 && yValue == 0 -> PPBodyDetailType.LF_BODY_TYPE_THIN_MUSCLE
            xValue == 1 && (yValue == 1 || yValue == 2) -> PPBodyDetailType.LF_BODY_TYPE_STANDARD
            xValue == 1 && (yValue == 3 || yValue == 4) -> PPBodyDetailType.LF_BODY_TYPE_FAT_MUSCLE
            xValue == 2 && yValue == 0 -> PPBodyDetailType.LF_BODY_TYPE_MUSCULAR
            xValue == 2 && (yValue == 1 || yValue == 2) -> PPBodyDetailType.LF_BODY_TYPE_STANDARD_MUSCLE
            xValue == 2 && (yValue == 3 || yValue == 4) -> PPBodyDetailType.LF_BODY_TYPE_MUSCLE_FAT
            else -> PPBodyDetailType.LF_BODY_TYPE_STANDARD
        }
    }

    @JvmStatic
    fun bodyDetailTypeWithFatRate8(fatModel: PPBodyFatModel): PPBodyDetailType {
        PPBodyDetailStandard.initWithBodyFatModel(fatModel)
        val fat: Float = fatModel.ppFat
        val mus: Float = fatModel.ppMuscleKg
        val standardMus1 = PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray()
        val standardMus = fatModel.ppMuscleKgList
        val standardFat = PPBodyDetailStandard.fetchPPBodyParam_BodyFat_StandartArray()
        val xValue = calculateCurrentStandardWithValue(mus, standardArray = standardMus ?: standardMus1)
        val yValue = calculateCurrentStandardWithValue(fat, standardArray = standardFat)

        return when {
            xValue == 0 && yValue == 0 -> PPBodyDetailType.LF_BODY_TYPE_THIN
            xValue == 0 && yValue == 1 -> PPBodyDetailType.LF_BODY_TYPE_LACK_EXERCISE
            xValue == 0 && yValue == 2 -> PPBodyDetailType.LF_BODY_TYPE_OBESE_FAT
            xValue == 1 && yValue == 0 -> PPBodyDetailType.LF_BODY_TYPE_THIN_MUSCLE
            xValue == 1 && yValue == 1 -> PPBodyDetailType.LF_BODY_TYPE_STANDARD
            xValue == 1 && yValue == 2 -> PPBodyDetailType.LF_BODY_TYPE_FAT_MUSCLE
            xValue == 2 && yValue == 0 -> PPBodyDetailType.LF_BODY_TYPE_MUSCULAR
            xValue == 2 && yValue == 1 -> PPBodyDetailType.LF_BODY_TYPE_STANDARD_MUSCLE
            xValue == 2 && yValue == 2 -> PPBodyDetailType.LF_BODY_TYPE_MUSCLE_FAT
            else -> PPBodyDetailType.LF_BODY_TYPE_STANDARD
        }
    }

    @JvmStatic
    fun countBodyType(bhBodyType: Int): PPBodyDetailType {
        if (bhBodyType == HTBodyType.LF_BODY_TYPE_THIN.getType()) { //偏瘦型
            return PPBodyDetailType.LF_BODY_TYPE_THIN
        } else if (bhBodyType == HTBodyType.LF_BODY_TYPE_THIN_MUSCLE.getType()) { //偏瘦肌肉型
            return PPBodyDetailType.LF_BODY_TYPE_THIN_MUSCLE
        } else if (bhBodyType == HTBodyType.LF_BODY_TYPE_MUSCULAR.getType()) { //肌肉发达型
            return PPBodyDetailType.LF_BODY_TYPE_MUSCULAR
        } else if (bhBodyType == HTBodyType.LF_BODY_TYPE_OBESE_FAT.getType()) { //浮肿肥胖型
            return PPBodyDetailType.LF_BODY_TYPE_OBESE_FAT
        } else if (bhBodyType == HTBodyType.LF_BODY_TYPE_FAT_MUSCLE.getType()) { //偏胖肌肉型
            return PPBodyDetailType.LF_BODY_TYPE_FAT_MUSCLE
        } else if (bhBodyType == HTBodyType.LF_BODY_TYPE_MUSCLE_FAT.getType()) { //肌肉型偏胖
            return PPBodyDetailType.LF_BODY_TYPE_MUSCLE_FAT
        } else if (bhBodyType == HTBodyType.LF_BODY_TYPE_LACK_EXERCISE.getType()) { //缺乏运动型
            return PPBodyDetailType.LF_BODY_TYPE_LACK_EXERCISE
        } else if (bhBodyType == HTBodyType.LF_BODY_TYPE_STANDARD.getType()) { //标准型
            return PPBodyDetailType.LF_BODY_TYPE_STANDARD
        } else if (bhBodyType == HTBodyType.LF_BODY_TYPE_STANDARD_MUSCLE.getType()) { //标准肌肉型
            return PPBodyDetailType.LF_BODY_TYPE_STANDARD_MUSCLE
        }
        return PPBodyDetailType.LF_BODY_TYPE_THIN
    }

    fun calculateCurrentStandardWithValue(number: Float, standardArray: List<Float>): Int {
        val count = standardArray.size
        val first = standardArray.first()
        if (number < first) {
            return 0
        }
        val last = standardArray.last()
        if (number >= last) {
            return count - 2
        }
        for (i in 1 until count) {
            val prev = standardArray[i - 1]
            val current = standardArray[i]
            if (number >= prev && number < current) {
                return i - 1
            }
        }
        return 0
    }

}
