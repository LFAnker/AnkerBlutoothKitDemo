package com.peng.ppscale.business.torre;

import com.peng.ppscale.business.ble.listener.PPBleSendResultCallBack;
import com.peng.ppscale.business.ble.listener.PPDataChangeListener;
import com.peng.ppscale.business.ble.listener.PPDeviceLogInterface;
import com.peng.ppscale.business.ble.listener.PPDeviceSetInfoInterface;
import com.peng.ppscale.business.ble.listener.PPHistoryDataInterface;
import com.peng.ppscale.business.ble.listener.PPTorreDeviceModeChangeInterface;
import com.peng.ppscale.business.ble.listener.PPUserInfoInterface;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.business.ota.OnOTAStateListener;
import com.peng.ppscale.business.torre.listener.OnDFUStateListener;
import com.peng.ppscale.business.torre.listener.PPClearDataInterface;
import com.peng.ppscale.business.torre.listener.PPTorreConfigWifiInterface;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.vo.PPUserModel;

import org.jetbrains.annotations.NotNull;

public class TorreDeviceManager {

    private static volatile TorreDeviceManager instance = null;
    public static String deleteNormalMemberId = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
    public static String normalMemberId = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    public static String normalDeleteUId = "DE000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

    public static final String TouristUID = "0000000000000000000000000000000000000000000000000000000000000000";

    private TorreDeviceManager() {
    }

    @NotNull
    public static TorreDeviceManager getInstance() {
        if (instance == null) {
            synchronized (TorreDeviceManager.class) {
                if (instance == null) {
                    instance = new TorreDeviceManager();
                }
            }
        }
        return instance;
    }

    public void setLight(int light, PPDeviceSetInfoInterface deviceSetInfoInterface) {
        TorreDelegate.getInstance().setLight(light, deviceSetInfoInterface);
    }

    public void getLight(PPDeviceSetInfoInterface deviceSetInfoInterface) {
        TorreDelegate.getInstance().getLight(deviceSetInfoInterface);
    }

    /**
     * @param logFilePath             指定文件存储路径，必传例如：val fileFath = context.filesDir.absolutePath + "/Log/DeviceLog"
     * @param torreDeviceLogInterface
     */
    public void syncLog(String logFilePath, PPDeviceLogInterface torreDeviceLogInterface) {
        TorreDelegate.getInstance().syncLogStart(logFilePath, torreDeviceLogInterface);
    }

    public void syncTime(PPBleSendResultCallBack sendResultCallBack) {
        TorreDelegate.getInstance().syncTime(System.currentTimeMillis(), sendResultCallBack);
    }

    public void syncUnit(PPUnitType unitType, PPBleSendResultCallBack sendResultCallBack) {
        TorreDelegate.getInstance().syncUnit(unitType, sendResultCallBack);
    }

    public void resetDevice(PPDeviceSetInfoInterface deviceSetInfoInterface) {
        TorreDelegate.getInstance().resetDevice(deviceSetInfoInterface);
    }

    /**
     * 从设备特征值里面读取信息包括版本号，序列号，硬件版本号等
     */
    public void readDeviceInfoFromCharacter(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        TorreDelegate.getInstance().readDeviceInfoFromCharacter(modeChangeInterface);
    }

    public void readDeviceBattery(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        TorreDelegate.getInstance().readDeviceBattery(modeChangeInterface);
    }

//    /**
//     * 获取指定成员的历史数据
//     *
//     * @param userModel
//     */
//    public void syncHistory(PPUserModel userModel, PPHistoryDataInterface historyDataInterface) {
//        TorreDelegate.getInstance().syncHistoryStart(userModel, historyDataInterface);
//    }

    /**
     * 同步游客数据
     *
     * @param historyDataInterface
     */
    public void syncTouristHistory(PPHistoryDataInterface historyDataInterface) {
        PPUserModel userModel = new PPUserModel.Builder().build();
        userModel.userID = TouristUID;
        userModel.memberID = TouristUID;
        ProtocalTorreDeviceHelper.getInstance().setHistoryDataInterface(historyDataInterface);
        TorreDelegate.getInstance().syncUserHistoryStart(userModel);
    }

    /**
     * 根据Uid获取用户历史数据
     *
     * @param userModel
     */
    public void syncUserHistory(PPUserModel userModel, PPHistoryDataInterface historyDataInterface) {
        ProtocalTorreDeviceHelper.getInstance().setHistoryDataInterface(historyDataInterface);
        TorreDelegate.getInstance().syncUserHistoryStart(userModel);
    }

//    public void syncAllHistory() {
//        TorreDelegate.getInstance().syncAllHistoryStart();
//    }

