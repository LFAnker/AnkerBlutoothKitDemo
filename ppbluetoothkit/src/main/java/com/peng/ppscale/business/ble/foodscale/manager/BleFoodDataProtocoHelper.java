package com.peng.ppscale.business.ble.foodscale.manager;

import com.peng.ppscale.business.device.PPUnitType;

import java.util.Arrays;
import java.util.List;

public class BleFoodDataProtocoHelper {

    /*ElectronicScale单位*/
    public static PPUnitType electronicUnitInt11Enum(int enumUnit) {
        switch (enumUnit) {
            case 0:
                return PPUnitType.Unit_KG;
            case 1:
                return PPUnitType.Unit_LB;
            case 2:
                return PPUnitType.PPUnitST;
            case 3:
                return PPUnitType.PPUnitJin;
            case 4:
                return PPUnitType.PPUnitG;
            case 5:
                return PPUnitType.PPUnitLBOZ;
            case 6:
                return PPUnitType.PPUnitOZ;
            case 7:
                return PPUnitType.PPUnitMLWater;
            case 8:
                return PPUnitType.PPUnitMLMilk;
            case 9:
                return PPUnitType.PPUnitFL_OZ_WATER;
            case 10:
                return PPUnitType.PPUnitFL_OZ_MILK;
            default:
                return PPUnitType.PPUnitG;
        }
    }

    /**
     * ElectronicScale单位 16字节
     * <p>
     * 00表示g
     * 01表示LB：oz
     * 02：表示(water)ml
     * 03表示fl.oz.
     * 4：表示（milk）ml
     *
     * @param enumUnit
     * @return
     */
    public static PPUnitType electronicUnitInt16Enum(int enumUnit) {
        switch (enumUnit) {
            case 0:
                return PPUnitType.PPUnitG;
            case 1:
                return PPUnitType.PPUnitLBOZ;
            case 2:
                return PPUnitType.PPUnitMLWater;
            case 3:
                return PPUnitType.PPUnitOZ;
            case 4:
                return PPUnitType.PPUnitMLMilk;
            default:
                return PPUnitType.PPUnitG;
        }
    }

    /*ElectronicScale单位*/
    public static int electronicUnitEnum11Int(PPUnitType unitType) {
        int unit = 4;
        switch (unitType) {
            case Unit_KG:
                unit = 0;
                break;
            case Unit_LB:
                unit = 1;
                break;
            case PPUnitST:
                unit = 2;
                break;
            case PPUnitJin:
                unit = 3;
                break;
            case PPUnitG:
                unit = 4;
                break;
            case PPUnitLBOZ:
                unit = 5;
                break;
            case PPUnitOZ:
                unit = 6;
                break;
            case PPUnitMLWater:
                unit = 7;
                break;
            case PPUnitMLMilk:
                unit = 8;
                break;
            case PPUnitFL_OZ_WATER:
                unit = 9;
                break;
            case PPUnitFL_OZ_MILK:
                unit = 10;
                break;
        }
        return unit;
    }


    /**
     * ElectronicScale单位 16字节
     * <p>
     * 00表示g
     * 01表示LB：oz
     * 02：表示(water)ml
     * 03表示fl.oz.
     * 4：表示（milk）ml
     *
     * @param enumUnit
     * @return
     */
    protected static int electronicUnitEnum16Int(PPUnitType enumUnit) {
        switch (enumUnit) {
            case PPUnitG:
                return 0;
            case PPUnitLBOZ:
                return 1;
            case PPUnitMLWater:
                return 2;
            case PPUnitOZ:
                return 3;
            case PPUnitMLMilk:
                return 4;
            default:
                return 0;
        }
    }

    public static List<String> ScaleUnitList = Arrays.asList(
            "kg",
            "lb",
            "st",
            "斤",
            "g",
            "lb:oz",
            "oz",
            "ml(water)",
            "ml(milk)",
            "fl oz(water)",
            "fl oz(milk)"
    );


}
