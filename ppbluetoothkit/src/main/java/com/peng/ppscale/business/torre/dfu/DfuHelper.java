package com.peng.ppscale.business.torre.dfu;

import com.peng.ppscale.business.torre.vo.DFUTransferContinueVo;
import com.peng.ppscale.business.torre.vo.DfuFileVo;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.util.json.GsonUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DfuHelper {

    public static DataVo dataVo;
    public static DFUTransferContinueVo dfuTransferContinueVo;
    public static int currentPackageNum = 0;//当前的块位置

    public static int packageNum = 0;//总块数

    public static long curentLen = 0;//记录当前发送的长度
    public static long fileLen = 0;//数据总长度

    public static List<byte[]> getSendDfuData(int mtu) {
        if (dataVo != null) {
            byte[] data = dataVo.getData();
            if (dfuTransferContinueVo != null) {
                int maxChunkeSize = dfuTransferContinueVo.maxChunkeSize;
                //总块数
                Logger.d("getSendDfuData 单个文件总长data.length: " + data.length + " 最大传输块maxChunkeSize: " + maxChunkeSize);
                packageNum = data.length / maxChunkeSize + (data.length % maxChunkeSize > 0 ? 1 : 0);
                int surplusLen = data.length % maxChunkeSize;
                Logger.d("getSendDfuData currentPackageNum: " + currentPackageNum + " 总块数packageNum:" + packageNum + " 最后块的长度surplusLen: " + surplusLen);
                List<byte[]> sendDataList = new ArrayList<>();
                if (currentPackageNum < packageNum - 1) {
                    curentLen += maxChunkeSize;
                    byte[] bytes = new byte[maxChunkeSize];
                    System.arraycopy(data, maxChunkeSize * currentPackageNum, bytes, 0, maxChunkeSize);
                    sendDataList = ByteUtil.subAccordToMTU(bytes, mtu);
                } else if (surplusLen > 0) {
                    //截取maxChunkeSize长度的byte
                    Logger.d("getSendDfuData 最后一块 ");
                    curentLen += surplusLen;
                    byte[] bytes = new byte[surplusLen];
                    System.arraycopy(data, maxChunkeSize * currentPackageNum, bytes, 0, surplusLen);
                    sendDataList = ByteUtil.subAccordToMTU(bytes, mtu);
                } else {
                    return null;
                }
                currentPackageNum++;
                Logger.d("getSendDfuData  每块的包数sendDataList size:" + sendDataList.size());
                return sendDataList;
            }
        }
        return null;
    }

    /**
     * //组装数据
     *
     * @param dfuFilePath
     * @return
     * @throws NullPointerException
     */
    public static String getStartDfuData(String dfuFilePath) throws NullPointerException {
        if (dfuFilePath == null || dfuFilePath.isEmpty()) {
            Logger.e("startDFUSend  dfuFilePath is null");
            throw new NullPointerException();
        }
        Logger.d("getStartDfuData dfuFilePath: " + dfuFilePath);
        String startCmd = "03";
        currentPackageNum = 0;
        curentLen = 0;
        String maxChunkeSizeHex = ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(dfuTransferContinueVo.maxChunkeSize), 6));
        Logger.d("getStartDfuData maxChunkeSizeHex: " + maxChunkeSizeHex);
        //断点续传状态
        String dfuTransferContinueStateHex = ByteUtil.decimal2Hex(dfuTransferContinueVo.dfuTransferContinueState);
        Logger.d("getStartDfuData dfuTransferContinueStateHex: " + dfuTransferContinueStateHex);
        //当前DFU文件类型
        int fileType = dfuTransferContinueVo.dfuTransferContinueFileType;
        String dfuTransferContinueFileType = ByteUtil.decimal2Hex(fileType);
        Logger.d("getStartDfuData dfuTransferContinueFileType: " + dfuTransferContinueFileType);

        List<DfuHelper.DataVo> dataVos = DfuHelper.getDfuFileByte(dfuFilePath);
        //MCU 1 BLE 0 RES 3
        if (fileType == 0) {
            dataVo = dataVos.get(1);
        } else if (fileType == 1) {
            dataVo = dataVos.get(0);
        } else if (fileType == 3) {
            dataVo = dataVos.get(2);
        }
        if (dataVo == null) {
            Logger.e("startDFUSend  dfuFilePath is null");
            throw new NullPointerException();
        }
