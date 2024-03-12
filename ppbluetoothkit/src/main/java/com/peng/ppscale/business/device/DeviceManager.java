package com.peng.ppscale.business.device;

import android.text.TextUtils;

import com.peng.ppscale.vo.PPScaleDefine;

import java.util.Arrays;
import java.util.List;

public class DeviceManager {

    public static final String ENERGY_SCALE = "Energy Scale";//脂肪
    public static final String BODYFAT_SCALE = "BodyFat Scale";//脂肪
    public static final String HEALTH_SCALE = "Health Scale";//脂肪
    public static final String SK_SmartScale68 = "1SK-SmartScale68";//脂肪
    public static final String HEALTH_SCALE2 = "Health Scale2";//脂肪   不带离线 helen客户
    public static final String HEALTH_SCALE3 = "Health Scale3";//脂肪   st单位
    public static final String HEALTH_SCALE5 = "Health Scale5";//脂肪   离线  蓝牙WiFi 秤的蓝牙名称 旺凌 516 0.05
    public static final String HEALTH_SCALE6 = "Health Scale6";//脂肪   离线  蓝牙WiFi 秤的蓝牙名称 乐鑫 522 ESP32
    public static final String HEALTH_SCALE7 = "Health Scale7";//脂肪   离线  蓝牙WiFi 秤的蓝牙名称 低精度 旺凌 509
    //    public static final String HEALTH_SCALE7 = "INSPIRE_CF509";//脂肪   离线  蓝牙WiFi 秤的蓝牙名称 低精度 旺凌 509
    public static final String HEARTRATE_SCALE = "HeartRate Scale";//心率、离线
    public static final String HEARTRATE_SCALE_SMART_516 = "Smart scale CF516";//心率、离线
    public static final String HEARTRATE_SCALE1 = "HeartRate Scale1";//心率、离线 精度0.1kg  0.2lb
    public static final String HEARTRATE_SCALE2 = "HeartRate Scale2";//蓝牙wifi秤（HEALTH_SCALE7） + 心率、离线 精度0.1kg  0.2lb 0.01st, 配网是根据HEALTH_SCALE5
    public static final String HEARTRATE_SCALE3 = "HeartRate Scale3";//蓝牙wifi秤（HEALTH_SCALE7） + 心率、离线 精度0.05kg  0.2lb 0.01st, 配网是根据HEALTH_SCALE5
    public static final String BM_SCALE = "BM Scale";    //闭目单脚
    public static final String ELECTRONIC_SCALE = "Electronic Scale";//秤端计算，脂肪, 直连
    public static final String ELECTRONIC_SCALE1 = "Electronic Scale1";//秤端计算，脂肪, 直连 CC客户，脂肪和骨骼的单位展示不同
    public static final String CF367_SCALE = "LEFU_SCALE CF376";//三星，秤端计算，脂肪，直连
    public static final String ADORE_SCALE = "ADORE";//离线存储
    public static final String ADORE_SCALE1 = "ADORE1";//离线存储 两位小数
    public static final String ADORE_ANYLOOP_SS01 = "Anyloop_SS01";//ADORE1改名称离线存储
    public static final String LF_SCALE = "LFScale";//脂肪 纯广播秤
    public static final String BF_SCALE = "BFScale";//脂肪  纯广播秤
    public static final String HUMAN_SCALE = "Human Scale";//人体秤
    public static final String WEIGHT_SCALE = "Weight Scale";//人体秤 带离线
    public static final String WEIGHT_SCALE1 = "Weight Scale1";//人体秤 不带离线 单精度
    public static final String WEIGHT_SCALE2 = "Weight Scale2";//人体秤 不带离线 高精度
    public static final String BODYFAT_SCALE_1 = "BodyFat Scale1";//心率秤，不带心率 只有kg,高精度516
    public static final String LF_SC = "LF_SC";//脂肪  纯广播秤
    public static final String WF_SCALE = "WFScale";//人体秤  巨微纯广播体重秤 0.1kg
    public static final String FL_SCALE = "FLScale";//脂肪 巨微纯广播秤  0.05kg
    public static final String FW_SCALE = "FWScale";//体秤  巨微纯广播体重秤  0.05kg
    public static final String DF_Scale = "DFScale";//体秤  巨微纯广播直流  0.1kg
    public static final String FD_Scale = "FDScale";//体秤  巨微纯广播直流  0.05kg
    public static final String FD_Scale260H = "260H";//体秤  巨微纯广播直流  0.05kg insmart客户

