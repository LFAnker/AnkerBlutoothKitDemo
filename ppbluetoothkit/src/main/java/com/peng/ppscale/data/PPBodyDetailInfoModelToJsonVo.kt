package com.peng.ppscale.data

/**
 *    @author : whs
 *    e-mail : haisilen@163.com
 *    date   : 2023/11/9 11:41
 *    desc   :
 */
data class PPBodyDetailInfoModelToJsonVo(
    val errorType:String = "PP_ERROR_TYPE_NONE",
    val lefuBodyData:MutableList<PPBodyDetailInfoModel> = ArrayList()
):java.io.Serializable
