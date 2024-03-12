package com.peng.ppscale.util


import android.content.Context
import com.peng.ppscale.business.device.PPUnitType
import com.peng.ppscale.vo.PPScaleDefine
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt

//
// Created by L on 2020/7/2.
// Since: 1.0.0
// Copyright (c) 2020 lefu.cc . All rights reserved.
//
// fixme 变动参照ios版本算法
/**
 * 先乘以系数，然后除以最小分度（1的除1，0.1的也是除1，0.01的也是除1，0.05的除5、,,,），
 * 然后比最终结果多取一位小数，
 * 然后多出的一个小数做五舍六入,乘以最小分度(1的乘1，0.1的也是乘1，0.01的也是乘1，0.05的乘5)
 *
 * 先乘以系数，然后比最终结果多取一位小数，然后多出的一个小数做五舍六入，最后做最小分度处理

①oz:两位小数，最小分度0.05
1g=0.03527390oz

②lb:oz，lb：0位小数、最小分度1，oz：一位小数，最小分度0.1
1g=0.03527390oz

③fl.oz(water)：2位小数、最小分度0.05
1g=0.03381805fl.oz(water)

④fl.oz(milk)：2位小数、最小分度0.05
1g=0.03283732fl.oz(milk)

⑤g:0位小数，最小分度1

⑥ml（water）:0位小数，最小分度1
1g=1ml（water）

⑦ml（milk）:0位小数，最小分度1
1g=0.971ml（water）
 *
 */
class EnergyStruct(val g: Float) {

    fun toMlWater(): EnergyUnit =
        EnergyUnit(
            g,
            PPUnitType.PPUnitMLWater
        )

    /**
     * ③fl.oz(water)：2位小数、最小分度0.05
     * 1g=0.03381805fl.oz(water)
     */
    fun toFlOzWater(accuracyType: PPScaleDefine.PPDeviceAccuracyType): EnergyUnit =
        EnergyUnit(
//                    g.times(10000).times(0.033814023f).div(10000),
            getFlOzWater(accuracyType),
            PPUnitType.PPUnitFL_OZ_WATER
        )

    fun getFlOzWater(accuracyType: PPScaleDefine.PPDeviceAccuracyType): Float {
        val sign = if (g >= 0) 1 else -1
//        return if (DeviceType.deviceType == 1) g.absoluteValue.times(0.03381805f).round56_5(2)
//            .times(sign)
//        else
        return if (accuracyType != PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePointG)
            getDevice3FlozWater(sign)
        else Math.floor(g.absoluteValue.times(23117).plus(32768).div(65535.0f).toDouble()).toFloat()
            .div(10.0f).times(sign)

    }

    private fun getDevice3FlozWater(sign: Int): Float {
        var fl_oz = ((g.absoluteValue * 10 * 33818 + 5000) / 10000).toInt()
        if (fl_oz >= 100000) {
            fl_oz = fl_oz + 5
            return fl_oz.div(10.0f).toInt().div(100.0f).times(sign)
        } else {
            return fl_oz.div(1000.0f).times(sign)
        }
    }

    /**
     * ④fl.oz(milk)：2位小数、最小分度0.05
     * 1g=0.03283732fl.oz(milk)
     */
    fun toFlOzMilk(accuracyType: PPScaleDefine.PPDeviceAccuracyType): EnergyUnit =
        EnergyUnit(
            getFlOzMilk(accuracyType),
            PPUnitType.PPUnitMLMilk
        )

    fun getFlOzMilk(accuracyType: PPScaleDefine.PPDeviceAccuracyType): Float {
        val sign = if (g >= 0) 1 else -1
//        return if (DeviceType.deviceType == 1) g.absoluteValue.times(0.03283732f).round56_5(2)
//            .times(sign)
//        else
        val result: Float
        if (accuracyType != PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePointG) {
            result = getDevice3FlozMilk(sign)
        } else {
            result = Math.floor(g.absoluteValue.times(22446).plus(32768).div(65535.0f).toDouble())
                .toFloat()
                .div(10.0f).times(sign)
        }
        Logger.d("getDevice3FlozMilk result = $result")
        return result
    }

    private fun getDevice3FlozMilk(sign: Int): Float {
        var fl_oz = ((g.absoluteValue * 10 * 32837 + 5000) / 10000).toInt()
        if (fl_oz >= 100000) {
            fl_oz = fl_oz + 5
            return fl_oz.div(10.0f).toInt().div(100.0f).times(sign)
        }
        return fl_oz.div(1000.0f).times(sign)
    }


