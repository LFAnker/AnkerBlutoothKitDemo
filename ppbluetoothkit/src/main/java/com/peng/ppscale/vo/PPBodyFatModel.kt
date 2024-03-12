package com.peng.ppscale.vo

import com.peng.ppscale.data.PPBodyDetailModel
import com.peng.ppscale.util.Logger
import com.peng.ppscale.calcute.CalculateManager

open class PPBodyFatModel {

    //用于存储计算所必须的参数
    var ppBodyBaseModel: PPBodyBaseModel? = null
    var ppSDKVersion: String? = null//计算库版本号


    // 性别
    var ppSex: PPUserGender = ppBodyBaseModel?.userModel?.sex ?: PPUserGender.PPUserGenderFemale

    // 身高(cm)
    var ppHeightCm: Int = ppBodyBaseModel?.userModel?.userHeight ?: 100

    // 年龄(岁)
    var ppAge: Int = ppBodyBaseModel?.userModel?.age ?: 0

    // 体脂错误类型
    var errorType: BodyFatErrorType = BodyFatErrorType.PP_ERROR_TYPE_NONE

    // 数据区间范围和介绍描述
    var bodyDetailModel: PPBodyDetailModel? = null

    /**************** 四电极算法 ****************************/
    // 体重(kg)
    var ppWeightKg: Float = (ppBodyBaseModel?.weight?.toFloat() ?: 0.0f).div(100.0f)

    var ppWeightKgList: List<Float>? = null

    // Body Mass Index
    var ppBMI: Float = 0f
    var ppBMIList: List<Float>? = null

    // 脂肪率(%)
    var ppFat: Float = 0f
    var ppFatList: List<Float>? = null

    // 脂肪量(kg)
    var ppBodyfatKg: Float = 0f
    var ppBodyfatKgList: List<Float>? = null

    // 肌肉率(%)
    var ppMusclePercentage: Float = 0f
    var ppMusclePercentageList: List<Float>? = null

    // 肌肉量(kg)
    var ppMuscleKg: Float = 0f
    var ppMuscleKgList: List<Float>? = null

    // 骨骼肌率(%)
    var ppBodySkeletal: Float = 0f
    var ppBodySkeletalList: List<Float>? = null

    // 骨骼肌量(kg)
    var ppBodySkeletalKg: Float = 0f
    var ppBodySkeletalKgList: List<Float>? = null

    // 水分率(%), 分辨率0.1,
    var ppWaterPercentage: Float = 0f
    var ppWaterPercentageList: List<Float>? = null

    //水分量(Kg)
    var ppWaterKg: Float = 0f
    var ppWaterKgList: List<Float>? = null

    // 蛋白质率(%)
    var ppProteinPercentage: Float = 0f
    var ppProteinPercentageList: List<Float>? = null

    //蛋白质量(Kg)
    var ppProteinKg: Float = 0f
    var ppProteinKgList: List<Float>? = null

    // 去脂体重(kg)
    var ppLoseFatWeightKg: Float = 0f
    var ppLoseFatWeightKgList: List<Float>? = null

    // 皮下脂肪率(%)
    var ppBodyFatSubCutPercentage: Float = 0f
    var ppBodyFatSubCutPercentageList: List<Float>? = null

    // 皮下脂肪量
    var ppBodyFatSubCutKg: Float = 0f
    var ppBodyFatSubCutKgList: List<Float>? = null

    // 心率(bmp)
    var ppHeartRate: Int = ppBodyBaseModel?.heartRate ?: 0
    var ppHeartRateList: List<Float>? = null

    // 基础代谢
    var ppBMR: Int = 0
    var ppBMRList: List<Float>? = null

    // 内脏脂肪等级
    var ppVisceralFat: Int = 0
    var ppVisceralFatList: List<Float>? = null

    // 骨量(kg)
    var ppBoneKg: Float = 0f
    var ppBoneKgList: List<Float>? = null

    // 肌肉控制量(kg)
    var ppBodyMuscleControl: Float = 0f

    // 脂肪控制量(kg)
    var ppFatControlKg: Float = 0f

    // 标准体重
    var ppBodyStandardWeightKg: Float = 0f

    // 理想体重(kg)
    var ppIdealWeightKg: Float = 0f

    // 控制体重(kg)
    var ppControlWeightKg: Float = 0f

    // 身体类型
    var ppBodyType: PPBodyDetailType? = null

