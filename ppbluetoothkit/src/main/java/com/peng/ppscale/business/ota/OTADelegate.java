package com.peng.ppscale.business.ota;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;
import com.peng.ppscale.business.ble.listener.PPBleSendResultCallBack;
import com.peng.ppscale.business.ble.send.BleSendManager;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleSendState;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OTADelegate {

    static final int OTA_BLOCK_SIZE = 16;
    private int mBlockCount;
    private byte[] promoteDatas;
    UUID serverUUID;
    UUID characterUUID1;
    UUID characterUUID2;

    UUID internalCodeModeServerUUID;
    UUID internalCodeModeCharacterUUID;

    BluetoothClient mBleClient;
    PPDeviceModel currentDevice;
    boolean isOTA = false;
    boolean isStop = true;
    OnOTAStateListener otaStateListener;
    private int blockIndex = 0;

    private final ExecutorService executorService;
    private BleSendManager bleSendManager;
    private BleSendManager internalCodeModeSendManager;
    boolean isUpgradeSuccess = false;
    boolean updateFail = false;//升级失败标识

    public OTADelegate() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void addOnOtaStateListener(OnOTAStateListener otaStateListener) {
        this.otaStateListener = otaStateListener;
        if (otaStateListener != null) {
//            this.isOTA = otaStateListener.isOTA();
        }
    }

    public void onResponseOTA1(UUID serverUUID, UUID characterUUID1) {
        if (currentDevice == null) {
            Logger.e("onResponseOTA1 currentDevice is null");
            return;
        }
        if (mBleClient == null) {
            Logger.e("onResponseOTA1 mBleClient is null");
            return;
        }
        if (!isOTA) {
            Logger.e("onResponseOTA1 isOTA is " + isOTA);
            return;
        }
        this.serverUUID = serverUUID;
        this.characterUUID1 = characterUUID1;
        if (bleSendManager == null) {
            bleSendManager = new BleSendManager.Builder()
                    .setDevice(currentDevice)
                    .setService(serverUUID)
                    .setCharacter(characterUUID1)
                    .setBleClient(mBleClient)
                    .build();
        }
        enableNotify1();
    }

    public void onResponseOTA2(UUID serverUUID, UUID characterUUID2) {
        if (currentDevice == null) {
            Logger.e("onResponseOTA2 currentDevice is null");
            return;
        }
        if (mBleClient == null) {
            Logger.e("onResponseOTA2 mBleClient is null");
            return;
        }
        if (!isOTA) {
            Logger.e("onResponseOTA2 isOTA is " + isOTA);
            return;
        }
        this.serverUUID = serverUUID;
        this.characterUUID2 = characterUUID2;
        enableNotify2();
    }

    public void bindBleClient(BluetoothClient bluetoothClient) {
        this.mBleClient = bluetoothClient;
        if (bleSendManager != null) {
            bleSendManager.bindBleClient(this.mBleClient);
        }
        if (internalCodeModeSendManager != null) {
            internalCodeModeSendManager.bindBleClient(this.mBleClient);
        }
    }

    public void bindDevice(PPDeviceModel currentDevice) {
        this.currentDevice = currentDevice;
    }

    public void setOTAStatus(boolean isOTA) {
        this.isOTA = isOTA;
    }

    /**
     * 进入内码模式
     */
    public void startInternalCodeMode() {
        if (internalCodeModeSendManager == null) {
            internalCodeModeSendManager = new BleSendManager.Builder()
                    .setDevice(currentDevice)
                    .setService(internalCodeModeServerUUID)
                    .setCharacter(internalCodeModeCharacterUUID)
                    .setBleClient(mBleClient)
                    .build();
        }
        //FD340000000000000000C9 进入内码模式，秤不会关机
        //FD350000000000000000C8 熄屏进入休眠
        byte[] innerCodeMode = ByteUtils.stringToBytes("FD340000000000000000C9");
        internalCodeModeSendManager.sendData(innerCodeMode,new PPBleSendResultCallBack() {

            @Override
            public void onResult(@NotNull PPScaleSendState sendState) {
                if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                    isUpgradeSuccess = false;
                    if (otaStateListener != null) {
                        otaStateListener.onReadyToUpdate();
                    }
                } else {
                    Logger.e("ota 内码模式启动失败");
                }
            }
        });
    }

    public void startUpgradeRequest(final String binPath, final Context context) {
        if (!isOTA) return;
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    isStop = false;
                    InputStream open = context.getAssets().open(binPath);
                    promoteDatas = OtaStreamUtils.convertStreamToByte(open);
                    int count = promoteDatas.length / OTA_BLOCK_SIZE;
                    mBlockCount = promoteDatas.length % OTA_BLOCK_SIZE == 0 ? count : count + 1;
                    if (promoteDatas != null && promoteDatas.length > 16) {
                        byte[] datas = new byte[16];
                        System.arraycopy(promoteDatas, 0, datas, 0, datas.length);
                        //00006AF5FD352000C02442424242FFFF0A00
                        Logger.d("startUpgradeRequest byte  = " + ByteUtil.byteToString(datas));

                        if (serverUUID == null) {
                            Logger.e("startUpgradeRequest  serverUUID is null");
                        }
                        if (characterUUID1 == null) {
                            Logger.e("startUpgradeRequest  characterUUID1 is null");
                        }
                        if (bleSendManager == null) {
                            bleSendManager = new BleSendManager.Builder()
                                    .setDevice(currentDevice)
                                    .setService(serverUUID)
                                    .setCharacter(characterUUID1)
                                    .setBleClient(mBleClient)
                                    .build();
                        } else {
                            bleSendManager.setServiceUUID(serverUUID);
                            bleSendManager.setCharacter(characterUUID1);
                        }
                        bleSendManager.sendData(datas,new PPBleSendResultCallBack() {
                            @Override
                            public void onResult(PPScaleSendState sendState) {
                                if (sendState != PPScaleSendState.PP_SEND_SUCCESS) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (otaStateListener != null) {
                                                otaStateListener.onUpdateFail(0);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void exitUpgrade() {
        isStop = true;
        isOTA = false;

        if (isUpgradeSuccess) {
            //FD350000000000000000C8 熄屏进入休眠
            byte[] normalMode = ByteUtils.stringToBytes("FD350000000000000000C8");
            if (bleSendManager != null) {
                bleSendManager.setServiceUUID(internalCodeModeServerUUID);
                bleSendManager.setCharacter(internalCodeModeCharacterUUID);
                bleSendManager.sendData(normalMode, new PPBleSendResultCallBack() {
                    @Override
                    public void onResult(PPScaleSendState sendState) {
                        if (sendState == PPScaleSendState.PP_SEND_SUCCESS) {
                            updateFail = true;
                        } else {
                            updateFail = false;
                        }
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (otaStateListener != null) {
                            otaStateListener.onUpdateEnd();
                            otaStateListener = null;
                        }
                    }
                }, 300);
            }
        }
        isUpgradeSuccess = false;
    }

    private void startUpdate() {
        if (!isOTA) return;
        if (promoteDatas != null) {
            if (otaStateListener != null) {
                otaStateListener.onStartUpdate();
            }
            if (bleSendManager != null) {
                blockIndex = 0;
                bleSendManager.setServiceUUID(serverUUID);
                bleSendManager.setCharacter(characterUUID2);
                startWriteOTABody();
            }
        }
    }

    private void startWriteOTABody() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (bleSendManager != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (otaStateListener != null) {
                                int progress = (int) (blockIndex / (mBlockCount * 1.0f) * 100.0f);
                                otaStateListener.onUpdateProgress(progress);
                            }
                        }
                    });

                    if (blockIndex < mBlockCount) {
                        byte[] bodyData = new byte[18];
//                            if (blockIndex * 16 < promoteDatas.length) {
                        int len = (blockIndex * OTA_BLOCK_SIZE + OTA_BLOCK_SIZE) > promoteDatas.length ? (promoteDatas.length - blockIndex * OTA_BLOCK_SIZE) : OTA_BLOCK_SIZE;
                        bodyData[0] = OtaConvertUtil.loUint16((short) blockIndex);
                        bodyData[1] = OtaConvertUtil.hiUint16((short) blockIndex);
                        System.arraycopy(promoteDatas, OTA_BLOCK_SIZE * blockIndex, bodyData, 2, len);
                        blockIndex++;
                        Logger.d("startWriteOTABody byte  = " + ByteUtil.byteToString(bodyData));
                        bleSendManager.sendData(bodyData, null);
                        // Log.i("OTA", "数据写入 = " + blockIndex + "  " + OtaConvertUtil.bytesToHexString(bodyData) + "  " + b);
                        try {
                            Thread.sleep(15);
                            if (!isStop) {
                                startWriteOTABody();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        isStop = true;
                        isUpgradeSuccess = true;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (otaStateListener != null) {
                                    otaStateListener.onUpdateSucess();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void enableNotify1() {
        mBleClient.notify(currentDevice.getDeviceMac(), serverUUID, characterUUID1, new BleNotifyResponse() {

            @Override
            public void onNotify(UUID service, UUID character, final byte[] value) {
                Logger.d("ota enableNotify1  mac = " + currentDevice.getDeviceMac() + " value = " + ByteUtil.byteToString(value));
                String firmwareVersionInfo = ByteUtil.byteToString(value);
                if (firmwareVersionInfo != null && !firmwareVersionInfo.isEmpty()) {
                    isStop = true;
                    if (otaStateListener != null) {
                        otaStateListener.onUpdateFail(0);
                    }
                }
            }

            @Override
            public void onResponse(int code) {
                Logger.d("reciveDataCode--------- " + code);
            }
        });
    }

    private void enableNotify2() {
        if (serverUUID != null && characterUUID2 != null) {
            mBleClient.notify(currentDevice.getDeviceMac(), serverUUID, characterUUID2, new BleNotifyResponse() {

                @Override
                public void onNotify(UUID service, UUID character, final byte[] value) {
                    Logger.d("ota enableNotify2  mac = " + currentDevice.getDeviceMac() + " value = " + ByteUtil.byteToString(value));
                    String successInfo = ByteUtil.byteToString(value);
                    if (successInfo != null && !successInfo.isEmpty()) {
                        if (successInfo.equals("0000")) {
                            startUpdate();
                        }
                    } else {

                    }
                }

                @Override
                public void onResponse(int code) {
                    Logger.d("reciveDataCode--------- " + code);
                }
            });
        }
    }

    /**
     * 记录内码模式uuid
     *
     * @param internalCodeModeServerUUID
     * @param internalCodeModeCharacterUUID
     */
    public void targetResponseOTAState(UUID internalCodeModeServerUUID, UUID
            internalCodeModeCharacterUUID) {
        this.internalCodeModeServerUUID = internalCodeModeServerUUID;
        this.internalCodeModeCharacterUUID = internalCodeModeCharacterUUID;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUpgradeSuccess) {
                    exitUpgrade();
                } else {
                    //进入内码
                    startInternalCodeMode();
                }
            }
        }, 200);
    }
}