    /**
     * ⑦ml（milk）:0位小数，最小分度1
     * 1g=0.971ml（water）
     */
    fun toMlMilk(accuracyType: PPScaleDefine.PPDeviceAccuracyType): EnergyUnit =
        EnergyUnit(
//                    g.times(10000).div(1.0288f).div(10000),
//                    g.times(10000).times(0.971f).div(10000),
            getMlMilk(accuracyType),
            PPUnitType.PPUnitMLMilk
        )

    private fun getMlMilk(accuracyType: PPScaleDefine.PPDeviceAccuracyType): Float {
        val sign = if (g >= 0) 1 else -1
        if (DeviceType.deviceType == 1) {
            return g.absoluteValue.times(0.971f).round56_1(0).times(sign)
        } else if (accuracyType != PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePointG && DeviceType.deviceType == 3) {
            return ((g.absoluteValue * 10 * 972 + 500) / 1000.0f).toInt().div(10.0f).times(sign)
        } else {
//            return g.times(10000).div(1.0288f).div(10000)
            return g.absoluteValue.times(63634).div(65536.0f).times(sign)
        }
    }

    /**
     * ①oz:两位小数，最小分度0.05
     * 1g=0.03527390oz
     * oz两位小数 lboz 一位小数
     */
    fun toOz(len: Int, accuracyType: PPScaleDefine.PPDeviceAccuracyType): EnergyUnit {
        val energyUnit = EnergyUnit(
            getOz(len, accuracyType),
            PPUnitType.PPUnitOZ
        )
        return energyUnit
    }

    private fun getOz(len: Int, accuracyType: PPScaleDefine.PPDeviceAccuracyType): Float {
        val sign = if (g >= 0) 1 else -1
//        if (DeviceType.deviceType == 1) {
//            when (len) {
//                2 -> return g.absoluteValue.times(0.035273962f).round56_5(len).times(sign)
//                else -> return g.absoluteValue.times(0.035273962f).round56_1(len).times(sign)
//            }
//        } else
        if (accuracyType != PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePointG && DeviceType.deviceType == 3) {
            return getDevice3Oz(sign)
        } else {
            val value = g.absoluteValue.times(35274).div(100000)

            val b: BigDecimal = BigDecimal(value.toString())
            val f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).toDouble().div(10).times(sign).toFloat()
            return f1
//            return .round(1).div(10).times(sign).toFloat()
        }
    }

    private fun getDevice3Oz(sign: Int): Float {
        var fl_oz = ((g.absoluteValue * 10 * 35274 + 5000) / 10000).toInt()
        if (fl_oz >= 100000) {
            fl_oz = fl_oz + 5
            return fl_oz.div(10.0f).toInt().div(100.0f).times(sign)
        }
        return fl_oz.div(1000.0f).times(sign)
    }


    fun toLbOz(accuracyType: PPScaleDefine.PPDeviceAccuracyType): EnergyUnitLbOz {
        val oz = toOz(1, accuracyType).value
        return EnergyUnitLbOz(
            (oz.div(16)).toInt(),
            oz % 16
        )
    }

    fun toLb(accuracyType: PPScaleDefine.PPDeviceAccuracyType): EnergyUnit {
        if (accuracyType != PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePointG) {
            val value = g.times(10).times(22046).plus(50000).div(10000).div(1000).div(10)
            return EnergyUnit(value, PPUnitType.Unit_LB)
        } else {
            val value = g.times(10).times(22046).div(10000).div(1000).div(10)
            val scale = BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP).toFloat()
            return EnergyUnit(scale, PPUnitType.Unit_LB)
        }

    }

}

/**
 * 其他类型包装成g
 * other->g
 */
open class EnergyUnit(val value: Float, val type: PPUnitType) {
    open fun toG(scaleType: String = ""): EnergyStruct = when (type) {
        PPUnitType.PPUnitOZ -> oz2g(value)
        PPUnitType.PPUnitMLWater -> ml_water2g(value)
        PPUnitType.PPUnitMLMilk -> ml_milk2g(value)
        PPUnitType.Unit_LB -> lb2g(value)
        PPUnitType.PPUnitFL_OZ_WATER -> fl_oz_water2g(value)
        PPUnitType.PPUnitFL_OZ_MILK -> fl_oz_milk2g(value)
        else -> EnergyStruct(value)
    }

