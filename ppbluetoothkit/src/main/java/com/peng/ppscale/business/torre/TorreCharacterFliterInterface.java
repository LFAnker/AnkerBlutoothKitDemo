package com.peng.ppscale.business.torre;

import com.inuker.bluetooth.library.model.BleGattCharacter;

import java.util.List;
import java.util.UUID;

/**
 * Torre 设备 4电极和8电极
 * 0xFFF1 Write，Read，Notify 控制类指令通道[APP<－>设备]
 * 0xFFF2  Write，Read，Notify 数据类指令通道[APP<－>设备]
 * 0xFFF3 Indicate 实时数据[设备－>APP]
 */
public interface TorreCharacterFliterInterface {

    void targetF1(UUID service, UUID character);

    void targetF2(UUID service, UUID character);

    void targetF3(UUID service, UUID character);

    void batteryRead(UUID uuid, UUID uuid1);

    void targetDeviceInfo(UUID uuid, List<BleGattCharacter> characters);
}