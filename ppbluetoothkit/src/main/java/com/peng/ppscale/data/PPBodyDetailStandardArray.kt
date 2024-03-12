package com.peng.ppscale.data


import com.peng.ppscale.vo.HT_8_
import com.peng.ppscale.vo.PPBodyFatModel
import com.peng.ppscale.vo.PPUserGender

/**
 *    @author : whs
 *    e-mail : haisilen@163.com
 *    date   : 2023/11/2 9:05
 *    desc   :身体数据标准集合
 */
object PPBodyDetailStandardArray {

    lateinit var fatModel: PPBodyFatModel
    var idealWeight:Float = 0f
    var weight:Float = 0f

    fun initWithBodyFatModel(fatModel: PPBodyFatModel) {
        this.fatModel = fatModel
        this.weight =  fatModel.ppWeightKg
        if (fatModel.ppSDKVersion?.startsWith(HT_8_) == true) {//八电极
            if (fatModel.ppSex == PPUserGender.PPUserGenderMale) {
                idealWeight = 22 * Math.pow(fatModel.ppHeightCm / 100.0, 2.0).toFloat()
            } else {
                idealWeight = 21 * Math.pow(fatModel.ppHeightCm / 100.0, 2.0).toFloat()
            }
        } else {//四电极
            idealWeight = 22 * Math.pow(fatModel.ppHeightCm / 100.0, 2.0).toFloat()
        }
    }


    fun calculateCurrentStandardWithValue(number: Float, standardArray: List<Float>): Int {
        val count = standardArray.size

        val first = standardArray.first()
        if (number < first) {
            return 0
        }
        val last = standardArray.last()
        if (number >= last) {
            return count - 2
        }
        for (i in 1 until count) {
            val prev = standardArray[i - 1]
            val current = standardArray[i]
            if (number >= prev && number < current) {
                return i - 1
            }
        }
        return 0
    }

    fun descDic2Arr(dictionary: Map<Any, Any>): List<Any> {
        val sortedValues = dictionary.values.sortedWith(Comparator { obj1, obj2 ->
            when {
                obj1 is Int && obj2 is Int -> obj1.compareTo(obj2)
                else -> 0
            }
        })
        return sortedValues
    }

    fun intervalRange(old: List<Float>, boundary: List<Float>): List<Float> {
        if (boundary.size == 2) {
            val standardArray = ArrayList(old)
            standardArray.add(0, boundary[0])
            standardArray.add(standardArray.size, boundary[1])
            return standardArray
        } else {
            val standardArray = typeValueIncrementBoundary(old)
            return standardArray
        }
    }

    fun typeValueIncrementBoundary(old: List<Float>?): List<Float> {
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

    fun idealkg2percent(source: List<Float>): List<Float> {
        val weight = (idealWeight * 10).toInt() / 10.0f

        val temp = ArrayList<Float>()
        for (n in source) {
            val percent = (n as Number).toFloat() / weight * 100
            temp.add(percent)
        }

        return temp
    }

    fun percent2kg(source: List<Float>): List<Float> {
        val temp = ArrayList<Float>()
        val weight = (weight * 10).toInt() / 10.0f
        for (n in source) {
            val kg = weight * (n as Number).toFloat() / 100.0f
            temp.add(kg)
        }
        return temp
    }

    fun kg2percent(source: List<Float>): List<Float> {
        val weight = (weight * 10).toInt() / 10.0f

        val temp = ArrayList<Float>()
        for (n in source) {
            val percent = (n as Number).toFloat() / weight * 100
            temp.add(percent)
        }

        return temp
    }


    /**
     * pragma mark - 体重 【3段】【量】
     */
    fun fetchPPBodyParam_Weight_StandartArray_4(): List<Float> {
        val old = constWeightKg()
        val standardArray = intervalRange(old, boundary = emptyList())
        return standardArray
    }

    fun fetchPPBodyParam_Weight_StandartArray_8(old: List<Float>): List<Any> {
        val standardArray = intervalRange(old, boundary = emptyList())
        return standardArray
    }

    // 体脂 【4电极5段 8电极3段】 【量、率】
    fun fetchPPBodyParam_BodyFat_StandartArray_4_D_typeWeight(isWeight: Boolean): List<Float> {
        val old: List<Float> = constBodyFatRate()
        val standardArray: List<Float> = intervalRange(old, listOf(5f, 75f))
        return if (isWeight) {
            val kgArr: List<Float> = percent2kg(standardArray)
          kgArr
        } else {
            standardArray
        }
    }

    fun fetchPPBodyParam_BodyFat_StandartArray_percent_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray: List<Float> = intervalRange(old, listOf(5f, 75f))
        return if (isWeight) {
            val kgArr: List<Float> = percent2kg(standardArray)
            kgArr
        } else {
           standardArray
        }
    }

