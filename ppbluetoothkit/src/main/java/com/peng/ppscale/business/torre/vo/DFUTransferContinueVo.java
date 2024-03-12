package com.peng.ppscale.business.torre.vo;

public class DFUTransferContinueVo {

    public int dfuTransferContinueState = 1;//断点续传状态Transfer continue  status 0-从上次的断点开始传 1-从头开始传输
    public int dfuTransferContinueFileType = 0;//上次断点DFU文件类型  1mcu 0ble 3res
    public String dfuTransferContinueFileVersion = "001";//上次断点DFU文件版本号-ANSSI码
    public int dfuTransferContinueUpgradedOffset = 0;//上次断点DFU文件已升级大小-文件OFFSET,APP根据此偏移继续下发升级数据，实现断点续传状态
    public int maxChunkeSize = 0;

    @Override
    public String toString() {
        return "DFUTransferContinueVo{" +
                "dfuTransferContinueState=" + dfuTransferContinueState +
                ", dfuTransferContinueFileType=" + dfuTransferContinueFileType +
                ", dfuTransferContinueFileVersion='" + dfuTransferContinueFileVersion + '\'' +
                ", dfuTransferContinueUpgradedOffset=" + dfuTransferContinueUpgradedOffset +
                ", maxChunkeSize=" + maxChunkeSize +
                '}';
    }
}
