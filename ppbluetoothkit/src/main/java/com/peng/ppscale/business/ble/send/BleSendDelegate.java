package com.peng.ppscale.business.ble.send;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.peng.ppscale.business.ble.foodscale.manager.BleFoodDataProtocoManager;
import com.peng.ppscale.business.ble.listener.PPBleSendResultCallBack;
import com.peng.ppscale.business.device.DeviceManager;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.PPBlutoothKit;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;
import com.peng.ppscale.vo.PPScaleSendState;
import com.peng.ppscale.vo.PPUserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BleSendDelegate {
    UUID service;
    UUID character;
    PPUserModel userModel;
    PPUnitType userUnit;
    int deviceType;
    PPDeviceModel device;
    //记录离线数据发送的次数
    int num = 0;
    PPBleSendResultCallBack bleSendListener;
    public static boolean isSendHistoryCmd = false;

    /**
     * 发送间隔时间
     */
    public static final int POST_DELAY_MILLS = 300;
    public static final int POST_DELAY_MILLS_LIST = 30;
    public int index = 0;

    public BleSendDelegate() {
    }

    /**
     * 获取历史数据
     */
    public void sendGetHistoryData(PPBleSendResultCallBack sendResultCallBack) {
        byte[] historyData = getHistoryData(deviceType);
        if (historyData != null) {
            sendMessage(historyData, sendResultCallBack);
        } else {
            if (this.bleSendListener != null) {
                this.bleSendListener.onResult(PPScaleSendState.PP_DEVICE_ERROR);
            }
        }
    }

    /**
     * @param userUnit
     * @param address
     * @param mode  0-表示为有效的用户组信息，退出安全模式   1-表示秤进入安全模式，不会测量阻抗。
     */
    public void sendSwitchUnitDataByAdvert(PPUnitType userUnit, String address, int mode) {
        String name = "Phone";
        String dataStr = BleSendHelper.sendAdvertisingData(userUnit, address, mode);

        byte[] bytes = ByteUtils.stringToBytes(dataStr);
        startAdvertising(name, true, null, null, bytes);
    }

    public void sendSwitchUnitData(PPUnitType userUnit, PPBleSendResultCallBack sendResultCallBack) {
        List<byte[]> bytes = new ArrayList<>();
        if (device != null && device.deviceConnectType == PPScaleDefine.PPDeviceConnectType.PPDeviceConnectTypeDirect) {
            bytes.add(BleSendHelper.syncUnitV2(userUnit, this.userModel, device));
        }
        if ((deviceType & PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType())
                == PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType()) {
            if (device != null && !device.getDeviceName().equals(DeviceManager.HEALTH_SCALE5)) {
                bytes.add(BleSendHelper.sendSyncTimeData2AdoreScale());
            }
//            bytes.add(BleSendHelper.sendSyncHistoryData2AdoreScale());
        }
        if (bytes.size() == 1) {
            num = 0;
            sendMessage(bytes.get(0), sendResultCallBack);
        } else if (!bytes.isEmpty()) {
            num = 0;
            sendMessage(bytes, sendResultCallBack);
        } else {
            if (BleSendDelegate.this.bleSendListener != null) {
                BleSendDelegate.this.bleSendListener.onResult(PPScaleSendState.PP_SEND_FAIL);
            }
        }
    }

    public void configWifi(String ssid, String password, PPBleSendResultCallBack sendResultCallBack) {
        num = 0;
        List<byte[]> bytes = BleSendHelper.codeBySSIDAndPassword1(ssid, password);
        sendMessage(bytes, sendResultCallBack);
    }

    public void disWifi(PPBleSendResultCallBack sendResultCallBack) {
        sendMessage(BleSendHelper.disWifi(), sendResultCallBack);
    }

    public void setDeviceInfo(PPDeviceModel device, UUID service, UUID character) {
        if (device != null) {
            this.device = device;
            this.service = service;
            this.character = character;
            this.deviceType = device.deviceFuncType;
        }
    }


    public void setDevice(PPDeviceModel device) {
//        Logger.e("BleSendDelegate setDevice" + device.toString());
        this.device = device;
    }

    public void setService(UUID service) {
        this.service = service;
    }

    public void setCharacter(UUID character) {
        this.character = character;
    }

    public void bindBleClient(BluetoothClient bleClient) {
        BleSendPoolManager.INSTANCE.setBleClient(bleClient);
    }

    public void setUnit(PPUnitType userUnit) {
        this.userUnit = userUnit;
    }

    public void sendData2ElectronicScale(PPUnitType userUnit, PPUserModel userModel, PPBleSendResultCallBack sendResultCallBack) {
        if (device.deviceCalcuteType == PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeInScale) {
            sendMessage(BleSendHelper.syncUnitInScale(userModel, userUnit), sendResultCallBack);
        }
    }

    /*向秤发送指令*/
    public byte[] getHistoryData(int deviceType) {
        Logger.e("deviceType = " + deviceType);
        if ((deviceType & PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType())
                == PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeHistory.getType()) {
            return BleSendHelper.sendSyncHistoryData2AdoreScale();
        } else {
//            resultArr.add(BleSendHelper.sendUnitData2Scale(userUnit, this.userModel, this.deviceName));
        }
        return null;
    }

    public void sendSyncTimeData2AdoreScale(PPBleSendResultCallBack sendResultCallBack) {
        byte[] bytes = BleSendHelper.sendSyncTimeData2AdoreScale();
        sendMessage(bytes, sendResultCallBack);
    }

    public void sendSyncTimeUTC() {
        byte[] bytes = BleSendHelper.sendSyncTimeUTC();
        sendMessage(bytes, null);
    }

    public void sendSyncTime(PPBleSendResultCallBack sendResultCallBack) {
        byte[] bytes = BleSendHelper.sendSyncTime();
        sendMessage(bytes, sendResultCallBack);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x02: {
                    List<byte[]> commond = (List<byte[]>) msg.obj;
//                    sendMessageList(commond);
                    BleSendPoolManager.INSTANCE.sendListDataResponse(device.getDeviceMac(), service, character, commond, bleSendListener);
                }
                break;
                case 0x03:
                    final byte[] byteSend = (byte[]) msg.obj;
                    sendMessageOneByte(byteSend, bleSendListener);
                    break;
                default:
                    break;
            }
        }
    };

    public void sendExitMBDJData2Scale(PPBleSendResultCallBack sendResultCallBack) {
        sendMessage(BleSendHelper.sendExitMBDJData2Scale(), sendResultCallBack);
    }

    public void sendMBDJData2Scale() {
        sendMessage(BleSendHelper.sendMBDJData2Scale(), null);
    }

    public void sendModifyServerIp(String sergerIP) {
        sendMessage(BleSendHelper.modifyServerIp(sergerIP), null);
    }

    public void sendModifyServerDNS(String serverDNS) {
        sendMessage(BleSendHelper.modifyServerDomain(serverDNS), null);
    }

    public void sendDeleteWifiConfig() {
        sendMessage(BleSendHelper.deleteWifiConfig(), null);
    }

    public void sendInquityWifiConfig() {
        sendMessage(BleSendHelper.inquityWifiConfig(), null);
    }

    public void changeKitchenScaleUnit(PPUnitType unitType) {
//        sendMessage(BleSendHelper.sendUnitData2Scale(unitType, this.userModel, device));
        sendMessage(BleFoodDataProtocoManager.sendData2ElectronicScale(unitType), null);
    }

    public void toZeroKitchenScale() {
        sendMessage(BleSendHelper.toZeroKitchenScale(), null);
    }

    /**
     * 蜂鸣器开关
     *
     * @param isOpen
     */
    public void switchBuzzer(boolean isOpen) {
        sendMessage(BleSendHelper.switchBuzzer(isOpen), null);
    }

    public void sendResetDevice() {
        sendMessage(BleSendHelper.resetDeviceConfig(), null);
    }

    public void sendDeleteHistoryData(PPBleSendResultCallBack bleSendListener) {
        sendMessageOneByte(BleSendHelper.deleteAdoreHistoryData(), bleSendListener);
    }

    private void sendMessage(final byte[] bytes, final PPBleSendResultCallBack bleSendListener) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getConnectState()) {
                    sendMessageOneByte(bytes, bleSendListener);
                } else {
                    //未连接
                    Logger.e("sendMessage fail because ble not connected ");
                    if (BleSendDelegate.this.bleSendListener != null) {
                        BleSendDelegate.this.bleSendListener.onResult(PPScaleSendState.PP_DEVICE_NO_CONNECT);
                    }
                }
            }
        }, POST_DELAY_MILLS);
