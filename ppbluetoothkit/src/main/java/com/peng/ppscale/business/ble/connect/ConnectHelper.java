package com.peng.ppscale.business.ble.connect;

import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.peng.ppscale.business.torre.TorreCharacterFliterInterface;
import com.peng.ppscale.business.ota.OTAManager;
import com.peng.ppscale.business.v4.V4CharacterFliterInterface;
import com.peng.ppscale.device.BleGattProfileFliterInterface;
import com.peng.ppscale.util.Logger;

public class ConnectHelper {

    public static void monitorTargetResponse(BleGattProfile profile, BleGattProfileFliterInterface fliterInterface) {
        Logger.d(String.format("profile----------------------:\n%s", profile));
        for (BleGattService service : profile.getServices()) {
            // 过滤service
            if (service.getUUID().toString().startsWith(CharacteristicUUID.serviceUUID)) {
                if (!OTAManager.getInstance().isOTA()) {
                    for (BleGattCharacter character : service.getCharacters()) {
                        // 过滤character
                        if (character.getUuid().toString().startsWith(CharacteristicUUID.characteristicNofifyUUID)) {
                            // 监听指定的nofity通道
                            if (fliterInterface != null) {
                                fliterInterface.targetNotify(service.getUUID(), character.getUuid());
                            }
                        }
                        if (character.getUuid().toString().startsWith(CharacteristicUUID.characteristicWriteUUID)) {
                            // 监听指定的write通道
                            if (fliterInterface != null) {
                                fliterInterface.targetWrite(service.getUUID(), character.getUuid());
                            }
                        }
                    }
                } else {
                }
            } else if (service.getUUID().toString().startsWith(CharacteristicUUID.batteryServiceUUID)) {
                if (!OTAManager.getInstance().isOTA()) {
                    for (BleGattCharacter character : service.getCharacters()) {
                        if (character.getUuid().toString().contains(CharacteristicUUID.batteryReadUUID)) {
                            // 监听指定的write通道
                            if (fliterInterface != null) {
                                fliterInterface.batteryRead(service.getUUID(), character.getUuid());
                            }
                        }
                    }
                }
            } else if (service.getUUID().toString().startsWith(CharacteristicUUID.deviceInfoServiceUUID)) {
                if (fliterInterface != null) {
                    fliterInterface.targetDeviceInfo(service.getUUID(), service.getCharacters());
                }
            }
        }
    }

    public static void monitirBMDJResponse(BleGattProfile profile, ConnectFliterInterface fliterInterface) {
        Logger.d(String.format("monitirBMDJResponse：profile----------------------:\n%s", profile));
        for (BleGattService service : profile.getServices()) {
            // 过滤service
            if (service.getUUID().toString().startsWith(CharacteristicUUID.serviceUUID)) {
                for (BleGattCharacter character : service.getCharacters()) {
                    // 过滤character
                    if (character.getUuid().toString().startsWith(CharacteristicUUID.characteristicBMDJUUID)) {
                        // 监听指定的nofity通道
                        if (fliterInterface != null) {
                            Logger.d(String.format("service.getUUID()----------------------:%s\ncharacter.getUuid()----------------------:%s", service.getUUID(), character.getUuid()));
                            fliterInterface.targetResponse(service.getUUID(), character.getUuid());
                        }
                    }
                }
            }
        }

    }

    public static void monitorTorreResponse(BleGattProfile profile, TorreCharacterFliterInterface fliterInterface) {
        for (BleGattService service : profile.getServices()) {
            if (service.getUUID().toString().startsWith(CharacteristicUUID.serviceUUID)) {
                for (BleGattCharacter character : service.getCharacters()) {
                    // 过滤character
                    if (character.getUuid().toString().startsWith(CharacteristicUUID.TorreCharacteristicF1)) {
                        // 监听指定的nofity通道
                        if (fliterInterface != null) {
                            fliterInterface.targetF1(service.getUUID(), character.getUuid());
                        }
                    }
                    if (character.getUuid().toString().startsWith(CharacteristicUUID.TorreCharacteristicF2)) {
                        // 监听指定的write通道
                        if (fliterInterface != null) {
                            fliterInterface.targetF2(service.getUUID(), character.getUuid());
                        }
                    }
                    if (character.getUuid().toString().startsWith(CharacteristicUUID.TorreCharacteristicF3)) {
                        // 监听指定的write通道
                        if (fliterInterface != null) {
                            fliterInterface.targetF3(service.getUUID(), character.getUuid());
                        }
                    }
                }
            } else if (service.getUUID().toString().startsWith(CharacteristicUUID.batteryServiceUUID)) {
                if (!OTAManager.getInstance().isOTA()) {
                    for (BleGattCharacter character : service.getCharacters()) {
                        if (character.getUuid().toString().contains(CharacteristicUUID.batteryReadUUID)) {
                            // 监听指定的write通道
                            if (fliterInterface != null) {
                                fliterInterface.batteryRead(service.getUUID(), character.getUuid());
                            }
                        }
                    }
                }
            } else if (service.getUUID().toString().startsWith(CharacteristicUUID.deviceInfoServiceUUID)) {
                if (fliterInterface != null) {
                    fliterInterface.targetDeviceInfo(service.getUUID(), service.getCharacters());
                }

            }
        }
    }

    public static void monitoV4Response(BleGattProfile profile, V4CharacterFliterInterface fliterInterface) {
        Logger.d(String.format("profile----------------------:\n%s", profile));

        for (BleGattService service : profile.getServices()) {
            // 过滤service
            if (service.getUUID().toString().startsWith(CharacteristicUUID.serviceUUID)) {
                for (BleGattCharacter character : service.getCharacters()) {
                    // 数据传输
                    if (character.getUuid().toString().startsWith(CharacteristicUUID.TorreCharacteristicF1)) {
                        // 指令通信协议APP－>设备，见Part1、Part2、Part3、Part4、Part7和Part8
                        if (fliterInterface != null) {
                            fliterInterface.targetFFF1(service.getUUID(), character.getUuid());
                        }
                    }
                    if (character.getUuid().toString().startsWith(CharacteristicUUID.TorreCharacteristicF2)) {
                        // 监听指定的write通道
                        if (fliterInterface != null) {
                            fliterInterface.targetFFF2(service.getUUID(), character.getUuid());
                        }
                    }
                    if (character.getUuid().toString().startsWith(CharacteristicUUID.TorreCharacteristicF4)) {
                        // 指令通信协议设备－>APP， 见Part1、Part2、Part3、Part4、Part7和Part8
                        if (fliterInterface != null) {
                            fliterInterface.targetFFF4(service.getUUID(), character.getUuid());
                        }
                    }
                }

            } else if (service.getUUID().toString().startsWith(CharacteristicUUID.batteryServiceUUID)) {
                for (BleGattCharacter character : service.getCharacters()) {
                    if (character.getUuid().toString().contains(CharacteristicUUID.batteryReadUUID)) {
                        // 监听指定的write通道
                        if (fliterInterface != null) {
                            fliterInterface.batteryRead(service.getUUID(), character.getUuid());
                        }
                    }
                }
            } else if (service.getUUID().toString().startsWith(CharacteristicUUID.deviceInfoServiceUUID)) {
                if (fliterInterface != null) {
                    fliterInterface.readDeviceInfo(service.getUUID(), service.getCharacters());
                }
            }
        }

    }



}


