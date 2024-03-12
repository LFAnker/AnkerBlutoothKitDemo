package com.peng.ppscale.business.ble;

import java.util.ArrayList;
import java.util.List;


public class BleOptions {
    List<String> deviceListCustorm;
    int deviceType = 0x0F;
    String ssid;
    String password;
    int searchTag;  //开发者模式直连1 正常扫描连接0 //默认0
    boolean directGetHistory = false;//是否直接读取历史，true为直接读取历史，直接发起连接

    public BleOptions(Builder builder) {
        this.deviceListCustorm = builder.deviceListCustorm;
        this.deviceType = builder.deviceType;
        this.ssid = builder.ssid;
        this.password = builder.password;
        this.searchTag = builder.searchTag;
    }

    public List<String> getDeviceListCustorm() {
        return deviceListCustorm;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getSsid() {
        return ssid;
    }

    public int getSearchTag() {
        return searchTag;
    }

    public String getPassword() {
        return password;
    }

    public boolean isDirectGetHistory() {
        return directGetHistory;
    }

    public void setDirectGetHistory(boolean directGetHistory) {
        this.directGetHistory = directGetHistory;
    }

    public static class Builder {

        int deviceType = 0x0F;

        List<String> deviceListCustorm = new ArrayList<>();

        String ssid = "";

        String password = "";

        int searchTag = 0;

        public Builder setCustormDeviceList(List<String> deviceListCustorm) {
            this.deviceListCustorm = deviceListCustorm;
            return this;
        }

        public Builder setDeviceType(int deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public Builder setSsid(String ssid) {
            this.ssid = ssid;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setSearchTag(int searchTag) {
            this.searchTag = searchTag;
            return this;
        }

        public BleOptions build() {
            return new BleOptions(this);
        }
    }

}
