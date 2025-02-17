package com.anker.ppblutoothkit.util;

import com.anker.ppblutoothkit.SettingManager;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.vo.PPBodyBaseModel;
import com.peng.ppscale.vo.PPBodyFatModel;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPUserModel;
import com.peng.ppscale.vo.PPUserGender;

public class DataUtil {
    private static DataUtil dataUtil;
    private PPBodyFatModel bodyDataModel;
    private PPBodyBaseModel bodyBaseModel;
    private PPUserModel userModel;
    private PPDeviceModel deviceModel;
    PPUnitType unit = PPUnitType.Unit_KG;

    public static DataUtil util() {
        if (dataUtil == null) {
            synchronized (DataUtil.class) {
                if (dataUtil == null) {
                    dataUtil = new DataUtil();
                }
            }
        }
        return dataUtil;
    }

    public DataUtil() {

    }

    public PPBodyFatModel getBodyDataModel() {
        return bodyDataModel;
    }

    public void setBodyDataModel(PPBodyFatModel bodyDataModel) {
        this.bodyDataModel = bodyDataModel;
    }

    public PPUserModel getUserModel() {
        userModel = SettingManager.get().getDataObj(SettingManager.USER_MODEL, PPUserModel.class);
        if (userModel == null) {
            userModel = new PPUserModel.Builder()
                    .setAge(18)
                    .setHeight(180)
                    .setSex(PPUserGender.PPUserGenderMale)
                    .setGroupNum(0)
                    .build();
        }
        return userModel;
    }

    public void setUserModel(PPUserModel userModel) {
        SettingManager.get().setDataObj(SettingManager.USER_MODEL, userModel);
    }

    public void setUnit(PPUnitType unit) {
        this.unit = unit;
    }

    public PPUnitType getUnit() {
        return unit;
    }

    public PPDeviceModel getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(PPDeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }


    public PPBodyBaseModel getBodyBaseModel() {
        return bodyBaseModel;
    }

    public void setBodyBaseModel(PPBodyBaseModel bodyBaseModel) {
        this.bodyBaseModel = bodyBaseModel;
    }
}
