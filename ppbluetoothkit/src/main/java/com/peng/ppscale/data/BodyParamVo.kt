package com.peng.ppscale.data

/**
 *    @author : whs
 *    e-mail : haisilen@163.com
 *    date   : 2023/6/19 10:07
 *    desc   :
 */

data class BodyParamVo(
    //唯一键
    val bodyParamKey: String,
    val bodyParamNameString: String,
    val colorArray: ArrayList<String> ?,
    val unit: String = "",
    val evaluationArray: ArrayList<String>?,
    val introductionString: String?,
    val standardTitleArray: ArrayList<String>?,
    val suggestionArray: ArrayList<String>?,
    val watchfulArray: ArrayList<Int>?
):java.io.Serializable


