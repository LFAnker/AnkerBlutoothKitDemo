package com.peng.ppscale.vo


const val LF_X_L_16 = "LF_X_L16"
const val LF_X_L_20 = "LF_X_L20"
const val LF_X_L_17 = "LF_X_L17"
const val HT_8_ = "HT_8_"
const val HT_4_ARM_ = "HT_4_Arm_"
const val LF_4_Z = "LF_4_ZLefu_2023-05-15_V1.0.0"
const val HT_4_LEG_ = "HT_4_Leg_"
const val HT_4_LEG2_ = "HT_4_Leg2_"
const val HT_4_2Channel_ = "HT_4_2Channel_"

class PPScaleDefine {

    /**
     * 设备连接类型，用于必须直连的状态
     */
    enum class PPDeviceConnectType(bodygrade: Int) {
        PPDeviceConnectTypeUnknow(0),
        PPDeviceConnectTypeBroadcast(1), //广播
        PPDeviceConnectTypeDirect(2),//直连
        PPDeviceConnectTypeBroadcastOrDirect(3); //广播或直连

        private var type = bodygrade

        open fun getType(): Int {
            return type
        }
    }

    /**
     * 设备分组类型
     */
    enum class PPDevicePeripheralType {
        //2.x /连接 /人体秤
        PeripheralApple,

        //2.x /广播 /人体秤
        PeripheralBanana,

        //3.x /连接 /人体秤
        PeripheralCoconut,

        //2.x /设备端计算的连接 /人体秤
        PeripheralDurian,

        //2.x /连接 /厨房秤
        PeripheralEgg,

        //3.x /连接 /厨房秤
        PeripheralFish,

        //2.x /广播 /厨房秤
        PeripheralGrapes,

        //3.x /广播 /厨房秤
        PeripheralHamburger,

        //torre /连接 /人体秤
        PeripheralTorre,

        //4.0 /连接 /人体秤
        PeripheralIce,

        //3.x /广播 /人体秤
        PeripheralJambul
    }

    /**
     * 设备类型
     */
    enum class PPDeviceType {
        PPDeviceTypeUnknow, // 未知
        PPDeviceTypeCF, // 体脂秤
        PPDeviceTypeCE, //体重秤
        PPDeviceTypeCB, // 婴儿秤
        PPDeviceTypeCA; // 厨房秤
    }


    /**
     * 协议模式
     */
    enum class PPDeviceProtocolType(bodygrade: Int) {
        //未知
        PPDeviceProtocolTypeUnknow(0),

        //使用V2.0蓝牙协议
        PPDeviceProtocolTypeV2(1),

        //使用V3.0蓝牙协议
        PPDeviceProtocolTypeV3(2),

        //Torre协议
        PPDeviceProtocolTypeTorre(3),

        //V4.0协议
        PPDeviceProtocolTypeV4(4),

        /**
         * AnkerT9149
         */
        PPDeviceProtocolTypeAnker149(5);

        private var type = bodygrade

        open fun getType(): Int {
            return type
        }

    }

    /**
     * 计算方式 默认体脂率采用原始值
     */
    enum class PPDeviceCalcuteType {
        PPDeviceCalcuteTypeUnknow(0),//未知
        PPDeviceCalcuteTypeInScale(1), //秤端计算
        PPDeviceCalcuteTypeDirect(2), //直流4DC
        PPDeviceCalcuteTypeAlternate(3),//4电极交流-V5.0.5固定版本-做减法
        PPDeviceCalcuteTypeAlternate8(4),// 8电极交流算法, bhProduct=1--CF577
        PPDeviceCalcuteTypeNormal(5), //4电极交流4-V5.0.5固定版本-不做减法
        PPDeviceCalcuteTypeNeedNot(6),//不需要计算
        PPDeviceCalcuteTypeAlternate8_0(7),//8电极算法，bhProduct =4--CF597
        PPDeviceCalcuteTypeAlternate8_1(8),//8电极算法，bhProduct =3--CF586
        PPDeviceCalcuteTypeAlternate4_0(9),//4电极交流(新)-跟随方案商，最新版本
        PPDeviceCalcuteTypeAlternate4_1(10),//4电极双频-跟随方案商，最新版本
        PPDeviceCalcuteTypeAlternate8_2(11),//8电极算法，bhProduct =0--CF610
        PPDeviceCalcuteTypeAlternate8_3(12),//8电极算法，bhProduct =5 --CF577_N1
        ;
        private var type = 0

        constructor(bodygrade: Int) {
            this.type = bodygrade
        }

        open fun getType(): Int {
            return type
        }

    }

    /**
     * 精度
     */
    enum class PPDeviceAccuracyType {

        PPDeviceAccuracyTypeUnknow(0), //未知精度

        PPDeviceAccuracyTypePoint01(1), //精度0.1

        PPDeviceAccuracyTypePoint005(2),//精度0.05

        PPDeviceAccuracyTypePointG(3),  // 1G精度

        PPDeviceAccuracyTypePoint01G(4), // 0.1G精度

        PPDeviceAccuracyTypePoint001(5); //0.01KG精度

        private var type = 0

        constructor(bodygrade: Int) {
            this.type = bodygrade
        }

        open fun getType(): Int {
            return type
        }

    }

    /**
     * 供电模式
     */
    enum class PPDevicePowerType {

        PPDevicePowerTypeUnknow(0),//未知
        PPDevicePowerTypeBattery(1),//电池供电
        PPDevicePowerTypeSolar(2),//太阳能供电
        PPDevicePowerTypeCharge(3); //充电款

        private var type = 0

        constructor(bodygrade: Int) {
            this.type = bodygrade
        }

        open fun getType(): Int {
            return type
        }


    }

    /**
     * 功能类型，可多功能叠加
     */
    enum class PPDeviceFuncType {
        // 称重
        PPDeviceFuncTypeWeight(0x01),

        //测体脂
        PPDeviceFuncTypeFat(0x02),

        //心率
        PPDeviceFuncTypeHeartRate(0x04),

        //历史数据
        PPDeviceFuncTypeHistory(0x08),

        //安全模式，孕妇模式
        PPDeviceFuncTypeSafe(0x10),

        //闭幕单脚
        PPDeviceFuncTypeBMDJ(0x20),

        //抱婴模式
        PPDeviceFuncTypeBaby(0x40),

        //wifi配网
        PPDeviceFuncTypeWifi(0x80),

        //时钟功能
        PPDeviceFuncTypeTime(0x0100),

        //按键声音
        PPDeviceFuncTypeKeyVoice(0x0200),

        //双向广播功能
        PPDeviceFuncTypeBidirectional(0x0400);


        private var type = 0

        constructor(type: Int) {
            this.type = type
        }

        open fun getType(): Int {
            return type
        }

    }

    /**
     * 支持的单位
     */
    enum class PPDeviceUnitType {
        //kg
        PPDeviceUnitTypeKG(0x01),

        //lb
        PPDeviceUnitTypeLB(0x02),

        //st
        PPDeviceUnitTypeST(0x04),//2.3st

        //斤
        PPDeviceUnitTypeJin(0x08),

        //st:lb
        PPDeviceUnitTypeSTLB(0x10);


        private var type = 0

        constructor(type: Int) {
            this.type = type
        }

        open fun getType(): Int {
            return type
        }

    }

    object PPScaleType {
        const val BLE_SCALE_TYPE_CF = "cf"
        const val BLE_SCALE_TYPE_CE = "ce"
        const val BLE_SCALE_TYPE_CA = "ca"
        const val BLE_SCALE_TYPE_CB = "cb"
    }


}
