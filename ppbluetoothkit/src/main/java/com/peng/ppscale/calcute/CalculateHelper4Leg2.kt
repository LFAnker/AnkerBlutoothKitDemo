package com.peng.ppscale.calcute

import com.besthealth.bh1BodyComposition.BhTwoLegs140
import com.besthealth.bhBodyComposition.BhErrorType
import com.besthealth.bhBodyComposition.BhPeopleType
import com.besthealth.bhBodyComposition.BhSex
import com.peng.ppscale.data.PPBodyDetailModel
import com.peng.ppscale.data.PPBodyDetailStandard
import com.peng.ppscale.data.createList
import com.peng.ppscale.data.fetchPPBodyParam_BMI_StandartArray
import com.peng.ppscale.data.fetchPPBodyParam_BodyScore_StandartArray
import com.peng.ppscale.data.fetchPPBodyParam_VisFat_StandartArray
import com.peng.ppscale.data.fetchPPBodyParam_Weight_StandartArray
import com.peng.ppscale.data.fetchPPBodyParam_Weight_StandartArray_4
import com.peng.ppscale.data.fetchPPBodyParam_heart_StandartArray
import com.peng.ppscale.data.fetchPPBodyParam_physicalAgeValue_StandartArray
import com.peng.ppscale.vo.BodyFatCalcuteHelper
import com.peng.ppscale.vo.BodyFatErrorType
import com.peng.ppscale.vo.HT_4_LEG2_
import com.peng.ppscale.vo.PPBodyBaseModel
import com.peng.ppscale.vo.PPBodyFatModel
import com.peng.ppscale.vo.PPScaleDefine
import com.peng.ppscale.vo.PPUserGender

object CalculateHelper4Leg2 {

