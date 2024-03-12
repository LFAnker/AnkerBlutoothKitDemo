package com.peng.ppscale.data

import com.peng.ppscale.util.PPUtil
import com.peng.ppscale.vo.PPUserGender
import com.peng.ppscale.calcute.BodyDetailUnitType
import java.util.*

/**
 *    @author : whs
 *    e-mail : haisilen@163.com
 *    date   : 2023/11/7 17:26
 *    desc   :标准集合的扩展方法
 */


fun PPBodyDetailStandard.fetchPPBodyParam_Weight_StandartArray(): List<Float> {
    val standardArray = mutableListOf<Float>()

    val gender = PPBodyDetailStandard.fatModel.ppSex
    val height = PPBodyDetailStandard.fatModel.ppHeightCm.toFloat()

    val standValue = if (gender == PPUserGender.PPUserGenderMale) {
        (height - 80) * 0.7f
    } else {
        (height - 70) * 0.6f
    }

    val point0 = standValue * 0.7f
    val point1 = standValue * 0.9f
    val point2 = standValue * 1.1f
    val point3 = standValue * 1.3f

    standardArray.addAll(listOf(point0, point1, point2, point3))
    return standardArray
}

/**
 * pragma mark - 体重 【3段】【量】
 */
fun PPBodyDetailStandard.fetchPPBodyParam_Weight_StandartArray_4(): List<Float> {
    val old = constWeightKg()
    val standardArray = intervalRange(old, boundary = emptyList())
    return standardArray
}

// 数据常量 - 体重（公斤）
fun PPBodyDetailStandard.constWeightKg(): List<Float> {
    val point1 = idealWeight * 0.9f
    val point2 = idealWeight * 1.1f
    return listOf(point1, point2)
}

fun PPBodyDetailStandard.fetchPPBodyParam_BodyFat_StandartArray(): List<Float> {
    val gender = fatModel.ppSex
    val age = fatModel.ppAge
    val standardArray: List<Float>

    standardArray = if (gender == PPUserGender.PPUserGenderMale) {
        when {
            age <= 7 -> listOf(0f, 7f, 16f, 25f, 30f, 100f)
            age <= 11 -> listOf(0f, 7f, 16f, 26f, 30f, 100f)
            age <= 13 -> listOf(0f, 7f, 16f, 25f, 30f, 100f)
            age <= 14 -> listOf(0f, 7f, 15f, 25f, 29f, 100f)
            age <= 15 -> listOf(0f, 8f, 15f, 24f, 29f, 100f)
            age <= 16 -> listOf(0f, 8f, 16f, 24f, 28f, 100f)
            age <= 17 -> listOf(0f, 9f, 16f, 23f, 28f, 100f)
            age <= 39 -> listOf(0f, 11f, 17f, 22f, 27f, 100f)
            age <= 59 -> listOf(0f, 12f, 18f, 23f, 28f, 100f)
            else -> listOf(0f, 14f, 20f, 25f, 30f, 100f)
        }
    } else {
        when {
            age <= 6 -> listOf(0f, 8f, 16f, 25f, 29f, 100f)
            age <= 7 -> listOf(0f, 9f, 17f, 25f, 30f, 100f)
            age <= 8 -> listOf(0f, 10f, 18f, 26f, 31f, 100f)
            age <= 9 -> listOf(0f, 10f, 19f, 28f, 32f, 100f)
            age <= 10 -> listOf(0f, 11f, 20f, 29f, 33f, 100f)
            age <= 11 -> listOf(0f, 13f, 22f, 31f, 35f, 100f)
            age <= 12 -> listOf(0f, 14f, 23f, 32f, 36f, 100f)
            age <= 13 -> listOf(0f, 15f, 25f, 34f, 38f, 100f)
            age <= 14 -> listOf(0f, 17f, 26f, 35f, 39f, 100f)
            age <= 15 -> listOf(0f, 18f, 27f, 36f, 40f, 100f)
            age <= 16 -> listOf(0f, 19f, 28f, 37f, 41f, 100f)
            age <= 17 -> listOf(0f, 20f, 28f, 37f, 41f, 100f)
            age <= 39 -> listOf(0f, 21f, 28f, 35f, 40f, 100f)
            age <= 59 -> listOf(0f, 22f, 29f, 36f, 41f, 100f)
            else -> listOf(0f, 23f, 30f, 37f, 42f, 100f)
        }
    }
    return standardArray
}