    // 肥胖等级
    var ppFatGrade: PPBodyFatGrade? = null
    var ppFatGradeList: List<Float>? = null

    // 健康评估
    var ppBodyHealth: PPBodyHealthAssessment? = null
    var ppBodyHealthList: List<Float>? = null

    // 身体年龄
    var ppBodyAge: Int = 0
    var ppBodyAgeList: List<Float>? = null

    // 身体得分
    var ppBodyScore: Int = 0
    var ppBodyScoreList: List<Float>? = null

    /**************** 八电极算法独有 ****************************/

    // 輸出參數-全身体组成:身体细胞量(kg)
    var ppCellMassKg: Float = 0.0f
    var ppCellMassKgList: List<Float> = listOf()

    // 輸出參數-评价建议:建议卡路里摄入量 Kcal/day
    var ppDCI: Int = 0

    // 輸出參數-全身体组成:无机盐量(Kg)
    var ppMineralKg: Float = 0.0f
    var ppMineralKgList: List<Float> = listOf()

    // 輸出參數-评价建议: 肥胖度(%)
    var ppObesity: Float = 0.0f
    var ppObesityList: List<Float> = listOf()

    // 輸出參數-全身体组成:细胞外水量(kg)
    var ppWaterECWKg: Float = 0.0f
    var ppWaterECWKgList: List<Float> = listOf()

    // 輸出參數-全身体组成:细胞内水量(kg)
    var ppWaterICWKg: Float = 0.0f
    var ppWaterICWKgList: List<Float> = listOf()

    // 左手脂肪量(%), 分辨率0.1
    var ppBodyFatKgLeftArm: Float = 0.0f

    // 左脚脂肪量(%), 分辨率0.1
    var ppBodyFatKgLeftLeg: Float = 0.0f

    // 右手脂肪量(%), 分辨率0.1
    var ppBodyFatKgRightArm: Float = 0.0f

    // 右脚脂肪量(%), 分辨率0.1
    var ppBodyFatKgRightLeg: Float = 0.0f

    // 躯干脂肪量(%), 分辨率0.1
    var ppBodyFatKgTrunk: Float = 0.0f

    // 左手脂肪率(%), 分辨率0.1
    var ppBodyFatRateLeftArm: Float = 0.0f

    // 左脚脂肪率(%), 分辨率0.1
    var ppBodyFatRateLeftLeg: Float = 0.0f

    // 右手脂肪率(%), 分辨率0.1
    var ppBodyFatRateRightArm: Float = 0.0f

    // 右脚脂肪率(%), 分辨率0.1
    var ppBodyFatRateRightLeg: Float = 0.0f

    // 躯干脂肪率(%), 分辨率0.1
    var ppBodyFatRateTrunk: Float = 0.0f

    // 左手肌肉量(kg), 分辨率0.1shi
    var ppMuscleKgLeftArm: Float = 0.0f

    // 左脚肌肉量(kg), 分辨率0.1
    var ppMuscleKgLeftLeg: Float = 0.0f

    // 右手肌肉量(kg), 分辨率0.1
    var ppMuscleKgRightArm: Float = 0.0f

    // 右脚肌肉量(kg), 分辨率0.1
    var ppMuscleKgRightLeg: Float = 0.0f

    // 躯干肌肉量(kg), 分辨率0.1
    var ppMuscleKgTrunk: Float = 0.0f

    //左手肌肉率(%), 分辨率0.1
    var ppMuscleRateLeftArm: Float = 0.0f

    //左脚肌肉率(%), 分辨率0.1
    var ppMuscleRateLeftLeg: Float = 0.0f

    //右手肌肉率(%), 分辨率0.1
    var ppMuscleRateRightArm: Float = 0.0f

    //右脚肌肉率(%), 分辨率0.1
    var ppMuscleRateRightLeg: Float = 0.0f

    //躯干肌肉率(%), 分辨率0.1
    var ppMuscleRateTrunk: Float = 0.0f

    // 骨骼肌质量指数
    var ppSmi: Float = 0.0f
    var ppSmiList: List<Float> = listOf()

    // 腰臀比
    var ppWHR: Float = 0.0f

    // 腰臀比列表
    var ppWHRList: List<Float> = listOf()

    /**************** V1.6.8 八电极新增16项数据 ****************************/
    // 右手肌肉标准
    var ppRightArmMuscleLevel: Int = 0

    // 左手肌肉标准
    var ppLeftArmMuscleLevel: Int = 0

    //躯干肌肉标准
    var ppTrunkMuscleLevel: Int = 0

