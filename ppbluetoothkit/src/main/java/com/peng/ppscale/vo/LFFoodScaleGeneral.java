package com.peng.ppscale.vo;

import com.peng.ppscale.business.ble.foodscale.manager.BleFoodDataProtocoHelper;
import com.peng.ppscale.business.device.PPUnitType;

public class LFFoodScaleGeneral {

    private double lfWeightKg;      //重量
    private PPUnitType unit;   //单位
    private int byteNum;   //11字节和16字节 由于秤蓝牙名称一样，所以根据自己长度分辨秤类型
    private int thanZero; //正负 0表示负值 1 正值
    private String scaleType;//秤类型，根据类型判断单位对应的精度

    public LFFoodScaleGeneral() {
    }

    public LFFoodScaleGeneral(double lfWeightKg, PPUnitType unit) {
        this.lfWeightKg = lfWeightKg;
        this.unit = unit;
    }

    public LFFoodScaleGeneral(double lfWeightKg, PPUnitType unit, int byteNum, String scaleType) {
        this.lfWeightKg = lfWeightKg;
        this.unit = unit;
        this.byteNum = byteNum;
        this.scaleType = scaleType;
    }

    public double getLfWeightKg() {
        return lfWeightKg;
    }

    public void setLfWeightKg(double lfWeightKg) {
        this.lfWeightKg = lfWeightKg;
    }

    public PPUnitType getUnit() {
        return unit;
    }

    public String getUnitStr() {
        return BleFoodDataProtocoHelper.ScaleUnitList.get(BleFoodDataProtocoHelper.electronicUnitEnum11Int(unit));
    }

    public void setUnit(PPUnitType unit) {
        this.unit = unit;
    }

    public int getByteNum() {
        return byteNum;
    }

    public void setByteNum(int byteNum) {
        this.byteNum = byteNum;
    }

    public int getThanZero() {
        return thanZero;
    }

    public void setThanZero(int thanZero) {
        this.thanZero = thanZero;
    }

    public String getScaleType() {
        return scaleType;
    }

    public void setScaleType(String scaleType) {
        this.scaleType = scaleType;
    }

    @Override
    public String toString() {
        return "LFFoodScaleGeneral{" +
                "lfWeightKg=" + lfWeightKg +
                ", unit=" + unit +
                ", byteNum=" + byteNum +
                ", thanZero=" + thanZero +
                ", scaleType='" + scaleType + '\'' +
                '}';
    }
}
