package com.peng.ppscale.vo;

/**
 * PPBodyType类型与HTBodyType的int值不对应，要注意，为了保持与老的数据一致
 */
public enum PPBodyDetailType {

    /**
     * 旧版本的合泰对应关系
     * public static final int HTBodyTypeThin = 0;
     * public static final int HTBodyTypeLThinMuscle = 1;
     * public static final int HTBodyTypeMuscular = 2;
     * public static final int HTBodyTypeLackofexercise = 3;
     * public static final int HTBodyTypeStandard = 4;
     * public static final int HTBodyTypeStandardMuscle = 5;
     * public static final int HTBodyTypeObesFat = 6;
     * public static final int HTBodyTypeLFatMuscle = 7;
     * public static final int HTBodyTypeMuscleFat = 8;
     */
    LF_BODY_TYPE_THIN(0),//偏瘦型
    LF_BODY_TYPE_THIN_MUSCLE(1),//偏瘦肌肉型
    LF_BODY_TYPE_MUSCULAR(2),//肌肉发达型
    LF_BODY_TYPE_LACK_EXERCISE(3),//缺乏运动型
    LF_BODY_TYPE_STANDARD(4),//标准型
    LF_BODY_TYPE_STANDARD_MUSCLE(5),//标准肌肉型
    LF_BODY_TYPE_OBESE_FAT(6),//浮肿肥胖型
    LF_BODY_TYPE_FAT_MUSCLE(7),//偏胖肌肉型
    LF_BODY_TYPE_MUSCLE_FAT(8);//肌肉型偏胖


    public int type;

    PPBodyDetailType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
