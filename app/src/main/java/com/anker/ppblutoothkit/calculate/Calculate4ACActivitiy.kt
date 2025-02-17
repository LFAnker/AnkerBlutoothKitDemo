package com.anker.ppblutoothkit.calculate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.anker.ppblutoothkit.R
import com.anker.ppblutoothkit.util.UnitUtil
import com.anker.ppblutoothkit.util.DataUtil
import com.peng.ppscale.business.device.PPUnitType
import com.peng.ppscale.util.DeviceUtil
import com.peng.ppscale.vo.*
import kotlinx.android.synthetic.main.activity_calculate_4ac.*
import kotlinx.android.synthetic.main.activity_calculate_8ac.etAge
import kotlinx.android.synthetic.main.activity_calculate_8ac.etHeight
import kotlinx.android.synthetic.main.activity_calculate_8ac.etSex
import kotlinx.android.synthetic.main.activity_calculate_8ac.etWeight

/**
 * 4电极交流算法
 */
class Calculate4ACActivitiy : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_4ac)

        findViewById<Button>(R.id.calculateBtn).setOnClickListener {
            startCalculate()
        }

        initData()
    }

    private fun initData() {

        val tag = intent.getStringExtra("bodyDataModel")
        if (tag != null) {
            //显示称重完成后的数据
            val bodyBaseModel = DataUtil.util().bodyBaseModel
            etSex.setText(if (bodyBaseModel?.userModel?.sex == PPUserGender.PPUserGenderFemale) "0" else "1")
            etHeight.setText(bodyBaseModel?.userModel?.userHeight.toString())
            etAge.setText(bodyBaseModel?.userModel?.age.toString())
            etWeight.setText(bodyBaseModel?.getPpWeightKg().toString())
            etImpedance.setText(bodyBaseModel?.impedance.toString())
        }
    }

    private fun startCalculate() {
        val sex = if (etSex.text?.toString()?.toInt() == 0) {
            PPUserGender.PPUserGenderFemale
        } else {
            PPUserGender.PPUserGenderMale
        }
        val height = etHeight.text?.toString()?.toInt() ?: 180
        val age = etAge.text?.toString()?.toInt() ?: 28
        val weight = etWeight.text?.toString()?.toDouble() ?: 70.00
        val impedance = etImpedance.text?.toString()?.toLong() ?: 4195332L

        val userModel = PPUserModel.Builder()
            .setSex(sex) //gender
            .setHeight(height)//height 100-220
            .setAge(age)//age 10-99
            .build()

        val bodyBaseModel = PPBodyBaseModel()
        bodyBaseModel.weight = UnitUtil.getWeight(weight)
        bodyBaseModel.impedance = impedance
        bodyBaseModel.userModel = userModel
        bodyBaseModel.unit = PPUnitType.Unit_KG

        val ppBodyFatModel = PPBodyFatModel(bodyBaseModel)

        DataUtil.util().bodyDataModel = ppBodyFatModel
        Log.d("liyp_", ppBodyFatModel.toString())

        val intent = Intent(this, BodyDataDetailActivity::class.java)
        startActivity(intent)
    }

}