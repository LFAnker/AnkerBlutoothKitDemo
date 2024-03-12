package com.peng.ppscale.util.weight;


import java.math.BigDecimal;

public class UnitSwapStImpl2Point implements IUnitSwap {
    private final double mBasicValue;

    public UnitSwapStImpl2Point(double kg) {
        this.mBasicValue = kg;
//        mBasicValue = 38.05;
    }

    @Override
    public int getIndex() {
        return 2;
    }

    @Override
    public double getValue() {

        // 参照ios
//        int value = (int)Math.round((this.mBasicValue * 100 + 5) / 10);
//
//        float basicValue = value / 10.0f;
//
//        float lb = basicValue * 10 * 22046 / 1000;
//
//        int stInt =(int) Math.floor(lb / 14);
//
//        return stInt / 100.0;


        // 参照ios
//        int value = (int) ((this.mBasicValue * 100 + 5) / 10);

        int value = (int) Math.round(this.mBasicValue * 10);

        float basicValue = value / 10.0f;

        int lbInt = (int) Math.floor(basicValue * 10 * 22046 / 1000);

        float lb = lbInt / 100.0f;

        int stInt = (int) (lb * 100 / 14);

        return stInt / 100.0;


    }

    @Override
    public double toValue() {
        return this.mBasicValue / 0.157473044418f;
    }

    @Override
    public String getUnitString() {
        return "st";
    }

    @Override
    public String toUnitString() {
        return "kg";
    }

    @Override
    public String toString() {
        return keep1Point2(getValue()) + getUnitString();
    }

    /**
     * 四舍五入
     *
     * @param tempKg
     * @return
     */
    public static float keep1Point2(double tempKg) {
        tempKg = (tempKg * 10000 + 5) / 10000;
        BigDecimal b = new BigDecimal(String.valueOf(tempKg));
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

}
