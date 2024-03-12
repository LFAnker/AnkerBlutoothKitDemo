package com.peng.ppscale.business.ble.connect;


import java.util.UUID;

public interface ConnectFliterInterface {

    void targetResponse(UUID service, UUID character);

    void target2Write(UUID service, UUID character);

    void batteryRead(UUID service, UUID character);

    void softWareRevisionRead(UUID service, UUID character);

    void serialNumberRead(UUID service, UUID character);

    void targetResponseOTA1(UUID uuid, UUID uuid1);

    void targetResponseOTA2(UUID uuid, UUID uuid1);

    /**
     * 让秤进入内码模式
     * @param internalCodeModeServerUUID
     * @param internalCodeModeCharacterUUID
     */
    void targetResponseOTAState(UUID internalCodeModeServerUUID, UUID internalCodeModeCharacterUUID);

    void modelNumberRead(UUID uuid, UUID uuid1);
}