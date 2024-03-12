package com.peng.ppscale.vo;

import java.io.Serializable;


public class PPWifiModel implements Serializable {

    int sign;

    String ssid;

    String bssid;


    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }
}
