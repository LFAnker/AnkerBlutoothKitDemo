package com.peng.ppscale.calcute

import com.besthealth.bhBodyComposition.BhErrorType
import com.besthealth.bhBodyComposition.BhPeopleType
import com.besthealth.bhBodyComposition.BhSex
import com.besthealth.bhBodyComposition.BhTwoLegs140
import com.peng.ppscale.data.*
import com.peng.ppscale.vo.*

object CalculateHelper4 {

    /**
     * 4电极双脚阻抗
     *
     * @param bodyBaseModel
     * @param bodyFatModel
     */
    fun calcuteTypeAlternateTwoLegs(bodyBaseModel: PPBodyBaseModel, bodyFatModel: PPBodyFatModel) {

        val body = BhTwoLegs140()
        bodyFatModel.ppSDKVersion = HT_4_LEG_ + body.getSDKVersion()
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
            bodyFatModel.ppMuscleKgList = PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray_kg_4_A(PPBodyDetailStandard.createList(body.bhMuscleKgListUnderOrStandard, body.bhMuscleKgListStandardOrExcellent), true)
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
            bodyFatModel.ppMusclePercentageList = PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray_kg_4_A(PPBodyDetailStandard.createList(body.bhMuscleKgListUnderOrStandard, body.bhMuscleKgListStandardOrExcellent), false)
            bodyFatModel.ppWaterPercentageList = PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray_percent_4_A(PPBodyDetailStandard.createList(body.bhWaterRateListUnderOrStandard, body.bhWaterRateListStandardOrExcellent), false)
            bodyFatModel.ppWaterKgList = PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray_percent_4_A(PPBodyDetailStandard.createList(body.bhWaterRateListUnderOrStandard, body.bhWaterRateListStandardOrExcellent), true)
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
     * 秤端计算的秤
     *
     * @param bodyBaseModel
     * @param bodyFatModel
     */
    fun calcuteTypeInScale(bodyFatModel: PPBodyFatModel) {
        System.out.println(bodyFatModel.ppSDKVersion)
        if (bodyFatModel.ppFat > 0) {
            bodyFatModel.errorType = BodyFatErrorType.PP_ERROR_TYPE_NONE
        } else {
            bodyFatModel.errorType = BodyFatErrorType.PP_ERROR_TYPE_IMPEDANCE_TWO_LEGS
        }
        if (bodyFatModel.errorType == BodyFatErrorType.PP_ERROR_TYPE_NONE) {
            System.out.println("體重(Kg)=" + bodyFatModel.ppWeightKg)
            System.out.println("身高(cm)=" + bodyFatModel.ppHeightCm)
            System.out.println("年齡(歲)=" + bodyFatModel.ppAge)
            System.out.println("性別=" + bodyFatModel.ppSex)
            calculateListNormal(bodyFatModel)
        } else {
            PPBodyDetailStandard.initWithBodyFatModel(bodyFatModel)
            bodyFatModel.ppBMI = CalculateUtil.calculateBMI(bodyFatModel.ppHeightCm, bodyFatModel.ppWeightKg)
            bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray()
            bodyFatModel.ppWeightKgList = PPBodyDetailStandard.fetchPPBodyParam_Weight_StandartArray()
        }
        bodyFatModel.bodyDetailModel = PPBodyDetailModel(bodyFatModel)
    }

    /**
     * 直流称和秤端计算称
     */
    fun calculateListNormal(bodyFatModel: PPBodyFatModel) {
        PPBodyDetailStandard.initWithBodyFatModel(bodyFatModel)
        bodyFatModel.ppWeightKgList = PPBodyDetailStandard.fetchPPBodyParam_Weight_StandartArray_4()
        bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray()
        bodyFatModel.ppBodyfatKgList =
            PPBodyDetailStandard.fetchPPBodyParam_BodyFat_StandartArray_4_D_typeWeight(true)
        bodyFatModel.ppFatList =
            PPBodyDetailStandard.fetchPPBodyParam_BodyFat_StandartArray_4_D_typeWeight(false)
        bodyFatModel.ppMusclePercentageList =
            PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray_kg_4_D_typeWeight(false)
        bodyFatModel.ppMuscleKgList =
            PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray_kg_4_D_typeWeight(true)
        bodyFatModel.ppBodySkeletalList =
            PPBodyDetailStandard.fetchPPBodyParam_BodySkeletal_StandartArray_4_D_typeWeight(false)
        bodyFatModel.ppBodySkeletalKgList =
            PPBodyDetailStandard.fetchPPBodyParam_BodySkeletal_StandartArray_4_D_typeWeight(true)
        bodyFatModel.ppWaterPercentageList =
            PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray_4_D_typeWeight(false)
        bodyFatModel.ppWaterKgList =
            PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray_4_D_typeWeight(true)
        bodyFatModel.ppProteinPercentageList =
            PPBodyDetailStandard.fetchPPBodyParam_Protein_StandartArray_4_D_typeWeight(false)
        bodyFatModel.ppProteinKgList =
            PPBodyDetailStandard.fetchPPBodyParam_Protein_StandartArray_4_D_typeWeight(true)
        bodyFatModel.ppBodyFatSubCutPercentageList =
            PPBodyDetailStandard.fetchPPBodyParam_BodySubcutaneousFat_StandartArray_4_D_typeWeight(
                false
            )
        bodyFatModel.ppBodyFatSubCutKgList =
            PPBodyDetailStandard.fetchPPBodyParam_BodySubcutaneousFat_StandartArray_4_D_typeWeight(
                true
            )
        bodyFatModel.ppHeartRateList = PPBodyDetailStandard.fetchPPBodyParam_heart_StandartArray()
        bodyFatModel.ppBMRList = PPBodyDetailStandard.fetchPPBodyParam_BMR_StandartArray_kcal_4_D()
        bodyFatModel.ppVisceralFatList =
            PPBodyDetailStandard.fetchPPBodyParam_VisFat_StandartArray_4()
        bodyFatModel.ppBoneKgList =
            PPBodyDetailStandard.fetchPPBodyParam_Bone_StandartArray_kg_4_D()
        bodyFatModel.ppBodyHealthList = listOf()
        bodyFatModel.ppBodyAgeList =
            PPBodyDetailStandard.fetchPPBodyParam_physicalAgeValue_StandartArray()
        bodyFatModel.ppBodyScoreList =
            PPBodyDetailStandard.fetchPPBodyParam_BodyScore_StandartArray()
        bodyFatModel.ppFatGradeList = listOf()
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
