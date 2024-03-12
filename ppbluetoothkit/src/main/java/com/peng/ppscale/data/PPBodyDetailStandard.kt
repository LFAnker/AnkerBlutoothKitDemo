package com.peng.ppscale.data

import com.peng.ppscale.vo.PPBodyFatModel

object PPBodyDetailStandard {

    lateinit var fatModel: PPBodyFatModel
    var idealWeight:Float = 0f
    var weight:Float = 0f

    fun initWithBodyFatModel(fatModel: PPBodyFatModel) {
        this.fatModel = fatModel
        idealWeight = 22 * Math.pow(fatModel.ppHeightCm / 100.0, 2.0).toFloat()
        this.weight = fatModel.ppWeightKg
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

    /**
     * @param isWeight 是否是重量
     * @param old 传进来的是体重的标准
     */
    fun fetchPPBodyParam_Mus_StandartArray_kg_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray = intervalRange(old, listOf(10.0f, 120.0f))
        return if (isWeight) {
            standardArray
        } else {
            val kgArr = kg2percent(standardArray)
            return kgArr
        }
    }

    fun fetchPPBodyParam_Water_StandartArray_percent_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray = intervalRange(old, listOf(35.0f, 75.0f))
        return if (isWeight) {
            percent2kg(standardArray)
        } else {
            standardArray
        }
    }

    fun fetchPPBodyParam_Bone_StandartArray_kg_4_A(old: List<Float>): List<Float> {
        val standardArray = intervalRange(old, listOf(0.5f, 8.0f))
        return standardArray
    }

    fun fetchPPBodyParam_BodyFat_StandartArray_percent_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray = intervalRange(old, listOf(5.0f, 75.0f))
        return if (isWeight) {
            percent2kg(standardArray)
        } else {
            standardArray
        }
    }

    fun fetchPPBodyParam_BodySkeletal_StandartArray_kg_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray = intervalRange(old, listOf(8.0f, 100.0f))
        return if (isWeight) {
            standardArray
        } else {
            return kg2percent(standardArray)
        }
    }

    fun fetchPPBodyParam_Protein_StandartArray_percent_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray = intervalRange(old, listOf(2.0f, 30.0f))
        return if (isWeight) {
            percent2kg(standardArray)
        } else {
            standardArray
        }
    }

    fun fetchPPBodyParam_BodySubcutaneousFat_StandartArray_percent_4_A(old: List<Float>, isWeight: Boolean): List<Float> {
        val standardArray = intervalRange(old, listOf(0.1f, 60.0f))
        return if (isWeight) {
            percent2kg(standardArray)
        } else {
            standardArray
        }
    }

    fun fetchPPBodyParam_BMR_StandartArray_kcal_4_A(old: List<Float>): List<Float> {
        val standardArray = intervalRange(old, listOf(500.0f, 5000.0f))
        return standardArray
    }


    // 体脂 【4电极5段 8电极3段】 【量、率】
    fun fetchPPBodyParam_BodyFat_StandartArray_4_D_typeWeight(isWeight: Boolean): List<Float> {
        val old: List<Float> = constBodyFatRate()
        val standardArray: List<Float> = intervalRange(old, listOf(5f, 75f))
        return if (isWeight) {
            percent2kg(standardArray)
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
            kg2percent(standardArray)
        }
    }

    // 骨骼肌 【3段】【量、率】
    fun fetchPPBodyParam_BodySkeletal_StandartArray_4_D_typeWeight(isWeight: Boolean): List<Float> {
        val old: List<Float> = skeletalMuscleKg()
        val standardArray: List<Float> = intervalRange(old, listOf(8f, 100f))
        if (isWeight) {
            return standardArray
        } else {
            return kg2percent(standardArray)
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

    // 蛋白质 【3段】【量、率】
    fun fetchPPBodyParam_Protein_StandartArray_4_D_typeWeight(isWeight: Boolean): List<Float> {
        val old: List<Float> = constproteinPercentage()
        val standardArray: List<Float> = intervalRange(old, listOf(2f, 30f))

        if (isWeight) {
            return percent2kg(standardArray)
        } else {
            return standardArray
        }
    }

    // 皮下脂肪 【3段】【量、率】
    fun fetchPPBodyParam_BodySubcutaneousFat_StandartArray_4_D_typeWeight(isWeight: Boolean): List<Float> {
        val old: List<Float> = subcutaneousFatPercentage()
        val standardArray: List<Float> = intervalRange(old, listOf(0.1f, 60f))
        if (isWeight) {
            return percent2kg(standardArray)
        } else {
            return standardArray
        }
    }

    // BMR 【4电极2段】【8电极3段】【kcal】
    fun fetchPPBodyParam_BMR_StandartArray_kcal_4_D(): List<Float> {
        val old: List<Float> = constBMR()
        val standardArray: List<Float> = intervalRange(old, listOf(500f, 5000f))
        return standardArray
    }

    // 内脏脂肪等级 【3段】
    fun fetchPPBodyParam_VisFat_StandartArray_4(): List<Float> {
        return listOf(1f, 10f, 15f, 50f)
    }

    // 骨量 【3段】【量】
    fun fetchPPBodyParam_Bone_StandartArray_kg_4_D(): List<Float> {
        val old: List<Float> = constBoneWeightKg()
        val standardArray: List<Float> = intervalRange(old, listOf(0.5f, 8f))
        return standardArray
    }

}

enum class CalculateBodyType(val type:Int){
    body270(0),//八电极
    bodyLegs140(1),//双脚四电极
    bodyArms140(2),//双手四电极
    bodyNomal(3)//默认算法
}

