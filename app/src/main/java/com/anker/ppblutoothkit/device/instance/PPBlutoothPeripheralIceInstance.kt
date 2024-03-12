package com.anker.ppblutoothkit.device.instance

import com.peng.ppscale.device.PeripheralIce.PPBlutoothPeripheralIceController

class PPBlutoothPeripheralIceInstance {

    var controller: PPBlutoothPeripheralIceController? = null

    companion object Factory {
        val instance by lazy { PPBlutoothPeripheralIceInstance() }
    }

    init {
        controller = PPBlutoothPeripheralIceController()
    }

}