fun PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray(v1: Float, v2: Float): List<Float> {
    val temp = PPBodyDetailStandard.descDic2Arr(PPBodyDetailStandard.createList(v1, v2), 0f, 100.0f)
    return temp
}

fun PPBodyDetailStandard.fetchPPBodyParam_Water_StandartArray(): List<Float> {
    val gender = PPBodyDetailStandard.fatModel.ppSex

    return if (gender == PPUserGender.PPUserGenderMale) {
        listOf(0f, 55f, 65f, 100f)
    } else {
        listOf(0f, 45f, 60f, 100f)
    }
}

fun PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray(v1: Float, v2: Float): List<Float> {
    val sortedValues = PPBodyDetailStandard.createList(v1, v2)
    if (sortedValues.isNullOrEmpty() || sortedValues.size < 2) {
        val temp = PPBodyDetailStandard.descDic2Arr(
            sortedValues,
            0f,
            PPBodyDetailStandard.fatModel.ppWeightKg
        )
        return temp
    } else {
        val value = 2 * sortedValues[sortedValues.size - 1] - sortedValues[sortedValues.size - 2]
        return PPBodyDetailStandard.descDic2Arr(sortedValues, 0f, value)
    }
}

fun PPBodyDetailStandard.fetchPPBodyParam_Mus_StandartArray(): List<Float> {
    val gender = PPBodyDetailStandard.fatModel.ppSex
    val height = PPBodyDetailStandard.fatModel.ppHeightCm
    var standValue = 0f
    var difference = 0f

    if (gender == PPUserGender.PPUserGenderMale) {
        when {
            height < 160 -> {
                standValue = 42.5f
                difference = 4.0f
            }
            height > 170 -> {
                standValue = 54.4f
                difference = 5.0f
            }
            else -> {
                standValue = 48.2f
                difference = 4.2f
            }
        }
    } else {
        when {
            height < 150 -> {
                standValue = 31.9f
                difference = 2.8f
            }
            height > 160 -> {
                standValue = 39.5f
                difference = 3.0f
            }
            else -> {
                standValue = 35.2f
                difference = 2.3f
            }
        }
    }

    val point0 = standValue - difference * 3
    val point1 = standValue - difference
    val point2 = standValue + difference
    val point3 = standValue + difference * 3

    return listOf(point0, point1, point2, point3)
}

fun PPBodyDetailStandard.fetchPPBodyParam_Bone_StandartArray(v1: Float, v2: Float): List<Float> {
    val sortedValues = PPBodyDetailStandard.createList(v1, v2)
    if (sortedValues.isNullOrEmpty() || sortedValues.size < 2) {
        val temp = PPBodyDetailStandard.descDic2Arr(
            sortedValues,
            0f,
            PPBodyDetailStandard.fatModel.ppWeightKg
        )
        return temp
    } else {
        val value = 2 * sortedValues[sortedValues.size - 1] - sortedValues[sortedValues.size - 2]
        return PPBodyDetailStandard.descDic2Arr(sortedValues, 0f, value)
    }
}

fun PPBodyDetailStandard.fetchPPBodyParam_Bone_StandartArray(): List<Float> {
    val gender = PPBodyDetailStandard.fatModel.ppSex
    val weight = PPBodyDetailStandard.fatModel.ppWeightKg
    var standValue = 0f
    val difference = 0.1f

    if (gender == PPUserGender.PPUserGenderMale) {
        when {
            weight < 60.0 -> {
                standValue = 2.5f
            }
            weight > 75.0 -> {
                standValue = 3.2f
            }
            else -> {
                standValue = 2.9f
            }
        }
    } else {
        when {
            weight < 45.0 -> {
                standValue = 1.8f
            }
            weight > 60.0 -> {
                standValue = 2.5f
            }
            else -> {
                standValue = 2.2f
            }
        }
    }

    val point0 = PPUtil.keepPoint1f(standValue - difference * 3)
    val point1 = PPUtil.keepPoint1f(standValue - difference)
    val point2 = PPUtil.keepPoint1f(standValue + difference)
    val point3 = PPUtil.keepPoint1f(standValue + difference * 3)

    return listOf(point0, point1, point2, point3)
}

