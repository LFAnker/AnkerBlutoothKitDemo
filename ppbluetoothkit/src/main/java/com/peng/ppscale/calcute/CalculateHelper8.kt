package com.peng.ppscale.calcute

import com.besthealth.bh1BodyComposition.*
import com.peng.ppscale.data.*
import com.peng.ppscale.vo.*
import com.peng.ppscale.calcute.BodyDetailUnitType.*

object CalculateHelper8 {

    /**
     * 8电极算法
     *
     * @param bodyFatModel
     */
    fun calcuteTypeAlternate8(bodyFatModel: PPBodyFatModel, product: Int) {
        val bodyBaseModel: PPBodyBaseModel? = bodyFatModel.ppBodyBaseModel
        bodyBaseModel?.let {
            val body = BhBody270()

            body.bhAge = bodyBaseModel.userModel?.age ?: 0
            body.bhHeightCm = bodyFatModel.ppHeightCm.toFloat()
            body.bhWeightKg = bodyBaseModel.weight / 100.0f
            body.bhSex = if (bodyBaseModel.userModel?.sex == PPUserGender.PPUserGenderMale) BhSex.MALE.ordinal else BhSex.FEMALE.ordinal
            body.bhZ20KhzRightArmEnCode = bodyBaseModel.z20KhzRightArmEnCode
            body.bhZ20KhzLeftArmEnCode = bodyBaseModel.z20KhzLeftArmEnCode
            body.bhZ20KhzTrunkEnCode = bodyBaseModel.z20KhzTrunkEnCode
            body.bhZ20KhzRightLegEnCode = bodyBaseModel.z20KhzRightLegEnCode
            body.bhZ20KhzLeftLegEnCode = bodyBaseModel.z20KhzLeftLegEnCode
            body.bhZ100KhzRightArmEnCode = bodyBaseModel.z100KhzRightArmEnCode
            body.bhZ100KhzLeftArmEnCode = bodyBaseModel.z100KhzLeftArmEnCode
            body.bhZ100KhzTrunkEnCode = bodyBaseModel.z100KhzTrunkEnCode
            body.bhZ100KhzRightLegEnCode = bodyBaseModel.z100KhzRightLegEnCode
            body.bhZ100KhzLeftLegEnCode = bodyBaseModel.z100KhzLeftLegEnCode


            /*****************************************验证测试代码************************************/
//            body.bhAge = 21
//            body.bhHeightCm = 181f
//            body.bhWeightKg = 87.0f
//            body.bhSex = BhSex.MALE.ordinal
//            body.bhZ20KhzRightArmEnCode = 8000391
//            body.bhZ20KhzLeftArmEnCode = 540676509
//            body.bhZ20KhzTrunkEnCode = 10489417
//            body.bhZ20KhzRightLegEnCode = 1883928548
//            body.bhZ20KhzLeftLegEnCode = 6971739
//            body.bhZ100KhzRightArmEnCode = 3920247
//            body.bhZ100KhzLeftArmEnCode = 1342749892
//            body.bhZ100KhzTrunkEnCode = 816076465
//            body.bhZ100KhzRightLegEnCode = 537368723
//            body.bhZ100KhzLeftLegEnCode = 32829975
            /*****************************************验证测试代码************************************/


//            bhProduct = 1， bhTrimming= 1，bhIsHome = 0，bhIsEnhancedRepeat = 0
            body.bhProduct = product
            body.bhTrimming = 1.0f
            body.bhIsHome = false
            body.bhIsEnhancedRepeat = false
            val bhErrorType: BhErrorType = BhErrorType.values().get(body.getBodyComposition())
            println("错误信息：$bhErrorType")
            bodyFatModel.ppSDKVersion = HT_8_ + body.getSDKVersion()
            System.out.println("SDKVersion=" + bodyFatModel.ppSDKVersion)
            bodyFatModel.errorType = BodyFatCalcuteHelper.calculateHTErrorType(bhErrorType)
            System.out.println("體重(Kg)=" + body.bhWeightKg)
            System.out.println("身高(cm)=" + body.bhHeightCm)
            System.out.println("年齡(歲)=" + body.bhAge)
            System.out.println("性別=" + BhSex.values().get(body.bhSex))
            System.out.println("bhProduct=" + body.bhProduct)
            System.out.println("加密阻抗-100Khz右手=" + body.bhZ100KhzRightArmEnCode)
            System.out.println("加密阻抗-100Khz左手=" + body.bhZ100KhzLeftArmEnCode)
            System.out.println("加密阻抗-100Khz躯干=" + body.bhZ100KhzTrunkEnCode)
            System.out.println("加密阻抗-100Khz右脚=" + body.bhZ100KhzRightLegEnCode)
            System.out.println("加密阻抗-100Khz左脚=" + body.bhZ100KhzLeftLegEnCode)
            System.out.println("加密阻抗-20Khz右手=" + body.bhZ20KhzRightArmEnCode)
            System.out.println("加密阻抗-20Khz左手=" + body.bhZ20KhzLeftArmEnCode)
            System.out.println("加密阻抗-20Khz躯干=" + body.bhZ20KhzTrunkEnCode)
            System.out.println("加密阻抗-20Khz右脚=" + body.bhZ20KhzRightLegEnCode)
            System.out.println("加密阻抗-20Khz左脚=" + body.bhZ20KhzLeftLegEnCode)
            System.out.println("解密阻抗-100Khz右手=" + body.bhZ100KhzRightArmDeCode)
            System.out.println("解密阻抗-100Khz左手=" + body.bhZ100KhzLeftArmDeCode)
            System.out.println("解密阻抗-100Khz躯干=" + body.bhZ100KhzTrunkDeCode)
            System.out.println("解密阻抗-100Khz右脚=" + body.bhZ100KhzRightLegDeCode)
            System.out.println("解密阻抗-100Khz左脚=" + body.bhZ100KhzLeftLegDeCode)
            System.out.println("解密阻抗-20Khz右手=" + body.bhZ20KhzRightArmDeCode)
            System.out.println("解密阻抗-20Khz左手=" + body.bhZ20KhzLeftArmDeCode)
            System.out.println("解密阻抗-20Khz躯干=" + body.bhZ20KhzTrunkDeCode)
            System.out.println("解密阻抗-20Khz右脚=" + body.bhZ20KhzRightLegDeCode)
            System.out.println("解密阻抗-20Khz左手=" + body.bhZ20KhzLeftLegDeCode)
            bodyBaseModel.z100KhzLeftArmDeCode = body.bhZ100KhzLeftArmDeCode
            bodyBaseModel.z100KhzLeftLegDeCode = body.bhZ100KhzLeftLegDeCode
            bodyBaseModel.z100KhzRightArmDeCode = body.bhZ100KhzRightArmDeCode
            bodyBaseModel.z100KhzRightLegDeCode = body.bhZ100KhzRightLegDeCode
            bodyBaseModel.z100KhzTrunkDeCode = body.bhZ100KhzTrunkDeCode
            bodyBaseModel.z20KhzLeftArmDeCode = body.bhZ20KhzLeftArmDeCode
            bodyBaseModel.z20KhzLeftLegDeCode = body.bhZ20KhzLeftLegDeCode
            bodyBaseModel.z20KhzRightArmDeCode = body.bhZ20KhzRightArmDeCode
            bodyBaseModel.z20KhzRightLegDeCode = body.bhZ20KhzRightLegDeCode
            bodyBaseModel.z20KhzTrunkDeCode = body.bhZ20KhzTrunkDeCode
            if (bodyFatModel.errorType == BodyFatErrorType.PP_ERROR_TYPE_NONE) {
                if (com.peng.ppscalelibrary.BuildConfig.DEBUG) {
                    System.out.println("全身体组成参数")
                    System.out.println("水分量(Kg)=" + body.bhWaterKg)
                    System.out.println("身體類型=" + body.bhBodyType)
                    System.out.println("脂肪率=" + body.bhBodyFatRate)
                    System.out.println("bhBMI=" + body.bhBMI)
                    System.out.println("bhWaterKg=" + body.bhWaterKg)
                    System.out.println("bhProteinKg=" + body.bhProteinKg)
                    System.out.println("bhBodyFatSubCutRate=" + body.bhBodyFatSubCutRate)
                    System.out.println("bhMuscleKg=" + body.bhMuscleKg)
                    System.out.println("bhBodyType=" + body.bhBodyType)
                    System.out.println("bhMineralKg=" + body.bhMineralKg)
                    System.out.println("bhBoneKg=" + body.bhBoneKg)
                    System.out.println("bhBodyFatKg=" + body.bhBodyFatKg)
                    System.out.println("bhProteinKg=" + body.bhProteinKg)
                    System.out.println("bhBodyFatFreeMassKg=" + body.bhBodyFatFreeMassKg)
                    System.out.println("bhMuscleRate=" + body.bhMuscleRate)
                    System.out.println("bhSkeletalMuscleKg=" + body.bhSkeletalMuscleKg)
                    System.out.println("bhWaterECWKg=" + body.bhWaterECWKg)
                    System.out.println("bhWaterICWKg=" + body.bhWaterICWKg)
                    System.out.println("bhCellMassKg=" + body.bhCellMassKg)
                    System.out.println("bhDCI=" + body.bhDCI)
                    System.out.println("bhBodyScore=" + body.bhBodyScore)
                    System.out.println("bhVFAL=" + body.bhVFAL)
                    System.out.println("bhObesity=" + body.bhObesity)
                    System.out.println("bhIdealWeightKg=" + body.bhIdealWeightKg)
                    System.out.println("bhWeightKgCon=" + body.bhWeightKgCon)
                    System.out.println("bhBodyFatKgTrunk=" + body.bhBodyFatKgTrunk)
                    System.out.println("bhBodyFatKgLeftLeg=" + body.bhBodyFatKgLeftLeg)
                    System.out.println("bhBodyFatKgRightLeg=" + body.bhBodyFatKgRightLeg)
                    System.out.println("bhBodyFatKgLeftArm=" + body.bhBodyFatKgLeftArm)
                    System.out.println("bhBodyFatKgRightArm=" + body.bhBodyFatKgRightArm)
                    System.out.println("bhBodyFatRateTrunk=" + body.bhBodyFatRateTrunk)
                    System.out.println("bhBodyFatRateLeftLeg=" + body.bhBodyFatRateLeftLeg)
                    System.out.println("bhBodyFatRateRightLeg=" + body.bhBodyFatRateRightLeg)
                    System.out.println("bhBodyFatRateLeftArm=" + body.bhBodyFatRateLeftArm)
                    System.out.println("bhBodyFatRateRightArm=" + body.bhBodyFatRateRightArm)
                    System.out.println("bhMuscleKgTrunk=" + body.bhMuscleKgTrunk)
                    System.out.println("bhMuscleKgLeftLeg=" + body.bhMuscleKgLeftLeg)
                    System.out.println("bhMuscleKgRightLeg=" + body.bhMuscleKgRightLeg)
                    System.out.println("bhMuscleKgLeftArm=" + body.bhMuscleKgLeftArm)
                    System.out.println("bhMuscleKgRightArm=" + body.bhMuscleKgRightArm)
                }
//                if (product == 5){ //验证
//                    bodyFatModel.ppFat = filterFat(body.bhBodyFatRate, bodyFatModel) + 100
//                } else {
                    bodyFatModel.ppFat = filterFat(body.bhBodyFatRate, bodyFatModel)
//                }

                bodyFatModel.ppBMI = CalculateUtil.countLfBmi(body.bhBMI)
                bodyFatModel.ppBMR = body.bhBMR
                bodyFatModel.ppFatGrade = BodyFatCalcuteHelper.bodyFatGrade(bodyFatModel.ppBMI)
                //算法库1.6.6
                bodyFatModel.ppWaterPercentage = body.bhWaterRate
                bodyFatModel.ppWaterKg = body.bhWaterKg
                //1.6.6
                bodyFatModel.ppProteinPercentage = body.bhProteinRate
                bodyFatModel.ppBodyFatSubCutPercentage = body.bhBodyFatSubCutRate //皮下脂肪率
                bodyFatModel.ppBodyFatSubCutKg = body.bhBodyFatSubCutKg //皮下脂肪量
                bodyFatModel.ppMuscleKg = body.bhMuscleKg
                bodyFatModel.ppBodyType = BodyFatCalcuteHelper.countBodyType(body.bhBodyType)
                bodyFatModel.ppMineralKg = body.bhMineralKg
                bodyFatModel.ppBoneKg = body.bhBoneKg
                bodyFatModel.ppBodyfatKg = body.bhBodyFatKg
                bodyFatModel.ppProteinKg = body.bhProteinKg
                bodyFatModel.ppLoseFatWeightKg = body.bhBodyFatFreeMassKg //去脂体重(kg)
                bodyFatModel.ppMusclePercentage = body.bhMuscleRate //肌肉率
                bodyFatModel.ppBodySkeletalKg = body.bhSkeletalMuscleKg
//                bodyFatModel.ppBodySkeletal = bodyFatModel.ppBodySkeletalKg / bodyFatModel.ppWeightKg * 100.0f
                bodyFatModel.ppBodySkeletal = body.bhSkeletalMuscleRate
                bodyFatModel.ppWaterECWKg = body.bhWaterECWKg
                bodyFatModel.ppWaterICWKg = body.bhWaterICWKg
                bodyFatModel.ppCellMassKg = body.bhCellMassKg
                bodyFatModel.ppDCI = body.bhDCI
                bodyFatModel.ppBodyAge =
                    CalculateUtil.calcuteBodyAge(bodyFatModel.ppBMI, bodyFatModel.ppAge)
                bodyFatModel.ppBodyScore = body.bhBodyScore
                bodyFatModel.ppVisceralFat = body.bhVFAL
                bodyFatModel.ppObesity = body.bhObesity
                bodyFatModel.ppIdealWeightKg = body.bhIdealWeightKg
                bodyFatModel.ppControlWeightKg = body.bhWeightKgCon
                bodyFatModel.ppBodyMuscleControl = body.bhMuscleKgCon
                bodyFatModel.ppFatControlKg = body.bhBodyFatKgCon
                bodyFatModel.ppBodyFatKgTrunk = body.bhBodyFatKgTrunk
                bodyFatModel.ppBodyFatKgLeftLeg = body.bhBodyFatKgLeftLeg
                bodyFatModel.ppBodyFatKgRightLeg = body.bhBodyFatKgRightLeg
                bodyFatModel.ppBodyFatKgLeftArm = body.bhBodyFatKgLeftArm
                bodyFatModel.ppBodyFatKgRightArm = body.bhBodyFatKgRightArm
                bodyFatModel.ppBodyFatRateTrunk = body.bhBodyFatRateTrunk
                bodyFatModel.ppBodyFatRateLeftLeg = body.bhBodyFatRateLeftLeg
                bodyFatModel.ppBodyFatRateRightLeg = body.bhBodyFatRateRightLeg
                bodyFatModel.ppBodyFatRateLeftArm = body.bhBodyFatRateLeftArm
                bodyFatModel.ppBodyFatRateRightArm = body.bhBodyFatRateRightArm
                bodyFatModel.ppMuscleKgTrunk = body.bhMuscleKgTrunk
                bodyFatModel.ppMuscleKgLeftLeg = body.bhMuscleKgLeftLeg
                bodyFatModel.ppMuscleKgRightLeg = body.bhMuscleKgRightLeg
                bodyFatModel.ppMuscleKgLeftArm = body.bhMuscleKgLeftArm
                bodyFatModel.ppMuscleKgRightArm = body.bhMuscleKgRightArm
                //1.6.6
                bodyFatModel.ppBodyStandardWeightKg = body.bhIdealWeightKg
                bodyFatModel.ppBodyHealth =
                    BodyFatCalcuteHelper.healthScore(bodyFatModel.ppBodyScore)
                bodyFatModel.ppSmi = body.bhSmi
                bodyFatModel.ppWHR = body.bhWHR
                bodyFatModel.ppMuscleRateLeftArm = body.bhMuscleRateLeftArm
                bodyFatModel.ppMuscleRateLeftLeg = body.bhMuscleRateLeftLeg
                bodyFatModel.ppMuscleRateRightArm = body.bhMuscleRateRightArm
                bodyFatModel.ppMuscleRateRightLeg = body.bhMuscleRateRightLeg
                bodyFatModel.ppMuscleRateTrunk = body.bhMuscleRateTrunk
                //1.6.8 16项八电极
                bodyFatModel.ppRightArmMuscleLevel = body.bhRightArmMuscleLevel
                bodyFatModel.ppLeftArmMuscleLevel = body.bhLeftArmMuscleLevel
                bodyFatModel.ppTrunkMuscleLevel = body.bhTrunkMuscleLevel
                bodyFatModel.ppRightLegMuscleLevel = body.bhRightLegMuscleLevel
                bodyFatModel.ppLeftLegMuscleLevel = body.bhLeftLegMuscleLevel
                bodyFatModel.ppRightArmFatLevel = body.bhRightArmFatLevel
                bodyFatModel.ppLeftArmFatLevel = body.bhLeftArmFatLevel
                bodyFatModel.ppTrunkFatLevel = body.bhTrunkFatLevel
                bodyFatModel.ppRightLegFatLevel = body.bhRightLegFatLevel
                bodyFatModel.ppLeftLegFatLevel = body.bhLeftLegFatLevel
                bodyFatModel.ppBalanceArmsLevel = body.bhBalanceArmsLevel
                bodyFatModel.ppBalanceLegsLevel = body.bhBalanceLegsLevel
                bodyFatModel.ppBalanceArmLegLevel = body.bhBalanceArmLegLevel
                bodyFatModel.ppBalanceFatArmsLevel = body.bhBalanceFatArmsLevel
                bodyFatModel.ppBalanceFatLegsLevel = body.bhBalanceFatLegsLevel
                bodyFatModel.ppBalanceFatArmLegLevel = body.bhBalanceFatArmLegLevel

                PPBodyDetailStandard.initWithBodyFatModel(bodyFatModel)
                bodyFatModel.ppWeightKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhWeightKgListMin,
                    body.bhWeightKgListMax
                )
                bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray8()
                bodyFatModel.ppFatList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhBodyFatRateListMin,
                    body.bhBodyFatRateListMax
                )
                bodyFatModel.ppBodyfatKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhBodyFatKgListMin,
                    body.bhBodyFatKgListMax
                )
                bodyFatModel.ppMusclePercentageList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhMuscleRateListMin,
                    body.bhMuscleRateListMax
                )
                bodyFatModel.ppMuscleKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhMuscleKgListMin,
                    body.bhMuscleKgListMax
                )
                bodyFatModel.ppBodySkeletalList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhSkeletalMuscleRateListMin,
                    body.bhSkeletalMuscleRateListMax
                )
                bodyFatModel.ppBodySkeletalKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhSkeletalMuscleKgListMin,
                    body.bhSkeletalMuscleKgListMax
                )
                bodyFatModel.ppWaterPercentageList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhWaterRateListMin,
                    body.bhWaterRateListMax
                )

                bodyFatModel.ppWaterKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhWaterKgListMin,
                    body.bhWaterKgListMax
                )
                bodyFatModel.ppProteinPercentageList =
                    PPBodyDetailStandard.sortValuesFromDictionary(
                        body.bhProteinRateListMin,
                        body.bhProteinRateListMax
                    )

                bodyFatModel.ppProteinKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhProteinKgListMin,
                    body.bhProteinKgListMax
                )
                bodyFatModel.ppLoseFatWeightKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhBodyFatFreeMassKgListMin,
                    body.bhBodyFatFreeMassKgListMax
                )
                bodyFatModel.ppBodyFatSubCutPercentageList =
                    PPBodyDetailStandard.sortValuesFromDictionary(
                        body.bhBodyFatSubCutRateListMin,
                        body.bhBodyFatSubCutRateListMax
                    )
                bodyFatModel.ppBodyFatSubCutKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhBodyFatSubCutKgListMin,
                    body.bhBodyFatSubCutKgListMax
                )
                bodyFatModel.ppHeartRateList =
                    PPBodyDetailStandard.fetchPPBodyParam_heart_StandartArray()
                bodyFatModel.ppBMRList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhBMRListMin,
                    body.bhBMRListMax
                )
                bodyFatModel.ppVisceralFatList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhVFALListMin,
                    body.bhVFALListMax
                )
                bodyFatModel.ppBoneKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhBoneKgListMin,
                    body.bhBoneKgListMax
                )
