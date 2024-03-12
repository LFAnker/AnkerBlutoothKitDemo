package com.peng.ppscale.business.torre;

import android.bluetooth.BluetoothGatt;
import android.text.TextUtils;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleMtuResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.peng.ppscale.business.ble.connect.CharacteristicUUID;
import com.peng.ppscale.business.ble.listener.PPBleSendResultCallBack;
import com.peng.ppscale.business.ble.listener.PPBleStateInterface;
import com.peng.ppscale.business.ble.listener.PPDeviceLogInterface;
import com.peng.ppscale.business.ble.listener.PPDeviceSetInfoInterface;
import com.peng.ppscale.business.ble.listener.PPHistoryDataInterface;
import com.peng.ppscale.business.ble.listener.PPTorreDeviceModeChangeInterface;
import com.peng.ppscale.business.ble.listener.PPUserInfoInterface;
import com.peng.ppscale.business.ble.send.BleSendManager;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.business.protocall.ProtocalFilterManager;
import com.peng.ppscale.business.state.PPBleWorkState;
import com.peng.ppscale.business.torre.dfu.DfuHelper;
import com.peng.ppscale.business.torre.listener.OnDFUStateListener;
import com.peng.ppscale.business.torre.listener.PPTorreConfigWifiInterface;
import com.peng.ppscale.business.torre.vo.DFUTransferContinueVo;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.DateUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.util.UnitUtil;
import com.peng.ppscale.vo.PPBodyBaseModel;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleSendState;
import com.peng.ppscale.vo.PPUserModel;

import java.util.List;
import java.util.UUID;

public class TorreDelegate implements TorreCharacterFliterInterface {

    private static volatile TorreDelegate instance = null;

    String TAG = TorreDelegate.class.getSimpleName() + " ";

    BluetoothClient mBleClient;
    PPDeviceModel currentDevice;
    private BleSendManager bleSendManager;
    UUID serverUUID;
    UUID characterUUID1;
    UUID characterUUID2;
    UUID characterUUID3;
    UUID characterDeviceInfoService;//设备信息的UUID
    UUID characterBatteryService;//电量的UUID
    UUID characterBatteryUUID;//电量的UUID

    List<BleGattCharacter> characterDeviceInfoCharacters;
    ProtocalFilterManager filterManager;
    PPUserModel userModel;
    PPUserModel syncUserModel;
    PPUserModel deleteUserModel;

    PPBleStateInterface bleStateInterface;
    //    Map<String, Integer> requestMtuMap = new HashMap<>();
    boolean isDFU = false;//控制DFU升级，用于中途退出停止升级
    boolean isFullyDFUState = false;//控制是否全量升级 true全量升级
    public static int configWifiTag = TorreHelper.CONFIG_WIFI_TAG_SSID;

    int sendTag = TorreHelper.SEND_TAG_NORMAL;//用于区分接收到的数据类型
    private PPBodyBaseModel bodyBaseModel;
    OnDFUStateListener dfuListener;
    String ssid;
    String password;
    String domainName;

    String dfuFilePath;//Dfu文件路径

    PPTorreDeviceModeChangeInterface modeChangeInterface;


    private TorreDelegate() {
    }

    public static TorreDelegate getInstance() {
        if (instance == null) {
            synchronized (TorreDelegate.class) {
                if (instance == null) {
                    instance = new TorreDelegate();
                }
            }
        }
        return instance;
    }

    public void bindBleClient(BluetoothClient bluetoothClient) {
        this.mBleClient = bluetoothClient;
        BleSendManager.getInstance().bindBleClient(bluetoothClient);
        if (this.filterManager == null) {
            this.filterManager = ProtocalFilterManager.get();
        }
    }

    public void bindDevice(PPDeviceModel currentDevice) {
        this.currentDevice = currentDevice;
    }

