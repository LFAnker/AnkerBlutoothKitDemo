package com.peng.ppscale.business.v4

/**
 * 配网步骤
 *
 */
enum class WifiConfigStep {

    WifiConfigStep_Start,
    WifiConfigStep_SSID,
    WifiConfigStep_PWD,

    /**
     * 下发域名证书
     */
    WifiConfigStep_DomainCertificate,

    /**
     * 下发配网code、uid、服务器域名
     * 如果失败，需要重新发送配网code, code需要与服务器协商一致，确保code能正常使用。
     */
    WifiConfigStep_SendCodeUidDomain,

    /**
     * 连接路由器网络结果回调
     */
    WifiConfigStep_ConnectToRouter,


}