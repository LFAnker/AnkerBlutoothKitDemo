package com.peng.ppscale.search;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.peng.ppscale.PPBlutoothKit;
import com.peng.ppscale.business.ble.listener.PPBleStateInterface;
import com.peng.ppscale.business.ble.listener.PPSearchDeviceInfoInterface;
import com.peng.ppscale.business.device.DeviceManager;
import com.peng.ppscale.business.protocall.ProtocalFilterManager;
import com.peng.ppscale.business.state.PPBleSwitchState;
import com.peng.ppscale.business.state.PPBleWorkState;
import com.peng.ppscale.data.PPBodyDetailModel;
import com.peng.ppscale.util.BleUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BleSearchDelegate implements SearchResponse {

    public static final int MSG_SEARCH_DEVICE = 0x01;
    public static int DURATION_TIME = 300000;
    private final BluetoothClient bluetoothClient;
    private boolean searchStatus = false;
    private PPBleStateInterface bleStateInterface;  //蓝牙相关状态监听
    private final ExecutorService executorService;

    private static final String TAG = BleSearchDelegate.class.getSimpleName();

    private static volatile BleSearchDelegate instance = null;
    private PPSearchDeviceInfoInterface searchDeviceInfoInterface;

    SearchResult mSearchResult;

    PPDeviceModel deviceModel;
    String advDataStr = "";

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SEARCH_DEVICE: {
//                    PPDeviceModel deviceModel = (PPDeviceModel) msg.obj;
                    if (searchDeviceInfoInterface != null && deviceModel != null) {
                        searchDeviceInfoInterface.onSearchDevice(deviceModel, advDataStr);
                    }
                }
                break;
                default:
                    break;
            }
        }
    };

    public static BleSearchDelegate getInstance() {
        if (instance == null) {
            synchronized (BleSearchDelegate.class) {
                if (instance == null) {
                    instance = new BleSearchDelegate();
                }
            }
        }
        return instance;
    }

    public static BleSearchDelegate get() {
        return instance;
    }

    private BleSearchDelegate() {
//        filterManager = ProtocalFilterManager.get();
        executorService = Executors.newSingleThreadExecutor();
        bluetoothClient = PPBlutoothKit.INSTANCE.getBluetoothClient();
    }

    private SearchRequest getRequest(int DURATION_TIME) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return new SearchRequest.Builder()
                    .searchBluetoothLeDevice(1000, 1000)
                    .build();
        } else {
            return new SearchRequest.Builder()
                    .searchBluetoothLeDevice(DURATION_TIME)
                    .build();
        }
    }

    BluetoothStateListener bluetoothStateListener = new BluetoothStateListener() {

        @Override
        public void onBluetoothStateChanged(boolean b) {
            if (bleStateInterface != null) {
                bleStateInterface.monitorBluetoothSwitchState(b ? PPBleSwitchState.PPBleSwitchStateOn : PPBleSwitchState.PPBleSwitchStateOff);
            }
        }
    };

    public void startSearchBluetoothScale(PPSearchDeviceInfoInterface searchDeviceInfoInterface, PPBleStateInterface bleStateInterface) {
        if (isOpenBle()) {
            this.searchDeviceInfoInterface = searchDeviceInfoInterface;
            this.bleStateInterface = bleStateInterface;
            startSearch();
        } else {
            searchStatus = false;
            Logger.d(TAG + " ble is not open");
        }
    }

    @Override
    public void onSearchStarted() {
        Logger.d(TAG + " onSearchStarted");
        searchStatus = true;
        if (bleStateInterface != null) {
            bleStateInterface.monitorBluetoothWorkState(PPBleWorkState.PPBleWorkStateSearching, null);
        }
    }

    private void startSearch() {
        Logger.d(TAG + " startSearch");
//        filterManager.resetCache();
        searchStatus = true;
        bluetoothClient.search(getRequest(DURATION_TIME), BleSearchDelegate.this);
        bluetoothClient.unregisterBluetoothStateListener(bluetoothStateListener);
        bluetoothClient.registerBluetoothStateListener(bluetoothStateListener);
    }

    @Override
    public void onDeviceFounded(final SearchResult searchResult) {
        if (searchResult == null) {
            return;
        }
        this.mSearchResult = searchResult;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                onSearchDevice();
            }
        });
    }

    private void onSearchDevice() {
        SearchResult searchResult = mSearchResult;
        if (searchResult.scanRecord == null || searchResult.scanRecord.length <= 0) {
            Logger.e(TAG + " Broadcast data length error");
            return;
        }
        if (!searchStatus) {
            Logger.e(TAG + " Scanning stopped");
            return;
        }
        String deviceName = searchResult.getName();
        if (TextUtils.isEmpty(deviceName) || deviceName.equals("NULL")) {
            return;
        }
//        Logger.d("onSearchDevice deviceName:" + deviceName + " mac：" + searchResult.getAddress());
//        if (!DeviceManager.DeviceList.DeviceListAll.contains(deviceName)
//            && !DeviceManager.DeviceList.smartV3DeviceList.contains(deviceName) && !DeviceManager.DeviceList.torreList.contains(deviceName)) {
//            return;
//        }
//        PPDeviceModel deviceModel = BleSearchHelper.deviceTypeByCBAdvDataManufacturerData(searchResult);
//        PPDeviceModel deviceModel = DeviceFilterHelper.INSTANCE.getDeviceModel(searchResult);
        PPDeviceModel deviceModel = BleSearchHelper.deviceTypeByCBAdvDataManufacturerData(searchResult);
        if (deviceModel == null) {
            return;
        }
        if (deviceModel.deviceType == PPScaleDefine.PPDeviceType.PPDeviceTypeUnknow) {
            Logger.e(TAG + " deviceModel.deviceType is PPDeviceTypeUnknow");
            return;
        }
        if (searchDeviceInfoInterface != null && handler != null) {
            this.deviceModel = deviceModel;
            this.advDataStr = DeviceFilterHelper.INSTANCE.getAdvDataStr();
            handler.sendEmptyMessage(MSG_SEARCH_DEVICE);
        }
//        String outData = DeviceFilterHelper.INSTANCE.getAdvDataStr();
//        if (outData != null && outData.length() > 8) {
//            Logger.d(TAG + " outData = " + outData);
//            ProtocalFilterManager.get().analysiBroadcastData(outData, deviceModel);
//        }
    }

    @Override
    public void onSearchStopped() {
        Logger.d(TAG + " 扫描时间到");
        searchStatus = false;
        if (bleStateInterface != null) {
            bleStateInterface.monitorBluetoothWorkState(PPBleWorkState.PPBleWorkSearchTimeOut, null);
        }
    }

    @Override
    public void onSearchCanceled() {
        Logger.d(TAG + " 主动取消扫描");
        searchStatus = false;
        if (bleStateInterface != null) {
            bleStateInterface.monitorBluetoothWorkState(PPBleWorkState.PPBleStateSearchCanceled, null);
        }
    }

    @Override
    public void onSearchFail(int errorCode) {
        Logger.d(TAG + " onSearchFail errorCode:" + errorCode);
        searchStatus = false;
        if (bleStateInterface != null) {
            bleStateInterface.monitorBluetoothWorkState(PPBleWorkState.PPBleWorkSearchFail, null);
        }
    }

    public void stopSearch() {
        searchStatus = false;
        Logger.d("调用BleAPI 停止扫描");
        if (PPBlutoothKit.INSTANCE.getBluetoothClient() != null) {
            PPBlutoothKit.INSTANCE.getBluetoothClient().stopSearch();
        }
        if (handler != null) {
            handler.removeCallbacks(null);
        }
    }

    public boolean isSearching() {
        return searchStatus;
    }

    private boolean isOpenBle() {
        if (PPBlutoothKit.INSTANCE.getBluetoothClient() != null) {
            if (PPBlutoothKit.INSTANCE.getBluetoothClient().isBluetoothOpened()) {
                return true;
            } else {
//                PPBlutoothKit.INSTANCE.getBluetoothClient().openBluetooth();
            }
        }
        return false;
    }

}
