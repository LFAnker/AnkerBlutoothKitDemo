package com.peng.ppscale.business.protocall;

import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.peng.ppscale.business.ble.bmdj.PPBMDJConnectInterface;
import com.peng.ppscale.business.ble.bmdj.PPBMDJDataInterface;
import com.peng.ppscale.business.ble.bmdj.PPBMDJStatesInterface;
import com.peng.ppscale.business.ble.listener.ProtocalFilterImpl;
import com.peng.ppscale.util.ByteUtil;

public class ProtocalBMDJDeviceHelper {

    private PPBMDJDataInterface bmdjDataInterface;
    private PPBMDJStatesInterface bmdjStatesInterface;
    private PPBMDJConnectInterface bmdjConnectInterface;

    private static volatile ProtocalBMDJDeviceHelper instance = null;

    private ProtocalBMDJDeviceHelper() {
    }

    public static ProtocalBMDJDeviceHelper getInstance() {
        if (instance == null) {
            synchronized (ProtocalBMDJDeviceHelper.class) {
                if (instance == null) {
                    instance = new ProtocalBMDJDeviceHelper();
                }
            }
        }
        return instance;
    }

    public void bmScaleStatusProtocol(final String reciveData) {
        BluetoothLog.v(reciveData);
        if (reciveData.equals("10060F0001")) {
            /*是否进入了闭目单脚模式*/
            if (bmdjConnectInterface != null) {
                bmdjConnectInterface.monitorBMDJConnectSuccess();
            }
        } else if (reciveData.equals("10060F0003")) {
            if (bmdjConnectInterface != null) {
                bmdjConnectInterface.monitorBMDJConnectFail();
            }
        } else if (reciveData.equals("1006110001")) {
            /*是否退出了闭目单脚模式*/
            if (bmdjStatesInterface != null) {
                bmdjStatesInterface.monitorBMDJExitSuccess();
            }
        } else if (reciveData.equals("1006110003")) {
            if (bmdjStatesInterface != null) {
                bmdjStatesInterface.monitorBMDJExittFail();
            }
        } else if (reciveData.equals("10060F010100")) {
            /*BM Scale秤开始计时*/
            bmdjStatesInterface.startTiming();
        } else {
            /*BM Scale秤的时间数据*/
            bmScaleProtocol(reciveData);
        }
    }

    /*BM Scale秤的时间数据*/
    private void bmScaleProtocol(final String reciveData) {

        if (reciveData.length() < 12) return;

        int time = 0;
        String timeHigh = reciveData.substring(8, 10);
        String timeLow = reciveData.substring(10, 12);
        time = ByteUtil.hexToTen(timeLow + timeHigh);
        final int finalTime = time;
        if (reciveData.startsWith("10060F01")) {
            if (bmdjDataInterface != null) {
                bmdjDataInterface.monitorBMDJStandTime(finalTime);
            }
        } else if (reciveData.startsWith("10060F02")) {
            if (bmdjDataInterface != null) {
                bmdjDataInterface.monitorBMDJMeasureEnd(finalTime);
            }
        }

    }

    public void setInterface(ProtocalFilterImpl protocalFilter) {
        this.bmdjDataInterface = protocalFilter.getBmdjDataInterface();
        this.bmdjStatesInterface = protocalFilter.getBmdjStatesInterface();
        this.bmdjConnectInterface = protocalFilter.getBmdjConnectInterface();
    }

}