    public static final String BODYFAT_SCALE1_D = "BodyFat Scale1-D";//脂肪  直流秤 不支持离线 0.05kg
    public static final String ADORE_1_D = "ADORE1-D";//脂肪  直流秤 离线 0.05kg

    public static final String LF_SMART_SCALE = "LFSmart Scale";//3.0蓝牙协议-------通用秤 公司通用
    public static final String INSMART_589 = "INSMART-589";//3.0蓝牙协议-------通用秤 客户改款蓝牙名称insmart
    public static final String INSMART_818 = "INSMART-818";//3.0蓝牙协议-------通用秤 客户改款蓝牙名称insmart
    public static final String LF_SMART_SCALE_CF539 = "Smart scale CF539";//3.0蓝牙协议通用秤 客户需改名称
    public static final String Asligtco_scale = "Asligtco scale";//3.0蓝牙协议通用秤 客户需改名称

    public static final String CF568 = "CF568_G";//4 电极   公司通用
    public static final String CF568_BG = "CF568_BG";//4 电极   公司通用 无Wifi
    public static final String CF568_FUTULA = "FUTULA";//4 电极  公司通用 futula客户改款蓝牙名称
    public static final String CF568_VENUS = "Venus";//4 电极   公司通用  客户改款蓝牙名称
    public static final String CF568_CF587 = "CF587";//4 电极
    public static final String CF568_CF577 = "CF577";//8 电极
    public static final String CF568_CF577_FUTULA = "CF577_Futula";//8 电极
    public static final String CF568_CF577_MIX = "CF577_Mix";//8 电极  带4电极模式
    public static final String CF568_CF578 = "CF578";//4 电极  无Wifi
    public static final String CF568_CF588 = "CF588";//4 电极  无Wifi
    public static final String CF568_CF586 = "CF586";//4 电极  带Wifi 可不在UqHealhth上显示
    public static final String CF568_MCF_A2 = "MCF-A2";//4 电极  带Wifi 可不在UqHealhth上显示
    public static final String CF568_TM_315 = "TM-315";//4 电极   Tokuyo客户  无Wifi
    public static final String CF568_ANYLOOP_SS02 = "Anyloop_SS02";//4 电极 元寰客户 无Wifi
    public static final String CF568_CF568_GD = "CF568_GD";//4 电极 带Wifi


    /**
     * Anker体脂秤
     */
    public static final String EUFY_T9149 = "eufy T9149";//AnkerT9149带WiFi
    public static final String EUFY_T9148 = "eufy T9148";//AnkerT9148带WiFi

    /**
     * 营养秤
     **/
    public static final String KITCHEN_SCALE = "Kitchen Scale";//直连 直连秤计算方式与KF_SCALE不同
    public static final String KITCHEN_SCALE_3 = "Kitchen Scale3";//直连 直连秤计算方式与KF_SCALE不同
    public static final String KITCHEN_SCALE_1 = "Kitchen Scale1"; //直连 计算方式与KF_SCALE 一致
    public static final String KITCHEN_SCALE_1_MyiStamp_Scale = "MyiStamp Scale"; //直连 计算方式与KF_SCALE 一致
    public static final String KF_SCALE = "KFScale";//纯广播秤  不支持切换单位
    public static final String KF_SCALE_Sobar = "Sobar";//纯广播秤  不支持切换单位成丹客户KFScale更改名称
    public static final String LFSc = "LFSc";//3.0蓝牙协议通用------广播秤食物秤

    public static final String LFAdv_B232 = "B232";//3.0蓝牙协议通用------广播秤食物秤  公司通用
    public static final String WOLO_KITCHEN = "WOLO-KITCHEN";//沃来食物秤