    @Override
    public void targetF1(UUID service, UUID character) {
        if (currentDevice == null) {
            Logger.e("targetF1 currentDevice is null");
            return;
        }
        if (mBleClient == null) {
            Logger.e("targetF1 mBleClient is null");
            return;
        }
        this.serverUUID = service;
        this.characterUUID1 = character;
        mBleClient.notify(currentDevice.getDeviceMac(), serverUUID, characterUUID1, new BleNotifyResponse() {

            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                String data = ByteUtil.byteToString(bytes);
                Logger.d("targetF1 onNotify1  mac = " + currentDevice.getDeviceMac() + " value = " + data);
                ProtocalTorreDeviceHelper.getInstance().analyticalData(data, currentDevice, characterUUID1);
            }

            @Override
            public void onResponse(int code) {
                Logger.d("targetF1 onResponse  code = " + code);
                if (code == 0) {
                    Logger.d("requestMtu 开始请求mtu");
                    mBleClient.requestMtu(currentDevice.getDeviceMac(), TorreHelper.normalMtuLen, mtuResponse);
                    if (bleStateInterface != null) {
                        bleStateInterface.monitorBluetoothWorkState(PPBleWorkState.PPBleWorkStateWritable, currentDevice);
                    }
                }
            }
        });
        mBleClient.read(currentDevice.getDeviceMac(), serverUUID, characterUUID1, new BleReadResponse() {

            @Override
            public void onResponse(int i, byte[] bytes) {
                if (currentDevice != null) {
                    String hex = ByteUtil.byteToString(bytes);
                    Logger.d("计算版本号 = " + hex);
                    String strData = ByteUtil.hexStringToString(hex);
                    Logger.d("计算版本号Str = " + strData);
                    currentDevice.setCalculateVersion(strData);
                }
            }
        });
    }

    @Override
    public void targetF2(UUID service, UUID character) {
        if (currentDevice == null) {
            Logger.e("targetF2 currentDevice is null");
            return;
        }
        if (mBleClient == null) {
            Logger.e("targetF2 mBleClient is null");
            return;
        }
        this.serverUUID = service;
        this.characterUUID2 = character;

        mBleClient.notify(currentDevice.getDeviceMac(), serverUUID, characterUUID2, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                TorreHelper.lastSendOrReceiveDataTime = System.currentTimeMillis();
                if ((bytes[0] & 0x0B) == 0x0B) {
                    Logger.d("targetF2 onNotify2  mac = " + currentDevice.getDeviceMac() + " DFU数据传输ACK[设备 ->APP] ");
                    //DFU升级ACK为了节省时间，此处直接回调
                    ProtocalTorreDeviceHelper.getInstance().analyticalDFUData(bytes, currentDevice, characterUUID2);
                } else {
                    String data = ByteUtil.byteToString(bytes);
                    ProtocalTorreDeviceHelper.getInstance().analyticalData(data, currentDevice, characterUUID2);
                }
            }

            @Override
            public void onResponse(int code) {
                Logger.d("targetF2 onResponse  code = " + code);
            }
        });

    }

    @Override
    public void targetF3(UUID service, UUID character) {
        if (currentDevice == null) {
            Logger.e("targetF3 currentDevice is null");
            return;
        }
        if (mBleClient == null) {
            Logger.e("targetF3 mBleClient is null");
            return;
        }
        this.serverUUID = service;
        this.characterUUID3 = character;
        PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
        bodyBaseModel.deviceModel = currentDevice;
        bodyBaseModel.userModel = userModel;
        bodyBaseModel.impedance = 0;
        bodyBaseModel.weight = 0;
        bodyBaseModel.unit = PPUnitType.Unit_KG;
        this.bodyBaseModel = bodyBaseModel;

        mBleClient.indicate(currentDevice.getDeviceMac(), serverUUID, characterUUID3, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                Logger.d("targetF3 onNotify  mac = " + currentDevice.getDeviceMac() + " value = " + ByteUtil.byteToString(bytes));
                String data = ByteUtil.byteToString(bytes);
                ProtocalTorreDeviceHelper.getInstance().analyticalData(data, currentDevice, characterUUID3);
            }

            @Override
            public void onResponse(int code) {
                Logger.d("targetF3 onResponse  code = " + code);
            }
        });
    }

    BleMtuResponse mtuResponse = new BleMtuResponse() {
        @Override
        public void onResponse(int status, Integer integer) {
            Logger.d("requestMtu result  status = " + status + " int = " + integer);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                TorreDelegate.getInstance().getMtuLen();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, 100);
//                requestMtuMap.put(currentDevice.getDeviceMac(), integer - 3);
            } else {
                if (currentDevice != null) {
                    currentDevice.mtu = 20;
                }
            }
        }
    };

    public void closeGatt() {
        Logger.d("closeGatt  address = " + currentDevice.getDeviceMac());
        if (serverUUID == null) {
            if (mBleClient != null) {
                mBleClient.disconnect(currentDevice.getDeviceMac());
            } else {
                Logger.e("closeGatt mBleClient is null");
            }
        } else {
            if (currentDevice != null && mBleClient != null) {
                mBleClient.refreshCache(currentDevice.getDeviceMac());
                mBleClient.clearRequest(currentDevice.getDeviceMac(), 0);
                mBleClient.disconnect(currentDevice.getDeviceMac());
            }
//            mBleClient.unindicate(currentDevice.getDeviceMac(), serverUUID, characterUUID1, new BleUnnotifyResponse() {
//                @Override
//                public void onResponse(int code) {
//                    Logger.d("closeGatt unindicate uuid: " + characterUUID1.toString() + " code = " + code);
//
//                }
//            });
//            mBleClient.unnotify(currentDevice.getDeviceMac(), serverUUID, characterUUID1, new BleUnnotifyResponse() {
//                @Override
//                public void onResponse(int code) {
//                    Logger.d("closeGatt unnotify uuid: " + characterUUID1.toString() + " code = " + code);
//                }
//            });
//            mBleClient.unnotify(currentDevice.getDeviceMac(), serverUUID, characterUUID2, new BleUnnotifyResponse() {
//                @Override
//                public void onResponse(int code) {
//                    Logger.d("closeGatt unnotify uuid: " + characterUUID2.toString() + " code = " + code);
//                }
//            });
//            mBleClient.unnotify(currentDevice.getDeviceMac(), serverUUID, characterUUID3, new BleUnnotifyResponse() {
//                @Override
//                public void onResponse(int code) {
//                    Logger.d("closeGatt unnotify uuid: " + characterUUID3.toString() + " code = " + code);
//                }
//            });
//            mBleClient.unindicate(currentDevice.getDeviceMac(), serverUUID, characterUUID2, new BleUnnotifyResponse() {
//                @Override
//                public void onResponse(int code) {
//                    Logger.d("closeGatt unindicate uuid: " + characterUUID2.toString() + " code = " + code);
//                }
//            });
//            mBleClient.unindicate(currentDevice.getDeviceMac(), serverUUID, characterUUID3, new BleUnnotifyResponse() {
//                @Override
//                public void onResponse(int code) {
//                    Logger.d("closeGatt unindicate uuid: " + characterUUID3.toString() + " code = " + code);
//                    if (code == 0) {
//
//                    } else {
//                    }
//                }
//            });
        }
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
                        if (modeChangeInterface != null) {
                            modeChangeInterface.readDevicePower(power);
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
    public void targetDeviceInfo(UUID uuid, List<BleGattCharacter> characters) {
        characterDeviceInfoService = uuid;
        characterDeviceInfoCharacters = characters;
//        readDeviceInfoFromCharacter();
    }

    public void readDeviceBattery(final PPTorreDeviceModeChangeInterface modeChangeInterface) {
        this.modeChangeInterface = modeChangeInterface;
        if (currentDevice != null && mBleClient != null) {
            mBleClient.read(currentDevice.getDeviceMac(), characterBatteryService, characterBatteryUUID, new BleReadResponse() {

                @Override
                public void onResponse(int i, byte[] bytes) {
                    if (currentDevice != null) {
                        String hex = ByteUtil.byteToString(bytes);
                        int power = ByteUtil.hexToTen(hex);
                        Logger.d("batteryRead 电量 = " + power);
                        currentDevice.setDevicePower(power);
                        if (modeChangeInterface != null) {
                            modeChangeInterface.readDevicePower(power);
                        }
                    }
                }
            });
        }
    }

    public void readDeviceInfoFromCharcter(UUID uuid, final PPTorreDeviceModeChangeInterface modeChangeInterface, final BleGattCharacter lastObj) {
        if (currentDevice != null) {
            final String characterID = uuid.toString();
            mBleClient.read(currentDevice.getDeviceMac(), characterDeviceInfoService, uuid, new BleReadResponse() {

                @Override
                public void onResponse(int i, byte[] bytes) {
                    if (currentDevice != null) {
                        String hex = ByteUtil.byteToString(bytes);
                        Logger.d("特征值返回 = " + hex + "characterID = " + characterID);
                        String strData = ByteUtil.hexStringToString(hex);
                        Logger.d("特征值返回字符串切换 = " + strData + "  characterID = " + characterID);
                        if (characterID.contains(CharacteristicUUID.serialNumberUUID)) {
                            Logger.d("serialNumber = " + strData);
                            //0
                            currentDevice.setSerialNumber(strData);
                        } else if (characterID.contains(CharacteristicUUID.firmwareRevisionUUID)) {
                            Logger.d("firmwareRevision = " + strData);
                            //1
                            currentDevice.setFirmwareVersion(strData);
                        } else if (characterID.contains(CharacteristicUUID.hardwareRevisionUUID)) {
                            Logger.d("hardwareRevision = " + strData);
                            //2
                            currentDevice.setHardwareVersion(strData);
                        } else if (characterID.contains(CharacteristicUUID.softwareRevisionUUID)) {
                            Logger.d("softwareRevision = " + strData);
                            currentDevice.setSoftwareVersion(strData);
                        }
                        if (lastObj.getUuid().toString().equals(characterID)) {
                            if (modeChangeInterface != null) {
                                modeChangeInterface.readDeviceInfoComplete(currentDevice);
                            } else {
                                Logger.e("readDeviceInfoFromCharcter deviceInfoInterface is null");
                            }
                        }
                    }
                }
            });
        } else {
            Logger.e("readDeviceInfoFromCharcter but device is null");
        }
    }

    public void readDeviceInfoFromCharacter(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        if (characterDeviceInfoCharacters != null && !characterDeviceInfoCharacters.isEmpty()) {
            BleGattCharacter lastCharacter = characterDeviceInfoCharacters.get(characterDeviceInfoCharacters.size() - 1);
            for (BleGattCharacter character : characterDeviceInfoCharacters) {
                readDeviceInfoFromCharcter(character.getUuid(), modeChangeInterface, lastCharacter);
            }
        } else {
            Logger.e("characterDeviceInfoCharacters is null");
        }
    }

    /**
     * 获取设备支持的MTU[App->设备][FFF1]
     */
    public void getMtuLen() {
        sendDataToF1("0200");
    }

    /**
     * 保活
     */
    public void keepAlive() {
        if (TorreDelegate.getInstance().isDFU()) {
            Logger.e("sendDataResponse dfu is runing please wait");
        } else if (System.currentTimeMillis() - TorreHelper.lastSendOrReceiveDataTime <= 3000) {
            Logger.e("FFF2 is running please wait");
        } else {
            Logger.d("keepAlive");
            sendDataToF1("1000");
        }
    }

    public void getLight(PPDeviceSetInfoInterface deviceSetInfoInterface) {
        ProtocalTorreDeviceHelper.getInstance().setDeviceSetInfoInterface(deviceSetInfoInterface);
        sendDataToF1("03020200");
    }

    public void setLight(int light, PPDeviceSetInfoInterface deviceSetInfoInterface) {
        ProtocalTorreDeviceHelper.getInstance().setDeviceSetInfoInterface(deviceSetInfoInterface);
        String hex = ByteUtil.decimal2Hex(light);
        sendDataToF1(String.format("030201%s", hex));
    }

    public void syncTime(long timeMillis, PPBleSendResultCallBack sendResultCallBack) {

//        String hex = ByteUtil.longToHexAndSpecLen(timeMillis / 1000, 8);
//        boolean is24 = true;
//        sendDataToF1(String.format("0406%s08%s", ByteUtil.hexToLittleEndianMode(hex), is24 ? "01" : "00"), sendResultCallBack);

        String syncTimeCMDHex = DateUtil.getCmdSyncTimeCMD(timeMillis);
        Logger.d(TAG + "syncTime syncTimeCMDHex:" + syncTimeCMDHex);
        sendDataToF1(syncTimeCMDHex, sendResultCallBack);
    }

    public void getUnit() {
        Logger.d("syncUnit getUnit");
        sendDataToF1(String.format("05020200"));
    }

    public void syncUnit(PPUnitType unitType, PPBleSendResultCallBack sendResultCallBack) {
        Logger.d("syncUnit unitType:" + unitType);
        String hex = ByteUtil.decimal2Hex(UnitUtil.unitTorre2Int(unitType));
        sendDataToF1(String.format("050201%s", hex), sendResultCallBack);
    }

    /**
     * 设备恢复出厂[App->设备][FFF1]
     */
    public void resetDevice(PPDeviceSetInfoInterface deviceSetInfoInterface) {
        ProtocalTorreDeviceHelper.getInstance().setDeviceSetInfoInterface(deviceSetInfoInterface);
        sendDataToF1("0900");
    }

    /**
     * 设备绑定状态
     *
     * @param type  1设置  2获取
     * @param state 0设备未绑定 1已绑定
     */
    public void deviceBindStatus(int type, int state) {
        Logger.d("deviceBindStatus type：" + type + " state:" + state);
        if (type == 1) {
            sendDataToF1(String.format("1C0201%s", state == 0 ? "00" : "01"));
        } else {
            sendDataToF1("1C020200");
        }
    }

    /**
     * 设置/获取演示模式状态
     *
     * @param type  1设置  2获取
     * @param state 0x00：关闭演示模式
     *              0x01：打开演示模式
     */
    public void demoModeSwitch(int type, int state) {
        Logger.d("demoModeSwitch type：" + type + " state:" + state);
        if (type == 1) {
            sendDataToF1(String.format("310201%s", state == 0 ? "00" : "01"));
        } else {
            sendDataToF1("31020200");
        }
    }


    /*****************************************************用户数据同步与删除********************************************************************************************/

    /**
     * 发送当前用户
     */
    public void sendCurrentUserinfoStart(PPUserModel userModel, PPUserInfoInterface userInfoInterface) {
        this.userModel = userModel;
        ProtocalTorreDeviceHelper.getInstance().setUserInfoInterface(userInfoInterface);
        if (userModel != null && userModel.userID != null) {
            Logger.d("currentUserInfo 在线测量下发用户开始");
            sendDataToF2("050306" + ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(TorreHelper.userIdMormalLen * 2), 4)));
        }
    }

    protected void sendCurrentUserInfo() {
        String userIdHex = TorreHelper.getUseridHex(userModel.userID, userModel.memberID);
        sendDataToF2List(ByteUtil.subAccordToMTU(userIdHex, currentDevice.mtu), new PPBleSendResultCallBack() {

            @Override
            public void onResult(PPScaleSendState sendState) {
                if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                    Logger.d("currentUserInfo 在线测量下发用户结束");
                    sendCurrentUserInfoEnd();
                }
            }
        });
    }

    /**
     * 发送当前用户数据结束
     */
    public void sendCurrentUserInfoEnd() {
        sendDataToF2("050107");
    }

    /**
     * 同步用户信息开始
     *
     * @param userModel
     */
    public void syncUserInfoStart(PPUserModel userModel) {
        this.syncUserModel = userModel;
        Logger.d("syncUserInfo 开始同步用户信息");
        sendDataToF2("050301D703");
    }

    protected void syncUserInfo() {
        PPUserModel userModel = this.syncUserModel == null ? this.userModel : this.syncUserModel;
        String userDataHex = TorreHelper.getSyncUserInfoHex(userModel);
        sendDataToF2List(ByteUtil.subAccordToMTU(userDataHex, currentDevice.mtu), new PPBleSendResultCallBack() {

            @Override
            public void onResult(PPScaleSendState sendState) {
                if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                    Logger.d("syncUserInfo 用户信息发送结束");
                    setUserInfoEnd();
                }
            }
        });
    }

    public void setUserInfoEnd() {
        sendDataToF2("050102");
    }

    public void getUserList(PPUserInfoInterface userInfoInterface) {
        sendTag = TorreHelper.SEND_TAG_USER_LIST;
        ProtocalTorreDeviceHelper.getInstance().clearStringBuffer();
        ProtocalTorreDeviceHelper.getInstance().setUserInfoInterface(userInfoInterface);
        sendDataToF2("05020500");
    }

    protected void getUserListConfirmEnd() {
        sendDataToF2("05020501");
    }

    public void deleteUserInfoStart(PPUserModel userModel) {
        this.deleteUserModel = userModel;
        Logger.d("deleteUserInfo 删除用户信息开始确认");
        sendDataToF2("050303" + ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(TorreHelper.userIdMormalLen * 2), 4)));
    }

    public void deleteAllUserInfo(PPUserModel userModel) {
        Logger.d("deleteUserInfo 删除所有用户开始");
        this.deleteUserModel = userModel;
        this.deleteUserModel.memberID = "";
        sendDataToF2("050303" + ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(TorreHelper.userIdMormalLen * 2), 4)));
    }

    protected void deleteUserInfo() {
        Logger.d("deleteUserInfo 删除用户信息开始发送数据");
        if (deleteUserModel != null) {
            String userIdHex = TorreHelper.getDeleteSendUseridHex(deleteUserModel.userID, deleteUserModel.memberID);
            sendDataToF2List(ByteUtil.subAccordToMTU(userIdHex, currentDevice.mtu), new PPBleSendResultCallBack() {

                @Override
                public void onResult(PPScaleSendState sendState) {
                    if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                        Logger.d("deleteUserInfo 删除用户信息发送结束");
                        deleteUserInfoEnd();
                    }
                }
            });
        } else {
            Logger.e("deleteUserInfo 删除用户信息时 用户数据为空");
        }

    }


    /**
     * 清除用户信息
     */
    public void clearDeviceUserInfo() {
        sendDataToF1("160101");
    }

    public void clearHistoryData() {
        sendDataToF1("160102");
    }

    public void clearAllDeviceInfo() {
        sendDataToF1("160100");
    }

    public void clearConfigWifiInfo() {
        sendDataToF1("160103");
    }

    public void clearSettingInfo() {
        sendDataToF1("160104");
    }

    /**
     * 删除用户信息发送结束
     */
    protected void deleteUserInfoEnd() {
        sendDataToF2("050104");
    }

    /**********************************************************日志同步***************************************************************************************/

    /**
     * @param logFilePath             指定文件存储路径，必传例如：val fileFath = context.filesDir.absolutePath + "/Log/DeviceLog"
     * @param torreDeviceLogInterface
     */
    public void syncLogStart(String logFilePath, PPDeviceLogInterface torreDeviceLogInterface) {
        ProtocalTorreDeviceHelper.getInstance().setTorreDeviceLogInterface(logFilePath, torreDeviceLogInterface);
        sendDataToF2("010101");
    }

    public void syncLog(String totalLen, String startPoint) {
        sendTag = TorreHelper.SEND_TAG_LOG;
        ProtocalTorreDeviceHelper.getInstance().clearStringBuffer();
        sendDataToF2(String.format("010902%s%s", totalLen, startPoint));
    }

    public void confirmSyncLogEnd() {
        sendDataToF2("010103");
    }

    /**********************************************************历史数据同步***************************************************************************************/
    /**
     * 开始同步历史数据，需要等待确认成功
     */
    public void syncHistoryStart(PPUserModel userModel, PPHistoryDataInterface historyDataInterface) {
        if (userModel != null) {
            Logger.d("syncHistory 开始确认同步历史数据");
            this.syncUserModel = userModel;
            ProtocalTorreDeviceHelper.getInstance().setHistoryDataInterface(historyDataInterface);
            sendDataToF2("020301" + ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(TorreHelper.userIdMormalLen * 2), 4)));
        }
    }

    /**
     * 获取用户历史数据[App->设备]
     */
    public void syncAllHistoryStart() {
        Logger.d("syncAllHistoryStart 获取设备全部历史数据");
        sendTag = TorreHelper.SEND_TAG_ALL_HISTORY;
        ProtocalTorreDeviceHelper.getInstance().clearStringBuffer();
        sendDataToF2("020105");
    }

    /**
     * 01:获取用户历史数据用户ID下发
     *
     * @param userModel
     */
    public void syncUserHistoryStart(PPUserModel userModel) {
        if (userModel != null) {
            Logger.d("syncHistory 开始确认同步历史数据");
            this.syncUserModel = userModel;
            sendDataToF2("020307" + ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(TorreHelper.userIdMormalLen), 4)));
        }
    }

    /**
     * 02: 获取用户历史数据用户ID下发[App->设备]
     */
    public void syncUserHistoryToUserInfo() {
        sendTag = TorreHelper.SEND_TAG_USER_HISTORY;
        ProtocalTorreDeviceHelper.getInstance().clearStringBuffer();
        ProtocalTorreDeviceHelper.getInstance().clearHistoryList();

        PPUserModel userModel = this.syncUserModel == null ? this.userModel : this.syncUserModel;
        Logger.d(String.format("syncHistory 开始下发%s用户信息", userModel.userName));

        String uidHex = ByteUtil.autoPadZero(ByteUtil.stringToHexString(userModel.userID), TorreHelper.userIdMormalLen * 2);

        sendDataToF2List(ByteUtil.subAccordToMTU(uidHex, currentDevice.mtu), new PPBleSendResultCallBack() {

            @Override
            public void onResult(PPScaleSendState sendState) {
                if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                    Logger.d("syncHistory 用户历史数据用户ID数据下发完成[App->设备]");
                    syncUserHistoryToUserInfoEnd();
                }
            }
        });
    }

    /**
     * 03: 获取用户历史数据[App->设备]
     */
    public void syncUserHistory() {
        Logger.d("syncHistory 获取用户历史数据");
        sendDataToF2("020109");
    }

    /**
     * 用户历史数据用户ID数据下发完成[App->设备]
     */
    private void syncUserHistoryToUserInfoEnd() {
        sendDataToF2("020108");
    }

    /**
     * 05: 确认用户历史数据发送结束ACK
     */
    public void syncUserHistoryEnd() {
        sendDataToF2("02020A00");
    }

    /**
     * 获取历史数据-发送用户信息
     */
    protected void syncHistoryToUserInfo() {
        sendTag = TorreHelper.SEND_TAG_HISTORY;
        ProtocalTorreDeviceHelper.getInstance().clearStringBuffer();

        PPUserModel userModel = this.syncUserModel == null ? this.userModel : this.syncUserModel;
        Logger.d(String.format("syncHistory 开始下发%s用户信息", userModel.userName));
        String userIdHex = TorreHelper.getUseridHex(userModel.userID, userModel.memberID);

        sendDataToF2List(ByteUtil.subAccordToMTU(userIdHex, currentDevice.mtu), new PPBleSendResultCallBack() {

            @Override
            public void onResult(PPScaleSendState sendState) {
                if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                    Logger.d("syncHistory 下发用户信息结束");
                    sendSyncHistoryUserInfoEnd();
                }
            }
        });
    }

    /**
     * 开始真正的获取历史数据
     */
    protected void syncHistroy() {
        Logger.d("syncHistory 开始获取设备历史数据");
        sendDataToF2("020103");
    }

    protected PPUserModel getSyncHistoryUserModel() {
        return this.syncUserModel == null ? this.userModel : this.syncUserModel;
    }

    /**
     * 历史数据-发送用户信息结束
     */
    public void sendSyncHistoryUserInfoEnd() {
        sendDataToF2("020102");
    }

    /**
     * 历史数据确认接收结束
     */
    public void sendSyncHistoryEnd() {
        sendDataToF2("020104");
    }

    public void sendSyncAllHistoryEnd() {
        sendDataToF2("02020A00");
    }

    /**********************************************************配网***************************************************************************************/
    /**
     * 配网开始
     *
     * @param ssid
     * @param password
     */
    public void configWifi(String domainName, String ssid, String password, PPTorreConfigWifiInterface configWifiInterface) {
        this.ssid = ssid;
        this.password = password;
        this.domainName = domainName;
        ProtocalTorreDeviceHelper.getInstance().setConfigWifiInterface(configWifiInterface);
        sendDataToF2("030101");
    }

    public void configWifiStart() {
        if (!TextUtils.isEmpty(domainName)) {
            sendDomainNameStart();
        } else if (!TextUtils.isEmpty(ssid)) {
            sendSSIDStart();
        } else if (!TextUtils.isEmpty(password)) {
            sendPasswordStart();
        }
    }

    public void configWifiSendData() {
        if (configWifiTag == TorreHelper.CONFIG_WIFI_TAG_DOMAIN) {
            sendDomainName();
        } else if (configWifiTag == TorreHelper.CONFIG_WIFI_TAG_SSID) {
            sendSSID();
        } else if (configWifiTag == TorreHelper.CONFIG_WIFI_TAG_PASSWORD) {
            sendPassword();
        }
    }

    public void sendSSIDStart() {
        Logger.e("targetF2 ssid开始确认");
        configWifiTag = TorreHelper.CONFIG_WIFI_TAG_SSID;
        String ssidHex = ByteUtil.stringToHexString(ssid) + "00";
        sendDataToF2(String.format("03040200%s", ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(ssidHex.length() / 2), 4))));
    }

    private void sendSSID() {
        Logger.e("targetF2 发送ssid数据");
        String ssidHex = ByteUtil.stringToHexString(ssid) + "00";
        sendDataToF2List(ByteUtil.subAccordToMTU(ssidHex, currentDevice.mtu), new PPBleSendResultCallBack() {

            @Override
            public void onResult(PPScaleSendState sendState) {
                if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                    Logger.e("targetF2 发送ssid数据结束");
                    configWifiEnd();
                }
            }
        });
    }

    public void sendPasswordStart() {
        Logger.e("targetF2 pwd开始确认");
        configWifiTag = TorreHelper.CONFIG_WIFI_TAG_PASSWORD;
        String passwordHex = ByteUtil.stringToHexString(password) + "00";
        sendDataToF2(String.format("03040201%s", ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(passwordHex.length() / 2), 4))));
    }

    private void sendPassword() {
        Logger.e("targetF2 发送pwd数据");
        String passwordHex = ByteUtil.stringToHexString(password) + "00";
        sendDataToF2List(ByteUtil.subAccordToMTU(passwordHex, currentDevice.mtu), new PPBleSendResultCallBack() {

            @Override
            public void onResult(PPScaleSendState sendState) {
                if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                    Logger.e("targetF2 发送pwd结束");
                    configWifiEnd();
                }
            }
        });
    }

    /**
     * 配置域名
     */
    public void sendDomainNameStart() {
        Logger.e("targetF2 domain开始确认");
        configWifiTag = TorreHelper.CONFIG_WIFI_TAG_DOMAIN;
        String domainHex = ByteUtil.stringToHexString(domainName) + "00";
        sendDataToF2(String.format("03040202%s", ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(domainHex.length() / 2), 4))));
    }

    /**
     * 配置域名
     */
    private void sendDomainName() {
        Logger.e("targetF2 发送domain数据");
        String domainHex = ByteUtil.stringToHexString(domainName) + "00";
        sendDataToF2List(ByteUtil.subAccordToMTU(domainHex, currentDevice.mtu), new PPBleSendResultCallBack() {

            @Override
            public void onResult(PPScaleSendState sendState) {
                if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                    Logger.e("targetF2 发送domain结束");
                    configWifiEnd();
                }
            }
        });
    }

    /**
     * 配置数据结束
     */
    public void configWifiEnd() {
        sendDataToF2("030103");
    }

    /**
     * 触发设备向服务器注册指令
     */
    public void sendRegistToServer() {
        Logger.e("targetF2 设备向服务器注册指令");
        sendDataToF2("030104");
    }

    /**
     * 退出配网
     */
    public void exitConfigWifi() {
        Logger.e("targetF2 退出配网");
        sendDataToF2("030105");
    }

    /**
     * 正常用户升级
     */
    public void startUserOTA() {
        sendDataToF1("0F0100");
    }

    /**
     * 调测用户升级
     */
    public void startTestOTA() {
        sendDataToF1("0F0101");
    }

    /**
     *
     */
    public void startLocalOTA() {
        sendDataToF1("0B00");
    }

    /**
     * 启动测量
     */
    public void startMeasure(PPBleSendResultCallBack sendResultCallBack) {
        Logger.d("startMeasure 启动测量");
        sendDataToF1("2400", sendResultCallBack);
    }

    public void stopMeasure(PPBleSendResultCallBack sendResultCallBack) {
        sendDataToF1("2800", sendResultCallBack);
    }

    /**
     * 切换婴儿模式
     *
     * @param mode   00使能抱婴模式 01退出抱婴模式
     * @param step   0x00：第一步  0x01：第二步
     * @param weight 重量[单位10g]：当步骤为0x01[第一步]时重量发0 当步骤为0x02[第二步]时重量发第一步测得的重量
     */
    public void switchBaby(int mode, int step, double weight, PPBleSendResultCallBack sendResultCallBack) {
        String modeStrHex = mode == 0 ? "00" : "01";
        String stepStrHex = step == 0 ? "00" : "01";
        String weightHex = TorreHelper.weightKgToHex(weight);
        weightHex = step == 0 ? "0000" : weightHex;
        Logger.d("switchBaby modeStrHex = " + modeStrHex + " stepStrHex = " + stepStrHex + " weightHex = " + weightHex);
        sendDataToF1(String.format("2E0501%s%s%s", modeStrHex, stepStrHex, weightHex), sendResultCallBack);
    }

    //0x00：关闭基础模式
    //0x01：打开基础模式
    public void controlImpendance(int state, PPTorreDeviceModeChangeInterface modeChangeInterface) {
        Logger.d("controlImpendance state:" + state);
        ProtocalTorreDeviceHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
        sendDataToF1(state == 0 ? "07020100" : "07020101");
    }

    //0打开 1关闭
    public void controlImpendance2(int state) {
        Logger.d("controlImpendance2 state:" + state);
        sendDataToF1(state == 0 ? "2C020100" : "2C020101");
    }


    //心率0打开 1关闭
    public void controlHeartRate(int state, PPTorreDeviceModeChangeInterface modeChangeInterface) {
        Logger.d("controlHeartRate state:" + state);
        ProtocalTorreDeviceHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
        sendDataToF1(state == 0 ? "06020100" : "06020101");
    }

    //获取阻抗开关状态
    public void getImpendanceState(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        ProtocalTorreDeviceHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
        sendDataToF1("07020200");
    }

    //获取心率状态
    public void getHeartRateState(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        ProtocalTorreDeviceHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
        sendDataToF1("06020200");
    }

    public void getWifiSSID() {
        sendTag = TorreHelper.SEND_TAG_CONFIG_WIFI_INFO;
        ProtocalTorreDeviceHelper.getInstance().clearStringBuffer();
        sendDataToF2("0303060001");
    }

    /**
     * 获取设备配网状态[App->设备]
     */
    public void getWifiState(PPTorreConfigWifiInterface configWifiInterface) {
        ProtocalTorreDeviceHelper.getInstance().setConfigWifiInterface(configWifiInterface);
        sendDataToF1("0E0100");
    }

    public void getWifiMac() {
        Logger.d("获取wifi mac  getWifiMac");
        sendDataToF1("2700");
    }

    public void confirmWifiSsidEnd() {
        sendDataToF2("03020601");
    }

    /**
     * 获取wifi列表
     */
    public void getWifiList(PPTorreConfigWifiInterface configWifiInterface) {
        ProtocalTorreDeviceHelper.getInstance().setConfigWifiInterface(configWifiInterface);
        Logger.d("targetF2 获取wifi列表");
        sendTag = TorreHelper.SEND_TAG_WIFI_LIST;
        ProtocalTorreDeviceHelper.getInstance().clearStringBuffer();
        sendDataToF2("040101");
    }

    /**
     * 确认wifi结束
     */
    public void confirmWifiListEnd() {
        sendDataToF2("040102");
    }

    public void confirmGetWifiInfoEnd() {
        sendDataToF2("0303060100");
    }

    public void sendDataToF1(String sendData) {
        sendData(sendData, characterUUID1, null);
    }

    public void sendDataToF1(String sendData, PPBleSendResultCallBack sendResultCallBack) {
        sendData(sendData, characterUUID1, sendResultCallBack);
    }

    public void sendDataToF2(String sendData) {
        TorreHelper.lastSendOrReceiveDataTime = System.currentTimeMillis();
        sendData(sendData, characterUUID2, null);
    }

    public void sendDataToF2List(List<byte[]> dataList, PPBleSendResultCallBack bleSendListener) {
        TorreHelper.lastSendOrReceiveDataTime = System.currentTimeMillis();
        sendBleDataList(dataList, characterUUID2, bleSendListener, true);
    }

    public void sendData(String sendData, UUID characterUUID, PPBleSendResultCallBack sendResultCallBack) {
        byte[] bytes = ByteUtil.stringToBytes(sendData);
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
            bleSendManager.setCharacter(characterUUID);
        }
        Logger.d("sendBleDataList isResponse = " + isResponse);
        if (isResponse) {
            bleSendManager.sendDataList(dataList, bleSendListener);
        } else {
            bleSendManager.sendDataListNoResponse(dataList, bleSendListener);
        }
    }

    public int getSendTag() {
        return sendTag;
    }

    public PPBodyBaseModel getBodyBaseModel() {
        return bodyBaseModel;
    }

    public void setBleStateInterface(PPBleStateInterface bleStateInterface) {
        this.bleStateInterface = bleStateInterface;
    }

    public void setUserModel(PPUserModel userModel) {
        this.userModel = userModel;
    }

    /**
     * 查询DFU协议版本
     *
     * @param isFullyDFUState 控制是否全量升级 true全量升级
     * @param dfuFilePath     升级文件路径，该路径应包含所有待升级文件以及配套的json
     */
    public void startDFU(boolean isFullyDFUState, String dfuFilePath, OnDFUStateListener dfuListener) {
        isDFU = true;
        this.isFullyDFUState = isFullyDFUState;
        this.dfuFilePath = dfuFilePath;
        this.dfuListener = dfuListener;
        Logger.d("targetF2 DFU文件路径：" + dfuFilePath);
        Logger.d("targetF2 查询DFU协议版本");
        if (dfuListener != null) {
            dfuListener.onDfuStart();
        }
        sendDataToF2("0B020100");
    }

    /**
     * 7.13.5 查询DFU状态[APP ->设备]
     */
    public void queryDFUState() {
        Logger.d("targetF2 查询DFU状态");
        sendDataToF2("0B0102");
    }

    /**
     * 7.13.7 开始DFU数据传输[APP ->设备]
     *
     * @param dfuTransferContinueVo
     */
    public void startDFUSend(DFUTransferContinueVo dfuTransferContinueVo) {
        Logger.d("targetF2  DFU 开始DFU数据传输");
        DfuHelper.currentPackageNum = 0;
        DfuHelper.packageNum = 0;
        DfuHelper.curentLen = 0;//重置进度sendNextFile();
        dfuTransferContinueVo.dfuTransferContinueFileType = -1;//升级顺序 应该是MCU->BLE->RES升级顺序U
        DfuHelper.dfuTransferContinueVo = dfuTransferContinueVo;
        if (dfuListener != null) {
            dfuListener.onStartSendDfuData();
        }
        sendNextFile();
    }

    public void sendDfuData() {
        Logger.d("targetF2  DFU sendDfuData");
        if (DfuHelper.currentPackageNum == DfuHelper.packageNum && DfuHelper.currentPackageNum != 0) {
            if (isDFU()) {
                if (DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType == 3 && dfuListener != null) {
                    //判断是RES结束后就认定100% 升级顺序的最后一个文件
                    dfuListener.onDfuProgress(100);
                }
                //文件结束指令
                sendDfuFileEnd();
            }
        } else {
            Logger.d("targetF2  DFU sendDfuData 开始读取数据 " + System.currentTimeMillis());
            if (isDFU()) {
                caculateProgress();
                List<byte[]> sendDfuData = DfuHelper.getSendDfuData(currentDevice.mtu);
//                for (byte[] sendDfuDatum : sendDfuData) {
//                    String writeData = ByteUtil.byteToString(sendDfuDatum);
//                    Logger.v("sendMessageListNoResponse writeData:" + writeData);
//                }
                Logger.d("targetF2  DFU sendDfuData 读取数据结束 开始发送 " + System.currentTimeMillis());
                if (sendDfuData != null && !sendDfuData.isEmpty()) {
                    sendDataListNoResponse(sendDfuData, characterUUID2, new PPBleSendResultCallBack() {

                        @Override
                        public void onResult(PPScaleSendState sendState) {
                            if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                                Logger.d("DFU sendDfuData 文件DFU单块发送结束");
                            }
                        }
                    });
                }
            }
        }
    }

    private void caculateProgress() {
        //升级顺序 应该是MCU->BLE->RES升级顺序  1mcu 0ble 3res
        int progress = 0;
        if (DfuHelper.fileLen != 0) {
            if (DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType == 0) {//ble 占比5%-10%
                progress = (int) (DfuHelper.curentLen * 100.0f / DfuHelper.fileLen * 0.1 + 5);
            } else if (DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType == 1) {//mcu 占比0%-5%
                progress = (int) (DfuHelper.curentLen * 100.0f / DfuHelper.fileLen * 0.05);
            } else if (DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType == 3) {//res 占比15%-85%
                progress = (int) (DfuHelper.curentLen * 100.0f / DfuHelper.fileLen * 0.85 + 15);
            }
            if (dfuListener != null) {
                dfuListener.onDfuProgress(progress);
            }
        }
    }

    public void sendDfuFileEnd() {
        sendDataToF2("0B0105");
    }

    /**
     * 升级顺序 应该是MCU->BLE->RES升级顺序
     * 上次断点DFU文件类型  1mcu 0ble 3res
     */
    public void sendNextFile() {
        DfuHelper.dfuTransferContinueVo.dfuTransferContinueState = 1;//断点续传状态Transfer continue  status 0-从上次的断点开始传 1-从头开始传输
        //1mcu 0ble 3res
        int fileType = DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType;
        if (isFullyDFUState) {
            if (fileType == -1) {
                //升级MCU
                DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType = 1;
                String startDfuData = DfuHelper.getStartDfuData(dfuFilePath);
                Logger.d("targetF2  DFU 组装\"开始DFU数据\" startDfuData : " + startDfuData);
                sendDataToF2(startDfuData);
            } else if (fileType == 0) {
                //BLE升级完成，开始升级RES
                DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType = 3;
                String startDfuData = DfuHelper.getStartDfuData(dfuFilePath);
                Logger.d("targetF2  DFU 开始组装RES数据 startDfuData : " + startDfuData);
                sendDataToF2(startDfuData);
            } else if (fileType == 1) {
                //MCU升级完成开始升级BLE
                DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType = 0;
                String startDfuData = DfuHelper.getStartDfuData(dfuFilePath);
                Logger.d("targetF2  DFU 开始组装BLE文件数据 startDfuData : " + startDfuData);
                sendDataToF2(startDfuData);
            } else if (fileType == 3) {
                //系统DFU流程结束
                Logger.d("发送系统DFU流程结束指令");
                sendDataToF2("0B0106");
            }
        } else {
            String firmwareVersion = currentDevice.getFirmwareVersion();
            Logger.d("currentDevice firmwareVersion:" + firmwareVersion);

            String[] versionArray = firmwareVersion.split("\\.");

            String mcuVersion = versionArray[0];
            String bleVersion = versionArray[1];
            String resVersion = versionArray[2];

            List<DfuHelper.DataVo> dataVos = DfuHelper.getDfuFileByte(dfuFilePath);

            DfuHelper.DataVo dataVoMCU = dataVos.get(0);
            DfuHelper.DataVo dataVoBLE = dataVos.get(1);
            DfuHelper.DataVo dataVoRES = dataVos.get(2);
            if (fileType == -1) {
                //开始升级MCU
                DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType = 1;
                Logger.d("DFU dataVoMCU version :" + dataVoMCU.getVersion() + " mcuVersion:" + mcuVersion);
                if (Integer.parseInt(dataVoMCU.getVersion()) > Integer.parseInt(mcuVersion)) {
                    String startDfuData = DfuHelper.getStartDfuData(dfuFilePath);
                    Logger.d("targetF2 DFU 开始升级MCU startDfuData : " + startDfuData);
                    sendDataToF2(startDfuData);
                } else {
                    Logger.d("targetF2 DFU MCU 无需升级");
                    sendNextFile();
                }
            } else if (fileType == 0) {
                //BLE升级完成，开始RES
                DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType = 3;
                Logger.d("DFU dataVoRES version :" + dataVoRES.getVersion() + " mcuVersion:" + mcuVersion);
                if (Integer.parseInt(dataVoRES.getVersion()) > Integer.parseInt(resVersion)) {
                    String startDfuData = DfuHelper.getStartDfuData(dfuFilePath);
                    Logger.d("targetF2  DFU 开始组装RES数据 startDfuData : " + startDfuData);
                    sendDataToF2(startDfuData);
                } else {
                    Logger.d("targetF2 DFU RES 无需升级");
                    sendNextFile();
                }
            } else if (fileType == 1) {
                //MCU升级完成，开始BLE
                DfuHelper.dfuTransferContinueVo.dfuTransferContinueFileType = 0;
                Logger.d("DFU dataVoBLE version :" + dataVoBLE.getVersion() + " bleVersion:" + bleVersion);
                if (Integer.parseInt(dataVoBLE.getVersion()) > Integer.parseInt(bleVersion)) {
                    String startDfuData = DfuHelper.getStartDfuData(dfuFilePath);
                    Logger.d("targetF2  DFU 开始组装BLE文件数据 startDfuData : " + startDfuData);
                    sendDataToF2(startDfuData);
                } else {
                    Logger.d("targetF2  DFU BLE 无需升级");
                    sendNextFile();
                }
            } else if (fileType == 3) {//升级完成
                if (dfuListener != null) {
                    dfuListener.onDfuProgress(100);
                }
                //系统DFU流程结束
                Logger.d("发送系统DFU流程结束指令");
                sendDataToF2("0B0106");
            }
        }
    }

    public void stopDFU() {
        isDFU = false;
    }

    public boolean isDFU() {
        return isDFU;
    }
}