    //右脚肌肉标准
    var ppRightLegMuscleLevel: Int = 0

    //左脚肌肉标准
    var ppLeftLegMuscleLevel: Int = 0

    //右手脂肪标准
    var ppRightArmFatLevel: Int = 0

    //左手脂肪标准
    var ppLeftArmFatLevel: Int = 0

    //躯干脂肪标准
    var ppTrunkFatLevel: Int = 0

    //右脚脂肪标准
    var ppRightLegFatLevel: Int = 0

    //左脚脂肪标准
    var ppLeftLegFatLevel: Int = 0

    //上肢肌肉均衡
    var ppBalanceArmsLevel: Int = 0

    //下肢肌肉均衡
    var ppBalanceLegsLevel: Int = 0

    //肌肉-上下均衡度
    var ppBalanceArmLegLevel: Int = 0

    //上肢脂肪均衡
    var ppBalanceFatArmsLevel: Int = 0

    //下肢脂肪均衡
    var ppBalanceFatLegsLevel: Int = 0

    //脂肪-上下均衡度
    var ppBalanceFatArmLegLevel: Int = 0


    /**************** 闭目单脚功能 ****************************/
    // 闭目单脚站立时间
    var ppStandTime: Int = 0

    constructor(bodyBaseModel: PPBodyBaseModel) {
        this.ppBodyBaseModel = bodyBaseModel
//        this.ppWeightKg = bodyBaseModel.getPpWeightKg()
        this.ppSex = bodyBaseModel.userModel?.sex ?: PPUserGender.PPUserGenderFemale
        this.ppHeightCm = bodyBaseModel.userModel?.userHeight ?: 100
        this.ppAge = bodyBaseModel.userModel?.age ?: 0
        this.ppWeightKg = (bodyBaseModel.weight.toFloat()).div(100.0f)
        initWithUserModel(bodyBaseModel)
    }

    fun initWithUserModel(bodyBaseModel: PPBodyBaseModel) {
        ppHeartRate = bodyBaseModel.heartRate
        CalculateManager.calculateData(bodyBaseModel, this)
        Logger.d(toString())
    }

