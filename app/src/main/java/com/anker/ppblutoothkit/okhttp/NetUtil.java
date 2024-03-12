package com.anker.ppblutoothkit.okhttp;


import com.anker.ppblutoothkit.BuildConfig;

public class NetUtil {

    /**
     * http='https://healthu.lefuenergy.com',
     * ws='ws://120.79.144.170:8081',
     * scaleHttp='http://120.79.144.170:8288'
     */

    /***前面的链接地址***/
    public static String GET_URL() {
        String url = null;
        if (!BuildConfig.DEBUG) {
            url = "https://healthu.lefuenergy.com";   // 线上正式服务器
        } else {
            url = "https://healthu.lefuenergy.com";   // 线上正式服务器
        }
        return url;
    }

    //清除设备数据
    public static String CLEAR_DEVICE_DATA = GET_URL() + "/lefu/wifi/app/clearDeviceData";
    //获取秤端上传的体重信息（可根据sn号和uid单独查询 或 组合查询）
    public static String GET_SCALE_WEIGHTS = GET_URL() + "/lefu/wifi/app/getScaleWeights";
    //将Wifi与用户进行绑定，需要你自己的服务器，该处只做演示
    //Binding WiFi to users requires your own server, which will only be used for demonstration purposes
    public static String SAVE_WIFI_GROUP = GET_URL() + "/lefu/wifi/app/saveWifiGroup";


    /**
     * 下发给秤的域名/Domain name to be sent to the scale
     */
    public static String SCALE_DOMAIN = "appliances-api-qa.eufylife.com";
    /**
     * 秤的域名对应的证书/The certificate corresponding to the domain name of the scale
     */
    public static String CERTIFICATE = "-----BEGIN CERTIFICATE-----\n" +
            "MIIEADCCAuigAwIBAgIBADANBgkqhkiG9w0BAQUFADBjMQswCQYDVQQGEwJVUzEh\n" +
            "MB8GA1UEChMYVGhlIEdvIERhZGR5IEdyb3VwLCBJbmMuMTEwLwYDVQQLEyhHbyBE\n" +
            "YWRkeSBDbGFzcyAyIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA0MDYyOTE3\n" +
            "MDYyMFoXDTM0MDYyOTE3MDYyMFowYzELMAkGA1UEBhMCVVMxITAfBgNVBAoTGFRo\n" +
            "ZSBHbyBEYWRkeSBHcm91cCwgSW5jLjExMC8GA1UECxMoR28gRGFkZHkgQ2xhc3Mg\n" +
            "MiBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTCCASAwDQYJKoZIhvcNAQEBBQADggEN\n" +
            "ADCCAQgCggEBAN6d1+pXGEmhW+vXX0iG6r7d/+TvZxz0ZWizV3GgXne77ZtJ6XCA\n" +
            "PVYYYwhv2vLM0D9/AlQiVBDYsoHUwHU9S3/Hd8M+eKsaA7Ugay9qK7HFiH7Eux6w\n" +
            "wdhFJ2+qN1j3hybX2C32qRe3H3I2TqYXP2WYktsqbl2i/ojgC95/5Y0V4evLOtXi\n" +
            "EqITLdiOr18SPaAIBQi2XKVlOARFmR6jYGB0xUGlcmIbYsUfb18aQr4CUWWoriMY\n" +
            "avx4A6lNf4DD+qta/KFApMoZFv6yyO9ecw3ud72a9nmYvLEHZ6IVDd2gWMZEewo+\n" +
            "YihfukEHU1jPEX44dMX4/7VpkI+EdOqXG68CAQOjgcAwgb0wHQYDVR0OBBYEFNLE\n" +
            "sNKR1EwRcbNhyz2h/t2oatTjMIGNBgNVHSMEgYUwgYKAFNLEsNKR1EwRcbNhyz2h\n" +
            "/t2oatTjoWekZTBjMQswCQYDVQQGEwJVUzEhMB8GA1UEChMYVGhlIEdvIERhZGR5\n" +
            "IEdyb3VwLCBJbmMuMTEwLwYDVQQLEyhHbyBEYWRkeSBDbGFzcyAyIENlcnRpZmlj\n" +
            "YXRpb24gQXV0aG9yaXR5ggEAMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQEFBQAD\n" +
            "ggEBADJL87LKPpH8EsahB4yOd6AzBhRckB4Y9wimPQoZ+YeAEW5p5JYXMP80kWNy\n" +
            "OO7MHAGjHZQopDH2esRU1/blMVgDoszOYtuURXO1v0XJJLXVggKtI3lpjbi2Tc7P\n" +
            "TMozI+gciKqdi0FuFskg5YmezTvacPd+mSYgFFQlq25zheabIZ0KbIIOqPjCDPoQ\n" +
            "HmyW74cNxA9hi63ugyuV+I6ShHI56yDqg+2DzZduCLzrTia2cyvk0/ZM/iZx4mER\n" +
            "dEr/VxqHD3VILs9RaRegAhJhldXRQLIQTO7ErBBDpqWeCtWVYpoNz4iCxTIM5Cuf\n" +
            "ReYNnyicsbkqWletNw+vHX/bvZ8=\n" +
            "-----END CERTIFICATE-----";

    /**
     * 拉取设备配置信息，仅供Demo使用，与AppKey配套使用,
     * 在你自己的App中，请使用：PPBlutoothKit.initSdk(this, appKey, Companion.appSecret, "lefu.config")
     *
     */
    public static String GET_SCALE_CONFIG = "https://appliances-api-qa.eufylife.com/v1/user/wifi_scale/bind_code";

}
