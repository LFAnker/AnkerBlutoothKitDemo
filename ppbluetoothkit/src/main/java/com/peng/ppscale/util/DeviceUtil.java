package com.peng.ppscale.util;

import com.peng.ppscale.business.device.DeviceManager;

import java.util.Arrays;
import java.util.List;

public class DeviceUtil {

    /**
     * 保留两位小数的称
     */
    public static List<String> Point2_Scale_List = Arrays.asList(
            DeviceManager.HEARTRATE_SCALE,//心率称
            DeviceManager.HEARTRATE_SCALE_SMART_516,//心率称
            DeviceManager.WEIGHT_SCALE, //体重秤
            DeviceManager.WEIGHT_SCALE2, //体重秤
            DeviceManager.BODYFAT_SCALE_1, //helen客户
            DeviceManager.HEALTH_SCALE2,
            DeviceManager.ADORE_SCALE1,
            DeviceManager.ADORE_ANYLOOP_SS01,
            DeviceManager.HEALTH_SCALE5,
            DeviceManager.HEALTH_SCALE6,
            DeviceManager.LF_SC,
            DeviceManager.FL_SCALE,
            DeviceManager.FD_Scale,
            DeviceManager.FD_Scale260H,
            DeviceManager.FW_SCALE,
            DeviceManager.HEARTRATE_SCALE3,
            DeviceManager.BODYFAT_SCALE1_D,
            DeviceManager.ADORE_1_D,
            DeviceManager.CF568,
            DeviceManager.CF568_BG,
            DeviceManager.CF568_FUTULA,
            DeviceManager.CF568_VENUS,
            DeviceManager.CF568_CF587,
            DeviceManager.CF568_CF588,
            DeviceManager.CF568_CF586,
            DeviceManager.CF568_MCF_A2,
            DeviceManager.CF568_CF577,
            DeviceManager.CF568_CF577_FUTULA,
            DeviceManager.CF568_CF577_MIX,
            DeviceManager.CF568_CF568_GD,
            DeviceManager.CF568_TM_315,
            DeviceManager.CF568_ANYLOOP_SS02,
            DeviceManager.CF367_SCALE,
            DeviceManager.EUFY_T9148,
            DeviceManager.EUFY_T9149
    );

    /**
     * @param deviceName
     * @return
     */
    public static int getDeviceType(String deviceName) {
//        if (deviceName.equals(DeviceManager.KITCHEN_SCALE_3)) {
//            return 5;
//        } else
        if (deviceName.equals(DeviceManager.ELECTRONIC_SCALE)) {
            return 4;
        } else if (deviceName.equals(DeviceManager.LFSc)
                || deviceName.equals(DeviceManager.LFAdv_B232)
                || deviceName.equals(DeviceManager.WOLO_KITCHEN)
                || deviceName.equals(DeviceManager.INSMART_589)
                || deviceName.equals(DeviceManager.LF_SMART_SCALE)
                || deviceName.equals(DeviceManager.INSMART_818)
        ) {
            return 3;
        }
        if (devicesOz2Point.contains(deviceName)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static List<String> devicesOz2Point = Arrays.asList(
            DeviceManager.KF_SCALE,
            DeviceManager.KITCHEN_SCALE_1,
            DeviceManager.KITCHEN_SCALE_1_MyiStamp_Scale
    );


}
