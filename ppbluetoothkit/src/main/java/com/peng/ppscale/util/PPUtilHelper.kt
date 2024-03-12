package com.peng.ppscale.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

object PPUtilHelper {

    /**
     * kg转st 双精度
     */
    fun kgToSt2_Point2D(var0: Double): String {
        val one = BigDecimal(var0.toString()).multiply(BigDecimal(100)).add(BigDecimal(5)).divide(BigDecimal(10), 10, RoundingMode.HALF_UP).toInt()
        val two = BigDecimal(22046).multiply(BigDecimal(one)).divide(BigDecimal(1000), 10, RoundingMode.HALF_UP).setScale(0, RoundingMode.DOWN).toInt()
//        val var2 = Math.floor((((var0 * 100.0 + 5.0) / 10.0).toInt() * 22046 / 1000).toDouble()).toInt()
        val var3 = two / 14
        return keepPoint2D(var3.toDouble() / 100.0)
    }

    /**
     * kg转lb
     */
    fun kgToLB(kg: Double, scale: Double): String {
        if (0.0 == kg) {
            return "0.0"
        }
        if (scale == 0.05) {
            val one = BigDecimal(kg.toString()).multiply(BigDecimal("100")).add(BigDecimal("5")).toDouble()
            val weight = Math.round(one) / 10
            val roundWeightInt = weight.toInt()
            val floorWeightFloat = BigDecimal(roundWeightInt).divide(BigDecimal("10"), 10, RoundingMode.HALF_UP)
            val floorWeight = Math.floor((floorWeightFloat.multiply(BigDecimal("10")).multiply(BigDecimal("22046")).divide(BigDecimal("10000"), 10, RoundingMode.HALF_UP)).toDouble())
            val floorWeightInt = floorWeight.toInt()
            val floorWeightF = floorWeightInt / 10.0f
            return String.format(Locale.US, "%.1f", floorWeightF)
        } else {
            var floorWeight = BigDecimal(kg.toString()).multiply(BigDecimal("10")).multiply(BigDecimal("22046")).divide(BigDecimal("10000"), 10, RoundingMode.HALF_UP).toDouble()
            var intFloor = Math.floor(floorWeight).toInt()
            if (intFloor % 2 > 0) {
                intFloor = intFloor + 1
            }
            val weight = intFloor / 10.0f
            return String.format(Locale.US, "%.1f", weight)
        }
    }

    /**
     * 双精度精确
     */
    fun keepPoint2D(var0: Double): String {
        val var2 = BigDecimal(var0.toString())
        var var3 = var2.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
        return String.format(Locale.US, "%.2f", var3)
    }


}