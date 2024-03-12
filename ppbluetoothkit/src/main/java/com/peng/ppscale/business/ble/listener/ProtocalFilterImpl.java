package com.peng.ppscale.business.ble.listener;

import com.peng.ppscale.business.ble.bmdj.PPBMDJConnectInterface;
import com.peng.ppscale.business.ble.bmdj.PPBMDJDataInterface;
import com.peng.ppscale.business.ble.bmdj.PPBMDJStatesInterface;
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiInfoInterface;
import com.peng.ppscale.business.torre.listener.PPTorreConfigWifiInterface;

public class ProtocalFilterImpl {

    PPProcessDateInterface processDateInterface;
    PPLockDataInterface lockDataInterface;
    PPHistoryDataInterface historyDataInterface;
    BleDataStateInterface bleDataStateInterface;
    PPBMDJDataInterface bmdjDataInterface;
    PPBMDJStatesInterface bmdjStatesInterface;
    PPBMDJConnectInterface bmdjConnectInterface;
    PPConfigWifiInfoInterface configWifiInfoInterface;
    PPDeviceSetInfoInterface resetDeviceInterface;
    com.peng.ppscale.business.ble.listener.PPDeviceInfoInterface deviceInfoInterface;
    PPSearchDeviceInfoInterface searchDeviceInfoInterface;
    PPUserInfoInterface userInfoInterface;
    PPTorreConfigWifiInterface torreConfigWifiInterface;
    FoodScaleDataChangeListener foodScaleDataProtocoInterface;

    public void setPPProcessDateInterface(PPProcessDateInterface processDateInterface) {
        this.processDateInterface = processDateInterface;
    }

    public void setPPLockDataInterface(PPLockDataInterface lockDataInterface) {
        this.lockDataInterface = lockDataInterface;
    }

    public void setPPHistoryDataInterface(PPHistoryDataInterface historyDataInterface) {
        this.historyDataInterface = historyDataInterface;
    }

    public void setBleDataStateInterface(BleDataStateInterface bleDataStateInterface) {
        this.bleDataStateInterface = bleDataStateInterface;
    }

    public void setBmdjDataInterface(PPBMDJDataInterface bmdjDataInterface) {
        this.bmdjDataInterface = bmdjDataInterface;
    }

    public void setBmdjStatesInterface(PPBMDJStatesInterface bmdjStatesInterface) {
        this.bmdjStatesInterface = bmdjStatesInterface;
    }

    public void setBmdjConnectInterface(PPBMDJConnectInterface bmdjConnectInterface) {
        this.bmdjConnectInterface = bmdjConnectInterface;
    }

    public void setConfigWifiInfoInterface(PPConfigWifiInfoInterface configWifiInfoInterface) {
        this.configWifiInfoInterface = configWifiInfoInterface;
    }


    public void setDeviceSetInfoInterface(PPDeviceSetInfoInterface resetDeviceInterface) {
        this.resetDeviceInterface = resetDeviceInterface;
    }

    public void setSearchDeviceInfoInterface(PPSearchDeviceInfoInterface searchDeviceInfoInterface) {
        this.searchDeviceInfoInterface = searchDeviceInfoInterface;
    }

    public PPDeviceInfoInterface getDeviceInfoInterface() {
        return deviceInfoInterface;
    }

    public void setDeviceInfoInterface(com.peng.ppscale.business.ble.listener.PPDeviceInfoInterface deviceInfoInterface) {
        this.deviceInfoInterface = deviceInfoInterface;
    }

    public PPProcessDateInterface getProcessDateInterface() {
        return processDateInterface;
    }

    public PPLockDataInterface getLockDataInterface() {
        return lockDataInterface;
    }

    public PPHistoryDataInterface getHistoryDataInterface() {
        return historyDataInterface;
    }

    public BleDataStateInterface getBleDataStateInterface() {
        return bleDataStateInterface;
    }

    public PPBMDJStatesInterface getBmdjStatesInterface() {
        return bmdjStatesInterface;
    }

    public PPBMDJConnectInterface getBmdjConnectInterface() {
        return bmdjConnectInterface;
    }

    public PPBMDJDataInterface getBmdjDataInterface() {
        return bmdjDataInterface;
    }

    public PPConfigWifiInfoInterface getConfigWifiInfoInterface() {
        return configWifiInfoInterface;
    }

    public PPDeviceSetInfoInterface getDeviceSetInfoInterface() {
        return resetDeviceInterface;
    }

    public PPSearchDeviceInfoInterface getSearchDeviceInfoInterface() {
        return searchDeviceInfoInterface;
    }

    public PPUserInfoInterface getUserInfoInterface() {
        return userInfoInterface;
    }

    public void setUserInfoInterface(PPUserInfoInterface userInfoInterface) {
        this.userInfoInterface = userInfoInterface;
    }

    public PPTorreConfigWifiInterface getTorreConfigWifiInterface() {
        return torreConfigWifiInterface;
    }

    public void setTorreConfigWifiInterface(PPTorreConfigWifiInterface torreConfigWifiInterface) {
        this.torreConfigWifiInterface = torreConfigWifiInterface;
    }

    public FoodScaleDataChangeListener getFoodScaleDataProtocoInterface() {
        return foodScaleDataProtocoInterface;
    }

    public void setFoodScaleDataProtocoInterface(FoodScaleDataChangeListener foodScaleDataProtocoInterface) {
        this.foodScaleDataProtocoInterface = foodScaleDataProtocoInterface;
    }
}
