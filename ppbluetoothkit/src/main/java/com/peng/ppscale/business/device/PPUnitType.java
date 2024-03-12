package com.peng.ppscale.business.device;

public enum PPUnitType {

    /**
     * [0,1,4]
     * 00：表示KG
     * 01 表示LB
     * 02：表示ST_LB
     * 03 表示斤
     * 04：表示g
     * 05 表示lb:oz
     * 06：表示oz
     * 07 表示ml(water)
     * 08: 表示ml（milk）
     * 09: 表示fl_oz（water）
     * 10: 表示fl_oz（milk）
     * 11: 表示ST
     */

    Unit_KG(0),

    Unit_LB(1),

    PPUnitST_LB(2),

    PPUnitJin(3),

    PPUnitG(4),

    PPUnitLBOZ(5),

    PPUnitOZ(6),

    PPUnitMLWater(7),

    PPUnitMLMilk(8),

    PPUnitFL_OZ_WATER(9),

    PPUnitFL_OZ_MILK(10),

    PPUnitST(11);

    private final int type;

    PPUnitType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