fun PPBodyDetailStandard.fetchPPBodyParam_BMR_StandartArray(): List<Float> {
    val gender = PPBodyDetailStandard.fatModel.ppSex
    val weight = PPBodyDetailStandard.fatModel.ppWeightKg
    val age = PPBodyDetailStandard.fatModel.ppAge
    var standValue = 0.0f

    if (gender == PPUserGender.PPUserGenderMale) {
        when {
            age < 29 -> {
                standValue = weight * 24
            }
            age < 49 -> {
                standValue = weight * 22.3f
            }
            age < 69 -> {
                standValue = weight * 21.5f
            }
            else -> {
                standValue = weight * 21.5f
            }
        }
    } else {
        when {
            age < 29 -> {
                standValue = weight * 23.6f
            }
            age < 49 -> {
                standValue = weight * 21.7f
            }
            age < 69 -> {
                standValue = weight * 20.7f
            }
            else -> {
                standValue = weight * 20.7f
            }
        }
    }

    val point0 = 0f
    val point1 = standValue.toFloat()
    val point2 = standValue.toFloat() * 2

    return listOf(point0, point1, point2)
}

fun PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray8(): List<Float> {
    return listOf(10.0f, 18.5f, 23f, 50f)
}

fun PPBodyDetailStandard.fetchPPBodyParam_BMI_StandartArray(): List<Float> {
    return listOf(10f, 18.5f, 25f, 30f, 50f)
}

//内脏脂肪等级
fun PPBodyDetailStandard.fetchPPBodyParam_VisFat_StandartArray(): List<Float> {
    return listOf(1f, 10f, 15f, 50f)
}

fun PPBodyDetailStandard.fetchPPBodyParam_proteinPercentage_StandartArray(): List<Float> {
    return listOf(14f, 16f, 18f, 20f)
}

fun PPBodyDetailStandard.fetchPPBodyParam_BodySubcutaneousFat_StandartArray(): List<Float> {
    val gender = PPBodyDetailStandard.fatModel.ppSex
    return if (gender == PPUserGender.PPUserGenderMale) {
        listOf(0f, 8.6f, 16.7f, 24.8f)
    } else {
        listOf(10.3f, 18.5f, 26.7f, 34.9f)
    }
}

fun PPBodyDetailStandard.fetchPPBodyParam_BodySkeletal_StandartArray(): List<Float> {
    return listOf(5f, 20f, 35f, 50f)
}

fun PPBodyDetailStandard.fetchPPBodyParam_heart_StandartArray(): List<Float> {
    return listOf(0f, 55f, 80f, 100f, 200f)
}

fun PPBodyDetailStandard.fetchPPBodyParam_BodyHealth_StandartArray(): List<Float> {
    return listOf(0f, 1f, 2f, 3f, 4f, 5f)
}

fun PPBodyDetailStandard.fetchPPBodyParam_FatGrade_StandartArray(): List<Float> {
    return listOf(0f, 18.5f, 25f, 30f, 35f, 40f, 50f)
}

fun PPBodyDetailStandard.fetchPPBodyParam_physicalAgeValue_StandartArray(): List<Float> {
    val standValue = PPBodyDetailStandard.fatModel.ppAge
    val point0 = 0f
    val point1 = standValue.toFloat()
    val point2 = standValue * 2f

    return listOf(point0, point1, point2)
}

fun PPBodyDetailStandard.fetchPPBodyParam_BodyScore_StandartArray(): List<Float> {
    return listOf(60f, 70f, 80f, 90f, 100f)
}

fun PPBodyDetailStandard.fetchPPBodyParam_BodyType_StandartArray(): List<Float> {
    return listOf(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f)
}

fun PPBodyDetailStandard.percent2kg(source: List<Float>): List<Float> {
    val temp = mutableListOf<Float>()
    val weight = (fatModel.ppWeightKg * 10).toInt().toFloat() / 10.0f
    for (n in source) {
        val kg = weight * n / 100.0f
        temp.add(kg.toFloat())
    }
    return temp.toList()
}