    public interface DeviceList {
        //所有设备列表
        List<String> DeviceListAll = Arrays.asList(
                ENERGY_SCALE,
                BODYFAT_SCALE,
                ADORE_1_D,
                BODYFAT_SCALE1_D,
                HEALTH_SCALE,
                SK_SmartScale68,
                HEALTH_SCALE2,
                HEALTH_SCALE3,
                HEALTH_SCALE5,
                HEALTH_SCALE6,
                HEALTH_SCALE7,
                HEARTRATE_SCALE2,
                HEARTRATE_SCALE3,
                HEARTRATE_SCALE,
                HEARTRATE_SCALE_SMART_516,
                HEARTRATE_SCALE1,
                ELECTRONIC_SCALE,
                ELECTRONIC_SCALE1,
                CF367_SCALE,
                ADORE_SCALE,
                ADORE_SCALE1,
                ADORE_ANYLOOP_SS01,
                BM_SCALE,
                LF_SCALE,
                FL_SCALE,
                DF_Scale,
                FD_Scale,
                FD_Scale260H,
                BF_SCALE,
                WF_SCALE,
                FW_SCALE,
                HUMAN_SCALE,
                WEIGHT_SCALE,
                WEIGHT_SCALE1,
                WEIGHT_SCALE2,
                BODYFAT_SCALE_1,
                LF_SC,
                KITCHEN_SCALE,
                KITCHEN_SCALE_1,
                KITCHEN_SCALE_1_MyiStamp_Scale,
                KF_SCALE,
                KF_SCALE_Sobar
        );

        //直连设备列表
        List<String> DeviceListNeedConnect = Arrays.asList(
                ELECTRONIC_SCALE,
                ELECTRONIC_SCALE1,
                CF367_SCALE,
                HEALTH_SCALE5,
                KITCHEN_SCALE,
                KITCHEN_SCALE_1,
                KITCHEN_SCALE_1_MyiStamp_Scale,
                EUFY_T9149,
                EUFY_T9148);

        //人体秤设备列表
        List<String> DeviceListWeight = Arrays.asList(
                HUMAN_SCALE,
                WF_SCALE,
                FW_SCALE,
                WEIGHT_SCALE,
                WEIGHT_SCALE1,
                WEIGHT_SCALE2
        );

        //脂肪设备列表
        List<String> DeviceListFat = Arrays.asList(
                ENERGY_SCALE,
                BODYFAT_SCALE,
                ADORE_1_D,
                BODYFAT_SCALE1_D,
                HEALTH_SCALE,
                SK_SmartScale68,
                HEALTH_SCALE3,
                HEALTH_SCALE5,
                HEALTH_SCALE6,
                HEALTH_SCALE7,
                HEARTRATE_SCALE,
                HEARTRATE_SCALE_SMART_516,
                HEARTRATE_SCALE1,
                HEARTRATE_SCALE2,
                HEARTRATE_SCALE3,
                BM_SCALE,
                ADORE_SCALE,
                ADORE_SCALE1,
                ADORE_ANYLOOP_SS01,
                LF_SCALE,
                FL_SCALE,
                DF_Scale,
                FD_Scale,
                FD_Scale260H,
                BF_SCALE,
                BODYFAT_SCALE_1,
                HEALTH_SCALE2,
                LF_SC,
                EUFY_T9149,
                EUFY_T9148
        );

        //心率设备列表
        List<String> DeviceListHeartRate = Arrays.asList(
                HEARTRATE_SCALE,
                HEARTRATE_SCALE_SMART_516,
                HEARTRATE_SCALE1,
                HEARTRATE_SCALE2,
                HEARTRATE_SCALE3,
                EUFY_T9149,
                EUFY_T9148
        );

        //支持离线数据设备列表
        List<String> DeviceListHistory = Arrays.asList(
                HEARTRATE_SCALE,
                HEARTRATE_SCALE_SMART_516,
                HEARTRATE_SCALE1,
                HEARTRATE_SCALE2,
                HEARTRATE_SCALE3,
                ADORE_SCALE,
                ADORE_SCALE1,
                ADORE_ANYLOOP_SS01,
                WEIGHT_SCALE,
                HEALTH_SCALE5,
                HEALTH_SCALE6,
                HEALTH_SCALE7,
                ADORE_1_D,
                EUFY_T9149,
                EUFY_T9148
        );

        //闭幕单脚
        List<String> DeviceListBMDJ = Arrays.asList(
                BM_SCALE
        );

        //秤端计算
        List<String> DeviceListCalcuteInScale = Arrays.asList(
                ELECTRONIC_SCALE,
                ELECTRONIC_SCALE1,
                CF367_SCALE
        );