//                bodyFatModel.ppFatGradeList = PPBodyDetailStandard.fetchPPBodyParam_FatGrade_StandartArray()
                //1.6.6版本中,思远去掉
                bodyFatModel.ppFatGradeList = listOf()
                bodyFatModel.ppBodyHealthList = listOf()
                bodyFatModel.ppBodyAgeList =
                    PPBodyDetailStandard.fetchPPBodyParam_physicalAgeValue_StandartArray()
                bodyFatModel.ppBodyScoreList =
                    PPBodyDetailStandard.fetchPPBodyParam_BodyScore_StandartArray()

                /**************** 八电极算法独有 ****************************/
                bodyFatModel.ppCellMassKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhCellMassKgListMin,
                    body.bhCellMassKgListMax
                )
                bodyFatModel.ppMineralKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhMineralKgListMin,
                    body.bhMineralKgListMax
                )
                bodyFatModel.ppObesityList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhObesityListMin,
                    body.bhObesityListMax
                )
                bodyFatModel.ppWaterECWKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhWaterECWKgListMin,
                    body.bhWaterECWKgListMax
                )
                bodyFatModel.ppWaterICWKgList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhWaterICWKgListMin,
                    body.bhWaterICWKgListMax
                )
                bodyFatModel.ppWHRList = PPBodyDetailStandard.sortValuesFromDictionary(
                    body.bhWHRListMin,
                    body.bhWHRListMax
                )
                bodyFatModel.ppSmiList = PPBodyDetailStandard.fetchPPBodyParam_StandartArray_8(
                    PPBodyDetailStandard.createList(body.bhSmiListMin)
                )

            } else {
                PPBodyDetailStandard.initWithBodyFatModel(bodyFatModel)
                bodyFatModel.ppBMI =
                    CalculateUtil.calculateBMI(bodyFatModel.ppHeightCm, bodyFatModel.ppWeightKg)
                bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray8()
                bodyFatModel.ppWeightKgList =
                    PPBodyDetailStandard.fetchPPBodyParam_Weight_StandartArray_4()
            }
            bodyFatModel.bodyDetailModel = PPBodyDetailModel(bodyFatModel)
        }

    }

    fun c8ppVisceralFatList(min: Float, max: Float): List<Float> {
        val valueArray =
            PPBodyDetailStandard.sortValuesFromDictionary(min, max, BodyDetailUnitTypeValue)
        val temp = mutableListOf<Float>()
        for (n in valueArray) {
            temp.add(n + 0.1f)
        }
        return temp.toList()

    }

    /**
     * 四电极双手算法
     *
     * @param bodyBaseModel
     * @param bodyFatModel
     */
    fun calcuteTypeAlternateTwoArms(bodyBaseModel: PPBodyBaseModel, bodyFatModel: PPBodyFatModel) {
        val body = BhTwoArms140()
        body.bhAge = bodyFatModel.ppAge
        body.bhHeightCm = bodyFatModel.ppHeightCm.toFloat()
        body.bhWeightKg = bodyFatModel.ppWeightKg
        body.bhSex =
            if (bodyFatModel.ppSex == PPUserGender.PPUserGenderMale) BhSex.MALE.ordinal else BhSex.FEMALE.ordinal
        body.bhPeopleType = if (bodyBaseModel.userModel?.isAthleteMode
                ?: false
        ) BhPeopleType.ATHLETE.ordinal else BhPeopleType.NORMAL.ordinal
        body.bhZTwoArmsEnCode = bodyBaseModel.impedance
        val bhErrorType: BhErrorType = BhErrorType.values().get(body.getBodyComposition())
        println("impedance：" + bodyBaseModel.impedance)
        println("错误信息：$bhErrorType")
        System.out.println(body.getSDKVersion())
        bodyFatModel.ppSDKVersion = HT_4_ARM_ + body.getSDKVersion()
        bodyFatModel.errorType = BodyFatCalcuteHelper.calculateHTErrorType(bhErrorType)
        if (bodyFatModel.errorType == BodyFatErrorType.PP_ERROR_TYPE_NONE) {
            System.out.println("體重(Kg)=" + body.bhWeightKg)
            System.out.println("身高(cm)=" + body.bhHeightCm)
            System.out.println("年齡(歲)=" + body.bhAge)
            System.out.println("性別=" + BhSex.values().get(body.bhSex))
            System.out.println("用戶類型=" + body.bhPeopleType)
            System.out.println("加密阻抗-雙手50Khz(Ω)=" + body.bhZTwoArmsEnCode)
            System.out.println("解密阻抗-雙手50Khz(Ω)=" + body.bhZTwoArmsDeCode)
            bodyBaseModel.zTwoLegsDeCode = body.bhZTwoArmsDeCode
            bodyFatModel.ppProteinPercentage = body.bhProteinRate
            bodyFatModel.ppIdealWeightKg = body.bhIdealWeightKg
            bodyFatModel.ppBMI = if (body.bhBMI >= 10) body.bhBMI else 10.0f
            bodyFatModel.ppBMR = body.bhBMR
            bodyFatModel.ppVisceralFat = body.bhVFAL
            bodyFatModel.ppBoneKg = body.bhBoneKg
            bodyFatModel.ppFat = BodyFatCalcuteHelper.filterBodyfatPercentage(
                body.bhBodyFatRate,
                bodyFatModel.ppSex,
                bodyBaseModel.deviceModel?.deviceCalcuteType
                    ?: PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate
            )
            bodyFatModel.ppWaterPercentage = body.bhWaterRate
            bodyFatModel.ppMuscleKg = body.bhMuscleKg
            bodyFatModel.ppBodyScore = body.bhBodyScore
            bodyFatModel.ppMusclePercentage = body.bhMuscleRate
            bodyFatModel.ppBodySkeletalKg = body.bhSkeletalMuscleKg
            bodyFatModel.ppBodySkeletal =
                bodyFatModel.ppBodySkeletalKg / bodyFatModel.ppWeightKg * 100f
            bodyFatModel.ppBodyfatKg = bodyFatModel.ppFat * 0.01f * bodyFatModel.ppWeightKg
//            bodyFatModel.ppBodyType = BodyFatCalcuteHelper.bodyDetailTypeWithFatRate(bodyFatModel)
//            bodyFatModel.ppBodyStandardWeightKg = CalculateHelper.calculateStandardWeightWithHeight(bodyFatModel.ppHeightCm)
            bodyFatModel.ppBodyStandardWeightKg = body.bhIdealWeightKg
            bodyFatModel.ppIdealWeightKg = body.bhIdealWeightKg
            bodyFatModel.ppLoseFatWeightKg = body.bhBodyFatFreeMassKg
            bodyFatModel.ppControlWeightKg = CalculateUtil.calculateControlWeightKg(
                bodyFatModel.ppWeightKg,
                body.bhIdealWeightKg
            )
            bodyFatModel.ppFatControlKg = CalculateUtil.ppFatControlKgWithBodyfatKg(
                bodyFatModel.ppBodyfatKg,
                bodyFatModel.ppSex,
                bodyFatModel.ppBMI,
                bodyFatModel.ppAge
            )
            bodyFatModel.ppBodyMuscleControl = CalculateUtil.calculateBodyMuscleControl(
                bodyFatModel.ppSex,
                bodyFatModel.ppHeightCm,
                bodyFatModel.ppMuscleKg,
                body.bhIdealWeightKg
            )
            bodyFatModel.ppBodyFatSubCutPercentage = bodyFatModel.ppFat * 2.0f / 3.0f
            bodyFatModel.ppFatGrade = BodyFatCalcuteHelper.bodyFatGrade(bodyFatModel.ppBMI)
            bodyFatModel.ppBodyHealth = BodyFatCalcuteHelper.healthScore(bodyFatModel.ppBodyScore)
            //                    this.setPpBodyAge(body.bhBodyAge);
            bodyFatModel.ppBodyAge =
                CalculateUtil.calcuteBodyAge(bodyFatModel.ppBMI, bodyFatModel.ppAge)

            bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray()
            bodyFatModel.ppMusclePercentageList =
                PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray_kg_4_A(
                    PPBodyDetailStandard.createList(
                        body.bhMuscleKgListUnderOrStandard,
                        body.bhMuscleKgListStandardOrExcellent
                    ), false
                )
            bodyFatModel.ppMuscleKgList =
                PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray_kg_4_A(
                    PPBodyDetailStandard.createList(
                        body.bhMuscleKgListUnderOrStandard,
                        body.bhMuscleKgListStandardOrExcellent
                    ), true
                )
            bodyFatModel.ppWaterPercentageList =
                PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray_percent_4_A(
                    PPBodyDetailStandard.createList(
                        body.bhWaterRateListUnderOrStandard,
                        body.bhWaterRateListStandardOrExcellent
                    ),
                    false
                )
            bodyFatModel.ppWaterKgList =
                PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray_percent_4_A(
                    PPBodyDetailStandard.createList(
                        body.bhWaterRateListUnderOrStandard,
                        body.bhWaterRateListStandardOrExcellent
                    ),
                    true
                )
            bodyFatModel.ppBoneKgList =
                PPBodyDetailStandard.fetchPPBodyParam_Bone_StandartArray_kg_4_A(
                    PPBodyDetailStandard.createList(
                        body.bhBoneKgListUnderOrStandard,
                        body.bhBoneKgListStandardOrExcellent
                    )
                )
            calculateTwoArmList(bodyFatModel, body)
            bodyFatModel.ppBodyType = BodyFatCalcuteHelper.bodyDetailTypeWithFatRate(bodyFatModel)
        } else {
            PPBodyDetailStandard.initWithBodyFatModel(bodyFatModel)
            bodyFatModel.ppBMI =
                CalculateUtil.calculateBMI(bodyFatModel.ppHeightCm, bodyFatModel.ppWeightKg)
            bodyFatModel.ppBMIList = PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray()
            bodyFatModel.ppWeightKgList =
                PPBodyDetailStandard.fetchPPBodyParam_Weight_StandartArray_4()
        }
        bodyFatModel.bodyDetailModel = PPBodyDetailModel(bodyFatModel)
    }


    fun createList(min: Float, max: Float): List<Float> {
        val list = mutableListOf<Float>()
        list.add(min)
        list.add(max)
        return list
    }

    fun filterFat(fat: Float, bodyFatModel: PPBodyFatModel): Float {
        return if (fat >= 10) {
            fat
        } else if (fat >= 6) {
            fat + 3f
        } else if (fat >= 5) {
            fat + 4f
        } else if (fat >= 3) {
            fat + 5f
        } else if (fat >= 2) {
            fat + 6f
        } else if (fat >= 1) {
            fat + 7f
        } else {
            fat + 8f
        }
    }

    fun filterWater(water: Float): Float {
//        if (water < 5) {
//            return 5.0f
//        } else if (water > 75.0) {
//            return 75.0f
//        } else {
        return water
//        }
    }

    /**
     * 4电极双手
     */
    fun calculateTwoArmList(
        bodyFatModel: PPBodyFatModel,
        twoArms140: BhTwoArms140
    ) {
        PPBodyDetailStandard.initWithBodyFatModel(bodyFatModel)
        val ppBodyfatRateCreateList = PPBodyDetailStandard.createList(
            twoArms140.bhBodyFatRateListUnderFatOrStandardMinus,
            twoArms140.bhBodyFatRateListStandardMinusOrStandardPlus,
            twoArms140.bhBodyFatRateListStandardPlusOrOverFat,
            twoArms140.bhBodyFatRateListOverFatOrObese
        )
        val bhSkeletalMuscleKgList = PPBodyDetailStandard.createList(
            twoArms140.bhSkeletalMuscleKgListUnderOrStandard,
            twoArms140.bhSkeletalMuscleKgListStandardOrExcellent
        )
        val bhProteinRateList = PPBodyDetailStandard.createList(
            twoArms140.bhProteinRateListUnderOrStandard,
            twoArms140.bhProteinRateListStandardOrExcellent
        )
        val bhBodyFatSubCutRateList = PPBodyDetailStandard.createList(
            twoArms140.bhBodyFatSubCutRateListUnderOrStandard,
            twoArms140.bhBodyFatSubCutRateListStandardOrOver
        )
        val bhBMRList =
            PPBodyDetailStandard.createList(twoArms140.bhBMRListUnderOrStandard.toFloat())
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
