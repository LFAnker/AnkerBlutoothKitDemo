package com.peng.ppscale.business.v4;

import com.inuker.bluetooth.library.model.BleGattCharacter;

import java.util.List;
import java.util.UUID;

/**
 * Torre 设备 4电极和8电极
 * 0xFFF1 Write，Read，Notify 控制类指令通道[APP<－>设备]
 * 0xFFF2  Write，Read，Notify 数据类指令通道[APP<－>设备]
 * 0xFFF3 Indicate 实时数据[设备－>APP]
 */
public interface V4CharacterFliterInterface {

    void targetFFF1(UUID service, UUID character);

    void targetFFF2(UUID service, UUID character);

    void targetFFF4(UUID service, UUID character);

    void batteryRead(UUID uuid, UUID uuid1);

    void readDeviceInfo(UUID uuid, List<BleGattCharacter> characters);
}