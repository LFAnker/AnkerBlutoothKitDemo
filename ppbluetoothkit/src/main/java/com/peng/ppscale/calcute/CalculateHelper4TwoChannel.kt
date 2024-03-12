package com.peng.ppscale.calcute

import com.besthealth.bh1BodyComposition.BhTwoLegs240
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
import com.peng.ppscale.vo.HT_4_2Channel_
import com.peng.ppscale.vo.PPBodyBaseModel
import com.peng.ppscale.vo.PPBodyFatModel
import com.peng.ppscale.vo.PPScaleDefine
import com.peng.ppscale.vo.PPUserGender

object CalculateHelper4TwoChannel {

    /**
     * 4电极双频
     *
     * @param bodyBaseModel
     * @param bodyFatModel
     */
    fun calcuteTypeAlternate4TwoChannel(bodyBaseModel: PPBodyBaseModel, bodyFatModel: PPBodyFatModel) {
        val body = BhTwoLegs240()
        bodyFatModel.ppSDKVersion = HT_4_2Channel_ + body.getSDKVersion()
        body.bhAge = bodyFatModel.ppAge
        body.bhHeightCm = bodyFatModel.ppHeightCm.toFloat()
        body.bhWeightKg = bodyFatModel.ppWeightKg
        body.bhSex = if (bodyFatModel.ppSex == PPUserGender.PPUserGenderMale) BhSex.MALE.ordinal else BhSex.FEMALE.ordinal
        body.bhPeopleType = if (bodyBaseModel.userModel?.isAthleteMode ?: false) BhPeopleType.ATHLETE.ordinal else BhPeopleType.NORMAL.ordinal
        body.bhZTwoLegsEnCode50Khz = bodyBaseModel.impedance
        body.bhZTwoLegsEnCode100Khz = bodyBaseModel.impedance
        val bhErrorType: BhErrorType = BhErrorType.values().get(body.getBodyComposition())
        println("impedance：" + bodyBaseModel.impedance)
        println("错误信息：$bhErrorType")
        System.out.println(body.getSDKVersion())
        bodyFatModel.errorType = BodyFatCalcuteHelper.calculateHTErrorType(bhErrorType)
        System.out.println("體重(Kg)=" + body.bhWeightKg)
        System.out.println("身高(cm)=" + body.bhHeightCm)
        System.out.println("年齡(歲)=" + body.bhAge)
        System.out.println("性別=" + BhSex.values().get(body.bhSex))
        System.out.println("用戶類型=" + body.bhPeopleType)
        System.out.println("加密阻抗-bhZTwoLegsEnCode50Khz=" + body.bhZTwoLegsEnCode50Khz)
        System.out.println("加密阻抗-bhZTwoLegsEnCode100Khz=" + body.bhZTwoLegsEnCode100Khz)
        System.out.println("解密阻抗-bhZTwoLegsDeCode50Khz(Ω)=" + body.bhZTwoLegsDeCode50Khz)
        System.out.println("解密阻抗-bhZTwoLegsDeCode100Khz(Ω)=" + body.bhZTwoLegsDeCode100Khz)
        System.out.println("bodyFatModel.errorType=" + bodyFatModel.errorType)
        if (bodyFatModel.errorType == BodyFatErrorType.PP_ERROR_TYPE_NONE) {
            System.out.println("全身体组成参数")
            System.out.println("bhWaterRate=" + body.bhWaterRate)
            System.out.println("身體類型=" + body.bhBodyType)
            System.out.println("脂肪率=" + body.bhBodyFatRate)
            System.out.println("bhBMI=" + body.bhBMI)
            System.out.println("bhBodyFatSubCutRate=" + body.bhBodyFatSubCutRate)
            System.out.println("bhMuscleKg=" + body.bhMuscleKg)
            System.out.println("bhBoneKg=" + body.bhBoneKg)
            System.out.println("bhBodyFatKg=" + body.bhBodyFatKg)
            System.out.println("bhBodyFatFreeMassKg=" + body.bhBodyFatFreeMassKg)
            System.out.println("bhMuscleRate=" + body.bhMuscleRate)
            System.out.println("bhSkeletalMuscleKg=" + body.bhSkeletalMuscleKg)
            System.out.println("bhBodyScore=" + body.bhBodyScore)
            System.out.println("bhVFAL=" + body.bhVFAL)
            System.out.println("bhIdealWeightKg=" + body.bhIdealWeightKg)
            System.out.println("bhProteinRate=" + body.bhProteinRate)
            System.out.println("bhSkeletalMuscleKg=" + body.bhSkeletalMuscleKg)
            System.out.println("bhBodyAge=" + body.bhBodyAge)

            bodyBaseModel.zTwoLegsDeCode = body.bhZTwoLegsDeCode50Khz
            bodyFatModel.ppProteinPercentage = body.bhProteinRate
            bodyFatModel.ppIdealWeightKg = body.bhIdealWeightKg
            bodyFatModel.ppBMI = if (body.bhBMI >= 10) body.bhBMI else 10.0f
            bodyFatModel.ppBMR = body.bhBMR
            bodyFatModel.ppVisceralFat = body.bhVFAL
            bodyFatModel.ppBoneKg = body.bhBoneKg
            bodyFatModel.ppFat = BodyFatCalcuteHelper.filterBodyfatPercentage(
                body.bhBodyFatRate, bodyFatModel.ppSex,
                bodyBaseModel.deviceModel?.deviceCalcuteType ?: PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate4_1
            )
            bodyFatModel.ppWaterPercentage = body.bhWaterRate
            bodyFatModel.ppMuscleKg = body.bhMuscleKg
            bodyFatModel.ppBodyScore = body.bhBodyScore
            bodyFatModel.ppMusclePercentage = body.bhMuscleRate
            bodyFatModel.ppBodySkeletalKg = body.bhSkeletalMuscleKg
            bodyFatModel.ppBodySkeletal = bodyFatModel.ppBodySkeletalKg / bodyFatModel.ppWeightKg * 100f
            bodyFatModel.ppBodyfatKg = bodyFatModel.ppFat * 0.01f * bodyFatModel.ppWeightKg
            bodyFatModel.ppMuscleKgList =
                PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray_kg_4_A(PPBodyDetailStandard.createList(body.bhMuscleKgListUnderOrStandard, body.bhMuscleKgListStandardOrExcellent), true)
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
            bodyFatModel.ppBodyAge = body.bhBodyAge

//            bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray()
//            bodyFatModel.ppMusclePercentageList =
//                PPBodyDetailStandard.kg2percent(PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray(body.bhMuscleKgListUnderOrStandard, body.bhMuscleKgListStandardOrExcellent))
//            bodyFatModel.ppMuscleKgList = PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray(body.bhMuscleKgListUnderOrStandard, body.bhMuscleKgListStandardOrExcellent)
//            bodyFatModel.ppWaterPercentageList = PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray(body.bhWaterRateListUnderOrStandard, body.bhWaterRateListStandardOrExcellent)
//            bodyFatModel.ppWaterKgList =
//                PPBodyDetailStandard.percent2kg(PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray(body.bhWaterRateListUnderOrStandard, body.bhWaterRateListStandardOrExcellent))
//            bodyFatModel.ppBoneKgList = PPBodyDetailStandard.fetchPPBodyParam_Bone_StandartArray(body.bhBoneKgListUnderOrStandard, body.bhBoneKgListStandardOrExcellent)

            bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray()
            bodyFatModel.ppMusclePercentageList =
                PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray_kg_4_A(PPBodyDetailStandard.createList(body.bhMuscleKgListUnderOrStandard, body.bhMuscleKgListStandardOrExcellent), false)
            bodyFatModel.ppWaterPercentageList = PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray_percent_4_A(
                PPBodyDetailStandard.createList(body.bhWaterRateListUnderOrStandard, body.bhWaterRateListStandardOrExcellent),
                false
            )
            bodyFatModel.ppWaterKgList = PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray_percent_4_A(
                PPBodyDetailStandard.createList(body.bhWaterRateListUnderOrStandard, body.bhWaterRateListStandardOrExcellent),
                true
            )
            bodyFatModel.ppBoneKgList =
                PPBodyDetailStandard.fetchPPBodyParam_Bone_StandartArray_kg_4_A(PPBodyDetailStandard.createList(body.bhBoneKgListUnderOrStandard, body.bhBoneKgListStandardOrExcellent))

            calculate4TwoChannelList(bodyFatModel, body)
        } else {
            PPBodyDetailStandard.initWithBodyFatModel(bodyFatModel)
            bodyFatModel.ppBMI = CalculateUtil.calculateBMI(bodyFatModel.ppHeightCm, bodyFatModel.ppWeightKg)
            bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray()
            bodyFatModel.ppWeightKgList = PPBodyDetailStandard.fetchPPBodyParam_Weight_StandartArray()
        }
        bodyFatModel.bodyDetailModel = PPBodyDetailModel(bodyFatModel)
    }

    /**
     * 4电极双频
     */
    fun calculate4TwoChannelList(bodyFatModel: PPBodyFatModel, twoLegs140: BhTwoLegs240) {
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