    /**
     * 最大的数字长度，包含小数
     */
    fun maxLength(): Int {
        return when (this.type) {
            PPUnitType.Unit_LB, PPUnitType.PPUnitOZ, PPUnitType.PPUnitFL_OZ_WATER, PPUnitType.PPUnitFL_OZ_MILK -> 5
            else -> 4
        }
    }

    fun format(type: PPUnitType): String {
        val i = this.intercept(type)
        if (this is EnergyUnitLbOz) {
            return "$lb:" + "%.${i}f".round(this.oz, i)
        }
        return "%.${i}f".round(this.value, i)
    }

    fun intercept(type: PPUnitType): Int {
        return when (type) {
            PPUnitType.PPUnitFL_OZ_WATER, PPUnitType.PPUnitFL_OZ_MILK, PPUnitType.PPUnitOZ ->
                1
            PPUnitType.PPUnitG, PPUnitType.PPUnitMLWater, PPUnitType.PPUnitMLMilk -> 0
            PPUnitType.Unit_LB -> 2
            else -> 1
        }
//    return UnitUtil.unitRoundNum(this.value, this.type.toBleUnit(), scaleType);
    }

    fun format01(): String {
        val i = this.intercept01(this)
        if (this is EnergyUnitLbOz) {
            return "$lb:" + "%.${i}f".round(this.oz, i)
        }
        when (this.type) {
            PPUnitType.Unit_LB, PPUnitType.PPUnitOZ, PPUnitType.PPUnitFL_OZ_WATER, PPUnitType.PPUnitFL_OZ_MILK -> {
                val scaledNumber = Math.floor(this.value * (10.0.pow(i))) / (10.0.pow(i))
                return scaledNumber.toString()
            }
            else -> return "%.${i}f".round(this.value, i)
        }
    }

    fun intercept01(energyUnit: EnergyUnit): Int {
        return when (this.type) {
            PPUnitType.PPUnitFL_OZ_WATER, PPUnitType.PPUnitFL_OZ_MILK, PPUnitType.PPUnitOZ ->
                when (DeviceType.deviceType) {
                    3 -> if (this.value.absoluteValue * 1000 >= 100000) return 2
                    else return 3//三位小数
                    1 -> 2
                    else -> 1
                }
            PPUnitType.PPUnitG -> 1
            PPUnitType.PPUnitMLWater, PPUnitType.PPUnitMLMilk -> 1
            PPUnitType.Unit_LB -> 3
            PPUnitType.PPUnitLBOZ ->
                if (energyUnit is EnergyUnitLbOz) {
                    if (energyUnit.oz > 10) return 1 else return 2
                } else return 1
            else -> 1
        }
//    return UnitUtil.unitRoundNum(this.value, this.type.toBleUnit(), scaleType);
    }


//    fun unitText(context: Context): String {
//        return when (this.type) {
//            PPUnitType.PPUnitG -> context.getString(R.string.string_unit_g)
//            PPUnitType.PPUnitOZ -> context.getString(R.string.string_unit_oz)
//            PPUnitType.PPUnitLBOZ -> context.getString(R.string.string_unit_lb_oz)
//            PPUnitType.PPUnitFL_OZ_WATER -> context.getString(R.string.string_unit_fl_oz_water)
//            PPUnitType.PPUnitFL_OZ_MILK -> context.getString(R.string.string_unit_fl_oz_milk)
//            PPUnitType.PPUnitMLWater -> context.getString(R.string.string_unit_water_ml)
//            PPUnitType.PPUnitMLMilk -> context.getString(R.string.string_unit_milk_ml)
//            PPUnitType.Unit_LB -> context.getString(R.string.string_unit_lb)
//            else -> ""
//        }
//    }
}

class EnergyUnitLbOz(
    var lb: Int,
    var oz: Float
) : EnergyUnit(oz, PPUnitType.PPUnitLBOZ) {
    override fun toG(scaleType: String): EnergyStruct = lb_oz2gWithlb(lb, oz)
}