        /**
         * 蓝牙wifi
         */
        List<String> DeviceListBleConfigWifi = Arrays.asList(
                HEALTH_SCALE5,
                HEALTH_SCALE6,
                HEALTH_SCALE7,
                HEARTRATE_SCALE2,
                HEARTRATE_SCALE3,
                EUFY_T9149,
                EUFY_T9148
        );

        //营养秤
        List<String> DeviceListFoodScale = Arrays.asList(
                KITCHEN_SCALE,
                KITCHEN_SCALE_1,
                KITCHEN_SCALE_1_MyiStamp_Scale,
                KF_SCALE,
                KF_SCALE_Sobar
        );

        //纯广播
        List<String> DeviceListPureBroadCastScale = Arrays.asList(
                LF_SCALE,
                FL_SCALE,
                DF_Scale,
                FD_Scale,
                FD_Scale260H,
                BF_SCALE,
                WF_SCALE,
                FW_SCALE,
                LF_SC,
                LFSc,
                LFAdv_B232,
                KF_SCALE
        );

        //直流体脂计算公式设备
        List<String>
                DeviceListDirectCurrentScale = Arrays.asList(
                ADORE_1_D,//阻抗不除10
                FD_Scale,//阻抗除10
                FD_Scale260H,//阻抗除10
                DF_Scale,//阻抗除10
                BODYFAT_SCALE1_D
        );
        //控制是否除以10
        @Deprecated
        List<String> DeviceListDirectCurrentScaleTen = Arrays.asList(
                FD_Scale,//阻抗除10
                FD_Scale260H,//阻抗除10
                DF_Scale//阻抗除10
        );
        //V3.0设备
        List<String> smartV3DeviceList = Arrays.asList(
                LF_SMART_SCALE,
                INSMART_818,
                INSMART_589,
                LF_SMART_SCALE_CF539,
                Asligtco_scale,
                LFSc,
                LFAdv_B232,
                WOLO_KITCHEN
        );

        List<String> torreList = Arrays.asList(
                CF568,
                CF568_FUTULA,
                CF568_VENUS,
                CF568_CF587,
                CF568_CF577,
                CF568_CF577_FUTULA,
                CF568_CF577_MIX,
                CF568_TM_315,
                CF568_ANYLOOP_SS02,
                CF568_BG,
                CF568_CF578,
                CF568_CF588,
                CF568_CF586,
                CF568_MCF_A2,
                CF568_CF568_GD

        );

        List<String> torreListNoWifi = Arrays.asList(
                CF568_TM_315,
                CF568_ANYLOOP_SS02,
                CF568_BG,
                CF568_CF578,
                CF568_CF588
        );

        //数据加密的秤
        List<String> DeviceListEncryptScale = Arrays.asList(
                EUFY_T9149,
                EUFY_T9148
        );

        List<String> AnkerList = Arrays.asList(
                EUFY_T9149,
                EUFY_T9148
        );


    }


    public static int getDeviceType(String deviceName) {
        /**
         *     int PPDeviceTypeWeight = 1;
         *     int PPDeviceTypeBodyFat = 1 << 1;
         *     int PPDeviceTypeHearRate = 1 << 2;
         *     int PPDeviceTypeHistory = 1 << 3;
         *     int PPDeviceTypeBMDJ = 1 << 4;
         *     int PPDeviceTypeCalcuteInScale = 1 << 5;
         *     int PPDeviceTypeBleConfig = 1 << 6;
         *     int PPDeviceTypeFoodScale = 1 << 7;
         */
        int deviceType = 1;
        if (!TextUtils.isEmpty(deviceName)) {
            if (DeviceList.DeviceListWeight.contains(deviceName)) {
                deviceType = PPDeviceType.PPDeviceTypeWeight;
            }
            if (DeviceList.DeviceListFat.contains(deviceName)) {
                deviceType += PPDeviceType.PPDeviceTypeBodyFat;
            }
            if (DeviceList.DeviceListHeartRate.contains(deviceName)) {
                deviceType += PPDeviceType.PPDeviceTypeHearRate;
            }
            if (DeviceList.DeviceListHistory.contains(deviceName)) {
                deviceType += PPDeviceType.PPDeviceTypeHistory;
            }
            if (DeviceList.DeviceListBMDJ.contains(deviceName)) {
                deviceType += PPDeviceType.PPDeviceTypeBMDJ;
            }
            if (DeviceList.DeviceListCalcuteInScale.contains(deviceName)) {
                deviceType += PPDeviceType.PPDeviceTypeCalcuteInScale;
            }
            if (DeviceList.DeviceListBleConfigWifi.contains(deviceName)) {
                deviceType += PPDeviceType.PPDeviceTypeBleConfig;
            }
            if (DeviceList.DeviceListFoodScale.contains(deviceName)) {
                deviceType += PPDeviceType.PPDeviceTypeFoodScale;
            }
        }
        return deviceType;
    }

