package com.peng.ppscale.util;

import android.text.TextUtils;

import com.peng.ppscale.business.device.DeviceManager;
import com.peng.ppscale.business.device.PPUnitType;

import static com.peng.ppscale.business.device.PPUnitType.*;

public class UnitUtil {

    public static int getUnitInt(PPUnitType unitType, String scaleName) {
        switch (unitType) {
            case Unit_KG:
                return Unit_KG.getType();
            case Unit_LB:
                return Unit_LB.getType();
            case PPUnitST_LB:
                return PPUnitST_LB.getType();
            case PPUnitST:
                return !TextUtils.isEmpty(scaleName) && scaleName.equals(DeviceManager.HEALTH_SCALE3) ? PPUnitST_LB.getType() : PPUnitST.getType();
            case PPUnitJin:
                return PPUnitJin.getType();
            case PPUnitG:
                return PPUnitG.getType();
            case PPUnitLBOZ:
                return PPUnitLBOZ.getType();
            case PPUnitOZ:
                return PPUnitOZ.getType();
            case PPUnitMLWater:
                return PPUnitMLWater.getType();
            case PPUnitMLMilk:
                return PPUnitMLMilk.getType();
            case PPUnitFL_OZ_MILK:
                return PPUnitFL_OZ_MILK.getType();
            case PPUnitFL_OZ_WATER:
                return PPUnitFL_OZ_WATER.getType();
            default:
                return Unit_KG.getType();
        }
    }

    /**
     * PPUnitKG = 0,
     * PPUnitLB = 1,
     * PPUnitST = 2,
     * PPUnitJin = 3,
     * PPUnitG = 4,
     * PPUnitLBOZ = 5,
     * PPUnitOZ = 6,
     * PPUnitMLWater = 7,
     * PPUnitMLMilk = 8,
     * PPUnitST_LB = 9,
     */
    public static PPUnitType getUnitType(int unit) {
        switch (unit) {
            case 0:
                return Unit_KG;
            case 1:
                return Unit_LB;
            //Health Scale3代表st 其余代表st_lb
            case 2:
                return PPUnitST_LB;
            case 3:
                return PPUnitJin;
            case 4:
                return PPUnitG;
            case 5:
                return PPUnitLBOZ;
            case 6:
                return PPUnitOZ;
            case 7:
                return PPUnitMLWater;
            case 8:
                return PPUnitMLMilk;
            case 9:
                return PPUnitFL_OZ_WATER;
            case 10:
                return PPUnitFL_OZ_MILK;
            case 11:
                return PPUnitST;
            default:
                return Unit_KG;
        }
    }

   /*ElectronicScale单位*/
    public static int electronicUnitEnum2Int(PPUnitType unitType) {
        int unit = 0;
        switch (unitType) {
            case Unit_KG:
                unit = 1;
                break;
            case Unit_LB:
                unit = 2;
                break;
            case PPUnitJin:
                unit = 1;
                break;
            case PPUnitLBOZ:
                unit = 4;
                break;
        }
        return unit;
    }

    /*CF568单位切换*/
    public static PPUnitType unitTorre2PPUnit(int unit) {
        switch (unit) {
            case 0:
                return Unit_KG;
            case 1:
                return Unit_LB;
            case 2:
                return PPUnitJin;
            case 3:
                return PPUnitST;
            case 4:
                return PPUnitST_LB;
            default:
                return Unit_KG;
        }
    }

    public static int unitTorre2Int(PPUnitType unitType) {
        switch (unitType) {
            case Unit_KG:
                return 0;
            case Unit_LB:
                return 1;
            case PPUnitJin:
                return 2;
            case PPUnitST:
                return 3;
            case PPUnitST_LB:
                return 4;
            default:
                return 0;
        }
    }


}