    /**
     * 水分 【3段】【量、率】
     */
    fun fetchPPBodyParam_Water_StandartArray_4_D_typeWeight(isWeight: Boolean): List<Float> {
        val old: List<Float> = constWaterRate()
        val standardArray: List<Float> = intervalRange(old, listOf(35f, 75f))

        return if (isWeight) {
            val kgArr: List<Float> = percent2kg(standardArray)
            kgArr
        } else {
            standardArray
        }
    }

    fun fetchPPBodyParam_Water_StandartArray_percent_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray: List<Float> = intervalRange(old, listOf(35f, 75f))

        return if (isWeight) {
            val kgArr: List<Float> = percent2kg(standardArray)
            kgArr
        } else {
            standardArray
        }
    }

    // 肌肉 【3段】【量、率】
    fun fetchPPBodyParam_Mus_StandartArray_kg_4_D_typeWeight(isWeight: Boolean): List<Float> {
        val old: List<Float> = constMuscleWeightKg()
        val standardArray: List<Float> = intervalRange(old, listOf(10f, 120f))

        return if (isWeight) {
            standardArray
        } else {
            val kgArr: List<Float> = kg2percent(standardArray)
            kgArr
        }
    }

    fun fetchPPBodyParam_Mus_StandartArray_kg_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray: List<Float> = intervalRange(old, listOf(10f, 120f))

        return if (isWeight) {
            standardArray
        } else {
            val kgArr: List<Float> = kg2percent(standardArray)
            kgArr
        }
    }

    // 骨量 【3段】【量】
    fun fetchPPBodyParam_Bone_StandartArray_kg_4_D(): List<Float> {
        val old: List<Float> = constBoneWeightKg()
        val standardArray: List<Float> = intervalRange(old, listOf(0.5f, 8f))
        return standardArray
    }

    fun fetchPPBodyParam_Bone_StandartArray_kg_4_A(old: List<Float>): List<Float> {
        val standardArray: List<Float> = intervalRange(old, listOf(0.5f, 8f))
        return standardArray
    }


    // BMR 【4电极2段】【8电极3段】【kcal】
    fun fetchPPBodyParam_BMR_StandartArray_kcal_4_D(): List<Float> {
        val old: List<Float> = constBMR()
        val standardArray: List<Float> = intervalRange(old, listOf(500f, 5000f))
        return standardArray
    }

    fun fetchPPBodyParam_BMR_StandartArray_kcal_4_A(old: List<Float>): List<Float> {
        val standardArray: List<Float> = intervalRange(old, listOf(500f, 5000f))
        return standardArray
    }

    // BMI 【4电极4段】【8电极3段】
    fun fetchPPBodyParam_BMI_StandartArray_4(): List<Float> {
        return listOf(10f, 18.5f, 25f, 30f, 50f)
    }

    // BMI 【4电极4段】【8电极3段】
    fun fetchPPBodyParam_BMI_StandartArray_8(): List<Float> {
        return listOf(10f, 18.5f, 23f, 50f)
    }

    // 内脏脂肪等级 【3段】
    fun fetchPPBodyParam_VisFat_StandartArray_4(): List<Float> {
        return listOf(1f, 10f, 15f, 50f)
    }

    // 蛋白质 【3段】【量、率】
    fun fetchPPBodyParam_Protein_StandartArray_4_D_typeWeight(isWeight: Boolean): List<Float> {
        val old: List<Float> = constproteinPercentage()
        val standardArray: List<Float> = intervalRange(old, listOf(2f, 30f))

        if (isWeight) {
            val kgArr: List<Float> = percent2kg(standardArray)
            return kgArr
        } else {
            return standardArray
        }
    }

    fun constproteinPercentage(): List<Float> {
        return listOf(16f, 18f)
    }

    fun fetchPPBodyParam_Protein_StandartArray_percent_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray: List<Float> = intervalRange(old, listOf(2f, 30f))

        if (isWeight) {
            val kgArr: List<Float> = percent2kg(standardArray)
            return kgArr
        } else {
            return standardArray
        }
    }

    // 皮下脂肪 【3段】【量、率】
    fun fetchPPBodyParam_BodySubcutaneousFat_StandartArray_4_D_typeWeight(isWeight: Boolean): List<Float> {
        val old: List<Float> = subcutaneousFatPercentage()
        val standardArray: List<Float> = intervalRange(old, listOf(0.1f, 60f))

        if (isWeight) {
            val kgArr: List<Float> = percent2kg(standardArray)
            return kgArr
        } else {
            return standardArray
        }
    }

    fun fetchPPBodyParam_BodySubcutaneousFat_StandartArray_percent_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray: List<Float> = intervalRange(old, listOf(0.1f, 60f))
        if (isWeight) {
            val kgArr: List<Float> = percent2kg(standardArray)
            return kgArr
        } else {
            return standardArray
        }
    }

    // 骨骼肌 【3段】【量、率】
    fun fetchPPBodyParam_BodySkeletal_StandartArray_4_D_typeWeight(isWeight: Boolean): List<Float> {
        val old: List<Float> = skeletalMuscleKg()
        val standardArray: List<Float> = intervalRange(old, listOf(8f, 100f))

        if (isWeight) {
            return standardArray
        } else {
            val kgArr: List<Float> = kg2percent(standardArray)
            return kgArr
        }
    }

    fun fetchPPBodyParam_BodySkeletal_StandartArray_kg_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray: List<Float> = intervalRange(old, listOf(8f, 100f))

        if (isWeight) {
            return standardArray
        } else {
            val kgArr: List<Float> = kg2percent(standardArray)
            return kgArr
        }
    }


    fun fetchPPBodyParam_heart_StandartArray(): List<Float> {
        return listOf(0f, 55f, 80f, 100f, 200f)
    }

    fun fetchPPBodyParam_physicalAgeValue_StandartArray(): List<Int> {
        val standValue: Int = fatModel.ppAge
        val point0: Int = 0
        val point1: Int = standValue
        val point2: Int = standValue * 2

        return listOf(point0, point1, point2)
    }

    fun fetchPPBodyParam_BodyScore_StandartArray(): List<Int> {
        return listOf(60, 70, 80, 90, 100)
    }

    fun fetchPPBodyParam_StandartArray_8(old:List<Float>): List<Float> {
        val standardArray: List<Float> = intervalRange(old, listOf())
        return standardArray
    }

    fun skeletalMuscleKg(): List<Float> {
        return listOf(20f, 35f)
    }

    fun subcutaneousFatPercentage(): List<Float> {
        val gender: PPUserGender = fatModel.ppSex

        if (gender == PPUserGender.PPUserGenderMale) {
            return listOf(8.6f, 16.7f)
        } else {
            return listOf(18.5f, 26.7f)
        }
    }

    fun constBMR(): List<Float> {
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

    fun constMuscleWeightKg(): List<Float> {
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

    fun constWaterRate(): List<Float> {
        val gender: PPUserGender = fatModel.ppSex
        val standardArray: List<Float>

        if (gender == PPUserGender.PPUserGenderMale) {
            standardArray = listOf(55f, 65f)
        } else {
            standardArray = listOf(45f, 60f)
        }

        return standardArray
    }

    fun constBoneWeightKg(): List<Float> {
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

    fun constBodyFatRate(): List<Float> {
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

    // 数据常量 - 体重（公斤）
    fun constWeightKg(): List<Float> {
        val point1 = idealWeight * 0.9f
        val point2 = idealWeight * 1.1f
        return listOf(point1, point2)
    }


}