    public static String getScaleType(String deviceName) {
        if (!TextUtils.isEmpty(deviceName)) {
            if (DeviceList.DeviceListBleConfigWifi.contains(deviceName)) {
                return PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF;
            }
            if (DeviceList.DeviceListWeight.contains(deviceName)) {
                return PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE;
            }
            if (DeviceList.DeviceListFoodScale.contains(deviceName)) {
                return PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA;
            }
            if (DeviceList.DeviceListFat.contains(deviceName)) {
                return PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF;
            }
        }
        return PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF;
    }

//
//    public static boolean isDisconnect(int deviceType) {
//        if (deviceType == PPDeviceType.Contants.FAT_AND_CALCUTE_IN_SCALE
//                || deviceType == PPDeviceType.Contants.FAT_AND_HEARTRATE_HISTORY_BMDJ
//                || deviceType == PPDeviceType.Contants.FAT_AND_HISTORY_BMDJ
//                || deviceType == PPDeviceType.Contants.FAT_AND_HEARTRATE_BMDJ
//                || deviceType == PPDeviceType.Contants.FAT_HEARTRATE_AND_HISTORY
//                || deviceType == PPDeviceType.Contants.FAT_HISTORY
//                || deviceType == PPDeviceType.Contants.FAT_HEARTRATE
//                || deviceType == PPDeviceType.Contants.WEIGHT_HISTORY
//                || deviceType == PPDeviceType.Contants.CONFIG_WIFI
//                || deviceType == PPDeviceType.Contants.CONFIG_WIFI_HISTORY
//
//        ) {
//            return false;
//        }
//        return true;
//    }

    public static boolean isDisconnect(int deviceType) {
        return !((deviceType & PPDeviceType.PPDeviceTypeCalcuteInScale) == PPDeviceType.PPDeviceTypeCalcuteInScale
                || (deviceType & PPDeviceType.PPDeviceTypeHistory) == PPDeviceType.PPDeviceTypeHistory
                || (deviceType & PPDeviceType.PPDeviceTypeBMDJ) == PPDeviceType.PPDeviceTypeBMDJ
                || (deviceType & PPDeviceType.PPDeviceTypeHearRate) == PPDeviceType.PPDeviceTypeHearRate
                || (deviceType & PPDeviceType.PPDeviceTypeBleConfig) == PPDeviceType.PPDeviceTypeBleConfig
                || (deviceType & PPDeviceType.PPDeviceTypeFoodScale) == PPDeviceType.PPDeviceTypeFoodScale);
//        if (deviceType == PPDeviceType.Contants.FAT_AND_CALCUTE_IN_SCALE
//                || deviceType == PPDeviceType.Contants.FAT_AND_HEARTRATE_HISTORY_BMDJ
//                || deviceType == PPDeviceType.Contants.FAT_AND_HISTORY_BMDJ
//                || deviceType == PPDeviceType.Contants.FAT_AND_HEARTRATE_BMDJ
//                || deviceType == PPDeviceType.Contants.FAT_HEARTRATE_AND_HISTORY
//                || deviceType == PPDeviceType.Contants.FAT_HISTORY
//                || deviceType == PPDeviceType.Contants.FAT_HEARTRATE
//                || deviceType == PPDeviceType.Contants.WEIGHT_HISTORY
//                || deviceType == PPDeviceType.Contants.CONFIG_WIFI
//                || deviceType == PPDeviceType.Contants.CONFIG_WIFI_HISTORY
//
//        ) {
//            return false;
//        }
//        return true;
    }

}
