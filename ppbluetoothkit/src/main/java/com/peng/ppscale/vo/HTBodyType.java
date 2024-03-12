package com.peng.ppscale.vo;

public enum HTBodyType {
    LF_BODY_TYPE_THIN(1),//偏瘦型
    LF_BODY_TYPE_THIN_MUSCLE(2),//偏瘦肌肉型
    LF_BODY_TYPE_MUSCULAR(3),//肌肉发达型
    LF_BODY_TYPE_OBESE_FAT(4),//浮肿肥胖型
    LF_BODY_TYPE_FAT_MUSCLE(5),//偏胖肌肉型
    LF_BODY_TYPE_MUSCLE_FAT(6),//肌肉型偏胖
    LF_BODY_TYPE_LACK_EXERCISE(7),//缺乏运动型
    LF_BODY_TYPE_STANDARD(8),//标准型
    LF_BODY_TYPE_STANDARD_MUSCLE(9);//标准肌肉型

    public int type;

    HTBodyType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
