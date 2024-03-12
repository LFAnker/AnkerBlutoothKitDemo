package com.peng.ppscale.util;

import com.peng.ppscale.vo.PPUserGender;

public class UserUtil {

    /*性别*/
    public static int getEnumSex(PPUserGender enumSex) {
        int sex = 0;
        switch (enumSex) {
            case PPUserGenderMale:
                return  1;
            case PPUserGenderFemale:
                return 0;
        }
        return sex;
    }

    public static PPUserGender getEnumSexType(int sex) {
        PPUserGender gender;
        if (sex == 1) {
            gender = PPUserGender.PPUserGenderMale;
        } else {
            gender = PPUserGender.PPUserGenderFemale;
        }
        return gender;
    }


}