    override fun toString(): String {
        if (ppSDKVersion?.startsWith(LF_X_L_16) ?: false) {
            return "PPBodyFatModel(ppBodyBaseModel=$ppBodyBaseModel," + "\n" +
                    "ppSDKVersion=$ppSDKVersion, " + "\n" +
                    "ppSex=$ppSex, " + "\n" +
                    "ppHeightCm=$ppHeightCm, " + "\n" +
                    "ppAge=$ppAge, " + "\n" +
                    "errorType=$errorType, " + "\n" +
                    "ppWeightKg=$ppWeightKg, " + "\n" +
                    "ppBMI=$ppBMI, " + "\n" +
                    "ppFat=$ppFat, " + "\n" +
                    "ppMuscleKg=$ppMuscleKg, " + "\n" +
                    "ppWaterPercentage=$ppWaterPercentage, " + "\n" +
                    "ppWaterKg=$ppWaterKg, " + "\n" +
                    "ppBMR=$ppBMR, " + "\n" +
                    "ppBoneKg=$ppBoneKg, " + "\n" +
                    "ppVisceralFat=$ppVisceralFat)"
        } else if (ppSDKVersion?.startsWith(LF_X_L_17) ?: false) {
            return "PPBodyFatModel(ppBodyBaseModel=$ppBodyBaseModel," + "\n" +
                    "ppSDKVersion=$ppSDKVersion, " + "\n" +
                    "ppSex=$ppSex, " + "\n" +
                    "ppHeightCm=$ppHeightCm, " + "\n" +
                    "ppAge=$ppAge, " + "\n" +
                    "errorType=$errorType, " + "\n" +
                    "ppWeightKg=$ppWeightKg, " + "\n" +
                    "ppBMI=$ppBMI, " + "\n" +
                    "ppFat=$ppFat, " + "\n" +
                    "ppMuscleKg=$ppMuscleKg, " + "\n" +
                    "ppWaterPercentage=$ppWaterPercentage, " + "\n" +
                    "ppWaterKg=$ppWaterKg, " + "\n" +
                    "ppBMR=$ppBMR, " + "\n" +
                    "ppBoneKg=$ppBoneKg, " + "\n" +
                    "ppBodyAge=$ppBodyAge, " + "\n" +
                    "ppVisceralFat=$ppVisceralFat)"
        } else if (ppSDKVersion?.startsWith(HT_8_) ?: false) {
            return "PPBodyFatModel(ppBodyBaseModel=$ppBodyBaseModel," + "\n" +
                    "ppSDKVersion=$ppSDKVersion, " + "\n" +
                    "ppSex=$ppSex, " + "\n" +
                    "ppHeightCm=$ppHeightCm, " + "\n" +
                    "ppAge=$ppAge, " + "\n" +
                    "errorType=$errorType, " + "\n" +
                    "ppWeightKg=$ppWeightKg, " + "\n" +
//                    "ppWeightKgList=$ppWeightKgList, " + "\n" +
                    "ppBMI=$ppBMI, " + "\n" +
//                    "ppBMIList=$ppBMIList, " + "\n" +
                    "ppFat=$ppFat, " + "\n" +
//                    "ppFatList=$ppFatList, " + "\n" +
                    "ppBodyfatKg=$ppBodyfatKg, " + "\n" +
//                    "ppBodyfatKgList=$ppBodyfatKgList, " + "\n" +
                    "ppMusclePercentage=$ppMusclePercentage, " + "\n" +
//                    "ppMusclePercentageList=$ppMusclePercentageList, " + "\n" +
                    "ppMuscleKg=$ppMuscleKg, " + "\n" +
//                    "ppMuscleKgList=$ppMuscleKgList, " + "\n" +
                    "ppBodySkeletal=$ppBodySkeletal, " + "\n" +
//                    "ppBodySkeletalList=$ppBodySkeletalList, " + "\n" +
                    "ppBodySkeletalKg=$ppBodySkeletalKg, " + "\n" +
//                    "ppBodySkeletalKgList=$ppBodySkeletalKgList, " + "\n" +
                    "ppWaterPercentage=$ppWaterPercentage, " + "\n" +
//                    "ppWaterPercentageList=$ppWaterPercentageList, " + "\n" +
                    "ppWaterKg=$ppWaterKg, " + "\n" +
//                    "ppWaterKgList=$ppWaterKgList, " + "\n" +
                    "ppProteinPercentage=$ppProteinPercentage, " + "\n" +
//                    "ppProteinPercentageList=$ppProteinPercentageList, " + "\n" +
                    "ppProteinKg=$ppProteinKg, " + "\n" +
//                    "ppProteinKgList=$ppProteinKgList, " + "\n" +
                    "ppLoseFatWeightKg=$ppLoseFatWeightKg, " + "\n" +
//                    "ppLoseFatWeightKgList=$ppLoseFatWeightKgList, " + "\n" +
                    "ppBodyFatSubCutPercentage=$ppBodyFatSubCutPercentage, " + "\n" +
//                    "ppBodyFatSubCutPercentageList=$ppBodyFatSubCutPercentageList, " + "\n" +
                    "ppBodyFatSubCutKg=$ppBodyFatSubCutKg, " + "\n" +
//                    "ppBodyFatSubCutKgList=$ppBodyFatSubCutKgList, " + "\n" +
                    "ppHeartRate=$ppHeartRate, " + "\n" +
//                    "ppHeartRateList=$ppHeartRateList, " + "\n" +
                    "ppBMR=$ppBMR, " + "\n" +
//                    "ppBMRList=$ppBMRList, " + "\n" +
                    "ppVisceralFat=$ppVisceralFat, " + "\n" +
//                    "ppVisceralFatList=$ppVisceralFatList, " + "\n" +
                    "ppBoneKg=$ppBoneKg, " + "\n" +
//                    "ppBoneKgList=$ppBoneKgList, " + "\n" +
                    "ppBodyMuscleControl=$ppBodyMuscleControl, " + "\n" +
                    "ppFatControlKg=$ppFatControlKg, " + "\n" +
                    "ppBodyStandardWeightKg=$ppBodyStandardWeightKg, " + "\n" +
                    "ppIdealWeightKg=$ppIdealWeightKg, " + "\n" +
                    "ppControlWeightKg=$ppControlWeightKg, " + "\n" +
                    "ppBodyType=$ppBodyType, " + "\n" +
                    "ppFatGrade=$ppFatGrade, " + "\n" +
//                    "ppFatGradeList=$ppFatGradeList, " + "\n" +
                    "ppBodyHealth=$ppBodyHealth, " + "\n" +
//                    "ppBodyHealthList=$ppBodyHealthList, " + "\n" +
                    "ppBodyAge=$ppBodyAge, " + "\n" +
//                    "ppBodyAgeList=$ppBodyAgeList, " + "\n" +
                    "ppBodyScore=$ppBodyScore, " + "\n" +
//                    "ppBodyScoreList=$ppBodyScoreList, " + "\n" +
                    "ppCellMassKg=$ppCellMassKg, " + "\n" +
//                    "ppCellMassKgList=$ppCellMassKgList, " + "\n" +
                    "ppDCI=$ppDCI, " + "\n" +
                    "ppMineralKg=$ppMineralKg, " + "\n" +
//                    "ppMineralKgList=$ppMineralKgList, " + "\n" +
                    "ppObesity=$ppObesity, " + "\n" +
//                    "ppObesityList=$ppObesityList, " + "\n" +
                    "ppWaterECWKg=$ppWaterECWKg, " + "\n" +
                    "ppWaterECWKgList=$ppWaterECWKgList, " + "\n" +
                    "ppWaterICWKg=$ppWaterICWKg, " + "\n" +
//                    "ppWaterICWKgList=$ppWaterICWKgList, " + "\n" +
                    "ppBodyFatKgLeftArm=$ppBodyFatKgLeftArm, " + "\n" +
                    "ppBodyFatKgLeftLeg=$ppBodyFatKgLeftLeg, " + "\n" +
                    "ppBodyFatKgRightArm=$ppBodyFatKgRightArm, " + "\n" +
                    "ppBodyFatKgRightLeg=$ppBodyFatKgRightLeg, " + "\n" +
                    "ppBodyFatKgTrunk=$ppBodyFatKgTrunk, " + "\n" +
                    "ppBodyFatRateLeftArm=$ppBodyFatRateLeftArm, " + "\n" +
                    "ppBodyFatRateLeftLeg=$ppBodyFatRateLeftLeg, " + "\n" +
                    "ppBodyFatRateRightArm=$ppBodyFatRateRightArm, " + "\n" +
                    "ppBodyFatRateRightLeg=$ppBodyFatRateRightLeg, " + "\n" +
                    "ppBodyFatRateTrunk=$ppBodyFatRateTrunk, " + "\n" +
                    "ppMuscleKgLeftArm=$ppMuscleKgLeftArm, " + "\n" +
                    "ppMuscleKgLeftLeg=$ppMuscleKgLeftLeg, " + "\n" +
                    "ppMuscleKgRightArm=$ppMuscleKgRightArm, " + "\n" +
                    "ppMuscleKgRightLeg=$ppMuscleKgRightLeg, " + "\n" +
                    "ppMuscleKgTrunk=$ppMuscleKgTrunk, " + "\n" +
                    "ppRightArmMuscleLevel=$ppRightArmMuscleLevel, " + "\n" +
                    "ppLeftArmMuscleLevel=$ppLeftArmMuscleLevel, " + "\n" +
                    "ppTrunkMuscleLevel=$ppTrunkMuscleLevel, " + "\n" +
                    "ppRightLegMuscleLevel=$ppRightLegMuscleLevel, " + "\n" +
                    "ppLeftLegMuscleLevel=$ppLeftLegMuscleLevel, " + "\n" +
                    "ppRightArmFatLevel=$ppRightArmFatLevel, " + "\n" +
                    "ppLeftArmFatLevel=$ppLeftArmFatLevel, " + "\n" +
                    "ppTrunkFatLevel=$ppTrunkFatLevel, " + "\n" +
                    "ppRightLegFatLevel=$ppRightLegFatLevel, " + "\n" +
                    "ppLeftLegFatLevel=$ppLeftLegFatLevel, " + "\n" +
                    "ppBalanceArmsLevel=$ppBalanceArmsLevel, " + "\n" +
                    "ppBalanceLegsLevel=$ppBalanceLegsLevel, " + "\n" +
                    "ppBalanceArmLegLevel=$ppBalanceArmLegLevel, " + "\n" +
                    "ppBalanceFatArmsLevel=$ppBalanceFatArmsLevel, " + "\n" +
                    "ppBalanceFatLegsLevel=$ppBalanceFatLegsLevel, " + "\n" +
                    "ppBalanceFatArmLegLevel=$ppBalanceFatArmLegLevel, " + "\n" +
                    "ppStandTime=$ppStandTime)"
        } else {
            return "PPBodyFatModel(ppBodyBaseModel=$ppBodyBaseModel," + "\n" +
                    "ppSDKVersion=$ppSDKVersion, " + "\n" +
                    "ppSex=$ppSex, " + "\n" +
                    "ppHeightCm=$ppHeightCm, " + "\n" +
                    "ppAge=$ppAge, " + "\n" +
                    "errorType=$errorType, " + "\n" +
                    "ppWeightKg=$ppWeightKg, " + "\n" +
                    "ppWeightKgList=$ppWeightKgList, " + "\n" +
                    "ppBMI=$ppBMI, " + "\n" +
                    "ppBMIList=$ppBMIList, " + "\n" +
                    "ppFat=$ppFat, " + "\n" +
                    "ppFatList=$ppFatList, " + "\n" +
                    "ppBodyfatKg=$ppBodyfatKg, " + "\n" +
                    "ppBodyfatKgList=$ppBodyfatKgList, " + "\n" +
                    "ppMusclePercentage=$ppMusclePercentage, " + "\n" +
                    "ppMusclePercentageList=$ppMusclePercentageList, " + "\n" +
                    "ppMuscleKg=$ppMuscleKg, " + "\n" +
                    "ppMuscleKgList=$ppMuscleKgList, " + "\n" +
                    "ppBodySkeletal=$ppBodySkeletal, " + "\n" +
                    "ppBodySkeletalList=$ppBodySkeletalList, " + "\n" +
                    "ppBodySkeletalKg=$ppBodySkeletalKg, " + "\n" +
                    "ppBodySkeletalKgList=$ppBodySkeletalKgList, " + "\n" +
                    "ppWaterPercentage=$ppWaterPercentage, " + "\n" +
                    "ppWaterPercentageList=$ppWaterPercentageList, " + "\n" +
                    "ppWaterKg=$ppWaterKg, " + "\n" +
                    "ppWaterKgList=$ppWaterKgList, " + "\n" +
                    "ppProteinPercentage=$ppProteinPercentage, " + "\n" +
                    "ppProteinPercentageList=$ppProteinPercentageList, " + "\n" +
                    "ppProteinKg=$ppProteinKg, " + "\n" +
                    "ppProteinKgList=$ppProteinKgList, " + "\n" +
                    "ppLoseFatWeightKg=$ppLoseFatWeightKg, " + "\n" +
                    "ppLoseFatWeightKgList=$ppLoseFatWeightKgList, " + "\n" +
                    "ppBodyFatSubCutPercentage=$ppBodyFatSubCutPercentage, " + "\n" +
                    "ppBodyFatSubCutPercentageList=$ppBodyFatSubCutPercentageList, " + "\n" +
                    "ppBodyFatSubCutKg=$ppBodyFatSubCutKg, " + "\n" +
                    "ppBodyFatSubCutKgList=$ppBodyFatSubCutKgList, " + "\n" +
                    "ppHeartRate=$ppHeartRate, " + "\n" +
                    "ppHeartRateList=$ppHeartRateList, " + "\n" +
                    "ppBMR=$ppBMR, " + "\n" +
                    "ppBMRList=$ppBMRList, " + "\n" +
                    "ppVisceralFat=$ppVisceralFat, " + "\n" +
                    "ppVisceralFatList=$ppVisceralFatList, " + "\n" +
                    "ppBoneKg=$ppBoneKg, " + "\n" +
                    "ppBoneKgList=$ppBoneKgList, " + "\n" +
                    "ppBodyMuscleControl=$ppBodyMuscleControl, " + "\n" +
                    "ppFatControlKg=$ppFatControlKg, " + "\n" +
                    "ppBodyStandardWeightKg=$ppBodyStandardWeightKg, " + "\n" +
                    "ppIdealWeightKg=$ppIdealWeightKg, " + "\n" +
                    "ppControlWeightKg=$ppControlWeightKg, " + "\n" +
                    "ppBodyType=$ppBodyType, " + "\n" +
                    "ppFatGrade=$ppFatGrade, " + "\n" +
                    "ppFatGradeList=$ppFatGradeList, " + "\n" +
                    "ppBodyHealth=$ppBodyHealth, " + "\n" +
                    "ppBodyHealthList=$ppBodyHealthList, " + "\n" +
                    "ppBodyAge=$ppBodyAge, " + "\n" +
                    "ppBodyAgeList=$ppBodyAgeList, " + "\n" +
                    "ppBodyScore=$ppBodyScore, " + "\n" +
                    "ppBodyScoreList=$ppBodyScoreList)"
        }
    }
}
