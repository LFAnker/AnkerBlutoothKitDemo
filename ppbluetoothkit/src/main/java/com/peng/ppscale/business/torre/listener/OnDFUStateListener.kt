package com.peng.ppscale.business.torre.listener

interface OnDFUStateListener {
    fun onDfuStart(){}
    fun onDfuFail(errorType: String?){}
    fun onInfoOout(outInfo: String?){}
    fun onStartSendDfuData(){}
    fun onDfuProgress(progress: Int){}
    fun onDfuSucess(){}

}
