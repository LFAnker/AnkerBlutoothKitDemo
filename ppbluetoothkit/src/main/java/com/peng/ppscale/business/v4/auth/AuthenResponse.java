package com.peng.ppscale.business.v4.auth;

import java.util.List;
import java.util.Random;

/**
 * Copyright (C), 2017-2021, Anker
 * FileName: AuthenResponse
 *
 * @author ken.luo
 * Date: 2021/10/13 21:04
 * Description: 鉴权相应类
 * History:
 * 创建者 : ken.luo
 * <author>     <time>     <version>      <desc>
 * 作者姓名      修改时间      版本号          描述
 */
public class AuthenResponse {

    private static class SingletonHolder {
        private static final AuthenResponse INSTANCE = new AuthenResponse();
    }

    private AuthenResponse() {
    }

    public static AuthenResponse getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 加密密钥
     */
    public String secretKey;
    /**
     * 最终新的秘钥
     */
    public String appNewKey;



    /**
     * 2. APP生成一组random UUID字符，"AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA"，
     * 中间有四个 '-' 隔开，其中每个字符的取值为 '0' ~ '9' , 'A' ~ 'F' 字符（共36个字节）；
     */
    public String randomUuid;


    /**
     * 再通过base64 encode加密成一组加密数据；
     */
    public String encryptRandomUuid;
    /**
     * 最后的组合Uuid
     */
    public String encryptComposeUuid;


    public List<byte[]> commondList;
    /**
     * 第二次的指令
     */
    public List<byte[]> commondComposeList;
    public List<byte[]> commondThirdList;


    /**
     * 设备加密UUID
     */
    public String encryptDeviceUuid;



    /**
     * 设备UUID
     */
    public String deviceUuid;
    /**
     * AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA_BBBBBBBB-BBBB-BBBB-BBBB-BBBBBBBBBBBB
     * C5 UUID
     */
    public String C5Uuid;

    /**
     * C5回复的加密UUID
     */
    public String encryptC5Uuid;


    public String getComposeUuid() {
        String randomUuid = this.randomUuid == null ? "" : this.randomUuid;
        String deviceUuid = this.deviceUuid == null ? "" : this.deviceUuid;
        return randomUuid + "_" + deviceUuid;
    }

}
