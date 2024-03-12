package com.peng.ppscale.data

import android.content.Context
import com.lefu.gson.Gson
import com.peng.ppscale.util.Logger
import com.peng.ppscale.util.ReadJsonUtils
import org.json.JSONObject

/**
 *    @author : whs
 *    e-mail : haisilen@163.com
 *    date   : 2023/6/19 13:53
 *    desc   : 构建各种指标的范围工具
 */
object BuildPPBodyDetailModelUtils {

    //当前选中的json数据 比如选中BodyParam_BMI,就是BodyParam_BMI下的json
    var currentBodyParamJson: String = ""

    //所有的json
    var bodyParamJsonH8: String = ""
    var bodyParamJsonH4: String = ""

    val map48 = HashMap<String, BodyParamVo?>()
    val map24 = HashMap<String, BodyParamVo?>()

    var gson: Gson? = null

    //是否是48项数据(八电极)
    var ppSDKVersion: String? = null//计算库版本号

    /**
     * @param bodyParamKey 指标的key值 eq BodyParam_BMI
     * @param standardArray 该指标的标准范围集合
     * @param currentValue 当前指标的值
     * @param hasStandard 是否有标准范围
     */
    fun buildPPBodyDetailModel(
        bodyParamKey: String,
        standardArray: List<Float>?,
        currentValue: Float,
        hasStandard: Boolean,
        context: Context
    ): PPBodyDetailInfoModel {
        val ppBodyDetailInfoModel = PPBodyDetailInfoModel()
        var bodyParamVo: BodyParamVo? = null
        if (ppSDKVersion?.startsWith("HT_8") == true) {
            if (bodyParamJsonH8.isEmpty()) {
                bodyParamJsonH8 =
                    ReadJsonUtils.readLanguageJsonFromAssets(context, "BodyParam_HT_8.json")
            }
            bodyParamVo = map48.get(bodyParamKey)
            if (bodyParamVo == null) {
                val json = JSONObject(bodyParamJsonH8)
                val bodyParam = json.optJSONObject(bodyParamKey)
                if (gson == null) {
                    gson = Gson()
                }
                bodyParamVo = gson?.fromJson(bodyParam?.toString() ?: "", BodyParamVo::class.java)
                map48.put(bodyParamKey, bodyParamVo)
            }
        } else {
            if (bodyParamJsonH4.isEmpty()) {
                bodyParamJsonH4 =
                    ReadJsonUtils.readLanguageJsonFromAssets(context, "BodyParam.json")
            }
            bodyParamVo = map24.get(bodyParamKey)
            if (bodyParamVo == null) {
                val json = JSONObject(bodyParamJsonH4)
                val bodyParam = json.optJSONObject(bodyParamKey)
                if (gson == null) {
                    gson = Gson()
                }
                bodyParamVo = gson?.fromJson(bodyParam?.toString() ?: "", BodyParamVo::class.java)
                map24.put(bodyParamKey, bodyParamVo)
            }
        }
        if (bodyParamVo != null) {
            ppBodyDetailInfoModel.currentValue = currentValue
            ppBodyDetailInfoModel.bodyParamNameString = bodyParamVo.bodyParamNameString
            ppBodyDetailInfoModel.bodyParamKey = bodyParamVo.bodyParamKey
            ppBodyDetailInfoModel.unit = bodyParamVo.unit
            bodyParamVo.watchfulArray?.let {
                if (it.isNotEmpty()) {
                    ppBodyDetailInfoModel.watchfulArray = it
                }
            }
            bodyParamVo.suggestionArray?.let {
                if (it.isNotEmpty()) {
                    ppBodyDetailInfoModel.suggestionArray = it
                }
            }
            bodyParamVo.evaluationArray?.let {
                if (it.isNotEmpty()) {
                    ppBodyDetailInfoModel.evaluationArray = it
                }
            }
            bodyParamVo.standardTitleArray?.let {
                if (it.isNotEmpty()) {
                    ppBodyDetailInfoModel.standardTitleArray = it
                }
            }
            bodyParamVo.introductionString?.let {
                ppBodyDetailInfoModel.introductionString = it
            }
            if (standardArray?.isNotEmpty() == true) {
                bodyParamVo.colorArray?.let {
                    ppBodyDetailInfoModel.colorArray = it
                }
                fillWithStandardArray(
                    currentValue,
                    standardArray,
                    ppBodyDetailInfoModel,
                    bodyParamVo
                )

            } else if (//肥胖等级,身体类型和健康评估单独赋值
                bodyParamVo.bodyParamKey == "ppBodyHealth" ||
                bodyParamVo.bodyParamKey == "PPBodyFatGrade" ||
                bodyParamVo.bodyParamKey == "ppBodyType") {//处理小建议
                ppBodyDetailInfoModel.currentValueStr = bodyParamVo.standardTitleArray?.get(currentValue.toInt()) ?: ""
                if (bodyParamVo.suggestionArray?.isNotEmpty() == true){
                    if (currentValue.toInt() > bodyParamVo.suggestionArray!!.size -1){
                        ppBodyDetailInfoModel.standSuggestion = "--"
                        ppBodyDetailInfoModel.currentStandard = 0
                    } else {
                        ppBodyDetailInfoModel.currentStandard = currentValue.toInt()
                        ppBodyDetailInfoModel.standSuggestion = bodyParamVo.suggestionArray!![currentValue.toInt()]
                    }
                } else {
                    ppBodyDetailInfoModel.standSuggestion = "--"
                }
            } else {
                ppBodyDetailInfoModel.standeEvaluation = "--"
            }
        }
        return ppBodyDetailInfoModel
    }


