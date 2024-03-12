package com.peng.ppscale.util.weight;

import java.math.BigDecimal;

public class UnitSwapStImpl implements IUnitSwap {
    private final double mBasicValue;

    public UnitSwapStImpl(double kg) {
        this.mBasicValue = kg;
    }

    @Override
    public int getIndex() {
        return 2;
    }

    @Override
    public double getValue() {
//        //  // 参照ios
//        // （1 千克(kg)=0.157473044418 英石）
////        return this.mBasicValue * 0.157473044418;
//        double lb = this.mBasicValue * 10 * 22046 / 1000;
//        int st = (int) Math.floor((lb / 14));
////        if (st % 2 != 0) {
////            st++;
////        }
//        return st / 100.0;


        // 参照ios
//        int value = (int) ((this.mBasicValue * 100 + 5) / 10);

        double lb = this.mBasicValue * 10 * 22046 / 1000;

        int stInt = (int) Math.floor((lb / 14));

        if (stInt % 2 != 0) {
            stInt++;
        }
        return stInt / 100.0;

    }

//    @Override
//    public double getBasicValue() {
//        return mBasicValue;
//    }


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
