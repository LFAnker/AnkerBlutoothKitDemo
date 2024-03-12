package com.anker.ppblutoothkit.okhttp;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Map;

import okhttp3.MediaType;

public class DataTask {

    public static void post(String url, Map<String, String> parameter, Map<String, String> headers, RetCallBack callBack) {
        OkHttpUtils.post().url(url).headers(headers).params(parameter).tag(url).build().execute(callBack);

    }

    public static void postString(String url, String contents, Map<String, String> headers, RetCallBack callBack) {
        OkHttpUtils.postString()
                .url(url)
                .mediaType(MediaType.parse("application/json"))
                .headers(headers)
                .content(contents)
                .build()
                .execute(callBack);
    }




    public static void get(String url, Map<String, String> parameter, RetCallBack callBack) {
        OkHttpUtils.get().url(url).params(parameter).tag(url).build().execute(callBack);
    }


}
