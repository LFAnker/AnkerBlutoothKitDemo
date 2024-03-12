package com.peng.ppscale.util.json;

import android.text.TextUtils;

import com.lefu.gson.Gson;


public class GsonUtil {

    /**
     * 保存对象
     *
     * @param data
     */
    public static <T> String ObjToJsonString(T data) {
        if (data != null) {
            Gson gson = new Gson();

            //转换成json数据，再保存
            String strJson = gson.toJson(data);
            return strJson;
        }
        return "";
    }

    public static <T> T jsonStirngToObj(String strJson, Class<T> clazz) {
        Gson gson = new Gson();
        if (strJson != null && !TextUtils.isEmpty(strJson)) {
            T dataArr = gson.fromJson(strJson, clazz);
            return dataArr;
        }
        return null;
    }

}