    /**
     * 给这些标准区间赋值
     */
    private fun fillWithStandardArray(
        currentValue: Float, standardArray: List<Float>?,
        ppBodyDetailInfoModel: PPBodyDetailInfoModel,
        bodyParam: BodyParamVo
    ) {
        ppBodyDetailInfoModel.standardArray = standardArray
        //坐落的区间
        val currentStandard = calculateCurrentStandardWithValue(currentValue, standardArray)
        ppBodyDetailInfoModel.currentStandard = currentStandard
        //对节点区间的描述 不足 ,标准等
        ppBodyDetailInfoModel.standardTitle =
            bodyParam.standardTitleArray?.let { safeFetchListValueByIndex(currentStandard, it) }
                ?: ""
        // 节点区间的颜色
        ppBodyDetailInfoModel.standColor =
            bodyParam.colorArray?.let {
                safeFetchListValueByIndex(currentStandard, it)
            } ?: ""
        //对当前区间的建议
        ppBodyDetailInfoModel.standSuggestion =
            bodyParam.suggestionArray?.let {
                safeFetchListValueByIndex(currentStandard, it)
            } ?: ""
        //对当前区间的评价
        ppBodyDetailInfoModel.standeEvaluation =
            bodyParam.evaluationArray?.let {
                safeFetchListValueByIndex(currentStandard, it)
            } ?: ""
    }

    /**
     * 计算该值坐落在那个下标 67.25
     * 60.25
     */
    private fun calculateCurrentStandardWithValue(
        currentValue: Float,
        standardArray: List<Float>?
    ): Int {
        if (standardArray.isNullOrEmpty()) {
            return 0
        }
        val count = standardArray.size
        val first = standardArray.first()
        if (currentValue < first) {
            return 0
        }
        val last = standardArray.last()
        if (currentValue >= last) {
            return count - 2
        }
        for (i in 1 until count) {
            val prev = standardArray[i - 1]
            val current = standardArray[i]
            if (currentValue >= prev && currentValue < current) {
                return i - 1
            }
        }
        return 0
    }

    /**
     * 根据下标来获取值
     */
    private fun safeFetchListValueByIndex(index: Int, andList: List<String>): String {
        if (index <= andList.size - 1) {
            return andList.get(index)
        } else {
            return andList.first()
        }
    }

}
