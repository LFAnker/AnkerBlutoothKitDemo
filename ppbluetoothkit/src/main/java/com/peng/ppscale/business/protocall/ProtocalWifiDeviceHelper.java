package com.peng.ppscale.business.protocall;

import android.os.Handler;
import android.os.Looper;

import com.peng.ppscale.business.ble.configWifi.PPConfigWifiAppleStateMenu;
import com.peng.ppscale.business.ble.configWifi.PPConfigWifiInfoInterface;
import com.peng.ppscale.business.ble.listener.BleDataStateInterface;
import com.peng.ppscale.business.ble.listener.PPDeviceSetInfoInterface;
import com.peng.ppscale.business.ble.listener.ProtocalFilterImpl;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.vo.PPDeviceModel;

public class ProtocalWifiDeviceHelper {

    private PPConfigWifiInfoInterface configWifiInfoInterface;
    private BleDataStateInterface bleDataStateInterface;
    private PPDeviceSetInfoInterface resetDeviceInterface;

    private String ssid = "";
    private String password = "";

    private static volatile ProtocalWifiDeviceHelper instance = null;

    private final Handler handler;

    private ProtocalWifiDeviceHelper() {
        handler = new Handler(Looper.getMainLooper());
    }

    public static ProtocalWifiDeviceHelper getInstance() {
        if (instance == null) {
            synchronized (ProtocalWifiDeviceHelper.class) {
                if (instance == null) {
                    instance = new ProtocalWifiDeviceHelper();
                }
            }
        }
        return instance;
    }

    public void protocoConfigWifiFilter(byte[] value, final PPDeviceModel deviceModel) {
        if (deviceModel != null) {
            String reciveData = ByteUtil.byteToString(value);
            if (reciveData.startsWith("06")) {
                if (bleDataStateInterface != null) {
                    bleDataStateInterface.stopRetryConfig();
                }
                String snStr = reciveData.substring(2);
                final String sn = ByteUtil.hexStringToString(snStr);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (configWifiInfoInterface != null) {
//                            configWifiInfoInterface.monitorConfigSn(sn, deviceModel);
//                        }
//                    }
//                });
            } else if (reciveData.startsWith("0A")) {
                String snStr = reciveData.substring(8, reciveData.length() - 2);
                String totalStr = reciveData.substring(2, 4);
                String numStr = reciveData.substring(4, 6);//当前第几包
                int num = ByteUtil.hexToTen(numStr) + 1;
                int total = ByteUtil.hexToTen(totalStr);
                if (num <= 1) {
                    ssid = "";
                }
                if (!snStr.isEmpty()) {
                    ssid += ByteUtil.hexStringToString(snStr);
                }
                if (total == num && configWifiInfoInterface != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            configWifiInfoInterface.monitorConfigSsid(ssid, deviceModel);
                        }
                    });
                }
            } else if (reciveData.startsWith("0B")) {
                String snStr = reciveData.substring(8, reciveData.length() - 2);
                String totalStr = reciveData.substring(2, 4);
                String numStr = reciveData.substring(4, 6);//当前第几包
                int num = ByteUtil.hexToTen(numStr) + 1;
                int total = ByteUtil.hexToTen(totalStr);
                if (num <= 1) {
                    password = "";
                }
                password += ByteUtil.hexStringToString(snStr);
                if (total == num && configWifiInfoInterface != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            configWifiInfoInterface.monitorConfigPassword(password, deviceModel);
                        }
                    });
                }
            } else if (reciveData.startsWith("0C")) {
                String wifiResultHexStr = reciveData.substring(reciveData.length() - 2);
                int num = ByteUtil.hexToTen(wifiResultHexStr);
                if (configWifiInfoInterface != null) {
                    if (num >= 10) {
                        num = num - 10;
                    }
                    PPConfigWifiAppleStateMenu stateMenu;
                    switch (num) {
                        case 1:
                            stateMenu = PPConfigWifiAppleStateMenu.CONFIG_STATE_LOW_BATTERY_LEVEL;
                            break;
                        case 3:
                            stateMenu = PPConfigWifiAppleStateMenu.CONFIG_STATE_REGIST_FAIL;
                            break;
                        case 4:
                            stateMenu = PPConfigWifiAppleStateMenu.CONFIG_STATE_GET_CONFIG_FAIL;
                            break;
                        case 5:
                            stateMenu = PPConfigWifiAppleStateMenu.CONFIG_STATE_ROUTER_FAIL;
                            break;
                        case 6:
                            stateMenu = PPConfigWifiAppleStateMenu.CONFIG_STATE_PASSWORD_ERR;
                            break;
                        default:
                            stateMenu = PPConfigWifiAppleStateMenu.CONFIG_STATE_OTHER_FAIL;
                            break;
                    }
//                    configWifiInfoInterface.monitorConfigFail(stateMenu);
                }
            } else if (reciveData.startsWith("F501")) {//查询Wifi参数失败时回调
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (configWifiInfoInterface != null) {
                            configWifiInfoInterface.monitorConfigSsid("", deviceModel);
                        }
                    }
                });
            } else if (reciveData.startsWith("F700")) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (configWifiInfoInterface != null) {
                            configWifiInfoInterface.monitorModifyServerIpSuccess();
                        }
                    }
                });
            } else if (reciveData.startsWith("F800")) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (configWifiInfoInterface != null) {
                            configWifiInfoInterface.monitorModifyServerDomainSuccess();
                        }
                    }
                });
            } else if (reciveData.startsWith("F900")) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (resetDeviceInterface != null) {
                            resetDeviceInterface.monitorResetStateSuccess();
                        }
                    }
                });
            } else {
                ProtocalFilterManager.get().wifiBodyDataAnalyticalData(reciveData, deviceModel);
            }
        }

    }

    public void setInterface(ProtocalFilterImpl protocalFilter) {
        this.configWifiInfoInterface = protocalFilter.getConfigWifiInfoInterface();
        this.bleDataStateInterface = protocalFilter.getBleDataStateInterface();
        this.resetDeviceInterface = protocalFilter.getDeviceSetInfoInterface();
    }

    public void setConfigWifiInfoInterface(PPConfigWifiInfoInterface configWifiInfoInterface) {
        this.configWifiInfoInterface = configWifiInfoInterface;
    }


}