    public void confirmCurrentUser(PPUserModel userModel, PPUserInfoInterface userInfoInterface) {
        TorreDelegate.getInstance().sendCurrentUserinfoStart(userModel, userInfoInterface);
    }

    public void syncUserInfo(PPUserModel userModel, PPUserInfoInterface userInfoInterface) {
        ProtocalTorreDeviceHelper.getInstance().setUserInfoInterface(userInfoInterface);
        TorreDelegate.getInstance().syncUserInfoStart(userModel);
    }

    public void deleteUserInfo(PPUserModel userModel, PPUserInfoInterface userInfoInterface) {
        ProtocalTorreDeviceHelper.getInstance().setUserInfoInterface(userInfoInterface);
        TorreDelegate.getInstance().deleteUserInfoStart(userModel);
    }

    public void deleteAllUserInfo(PPUserModel userModel, PPUserInfoInterface userInfoInterface) {
        ProtocalTorreDeviceHelper.getInstance().setUserInfoInterface(userInfoInterface);
        TorreDelegate.getInstance().deleteAllUserInfo(userModel);
    }

    public void getUserList(PPUserInfoInterface userInfoInterface) {
        TorreDelegate.getInstance().getUserList(userInfoInterface);
    }

    public void configWifi(String domainName, String ssid, String password, PPTorreConfigWifiInterface configWifiInterface) {
        TorreDelegate.getInstance().configWifi(domainName, ssid, password, configWifiInterface);
    }

    /**
     * @param isFullyDFUState  控制是否全量升级
     * @param dfuFilePath
     * @param dfuStateListener
     */
    public void startDFU(boolean isFullyDFUState, String dfuFilePath, OnDFUStateListener dfuStateListener) {
        ProtocalTorreDeviceHelper.getInstance().setTorreDFUListener(dfuStateListener);
        TorreDelegate.getInstance().startDFU(isFullyDFUState, dfuFilePath, dfuStateListener);
    }

    /**
     * 设备绑定状态
     *
     * @param type  1设置  2获取
     * @param state 0设备未绑定 1已绑定
     */
    public void deviceBindStatus(int type, int state, PPTorreDeviceModeChangeInterface modeChangeInterface) {
        ProtocalTorreDeviceHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
        TorreDelegate.getInstance().deviceBindStatus(type, state);
    }

    /**
     * 设置/获取演示模式状态
     *
     * @param type  1设置  2获取
     * @param state 0x00：关闭演示模式 0x01：打开演示模式
     */
    public void demoModeSwitch(int type, int state, PPTorreDeviceModeChangeInterface modeChangeInterface) {
        ProtocalTorreDeviceHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
        TorreDelegate.getInstance().demoModeSwitch(type, state);
    }

    public void stopDFU() {
        TorreDelegate.getInstance().stopDFU();
    }

    public boolean isDFU() {
        return TorreDelegate.getInstance().isDFU();
    }


    /**
     * 退出配网
     */
    public void exitConfigWifi() {
        TorreDelegate.getInstance().exitConfigWifi();
    }

    /**
     * 获取wifi列表
     */
    public void getWifiList(PPTorreConfigWifiInterface torreConfigWifiInterface) {
        TorreDelegate.getInstance().getWifiList(torreConfigWifiInterface);
    }

    /**
     * 清除用户信息
     */
    public void clearDeviceUserInfo(PPClearDataInterface clearDataInterface) {
        ProtocalTorreDeviceHelper.getInstance().setClearDataInterface(clearDataInterface);
        TorreDelegate.getInstance().clearDeviceUserInfo();
    }

    public void clearHistoryData(PPClearDataInterface clearDataInterface) {
        ProtocalTorreDeviceHelper.getInstance().setClearDataInterface(clearDataInterface);
        TorreDelegate.getInstance().clearHistoryData();
    }

    public void clearAllDeviceInfo(PPClearDataInterface clearDataInterface) {
        ProtocalTorreDeviceHelper.getInstance().setClearDataInterface(clearDataInterface);
        TorreDelegate.getInstance().clearAllDeviceInfo();
    }

    public void clearConfigWifiInfo(PPClearDataInterface clearDataInterface) {
        ProtocalTorreDeviceHelper.getInstance().setClearDataInterface(clearDataInterface);
        TorreDelegate.getInstance().clearConfigWifiInfo();
    }

    public void clearSettingInfo(PPClearDataInterface clearDataInterface) {
        ProtocalTorreDeviceHelper.getInstance().setClearDataInterface(clearDataInterface);
        TorreDelegate.getInstance().clearSettingInfo();
    }

