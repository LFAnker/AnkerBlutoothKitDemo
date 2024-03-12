package com.peng.ppscale.data

import android.annotation.SuppressLint
import android.content.Context
import com.peng.ppscale.vo.BodyFatErrorType
import com.peng.ppscale.vo.HT_8_
import com.peng.ppscale.vo.PPBodyFatModel

class PPBodyDetailModel {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }

    var PPBodyParam_Weight: PPBodyDetailInfoModel? = null
    var PPBodyParam_BodyFat: PPBodyDetailInfoModel? = null
    var PPBodyParam_BMI: PPBodyDetailInfoModel? = null
    var PPBodyParam_Mus: PPBodyDetailInfoModel? = null
    var PPBodyParam_BMR: PPBodyDetailInfoModel? = null
    var PPBodyParam_Water: PPBodyDetailInfoModel? = null
    var PPBodyParam_heart: PPBodyDetailInfoModel? = null
    var PPBodyParam_BodyFatKg: PPBodyDetailInfoModel? = null
    var PPBodyParam_proteinPercentage: PPBodyDetailInfoModel? = null
    var PPBodyParam_BodyLBW: PPBodyDetailInfoModel? = null
    var PPBodyParam_BodySubcutaneousFat: PPBodyDetailInfoModel? = null
    var PPBodyParam_BodySkeletal: PPBodyDetailInfoModel? = null
    var PPBodyParam_SkeletalMuscleKg: PPBodyDetailInfoModel? = null
    var PPBodyParam_VisFat: PPBodyDetailInfoModel? = null
    var PPBodyParam_MusRate: PPBodyDetailInfoModel? = null
    var PPBodyParam_MuscleControl: PPBodyDetailInfoModel? = null
    var PPBodyParam_BodyControlLiang: PPBodyDetailInfoModel? = null
    var PPBodyParam_Bodystandard: PPBodyDetailInfoModel? = null
    var PPBodyParam_BodyControl: PPBodyDetailInfoModel? = null
    var PPBodyParam_Bone: PPBodyDetailInfoModel? = null
    var PPBodyParam_BodyType: PPBodyDetailInfoModel? = null
    var PPBodyParam_FatGrade: PPBodyDetailInfoModel? = null
    var PPBodyParam_BodyHealth: PPBodyDetailInfoModel? = null
    var PPBodyParam_physicalAgeValue: PPBodyDetailInfoModel? = null
    var PPBodyParam_BodyScore: PPBodyDetailInfoModel? = null


    var PPBodyParam_waterKg: PPBodyDetailInfoModel? = null
    var PPBodyParam_proteinKg: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatSubCutKg: PPBodyDetailInfoModel? = null
    var PPBodyParam_cellMassKg: PPBodyDetailInfoModel? = null
    var PPBodyParam_dCI: PPBodyDetailInfoModel? = null
    var PPBodyParam_mineralKg: PPBodyDetailInfoModel? = null
    var PPBodyParam_obesity: PPBodyDetailInfoModel? = null
    var PPBodyParam_waterECWKg: PPBodyDetailInfoModel? = null
    var PPBodyParam_waterICWKg: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatKgLeftArm: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatKgLeftLeg: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatKgRightArm: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatKgRightLeg: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatKgTrunk: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatRateLeftArm: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatRateLeftLeg: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatRateRightArm: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatRateRightLeg: PPBodyDetailInfoModel? = null
    var PPBodyParam_bodyFatRateTrunk: PPBodyDetailInfoModel? = null
    var PPBodyParam_muscleKgLeftArm: PPBodyDetailInfoModel? = null
    var PPBodyParam_muscleKgLeftLeg: PPBodyDetailInfoModel? = null
    var PPBodyParam_muscleKgRightArm: PPBodyDetailInfoModel? = null
    var PPBodyParam_muscleKgRightLeg: PPBodyDetailInfoModel? = null
    var PPBodyParam_muscleKgTrunk: PPBodyDetailInfoModel? = null

    var PPBodyParam_muscleRateLeftArm: PPBodyDetailInfoModel? = null
    var PPBodyParam_muscleRateLeftLeg: PPBodyDetailInfoModel? = null
    var PPBodyParam_muscleRateRightArm: PPBodyDetailInfoModel? = null
    var PPBodyParam_muscleRateRightLeg: PPBodyDetailInfoModel? = null
    var PPBodyParam_muscleRateTrunk: PPBodyDetailInfoModel? = null

    var PPBodyParam_smi: PPBodyDetailInfoModel? = null
    var PPBodyParam_WHR: PPBodyDetailInfoModel? = null
    var PPBodyParam_rightArmMuscleLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_leftArmMuscleLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_trunkMuscleLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_rightLegMuscleLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_leftLegMuscleLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_rightArmFatLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_leftArmFatLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_trunkFatLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_rightLegFatLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_leftLegFatLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_balanceArmsLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_balanceLegsLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_balanceArmLegLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_balanceFatArmsLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_balanceFatLegsLevel: PPBodyDetailInfoModel? = null
    var PPBodyParam_balanceFatArmLegLevel: PPBodyDetailInfoModel? = null
    var fatModel: PPBodyFatModel

    //输出排序好的数据对象
    var ppBodyDetailInfoModelToJsonVo: PPBodyDetailInfoModelToJsonVo? = null

    constructor(fatModel: PPBodyFatModel) {
        this.fatModel = fatModel
        BuildPPBodyDetailModelUtils.ppSDKVersion = fatModel.ppSDKVersion
        PPBodyDetailStandard.initWithBodyFatModel(fatModel)
        fetchAllPPBodyDetailInfo()
    }

    fun fetchAllPPBodyDetailInfo() {
        PPBodyParam_Weight = fetchPPBodyParam_Weight()
        PPBodyParam_Bodystandard = fetchPPBodyParam_Bodystandard()
        PPBodyParam_BodyControl = fetchPPBodyParam_BodyControl()
        PPBodyParam_BodyLBW = fetchPPBodyParam_BodyLBW()
        PPBodyParam_BodyFat = fetchPPBodyParam_BodyFat()
        PPBodyParam_BodyFatKg = fetchPPBodyParam_BodyFatKg()
        PPBodyParam_Water = fetchPPBodyParam_Water()
        PPBodyParam_MusRate = fetchPPBodyParam_MusRate()
        PPBodyParam_Mus = fetchPPBodyParam_Mus()
        PPBodyParam_Bone = fetchPPBodyParam_Bone()
        PPBodyParam_BMR = fetchPPBodyParam_BMR()
        PPBodyParam_BMI = fetchPPBodyParam_BMI()
        PPBodyParam_VisFat = fetchPPBodyParam_VisFat()
        PPBodyParam_physicalAgeValue = fetchPPBodyParam_physicalAgeValue()
        PPBodyParam_proteinPercentage = fetchPPBodyParam_proteinPercentage()
        PPBodyParam_BodyType = fetchPPBodyParam_BodyType()
        PPBodyParam_BodyScore = fetchPPBodyParam_BodyScore()
        PPBodyParam_BodySubcutaneousFat = fetchPPBodyParam_BodySubcutaneousFat()
        PPBodyParam_BodySkeletal = fetchPPBodyParam_BodySkeletal()
        PPBodyParam_SkeletalMuscleKg = fetchPPBodyParam_SkeletalMuscleKg()
        PPBodyParam_MuscleControl = fetchPPBodyParam_MuscleControl()
        PPBodyParam_BodyControlLiang = fetchPPBodyParam_BodyControlLiang()
        PPBodyParam_FatGrade = fetchPPBodyParam_FatGrade()
        PPBodyParam_BodyHealth = fetchPPBodyParam_BodyHealth()
        PPBodyParam_heart = fetchPPBodyParam_heart()
        PPBodyParam_waterKg = fetchPPBodyParam_waterKg()
        PPBodyParam_proteinKg = fetchPPBodyParam_proteinKg()
        PPBodyParam_bodyFatSubCutKg = fetchPPBodyParam_bodyFatSubCutKg()
        PPBodyParam_cellMassKg = fetchPPBodyParam_cellMassKg()
        PPBodyParam_dCI = fetchPPBodyParam_dCI()
        PPBodyParam_mineralKg = fetchPPBodyParam_mineralKg()
        PPBodyParam_obesity = fetchPPBodyParam_obesity()
        PPBodyParam_waterECWKg = fetchPPBodyParam_waterECWKg()
        PPBodyParam_waterICWKg = fetchPPBodyParam_waterICWKg()
        PPBodyParam_bodyFatKgLeftArm = fetchPPBodyParam_bodyFatKgLeftArm()
        PPBodyParam_bodyFatKgLeftLeg = fetchPPBodyParam_bodyFatKgLeftLeg()
        PPBodyParam_bodyFatKgRightArm = fetchPPBodyParam_bodyFatKgRightArm()
        PPBodyParam_bodyFatKgRightLeg = fetchPPBodyParam_bodyFatKgRightLeg()
        PPBodyParam_bodyFatKgTrunk = fetchPPBodyParam_bodyFatKgTrunk()
        PPBodyParam_bodyFatRateLeftArm = fetchPPBodyParam_bodyFatRateLeftArm()
        PPBodyParam_bodyFatRateLeftLeg = fetchPPBodyParam_bodyFatRateLeftLeg()
        PPBodyParam_bodyFatRateRightArm = fetchPPBodyParam_bodyFatRateRightArm()
        PPBodyParam_bodyFatRateRightLeg = fetchPPBodyParam_bodyFatRateRightLeg()
        PPBodyParam_bodyFatRateTrunk = fetchPPBodyParam_bodyFatRateTrunk()
        PPBodyParam_muscleKgLeftArm = fetchPPBodyParam_muscleKgLeftArm()
        PPBodyParam_muscleKgLeftLeg = fetchPPBodyParam_muscleKgLeftLeg()
        PPBodyParam_muscleKgRightArm = fetchPPBodyParam_muscleKgRightArm()
        PPBodyParam_muscleKgRightLeg = fetchPPBodyParam_muscleKgRightLeg()
        PPBodyParam_muscleKgTrunk = fetchPPBodyParam_muscleKgTrunk()

        PPBodyParam_muscleRateLeftArm = fetchPPBodyParam_muscleRateLeftArm()
        PPBodyParam_muscleRateLeftLeg = fetchPPBodyParam_muscleRateLeftLeg()
        PPBodyParam_muscleRateRightArm = fetchPPBodyParam_muscleRateRightArm()
        PPBodyParam_muscleRateRightLeg = fetchPPBodyParam_muscleRateRightLeg()
        PPBodyParam_muscleRateTrunk = fetchPPBodyParam_muscleRateTrunk()

        PPBodyParam_smi = fetchPPBodyParam_ppSmi()
        PPBodyParam_WHR = fetchPPBodyParam_WHR()
        PPBodyParam_rightArmMuscleLevel = fetchPPBodyParam_RightArmMuscleLevel()
        PPBodyParam_leftArmMuscleLevel = fetchPPBodyParam_LeftArmMuscleLevel()
        PPBodyParam_trunkMuscleLevel = fetchPPBodyParam_TrunkMuscleLevel()
        PPBodyParam_rightLegMuscleLevel = fetchPPBodyParam_RightLegMuscleLevel()
        PPBodyParam_leftLegMuscleLevel = fetchPPBodyParam_LeftLegMuscleLevel()
        PPBodyParam_rightArmFatLevel = fetchPPBodyParam_RightArmFatLevel()
        PPBodyParam_leftArmFatLevel = fetchPPBodyParam_LeftArmFatLevel()
        PPBodyParam_trunkFatLevel = fetchPPBodyParam_TrunkFatLevel()
        PPBodyParam_rightLegFatLevel = fetchPPBodyParam_RightLegFatLevel()
        PPBodyParam_leftLegFatLevel = fetchPPBodyParam_LeftLegFatLevel()
        PPBodyParam_balanceArmsLevel = fetchPPBodyParam_BalanceArmsLevel()
        PPBodyParam_balanceLegsLevel = fetchPPBodyParam_BalanceLegsLevel()
        PPBodyParam_balanceArmLegLevel = fetchPPBodyParam_BalanceArmLegLevel()
        PPBodyParam_balanceFatArmsLevel = fetchPPBodyParam_BalanceFatArmsLevel()
        PPBodyParam_balanceFatLegsLevel = fetchPPBodyParam_BalanceFatLegsLevel()
        PPBodyParam_balanceFatArmLegLevel = fetchPPBodyParam_BalanceFatArmLegLevel()
        ppBodyDetailInfoModelToJsonVo = fetchPPBodyParam_ppBodyDetailInfoModelToJsonVo()
    }

    /**
     * 构造数据的集合,排序好的
     */
    fun fetchPPBodyParam_ppBodyDetailInfoModelToJsonVo(): PPBodyDetailInfoModelToJsonVo {
        val lefuBodyDataList: MutableList<PPBodyDetailInfoModel> = ArrayList()
        val errorType = fatModel.errorType.name
        //有错误,没有测值
        if (fatModel.errorType != BodyFatErrorType.PP_ERROR_TYPE_NONE) {
            if (context != null) {
                lefuBodyDataList.add(PPBodyParam_Weight!!)
                lefuBodyDataList.add(PPBodyParam_BMI!!)
            }
        } else {
            if (fatModel.ppSDKVersion?.startsWith("HT_4") == true) {
                if (context != null) {
                    lefuBodyDataList.add(PPBodyParam_Weight!!)
                    lefuBodyDataList.add(PPBodyParam_BodyFat!!)
                    lefuBodyDataList.add(PPBodyParam_BMI!!)
                    lefuBodyDataList.add(PPBodyParam_Mus!!)
                    lefuBodyDataList.add(PPBodyParam_BMR!!)
                    lefuBodyDataList.add(PPBodyParam_Water!!)
                    lefuBodyDataList.add(PPBodyParam_heart!!)
                    lefuBodyDataList.add(PPBodyParam_BodyFatKg!!)
                    lefuBodyDataList.add(PPBodyParam_proteinPercentage!!)
                    lefuBodyDataList.add(PPBodyParam_BodyLBW!!)
                    lefuBodyDataList.add(PPBodyParam_BodySubcutaneousFat!!)
                    lefuBodyDataList.add(PPBodyParam_BodySkeletal!!)
                    lefuBodyDataList.add(PPBodyParam_SkeletalMuscleKg!!)
                    lefuBodyDataList.add(PPBodyParam_VisFat!!)
                    lefuBodyDataList.add(PPBodyParam_MusRate!!)
                    lefuBodyDataList.add(PPBodyParam_MuscleControl!!)
                    lefuBodyDataList.add(PPBodyParam_BodyControlLiang!!)
                    lefuBodyDataList.add(PPBodyParam_Bodystandard!!)
                    lefuBodyDataList.add(PPBodyParam_BodyControl!!)
                    lefuBodyDataList.add(PPBodyParam_Bone!!)
                    lefuBodyDataList.add(PPBodyParam_BodyType!!)
                    lefuBodyDataList.add(PPBodyParam_FatGrade!!)
                    lefuBodyDataList.add(PPBodyParam_BodyHealth!!)
                    lefuBodyDataList.add(PPBodyParam_physicalAgeValue!!)
                    lefuBodyDataList.add(PPBodyParam_BodyScore!!)
                }
            } else if (fatModel.ppSDKVersion?.startsWith("LF_4") == true) {
                if (context != null) {
                    lefuBodyDataList.add(PPBodyParam_Weight!!)
                    lefuBodyDataList.add(PPBodyParam_BodyFat!!)
                    lefuBodyDataList.add(PPBodyParam_BMI!!)
                    lefuBodyDataList.add(PPBodyParam_Mus!!)
                    lefuBodyDataList.add(PPBodyParam_BMR!!)
                    lefuBodyDataList.add(PPBodyParam_Water!!)
                    lefuBodyDataList.add(PPBodyParam_heart!!)
                    lefuBodyDataList.add(PPBodyParam_BodyFatKg!!)
                    lefuBodyDataList.add(PPBodyParam_proteinPercentage!!)
                    lefuBodyDataList.add(PPBodyParam_BodyLBW!!)
                    lefuBodyDataList.add(PPBodyParam_BodySubcutaneousFat!!)
                    lefuBodyDataList.add(PPBodyParam_BodySkeletal!!)
                    lefuBodyDataList.add(PPBodyParam_SkeletalMuscleKg!!)
                    lefuBodyDataList.add(PPBodyParam_VisFat!!)
                    lefuBodyDataList.add(PPBodyParam_MusRate!!)
                    lefuBodyDataList.add(PPBodyParam_MuscleControl!!)
                    lefuBodyDataList.add(PPBodyParam_BodyControlLiang!!)
                    lefuBodyDataList.add(PPBodyParam_Bodystandard!!)
                    lefuBodyDataList.add(PPBodyParam_BodyControl!!)
                    lefuBodyDataList.add(PPBodyParam_Bone!!)
                    lefuBodyDataList.add(PPBodyParam_BodyType!!)
                    lefuBodyDataList.add(PPBodyParam_FatGrade!!)
                    lefuBodyDataList.add(PPBodyParam_BodyHealth!!)
                    lefuBodyDataList.add(PPBodyParam_physicalAgeValue!!)
                    lefuBodyDataList.add(PPBodyParam_BodyScore!!)
                }
            } else if (fatModel.ppSDKVersion?.startsWith("HT_8") == true) {
                if (context != null) {
                    lefuBodyDataList.add(PPBodyParam_Weight!!)
                    lefuBodyDataList.add(PPBodyParam_BodyFat!!)
                    lefuBodyDataList.add(PPBodyParam_BMI!!)
                    lefuBodyDataList.add(PPBodyParam_Mus!!)
                    lefuBodyDataList.add(PPBodyParam_BMR!!)
                    lefuBodyDataList.add(PPBodyParam_dCI!!)
                    lefuBodyDataList.add(PPBodyParam_heart!!)
                    lefuBodyDataList.add(PPBodyParam_Water!!)
                    lefuBodyDataList.add(PPBodyParam_proteinPercentage!!)
                    lefuBodyDataList.add(PPBodyParam_waterKg!!)
                    lefuBodyDataList.add(PPBodyParam_proteinKg!!)
                    lefuBodyDataList.add(PPBodyParam_BodyFatKg!!)
                    lefuBodyDataList.add(PPBodyParam_BodyLBW!!)
                    lefuBodyDataList.add(PPBodyParam_BodySubcutaneousFat!!)
                    lefuBodyDataList.add(PPBodyParam_BodySkeletal!!)
                    lefuBodyDataList.add(PPBodyParam_SkeletalMuscleKg!!)
                    lefuBodyDataList.add(PPBodyParam_VisFat!!)
                    lefuBodyDataList.add(PPBodyParam_Bone!!)
                    lefuBodyDataList.add(PPBodyParam_bodyFatSubCutKg!!)
                    lefuBodyDataList.add(PPBodyParam_MusRate!!)
                    lefuBodyDataList.add(PPBodyParam_bodyFatRateLeftArm!!)
                    lefuBodyDataList.add(PPBodyParam_cellMassKg!!)
                    lefuBodyDataList.add(PPBodyParam_bodyFatRateRightArm!!)
                    lefuBodyDataList.add(PPBodyParam_waterICWKg!!)
                    lefuBodyDataList.add(PPBodyParam_bodyFatRateTrunk!!)
                    lefuBodyDataList.add(PPBodyParam_waterECWKg!!)

                    lefuBodyDataList.add(PPBodyParam_bodyFatRateLeftLeg!!)
                    lefuBodyDataList.add(PPBodyParam_mineralKg!!)
                    lefuBodyDataList.add(PPBodyParam_bodyFatRateRightLeg!!)
                    lefuBodyDataList.add(PPBodyParam_muscleKgLeftArm!!)
                    lefuBodyDataList.add(PPBodyParam_bodyFatKgLeftArm!!)
                    lefuBodyDataList.add(PPBodyParam_muscleKgRightArm!!)
                    lefuBodyDataList.add(PPBodyParam_bodyFatKgRightArm!!)
                    lefuBodyDataList.add(PPBodyParam_muscleKgTrunk!!)

                    lefuBodyDataList.add(PPBodyParam_muscleRateLeftArm!!)
                    lefuBodyDataList.add(PPBodyParam_muscleRateLeftLeg!!)
                    lefuBodyDataList.add(PPBodyParam_muscleRateRightArm!!)
                    lefuBodyDataList.add(PPBodyParam_muscleRateRightLeg!!)
                    lefuBodyDataList.add(PPBodyParam_muscleRateTrunk!!)

                    lefuBodyDataList.add(PPBodyParam_bodyFatKgTrunk!!)
                    lefuBodyDataList.add(PPBodyParam_muscleKgLeftLeg!!)

                    lefuBodyDataList.add(PPBodyParam_bodyFatKgLeftLeg!!)
                    lefuBodyDataList.add(PPBodyParam_muscleKgRightLeg!!)
                    lefuBodyDataList.add(PPBodyParam_bodyFatKgRightLeg!!)
                    lefuBodyDataList.add(PPBodyParam_MuscleControl!!)
                    lefuBodyDataList.add(PPBodyParam_BodyControlLiang!!)
                    lefuBodyDataList.add(PPBodyParam_Bodystandard!!)
                    lefuBodyDataList.add(PPBodyParam_BodyControl!!)
                    lefuBodyDataList.add(PPBodyParam_obesity!!)
                    lefuBodyDataList.add(PPBodyParam_BodyType!!)
                    lefuBodyDataList.add(PPBodyParam_FatGrade!!)
                    lefuBodyDataList.add(PPBodyParam_BodyHealth!!)
                    lefuBodyDataList.add(PPBodyParam_physicalAgeValue!!)
                    lefuBodyDataList.add(PPBodyParam_BodyScore!!)
                    lefuBodyDataList.add(PPBodyParam_smi!!)
                    lefuBodyDataList.add(PPBodyParam_WHR!!)
                    lefuBodyDataList.add(PPBodyParam_rightArmMuscleLevel!!)
                    lefuBodyDataList.add(PPBodyParam_leftArmMuscleLevel!!)
                    lefuBodyDataList.add(PPBodyParam_trunkMuscleLevel!!)
                    lefuBodyDataList.add(PPBodyParam_rightLegMuscleLevel!!)
                    lefuBodyDataList.add(PPBodyParam_leftLegMuscleLevel!!)
                    lefuBodyDataList.add(PPBodyParam_rightArmFatLevel!!)
                    lefuBodyDataList.add(PPBodyParam_leftArmFatLevel!!)
                    lefuBodyDataList.add(PPBodyParam_trunkFatLevel!!)
                    lefuBodyDataList.add(PPBodyParam_rightLegFatLevel!!)
                    lefuBodyDataList.add(PPBodyParam_leftLegFatLevel!!)
                    lefuBodyDataList.add(PPBodyParam_balanceArmsLevel!!)
                    lefuBodyDataList.add(PPBodyParam_balanceLegsLevel!!)
                    lefuBodyDataList.add(PPBodyParam_balanceArmLegLevel!!)
                    lefuBodyDataList.add(PPBodyParam_balanceFatArmsLevel!!)
                    lefuBodyDataList.add(PPBodyParam_balanceFatLegsLevel!!)
                    lefuBodyDataList.add(PPBodyParam_balanceFatArmLegLevel!!)
                }
            } else if (fatModel.ppSDKVersion?.startsWith("LF_X") == true) {
                if (context != null) {
                    lefuBodyDataList.add(PPBodyParam_Weight!!)
                    lefuBodyDataList.add(PPBodyParam_BMI!!)
                    lefuBodyDataList.add(PPBodyParam_BodyFat!!)
                    lefuBodyDataList.add(PPBodyParam_Bone!!)
                    lefuBodyDataList.add(PPBodyParam_Mus!!)
                    lefuBodyDataList.add(PPBodyParam_VisFat!!)
                    lefuBodyDataList.add(PPBodyParam_Water!!)
                    lefuBodyDataList.add(PPBodyParam_BMR!!)
                }
            } else {
                if (context != null) {
                    lefuBodyDataList.add(PPBodyParam_Weight!!)
                    lefuBodyDataList.add(PPBodyParam_BMI!!)
                }
            }
        }
        return PPBodyDetailInfoModelToJsonVo(errorType, lefuBodyDataList)
    }

    /**
     * 体重
     */
    fun fetchPPBodyParam_Weight(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppWeightKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_Weight.rawValue,
                fatModel.ppWeightKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    /**
     * 标准体重
     */
    fun fetchPPBodyParam_Bodystandard(): PPBodyDetailInfoModel? {
        val standardArray = listOf<Float>()
        val currentValue = fatModel.ppBodyStandardWeightKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_Bodystandard.rawValue,
                standardArray,
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    /**
     * 体重控制
     */
    fun fetchPPBodyParam_BodyControl(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppControlWeightKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BodyControl.rawValue,
                null,
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    /**
     * 去脂体重
     */
    fun fetchPPBodyParam_BodyLBW(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppLoseFatWeightKg
        val infoModel = context?.let {
            if (fatModel.ppSDKVersion?.startsWith(HT_8_) == true) {
                BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                    BodyParam.BodyParam_BodyLBW.rawValue,
                    fatModel.ppLoseFatWeightKgList,
                    currentValue,
                    true,
                    it
                )
            } else {
                BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                    BodyParam.BodyParam_BodyLBW.rawValue,
                    null,
                    currentValue,
                    false,
                    it
                )
            }
        }
        return infoModel
    }

    // 体脂率
    fun fetchPPBodyParam_BodyFat(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppFat
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BodyFat.rawValue,
                fatModel.ppFatList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 脂肪量
    fun fetchPPBodyParam_BodyFatKg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyfatKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BodyFatKg.rawValue,
                fatModel.ppBodyfatKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 水分率
    fun fetchPPBodyParam_Water(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppWaterPercentage
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_Water.rawValue,
                fatModel.ppWaterPercentageList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 肌肉率
    fun fetchPPBodyParam_MusRate(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMusclePercentage
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_MusRate.rawValue,
                fatModel.ppMusclePercentageList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 肌肉量
    fun fetchPPBodyParam_Mus(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_Mus.rawValue,
                fatModel.ppMuscleKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel

    }

    // 骨量
    fun fetchPPBodyParam_Bone(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBoneKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_Bone.rawValue,
                fatModel.ppBoneKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // BMR
    fun fetchPPBodyParam_BMR(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBMR.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BMR.rawValue,
                fatModel.ppBMRList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // BMI
    fun fetchPPBodyParam_BMI(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBMI

        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BMI.rawValue,
                fatModel.ppBMIList,
                currentValue,
                true,
                it
            )
        }
        return infoModel

    }

    // 内脏脂肪等级
    fun fetchPPBodyParam_VisFat(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppVisceralFat.toFloat()

        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_VisFat.rawValue,
                fatModel.ppVisceralFatList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 身体年龄
    fun fetchPPBodyParam_physicalAgeValue(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyAge.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_physicalAgeValue.rawValue,
                fatModel.ppBodyAgeList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 蛋白质率
    fun fetchPPBodyParam_proteinPercentage(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppProteinPercentage
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_proteinPercentage.rawValue,
                fatModel.ppProteinPercentageList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 身体类型
    fun fetchPPBodyParam_BodyType(): PPBodyDetailInfoModel? {
//        PPBodyDetailStandard.fetchPPBodyParam_BodyType_StandartArray()
        val currentValue = fatModel.ppBodyType?.type?.toFloat() ?: 0.0f
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BodyType.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 身体得分
    fun fetchPPBodyParam_BodyScore(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyScore.toFloat()

        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BodyScore.rawValue,
                fatModel.ppBodyScoreList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 皮下脂肪率
    fun fetchPPBodyParam_BodySubcutaneousFat(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatSubCutPercentage
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BodySubcutaneousFat.rawValue,
                fatModel.ppBodyFatSubCutPercentageList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 骨骼肌率
    fun fetchPPBodyParam_BodySkeletal(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodySkeletal
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BodySkeletal.rawValue,
                fatModel.ppBodySkeletalList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 骨骼肌量
    fun fetchPPBodyParam_SkeletalMuscleKg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodySkeletalKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_SkeletalMuscleKg.rawValue,
                fatModel.ppBodySkeletalKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 肌肉控制量
    fun fetchPPBodyParam_MuscleControl(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyMuscleControl

        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_MuscleControl.rawValue,
                null,
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 脂肪控制量
    fun fetchPPBodyParam_BodyControlLiang(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppFatControlKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BodyControlLiang.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 肥胖等级
    fun fetchPPBodyParam_FatGrade(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppFatGrade?.getType()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_FatGrade.rawValue,
                fatModel.ppFatGradeList,
                currentValue?.toFloat() ?:0f,
                true,
                it
            )
        }
        return infoModel
    }

    // 健康评估
    fun fetchPPBodyParam_BodyHealth(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyHealth?.getType()?.toFloat() ?: 0.0f
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_BodyHealth.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 心率
    fun fetchPPBodyParam_heart(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyBaseModel?.heartRate?.toFloat() ?: 0.0f
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_heart.rawValue,
                fatModel.ppHeartRateList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 体水分量
    fun fetchPPBodyParam_waterKg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppWaterKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_waterKg.rawValue,
                fatModel.ppWaterKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 蛋白质量
    fun fetchPPBodyParam_proteinKg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppProteinKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_proteinKg.rawValue,
                fatModel.ppProteinKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 皮下脂肪量
    fun fetchPPBodyParam_bodyFatSubCutKg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatSubCutKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatSubCutKg.rawValue,
                fatModel.ppBodyFatSubCutKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel

    }

    // 身体细胞量
    fun fetchPPBodyParam_cellMassKg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppCellMassKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_cellMassKg.rawValue,
                fatModel.ppCellMassKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 建议卡路里摄入量
    fun fetchPPBodyParam_dCI(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppDCI.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_dCI.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 无机盐量
    fun fetchPPBodyParam_mineralKg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMineralKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_mineralKg.rawValue,
                fatModel.ppMineralKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 肥胖度
    fun fetchPPBodyParam_obesity(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppObesity
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_obesity.rawValue,
                fatModel.ppObesityList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 细胞外水量
    fun fetchPPBodyParam_waterECWKg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppWaterECWKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_waterECWKg.rawValue,
                fatModel.ppWaterECWKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 细胞内水量
    fun fetchPPBodyParam_waterICWKg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppWaterICWKg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_waterICWKg.rawValue,
                fatModel.ppWaterICWKgList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 左手脂肪量
    fun fetchPPBodyParam_bodyFatKgLeftArm(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatKgLeftArm
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatKgLeftArm.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 左脚脂肪量
    fun fetchPPBodyParam_bodyFatKgLeftLeg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatKgLeftLeg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatKgLeftLeg.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 右手脂肪量
    fun fetchPPBodyParam_bodyFatKgRightArm(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatKgRightArm
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatKgRightArm.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 右脚脂肪量
    fun fetchPPBodyParam_bodyFatKgRightLeg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatKgRightLeg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatKgRightLeg.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 躯干脂肪量
    fun fetchPPBodyParam_bodyFatKgTrunk(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatKgTrunk
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatKgTrunk.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 左手脂肪率
    fun fetchPPBodyParam_bodyFatRateLeftArm(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatRateLeftArm
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatRateLeftArm.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 左脚脂肪率
    fun fetchPPBodyParam_bodyFatRateLeftLeg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatRateLeftLeg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatRateLeftLeg.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 右手脂肪率
    fun fetchPPBodyParam_bodyFatRateRightArm(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatRateRightArm
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatRateRightArm.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 右脚脂肪率
    fun fetchPPBodyParam_bodyFatRateRightLeg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatRateRightLeg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatRateRightLeg.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 躯干脂肪率
    fun fetchPPBodyParam_bodyFatRateTrunk(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppBodyFatRateTrunk
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_bodyFatRateTrunk.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 左手肌肉量
    fun fetchPPBodyParam_muscleKgLeftArm(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleKgLeftArm
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_muscleKgLeftArm.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 左脚肌肉量
    fun fetchPPBodyParam_muscleKgLeftLeg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleKgLeftLeg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_muscleKgLeftLeg.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 右手肌肉量
    fun fetchPPBodyParam_muscleKgRightArm(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleKgRightArm
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_muscleKgRightArm.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 右脚肌肉量
    fun fetchPPBodyParam_muscleKgRightLeg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleKgRightLeg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_muscleKgRightLeg.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 躯干肌肉量
    fun fetchPPBodyParam_muscleKgTrunk(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleKgTrunk
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_muscleKgTrunk.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 骨骼肌质量指数
    fun fetchPPBodyParam_ppSmi(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppSmi
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_smi.rawValue,
                fatModel.ppSmiList,
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 左手肌肉率(%), 分辨率0.1
    fun fetchPPBodyParam_muscleRateLeftArm(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleRateLeftArm
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_muscleRateLeftArm.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 左脚肌肉率(%), 分辨率0.1
    fun fetchPPBodyParam_muscleRateLeftLeg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleRateLeftLeg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_muscleRateLeftLeg.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 右手肌肉率(%), 分辨率0.1
    fun fetchPPBodyParam_muscleRateRightArm(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleRateRightArm
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_muscleRateRightArm.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 右脚肌肉率(%), 分辨率0.1
    fun fetchPPBodyParam_muscleRateRightLeg(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleRateRightLeg
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_muscleRateRightLeg.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 躯干肌肉率(%), 分辨率0.1
    fun fetchPPBodyParam_muscleRateTrunk(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppMuscleRateTrunk
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_muscleRateTrunk.rawValue,
                listOf(),
                currentValue,
                false,
                it
            )
        }
        return infoModel
    }

    // 腰臀比
    fun fetchPPBodyParam_WHR(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppWHR
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_WHR.rawValue,
                fatModel.ppWHRList,
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 右手肌肉标准
    fun fetchPPBodyParam_RightArmMuscleLevel(): PPBodyDetailInfoModel? {
        val currentValue = fatModel.ppRightArmMuscleLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_rightArmMuscleLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 左手肌肉标准
    fun fetchPPBodyParam_LeftArmMuscleLevel(): PPBodyDetailInfoModel? {
        val currentValue =  fatModel.ppLeftArmMuscleLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_leftArmMuscleLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 躯干肌肉标准
    fun fetchPPBodyParam_TrunkMuscleLevel(): PPBodyDetailInfoModel? {
        val currentValue =  fatModel.ppTrunkMuscleLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_trunkMuscleLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 右脚肌肉标准
    fun fetchPPBodyParam_RightLegMuscleLevel(): PPBodyDetailInfoModel? {
        val currentValue =  fatModel.ppRightLegMuscleLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_rightLegMuscleLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 左脚肌肉标准
    fun fetchPPBodyParam_LeftLegMuscleLevel(): PPBodyDetailInfoModel? {
        val currentValue =  fatModel.ppLeftLegMuscleLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_leftLegMuscleLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 右手脂肪标准
    fun fetchPPBodyParam_RightArmFatLevel(): PPBodyDetailInfoModel? {
         val currentValue =  fatModel.ppRightArmFatLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_rightArmFatLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 左手脂肪标准
    fun fetchPPBodyParam_LeftArmFatLevel(): PPBodyDetailInfoModel? {
         val currentValue =  fatModel.ppLeftArmFatLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_leftArmFatLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 躯干脂肪标准
    fun fetchPPBodyParam_TrunkFatLevel(): PPBodyDetailInfoModel? {
         val currentValue =  fatModel.ppTrunkFatLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_trunkFatLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 右脚脂肪标准
    fun fetchPPBodyParam_RightLegFatLevel(): PPBodyDetailInfoModel? {
         val currentValue =  fatModel.ppRightLegFatLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_rightLegFatLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 左脚脂肪标准
    fun fetchPPBodyParam_LeftLegFatLevel(): PPBodyDetailInfoModel? {
         val currentValue =  fatModel.ppLeftLegFatLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_leftLegFatLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 上肢肌肉均衡
    fun fetchPPBodyParam_BalanceArmsLevel(): PPBodyDetailInfoModel? {
        val currentValue =  fatModel.ppBalanceArmsLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_balanceArmsLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 下肢肌肉均衡
    fun fetchPPBodyParam_BalanceLegsLevel(): PPBodyDetailInfoModel? {
        val currentValue =  fatModel.ppBalanceLegsLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_balanceLegsLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 肌肉-上下均衡度
    fun fetchPPBodyParam_BalanceArmLegLevel(): PPBodyDetailInfoModel? {
        val currentValue =  fatModel.ppBalanceArmLegLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_balanceArmLegLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 上肢脂肪均衡
    fun fetchPPBodyParam_BalanceFatArmsLevel(): PPBodyDetailInfoModel? {
        val currentValue =  fatModel.ppBalanceFatArmsLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_balanceFatArmsLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 下肢脂肪均衡
    fun fetchPPBodyParam_BalanceFatLegsLevel(): PPBodyDetailInfoModel? {
        val currentValue =  fatModel.ppBalanceFatLegsLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_balanceFatLegsLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    // 脂肪-上下均衡度
    fun fetchPPBodyParam_BalanceFatArmLegLevel(): PPBodyDetailInfoModel? {
        val currentValue =  fatModel.ppBalanceFatArmLegLevel.toFloat()
        val infoModel = context?.let {
            BuildPPBodyDetailModelUtils.buildPPBodyDetailModel(
                BodyParam.BodyParam_balanceFatArmLegLevel.rawValue,
                listOf(),
                currentValue,
                true,
                it
            )
        }
        return infoModel
    }

    fun XM_ShowDataEnuArray(SDKVersion: String): Array<BodyParam> {
        val XM_ShowDataEnuArray: Array<BodyParam>
        if (SDKVersion.startsWith("HT_4") || SDKVersion.startsWith("LF_4")) {
            XM_ShowDataEnuArray = arrayOf(
                BodyParam.BodyParam_Weight,
                BodyParam.BodyParam_BodyFat,
                BodyParam.BodyParam_BMI,
                BodyParam.BodyParam_Mus,
                BodyParam.BodyParam_BMR,
                BodyParam.BodyParam_Water,
                BodyParam.BodyParam_heart,
                BodyParam.BodyParam_BodyFatKg,
                BodyParam.BodyParam_proteinPercentage,
                BodyParam.BodyParam_BodyLBW,
                BodyParam.BodyParam_BodySubcutaneousFat,
                BodyParam.BodyParam_BodySkeletal,
                BodyParam.BodyParam_VisFat,
                BodyParam.BodyParam_MusRate,
                BodyParam.BodyParam_MuscleControl,
                BodyParam.BodyParam_BodyControlLiang,
                BodyParam.BodyParam_Bodystandard,
                BodyParam.BodyParam_BodyControl,
                BodyParam.BodyParam_Bone,
                BodyParam.BodyParam_BodyType,
                BodyParam.BodyParam_FatGrade,
                BodyParam.BodyParam_BodyHealth,
                BodyParam.BodyParam_physicalAgeValue,
                BodyParam.BodyParam_BodyScore
            )
        } else if (SDKVersion.startsWith("HT_8")) {
            XM_ShowDataEnuArray = arrayOf(
                BodyParam.BodyParam_Weight,
                BodyParam.BodyParam_BodyFat,
                BodyParam.BodyParam_BMI,
                BodyParam.BodyParam_Mus,
                BodyParam.BodyParam_BMR,
                BodyParam.BodyParam_dCI,
                BodyParam.BodyParam_heart,
                BodyParam.BodyParam_Water,
                BodyParam.BodyParam_proteinPercentage,
                BodyParam.BodyParam_waterKg,
                BodyParam.BodyParam_proteinKg,
                BodyParam.BodyParam_BodyFatKg,
                BodyParam.BodyParam_BodyLBW,
                BodyParam.BodyParam_BodySubcutaneousFat,
                BodyParam.BodyParam_BodySkeletal,
                BodyParam.BodyParam_VisFat,
                BodyParam.BodyParam_Bone,
                BodyParam.BodyParam_bodyFatSubCutKg,
                BodyParam.BodyParam_MusRate,
                BodyParam.BodyParam_bodyFatRateLeftArm,
                BodyParam.BodyParam_cellMassKg,
                BodyParam.BodyParam_bodyFatRateRightArm,
                BodyParam.BodyParam_cellMassKg,
                BodyParam.BodyParam_bodyFatRateTrunk,
                BodyParam.BodyParam_waterECWKg,
                BodyParam.BodyParam_bodyFatRateLeftLeg,
                BodyParam.BodyParam_mineralKg,
                BodyParam.BodyParam_bodyFatRateRightLeg,
                BodyParam.BodyParam_muscleKgLeftArm,
                BodyParam.BodyParam_bodyFatKgLeftArm,
                BodyParam.BodyParam_muscleKgRightArm,
                BodyParam.BodyParam_bodyFatKgRightArm,
                BodyParam.BodyParam_muscleKgTrunk,
                BodyParam.BodyParam_bodyFatKgTrunk,
                BodyParam.BodyParam_muscleKgLeftLeg,
                BodyParam.BodyParam_bodyFatKgLeftLeg,
                BodyParam.BodyParam_muscleKgRightLeg,
                BodyParam.BodyParam_bodyFatKgRightLeg,
                BodyParam.BodyParam_MuscleControl,
                BodyParam.BodyParam_BodyControlLiang,
                BodyParam.BodyParam_Bodystandard,
                BodyParam.BodyParam_BodyControlLiang,
                BodyParam.BodyParam_obesity,
                BodyParam.BodyParam_BodyType,
                BodyParam.BodyParam_FatGrade,
                BodyParam.BodyParam_BodyHealth,
                BodyParam.BodyParam_physicalAgeValue,
                BodyParam.BodyParam_BodyScore
            )
        } else {
            XM_ShowDataEnuArray = arrayOf(
                BodyParam.BodyParam_Weight,
                BodyParam.BodyParam_BMI
            )
        }
        return XM_ShowDataEnuArray
    }

}



