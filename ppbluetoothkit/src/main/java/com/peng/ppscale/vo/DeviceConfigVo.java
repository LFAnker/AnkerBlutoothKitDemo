package com.peng.ppscale.vo;

import java.io.Serializable;

public class DeviceConfigVo implements Serializable {

    /**
     * "createBy": null,
     * "createTime": "2023-09-06 19:27:19",
     * "updateBy": null,
     * "updateTime": "2023-09-13 08:55:57",
     * "remark": null,
     * "id": 1,
     * "deviceName": "CF577",
     * "sign": "00",
     * "macAddressStart": 6,
     * "deviceConnectType": 2,
     * "deviceType": 1,
     * "deviceProtocolType": 3,
     * "deviceCalcuteType": 4,
     * "devicePowerType": 3,
     * "deviceFuncType": 223,
     * "deviceAccuracyType": 2,
     * "deviceUnitType": "0, 1, 11"
     * }
     */

    public String createTime;
    public String updateTime;
    public int id;
    public String deviceName;

    public String sign;//设备标签，用于V3.0协议的秤类型区分
    public int advLength; //广播数据长度
    public int macAddressStart; //mac起始位置
    public int calorieStatus;
    //连接类型
    public int deviceConnectType;
    //设备类型
    public int deviceType;
    //协议类型
    public int deviceProtocolType;
    //计算类型
    public int deviceCalcuteType;
    //供电类型
    public int devicePowerType;
    //支持的功能
    public int deviceFuncType;
    //精度
    public int deviceAccuracyType;
    //单位
    public String deviceUnitType;
    //自定义设备名称-优先展示该名称
    public String customDeviceName;
    //设备图片-暂未启用
    public String imgUrl;
    //品牌ID
    public String brandId;
    //品牌参数
    public Brand brand;

    public class Brand implements Serializable {
        public String brandName;
        public String brandLogo;

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getBrandLogo() {
            return brandLogo;
        }

        public void setBrandLogo(String brandLogo) {
            this.brandLogo = brandLogo;
        }

        @Override
        public String toString() {
            return "Brand{" +
                    "brandName='" + brandName + '\'' +
                    ", brandLogo='" + brandLogo + '\'' +
                    '}';
        }
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getCalorieStatus() {
        return calorieStatus;
    }

    public void setCalorieStatus(int calorieStatus) {
        this.calorieStatus = calorieStatus;
    }

    public int getMacAddressStart() {
        return macAddressStart;
    }

    public void setMacAddressStart(int macAddressStart) {
        this.macAddressStart = macAddressStart;
    }

    public int getDeviceConnectType() {
        return deviceConnectType;
    }

    public void setDeviceConnectType(int deviceConnectType) {
        this.deviceConnectType = deviceConnectType;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getDeviceProtocolType() {
        return deviceProtocolType;
    }

    public void setDeviceProtocolType(int deviceProtocolType) {
        this.deviceProtocolType = deviceProtocolType;
    }

    public int getDeviceCalcuteType() {
        return deviceCalcuteType;
    }

    public void setDeviceCalcuteType(int deviceCalcuteType) {
        this.deviceCalcuteType = deviceCalcuteType;
    }

    public int getDevicePowerType() {
        return devicePowerType;
    }

    public void setDevicePowerType(int devicePowerType) {
        this.devicePowerType = devicePowerType;
    }

    public int getAdvLength() {
        return advLength;
    }

    public void setAdvLength(int advLength) {
        this.advLength = advLength;
    }

    public int getDeviceFuncType() {
        return deviceFuncType;
    }

    public void setDeviceFuncType(int deviceFuncType) {
        this.deviceFuncType = deviceFuncType;
    }

    public int getDeviceAccuracyType() {
        return deviceAccuracyType;
    }

    public void setDeviceAccuracyType(int deviceAccuracyType) {
        this.deviceAccuracyType = deviceAccuracyType;
    }

    public String getDeviceUnitType() {
        return deviceUnitType;
    }

    public void setDeviceUnitType(String deviceUnitType) {
        this.deviceUnitType = deviceUnitType;
    }

    public String getCustomDeviceName() {
        return customDeviceName;
    }

    public void setCustomDeviceName(String customDeviceName) {
        this.customDeviceName = customDeviceName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "DeviceConfigVo{" +
                "createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", id=" + id +
                ", deviceName='" + deviceName + '\'' +
                ", sign='" + sign + '\'' +
                ", advLength=" + advLength +
                ", macAddressStart=" + macAddressStart +
                ", calorieStatus=" + calorieStatus +
                ", deviceConnectType=" + deviceConnectType +
                ", deviceType=" + deviceType +
                ", deviceProtocolType=" + deviceProtocolType +
                ", deviceCalcuteType=" + deviceCalcuteType +
                ", devicePowerType=" + devicePowerType +
                ", deviceFuncType=" + deviceFuncType +
                ", deviceAccuracyType=" + deviceAccuracyType +
                ", deviceUnitType='" + deviceUnitType + '\'' +
                ", customDeviceName='" + customDeviceName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", brand=" + brand +
                '}';
    }
}
