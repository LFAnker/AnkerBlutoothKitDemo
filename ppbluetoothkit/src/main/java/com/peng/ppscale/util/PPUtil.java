package com.peng.ppscale.util;

import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.vo.PPScaleDefine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

public class PPUtil {

    /**
     * 四舍五入
     *
     * @param tempKg
     * @return
     */
    public static float keepPoint1(double tempKg) {
        BigDecimal b = new BigDecimal(String.valueOf(tempKg));
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

    public static float keepPoint1f(float tempKg) {
        BigDecimal b = new BigDecimal(String.valueOf(tempKg));
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

    public static double keepPoint1D(double tempKg) {
        BigDecimal b = new BigDecimal(String.valueOf(tempKg));
        double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    public static String keepPoint1S(double tempKg) {
        BigDecimal b = new BigDecimal(String.valueOf(tempKg));
        double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//        DecimalFormat decimalFormat = new DecimalFormat("#.#");
//        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
//        return decimalFormat.format(tempKg);
        return String.format(Locale.US, "%.1f", f1);
    }

    /**
     * 四舍五入 保留0位
     *
     * @param tempKg
     * @return
     */
    public static float keepPoint0(double tempKg) {
        BigDecimal b = new BigDecimal(String.valueOf(tempKg));
        float f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

    public static double formatDouble1(double d) {
        return (double) Math.round(d * 100) / 100;
    }

    /**
     * 不四舍五入
     * <p>
     *
     * @param tempKg
     * @return
     */
    public static float keepPoint1_(double tempKg) {
        int intValue = (int) (tempKg * 10);
        double value = (double) intValue / 10;
        BigDecimal b = new BigDecimal(String.valueOf(value));
        float f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

    /**
     * 四舍五入 两位
     *
     * @param tempKg
     * @return
     */
    public static float keepPoint2(double tempKg) {
        tempKg = (tempKg * 10000 + 5) / 10000;
        BigDecimal b = new BigDecimal(String.valueOf(tempKg));
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

    /**
     * 四舍五入 两位
     *
     * @param tempKg
     * @return
     */
    public static double keepPoint2D(double tempKg) {
        tempKg = (tempKg * 10000 + 5) / 10000;
        BigDecimal b = new BigDecimal(String.valueOf(tempKg));
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * 四舍五入 两位
     *
     * @param tempKg
     * @return
     */
    public static String keepPoint2S(double tempKg) {
        tempKg = (tempKg * 10000 + 5) / 10000;
        BigDecimal b = new BigDecimal(String.valueOf(tempKg));
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.format(Locale.US, "%.2f", f1);
    }

    /**
     * 传入kg，根据重量单位得出相应值
     * 体脂秤一直是kg
     * 食物秤需要采用秤返回的单位和重量
     *
     * @param weightValue
     * @param accuracyType 体脂秤蓝牙名称
     * @return
     */
    public static String getWeight(PPUnitType unit, double weightValue, int accuracyType) {
        if (unit == PPUnitType.Unit_KG) {
            return getWeightValueD(unit, weightValue, accuracyType) + "kg";
        } else if (unit == PPUnitType.Unit_LB) {
            return getWeightValueD(unit, weightValue, accuracyType) + "lb";
        } else if (unit == PPUnitType.PPUnitST) {
            return getWeightValueD(unit, weightValue, accuracyType) + "st";
        } else if (unit == PPUnitType.PPUnitJin) {
            return kgToJinS(weightValue) + "斤";
        } else if (unit == PPUnitType.PPUnitG) {
            return weightValue + "g";
        } else if (unit == PPUnitType.PPUnitLBOZ) {
            return weightValue + "lb:oz";
        } else if (unit == PPUnitType.PPUnitOZ) {
            return weightValue + "oz";
        } else if (unit == PPUnitType.PPUnitMLWater) {
            return weightValue + "water";
        } else if (unit == PPUnitType.PPUnitMLMilk) {
            return weightValue + "milk";
        } else {
            return weightValue + "kg";
        }
    }

    public static String getWeight(PPUnitType unit, double weightValue) {
        if (unit == PPUnitType.Unit_KG) {
            return getWeightValue(unit, weightValue) + "kg";
        } else if (unit == PPUnitType.Unit_LB) {
            return getWeightValue(unit, weightValue) + "lb";
        } else if (unit == PPUnitType.PPUnitST) {
            return getWeightValue(unit, weightValue) + "st";
        } else if (unit == PPUnitType.PPUnitJin) {
            return kgToJinS(weightValue) + "斤";
        } else if (unit == PPUnitType.PPUnitG) {
            return weightValue + "g";
        } else if (unit == PPUnitType.PPUnitLBOZ) {
            return weightValue + "lb:oz";
        } else if (unit == PPUnitType.PPUnitOZ) {
            return weightValue + "oz";
        } else if (unit == PPUnitType.PPUnitMLWater) {
            return weightValue + "water";
        } else if (unit == PPUnitType.PPUnitMLMilk) {
            return weightValue + "milk";
        } else {
            return weightValue + "kg";
        }
    }

    public static String getWeightUnit(PPUnitType unit) {
        if (unit == PPUnitType.Unit_KG) {
            return "kg";
        } else if (unit == PPUnitType.Unit_LB) {
            return "lb";
        } else if (unit == PPUnitType.PPUnitST) {
            return "st";
        } else if (unit == PPUnitType.PPUnitJin) {
            return "斤";
        } else if (unit == PPUnitType.PPUnitG) {
            return "g";
        } else if (unit == PPUnitType.PPUnitLBOZ) {
            return "lb:oz";
        } else if (unit == PPUnitType.PPUnitOZ) {
            return "oz";
        } else if (unit == PPUnitType.PPUnitMLWater) {
            return "water";
        } else if (unit == PPUnitType.PPUnitMLMilk) {
            return "milk";
        } else {
            return "kg";
        }
    }

    public static int getWeightUnitNum(PPUnitType unit) {
        if (unit == PPUnitType.Unit_KG) {
            return 0;
        } else if (unit == PPUnitType.Unit_LB) {
            return 1;
        } else if (unit == PPUnitType.PPUnitST) {
            return 2;
        } else if (unit == PPUnitType.PPUnitJin) {
            return 3;
        } else if (unit == PPUnitType.PPUnitG) {
            return 4;
        } else if (unit == PPUnitType.PPUnitLBOZ) {
            return 5;
        } else if (unit == PPUnitType.PPUnitOZ) {
            return 6;
        } else if (unit == PPUnitType.PPUnitMLWater) {
            return 7;
        } else if (unit == PPUnitType.PPUnitMLMilk) {
            return 8;
        } else {
            return 0;
        }
    }

    /**
     * 传入kg，根据重量单位得出相应值 保留一位小数
     *
     * @param htWeightKg
     * @return
     */
    public static float getWeightValue(PPUnitType unit, double htWeightKg) {
        if (unit == PPUnitType.Unit_KG) {
            return keepPoint1(htWeightKg);
        } else if (unit == PPUnitType.Unit_LB) {
            return kgToLB_ForFatScale(htWeightKg);
        } else if (unit == PPUnitType.PPUnitST) {
            return keepPoint2(kgToSt2_Point2(htWeightKg));
        } else {
            return kgToJin(htWeightKg);
        }
    }

    public static String getWeightValueD(PPUnitType unit, double htWeightKg, int accuracyType) {
        if (isTwoPointScale(accuracyType)) {
            if (unit == PPUnitType.Unit_KG) {
                if (htWeightKg < 100) {
                    return keepPoint2S(htWeightKg);
                } else {
                    return keepPoint1S(htWeightKg);
                }
            } else if (unit == PPUnitType.Unit_LB) {
                return PPUtilHelper.INSTANCE.kgToLB(htWeightKg, 0.05);
            } else if (unit == PPUnitType.PPUnitST) {
                return PPUtilHelper.INSTANCE.kgToSt2_Point2D(htWeightKg);
            } else if (unit == PPUnitType.PPUnitJin) {
                return kgToJinS(htWeightKg);
            } else if (unit == PPUnitType.PPUnitST_LB) {
                return kgToSt2LB_(htWeightKg);
            }
        } else {
            if (unit == PPUnitType.Unit_KG) {
                return keepPoint1S(htWeightKg);
            } else if (unit == PPUnitType.Unit_LB) {
                return PPUtilHelper.INSTANCE.kgToLB(htWeightKg, 0.1);
            } else if (unit == PPUnitType.PPUnitST) {
                return kgToSt_Point2D(htWeightKg);
            } else if (unit == PPUnitType.PPUnitJin) {
                return kgToJinS(htWeightKg);
            } else if (unit == PPUnitType.PPUnitST_LB) {
                return kgToSt2LB_(htWeightKg);
            }
        }
        return keepPoint1S(htWeightKg);
    }

    private static boolean isTwoPointScale(int accuracyType) {
//        return !TextUtils.isEmpty(scaleType) && (DeviceUtil.Point2_Scale_List.contains(scaleType) || Constant.ScaleType.Point2_Scale_List.contains(scaleType) || scaleType.endsWith("005"));
        return accuracyType == PPScaleDefine.PPDeviceAccuracyType.PPDeviceAccuracyTypePoint005.getType();
    }

    /**
     * 普通的Kg->lb 并保留一位小数
     *
     * @param tempKg
     * @return
     */
    public static float kgToLB_ForFatScale(double tempKg) {
        if (0 == tempKg) return 0.0f;
        BigDecimal b0 = new BigDecimal(String.valueOf(tempKg));
        BigDecimal b1 = new BigDecimal("1155845");
        BigDecimal b2 = new BigDecimal("16");
        BigDecimal b4 = new BigDecimal("65536");
        BigDecimal b5 = new BigDecimal("2");
        BigDecimal b3 = new BigDecimal(String.valueOf(b0.multiply(b1).doubleValue())).divide(b2, 5, BigDecimal.ROUND_HALF_EVEN).divide(b4, 1, BigDecimal.ROUND_HALF_UP);
        return b3.multiply(b5).floatValue();
    }

    /**
     * 分度值为0.01st kg是2位小数的秤, kg->st
     *
     * @param
     * @return
     */
    public static float kgToSt2_Point2(double tempKg1) {
        BigDecimal tempKg = BigDecimal.valueOf(tempKg1);
        BigDecimal lbInt = tempKg.multiply(new BigDecimal("100")).add(new BigDecimal("5"))
                .divide(new BigDecimal("10"), 0, RoundingMode.DOWN)
                .multiply(new BigDecimal("22046")).divide(new BigDecimal("1000"), 0, RoundingMode.DOWN);
        BigDecimal stInt = lbInt.divide(new BigDecimal("14"), 0, RoundingMode.DOWN);
//        int lbInt = (int) Math.floor((int) ((tempKg * 100 + 5) / 10) * 22046 / 1000);
//        int stInt = lbInt / 14;
        BigDecimal stIntDecimal = stInt.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return keepPoint2(stIntDecimal.floatValue());
    }

    public static String kgToSt2LB_(double tempKg) {
        BigDecimal kgValue = BigDecimal.valueOf(tempKg);
        BigDecimal bigDecimal1000 = BigDecimal.valueOf(1000);
        BigDecimal l = kgValue.multiply(bigDecimal1000);
        BigDecimal valueOf50 = BigDecimal.valueOf(50);
        long toLong = l.add(valueOf50).longValue();
        long lbWeight = (toLong / 100 * 100 * 22046) / 10000;
        long stValue = lbWeight / 14000;
        long lbValue = lbWeight % 14000;
        BigDecimal bigDecimal = BigDecimal.valueOf(lbValue);
        BigDecimal bigDecimal1 = new BigDecimal("1000");
        BigDecimal divide = bigDecimal.divide(bigDecimal1, 1, RoundingMode.DOWN);
        return String.format(Locale.getDefault(), "%d:%s", stValue, divide);
    }

    /**
     * 分度值为0.02st kg是1位小数的秤, kg->st
     *
     * @param tempKg
     * @return
     */
    public static String kgToSt_Point2D(double tempKg) {
        int lbInt = (int) Math.floor((int) ((tempKg * 100 + 5) / 10) * 22046 / 1000);
        int stInt = lbInt / 14;
        if (stInt % 2 != 0) {
            stInt++;
        }
        return keepPoint2S(stInt / 100.0);

    }

    /**
     * kg是2位小数的秤, kg->jin
     *
     * @param kg
     * @return
     */
    public static String kgToJinPoint2D(double kg) {
        return keepPoint2S(kg * 2);
    }

    /**
     * kg是1位小数的秤, kg->jin
     *
     * @param lb
     * @return
     */
    public static float kgToJin(double lb) {
        double kg = lb * 2;
        return keepPoint1(kg);
    }

    /**
     * kg是1位小数的秤, kg->jin
     *
     * @param kg
     * @return
     */
    public static String kgToJinS(double kg) {
        return keepPoint1S(kg * 2);
    }


}
