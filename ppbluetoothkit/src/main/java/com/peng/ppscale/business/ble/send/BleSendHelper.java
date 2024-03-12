package com.peng.ppscale.business.ble.send;

import android.text.TextUtils;

import com.inuker.bluetooth.library.utils.ByteUtils;
import com.peng.ppscale.business.device.PPUnitType;
import com.peng.ppscale.util.ByteUtil;
import com.peng.ppscale.util.Logger;
import com.peng.ppscale.util.UnitUtil;
import com.peng.ppscale.util.UserUtil;
import com.peng.ppscale.vo.PPDeviceModel;
import com.peng.ppscale.vo.PPScaleDefine;
import com.peng.ppscale.vo.PPUserGender;
import com.peng.ppscale.vo.PPUserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class BleSendHelper {
    static int number = 0;//广播数据计数

    public static byte[] deleteAdoreHistoryData() {
        String byteStr = "F201";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    public static byte[] sendSyncHistoryData2AdoreScale() {
        String byteStr = "F200";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }


    /**
     * @param userUnit
     * @param address
     * @param mode     0-表示为有效的用户组信息，退出安全模式   1-表示秤进入安全模式，不会测量阻抗。
     * @return
     */
    static String sendAdvertisingData(PPUnitType userUnit, String address, int mode) {
        StringBuilder buffer = new StringBuilder();
        StringBuilder data = new StringBuilder();
        String[] split = address.split(":");
        data.append(split[2]).append(split[3]).append(split[4]).append(split[5]);
        String modeHex = "37";
        if (mode == 1) {
            modeHex = "38";
        }
        data.append(modeHex);
        int unit = UnitUtil.getUnitInt(userUnit, "");
        data.append(ByteUtil.decimal2Hex(unit));
        number++;
        if (number >= 256) {
            number = 0;
        }
        data.append(ByteUtil.decimal2Hex(number));//序号
        byte[] byteData = ByteUtils.stringToBytes(data.toString());
        byte[] bytes = new byte[1];
        bytes[0] = ByteUtil.getXorValue(byteData);
        String dataXor = ByteUtil.byteToString(bytes);
        Logger.d("sendAdvertisingData data: " + data + dataXor);
        buffer.append(data);
        buffer.append(dataXor);
        buffer.append("000000000000");
        Logger.d("sendAdvertisingData : " + buffer);
        return buffer.toString();
    }

    /**
     * 该秤不支持切换单位，代码先写这
     *
     * @param userModel
     * @param userUnit
     * @return
     */
    public static byte[] syncUnitInScale(PPUserModel userModel, PPUnitType userUnit) {
        if (userModel != null) {
            int sex = UserUtil.getEnumSex(userModel.sex);
            int unit = UnitUtil.electronicUnitEnum2Int(userUnit);
            String byteStr = "FE"
                    + ByteUtil.decimal2Hex(userModel.groupNum)
                    + ByteUtil.decimal2Hex(sex)
                    + ByteUtil.decimal2Hex(0)
                    + ByteUtil.decimal2Hex(userModel.userHeight)
                    + ByteUtil.decimal2Hex(userModel.age)
                    + ByteUtil.decimal2Hex(unit);
            byte[] bytes = ByteUtil.stringToBytes(byteStr);
            byte[] xorByte = ByteUtil.getXorForElectronicScale(bytes);
            return xorByte;
        }
        return null;
    }

    /**
     * 切换单位
     *
     * @param userUnit
     * @return
     */
    public static byte[] syncUnitV2(PPUnitType userUnit, PPUserModel userModel, PPDeviceModel deviceModel) {
        int unit = 0;
        if (deviceModel == null) {
            unit = UnitUtil.getUnitInt(userUnit, "");
        } else {
            unit = UnitUtil.getUnitInt(userUnit, deviceModel.getDeviceName());
        }
        String mode = getMode(userModel, deviceModel);
        String byteStr = "";
        if (deviceModel.getModelNumber() != null && deviceModel.getModelNumber().equals("UTC-0")) {
            byteStr += "FD";
            byteStr += mode;
            byteStr += ByteUtil.decimal2Hex(unit);
            byteStr += "00";
            byteStr += userModel != null && userModel.isAthleteMode ? "02" : "00";  //运动员级别（0：表示为普通1：表示为业余2：表示为专业）
            byteStr += userModel != null && userModel.sex == PPUserGender.PPUserGenderFemale ? "00" : "01";//性别（0：表示为女1：男）
            byteStr += userModel != null ? ByteUtil.decimal2Hex(userModel.age) : "00";//年龄（10-99 岁）
            byteStr += userModel != null ? ByteUtil.decimal2Hex(userModel.userHeight) : "00";//身高（100-220CM）
            byteStr += "00";
            byteStr += "00";
        } else {
            byteStr = "FD"
                    + mode
                    + ByteUtil.decimal2Hex(unit)
                    + "00"
                    + "00000000"
                    + "00"
                    + "00";
        }

        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return ByteUtil.getXor(bytes);
    }

    public static byte[] syncUnitV3(PPUnitType userUnit, PPUserModel userModel, PPDeviceModel deviceModel) {
        int unit = 0;
        if (deviceModel == null) {
            unit = UnitUtil.getUnitInt(userUnit, "");
        } else {
            unit = UnitUtil.getUnitInt(userUnit, deviceModel.getDeviceName());
        }
        String mode = getMode(userModel, deviceModel);
        StringBuffer buffer = new StringBuffer();
        buffer.append("FD");
        buffer.append(mode);
        buffer.append(ByteUtil.decimal2Hex(unit));
        buffer.append("00");//用户组
        buffer.append(userModel != null && userModel.isAthleteMode ? "02" : "00");//运动员级别（0：表示为普通1：表示为业余2：表示为专业）
        buffer.append(userModel != null && userModel.sex == PPUserGender.PPUserGenderFemale ? "00" : "01");//性别（0：表示为女1：男）
        buffer.append(userModel != null ? ByteUtil.decimal2Hex(userModel.age) : "00"); //年龄（10-99 岁）
        buffer.append(userModel != null ? ByteUtil.decimal2Hex(userModel.userHeight) : "00");//身高（100-220CM）
        buffer.append("00");//用户体重，精度0.01KG
        buffer.append("00");//用户体重，精度0.01KG
        //异或校验，表示Byte0-byte9 异或校验和
        byte[] bytes = ByteUtil.stringToBytes(buffer.toString());
        return ByteUtil.getXor(bytes);
    }

    private static String getMode(PPUserModel userModel, PPDeviceModel deviceModel) {
        String mode = "00";
        if (deviceModel == null) {
            return "00";
        }
        if (userModel != null && userModel.isPregnantMode) {
            mode = "38";
        } else if (deviceModel.getModelNumber() != null && deviceModel.getModelNumber().equals("UTC-0")) {
            mode = "37";
        } else if ((deviceModel.deviceFuncType & PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWifi.getType())
                == PPScaleDefine.PPDeviceFuncType.PPDeviceFuncTypeWifi.getType()
                || deviceModel.deviceType == PPScaleDefine.PPDeviceType.PPDeviceTypeCA) {
            mode = "00";
        } else {
            mode = "37";
        }
        return mode;
    }

    /**
     * 归零
     *
     * @return
     */
    public static byte[] toZeroKitchenScale() {
        /**
         * 32 归零
         */
        String byteStr = "FD"
                + "32"
                + "00"
                + "00"
                + "00"
                + "00"
                + "00"
                + "00"
                + "00"
                + "00";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        byte[] xorByte = ByteUtil.getXor(bytes);
        return xorByte;
    }

    /**
     * V3蜂鸣器开关
     *
     * @return
     */
    public static byte[] switchBuzzer(boolean isOpen) {
        String buzzerHex = isOpen ? "01" : "00";
        /**
         * 32 归零
         */
        String byteStr = "FD"
                + "41"
                + buzzerHex
                + "00"
                + "00"
                + "00"
                + "00"
                + "00"
                + "00"
                + "00";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        byte[] xorByte = ByteUtil.getXor(bytes);
        return xorByte;
    }

    /**
     * V3时间同步
     *
     * @return
     */
    static byte[] sendSyncTime() {
        // 获取指定格式的时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        // 输出字符串
        String dateStr = df.format(new Date());
        Logger.d("发送时间 sendSyncTime : utctime = " + dateStr);
        String[] strArr = dateStr.split("/");
        String yearHex = ByteUtil.decimal2Hex(Integer.parseInt(strArr[0]));
        /**
         * 40 下发时间
         */
        String byteStr = "FD"
                + "40"
                + yearHex.substring(2, 4) + yearHex.substring(0, 2)
                + ByteUtil.decimal2Hex(Integer.parseInt(strArr[1]))
                + ByteUtil.decimal2Hex(Integer.parseInt(strArr[2]))
                + ByteUtil.decimal2Hex(Integer.parseInt(strArr[3]))
                + ByteUtil.decimal2Hex(Integer.parseInt(strArr[4]))
                + ByteUtil.decimal2Hex(Integer.parseInt(strArr[5]))
                + "00";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        byte[] xorByte = ByteUtil.getXor(bytes);
        return xorByte;
    }

    public static byte[] sendSyncTimeData2AdoreScale() {
        // 获取指定格式的时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        // 输出字符串
        String dateStr = df.format(new Date());
        Logger.d("发送时间 sendSyncTimeData2AdoreScale : utctime = " + dateStr);
        String[] strArr = dateStr.split("/");
        String byteStr = "F1";
        for (String s : strArr) {
            int target = Integer.parseInt(s);
            byteStr += ByteUtil.decimal2Hex(target);
        }
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    /**
     * 同步格林威治时间
     *
     * @return
     */
    static byte[] sendSyncTimeUTC() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss", Locale.getDefault());
        fmt.setTimeZone(TimeZone.getTimeZone("Etc/GMT+0"));
        String utctime = fmt.format(new Date());
        Logger.d("UTC-0 发送UTC时间 : utctime = " + utctime);
        System.out.println("utc:" + utctime);
        String[] strArr = utctime.split("/");
        String byteStr = "F1";
        for (String s : strArr) {
            int target = Integer.parseInt(s);
            byteStr += ByteUtil.decimal2Hex(target);
        }
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    /*闭目单脚指令*/
    static byte[] sendMBDJData2Scale() {
        // 060F0000
        String byteStr = "06"
                + "0F"
                + "00"
                + "00";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    /*退出闭目单脚指令*/
    static byte[] sendExitMBDJData2Scale() {
        // 060F0000
        String byteStr = "06"
                + "11"
                + "00"
                + "00";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    /*根据ssid和password 生成配网指令*/
    @Deprecated
    static ArrayList<byte[]> codeBySSIDAndPassword(String ssid, String password) {
        String ssidHex = ByteUtil.stringToHexString(ssid);
        if (ssidHex.length() < 76) {
            for (int i = ssidHex.length(); i < 76; i++) {
                ssidHex += "00";
            }
        }
        String hexSSID1 = "03" + ssidHex.substring(0, 38);
        String hexSSID2 = "04" + ssidHex.substring(38, 76);
        String hexPassword = "05" + ByteUtil.stringToHexString(password);

        byte[] bytesSSID1 = ByteUtil.stringToBytes(hexSSID1);
        byte[] bytesSSID2 = ByteUtil.stringToBytes(hexSSID2);
        byte[] bytesPassword = ByteUtil.stringToBytes(hexPassword);
        byte[] bytesEnd = ByteUtil.stringToBytes("06");
        ArrayList<byte[]> result = new ArrayList<>();
        result.add(bytesSSID1);
        result.add(bytesSSID2);
        if (password.length() > 0) {
            result.add(bytesPassword);
        }
        result.add(bytesEnd);
        return result;
    }

    /*根据ssid和password 生成配网指令*/
    public static ArrayList<byte[]> codeBySSIDAndPassword1(String ssid, String password) {
        String ssidHex = ByteUtil.stringToHexString(ssid);
        List<byte[]> ssidArray = new ArrayList<>();
        if (ssidHex.length() > 0) {
            String head = "0A";
            int len = ssidHex.length();
            int total = (int) (ssidHex.length() / 30) + (ssidHex.length() % 30 > 0 ? 1 : 0);
            int num = 0;
            for (int i = 0; i < total; i++) {
                String dataBody = head
                        + ByteUtil.decimal2Hex(total)
                        + ByteUtil.decimal2Hex(num)
                        + ByteUtil.decimal2Hex(len / 2)
                        + ssidHex.substring(i * 30, Math.min(len, (i + 1) * 30));

                byte xorValue = ByteUtil.getXorValue(ByteUtil.stringToBytes(dataBody));
                String xorHex = String.format("%02X", xorValue);
                String data = dataBody + xorHex;
                num++;
                ssidArray.add(ByteUtil.stringToBytes(data));
            }
        }

        List<byte[]> passwordArray = getPassHex(password);
        ArrayList<byte[]> result = new ArrayList<>(ssidArray);
        if (passwordArray != null) {
            result.addAll(passwordArray);
        }
        byte[] bytesEnd = ByteUtil.stringToBytes("0E");
        result.add(bytesEnd);
        return result;
    }

    private static List<byte[]> getPassHex(String password) {
        String passwordHex = ByteUtil.stringToHexString(password);
        if (passwordHex.length() > 0) {
            List<byte[]> arrayHex = new ArrayList<>();
            String head = "0B";//0B15010031323334353637383930313233343547
            int len = passwordHex.length();
            int total = (int) (passwordHex.length() / 30) + (passwordHex.length() % 30 > 0 ? 1 : 0);
            int num = 0;
            for (int i = 0; i < total; i++) {
                String dataBody = head
                        + ByteUtil.decimal2Hex(total)
                        + ByteUtil.decimal2Hex(num)
                        + ByteUtil.decimal2Hex(len / 2)
                        + passwordHex.substring(i * 30, Math.min(len, (i + 1) * 30));

                byte xorValue = ByteUtil.getXorValue(ByteUtil.stringToBytes(dataBody));
                String xorHex = String.format("%02X", xorValue);
                String data = dataBody + xorHex;
                num++;
                arrayHex.add(ByteUtil.stringToBytes(data));
            }
            return arrayHex;
        }
        return null;
    }

    public static byte[] disWifi() {
        String byteStr = "F300";
        return ByteUtil.stringToBytes(byteStr);
    }

    /*修改服务器IP地址*/
    public static List<byte[]> modifyServerIp(String serverIP) {
        String byteStr = getSendString("F700", serverIP);
        return getSendByteList(byteStr, 40);
    }

    /*修改服务器域名*/
    public static List<byte[]> modifyServerDomain(String serverDNS) {
        if (serverDNS.contains("http://")) {
            serverDNS = serverDNS.replace("http://", "");
        }
        String byteStr = getSendString("F800", serverDNS);
        return getSendByteList(byteStr, 40);
    }

    public static List<byte[]> modifyServerDNSV4(String serverDNS) {
        String byteStr = getSendString("F800", serverDNS);
        return getSendByteList(byteStr, 40);
    }

    /*删除WiFi参数*/
    public static byte[] deleteWifiConfig() {
        // 060F0000
        String byteStr = "F400";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    /*查询WiFi参数*/
    public static byte[] inquityWifiConfig() {
        // 060F0000
        String byteStr = "F500";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    /*重置称*/
    public static byte[] resetDeviceConfig() {
        String byteStr = "F901";
        byte[] bytes = ByteUtil.stringToBytes(byteStr);
        return bytes;
    }

    private static String getSendString(String head, String serverIP) {
        String ipHex = ByteUtil.stringToHexString(serverIP);

        byte xorValue = ByteUtil.getXorValue(ByteUtil.stringToBytes(ipHex));
        String xorHex = String.format("%02X", xorValue);

        return head + ByteUtil.decimal2Hex(ipHex.length() / 2) + xorHex + ipHex;
    }

    public static List<byte[]> getSendByteList(String byteStr, int length) {
        int size = byteStr.length() / length;
        if (byteStr.length() % length != 0) size += 1;
        List<byte[]> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String childStr = substring(byteStr, i * length, (i + 1) * length);
            if (!TextUtils.isEmpty(childStr)) {
                byte[] subByte = ByteUtil.stringToBytes(childStr);
                result.add(subByte);
            }
        }
        return result;
    }

    private static String substring(String str, int x, int y) {
        if (x > str.length())
            return null;
        if (y > str.length()) {
            return str.substring(x, str.length());
        } else {
            return str.substring(x, y);
        }
    }

}
