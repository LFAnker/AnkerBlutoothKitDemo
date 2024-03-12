package com.peng.ppscale.data

data class PPBodyDetailInfoModel(
    //唯一键 ->"bodyParamKey": "ppBMI",
    var bodyParamKey: String = "",
    // 名称
    var bodyParamNameString: String = "",
    // 对名称的详细说明
    var introductionString: String = "",
    // 范围节点 [0,18.5,25,30,42]
    var standardArray: List<Float>? = ArrayList(),
    // 当前值 没有单位
    var currentValue: Float = 0.0f,
    //ui展示的值,经过计算的或者四色五人后的值
    var currentValueStr: String = "",
    // 当前值对应的单位
    var unit: String = "",
    // 当前值坐落的区间
    var currentStandard: Int = 0,
    // 对节点区间的描述 (瘦,普通,偏胖,肥胖)
    var standardTitleArray: List<String> = ArrayList(),
    //标准的名称
    var standardTitle: String = "",
    // 节点区间的颜色 (#F5A623,#14CCAD,#F43F31,#C23227)
    var colorArray: List<String> = ArrayList(),
    var standColor: String = "",
    // 对当前所在区间的评价(BMI_leve1_evaluation,BMI_leve2_evaluation,BMI_leve3_evaluation,BMI_leve4_evaluation)
    var evaluationArray: List<String> = ArrayList(),
    //当前的评价
    var standeEvaluation: String = "",
    // 对当前所在区间的建议(BMI_leve1_suggestion,BMI_leve2_suggestion,BMI_leve3_suggestion,BMI_leve4_suggestion)
    var suggestionArray: List<String> = ArrayList(),
    //需注意的区间 [1,2]
    var watchfulArray: ArrayList<Int> = ArrayList(),
    //当前的建议
    var standSuggestion: String = "",
    // 是否有区间
    var hasStandard: Boolean = false
):java.io.Serializable

