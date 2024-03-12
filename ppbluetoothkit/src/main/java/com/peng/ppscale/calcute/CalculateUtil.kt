package com.peng.ppscale.calcute

import com.peng.ppscale.data.*
import com.peng.ppscale.vo.*
import java.math.BigDecimal

object CalculateUtil {

    /**
     * 计算BMI
     *
     * @param lfWeightKg
     * @param lfHeightCm
     * @return
     */
    fun countLfBmi(bmi: Float): Float {
        return if (bmi >= 10) bmi else 10.0f
    }

    fun calculateBMI(height: Int, weight: Float): Float {
        if (height <= 0) {
            return 10.0f
        }
        val heightM = height / 100.0f
        val bmi = weight / (heightM * heightM)
        val bmiCeil = kotlin.math.round(bmi * 10.0f) / 10.0f

        return if (bmiCeil < 10) {
            10f
        } else {
            bmiCeil
        }
    }

    fun calculateStandardWeightWithHeight(height: Int): Float {
        return (21.75f * Math.pow(height * 0.01, 2.0)).toFloat()
    }

    fun calculateBodyMuscleControl(
        gender: PPUserGender,
        height: Int,
        muscleKg: Float,
        standardWeight: Float
    ): Float {
        val standMuscle = calculateStandMuscle(height, gender, standardWeight)
        val muscleControl = kotlin.math.abs(standMuscle - muscleKg)
        return muscleControl
    }

    fun calculateStandMuscle(height: Int, gender: PPUserGender, standardWeight: Float): Float {
        return if (gender == PPUserGender.PPUserGenderFemale) {
            standardWeight * 0.724f
        } else {
            standardWeight * 0.797f
        }
    }

    fun calculateControlWeightKg(weight: Float, standardWeight: Float): Float {
//        val standardWeight = calculateStandardWeightWithHeight(height)
        val controlWeightKg = kotlin.math.abs(weight - standardWeight)
        val bigDecimal1 = BigDecimal(weight.toString())
        val bigDecimal2 = BigDecimal(standardWeight.toString())
        val result = bigDecimal1.subtract(bigDecimal2)
        return result.toFloat()
    }

    //脂肪控制量
    fun ppFatControlKgWithBodyfatKg(
        bodyfatKg: Float,
        sex: PPUserGender,
        bmi: Float,
        age: Int
    ): Float {

        var control: Float = 0.0f

        if (sex == PPUserGender.PPUserGenderFemale) {
            val B0 = 67.2037f
            val B1 = 0.6351f
            val B2 = -2.6706f
            val B3 = -18.1146f
            val B4 = -0.2374f

            if (bmi <= 21) {
                if (bodyfatKg < 10.5f) {
                    return Math.abs(10.2f - bodyfatKg)
                } else {
                    return 0.0f
                }
            }
            control = B0 + B1 * age + B2 * bmi + B3 * age / bmi + B4 * bmi * bmi / age
        } else {
            val B0 = 24.1589f
            val B1 = -0.6282f
            val B2 = -0.5865f
            val B3 = 9.8772f
            val B4 = -0.368f

            if (bmi <= 22.5) {
                if (bodyfatKg < 8.5f) {
                    return Math.abs(9.0f - bodyfatKg)
                } else {
                    return 0.0f
                }
            }
            control = B0 + B1 * age + B2 * bmi + B3 * age / bmi + B4 * bmi * bmi / age
        }
        return Math.abs(control)
    }

    fun calcuteBodyAge(bmi: Float, bodyAge: Int): Int {
        var physicAge = 0f
        var age = bodyAge.toFloat()

        if (bmi < 22) {
            val temp = (age - 5) + ((22 - bmi) * (5 / (22 - 18.5f)))
            if (Math.abs(temp - age) >= 5) {
                physicAge = age + 5
            } else {
                physicAge = temp
            }
        } else if (bmi == 22f) {
            physicAge = age - 5
        } else if (bmi > 22) {
            val temp = (age - 5) + ((bmi - 22) * (5 / (24.9f - 22)))
            if (Math.abs(temp - age) >= 8) {
                physicAge = age + 8
            } else {
                physicAge = temp
            }
        }

        return (physicAge + 0.005f).toInt()
    }

}