    public void registDataChangeListener(PPDataChangeListener dataChangeListener) {
        ProtocalTorreDeviceHelper.getInstance().registDataChangeListener(dataChangeListener);
    }

    public void unRegistDataChangeListener() {
        ProtocalTorreDeviceHelper.getInstance().unRegistDataChangeListener();
    }

    /**
     * 启动测量
     */
    public void startMeasure(PPBleSendResultCallBack sendResultCallBack) {
        TorreDelegate.getInstance().startMeasure(sendResultCallBack);
    }

    public void stopMeasure(PPBleSendResultCallBack sendResultCallBack) {
        TorreDelegate.getInstance().stopMeasure(sendResultCallBack);
    }

    /**
     * 切换婴儿模式
     *
     * @param mode   00使能抱婴模式 01退出抱婴模式
     * @param step   0x00：第一步  0x01：第二步
     * @param weight 重量[单位10g]：当步骤为0x01[第一步]时重量发0 当步骤为0x02[第二步]时重量发第一步测得的重量
     */
    public void switchBaby(int mode, int step, double weight, PPBleSendResultCallBack sendResultCallBack) {
        TorreDelegate.getInstance().switchBaby(mode, step, weight, sendResultCallBack);
    }

    //获取阻抗开关状态
    public void getImpendanceState(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        TorreDelegate.getInstance().getImpendanceState(modeChangeInterface);
    }

    /**
     * 测值模式
     *
     * @param state 0打开 1关闭
     */
    public void controlImpendance(int state, PPTorreDeviceModeChangeInterface modeChangeInterface) {
        TorreDelegate.getInstance().controlImpendance(state, modeChangeInterface);
    }

    //心率0打开 1关闭
    public void controlHeartRate(int state, PPTorreDeviceModeChangeInterface modeChangeInterface) {
        TorreDelegate.getInstance().controlHeartRate(state, modeChangeInterface);
    }

    //获取心率状态
    public void getHeartRateState(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        TorreDelegate.getInstance().getHeartRateState(modeChangeInterface);
    }

    public void getUnit(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        ProtocalTorreDeviceHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
        TorreDelegate.getInstance().getUnit();
    }

    /**
     * 获取配网信息
     */
    public void getWifiSSID(PPTorreConfigWifiInterface configWifiInterface) {
        ProtocalTorreDeviceHelper.getInstance().setConfigWifiInterface(configWifiInterface);
        TorreDelegate.getInstance().getWifiSSID();
    }

    /**
     * 获取设备配网状态[App->设备]
     */
    public void getWifiState(PPTorreConfigWifiInterface configWifiInterface) {
        TorreDelegate.getInstance().getWifiState(configWifiInterface);
    }

    public void getWifiMac(PPTorreConfigWifiInterface configWifiInterface) {
        ProtocalTorreDeviceHelper.getInstance().setConfigWifiInterface(configWifiInterface);
        TorreDelegate.getInstance().getWifiMac();
    }

    /**
     * 正常用户升级
     */
    public void startUserOTA(OnOTAStateListener otaStateListener) {
        ProtocalTorreDeviceHelper.getInstance().setTorreOTAStateListener(otaStateListener);
        TorreDelegate.getInstance().startUserOTA();
    }

    /**
     * 调测用户升级
     */
    public void startTestOTA(OnOTAStateListener otaStateListener) {
        ProtocalTorreDeviceHelper.getInstance().setTorreOTAStateListener(otaStateListener);
        TorreDelegate.getInstance().startTestOTA();
    }

    /**
     * 启动本地OTA
     */
    public void startLocalOTA(OnOTAStateListener otaStateListener) {
        ProtocalTorreDeviceHelper.getInstance().setTorreOTAStateListener(otaStateListener);
        TorreDelegate.getInstance().startLocalOTA();
    }

    public void setPPTorreDeviceModeChangeInterface(PPTorreDeviceModeChangeInterface modeChangeInterface) {
        ProtocalTorreDeviceHelper.getInstance().setTorreDeviceModeChangeInterface(modeChangeInterface);
    }

    public void startKeepAlive() {
        Logger.d("startKeepAlive");
        LFTimerTask.getInstance().startDelay();
        LFTimerTask.getInstance().setRunCallBack(runCallBack);
    }

    public void stopKeepAlive() {
        Logger.d("stopKeepAlive");
        LFTimerTask.getInstance().stopDelay();
    }

    LFTimerTask.RunCallBack runCallBack = new LFTimerTask.RunCallBack() {
        @Override
        public void run() {
            Logger.v("RunCallBack keepAlive");
            TorreDelegate.getInstance().keepAlive();
        }
    };

}
