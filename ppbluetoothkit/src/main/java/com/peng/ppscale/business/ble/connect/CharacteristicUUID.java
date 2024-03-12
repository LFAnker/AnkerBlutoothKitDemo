package com.peng.ppscale.business.ble.connect;

public interface CharacteristicUUID {

    /**
     * 服务特征值
     */
    String serviceUUID = "0000fff0";
    /**
     * 电量
     */
    String batteryServiceUUID = "0000180f";
    /**
     *
     */
    String batteryReadUUID = "2a19";
    /**
     * 设备信息
     */
    String deviceInfoServiceUUID = "0000180a";
    /**
     * 固件软件版本号
     */
    String modelNumberUUID = "00002a24";
    /**
     * 固件软件版本号
     */
    String softwareRevisionUUID = "00002a28";
    /**
     * 硬件版本号
     */
    String hardwareRevisionUUID = "00002a27";
    /**
     * 固件版本号
     */
    String firmwareRevisionUUID = "00002a26";
    /**
     * 序列号字符串
     */
    String serialNumberUUID = "00002a25";
    String ManufacturerNameUUID = "00002a29";

    /**
     * 可写特征值
     */
    String characteristicWriteUUID = "0000fff1";

    /**
     * 可读特征值
     */
    String characteristicNofifyUUID = "0000fff4";

    /**
     * 读取日志特征
     */
    String characteristicReadLogUUID = "0000fff2";

    /**
     * BMDJ特征值 可读可写
     */
    String characteristicBMDJUUID = "0000fff5";
    /**
     * OTA server
     */
    String OtaServiceUUID = "f000ffc0";
    /**
     * OTA Characteristic1
     */
    String OtaCharacteristicC1 = "f000ffc1";
    /**
     * OTA Characteristic2
     */
    String OtaCharacteristicC2 = "f000ffc2";

    /**
     * Torre 设备 4电极和8电极
     * 0xFFF1 Write，Read，Notify 控制类指令通道[APP<－>设备]
     * 0xFFF2  Write，Read，Notify 数据类指令通道[APP<－>设备]
     * 0xFFF3 Indicate 实时数据[设备－>APP]
     */
    String TorreCharacteristicF1 = "0000fff1";
    String TorreCharacteristicF2 = "0000fff2";
    String TorreCharacteristicF3 = "0000fff3";
    String TorreCharacteristicF4 = "0000fff4";


}
