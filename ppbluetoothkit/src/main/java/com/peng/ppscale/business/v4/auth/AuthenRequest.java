package com.peng.ppscale.business.v4.auth;

/**
 * Copyright (C), 2017-2021, Anker
 * FileName: AuthenRequest
 *
 * @author ken.luo
 * Date: 2021/10/13 21:04
 * Description: 鉴权发起类
 * History:
 * 创建者 : ken.luo
 * <author>     <time>     <version>      <desc>
 * 作者姓名      修改时间      版本号          描述
 */
public class AuthenRequest {

    private static class SingletonHolder {
        private static final AuthenRequest INSTANCE = new AuthenRequest();
    }

    public static AuthenRequest getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 蓝牙Mac
     */
    public String bleMac = "88:4A:18:80:31:49";

    /**
     * 用于接收C6数据
     */
    public String reciverTempC6="";

    /**
     * 第一次发送授权数据中
     */
    public boolean isFirstAuthSending = false;

    /**
     * 用于接收WiFi列表数据
     */
    public String reciverTempWifi="";

}