//        Message message = handler.obtainMessage();
//        message.obj = bytes;
//        message.what = 0x03;
//        handler.sendMessageDelayed(message, POST_DELAY_MILLS);
    }

    private void sendMessage(final List<byte[]> commond, final PPBleSendResultCallBack bleSendListener) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getConnectState()) {
                    BleSendPoolManager.INSTANCE.sendListDataResponse(device.getDeviceMac(), service, character, commond, bleSendListener);
                } else {
                    //未连接
                    Logger.e("sendMessage fail because ble not connected ");
                    if (BleSendDelegate.this.bleSendListener != null) {
                        BleSendDelegate.this.bleSendListener.onResult(PPScaleSendState.PP_DEVICE_NO_CONNECT);
                    }
                }
            }
        }, POST_DELAY_MILLS);
    }

    public void sendMessageDelay30(final List<byte[]> commond, PPBleSendResultCallBack bleSendListener) {
        if (getConnectState()) {
            BleSendPoolManager.INSTANCE.sendListDataResponse(device.getDeviceMac(), service, character, commond, bleSendListener);
        } else {
            Logger.e("sendMessage fail because ble not connected ");
            if (BleSendDelegate.this.bleSendListener != null) {
                BleSendDelegate.this.bleSendListener.onResult(PPScaleSendState.PP_DEVICE_NO_CONNECT);
            }
        }
    }

    public void sendMessageDelay30NoResponse(final List<byte[]> commond, PPBleSendResultCallBack bleSendListener) {
        if (getConnectState()) {
//            sendMessageListNoResponse();
            BleSendPoolManager.INSTANCE.sendListDataNoResponse(device.getDeviceMac(), service, character, commond, bleSendListener);
        } else {
            //未连接,却发起连接，回调回去重新扫描或连接
            Logger.e("sendMessage fail because ble not connected ");
            if (BleSendDelegate.this.bleSendListener != null) {
                BleSendDelegate.this.bleSendListener.onResult(PPScaleSendState.PP_DEVICE_NO_CONNECT);
            }
        }
    }

    public void sendMessageOneByte(final byte[] bytes, PPBleSendResultCallBack bleSendListener) {
        if (getConnectState()) {
            BleSendPoolManager.INSTANCE.sendDataResponse(device.getDeviceMac(), service, character, bytes, bleSendListener);
        } else {
            Logger.e("sendMessage fail because ble not connected ");
            if (BleSendDelegate.this.bleSendListener != null) {
                BleSendDelegate.this.bleSendListener.onResult(PPScaleSendState.PP_DEVICE_NO_CONNECT);
            }
        }
    }

    public boolean getConnectState() {
        BluetoothClient bluetoothClient = PPBlutoothKit.INSTANCE.getBluetoothClient();
        if (device != null && service != null && character != null && bluetoothClient != null) {
            try {
                boolean b = bluetoothClient.getConnectStatus(device.getDeviceMac()) == Constants.STATUS_DEVICE_CONNECTED;
                Logger.d(" address =  " + device.getDeviceMac() + " connect state = " + b);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void startAdvertising(String name, boolean connectable, UUID serviceUUID, UUID characterUUID, final byte[] bytes) {
        BluetoothClient bluetoothClient = PPBlutoothKit.INSTANCE.getBluetoothClient();
        String writeData = String.format("%s", ByteUtils.byteToString(bytes));
        Logger.d("ppScale_ startAdvertising sendMessage--------- " + writeData);
        bluetoothClient.startAdvertising(serviceUUID, characterUUID, name, connectable, bytes, 0, null);
    }

    public void stopAdvertising() {
        BluetoothClient bluetoothClient = PPBlutoothKit.INSTANCE.getBluetoothClient();
        bluetoothClient.stopAdvertising(null);
    }



}
