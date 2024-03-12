package com.peng.ppscale.vo

enum class PPScaleSendState {

    PP_SEND_SUCCESS(0),
    PP_SEND_FAIL(1),
    PP_DEVICE_NO_CONNECT(2),
    PP_DEVICE_ERROR(3);


    private var type = 0

    constructor(bodygrade: Int) {
        this.type = bodygrade
    }

    open fun getType(): Int {
        return type
    }

}