    /**
     * 4电极双脚阻抗-新
     *
     * @param bodyBaseModel
     * @param bodyFatModel
     */
    fun calcuteTypeAlternateTwoLegs2(bodyBaseModel: PPBodyBaseModel, bodyFatModel: PPBodyFatModel) {
        val body = BhTwoLegs140()
        bodyFatModel.ppSDKVersion = HT_4_LEG2_ + body.getSDKVersion()
        body.bhAge = bodyFatModel.ppAge
        body.bhHeightCm = bodyFatModel.ppHeightCm.toFloat()
        body.bhWeightKg = bodyFatModel.ppWeightKg
        body.bhSex = if (bodyFatModel.ppSex == PPUserGender.PPUserGenderMale) BhSex.MALE.ordinal else BhSex.FEMALE.ordinal
        body.bhPeopleType = if (bodyBaseModel.userModel?.isAthleteMode ?: false) BhPeopleType.ATHLETE.ordinal else BhPeopleType.NORMAL.ordinal
        body.bhZTwoLegsEnCode = bodyBaseModel.impedance
        val bhErrorType: BhErrorType = BhErrorType.values().get(body.getBodyComposition())
        println("impedance：" + bodyBaseModel.impedance)
        println("错误信息：$bhErrorType")
        System.out.println(body.getSDKVersion())
        bodyFatModel.errorType = BodyFatCalcuteHelper.calculateHTErrorType(bhErrorType)
        if (bodyFatModel.errorType == BodyFatErrorType.PP_ERROR_TYPE_NONE) {
            System.out.println("體重(Kg)=" + body.bhWeightKg)
            System.out.println("身高(cm)=" + body.bhHeightCm)
            System.out.println("年齡(歲)=" + body.bhAge)
            System.out.println("性別=" + BhSex.values().get(body.bhSex))
            System.out.println("用戶類型=" + body.bhPeopleType)
            System.out.println("加密阻抗-雙腳=" + body.bhZTwoLegsEnCode)
            System.out.println("解密阻抗-雙腳(Ω)=" + body.bhZTwoLegsDeCode)
            bodyBaseModel.zTwoLegsDeCode = body.bhZTwoLegsDeCode
            bodyFatModel.ppProteinPercentage = body.bhProteinRate
            bodyFatModel.ppIdealWeightKg = body.bhIdealWeightKg
            bodyFatModel.ppBMI = if (body.bhBMI >= 10) body.bhBMI else 10.0f
            bodyFatModel.ppBMR = body.bhBMR
            bodyFatModel.ppVisceralFat = body.bhVFAL
            bodyFatModel.ppBoneKg = body.bhBoneKg
            bodyFatModel.ppFat = BodyFatCalcuteHelper.filterBodyfatPercentage(
                body.bhBodyFatRate,
                bodyFatModel.ppSex,
                bodyBaseModel.deviceModel?.deviceCalcuteType ?: PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate
            )
            bodyFatModel.ppWaterPercentage = body.bhWaterRate
            bodyFatModel.ppMuscleKg = body.bhMuscleKg
            bodyFatModel.ppBodyScore = body.bhBodyScore
            bodyFatModel.ppMusclePercentage = body.bhMuscleRate
            bodyFatModel.ppBodySkeletalKg = body.bhSkeletalMuscleKg
            bodyFatModel.ppBodySkeletal = bodyFatModel.ppBodySkeletalKg / bodyFatModel.ppWeightKg * 100f
            bodyFatModel.ppBodyfatKg = bodyFatModel.ppFat * 0.01f * bodyFatModel.ppWeightKg
            val bhMuscleKgList = PPBodyDetailStandard.createList(
                body.bhMuscleKgListUnderOrStandard,
                body.bhMuscleKgListStandardOrExcellent
            )
            bodyFatModel.ppMuscleKgList = PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray_kg_4_A(PPBodyDetailStandard.createList(body.bhMuscleKgListUnderOrStandard, body.bhMuscleKgListStandardOrExcellent),true)
            bodyFatModel.ppBodyType = BodyFatCalcuteHelper.bodyDetailTypeWithFatRate(bodyFatModel)
//            bodyFatModel.ppBodyStandardWeightKg = CalculateHelper.calculateStandardWeightWithHeight(bodyFatModel.ppHeightCm)
            bodyFatModel.ppBodyStandardWeightKg = body.bhIdealWeightKg
            bodyFatModel.ppIdealWeightKg = body.bhIdealWeightKg
//            bodyFatModel.ppLoseFatWeightKg = body.bhBodyFatFreeMassKg
            bodyFatModel.ppLoseFatWeightKg = bodyFatModel.ppWeightKg - bodyFatModel.ppBodyfatKg
            bodyFatModel.ppControlWeightKg = CalculateUtil.calculateControlWeightKg(bodyFatModel.ppWeightKg, body.bhIdealWeightKg)
            bodyFatModel.ppFatControlKg = CalculateUtil.ppFatControlKgWithBodyfatKg(bodyFatModel.ppBodyfatKg, bodyFatModel.ppSex, bodyFatModel.ppBMI, bodyFatModel.ppAge)
            bodyFatModel.ppBodyMuscleControl = CalculateUtil.calculateBodyMuscleControl(bodyFatModel.ppSex, bodyFatModel.ppHeightCm, bodyFatModel.ppMuscleKg, body.bhIdealWeightKg)
            bodyFatModel.ppBodyFatSubCutPercentage = bodyFatModel.ppFat * 2.0f / 3.0f
            bodyFatModel.ppFatGrade = BodyFatCalcuteHelper.bodyFatGrade(bodyFatModel.ppBMI)
            bodyFatModel.ppBodyHealth = BodyFatCalcuteHelper.healthScore(bodyFatModel.ppBodyScore)
            bodyFatModel.ppBodyAge = CalculateUtil.calcuteBodyAge(bodyFatModel.ppBMI, bodyFatModel.ppAge)

//            bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray()
//            bodyFatModel.ppMusclePercentageList =
//                PPBodyDetailStandard.kg2percent(PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray(body.bhMuscleKgListUnderOrStandard, body.bhMuscleKgListStandardOrExcellent))
//            bodyFatModel.ppMuscleKgList = PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray(body.bhMuscleKgListUnderOrStandard, body.bhMuscleKgListStandardOrExcellent)
//            bodyFatModel.ppWaterPercentageList = PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray(body.bhWaterRateListUnderOrStandard, body.bhWaterRateListStandardOrExcellent)
//            bodyFatModel.ppWaterKgList =
//                PPBodyDetailStandard.percent2kg(PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray(body.bhWaterRateListUnderOrStandard, body.bhWaterRateListStandardOrExcellent))
//            bodyFatModel.ppBoneKgList = PPBodyDetailStandard.fetchPPBodyParam_Bone_StandartArray(body.bhBoneKgListUnderOrStandard, body.bhBoneKgListStandardOrExcellent)

            bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray()
            bodyFatModel.ppMusclePercentageList = PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray_kg_4_A(PPBodyDetailStandard.createList(body.bhMuscleKgListUnderOrStandard, body.bhMuscleKgListStandardOrExcellent),false)
            bodyFatModel.ppWaterPercentageList = PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray_percent_4_A(PPBodyDetailStandard.createList(body.bhWaterRateListUnderOrStandard, body.bhWaterRateListStandardOrExcellent),false)
            bodyFatModel.ppWaterKgList = PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray_percent_4_A(PPBodyDetailStandard.createList(body.bhWaterRateListUnderOrStandard, body.bhWaterRateListStandardOrExcellent),true)
            bodyFatModel.ppBoneKgList = PPBodyDetailStandard.fetchPPBodyParam_Bone_StandartArray_kg_4_A(PPBodyDetailStandard.createList(body.bhBoneKgListUnderOrStandard, body.bhBoneKgListStandardOrExcellent))
            
            calculateList(bodyFatModel, body)
        } else {
            PPBodyDetailStandard.initWithBodyFatModel(bodyFatModel)
            bodyFatModel.ppBMI = CalculateUtil.calculateBMI(bodyFatModel.ppHeightCm, bodyFatModel.ppWeightKg)
            bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray()
            bodyFatModel.ppWeightKgList = PPBodyDetailStandard.fetchPPBodyParam_Weight_StandartArray()
        }
        bodyFatModel.bodyDetailModel = PPBodyDetailModel(bodyFatModel)
    }