fun PPBodyDetailStandard.kg2percent(source: List<Float>): List<Float> {
    val temp = mutableListOf<Float>()
    val weight = (PPBodyDetailStandard.fatModel.ppWeightKg * 10).toInt().toFloat() / 10.0f
    for (n in source) {
        val percent = n / weight * 100f
        temp.add(percent.toFloat())
    }
    return temp.toList()
}
fun PPBodyDetailStandard.constBodyFatRate(): List<Float> {
    var standardArray: List<Float> = emptyList()
    val gender: PPUserGender = fatModel.ppSex
    val age: Int = fatModel.ppAge

    if (gender == PPUserGender.PPUserGenderMale) {
        when {
            age <= 7 -> standardArray = listOf(7f, 16f, 25f, 30f)
            age <= 11 -> standardArray = listOf(7f, 16f, 26f, 30f)
            age <= 13 -> standardArray = listOf(7f, 16f, 25f, 30f)
            age <= 14 -> standardArray = listOf(7f, 15f, 25f, 29f)
            age <= 15 -> standardArray = listOf(8f, 15f, 24f, 29f)
            age <= 16 -> standardArray = listOf(8f, 16f, 24f, 28f)
            age <= 17 -> standardArray = listOf(9f, 16f, 23f, 28f)
            age <= 39 -> standardArray = listOf(11f, 17f, 22f, 27f)
            age <= 59 -> standardArray = listOf(12f, 18f, 23f, 28f)
            else -> standardArray = listOf(14f, 20f, 25f, 30f)
        }
    } else {
        when {
            age <= 6 -> standardArray = listOf(8f, 16f, 25f, 29f)
            age <= 7 -> standardArray = listOf(9f, 17f, 25f, 30f)
            age <= 8 -> standardArray = listOf(10f, 18f, 26f, 31f)
            age <= 9 -> standardArray = listOf(10f, 19f, 28f, 32f)
            age <= 10 -> standardArray = listOf(11f, 20f, 29f, 33f)
            age <= 11 -> standardArray = listOf(13f, 22f, 31f, 35f)
            age <= 12 -> standardArray = listOf(14f, 23f, 32f, 36f)
            age <= 13 -> standardArray = listOf(15f, 25f, 34f, 38f)
            age <= 14 -> standardArray = listOf(17f, 26f, 35f, 39f)
            age <= 15 -> standardArray = listOf(18f, 27f, 36f, 40f)
            age <= 16f -> standardArray = listOf(19f, 28f, 37f, 41f)
            age <= 17 -> standardArray = listOf(20f, 28f, 37f, 41f)
            age <= 39 -> standardArray = listOf(21f, 28f, 35f, 40f)
            age <= 59 -> standardArray = listOf(22f, 29f, 36f, 41f)
            else -> standardArray = listOf(23f, 30f, 37f, 42f)
        }
    }

    return standardArray
}

fun PPBodyDetailStandard.constMuscleWeightKg(): List<Float> {
    val gender: PPUserGender = fatModel.ppSex
    val height: Int = fatModel.ppHeightCm
    var standValue: Float = 0f
    var difference: Float = 0f

    if (gender == PPUserGender.PPUserGenderMale) {
        if (height < 160) {
            standValue = 42.5f
            difference = 4.0f
        } else if (height > 170) {
            standValue = 54.5f
            difference = 5.0f
        } else {
            standValue = 48.2f
            difference = 4.2f
        }
    } else {
        if (height < 150) {
            standValue = 31.9f
            difference = 2.8f
        } else if (height > 160) {
            standValue = 39.5f
            difference = 3.0f
        } else {
            standValue = 35.2f
            difference = 2.3f
        }
    }

    val point1: Float = standValue - difference
    val point2: Float = standValue + difference

    return listOf(point1, point2)
}

fun PPBodyDetailStandard.skeletalMuscleKg(): List<Float> {
    return listOf(20f, 35f)
}

fun PPBodyDetailStandard.constproteinPercentage(): List<Float> {
    return listOf(16f, 18f)
}

fun PPBodyDetailStandard.subcutaneousFatPercentage(): List<Float> {
    val gender: PPUserGender = fatModel.ppSex
    if (gender == PPUserGender.PPUserGenderMale) {
        return listOf(8.6f, 16.7f)
    } else {
        return listOf(18.5f, 26.7f)
    }
}

fun PPBodyDetailStandard.constBMR(): List<Float> {
    val gender: PPUserGender = fatModel.ppSex
    val weight: Float = this.weight.toFloat()
    val age: Int = fatModel.ppAge
    var standValue: Float = 0f

    if (gender == PPUserGender.PPUserGenderMale) {
        if (age < 29) {
            standValue = weight * 24
        } else if (age < 49) {
            standValue = weight * 22.3f
        } else if (age < 69) {
            standValue = weight * 21.5f
        } else {
            standValue = weight * 21.5f
        }
    } else {
        if (age < 29) {
            standValue = weight * 23.6f
        } else if (age < 49) {
            standValue = weight * 21.7f
        } else if (age < 69) {
            standValue = weight * 20.7f
        } else {
            standValue = weight * 20.7f
        }
    }

    val point1: Float = standValue

    return listOf(point1)
}