object Energy {
    // ignore
    /**
     * g->other
     */
    @JvmStatic
    public fun toG(
        value: Float,
        type: PPUnitType,
        accuracyType: PPScaleDefine.PPDeviceAccuracyType
    ): EnergyUnit {
        val struct = EnergyStruct(value)
        return when (type) {
            PPUnitType.PPUnitMLMilk -> struct.toMlMilk(accuracyType)
            PPUnitType.PPUnitMLWater -> struct.toMlWater()
            PPUnitType.PPUnitFL_OZ_WATER -> struct.toFlOzWater(accuracyType)
            PPUnitType.PPUnitFL_OZ_MILK -> struct.toFlOzMilk(accuracyType)
            PPUnitType.PPUnitLBOZ -> struct.toLbOz(accuracyType)
            PPUnitType.PPUnitOZ -> struct.toOz(2, accuracyType)
            PPUnitType.Unit_LB -> struct.toLb(accuracyType)
            else -> EnergyUnit(value, type)
        }
    }

}

fun unit(context: Context): String = "kcal"

fun lb_oz2gWithlb(lb: Int, oz: Float): EnergyStruct {
    val oz = lb.times(16f).plus(oz)
    return oz2g(oz)
}

/**
 * ⑥ml（water）:0位小数，最小分度1
 * 1g=1ml（water）
 */
fun ml_water2g(ml_water: Float): EnergyStruct {
    return EnergyStruct(ml_water)
}

/**
 * ③fl.oz(water)：2位小数、最小分度0.05
 * 1g=0.03381805fl.oz(water)
 */
fun fl_oz_water2g(ml_water: Float): EnergyStruct {
    return if (DeviceType.deviceType == 1) EnergyStruct(
        ml_water.times(10000).div(0.03381805f).div(10000)
    )
    else EnergyStruct(
        ml_water.times(65535).minus(32768).div(23117).times(10).toInt().toFloat()
    )
}

/**
 * ④fl.oz(milk)：2位小数、最小分度0.05
 * 1g=0.03283732fl.oz(milk)
 */
fun fl_oz_milk2g(ml_milk: Float): EnergyStruct {
    return if (DeviceType.deviceType == 1) EnergyStruct(
        ml_milk.times(10000).div(0.03283732f).div(10000)
    )
    else EnergyStruct(
        ml_milk.times(65535).minus(32768).div(22446).times(10).toInt().toFloat()
    )
}


/**
 * ⑦ml（milk）:0位小数，最小分度1
 * 1g=0.971ml（water）
 */
fun ml_milk2g(ml_milk: Float): EnergyStruct {
    when (DeviceType.deviceType) {
        1 -> return EnergyStruct(ml_milk.div(0.971f))
        else -> return EnergyStruct(ml_milk.times(10000).times(1.0288000000f).div(10000))
    }

}

fun oz2g(oz: Float): EnergyStruct {
    return EnergyStruct(
        oz.times(10000).div(0.035273962f).div(10000)
    )
}

// g.times(10).times(22046).div(10000).div(1000).div(10),
fun lb2g(lb: Float): EnergyStruct {
    return EnergyStruct(
        lb.times(10).times(1000).times(10000).div(22046).div(10)
    )
}


fun String.round(placeholder: Float, len: Int): String =
    String.format(Locale.US, this, placeholder round len)

infix fun Double.round(len: Int) = this.times(10.0.pow(len)).roundToInt().div(10.0.pow(len))
infix fun Float.round(len: Int) = this.toDouble().round(len)

/**
 * 五舍六入
 */
infix fun Float.round56_5(len: Int): Float {
    //a/5 =0.236  * 1000
    //0.236 ==0.24 * 5 = 1.2
    //0.236 *1000 236+4  /10 int() /100
    return this.div(5).times(10.0f.pow(len + 1)).toInt().plus(4).div(10).times(5)
        .div(10.0f.pow(len))
}

/**
 * 五舍六入
 */
infix fun Float.round56_1(len: Int): Float {
    return this.div(1).times(10.0f.pow(len + 1)).toInt().plus(4).div(10).times(1)
        .div(10.0f.pow(len))
}

object DeviceType {

    /**
     * 计算的算法公式使用哪一套
     * deviceType 0  Kitchenscale 1 KitchenScale1/KFScale
     */
    @JvmStatic
    var deviceType = 0;

    /**
     * 当前连接的设备是否是广播设备，广播设备隐藏切换单位按钮和归零按钮
     */
    var isBroadcastDevice = false;
}
