package com.peng.ppscale.search;

import android.text.TextUtils;

import com.inuker.bluetooth.library.search.SearchResult;
import com.peng.ppscale.business.device.DeviceManager;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;
import com.peng.ppscale.vo.PPScaleDefine.PPDeviceType;

import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.Volatile;

public class BleSearchHelper {
    public static String outData = "";
    public static String advDataStr = "";

    @Volatile
    public static Map<String, PPDeviceModel> deviceModelMap = new HashMap<>();

    /**
     * 根据不同的数据解析不同的设备
     *
     * @return
     */
    public static PPDeviceModel deviceTypeByCBAdvDataManufacturerData(SearchResult searchResult) {
        String brocastData = ByteUtil.byteToString(searchResult.scanRecord);
        String deviceName = searchResult.getName();
        String deviceMac = searchResult.getAddress();

        Logger.v("deviceName:" + deviceName + " deviceMac:" + deviceMac + " brocastData:" + brocastData);
        PPDeviceModel deviceModel = getPpDeviceModel(deviceMac, deviceName);
//        PPDeviceModel deviceModel = BleSearchHelper.getPpDeviceModel(deviceMac, deviceName);

        BleSearchBroadcastHelper.BroadcastData broadcastData = BleSearchBroadcastHelper.analysiBroadcastDataNormal(searchResult.scanRecord);
        byte[] advData = broadcastData.beacondata;

        outData = "";
        //0x64ff e106b3ea4cc4 cf0000410000000000018f
        if (DeviceManager.DeviceList.AnkerList.contains(deviceModel.getDeviceName())) {
            BleSearchTorreDeviceHelper.createAnkerDevice(advData, deviceModel);
        }
        return deviceModel;
    }

    private synchronized static PPDeviceModel getPpDeviceModel(String deviceMac, String deviceName) {
        PPDeviceModel deviceModel;
        if (deviceModelMap.containsKey(deviceMac)) {
            deviceModel = deviceModelMap.get(deviceMac);
        } else {
            deviceModel = new PPDeviceModel(deviceMac, deviceName);
            deviceModelMap.put(deviceMac, deviceModel);
        }
        return deviceModel;
    }


    /**
     * 秤端计算的秤
     *
     * @param data
     * @param deviceModel
     */
    private static void createOldCalcuteDevice(String data, PPDeviceModel deviceModel) {
        deviceModel.deviceCalcuteType = PPScaleDefine.PPDeviceCalcuteType.PPDeviceCalcuteTypeInScale;

    }


}
