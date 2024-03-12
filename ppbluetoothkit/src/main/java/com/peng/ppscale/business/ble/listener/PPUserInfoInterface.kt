package com.peng.ppscale.business.ble.listener

import com.peng.ppscale.vo.PPUserModel

interface PPUserInfoInterface {
    fun getUserListSuccess(memberIDs: List<String?>?){}

    /**
     * 同步用户信息成功，如果多个用户需要重复调用
     */
    fun syncUserInfoSuccess(){}
    fun syncUserInfoFail(){}

    /**
     * 删除用户信息成功
     */
    fun deleteUserInfoSuccess(userModel: PPUserModel?){}

    /**
     * 删除用户信息失败
     */
    fun deleteUserInfoFail(userModel: PPUserModel?){}

    /**
     * 当前用户信息下发成功
     */
    fun confirmCurrentUserInfoSuccess(){}

    /**
     * 当前用户信息下发失败
     */
    fun confirmCurrentUserInfoFail(){}
}