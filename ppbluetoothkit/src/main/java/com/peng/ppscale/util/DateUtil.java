package com.peng.ppscale.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat(FORMAT);

    /**
     * @param strTime yyyy-MM-dd HH:mm:ss --> long
     * @return
     */
    public static long stringToLong(String strTime) {
        try {
            Date date = stringToDate(strTime, FORMAT);
            if (date == null) {
                return 0;
            } else {
                long currentTime = date.getTime();
                return currentTime;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long stringToLong(String strTime, String formatType)  {
        Date date = null;
        try {
            date = stringToDate(strTime, formatType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime();
            return currentTime;
        }
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }


    /**
     * 格式化日期时间 日期时间格式yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatDatetime(Date date) {
        return datetimeFormat.format(date);
    }


    /**
     * 2      * 获取指定时间到格林威治时间的秒数
     * 3      * UTC：格林威治时间1970年01月01日00时00分00秒（UTC+8北京时间1970年01月01日08时00分00秒）
     * 4      * @param time
     * 5      * @return
     * 6
     */
    public static long diffSeconds() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        Date datetime = new Date();
        calendar.setTime(datetime);
        TimeZone timeZone = TimeZone.getTimeZone("GMT00:00");
        calendar.setTimeZone(timeZone);
        TimeZone tz = TimeZone.getDefault();
        Logger.d("UTC-0 RawOffset = " + tz.getRawOffset());
        return tz.getRawOffset();
    }


    /**
     * 时区转换
     *
     * @return
     */
    public static String getCmdSyncTimeCMD(long timeMillis) {
        TimeZone zoneObj = TimeZone.getDefault();
        int zone = zoneObj.getRawOffset() / 3600000;

        // 获取当前时区的Calendar实例
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(zoneObj);

        // 检查当前时间是否在夏令时
        boolean isDaylightTime = zoneObj.inDaylightTime(calendar.getTime());
        if (isDaylightTime) {
            Logger.d("DateUtil getCmdSyncTimeCMD 当前处于夏令时");
            zone += 1;
        } else {
            Logger.d("DateUtil getCmdSyncTimeCMD 当前不处于夏令时");
        }
        String hex = "";
        if (zone >= 0) {
            hex = "0";
        } else {
            hex = "1";
        }

        String dataZone = getHexByDecimal(Math.abs(zone));
        String byteStr = getBinaryStrByHexStr(dataZone);
        byteStr = hex + byteStr.substring(1);

        String timeStr = "0406";
        String timeZone = "08";
        timeZone = getHexByBinary(byteStr);
        if (timeZone.length() < 2) {
            timeZone = String.format("0%s", timeZone);
        }
        long timeStamp = timeMillis / 1000;
        String utcTime = getHexByDecimal(timeStamp);
        utcTime = reversStrWith2Step(utcTime);
        timeStr = timeStr + utcTime + timeZone;
        timeStr = timeStr + "01";
        return timeStr;
    }

    public static String getHexByDecimal(long decimal) {
        return Long.toHexString(decimal);
    }

    public static String getBinaryStrByHexStr(String hex) {
        String s = Long.toBinaryString(Long.parseLong(hex, 16));
        int len = s.length();
        if (s.length() < 8) {
            for (int i = 0; i < 8 - len; i++) {
                s = String.format("0%s", s);
            }
        }
        return s;
    }

    public static String getHexByBinary(String binary) {
        return Long.toHexString(Long.parseLong(binary, 2));
    }

    public static String reversStrWith2Step(String str) {
        StringBuilder reversed = new StringBuilder();
        for (int i = 0; i < str.length(); i += 2) {
            reversed.insert(0, str.substring(i, i + 2));
        }
        return reversed.toString();
    }


}
