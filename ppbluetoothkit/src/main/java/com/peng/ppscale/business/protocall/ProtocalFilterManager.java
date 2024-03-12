package com.peng.ppscale.business.protocall;

import com.peng.ppscale.business.ble.listener.BleDataStateInterface;
import com.peng.ppscale.business.ble.listener.PPDataChangeListener;
import com.peng.ppscale.business.ble.listener.PPHistoryDataInterface;
import com.peng.ppscale.business.ble.listener.ProtocalFilterImpl;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;
import com.peng.ppscale.vo.PPUserModel;

public class ProtocalFilterManager {
    private static volatile ProtocalFilterManager instance = null;
    private final ProtocalDelegate protocalDelegate;

    private ProtocalFilterManager() {
        protocalDelegate = new ProtocalDelegate();
    }

    public static ProtocalFilterManager get() {
        if (instance == null) {
            synchronized (ProtocalFilterManager.class) {
                if (instance == null) {
                    instance = new ProtocalFilterManager();
                }
            }
        }
        return instance;
    }

    /**
     * 解析数据
     *
     * @param value       11字节数据
     * @param deviceModel
     */
    public void analyticalData(String value, PPDeviceModel deviceModel) {
        protocalDelegate.protocoFilter(value, deviceModel);
    }

    public void wifiBodyDataAnalyticalData(String value, PPDeviceModel deviceModel) {
        protocalDelegate.wifiBodyDataAnalyticalData(value, deviceModel);
    }

    public void analysisDataCalcuteInScale(String value, PPUserModel userModel, PPDeviceModel deviceModel) {
        protocalDelegate.analysisDataCalcuteInScale(value, userModel, deviceModel);
    }

    public void setSerialNumber(PPDeviceModel deviceModel) {
        protocalDelegate.setSerialNumber(deviceModel);
    }

    public void setModelNumber(PPDeviceModel deviceModel) {
        protocalDelegate.setModelNumber(deviceModel);
    }

    public void analyticalBMDJData(byte[] value) {
        protocalDelegate.protocoBMDJFilter(value);
    }

    public void setHistoryDataInterface(PPHistoryDataInterface historyDataInterface) {
        protocalDelegate.setHistoryDataInterface(historyDataInterface);
    }

    public void setDataChangeListener(PPDataChangeListener historyDataInterface) {
        protocalDelegate.setDataChangeListener(historyDataInterface);
    }

    /**
     * @param data        11字节广播数据
     * @param deviceModel
     */
    public void analysiBroadcastData(String data, PPDeviceModel deviceModel) {
        if (data.toLowerCase().startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CA)) {
            analyticalData(data, deviceModel);
        } else {
            if (validDataAndAnalyticalData(data)) {
                analyticalData(data, deviceModel);
            }
        }
    }

    /**
     * 校验码确认
     *
     * @param validData
     * @return
     */
    public boolean validDataAndAnalyticalData(String validData) {
        if (validData.length() >= 22) {
            if (validData.startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CE.toUpperCase()) || validData.startsWith(PPScaleDefine.PPScaleType.BLE_SCALE_TYPE_CF.toUpperCase())) {
                String xorString = validData.substring(20, 22);
                String checkString = validData.substring(0, 20);
                if (ByteUtil.isXorValue(checkString, xorString)) {
//                            String finalData = data.substring(indexOf, indexOf + 22);
                    Logger.d("analysisSearchfinalData ------- " + validData);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMeetSign(byte[] scanRecord) {
        String byteToStr = ByteUtil.byteToString(scanRecord);
        byteToStr = byteToStr.substring(0, byteToStr.length() - 2);
        byte[] bytes = ByteUtil.stringToBytes(byteToStr);
        byte xor = ByteUtil.getXorValue(bytes);
        if (xor == scanRecord[scanRecord.length - 1]) {
            return true;
        }
        return false;
    }

    public void setProtocalFilter(ProtocalFilterImpl protocalFilter, PPUserModel userModel) {
        protocalDelegate.bindProtocalFiter(protocalFilter, userModel);
    }

    public void setBleDataStateInterface(BleDataStateInterface bleDataStateInterface) {
        protocalDelegate.setBleDataStateInterface(bleDataStateInterface);

    }

    /**
     * 清除历史
     */
    public void resetCache() {
        protocalDelegate.resetHistory();
    }

}
