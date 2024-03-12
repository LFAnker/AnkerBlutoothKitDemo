package com.peng.ppscale.business.ble.configWifi;

public enum PPConfigStateMenu {

    /**
     * 确认开始配网失败
     */
    CONFIG_STATE_ERROR_TYPE_START_FAIL,
    /**
     * 确认开始发送数据
     */
    CONFIG_STATE_ERROR_TYPE_DATA_SEND_FAIL,
    /**
     * 发送ssid数据
     */
    CONFIG_STATE_ERROR_TYPE_DATA_SEND_SSID_FAIL,
    /**
     * 发送pwd数据
     */
    CONFIG_STATE_ERROR_TYPE_DATA_SEND_PWD_FAIL,
    /**
     * 发送domain数据
     */
    CONFIG_STATE_ERROR_TYPE_DATA_SEND_DOMAIN_FAIL,
    /**
     * 注册成功
     */
    CONFIG_STATE_SUCCESS,
    /**
     * 超时失败
     */
    CONFIG_STATE_ERROR_TYPE_REGIST_TIMEOUT,
    /**
     * 路由器连接失败
     */
    CONFIG_STATE_ERROR_TYPE_REGIST_ROUTER,
    /**
     * 收到HTTP网络错误码
     */
    CONFIG_STATE_ERROR_TYPE_REGIST_HTTP,
    /**
     * 收到HTTPS网络错误码
     */
    CONFIG_STATE_ERROR_TYPE_REGIST_HTTPS,
    /**
     * 收到注册服务器返回失败
     */
    CONFIG_STATE_ERROR_TYPE_REGIST_SERVER,
    /**
     * 注册失败[配网指令漏发]
     */
    CONFIG_STATE_ERROR_TYPE_REGIST_FAIL,
    /**
     * 退出配网
     */
    CONFIG_STATE_EXIT,

}
