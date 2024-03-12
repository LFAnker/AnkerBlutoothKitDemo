package com.peng.ppscale.business.ble.send;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.peng.ppscale.business.ble.listener.PPBleSendResultCallBack;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPUserModel;

import java.util.List;
import java.util.UUID;

public class BleSendManager {

    private final BleSendDelegate bleSendDelegate;

    private static volatile BleSendManager instance = null;

    private BleSendManager() {
        bleSendDelegate = new BleSendDelegate();
    }

    public static BleSendManager getInstance() {
        if (instance == null) {
            synchronized (BleSendManager.class) {
                if (instance == null) {
                    instance = new BleSendManager();
                }
            }
        }
        return instance;
    }

    public BleSendManager setBuilder(Builder builder) {
        bleSendDelegate.setDeviceInfo(builder.device, builder.service, builder.character);
        bleSendDelegate.bindBleClient(builder.bleClient);
        return instance;
    }

    public void setDevice(PPDeviceModel deviceModel) {
//        Logger.e("BleSendManager setDevice" + deviceModel.toString());
        bleSendDelegate.setDevice(deviceModel);
    }

    public void setCharacter(UUID characterUuid) {
        bleSendDelegate.setCharacter(characterUuid);
    }

    public void setBleClient(BluetoothClient bleClient) {
        bleSendDelegate.bindBleClient(bleClient);
    }


    public void setService(UUID service) {
        bleSendDelegate.setService(service);
    }

    public void setServiceUUID(UUID serviceUUID) {
        bleSendDelegate.setService(serviceUUID);
    }

    public void bindBleClient(BluetoothClient bleClient) {
        bleSendDelegate.bindBleClient(bleClient);
    }

    /**
     * 发送删除历史数据指令
     */
    public void sendDeleteHistoryData(PPBleSendResultCallBack bleSendListener) {
        bleSendDelegate.sendDeleteHistoryData(bleSendListener);
    }

    /**
     * 获取历史数据
     */
    public void sendGetHistoryData(PPBleSendResultCallBack sendResultCallBack) {
        bleSendDelegate.sendGetHistoryData(sendResultCallBack);
    }

    public void sendData(byte[] bytes, PPBleSendResultCallBack bleSendListener) {
        bleSendDelegate.sendMessageOneByte(bytes, bleSendListener);
    }

    public void sendDataList(List<byte[]> list, PPBleSendResultCallBack bleSendListener) {
        bleSendDelegate.sendMessageDelay30(list, bleSendListener);
    }

    public void sendDataListNoResponse(List<byte[]> list, PPBleSendResultCallBack bleSendListener) {
        bleSendDelegate.sendMessageDelay30NoResponse(list, bleSendListener);
    }


    /**
     * @param userUnit
     * @param address
     * @param mode        0-表示为有效的用户组信息，退出安全模式   1-表示秤进入安全模式，不会测量阻抗。
     */
    public void sendSwitchUnitDataByAdvert(PPUnitType userUnit, String address, int mode) {
        bleSendDelegate.sendSwitchUnitDataByAdvert(userUnit, address, mode);
    }

    public void stopAdvertising(){
        bleSendDelegate.stopAdvertising();
    }

    /**
     * 切换单位
     */
    public void sendSwitchUnitData(PPUnitType userUnit, PPBleSendResultCallBack sendResultCallBack) {
        bleSendDelegate.sendSwitchUnitData(userUnit, sendResultCallBack);
    }

    /**
     * 同步时间
     */
    public void sendSyncTimeDataAdoreScale(PPBleSendResultCallBack sendResultCallBack) {
        bleSendDelegate.sendSyncTimeData2AdoreScale(sendResultCallBack);
    }

    /**
     * 同步时间
     */
    public void sendSyncTimeUTC() {
        bleSendDelegate.sendSyncTimeUTC();
    }

    /**
     * 切换单位
     */
    public void sendSyncTime(PPBleSendResultCallBack sendResultCallBack) {
        bleSendDelegate.sendSyncTime(sendResultCallBack);
    }

    public void sendData2ElectronicScale(PPUnitType userUnit, PPUserModel userModel, PPBleSendResultCallBack sendResultCallBack) {
        bleSendDelegate.sendData2ElectronicScale(userUnit, userModel, sendResultCallBack);
    }

    public BleSendManager configWifi(String ssid, String password, PPBleSendResultCallBack sendResultCallBack) {
        bleSendDelegate.configWifi(ssid, password, sendResultCallBack);
        return this;
    }

    public void disWifi(PPBleSendResultCallBack sendResultCallBack) {
        bleSendDelegate.disWifi(sendResultCallBack);
    }

    public void sendExitMBDJData2Scale(PPBleSendResultCallBack sendResultCallBack) {
        bleSendDelegate.sendExitMBDJData2Scale(sendResultCallBack);
    }

    public void sendMBDJData2Scale() {
        bleSendDelegate.sendMBDJData2Scale();
    }

    public static class Builder {
        PPDeviceModel device;
        UUID service;
        UUID character;
        BluetoothClient bleClient;
        int deviceType;

        public Builder setDevice(PPDeviceModel device) {
            this.device = device;
            return this;
        }

        public Builder setService(UUID service) {
            this.service = service;
            return this;
        }

        public Builder setCharacter(UUID character) {
            this.character = character;
            return this;
        }

        public Builder setBleClient(BluetoothClient bleClient) {
            this.bleClient = bleClient;
            return this;
        }

        public Builder setDeviceType(int deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public BleSendManager build() {
            return BleSendManager.getInstance().setBuilder(this);
        }
    }

    public BleSendDelegate getBleSendDelegate() {
        return bleSendDelegate;
    }


}