fun PPBodyDetailStandard.constBoneWeightKg(): List<Float> {
    val gender: PPUserGender = fatModel.ppSex
    val weight: Float = this.weight.toFloat()
    var standValue: Float = 0f
    val difference: Float = 0.1f

    if (gender == PPUserGender.PPUserGenderMale) {
        if (weight < 60.0f) {
            standValue = 2.5f
        } else if (weight > 75.0f) {
            standValue = 3.2f
        } else {
            standValue = 2.9f
        }
    } else {
        if (weight < 45.0f) {
            standValue = 1.8f
        } else if (weight > 60.0f) {
            standValue = 2.5f
        } else {
            standValue = 2.2f
        }
    }

    val point1: Float = standValue - difference
    val point2: Float = standValue + difference

    return listOf(point1, point2)
}

fun PPBodyDetailStandard.constWaterRate(): List<Float> {
    val gender: PPUserGender = fatModel.ppSex
    val standardArray: List<Float>
    if (gender == PPUserGender.PPUserGenderMale) {
        standardArray = listOf(55f, 65f)
    } else {
        standardArray = listOf(45f, 60f)
    }
    return standardArray
}

/**
 * 通过合泰给的两个值，计算出区间列表
 */
fun PPBodyDetailStandard.descDic2Arr(sortedValues: List<Float>?, min: Float, max: Float): List<Float> {
    if (sortedValues.isNullOrEmpty() || sortedValues.size < 2) {
        return listOf()
    } else {
        val result = mutableListOf<Float>()
        sortedValues.forEachIndexed { index, fl ->
            if (index == 0) {
                result.add(min)
            }
            result.add(index + 1, sortedValues[index])
            if (index == sortedValues.size - 1) {
                result.add(max)
            }
        }
        return result
    }
}

/**
 * 通过合泰给的两个值，计算出区间列表
 */
fun PPBodyDetailStandard.sortValuesFromDictionary(v1: Float, v2: Float, type: BodyDetailUnitType = BodyDetailUnitType.BodyDetailUnitTypeWeight): List<Float> {
    val sortedValues = createList(v1, v2)
    return fetchPPBodyParam_StandartArray_8(sortedValues)
//        return when (type) {
//            BodyDetailUnitType.BodyDetailUnitTypeWeight -> typeWeightIncrementBoundary(sortedValues)
//            BodyDetailUnitType.BodyDetailUnitTypeValue -> typeValueIncrementBoundary(sortedValues)
//            BodyDetailUnitType.BodyDetailUnitTypePercent -> typePercentIncrementBoundary(sortedValues)
//        }
}

fun PPBodyDetailStandard.sortValuesFromDictionary(type: BodyDetailUnitType): List<Float> {
    return emptyList()
}


fun PPBodyDetailStandard.typePercentIncrementBoundary(old: List<Float>): List<Float> {
    val temp = old.toMutableList()
    temp.add(0, 0.0f)
    temp.add(temp.size, 100f)
    return temp
}

fun PPBodyDetailStandard.typeWeightIncrementBoundary(old: List<Float>): List<Float> {
    val temp = old.toMutableList()
    val formattedWeight = String.format(Locale.US, "%.1f", PPBodyDetailStandard.fatModel.ppWeightKg).toFloat()
    temp.add(0, 0f)
    temp.add(temp.size, formattedWeight)
    return temp
}

fun PPBodyDetailStandard.fetchPPBodyParam_StandartArray_8(old:List<Float>): List<Float> {
    val standardArray: List<Float> = intervalRange(old, listOf())
    return standardArray
}

fun PPBodyDetailStandard.typeValueIncrementBoundary(old: List<Float>?): List<Float> {
    val temp = old?.toMutableList() ?: mutableListOf()

    if (old == null) {
        return emptyList()
    } else {
        if (old.isEmpty()) {
            return emptyList()
        } else if (old.size == 1) {
            temp.add(0, 0f)
            temp.add(temp.last() * 2)
            return temp.toList()
        } else {
            val min = old.first()
            val max = old.last()
            val mid = Math.abs(min - max) / 2.0f

            val first = if (min - mid < 0) 0f else min - mid
            val last = max + mid
            temp.add(0, first)
            temp.add(last)
            return temp.toList()
        }
    }
}


fun PPBodyDetailStandard.createList(vararg args: Float?): List<Float> {
    val list = mutableListOf<Float>()
    for (i in args.indices) {
        args[i]?.let { list.add(it) }
    }
    list.sorted()
    return list
}


