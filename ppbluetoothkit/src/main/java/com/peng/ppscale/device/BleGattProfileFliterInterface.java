package com.peng.ppscale.device;


import com.inuker.bluetooth.library.model.BleGattCharacter;

import java.util.List;
import java.util.UUID;

public interface BleGattProfileFliterInterface {

    void targetNotify(UUID service, UUID character);

    void targetWrite(UUID service, UUID character);

    void batteryRead(UUID service, UUID character);

    void targetDeviceInfo(UUID uuid, List<BleGattCharacter> characters);
}