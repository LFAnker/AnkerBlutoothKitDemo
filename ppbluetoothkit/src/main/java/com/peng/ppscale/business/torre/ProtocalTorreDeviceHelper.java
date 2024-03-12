package com.peng.ppscale.business.torre;

import android.text.TextUtils;

import com.peng.ppscale.business.ble.configWifi.PPConfigStateMenu;
import com.peng.ppscale.business.ble.connect.CharacteristicUUID;
import com.peng.ppscale.business.ble.listener.PPBleStateInterface;
import com.peng.ppscale.business.ble.listener.PPDataChangeListener;
import com.peng.ppscale.business.ble.listener.PPDeviceLogInterface;
import com.peng.ppscale.business.ble.listener.PPDeviceSetInfoInterface;
import com.peng.ppscale.business.ble.listener.PPHistoryDataInterface;
import com.peng.ppscale.business.ble.listener.PPTorreDeviceModeChangeInterface;
import com.peng.ppscale.business.ble.listener.PPUserInfoInterface;
import com.peng.ppscale.business.ble.listener.ProtocalFilterImpl;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.business.ota.OnOTAStateListener;
import com.peng.ppscale.business.protocall.ProtocalDelegate;
import com.peng.ppscale.business.state.PPCallbackStatus;
import com.peng.ppscale.business.torre.listener.OnDFUStateListener;
import com.peng.ppscale.business.torre.listener.PPClearDataInterface;
import com.peng.ppscale.business.torre.listener.PPTorreConfigWifiInterface;
import com.peng.ppscale.business.torre.vo.DFUTransferContinueVo;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.FileUtilCallBack;
import com.peng.ppscale.util.FileUtilsKotlin;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.util.UnitUtil;
import com.peng.ppscale.vo.PPBodyBaseModel;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPUserModel;
import com.peng.ppscale.vo.PPWifiModel;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocalTorreDeviceHelper {

    public static final String BODY_PART_RIGHT_HAND = "rightHand";
    public static final String BODY_PART_LEFT_HAND = "leftHand";
    public static final String BODY_PART_TRUNK = "trunk";
    public static final String BODY_PART_RIGHT_FOOT = "rightFoot";
    public static final String BODY_PART_LEFT_FOOT = "leftFoot";
    private static volatile ProtocalTorreDeviceHelper instance = null;

    static String TAG = ProtocalTorreDeviceHelper.class.getSimpleName() + " ";

    String logFilePath;
    private StringBuffer stringBuffer;
    private List<String> numberList = new ArrayList<>();//存包序号，防止重复接收数据
    private List<String> historyList = new ArrayList<>();
    private PPDataChangeListener dataChangeListener;
    private PPHistoryDataInterface historyDataInterface;
    private PPDeviceSetInfoInterface deviceSetInfoInterface;
    private PPUserInfoInterface userInfoInterface;
    private PPDeviceLogInterface torreDeviceLogInterface;
    private OnOTAStateListener otaStateListener;
    public OnDFUStateListener dfuListener;
    private PPClearDataInterface clearDataInterface;
    PPTorreConfigWifiInterface torreConfigWifiInterface;
    PPTorreDeviceModeChangeInterface modeChangeInterface;
    PPDeviceModel deviceModel;
    private int logLen;
    private DFUTransferContinueVo dfuTransferContinueVo;

    private PPBleStateInterface bleStateInterface;

    private ProtocalTorreDeviceHelper() {
    }

    public static ProtocalTorreDeviceHelper getInstance() {
        if (instance == null) {
            synchronized (ProtocalTorreDeviceHelper.class) {
                if (instance == null) {
                    instance = new ProtocalTorreDeviceHelper();
                }
            }
        }
        return instance;
    }

    public void analyticalDFUData(byte[] bytes, PPDeviceModel deviceModel, UUID characterUUID) {
        this.deviceModel = deviceModel;
//        String dataHex = ByteUtil.byteToString(bytes);
//        Logger.d("targetF2 onNotify2  mac = " + deviceModel.getDeviceMac() + " dataHex = " + dataHex);
        if (bytes[3] == 0x01) {
            switch (bytes[2]) {
                case 0x03:
                    if (dfuListener != null) {
                        dfuListener.onInfoOout("analyticalDFUData 收到开始DFU数据传输ACK[设备 ->APP]");
                    }
                    Logger.d("analyticalDFUData 收到开始DFU数据传输ACK[设备 ->APP]");
                    TorreDelegate.getInstance().sendDfuData();
                    break;
                case 0x04:
//                    if (dfuListener != null) {
//                        dfuListener.onInfoOout("analyticalDFUData 收到块发送完成ACK");
//                    }
                    Logger.v("analyticalDFUData 收到块发送完成ACK");
                    TorreDelegate.getInstance().sendDfuData();
                    break;
                case 0x05:
                    if (dfuListener != null) {
                        dfuListener.onInfoOout("开始发送下一个文件");
                    }
                    Logger.d("开始发送下一个文件");
                    TorreDelegate.getInstance().sendNextFile();
                    break;
                case 0x06:
                    if (dfuListener != null) {
                        dfuListener.onDfuSucess();
                    }
                    Logger.d("onDfuSucess");
                    TorreDelegate.getInstance().stopDFU();
                    break;
                default:
//                  Logger.d("targetF2 onNotify2  mac = " + currentDevice.getDeviceMac() + " value = " + ByteUtil.byteToString(bytes));
                    String successInfo = ByteUtil.byteToString(bytes);
                    processDFU(successInfo);
                    break;
            }
        } else if (bytes[3] == 0x0a) {
            Logger.e("analyticalDFUData DFU 固件升级中");
        } else {
            String hex = ByteUtil.byteToStringHex(bytes[3]);
            Logger.e("analyticalDFUData DFU失败:" + hex);
            TorreDelegate.getInstance().stopDFU();
            if (dfuListener != null) {
                dfuListener.onDfuFail(hex);
            }
        }

    }

    public void analyticalData(String reciveData, PPDeviceModel deviceModel, UUID characterUUID) {
        this.deviceModel = deviceModel;
        if (characterUUID.toString().contains(CharacteristicUUID.TorreCharacteristicF1)) {
            Logger.d("protocoDataF1---------  reciveData---------  " + reciveData);
            protocoDataF1(reciveData);
        } else if (characterUUID.toString().contains(CharacteristicUUID.TorreCharacteristicF2)) {
            protocoDataF2(reciveData);
        } else {
//            if (!(reciveData.startsWith("01") ||
//                    reciveData.startsWith("02") ||
//                    reciveData.startsWith("03")) ||
//                    !lastReciveData.equals(reciveData)) {
//                lastReciveData = reciveData;
//            Logger.d("protocoDataF3--------- reciveData---------  " + reciveData);
            protocoDataF3(reciveData, TorreDelegate.getInstance().getBodyBaseModel());
//            }
        }
    }

    private void protocoDataF1(String reciveData) {
        String preStr = reciveData.substring(0, 2);
        switch (preStr) {
            case "02"://回复当前支持的MTU[设备->App][FFF1]
                int mtuLen = ByteUtil.hexToTen(reciveData.substring(6, 8) + reciveData.substring(4, 6));
                Logger.d("requestMtu result device mtu =   " + mtuLen);
                if (mtuLen <= 0) {
                    deviceModel.mtu = 20;
                } else {
                    deviceModel.mtu = mtuLen;
                }
                if (bleStateInterface != null) {
                    bleStateInterface.monitorMtuChange(deviceModel);
                }
                break;
            case "03"://回复设备屏幕亮度结果[设备->App][FFF1]
                if (deviceSetInfoInterface != null) {
                    if (reciveData.substring(4, 6).equals("01")) {
                        //设置亮度
                        String lightResult = reciveData.substring(6, 8);
                        if (lightResult.equals("00")) {
                            //亮度设置成功
                            deviceSetInfoInterface.monitorLightReviseSuccess();
                        } else {
                            //亮度设置失败
                            deviceSetInfoInterface.monitorLightReviseFail();
                        }
                    } else {//获取亮度
                        int lightNum = ByteUtil.hexToTen(reciveData.substring(6, 8));
                        deviceSetInfoInterface.monitorLightValueChange(lightNum);
                    }
                }
                break;
            case "04"://时间数据回复[设备->App][FFF1]
                Logger.d("syncTime reciveData:" + reciveData);
                String syncTimeResult = reciveData.substring(4, 6);
                if (syncTimeResult.equals("00")) {
                    Logger.d("syncTime success");
                } else {
                    Logger.e("syncTime fail reciveData:" + reciveData);
                }
                break;
            case "05"://回复设置单位结果[设备->App][FFF1]
                String unitType = reciveData.substring(4, 6);
                if (unitType.equals("01")) {//设置单位
                    Logger.d("syncUnit success");
                    int heartRateState = ByteUtil.hexToTen(reciveData.substring(6, 8));
                    if (modeChangeInterface != null) {
                        modeChangeInterface.readDeviceUnitCallBack(1, heartRateState, null);
                    }
                } else {//获取单位
                    Logger.d("getUnit success");
                    PPUnitType ppUnitType = UnitUtil.unitTorre2PPUnit(ByteUtil.hexToTen(reciveData.substring(6, 8)));
                    if (modeChangeInterface != null) {
                        modeChangeInterface.readDeviceUnitCallBack(2, 0, ppUnitType);
                    }
                }
                break;
            case "06"://回复设置单位结果[设备->App][FFF1]
                String heartRateType = reciveData.substring(4, 6);
                if (heartRateType.equals("01")) {//设置心率
                    int heartRateState = ByteUtil.hexToTen(reciveData.substring(6, 8));
                    if (modeChangeInterface != null) {
                        modeChangeInterface.readHeartRateStateCallBack(1, heartRateState);
                    }
                } else {//获取心率 0开启，1关闭
                    int heartRateState = ByteUtil.hexToTen(reciveData.substring(6, 8));
                    if (modeChangeInterface != null) {
                        modeChangeInterface.readHeartRateStateCallBack(2, heartRateState);
                    }
                }
                break;
            case "07":
                String impedanceType = reciveData.substring(4, 6);
                String impedanceState = reciveData.substring(6, 8);
                if (modeChangeInterface != null) {
                    modeChangeInterface.controlImpendanceCallBack(ByteUtil.hexToTen(impedanceType), ByteUtil.hexToTen(impedanceState));
                }
                break;
            case "09":
                String resetResult = reciveData.substring(4, 6);
                if (deviceSetInfoInterface != null) {
                    if (resetResult.equals("00")) {//恢复出厂成功
                        deviceSetInfoInterface.monitorResetStateSuccess();
                    } else {//恢复出厂结果
                        deviceSetInfoInterface.monitorResetStateFail();
                    }
                }
                break;
            case "10":
                String keepAliveResult = reciveData.substring(4, 6);
                if (keepAliveResult.equals("00")) {//连接保活
                    Logger.v("keepAlive keepAliveResult Success code : " + keepAliveResult);
                } else {//连接保活
                    Logger.e("keepAlive keepAliveResult Error code : " + keepAliveResult);
                }
                break;
            case "0B":
                String localOtaResult = reciveData.substring(4, 6);
                if (otaStateListener != null) {
                    if (localOtaResult.equals("00")) {//升级开始成功
                        otaStateListener.onStartUpdate();
                    } else if (localOtaResult.equals("01")) {
                        otaStateListener.onUpdateFail(0);
                    }
                }
                break;
            case "0E"://获取设备绑定状态回复
                String sendUidResult = reciveData.substring(6, 8);
                if (torreConfigWifiInterface != null) {
                    torreConfigWifiInterface.configWifiState(ByteUtil.hexToTen(sendUidResult));
                }
                break;
            case "0F":
                String otaResult = reciveData.substring(4, 6);
                if (otaStateListener != null) {
                    if (otaResult.equals("00")) {//升级开始成功
                        otaStateListener.onStartUpdate();
                    } else if (otaResult.equals("01")) {
                        otaStateListener.onUpdateFail(1);
                    } else if (otaResult.equals("02")) {
                        otaStateListener.onUpdateFail(2);
                    } else {
                        otaStateListener.onUpdateFail(ByteUtil.hexToTen(otaResult));
                    }
                }
                break;
            case "16":
                String clearDataResult = reciveData.substring(4, 6);
                if (clearDataInterface != null) {
                    if (clearDataResult.equals("00")) {
                        clearDataInterface.onClearSuccess();
                    } else {
                        clearDataInterface.onClearFail();
                    }
                }
                break;
            case "1C"://设备绑定状态
                String bindStateType = reciveData.substring(4, 6);
                Logger.d("DeviceBindState 设备绑定状态：" + bindStateType);
                if (modeChangeInterface != null) {
                    modeChangeInterface.bindStateCallBack(ByteUtil.hexToTen(bindStateType), ByteUtil.hexToTen(reciveData.substring(6, 8)));
                }
                break;
            case "24":
                String startMeasure = reciveData.substring(4, 6);
                Logger.d("startMeasure 启动测量结果：" + startMeasure);
                if (modeChangeInterface != null) {
                    modeChangeInterface.startMeasureCallBack(ByteUtil.hexToTen(startMeasure));
                }
                break;
            case "27":
                String wifiMac = reciveData.substring(4);
//                String wifiMac = ByteUtil.hexStringToString(wifiMac);
                StringBuffer wifiMacBuffer = new StringBuffer();
                for (int i = 0; i < wifiMac.length(); i++) {
                    if (i % 2 == 0) {
                        wifiMacBuffer.append(wifiMac.substring(i, i + 2));
                        if (i != wifiMac.length() - 2) {
                            wifiMacBuffer.append(":");
                        }
                    }
                }
                if (torreConfigWifiInterface != null) {
                    torreConfigWifiInterface.readDeviceWifiMacCallBack(wifiMacBuffer.toString());
                }
                break;
            case "2C":
                String impedanceType2 = reciveData.substring(4, 6);
                if (impedanceType2.equals("00")) {//类型错误
                    Logger.d("controlImpendance2 阻抗设置开关：类型错误");
                    if (modeChangeInterface != null) {
                        modeChangeInterface.controlImpendanceCallBack(ByteUtil.hexToTen(impedanceType2), PPCallbackStatus.ERROR);
                    }
                } else if (impedanceType2.equals("01")) {//设置开关
                    String impedanceState2 = reciveData.substring(6, 8);
                    Logger.d("controlImpendance2 阻抗设置开关：" + impedanceState2);
                    if (modeChangeInterface != null) {
                        modeChangeInterface.controlImpendanceCallBack(ByteUtil.hexToTen(impedanceType2), ByteUtil.hexToTen(impedanceState2));
                    }
                } else {//获取开关
                    String impedanceState2 = reciveData.substring(6, 8);
                    Logger.d("controlImpendance2 获取阻抗开关：" + impedanceState2);
                    if (modeChangeInterface != null) {
                        modeChangeInterface.controlImpendanceCallBack(ByteUtil.hexToTen(impedanceType2), ByteUtil.hexToTen(impedanceState2));
                    }
                }
                break;
            case "2E":
                break;
            case "31":
                String demoModeType = reciveData.substring(4, 6);
                String demoModeState2 = reciveData.substring(6, 8);
                if (demoModeType.equals("01")) {//设置演示模式状态
                    Logger.d("demoMode 设置演示模式状态：" + demoModeState2);
                } else {//获取演示模式状态
                    Logger.d("demoMode 获取演示模式状态：" + demoModeState2);
                }
                if (modeChangeInterface != null) {
                    modeChangeInterface.demoModeSwitchCallBack(ByteUtil.hexToTen(demoModeType), ByteUtil.hexToTen(demoModeState2));
                }
                break;
            default:
                break;
        }
    }

    private void protocoDataF2(String reciveData) {
        String preStr = reciveData.substring(0, 2);
        switch (preStr) {
            case "00":
                Logger.v("protocoDataF2--------- reciveData---------  " + reciveData);
                cacheData(reciveData);
                break;
            case "01"://返回Log大小和开始地址
                Logger.d("protocoDataF2--------- reciveData---------  " + reciveData);
                processLog(reciveData);
                break;
            case "02"://设备历史数据发送结束
                Logger.d("protocoDataF2--------- reciveData---------  " + reciveData);
                processHistoryData(reciveData);
                break;
            case "03":
                Logger.d("protocoDataF2--------- reciveData---------  " + reciveData);
                processConfigWifi(reciveData);
                break;
            case "04"://wifi列表
                Logger.d("protocoDataF2--------- reciveData---------  " + reciveData);
                processWifiList(reciveData);
                break;
            case "05"://用户数据下发开始
                Logger.d("protocoDataF2--------- reciveData---------  " + reciveData);
                processSyncUserInfo(reciveData);
                break;
            case "0B"://7.13 BLE DFU指令
                Logger.d("protocoDataF2--------- reciveData---------  " + reciveData);
                processDFU(reciveData);
                break;
        }
    }

    /**
     * 数据缓存
     *
     * @param reciveData
     */
    private void cacheData(String reciveData) {
        if (stringBuffer == null) {
            stringBuffer = new StringBuffer();
        }
        if (TorreDelegate.getInstance().getSendTag() == TorreHelper.SEND_TAG_LOG) {
            String logItem = reciveData.substring(4);
            if (stringBuffer != null) {
                stringBuffer.append(logItem);
                if (torreDeviceLogInterface != null) {
                    double v = stringBuffer.length() * 1.0 / logLen / 2;
                    torreDeviceLogInterface.syncLoging((int) (v * 100));
                }
            }
        } else if (TorreDelegate.getInstance().getSendTag() == TorreHelper.SEND_TAG_HISTORY) {
            String historyItem = reciveData.substring(4);
            if (stringBuffer != null) {
                stringBuffer.append(historyItem);
            }
        } else if (TorreDelegate.getInstance().getSendTag() == TorreHelper.SEND_TAG_WIFI_LIST) {
            String logItem = reciveData.substring(4);
            if (stringBuffer != null) {
                stringBuffer.append(logItem);
            }
        } else if (TorreDelegate.getInstance().getSendTag() == TorreHelper.SEND_TAG_INDEX_ACQUISITION) {
            String logItem = reciveData.substring(4);
            if (stringBuffer != null) {
                stringBuffer.append(logItem);
            }
        } else if (TorreDelegate.getInstance().getSendTag() == TorreHelper.SEND_TAG_USER_LIST) {
            String logItem = reciveData.substring(4);
            if (stringBuffer != null) {
                stringBuffer.append(logItem);
            }
        } else if (TorreDelegate.getInstance().getSendTag() == TorreHelper.SEND_TAG_CONFIG_WIFI_INFO) {
            String logItem = reciveData.substring(4);
            if (stringBuffer != null) {
                stringBuffer.append(logItem);
            }
        } else if (TorreDelegate.getInstance().getSendTag() == TorreHelper.SEND_TAG_ALL_HISTORY) {
            String logItem = reciveData.substring(4);
            if (stringBuffer != null) {
                stringBuffer.append(logItem);
            }
        } else if (TorreDelegate.getInstance().getSendTag() == TorreHelper.SEND_TAG_USER_HISTORY) {
            String numberStr = reciveData.substring(2, 4);
            if (!numberList.contains(numberStr)) {
                Logger.d("历史数据原始数据： reciveData： " + reciveData);
                numberList.add(numberStr);
                String logItem = reciveData.substring(4);
                if (stringBuffer != null) {
                    stringBuffer.append(logItem);
                }
            } else {
                Logger.d("被过滤掉的历史数据原始数据： reciveData： " + reciveData);
            }
        }
    }


    /**
     * 7.13 BLE DFU指令
     *
     * @param reciveData
     */
    private void processDFU(String reciveData) {
        if (dfuListener != null) {
            dfuListener.onInfoOout("reciveData：" + reciveData);
        }
        String cmd = reciveData.substring(4, 6);
        String errorTypeHex = reciveData.substring(6, 8);
        Logger.d("DFU errorTypeHex:" + errorTypeHex);
        if (onErrTypeCheck(errorTypeHex)) {
            if (cmd.equals("01")) {//7.13.4 查询DFU协议版本ACK[设备 ->APP]
                String dfuVersionHex = reciveData.substring(8, 10);//DFU协议版本号
                String maxChunkeSizeHex = reciveData.substring(10, 14);//最大传输块大小chunke size
                int maxChunkeSize = ByteUtil.hexToTen(ByteUtil.hexToLittleEndianMode(maxChunkeSizeHex));
                Logger.d("DFU maxChunkeSize:" + maxChunkeSize);

                dfuTransferContinueVo = new DFUTransferContinueVo();
                dfuTransferContinueVo.maxChunkeSize = maxChunkeSize;
                if (dfuListener != null) {
                    dfuListener.onInfoOout("查询DFU协议版本ACK");
                }
                TorreDelegate.getInstance().queryDFUState();
            } else if (cmd.equals("02")) {//查询DFU状态
                String dfuTransferContinueState = reciveData.substring(8, 10);//断点续传状态Transfer continue  status
                Logger.d("DFU dfuTransferContinueState:" + dfuTransferContinueState);
                //0-从上次的断点开始传输
                //1-从头开始传输
                String dfuTransferContinueFileType = reciveData.substring(10, 12);//上次断点DFU文件类型
                Logger.d("DFU dfuTransferContinueFileType:" + dfuTransferContinueFileType);
                String dfuTransferContinueFileVersion = reciveData.substring(12, 18);//上次断点DFU文件版本号-ANSSI码
                Logger.d("DFU dfuTransferContinueFileVersion:" + dfuTransferContinueFileVersion);
                String dfuTransferContinueUpgradedOffset = reciveData.substring(18, 26);//上次断点DFU文件已升级大小-文件OFFSET,APP根据此偏移继续下发升级数据，实现断点续传状态
                Logger.d("DFU dfuTransferContinueUpgradedOffset:" + dfuTransferContinueUpgradedOffset);

                dfuTransferContinueVo.dfuTransferContinueState = ByteUtil.hexToTen(dfuTransferContinueState);
                dfuTransferContinueVo.dfuTransferContinueFileType = ByteUtil.hexToTen(dfuTransferContinueFileType);
                dfuTransferContinueVo.dfuTransferContinueFileVersion = ByteUtil.hexStringToString(dfuTransferContinueFileVersion);
                dfuTransferContinueVo.dfuTransferContinueUpgradedOffset = ByteUtil.hexToTen(ByteUtil.hexToLittleEndianMode(dfuTransferContinueUpgradedOffset));

                Logger.d("DFU dfuTransferContinueVo: " + dfuTransferContinueVo.toString());
                if (dfuListener != null) {
                    dfuListener.onInfoOout("查询DFU状态ACK 开始发送第一个文件数据");
                }
                Logger.d("查询DFU协议版本ACK 开始发送第一个文件数据");
                TorreDelegate.getInstance().startDFUSend(dfuTransferContinueVo);
            }
//            else if (cmd.equals("03")) {//开始DFU数据传输
//                if (dfuListener != null) {
//                    dfuListener.onInfoOout("收到开始DFU数据传输ACK[设备 ->APP]");
//                }
//                Logger.d("收到开始DFU数据传输ACK[设备 ->APP]");
//                TorreDelegate.getInstance().sendDfuData();
//            } else if (cmd.equals("04")) {//每块数据需要ACK，最后一包不足一个块时也要回ACK
//                if (dfuListener != null) {
//                    dfuListener.onInfoOout("收到块发送完成ACK");
//                }
//                Logger.d("收到块发送完成ACK");
//                TorreDelegate.getInstance().sendDfuData();
//            } else if (cmd.equals("05")) {
//                if (dfuListener != null) {
//                    dfuListener.onInfoOout("开始发送下一个文件");
//                }
//                Logger.d("开始发送下一个文件");
//                TorreDelegate.getInstance().sendNextFile();
//            } else if (cmd.equals("06")) {
//                if (dfuListener != null) {
//                    dfuListener.onDfuSucess();
//                }
//            }
        }
    }

    private boolean onErrTypeCheck(String errorTypeHex) {
        if (errorTypeHex.equals("01")) {
            return true;
        } else {
            if (dfuListener != null) {
                dfuListener.onDfuFail(errorTypeHex);
            }
            return false;
        }
    }

    /**
     * 处理历史数据
     *
     * @param reciveData
     */
    private void processHistoryData(String reciveData) {
        String historyType = reciveData.substring(4, 6);
        if (historyType.equals("01")) {//历史数据用户ID下发开始ACK[设备->App][FFF2]
            String historyResult = reciveData.substring(6, 8);
            if (historyResult.equals("00")) {
                Logger.d("syncHistory 确认同步历史数据成功");
                TorreDelegate.getInstance().syncHistoryToUserInfo();
            } else {
                Logger.e("syncHistory 确认同步历史数据失败");
                if (historyDataInterface != null) {
                    historyDataInterface.monitorHistoryFail();
                }
            }
        } else if (historyType.equals("02")) {//历史数据用户信息发送完成
            String historyResult = reciveData.substring(6, 8);
            if (historyResult.equals("00")) {
                Logger.d("syncHistory 历史数据用户信息确认发送成功");
                TorreDelegate.getInstance().syncHistroy();
            } else {
                Logger.e("syncHistory 历史数据用户信息确认发送失败");
                if (historyDataInterface != null) {
                    historyDataInterface.monitorHistoryFail();
                }
            }
        } else if (historyType.equals("04")) {//设备历史数据发送结束[设备->App][FFF2]
            Logger.d("syncHistory 设备历史数据发送结束");
            TorreDelegate.getInstance().sendSyncHistoryEnd();
            if (historyDataInterface != null) {
                if (stringBuffer != null) {
                    //4516C462D309000100000000000000000000000000000000000000000000000000000000000000000000000000000000
                    String historyData = stringBuffer.toString();
                    int arrlen = historyData.length() / (TorreHelper.memberIdMormalLen * 2);
                    if (arrlen > 0) {
                        for (int i = 0; i < arrlen; i++) {
                            String itemStrHex = historyData.substring(i * TorreHelper.memberIdMormalLen * 2, (i + 1) * TorreHelper.memberIdMormalLen * 2);
                            Logger.d("syncHistory item Hex = " + itemStrHex);
                            String historyTimeHex = itemStrHex.substring(0, 8);
                            long timeL = TorreHelper.getTimeL(historyTimeHex);
                            if (timeL < ProtocalDelegate.ABNORMAL_HISTORY_INTERVAL_TIME_TORRE) {
                                continue;
                            }
                            String time = TorreHelper.getTime(historyTimeHex);
//                            double weightKg = TorreHelper.getWeightKg(itemStrHex.substring(8, 12));
                            int weightKgInt = TorreHelper.getWeightKgInt(itemStrHex.substring(8, 12));
                            int heartRate = TorreHelper.getHeartRate(itemStrHex.substring(12, 14));
                            String imType = itemStrHex.substring(14, 16);
                            int impedance = 0;
                            if (imType.equals("01")) {
                                impedance = TorreHelper.getImpedance(itemStrHex.substring(16, 24));
                            }
                            PPUserModel userModel = TorreDelegate.getInstance().getSyncHistoryUserModel();

                            PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
                            bodyBaseModel.weight = weightKgInt;
                            bodyBaseModel.impedance = impedance;
                            bodyBaseModel.deviceModel = deviceModel;
                            bodyBaseModel.userModel = userModel;
                            bodyBaseModel.heartRate = heartRate;
                            bodyBaseModel.isHeartRating = false;
                            historyDataInterface.monitorHistoryData(bodyBaseModel, time);
                        }
                    }
                }
//                historyDataInterface.monitorHistoryEnd(deviceModel);
            }
        } else if (historyType.equals("06")) {//设备历史数据发送结束[设备->App][FFF2]
            String historySendResult = reciveData.substring(6, 8);
            onAllHistorySync();
            if (historySendResult.equals("00")) {//当前用户历史数据发送完成
                Logger.d("syncAllHistory 当前用户历史数据发送完成");
            } else {//所有历史数据发送完成
                TorreDelegate.getInstance().sendSyncAllHistoryEnd();
                Logger.d("syncAllHistory 所有历史数据发送完成");
                if (historyDataInterface != null) {
//                    historyDataInterface.monitorHistoryEnd(deviceModel);
                }
            }
        } else if (historyType.equals("07")) {//获取用户历史数据下发开始ACK
            String historySendResult = reciveData.substring(6, 8);
            if (historySendResult.equals("00")) {//接收完成
                TorreDelegate.getInstance().syncUserHistoryToUserInfo();
            } else if (historySendResult.equals("01")) {//接收失败
                Logger.e("syncUserHistory 接收失败");
                if (historyDataInterface != null) {
                    historyDataInterface.monitorHistoryFail();
                }
            }
        } else if (historyType.equals("08")) {//用户历史数据用户ID数据下发完成ACK
            String historySendResult = reciveData.substring(6, 8);
            if (historySendResult.equals("00")) {//接收完成
                Logger.d("syncUserHistory 用户历史数据用户ID数据下发 接收完成");
                TorreDelegate.getInstance().syncUserHistory();
            } else if (historySendResult.equals("01")) {//接收失败
                Logger.e("syncUserHistory 用户历史数据用户ID数据下发 接收失败");
                if (historyDataInterface != null) {
                    historyDataInterface.monitorHistoryFail();
                }
            } else if (historySendResult.equals("02")) {//用户未找到
                Logger.e("syncUserHistory 用户历史数据用户ID数据下发 用户未找到");
                if (historyDataInterface != null) {
                    historyDataInterface.monitorHistoryFail();
                }
            }
        } else if (historyType.equals("0A")) {//用户历史数据发送结束[设备->App]
            String historySendResult = reciveData.substring(6, 8);
            String historyData = stringBuffer.toString();
            clearStringBuffer();
            if (!historyData.isEmpty()) {
                historyList.add(historyData);
                if (historySendResult.equals("00")) {//当前用户历史数据发送完成
                    Logger.d("syncUserHistory 当前用户历史数据发送完成");
                } else {//所有历史数据发送完成
                    TorreDelegate.getInstance().syncUserHistoryEnd();
                    Logger.d("syncUserHistory 历史数据全部接收完成");
                    if (!historyList.isEmpty()) {
                        for (int i = 0; i < historyList.size(); i++) {
                            onUserHistorySync(historyList.get(i));
                        }
                    } else {
                        Logger.d("syncUserHistory 当前历史数据为空");
                    }
                    historyList.clear();
                    if (historyDataInterface != null) {
//                        historyDataInterface.monitorHistoryEnd(deviceModel);
                    }
                }
            } else {
                TorreDelegate.getInstance().syncUserHistoryEnd();
                Logger.e("syncUserHistory 历史数据为空");
                if (historyDataInterface != null) {
//                    historyDataInterface.monitorHistoryEnd(deviceModel);
                }
            }
        }
    }

    /**
     * 指定用户（uid下面会包含多个memberId）历史数据
     */
    public void onUserHistorySync(String historyData) {
        if (!TextUtils.isEmpty(historyData)) {
            //4516C462D309000100000000000000000000000000000000000000000000000000000000000000000000000000000000
            Logger.d("syncHistory item historyData = " + historyData);
            int memberIdLen = TorreHelper.userIdMormalLen * 2;
            int historyAllLen = historyData.length() - memberIdLen;
            String memberIdHex = historyData.substring(0, memberIdLen);
            Logger.d("syncHistory item memberIdHex = " + memberIdHex);
            String memberId = ByteUtil.hexStringToString(memberIdHex);
            Logger.d("syncHistory item memberId = " + memberId);
            int hitoryItemLen = 96;
            int arrlen = historyAllLen / hitoryItemLen;
            if (arrlen > 0) {
                for (int i = 0; i < arrlen; i++) {
                    String itemStrHex = historyData.substring(memberIdLen + i * hitoryItemLen, memberIdLen + (i + 1) * hitoryItemLen);

                    historyItem(memberId, itemStrHex);
                }
            }
        } else {
            Logger.d("syncHistory item historyData is null");
        }
    }

    public void historyItem(String memberId, String itemStrHex) {
        Logger.d("syncHistory item Hex = " + itemStrHex);
        String timeHex = itemStrHex.substring(0, 8);
        Logger.d("syncHistory item timeHex = " + timeHex);
        long timeL = TorreHelper.getTimeL(timeHex);
        if (timeL < ProtocalDelegate.ABNORMAL_HISTORY_INTERVAL_TIME_TORRE) {
            return;
        }
        String time = TorreHelper.getTime(timeHex);
//                        double weightKg = TorreHelper.getWeightKg(itemStrHex.substring(8, 12));
        int weightKg = TorreHelper.getWeightKgInt(itemStrHex.substring(8, 12));
        Logger.d("syncHistory item weightKg : " + weightKg);
        int heartRate = TorreHelper.getHeartRate(itemStrHex.substring(12, 14));
        PPUserModel userModel = new PPUserModel.Builder().build();
        userModel.memberID = memberId;
        PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
        String imType = itemStrHex.substring(14, 16);
        Logger.d("syncHistory item imType = " + imType);
        if (imType.equals("01")) {
            //阻抗测量频率 0x00：20KHz&100KHz  0x01：50KHz
            int impedance = TorreHelper.getImpedance(itemStrHex.substring(16, 24));
            Logger.d("syncHistory item 4电极 impedance : " + impedance);
            bodyBaseModel.impedance = impedance;
        } else {
            //八电极设备 20kHz密文阻抗[4byte]  100kHz密文阻抗[4byte]
            //指定部位阻抗值 数据起始为最低位到高位：
            //如：0x03 (bit1 | bit0) ， 数据组成为：右手20KHz密文阻抗 + 右手100KHz密文阻抗 + 左手20KHz密文阻抗 + 左手100KHz密文阻抗
            String impedanceData = itemStrHex.substring(16);
            Logger.d("syncHistory item 8电极 impedanceData : " + impedanceData);
            int len = 8;
            //右手
            bodyBaseModel.z20KhzRightArmEnCode = TorreHelper.getImpedance(impedanceData.substring(0, len));
            bodyBaseModel.z100KhzRightArmEnCode = TorreHelper.getImpedance(impedanceData.substring(len, 2 * len));
            //左手
            bodyBaseModel.z20KhzLeftArmEnCode = TorreHelper.getImpedance(impedanceData.substring(2 * len, 3 * len));
            bodyBaseModel.z100KhzLeftArmEnCode = TorreHelper.getImpedance(impedanceData.substring(3 * len, 4 * len));
            //躯干
            bodyBaseModel.z20KhzTrunkEnCode = TorreHelper.getImpedance(impedanceData.substring(4 * len, 5 * len));
            bodyBaseModel.z100KhzTrunkEnCode = TorreHelper.getImpedance(impedanceData.substring(5 * len, 6 * len));
            //右脚
            bodyBaseModel.z20KhzRightLegEnCode = TorreHelper.getImpedance(impedanceData.substring(6 * len, 7 * len));
            bodyBaseModel.z100KhzRightLegEnCode = TorreHelper.getImpedance(impedanceData.substring(7 * len, 8 * len));
            //左脚
            bodyBaseModel.z20KhzLeftLegEnCode = TorreHelper.getImpedance(impedanceData.substring(8 * len, 9 * len));
            bodyBaseModel.z100KhzLeftLegEnCode = TorreHelper.getImpedance(impedanceData.substring(9 * len, 10 * len));
        }
        bodyBaseModel.weight = weightKg;
        bodyBaseModel.userModel = userModel;
//        PPDeviceModel ppDeviceModel = new PPDeviceModel("", "");
//        ppDeviceModel.deviceCalcuteType = PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeAlternate8;
        bodyBaseModel.deviceModel = deviceModel;
        bodyBaseModel.unit = PPUnitType.Unit_KG;
        bodyBaseModel.heartRate = heartRate;
        bodyBaseModel.isHeartRating = false;

        Logger.d("syncUserHistory weightKg:" + weightKg + " bodyBaseModel:" + bodyBaseModel.toString());
//        PPBodyFatModel bodyFatModel = new PPBodyFatModel(bodyBaseModel);
//        Logger.d("syncUserHistory 体脂数据 weightKg:" + weightKg + " fat:" + bodyFatModel.getPpFat() + " heartRate:" + heartRate + " errorType:" + bodyFatModel.getErrorType());
        if (historyDataInterface != null) {
            historyDataInterface.monitorHistoryData(bodyBaseModel, time);
        }
    }

    /**
     * 所有用户数据同步处理
     */
    private void onAllHistorySync() {
        if (stringBuffer != null) {
            //4516C462D309000100000000000000000000000000000000000000000000000000000000000000000000000000000000
            String historyData = stringBuffer.toString();
            if (!TextUtils.isEmpty(historyData)) {
                clearStringBuffer();
                Logger.d("syncHistory item historyData = " + historyData);
                int memberIdLen = TorreHelper.memberIdMormalLen * 2;
                int historyAllLen = historyData.length() - memberIdLen;
                int hitoryItemLen = 24;
                int arrlen = historyAllLen / hitoryItemLen;
                String memberIdHex = historyData.substring(0, memberIdLen);
                Logger.d("syncHistory item memberIdHex = " + memberIdHex);
                String memberId = ByteUtil.hexStringToString(memberIdHex);
                Logger.d("syncHistory item memberId = " + memberId);
                if (arrlen > 0) {
                    for (int i = 0; i < arrlen; i++) {
                        String itemStrHex = historyData.substring(memberIdLen + i * hitoryItemLen, memberIdLen + (i + 1) * hitoryItemLen);
                        Logger.d("syncHistory item Hex = " + itemStrHex);
                        String timeHex = itemStrHex.substring(0, 8);
                        Logger.d("syncHistory item timeHex = " + timeHex);
                        long timeL = TorreHelper.getTimeL(timeHex);
                        if (timeL < ProtocalDelegate.ABNORMAL_HISTORY_INTERVAL_TIME_TORRE) {
                            continue;
                        }
                        String time = TorreHelper.getTime(timeHex);
//                        double weightKg = TorreHelper.getWeightKg(itemStrHex.substring(8, 12));
                        int weightKgInt = TorreHelper.getWeightKgInt(itemStrHex.substring(8, 12));
                        int heartRate = TorreHelper.getHeartRate(itemStrHex.substring(12, 14));
                        String imType = itemStrHex.substring(14, 16);
                        int impedance = 0;
                        if (imType.equals("01")) {
                            impedance = TorreHelper.getImpedance(itemStrHex.substring(16, 24));
                        }
                        PPUserModel userModel = new PPUserModel.Builder().build();
                        userModel.memberID = memberId;
                        PPBodyBaseModel bodyBaseModel = new PPBodyBaseModel();
                        bodyBaseModel.weight = weightKgInt;
                        bodyBaseModel.impedance = impedance;
                        bodyBaseModel.deviceModel = deviceModel;
                        bodyBaseModel.userModel = userModel;
                        bodyBaseModel.heartRate = heartRate;
                        bodyBaseModel.isHeartRating = false;
                        if (historyDataInterface != null) {
                            historyDataInterface.monitorHistoryData(bodyBaseModel, time);
                        }
                    }
                }
            } else {
                Logger.d("syncHistory item historyData is null");
            }
        }
    }

    private void processLog(String reciveData) {
        String logType = reciveData.substring(4, 6);
        if (logType.equals("01")) {//返回LOG的HEAD信息
            String logLenHex = reciveData.substring(6, 14);
            logLen = TorreHelper.getImpedance(logLenHex);
            TorreDelegate.getInstance().syncLog(logLenHex, reciveData.substring(14, 22));
            if (torreDeviceLogInterface != null) {
                torreDeviceLogInterface.syncLogStart();
            }
        } else if (logType.equals("03")) {//设备发送LOG发送结束
            TorreDelegate.getInstance().confirmSyncLogEnd();
            if (stringBuffer != null) {
                String stringToString = ByteUtil.hexStringToString(stringBuffer.toString());
                FileUtilsKotlin.INSTANCE.createFileAndWrite(logFilePath, deviceModel.getDeviceName(), deviceModel.getDeviceMac(), stringToString, new FileUtilCallBack() {
                    @Override
                    public void callBack(@Nullable String logFilePath) {
                        if (torreDeviceLogInterface != null) {
                            torreDeviceLogInterface.syncLogEnd(logFilePath);
                        }
                    }
                });

//                String logFilePath = FileUtils.createFileAndWrite(context, stringToString);

            }
        }
    }

    /**
     * 同步用户信息
     *
     * @param reciveData
     */
    private void processSyncUserInfo(String reciveData) {
        String userInfoType = reciveData.substring(4, 6);
        switch (userInfoType) {
            case "01": {
                String userInfoStartResult = reciveData.substring(6, 8);
                if (userInfoStartResult.equals("00")) {
                    Logger.d("syncUserInfo 开始用户信息确认成功");
                    TorreDelegate.getInstance().syncUserInfo();
                } else {
                    Logger.e("syncUserInfo 开始用户信息确认失败");
                }
                break;
            }
            case "02": {
                String userInfoStartResult = reciveData.substring(6, 8);
                if (userInfoInterface != null) {
                    if (userInfoStartResult.equals("00")) {//下发用户信息成功
                        Logger.d("syncUserInfo 下发用户信息成功");
                        userInfoInterface.syncUserInfoSuccess();
                    } else if (userInfoStartResult.equals("FF")) {//数据总长度为0或接收长度与总长度不匹配
                        Logger.e("syncUserInfo 数据总长度为0或接收长度与总长度不匹配");
                        userInfoInterface.syncUserInfoFail();
                    } else {
                        Logger.e("syncUserInfo 下发用户信息失败");
                        userInfoInterface.syncUserInfoFail();
                    }
                }
                break;
            }
            case "03":
                String deleteUserConfirmResult = reciveData.substring(6, 8);
                if (deleteUserConfirmResult.equals("00")) {//删除用户信息确认成功
                    Logger.d("deleteUserInfo 删除用户信息确认成功");
                    TorreDelegate.getInstance().deleteUserInfo();
                } else {//删除失败
                    Logger.e("deleteUserInfo 删除用户信息确认失败");
                    if (userInfoInterface != null) {
                        userInfoInterface.deleteUserInfoFail(TorreDelegate.getInstance().deleteUserModel);
                    }
                }
                break;
            case "04"://删除用户ID数据下发结束返回
                String deleteUserResult = reciveData.substring(6, 8);
                if (userInfoInterface != null) {
                    if (deleteUserResult.equals("00")) {//删除用户信息确认成功
                        Logger.d("deleteUserInfo 删除用户信息成功");
                        userInfoInterface.deleteUserInfoSuccess(TorreDelegate.getInstance().deleteUserModel);
                    } else {//删除失败
                        Logger.e("deleteUserInfo 删除用户信息失败");
                        userInfoInterface.deleteUserInfoFail(TorreDelegate.getInstance().deleteUserModel);
                    }
                }
                break;
            case "05"://获取已存储用户ID返回结束
                String userListHex = stringBuffer.toString();

                String subuserNum = userListHex.substring(0, 2);

                Logger.d("获取已存储用户ID 用户数 subuser Num = " + subuserNum);

                String substring1 = userListHex.substring(2);
                int num = substring1.length() / 128;
                List<String> list = new ArrayList<>();
                for (int i = 0; i < num; i++) {
                    String substring2 = substring1.substring(i * 128, (i + 1) * 128);
                    String userId = ByteUtil.hexStringToString(substring2);
                    Logger.d("getUserInfo userId = " + userId);
                    list.add(userId);
                }
                TorreDelegate.getInstance().getUserListConfirmEnd();
                if (userInfoInterface != null) {
                    userInfoInterface.getUserListSuccess(list);
                }
                break;
            case "06":
                String currentUserStartResult = reciveData.substring(6, 8);
                if (currentUserStartResult.equals("00")) {
                    Logger.d("currentUserResult 在线用户下发确认成功");
                    TorreDelegate.getInstance().sendCurrentUserInfo();
                } else {
                    Logger.e("currentUserResult 在线用户下发确认失败");
                    if (userInfoInterface != null) {
                        userInfoInterface.confirmCurrentUserInfoFail();
                    }
                }
                break;
            case "07":
                if (userInfoInterface != null) {
                    String currentUserResult = reciveData.substring(6, 8);
                    if (currentUserResult.equals("00")) {
                        Logger.d("currentUserResult 在线用户下发成功");
                        userInfoInterface.confirmCurrentUserInfoSuccess();
                    } else if (currentUserResult.equals("01")) {
                        userInfoInterface.confirmCurrentUserInfoFail();
                        Logger.e("currentUserResult 参数错误");
                    } else {
                        userInfoInterface.confirmCurrentUserInfoFail();
                        Logger.e("currentUserResult 用户未找到");
                    }
                }
                break;
        }
    }

    private void processWifiList(String reciveData) {
        String wifiListEnd = reciveData.substring(4, 6);
        if (wifiListEnd.equals("02")) {
            TorreDelegate.getInstance().confirmWifiListEnd();
            String wifiListHex = stringBuffer.toString();

            Logger.d("wifiListHex targetF2 hex :" + wifiListHex);

            List<String> hexList = new ArrayList<>();
            int j = 0;
            for (int i = 0; i < wifiListHex.length(); i += 2) {
                String hexI = wifiListHex.substring(i, i + 2);
                if (hexI.equals("00")) {
                    String hex = wifiListHex.substring(j, i);
                    if (hex.length() > 40) {
                        hexList.add(hex);
                        j = i + 2;
                        Logger.d("wifiListHex targetF2 item hex :" + hex);
                    } else  {
                        Logger.e("wifiListHex targetF2 item error hex :" + hex);
                    }
                }
            }

//            String[] split = wifiListHex.split("00");
//            for (int i = 0; i < split.length; i++) {
//                int index = wifiListHex.indexOf("00");
//                if (index % 2 != 0) {
//                    index += 1;
//                }
//                String hex0 = wifiListHex.substring(index, index + 2);
//                if (hex0.equals("00")) {
//                    String hex = wifiListHex.substring(0, index);
//                    hexList.add(hex);
//                    Logger.d("wifiListHex targetF2 item hex :" + hex);
//                    wifiListHex = wifiListHex.substring(index + 2);
//                }
//            }

            List<PPWifiModel> wifiModels = new ArrayList<>();
            if (!hexList.isEmpty()) {
                for (int i = 0; i < hexList.size(); i++) {
                    PPWifiModel ppWifiModel = new PPWifiModel();
                    String item = hexList.get(i);
                    int sign = ByteUtil.hexToTen(item.substring(0, 2));
                    String ssidHex = item.substring(36);
                    Logger.e("wifiListHex targetF2 ssidHex： " + ssidHex);
                    String ssid = ByteUtil.hexStringToString(ssidHex);
                    ppWifiModel.setSign(sign);
                    ppWifiModel.setSsid(ssid);
                    wifiModels.add(ppWifiModel);
                    Logger.e("wifiListHex targetF2 ssid： " + ssid + " sign = " + sign);
                }
            }
            stringBuffer.delete(0, stringBuffer.length());
            if (torreConfigWifiInterface != null) {
                torreConfigWifiInterface.monitorWiFiListSuccess(wifiModels);
            }
        }
    }

    private void processConfigWifi(String reciveData) {
        String configWifiType = reciveData.substring(4, 6);
        String registErrorCode = "";
        PPConfigStateMenu configStateMenu = null;
        if (configWifiType.equals("01")) { //开始配网
            String configWifiResult = reciveData.substring(6, 8);
            if (configWifiResult.equals("00")) {//接收完成
                Logger.d("targetF2 确认开始配网成功");
                TorreDelegate.getInstance().configWifiStart();
            } else {
                configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_START_FAIL;
            }
        } else if (configWifiType.equals("02")) {//配网数据下发开始
            String configWifiResult = reciveData.substring(6, 8);
            if (configWifiResult.equals("00")) {//接收完成
                Logger.d("targetF2 确认开始发送数据成功");
                TorreDelegate.getInstance().configWifiSendData();
            } else {
                configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_DATA_SEND_FAIL;
            }
        } else if (configWifiType.equals("03")) {
            if (reciveData.length() < 10) {
                return;
            }
            String configWifiResult = reciveData.substring(6, 8);
            registErrorCode = reciveData.substring(8, 10);
            if (configWifiResult.equals("00")) {//ssid
                if (registErrorCode.equals("00")) {//接收成功
                    Logger.d("targetF2 确认ssid下发结束成功");
                    TorreDelegate.getInstance().sendPasswordStart();
                    return;
                } else if (registErrorCode.equals("01")) {//配置信息指令顺序错误
                } else if (registErrorCode.equals("02")) {//配置信息格式错误(无结束符)
                } else if (registErrorCode.equals("03")) {//配置信息传输错误
                } else if (registErrorCode.equals("04")) {//超时错误
                } else if (registErrorCode.equals("05")) {//URL不带HTTP或HTTPS协议头
                } else {//接收长度错误
                }
                configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_DATA_SEND_SSID_FAIL;
                Logger.e("targetF2 确认ssid下发失败");
            } else if (configWifiResult.equals("01")) {//pwd
                if (registErrorCode.equals("00")) {//接收成功
                    Logger.d("targetF2 确认pwd下发结束成功");
                    TorreDelegate.getInstance().sendRegistToServer();
                    return;
                } else if (registErrorCode.equals("01")) {//配置信息指令顺序错误
                } else if (registErrorCode.equals("02")) {//配置信息格式错误(无结束符)
                } else if (registErrorCode.equals("03")) {//配置信息传输错误
                } else if (registErrorCode.equals("04")) {//超时错误
                } else if (registErrorCode.equals("05")) {//URL不带HTTP或HTTPS协议头
                } else {//接收长度错误
                }
                configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_DATA_SEND_PWD_FAIL;
                Logger.e("targetF2 确认pwd下发失败");
            } else if (configWifiResult.equals("02")) {//domain
                if (registErrorCode.equals("00")) {//接收成功
                    Logger.e("targetF2 确认domain下发结束成功");
                    TorreDelegate.getInstance().sendSSIDStart();
                    return;
                } else if (registErrorCode.equals("01")) {//配置信息指令顺序错误
                } else if (registErrorCode.equals("02")) {//配置信息格式错误(无结束符)
                } else if (registErrorCode.equals("03")) {//配置信息传输错误
                } else if (registErrorCode.equals("04")) {//超时错误
                } else if (registErrorCode.equals("05")) {//URL不带HTTP或HTTPS协议头
                } else {//接收长度错误
                }
                configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_DATA_SEND_DOMAIN_FAIL;
                Logger.e("targetF2 确认domain下发失败");
            }
        } else if (configWifiType.equals("04")) {//设备向服务器注册
            registErrorCode = reciveData.substring(8, 10);
            String configWifiResult = reciveData.substring(6, 8);
            if (configWifiResult.equals("00")) {//设备向服务器注册成功
                Logger.e("targetF2 设备向服务器注册成功");
                configStateMenu = PPConfigStateMenu.CONFIG_STATE_SUCCESS;
            } else if (configWifiResult.equals("01")) {//超时失败
                Logger.e("targetF2 超时失败 errorCode = " + registErrorCode);
                configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_REGIST_TIMEOUT;
                if (registErrorCode.equals("0001")) {//路由器连接超时
                } else if (registErrorCode.equals("0002")) {//注册服务器超时
                }
            } else if (configWifiResult.equals("02")) {//路由器连接失败
                Logger.e("targetF2 设备向服务器注册 路由器连接失败 errorCode = " + registErrorCode);
                configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_REGIST_ROUTER;
            } else if (configWifiResult.equals("03")) {//收到HTTP网络错误码
                Logger.e("targetF2 收到HTTP网络错误码 errorCode = " + registErrorCode);
                if (registErrorCode.equals("0000")) {//无错误
                } else {//0x0001-0xFFFF
                    configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_REGIST_HTTP;
                }
            } else if (configWifiResult.equals("04")) {//收到HTTPS网络错误码
                Logger.e("targetF2 设备向服务器注册 收到HTTPS网络错误码 errorCode = " + registErrorCode);
                if (registErrorCode.equals("0000")) {//无错误
                } else {//0x0001-0xFFFF
                    configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_REGIST_HTTPS;
                }
            } else if (configWifiResult.equals("05")) {//收到注册服务器返回失败
                Logger.e("targetF2 设备向服务器注册 收到注册服务器返回失败 errorCode = " + registErrorCode);
                configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_REGIST_SERVER;
            } else if (configWifiResult.equals("06")) {
                Logger.e("targetF2 设备向服务器注册 注册失败[配网指令漏发] errorCode = " + registErrorCode);
                configStateMenu = PPConfigStateMenu.CONFIG_STATE_ERROR_TYPE_REGIST_FAIL;
            }
        } else if (configWifiType.equals("05")) {//退出配网
            configStateMenu = PPConfigStateMenu.CONFIG_STATE_EXIT;
        } else if (configWifiType.equals("06")) {//返回设备配网信息结束[设备->App]
            String wifiListEnd = reciveData.substring(6, 8);
            if (wifiListEnd.equals("01")) {
                TorreDelegate.getInstance().confirmGetWifiInfoEnd();
                String ssid = "";
                if (stringBuffer != null && stringBuffer.length() > 0) {
                    String wifiListHex = stringBuffer.toString();
                    Logger.d("wifiListHex targetF2 hex :" + wifiListHex);
                    String ssidHex = wifiListHex.substring(4, wifiListHex.length() - 2);
                    ssid = ByteUtil.hexStringToString(ssidHex);
                }
                if (torreConfigWifiInterface != null) {
                    torreConfigWifiInterface.readDeviceSsidCallBack(ssid, 0);
                }
            } else {
                if (torreConfigWifiInterface != null) {
                    torreConfigWifiInterface.readDeviceSsidCallBack("", 1);
                }
            }
            return;
        }
        if (torreConfigWifiInterface != null && configStateMenu != null) {
            torreConfigWifiInterface.configResult(configStateMenu, reciveData);
        }
    }

    protected void clearStringBuffer() {
        if (stringBuffer == null) {
            stringBuffer = new StringBuffer();
        } else {
            stringBuffer.delete(0, stringBuffer.length());
        }
        if (numberList != null) {
            numberList.clear();
        }
        Logger.d(TAG + " clearStringBuffer local cache");
    }

    protected void clearHistoryList() {
        historyList.clear();
        Logger.d(TAG + " clearHistoryList local cache");
    }

    private void protocoDataF3(String reciveData, PPBodyBaseModel bodyBaseModel) {
        String preStr = reciveData.substring(0, 2);
        switch (preStr) {
            case "01":
                String measuStateLow = reciveData.substring(4, 6);
                String measuStateHigh = reciveData.substring(6, 8);
                byte measuStateLowByte = ByteUtil.stringToBytes(measuStateLow)[0];
                byte measuStateHighByte = ByteUtil.stringToBytes(measuStateHigh)[0];
                if ((measuStateLowByte & 0x01) == 0x01) {
                    //抓零成功
                    Logger.d("protocoDataF3 抓零成功");
                } else {
                    //抓零中
                    Logger.d("protocoDataF3 抓零中");
                    return;
                }
                if ((measuStateLowByte & 0x02) == 0x02) {
                    //标定模式
                    Logger.d("protocoDataF3 标定模式");
                    return;
                } else {
                    //测量模式
                    Logger.d("protocoDataF3 测量模式");
                }

                int weightKgInt = TorreHelper.getWeightKgInt(reciveData.substring(8, 12));

                bodyBaseModel.unit = TorreHelper.getPpUnitType(reciveData);

                if ((measuStateLowByte & 0x0C) == 0x0C) {
                    Logger.d("protocoDataF3 离秤");
                    //离秤
                    if (dataChangeListener != null) {
                        //实时重量  离秤的同时也可能有重量上来
                        bodyBaseModel.weight = weightKgInt;
//                        dataChangeListener.monitorProcessData(bodyBaseModel, deviceModel);
                    }
                } else if ((measuStateLowByte & 0x08) == 0x08) {
                    //超重
                    Logger.e("protocoDataF3 超重");
                    if (dataChangeListener != null) {
                        dataChangeListener.monitorOverWeight();
                    }
                } else if ((measuStateLowByte & 0x04) == 0x04) {
                    Logger.d("protocoDataF3 稳定重量: " + weightKgInt);
                    //稳定重量
                    bodyBaseModel.weight = weightKgInt;
                    if (dataChangeListener != null) {
                        //由于在拿到稳定重量后，秤端还有很多后续操作，为了让App的展示与秤一致，所以在拿到稳定重量也同步给过程回调
//                        dataChangeListener.monitorProcessData(bodyBaseModel, deviceModel);
                    }
                } else {
                    Logger.d("protocoDataF3 实时重量: " + weightKgInt);
                    if (dataChangeListener != null) {
                        //实时重量
                        bodyBaseModel.weight = weightKgInt;
//                        dataChangeListener.monitorProcessData(bodyBaseModel, deviceModel);
                    }
                }
                if ((measuStateLowByte & 0x30) == 0x30) {
                    //阻抗测量失败
                    Logger.e("protocoDataF3 阻抗测量失败");
                } else if ((measuStateLowByte & 0x20) == 0x20) {
                    //阻抗测量成功
                    Logger.e("protocoDataF3 阻抗测量成功");
                    bodyBaseModel.isHeartRating = false;
                } else if ((measuStateLowByte & 0x10) == 0x10) {
                    //阻抗测量中
                    Logger.e("protocoDataF3 阻抗测量中");
                    if (dataChangeListener != null) {
                        dataChangeListener.onImpedanceFatting();
                    }
                } else {
                    //阻抗未测量
                    Logger.e("protocoDataF3 阻抗未测量");
                }
                if ((measuStateHighByte & 0x40) == 0x40) {
                    //关机
                    Logger.e("protocoDataF3 秤关机了");
                    if (dataChangeListener != null) {
                        dataChangeListener.onDeviceShutdown();
                    }
                }
                if ((measuStateLowByte & 0xC0) == 0xC0) {
                    //心率测量失败
                    Logger.e("protocoDataF3 心率测量失败");
                    bodyBaseModel.isHeartRating = false;
                } else if ((measuStateLowByte & 0x80) == 0x80) {
                    //心率测量成功
                    Logger.e("protocoDataF3 心率测量成功");
                    bodyBaseModel.heartRate = ByteUtil.hexToTen(reciveData.substring(14, 16));
                    bodyBaseModel.isHeartRating = false;
                } else if ((measuStateLowByte & 0x40) == 0x40) {
                    //心率测量中
                    Logger.e("protocoDataF3 心率测量中");
                    bodyBaseModel.isHeartRating = true;
                    if (dataChangeListener != null) {
//                        dataChangeListener.monitorLockData(bodyBaseModel, deviceModel);
                    }
                    return;
                } else {
                    //心率未测量
                    Logger.e("protocoDataF3 心率未测量");
                    bodyBaseModel.isHeartRating = false;
                }
                if ((measuStateHighByte & 0x80) == 0x80) {
                    //测量完成
                    Logger.e("protocoDataF3 测量完成");
                    if (dataChangeListener != null) {
//                        dataChangeListener.monitorLockData(bodyBaseModel, deviceModel);
                        bodyBaseModel.resetBofyFat();
                    }
                    return;
                } else {
                    //测量未完成
                    Logger.e("protocoDataF3 测量未完成");
                }
                if (reciveData.length() > 14) {
                    String impedancePosition = reciveData.substring(16, 18);
                    //阻抗测量部位：
                    //0x00：双脚
                    //0x01：双手
                    //0x02：左半身
                    //0x03：右半身
                    //0x04：左手右脚
                    //0x05：右手左脚
                    //0x06：躯干
                    //明文阻抗值类型
                    String impedanceType = reciveData.substring(18, 20);
                    //明文阻抗值
                    int impedance = ByteUtil.hexToTen(reciveData.substring(20, 22) + reciveData.substring(20, 22));
                    //明文阻抗值类型[单频阻抗模式下此字段为0]
                    String impedanceType2 = reciveData.substring(22, 24);
                    //明文阻抗值[在单频阻抗模式下此字段为0]
                    int impedance2 = ByteUtil.hexToTen(reciveData.substring(26, 28) + reciveData.substring(24, 26));
                }
                break;
            case "02"://密文阻抗
                //指定部位阻抗值
                byte[] dataByteArray = ByteUtil.stringToBytes(reciveData);
                Logger.d("protocoDataF3 密文阻抗");
                //阻抗测量频率 0x00：20KHz&100KHz  0x01：50KHz
                byte impedanceMeasureFrequencyByte = dataByteArray[3];
                if (impedanceMeasureFrequencyByte == 0x01) {
                    //四电极设备 50kHz密文阻抗[4byte]
                    int impedance = TorreHelper.getImpedance(reciveData.substring(8, 16));
                    bodyBaseModel.impedance = impedance;
                    Logger.d("protocoDataF3 四电极 impedance:" + impedance);
                } else {
                    //八电极设备 20kHz密文阻抗[4byte]  100kHz密文阻抗[4byte]
                    //指定部位阻抗值 数据起始为最低位到高位：
                    //如：0x03 (bit1 | bit0) ， 数据组成为：右手20KHz密文阻抗 + 右手100KHz密文阻抗 + 左手20KHz密文阻抗 + 左手100KHz密文阻抗
                    String impedanceData = reciveData.substring(8);
                    byte bodyPartsByte = dataByteArray[2];
                    int len = 8;
                    if ((bodyPartsByte & 0x01) == 0x01) {
                        //右手
                        bodyBaseModel.z20KhzRightArmEnCode = TorreHelper.getImpedance(impedanceData.substring(0, len));
                        bodyBaseModel.z100KhzRightArmEnCode = TorreHelper.getImpedance(impedanceData.substring(len, 2 * len));
                    }
                    if ((bodyPartsByte & 0x02) == 0x02) {
                        //左手
                        bodyBaseModel.z20KhzLeftArmEnCode = TorreHelper.getImpedance(impedanceData.substring(2 * len, 3 * len));
                        bodyBaseModel.z100KhzLeftArmEnCode = TorreHelper.getImpedance(impedanceData.substring(3 * len, 4 * len));
                    }
                    if ((bodyPartsByte & 0x04) == 0x04) {
                        //躯干
                        bodyBaseModel.z20KhzTrunkEnCode = TorreHelper.getImpedance(impedanceData.substring(0, len));
                        bodyBaseModel.z100KhzTrunkEnCode = TorreHelper.getImpedance(impedanceData.substring(len, 2 * len));
                    }
                    if ((bodyPartsByte & 0x08) == 0x08) {
                        //右脚
                        bodyBaseModel.z20KhzRightLegEnCode = TorreHelper.getImpedance(impedanceData.substring(2 * len, 3 * len));
                        bodyBaseModel.z100KhzRightLegEnCode = TorreHelper.getImpedance(impedanceData.substring(3 * len, 4 * len));
                    }
                    if ((bodyPartsByte & 0x10) == 0x10) {
                        //左脚
                        bodyBaseModel.z20KhzLeftLegEnCode = TorreHelper.getImpedance(impedanceData.substring(0, len));
                        bodyBaseModel.z100KhzLeftLegEnCode = TorreHelper.getImpedance(impedanceData.substring(len, 2 * len));
                    }
                    Logger.d("protocoDataF3 八电极 bodyBaseModel:" + bodyBaseModel.toString());
                }
                break;
            case "03"://明文阻抗
                //指定部位阻抗值
                byte plainTextBodyPartsByte = ByteUtil.stringToBytes(reciveData.substring(4, 6))[0];
                //byte[2] = 0x20，明文阻抗则包含部位双脚明文阻抗  byte[2] = 0x1F，明文阻抗则包含节段右手明文阻抗+节段左手明文阻抗+节段躯干明文阻抗+节段右脚明文阻抗+节段左脚明文阻抗
                byte plainTextImpedanceTypeByte = ByteUtil.stringToBytes(reciveData.substring(8, 10))[0];

                if (plainTextBodyPartsByte == 0x20) {
                    //双脚
                    //明文阻抗
                    int plainTextImpedance = ByteUtil.hexToTen(reciveData.substring(10, 12) + reciveData.substring(8, 10));

                } else {
                    //其他部位
                }
                break;
            case "04":
                Logger.d("Gsensor触发状态");
                break;
            case "05":
                Logger.d("称端有新的历史数据");
                if (dataChangeListener != null) {
                    //由于在拿到稳定重量后，秤端还有很多后续操作，为了让App的展示与秤一致，所以在拿到稳定重量也同步给过程回调
                    dataChangeListener.onHistoryDataChange();
                }
                break;
            case "06":
                Logger.d("设备端用户选择状态上报");
                break;
            default: {
            }
        }
    }

    public void setInterface(ProtocalFilterImpl protocalFilter) {
        deviceSetInfoInterface = protocalFilter.getDeviceSetInfoInterface();
        userInfoInterface = protocalFilter.getUserInfoInterface();
        torreConfigWifiInterface = protocalFilter.getTorreConfigWifiInterface();
    }

    public void setTorreDeviceLogInterface(String logFilePath, PPDeviceLogInterface deviceLogInterface) {
        this.logFilePath = logFilePath;
        this.torreDeviceLogInterface = deviceLogInterface;
    }

    public void setTorreOTAStateListener(OnOTAStateListener otaStateListener) {
        this.otaStateListener = otaStateListener;
    }

    public void setTorreDFUListener(OnDFUStateListener dfuListener) {
        this.dfuListener = dfuListener;
    }

    public void setClearDataInterface(PPClearDataInterface clearDataInterface) {
        this.clearDataInterface = clearDataInterface;
    }

    public void setTorreDeviceModeChangeInterface(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        this.modeChangeInterface = modeChangeInterface;
    }

    public void setDeviceSetInfoInterface(PPDeviceSetInfoInterface deviceSetInfoInterface) {
        this.deviceSetInfoInterface = deviceSetInfoInterface;
    }

    public void setHistoryDataInterface(PPHistoryDataInterface historyDataInterface) {
        this.historyDataInterface = historyDataInterface;
    }

    public void setConfigWifiInterface(PPTorreConfigWifiInterface torreConfigWifiInterface) {
        this.torreConfigWifiInterface = torreConfigWifiInterface;
    }

    public void setUserInfoInterface(PPUserInfoInterface userInfoInterface) {
        this.userInfoInterface = userInfoInterface;
    }

    public void registDataChangeListener(PPDataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    public void unRegistDataChangeListener() {
        this.dataChangeListener = null;
    }

    public void setBleStateInterface(PPBleStateInterface bleStateInterface) {
        this.bleStateInterface = bleStateInterface;
    }

}
