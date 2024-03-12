package com.peng.ppscale.business.v4;

import android.bluetooth.BluetoothGatt;
import android.text.TextUtils;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleMtuResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiAppleStateMenu;
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiInfoInterface;
import com.peng.ppscale.business.ble.connect.CharacteristicUUID;
import com.peng.ppscale.business.ble.listener.BleReviveDataHandle;
import com.peng.ppscale.business.ble.listener.PPBleSendResultCallBack;
import com.peng.ppscale.business.ble.listener.PPBleStateInterface;
import com.peng.ppscale.business.ble.listener.PPDeviceInfoInterface;
import com.peng.ppscale.business.ble.listener.PPDeviceLogInterface;
import com.peng.ppscale.business.ble.listener.PPDeviceSetInfoInterface;
import com.peng.ppscale.business.ble.listener.PPHistoryDataInterface;
import com.peng.ppscale.business.ble.listener.PPTorreDeviceModeChangeInterface;
import com.peng.ppscale.business.ble.send.BleSendHelper;
import com.peng.ppscale.business.ble.send.BleSendManager;
import com.peng.ppscale.business.device.PPDeviceType;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.business.ota.OnOTAStateListener;
import com.peng.ppscale.business.state.PPBleWorkState;
import com.peng.ppscale.business.torre.TorreHelper;
import com.peng.ppscale.business.v4.auth.AuthenRequest;
import com.peng.ppscale.business.v4.auth.AuthenResponse;
import com.peng.ppscale.business.v4.auth.BLEAuthentication;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.DateUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.util.UnitUtil;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleSendState;
import com.peng.ppscale.vo.PPUserGender;
import com.peng.ppscale.vo.PPUserModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class V4Delegate implements V4CharacterFliterInterface {

    String TAG = V4Delegate.class.getSimpleName() + " ";
    private static volatile V4Delegate instance = null;

    BluetoothClient mBleClient;
    PPDeviceModel currentDevice;
    private BleSendManager bleSendManager;
    UUID serverUUID;
    UUID characterFFF1;
    UUID characterFFF4;
    UUID characterDeviceInfoService;//设备信息的UUID
    UUID characterBatteryService;//电量的UUID
    UUID characterBatteryUUID;//电量的UUID

    int sendTag = TorreHelper.SEND_TAG_HISTORY;//用于区分接收到的数据类型

    List<BleGattCharacter> characterDeviceInfoCharacters;

    PPBleStateInterface bleStateInterface;
    PPDeviceSetInfoInterface deviceSetInfoInterface;
    PPDeviceInfoInterface deviceInfoInterface;

    BleReviveDataHandle dataHandle;

    private V4Delegate() {
    }

    public static V4Delegate getInstance() {
        if (instance == null) {
            synchronized (V4Delegate.class) {
                if (instance == null) {
                    instance = new V4Delegate();
                }
            }
        }
        return instance;
    }

    public void bindBleClient(BluetoothClient bluetoothClient) {
        this.mBleClient = bluetoothClient;
        BleSendManager.getInstance().bindBleClient(bluetoothClient);
    }

    public void bindDevice(PPDeviceModel currentDevice) {
        this.currentDevice = currentDevice;
    }

    public void disConnect() {
        if (currentDevice != null && mBleClient != null) {
            Logger.d(TAG + "closeGatt  address = " + currentDevice.getDeviceMac());
            mBleClient.refreshCache(currentDevice.getDeviceMac());
            mBleClient.clearRequest(currentDevice.getDeviceMac(), 0);
            mBleClient.disconnect(currentDevice.getDeviceMac());
        }
    }

    @Override
    public void targetFFF1(UUID service, UUID character) {
        this.serverUUID = service;
        this.characterFFF1 = character;

    }

    @Override
    public void targetFFF2(UUID service, UUID character) {
        if (currentDevice == null) {
            Logger.e(TAG + "targetNotify currentDevice is null");
            return;
        }
        if (mBleClient == null) {
            Logger.e(TAG + "targetNotify mBleClient is null");
            return;
        }
        this.serverUUID = service;
        BleSendCrypt.getInstance().setOnAuthListener(onAuthListener);
//        BleSendCrypt.getInstance().createSecretKey(currentDevice.getDeviceMac());
        Logger.d("开始鉴权");
        AuthenRequest.getInstance().bleMac = currentDevice.getDeviceMac();
        BLEAuthentication.getInstance().startAuthen(true);
        //1发起鉴权
        sendDataToWriteList(AuthenResponse.getInstance().commondList, null);
        mBleClient.notify(currentDevice.getDeviceMac(), serverUUID, character, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                ProtocalV4DataHelper.getInstance().analysiEncryptData(bytes, currentDevice);
            }

            @Override
            public void onResponse(int code) {
                Logger.d(TAG + "targetNotify onResponse  code = " + code);
            }
        });
    }

    @Override
    public void targetFFF4(UUID service, UUID character) {
        if (currentDevice == null) {
            Logger.e(TAG + "targetReadLog currentDevice is null");
            return;
        }
        if (mBleClient == null) {
            Logger.e(TAG + "targetReadLog mBleClient is null");
            return;
        }
        this.serverUUID = service;
        this.characterFFF4 = character;
        mBleClient.notify(currentDevice.getDeviceMac(), serverUUID, characterFFF4, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                ProtocalV4DataHelper.getInstance().analysiEncryptData(bytes, currentDevice);
            }

            @Override
            public void onResponse(int code) {
                Logger.d(TAG + " targetFFF4 onResponse  code = " + code);
            }
        });
    }

    @Override
    public void batteryRead(UUID uuid, UUID uuid1) {
        characterBatteryService = uuid;
        characterBatteryUUID = uuid1;

        mBleClient.notify(currentDevice.getDeviceMac(), characterBatteryService, characterBatteryUUID, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if (currentDevice != null) {
                    String hex = ByteUtil.byteToString(bytes);
                    int power = ByteUtil.hexToTen(hex);
                    if (power > 0 & power <= 100) {
                        Logger.d(TAG + "batteryRead notify 电量 = " + power);
                        currentDevice.setDevicePower(power);
                        if (deviceInfoInterface != null) {
                            deviceInfoInterface.readDevicePower(power);
                        }
                    } else {
                        Logger.d(TAG + "batteryRead notify 电量 充电中 hex=" + hex);
                    }
                }
            }

            @Override
            public void onResponse(int code) {
                Logger.d(TAG + "targetReadLog onResponse  code = " + code);
            }
        });
    }

    @Override
    public void readDeviceInfo(UUID uuid, List<BleGattCharacter> characters) {
        characterDeviceInfoService = uuid;
        characterDeviceInfoCharacters = characters;
//        readDeviceInfoFromCharacter();
    }

    public void readDeviceBattery(final PPDeviceInfoInterface deviceInfoInterface) {
        this.deviceInfoInterface = deviceInfoInterface;
        if (currentDevice != null && mBleClient != null) {
            mBleClient.read(currentDevice.getDeviceMac(), characterBatteryService, characterBatteryUUID, new BleReadResponse() {

                @Override
                public void onResponse(int i, byte[] bytes) {
                    if (currentDevice != null) {
                        String hex = ByteUtil.byteToString(bytes);
                        int power = ByteUtil.hexToTen(hex);
                        if (power > 0 & power <= 100) {
                            Logger.d(TAG + "batteryRead read 电量 = " + power);
                            currentDevice.setDevicePower(power);
                            if (deviceInfoInterface != null) {
                                deviceInfoInterface.readDevicePower(power);
                            }
                        } else {
                            Logger.d(TAG + "batteryRead read 电量 充电中 hex=" + hex);
                        }
                    }
                }
            });
        }
    }

    private void readDeviceInfoFromCharcter(UUID uuid, final PPDeviceInfoInterface deviceInfoInterface, final BleGattCharacter lastObj) {
        if (currentDevice != null) {
            final String characterID = uuid.toString();
            mBleClient.read(currentDevice.getDeviceMac(), characterDeviceInfoService, uuid, new BleReadResponse() {

                @Override
                public void onResponse(int i, byte[] bytes) {
                    if (currentDevice != null) {
                        String hex = ByteUtil.byteToString(bytes);
                        String strData = ByteUtil.hexStringToString(hex);
                        if (characterID.contains(CharacteristicUUID.serialNumberUUID)) {
                            Logger.d(TAG + "serialNumber = " + strData);
                            currentDevice.setSerialNumber(strData);
                        } else if (characterID.contains(CharacteristicUUID.firmwareRevisionUUID)) {
                            Logger.d(TAG + "firmwareRevision = " + strData);
                            currentDevice.setFirmwareVersion(strData);
                        } else if (characterID.contains(CharacteristicUUID.hardwareRevisionUUID)) {
                            Logger.d(TAG + "hardwareRevision = " + strData);
                            currentDevice.setHardwareVersion(strData);
                        } else if (characterID.contains(CharacteristicUUID.softwareRevisionUUID)) {
                            Logger.d(TAG + "softwareRevision = " + strData);
                            currentDevice.setSoftwareVersion(strData);
                        } else if (characterID.contains(CharacteristicUUID.modelNumberUUID)) {
                            Logger.d(TAG + "modelNumber = " + strData);
                            currentDevice.setModelNumber(strData);
                        } else if (characterID.contains(CharacteristicUUID.ManufacturerNameUUID)) {
                            Logger.d(TAG + "ManufacturerName = " + strData);
                            currentDevice.setManufacturerName(strData);
                        }
                        if (lastObj.getUuid().toString().equals(characterID)) {
                            if (deviceInfoInterface != null) {
                                Logger.d(TAG + "readDeviceInfoFromCharcter readDeviceInfoComplete");
                                deviceInfoInterface.readDeviceInfoComplete(currentDevice);
                            } else {
                                Logger.e(TAG + "readDeviceInfoFromCharcter deviceInfoInterface is null");
                            }
                        }
                    }
                }
            });
        } else {
            Logger.e(TAG + "readDeviceInfoFromCharcter but device is null");
        }
    }

    public void readDeviceInfoFromCharacter(PPDeviceInfoInterface deviceInfoInterface) {
        if (characterDeviceInfoCharacters != null && !characterDeviceInfoCharacters.isEmpty()) {
            BleGattCharacter lastCharacter = characterDeviceInfoCharacters.get(characterDeviceInfoCharacters.size() - 1);
            for (BleGattCharacter character : characterDeviceInfoCharacters) {
                readDeviceInfoFromCharcter(character.getUuid(), deviceInfoInterface, lastCharacter);
            }
        } else {
            Logger.e(TAG + "characterDeviceInfoCharacters is null");
        }
    }

    /**********************************************************日志同步***************************************************************************************/

    public void getHistory(PPHistoryDataInterface historyDataInterface) {
        ProtocalV4DataHelper.getInstance().setHistoryDataInterface(historyDataInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.getHistory(), null);
    }

    public void deleteHistory(PPBleSendResultCallBack callBack) {
        sendDataToWriteList(V4DelegateHelper.INSTANCE.deleteHistory(), callBack);
    }

    public void syncTime(PPBleSendResultCallBack sendResultCallBack) {
        sendDataToWriteList(V4DelegateHelper.INSTANCE.syncTime(), sendResultCallBack);
    }

    public void getTime() {
        sendDataToWriteList(V4DelegateHelper.INSTANCE.getTime(), null);
    }

    /**
     * 设备恢复出厂[App->设备][FFF1]
     */
    public void resetDevice(PPDeviceSetInfoInterface resetDeviceInterface) {
        ProtocalV4DataHelper.getInstance().setResetDeviceInterface(resetDeviceInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.recoverDevice(), null);
    }

    //心率0打开 1关闭
    public void controlHeartRate(int state, PPTorreDeviceModeChangeInterface modeChangeInterface) {
        Logger.d(TAG + "controlHeartRate state:" + state);
        ProtocalV4DataHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
        if (state == 0) {
            sendDataToWriteList(V4DelegateHelper.INSTANCE.openHeartRate(), null);
        } else {
            sendDataToWriteList(V4DelegateHelper.INSTANCE.closeHeartRate(), null);
        }
    }

    //获取心率状态
    public void getHeartRateState(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        ProtocalV4DataHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.heartRateState(), null);
    }

    public void syncUnit(PPUnitType unitType, PPBleSendResultCallBack callBack) {
        sendDataToWriteList(V4DelegateHelper.INSTANCE.setUnit(UnitUtil.getUnitInt(unitType, "")), callBack);
    }

    /**
     * 测值模式
     *
     * @param state 0打开 1关闭
     */
    public void controlImpendance(int state, PPTorreDeviceModeChangeInterface modeChangeInterface) {
        Logger.d(TAG + "controlImpendance state:" + state);
        ProtocalV4DataHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
        if (state == 0) {
            sendDataToWriteList(V4DelegateHelper.INSTANCE.closeSafeMode(), null);
        } else {
            sendDataToWriteList(V4DelegateHelper.INSTANCE.openSafeMode(), null);
        }
    }

    //获取阻抗开关状态
    public void getImpendanceState(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        ProtocalV4DataHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
//        sendDataToWriteList(V4DelegateHelper.INSTANCE.(), null);
    }

    /**
     * 切换婴儿模式
     *
     * @param mode 00使能抱婴模式 01退出抱婴模式
     */
    public void switchBaby(int mode, PPBleSendResultCallBack sendResultCallBack) {
        if (mode == 0) {
            sendDataToWriteList(V4DelegateHelper.INSTANCE.enterHoldBabyMode(), sendResultCallBack);
        } else {
            sendDataToWriteList(V4DelegateHelper.INSTANCE.exitHoldBabyMode(), sendResultCallBack);
        }
    }

    /**
     * 切换宠物模式
     *
     * @param mode 00使能宠物模式 01退出宠物模式
     */
    public void switchPet(int mode, PPBleSendResultCallBack sendResultCallBack) {
        if (mode == 0) {
            sendDataToWriteList(V4DelegateHelper.INSTANCE.enterPetMode(), sendResultCallBack);
        } else {
            sendDataToWriteList(V4DelegateHelper.INSTANCE.exitPetMode(), sendResultCallBack);
        }
    }

    /**********************************************************配网***************************************************************************************/

    public void configWifiStart(PPConfigWifiInfoInterface configWifiInfoInterface) {
        Logger.d(TAG + "configWifiStart");
        ProtocalV4DataHelper.getInstance().setConfigWifiInfoInterface(configWifiInfoInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configWifiStart(), null);
    }

    /**
     * 下发配网code、uid、服务器域名
     *
     * @param code
     * @param uid
     * @param url
     * @param configWifiInfoInterface
     */
    public void configNewCodeUidUrl(String code, String uid, String url, PPConfigWifiInfoInterface configWifiInfoInterface) {
        Logger.d(TAG + "configNewCodeUidUrl");
        ProtocalV4DataHelper.getInstance().setConfigWifiInfoInterface(configWifiInfoInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configNewCodeUidUrl(code, uid, url), null);
    }

    /**
     * 下发域名证书
     *
     * @param domainCertificate
     * @param configWifiInfoInterface
     */
    public void configDomainCertificate(String domainCertificate, final PPConfigWifiInfoInterface configWifiInfoInterface) {
        Logger.d(TAG + "configDomainCertificate");
        ProtocalV4DataHelper.getInstance().setConfigWifiInfoInterface(configWifiInfoInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configDomainCertificate(domainCertificate), null);
    }

    /**
     * 域名证书下发完成（结束）
     *
     */
    public void configFinish() {
        Logger.d(TAG + "configFinish");
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configFinish(), null);
    }

    /**
     * 删除WiFi参数
     *
     * @param configWifiInfoInterface
     */
    public void configDeleteWifi(PPConfigWifiInfoInterface configWifiInfoInterface) {
        Logger.d(TAG + "configDeleteWifi");
        ProtocalV4DataHelper.getInstance().setConfigWifiInfoInterface(configWifiInfoInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configDeleteWifi(), null);
    }

    /**
     * 获取Wifi信息 查询WiFi参数
     *
     * @param configWifiInfoInterface
     */
    public void getWifiInfo(PPConfigWifiInfoInterface configWifiInfoInterface) {
        Logger.d(TAG + "getWifiInfo");
        ProtocalV4DataHelper.getInstance().setConfigWifiInfoInterface(configWifiInfoInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configQueryWifi(), null);
    }

    /**
     * 更新WiFi参数(配网)-路由器名称
     *
     * @param ssid
     * @param configWifiInfoInterface
     */
    public void configUpdateWifiSSID(String ssid, PPConfigWifiInfoInterface configWifiInfoInterface) {
        Logger.d(TAG + "configUpdateWifiSSID ssid:" + ssid);
        ProtocalV4DataHelper.getInstance().setConfigWifiInfoInterface(configWifiInfoInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configUpdateWifiName(ssid), null);
    }

    /**
     * 更新WiFi参数(配网)-路由器密码
     *
     * @param pwd
     * @param configWifiInfoInterface
     */
    public void configUpdateWifiPassword(String pwd, PPConfigWifiInfoInterface configWifiInfoInterface) {
        Logger.d(TAG + "configUpdateWifiPassword pwd：" + pwd);
        ProtocalV4DataHelper.getInstance().setConfigWifiInfoInterface(configWifiInfoInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configUpdateWifiPassword(pwd), null);
    }

    /**
     * 更新WiFi参数(配网)-结束
     *
     * @param configWifiInfoInterface
     */
    public void configUpdateWifiFinish(PPConfigWifiInfoInterface configWifiInfoInterface) {
        Logger.d(TAG + "configUpdateWifiFinish");
        ProtocalV4DataHelper.getInstance().setConfigWifiInfoInterface(configWifiInfoInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configUpdateWifiFinish(), null);
    }

    /**
     * 获取wifi列表
     */
    public void getWifiList(PPConfigWifiInfoInterface configWifiInfoInterface) {
        Logger.d(TAG + "getWifiList");
        ProtocalV4DataHelper.getInstance().setConfigWifiInfoInterface(configWifiInfoInterface);
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configGetWifiList(), null);

    }

    public void exitConfigWifi() {
        Logger.d(TAG + "exitConfigWifi");
        sendDataToWriteList(V4DelegateHelper.INSTANCE.cancelConfigNet(), null);
    }

    //秤亮灯指令
    public void openScaleLight() {
        Logger.d(TAG + "openScaleLight");
        sendDataToWriteList(V4DelegateHelper.INSTANCE.openScaleLight(), null);
    }

    //配网心跳包
    public void keepAlive() {
        Logger.d(TAG + "keepAlive");
        sendDataToWriteList(V4DelegateHelper.INSTANCE.configNetHeart(), null);
    }

    //获取设备绑定状态
    public void getDeviceBindState() {
        Logger.d(TAG + "getDeviceBindState");
        sendDataToWriteList(V4DelegateHelper.INSTANCE.getDeviceBindState(), null);
    }

    //获取设备Token 状态
    public void getDeviceTokenState() {
        Logger.d(TAG + "getDeviceTokenState");
        sendDataToWriteList(V4DelegateHelper.INSTANCE.getDeviceTokenState(), null);
    }

    //准备更新Token
    public void prepareUpdateToken() {
        Logger.d(TAG + "prepareUpdateToken");
        sendDataToWriteList(V4DelegateHelper.INSTANCE.prepareUpdateToken(), null);
    }

    //设备模式查询
    public void getCurrDeviceModel() {
        Logger.d(TAG + "getCurrDeviceModel");
        sendDataToWrite(V4DelegateHelper.INSTANCE.getCurrDeviceModel(), null);
    }

    //获取当前wifi Rssi
    public void getCurrWifiRSSI() {
        Logger.d(TAG + "getCurrWifiRSSI");
        sendDataToWriteList(V4DelegateHelper.INSTANCE.getCurrWifiRSSI(), null);
    }

    public void userMode() {
        Logger.d(TAG + "getCurrWifiRSSI");
        sendDataToWrite(V4DelegateHelper.INSTANCE.userMode(), null);
    }

    public void syncUserInfo(PPUserModel userModel, PPBleSendResultCallBack bleSendResultCallBack) {
        String byteStr = "";
        byteStr += "FD";
        byteStr += "37";
        byteStr += "00";
        byteStr += "00";
        byteStr += userModel != null && userModel.isAthleteMode ? "02" : "00";  //运动员级别（0：表示为普通1：表示为业余2：表示为专业）
        byteStr += userModel != null && userModel.sex == PPUserGender.PPUserGenderFemale ? "00" : "01";//性别（0：表示为女1：男）
        byteStr += userModel != null ? ByteUtil.decimal2Hex(userModel.age) : "00";//年龄（10-99 岁）
        byteStr += userModel != null ? ByteUtil.decimal2Hex(userModel.userHeight) : "00";//身高（100-220CM）
        byteStr += "00";
        byteStr += "00";
        byte[] bytes = ByteUtil.getXor(ByteUtil.stringToBytes(byteStr));
        sendDataToWrite(bytes, bleSendResultCallBack);
    }

    public void sendDataToF2(String sendData, PPBleSendResultCallBack sendResultCallBack) {
        sendData(sendData, characterFFF4, sendResultCallBack);
    }

    public void sendDataToWrite(byte[] sendData, PPBleSendResultCallBack sendResultCallBack) {
        sendData(sendData, characterFFF1, sendResultCallBack);
    }

    public void sendDataToWriteList(List<byte[]> dataList, PPBleSendResultCallBack bleSendListener) {
        sendBleDataList(dataList, characterFFF1, bleSendListener, true);
    }

    public void sendData(String sendData, UUID characterUUID, PPBleSendResultCallBack sendResultCallBack) {
        byte[] bytes = ByteUtil.stringToBytes(sendData);
        sendData(bytes, characterUUID, sendResultCallBack);
    }

    public void sendData(byte[] bytes, UUID characterUUID, PPBleSendResultCallBack sendResultCallBack) {
        if (bleSendManager == null) {
            bleSendManager = new BleSendManager.Builder()
                    .setDevice(currentDevice)
                    .setService(serverUUID)
                    .setCharacter(characterUUID)
                    .setBleClient(mBleClient)
                    .build();
        } else {
            bleSendManager.setDevice(currentDevice);
            bleSendManager.setService(serverUUID);
            bleSendManager.setBleClient(mBleClient);
            bleSendManager.setCharacter(characterUUID);
        }
        bleSendManager.sendData(bytes, sendResultCallBack);
    }

    public void sendDataListNoResponse(List<byte[]> dataList, UUID characterUUID, PPBleSendResultCallBack bleSendListener) {
        TorreHelper.lastSendOrReceiveDataTime = System.currentTimeMillis();
        sendBleDataList(dataList, characterUUID, bleSendListener, false);
    }

    private void sendBleDataList(List<byte[]> dataList, UUID characterUUID, PPBleSendResultCallBack bleSendListener, boolean isResponse) {
        if (bleSendManager == null) {
            bleSendManager = new BleSendManager.Builder()
                    .setDevice(currentDevice)
                    .setService(serverUUID)
                    .setCharacter(characterUUID)
                    .setBleClient(mBleClient)
                    .build();
        } else {
            bleSendManager.setDevice(currentDevice);
            bleSendManager.setService(serverUUID);
            bleSendManager.setBleClient(mBleClient);
            bleSendManager.setCharacter(characterUUID);
        }
        Logger.d(TAG + "sendBleDataList isResponse = " + isResponse);
        if (isResponse) {
            bleSendManager.sendDataList(dataList, bleSendListener);
        } else {
            bleSendManager.sendDataListNoResponse(dataList, bleSendListener);
        }
    }

    public void setBleStateInterface(PPBleStateInterface bleStateInterface) {
        this.bleStateInterface = bleStateInterface;
    }

    public void setDeviceSetInfoInterface(PPDeviceSetInfoInterface deviceSetInfoInterface) {
        this.deviceSetInfoInterface = deviceSetInfoInterface;
    }

    BleSendCrypt.OnAuthListener onAuthListener = new BleSendCrypt.OnAuthListener() {

        @Override
        public void startAuthToken() {
            //确认鉴权
            sendDataToWriteList(AuthenResponse.getInstance().commondComposeList, null);
        }

        @Override
        public void onAuthResult(boolean isSuccess) {
            if (isSuccess) {
                //发起鉴权指令
                sendDataToWriteList(BleSendCryptHelper.randomKey(), null);
            } else {
                disConnect();
            }
        }

        @Override
        public void onAuthKeyResult(boolean isSuccess) {
            if (isSuccess) {
                if (bleStateInterface != null) {
                    bleStateInterface.monitorBluetoothWorkState(PPBleWorkState.PPBleWorkStateWritable, currentDevice);
                }
            } else {
                disConnect();
            }
        }
    };

}