//        Logger.d("targetF2 data ： " + dataVo.toString());
        //当前DFU文件大小
        byte[] dfuFileByte = dataVo.getData();
        fileLen = dfuFileByte != null ? dfuFileByte.length : 0;
        Logger.d("getStartDfuData fileLen: " + fileLen);
        String dfuFileByteSizeHex = ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(fileLen), 8));
        Logger.d("getStartDfuData dfuFileByteSizeHex: " + dfuFileByteSizeHex);
        //当前DFU文件的云端版本号
        String version = dataVo.getVersion();
        String versionHex = ByteUtil.stringToHexString(version);
        Logger.d("getStartDfuData version: " + version + " versionHex : " + versionHex);
        //连续升级文件个数(几个组件需要升级)
        int fileNum = 3;
        String fileNumHex = ByteUtil.decimal2Hex(fileNum);
        Logger.d("getStartDfuData fileNumHex: " + fileNumHex);
        long allLen = 0;
        for (DfuHelper.DataVo dataVo1 : dataVos) {
            allLen += dataVo1.getData().length;
        }
        Logger.e("getStartDfuData allLen: " + allLen);
        String allLenHex = ByteUtil.hexToLittleEndianMode(ByteUtil.autoLeftPadZero(ByteUtil.decimal2Hex(allLen), 8));
        Logger.d("getStartDfuData allLenHex: " + allLenHex);
        String data = startCmd + maxChunkeSizeHex + dfuTransferContinueStateHex + dfuTransferContinueFileType +
                dfuFileByteSizeHex + versionHex + fileNumHex + allLenHex;
        Logger.d("getStartDfuData data: " + data);
        String startDfuDataHex = "0B" + ByteUtil.decimal2Hex(data.length()) + data;
        return startDfuDataHex;
    }

    public static List<DataVo> getDfuFileByte(String parentFilePath) {
        List<DataVo> dataVoList = new ArrayList<>();
        DfuFileVo dfuFileVo = getDfuFile(parentFilePath);
        if (dfuFileVo != null) {
            Logger.d("DFU DfuHelper dfuFileVo: " + dfuFileVo.toString());
            DfuFileVo.PackagesBean packages = dfuFileVo.getPackages();
            //BLE 0 MCU 1 RES 3
            DfuFileVo.PackagesBean.McuBean mcu = packages.getMcu();
            DfuFileVo.PackagesBean.BleBean ble = packages.getBle();
            DfuFileVo.PackagesBean.ResBean res = packages.getRes();
            if (mcu != null) {
                dataVoList.add(getDataVo(parentFilePath, mcu.getFilename(), mcu.getVersion()));
            } else {
                dataVoList.add(new DataVo());
            }
            if (ble != null) {
                dataVoList.add(getDataVo(parentFilePath, ble.getFilename(), ble.getVersion()));
            } else {
                dataVoList.add(new DataVo());
            }
            if (res != null) {
                dataVoList.add(getDataVo(parentFilePath, res.getFilename(), res.getVersion()));
            } else {
                dataVoList.add(new DataVo());
            }
        } else {
            Logger.e("DFU DfuHelper dfuFileVo is null");
        }
        return dataVoList;
    }

    public static DfuFileVo getDfuFile(String parentFilePath) {
        File file = new File(parentFilePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File file1 : files) {
                    if (file1.getName().contains(".json")) {
                        try {
                            FileInputStream fileInputStream = new FileInputStream(file1);
                            InputStreamReader inputreader = new InputStreamReader(fileInputStream);
                            BufferedReader buffreader = new BufferedReader(inputreader);
                            String line;
                            StringBuilder content = new StringBuilder(); //文件内容字符串
                            while ((line = buffreader.readLine()) != null) {
                                content.append(line);
                            }
                            fileInputStream.close();
                            Logger.d("DFU DfuHelper getDfuFileByte: " + content);
                            DfuFileVo dfuFileVo = GsonUtil.jsonStirngToObj(content.toString(), DfuFileVo.class);
                            if (dfuFileVo != null) {
                                Logger.d("DFU DfuHelper dfuFileVo: " + dfuFileVo.toString());
                            }
                            return dfuFileVo;
                        } catch (NullPointerException e) {
                            Logger.e("DFU DfuHelper getDfuFile  " + e.getMessage());
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            Logger.e("DFU DfuHelper getDfuFile " + e.getMessage());
                            e.printStackTrace();
                        } catch (IOException e) {
                            Logger.e("DFU DfuHelper getDfuFile " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    private static DataVo getDataVo(String parentFilePath, String fileName, String version) {
        Logger.d("parentFilePath: " + parentFilePath);
        Logger.d("fileName: " + fileName);
        String filePath = parentFilePath + fileName;
        Logger.d("filePath: " + filePath);
        byte[] fileByte = DfuHelper.getFileToByte(filePath);
        DataVo dataVo = new DataVo();
        dataVo.setData(fileByte);
        dataVo.setVersion(version);
        return dataVo;
    }

    private static byte[] getFileToByte(String filePath) {
        try {
            File updateFile = new File(filePath);
            FileInputStream in = null;
            in = new FileInputStream(updateFile);
            int updateFileLen = 0;
            updateFileLen = in.available();
            byte[] lData = new byte[updateFileLen];
            DataInputStream lDataIS = new DataInputStream(in);
            lDataIS.readFully(lData);
            return lData;
        } catch (FileNotFoundException e) {
            Logger.d("DFU getFileToByte: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Logger.d("DFU getFileToByte: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static class DataVo {

        byte[] data;
        String version = "0";

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "DataVo{" +
                    "data=" + Arrays.toString(data) +
                    ", version='" + version + '\'' +
                    '}';
        }
    }

}