    /**
     * 4电极双脚
     */
    fun calculateList(bodyFatModel: PPBodyFatModel, twoLegs140: BhTwoLegs140) {
        PPBodyDetailStandard.initWithBodyFatModel(bodyFatModel)
        val ppBodyfatRateCreateList = PPBodyDetailStandard.createList(
            twoLegs140.bhBodyFatRateListUnderFatOrStandardMinus,
            twoLegs140.bhBodyFatRateListStandardMinusOrStandardPlus,
            twoLegs140.bhBodyFatRateListStandardPlusOrOverFat,
            twoLegs140.bhBodyFatRateListOverFatOrObese
        )
        val bhSkeletalMuscleKgList = PPBodyDetailStandard.createList(
            twoLegs140.bhSkeletalMuscleKgListUnderOrStandard,
            twoLegs140.bhSkeletalMuscleKgListStandardOrExcellent
        )
        val bhProteinRateList = PPBodyDetailStandard.createList(
            twoLegs140.bhProteinRateListUnderOrStandard,
            twoLegs140.bhProteinRateListStandardOrExcellent
        )
        val bhBodyFatSubCutRateList = PPBodyDetailStandard.createList(
            twoLegs140.bhBodyFatSubCutRateListUnderOrStandard,
            twoLegs140.bhBodyFatSubCutRateListStandardOrOver
        )
        val bhBMRList =
            PPBodyDetailStandard.createList(twoLegs140.bhBMRListUnderOrStandard.toFloat())
        bodyFatModel.ppWeightKgList = PPBodyDetailStandard.fetchPPBodyParam_Weight_StandartArray_4()
        bodyFatModel.ppBodyfatKgList =
            PPBodyDetailStandard.fetchPPBodyParam_BodyFat_StandartArray_percent_4_A(
                ppBodyfatRateCreateList,
                true
            )
        bodyFatModel.ppFatList =
            PPBodyDetailStandard.fetchPPBodyParam_BodyFat_StandartArray_percent_4_A(
                ppBodyfatRateCreateList,
                false
            )
        bodyFatModel.ppBodySkeletalList =
            PPBodyDetailStandard.fetchPPBodyParam_BodySkeletal_StandartArray_kg_4_A(
                bhSkeletalMuscleKgList,
                false
            )
        bodyFatModel.ppBodySkeletalKgList =
            PPBodyDetailStandard.fetchPPBodyParam_BodySkeletal_StandartArray_kg_4_A(
                bhSkeletalMuscleKgList,
                true
            )
        bodyFatModel.ppProteinPercentageList =
            PPBodyDetailStandard.fetchPPBodyParam_Protein_StandartArray_percent_4_A(
                bhProteinRateList,
                false
            )
        bodyFatModel.ppProteinKgList =
            PPBodyDetailStandard.fetchPPBodyParam_Protein_StandartArray_percent_4_A(
                bhProteinRateList,
                true
            )
        bodyFatModel.ppBodyFatSubCutPercentageList =
            PPBodyDetailStandard.fetchPPBodyParam_BodySubcutaneousFat_StandartArray_percent_4_A(
                bhBodyFatSubCutRateList,
                false
            )
        bodyFatModel.ppBodyFatSubCutKgList =
            PPBodyDetailStandard.fetchPPBodyParam_BodySubcutaneousFat_StandartArray_percent_4_A(
                bhBodyFatSubCutRateList,
                true
            )
        bodyFatModel.ppHeartRateList = PPBodyDetailStandard.fetchPPBodyParam_heart_StandartArray()
        bodyFatModel.ppBMRList =
            PPBodyDetailStandard.fetchPPBodyParam_BMR_StandartArray_kcal_4_A(bhBMRList)
        bodyFatModel.ppVisceralFatList =
            PPBodyDetailStandard.fetchPPBodyParam_VisFat_StandartArray()
        bodyFatModel.ppBodyHealthList = listOf()
        bodyFatModel.ppBodyAgeList =
            PPBodyDetailStandard.fetchPPBodyParam_physicalAgeValue_StandartArray()
        bodyFatModel.ppBodyScoreList =
            PPBodyDetailStandard.fetchPPBodyParam_BodyScore_StandartArray()
        bodyFatModel.ppFatGradeList = listOf()
    }

}
