package com.peng.ppscale.business.ble.connect;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.search.SearchResult;
import com.peng.ppscale.business.ble.BleOptions;
import com.peng.ppscale.business.ble.listener.PPBleSendResultCallBack;
import com.peng.ppscale.business.ble.listener.PPBleStateInterface;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPUserModel;

public interface ConnectDeviceInterface {

    /**
     * 连接设备
     */
    void connectDevice(PPDeviceModel deviceModel);

    /**
     * 断开连接
     */
    void disConnect();

    /**
     * 强制断开
     */
    void disConnectForced();

    /**
     * 找到设备
     *
     * @param bleOptions
     */
    void bindOptions(BleOptions bleOptions);

    /**
     * 蓝牙相关状态回调
     *
     * @param bleConnectStateListener
     */
    void setBleStateInterface(PPBleStateInterface bleConnectStateListener);

    void exitBMDJModel();

    void clearHistoryData();

    void getHistoryData(PPBleSendResultCallBack sendResultCallBack);

    void sendModifyServerIp(String serverIP);

    void sendModifyServerDNS(String serverDNS);

    void sendDeleteWifiConfig();

    void sendInquityWifiConfig();

    void sendResetDevice();

    /**
     * 连接状态
     *
     * @return
     */
    boolean getConnectState();

    void setUserModel(PPUserModel userModel);

    void changeKitchenScaleUnit(PPUnitType unitType);

    void toZeroKitchenScale();

    void switchBuzzer(boolean isOpen);

    /**
     * 取消扫描，会终止失败重连
     */
    void cancleScan();

    void sendUnitDataScale(PPUnitType userUnit, PPBleSendResultCallBack sendResultCallBack);

    void sendSyncTimeDataAdoreScale(PPBleSendResultCallBack sendResultCallBack);

    void sendData2ElectronicScale(PPUnitType userUnit, PPBleSendResultCallBack sendResultCallBack);

    /**
     * 是否是主动断开
     *
     * @return
     */
    public boolean isDriving();

    /**
     * 配网
     *
     * @param ssid
     * @param password
     */
    void configWifi(String ssid, String password, PPBleSendResultCallBack sendResultCallBack);

}
