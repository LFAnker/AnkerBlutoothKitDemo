package com.peng.ppscale.business.v4;

/**
 * Created by hero.he on 2021/10/19.
 */

public class T9148Cmd {
    private T9148Cmd() {

    }

    //时间同步
    public static final byte[] T9148_SYNC_TIME = new byte[]{(byte) 0xF1, (byte) 0x08, (byte) 0x01};
    public static final byte[] T9148_GET_TIME = new byte[]{(byte) 0xF1, (byte) 0x01, (byte) 0x02};

    // 历史记录
    public static final byte[] T9148_DELETE_HISTORY = new byte[]{(byte) 0xF2, (byte) 0x01, (byte) 0x01};
    public static final byte[] T9148_SYNC_HISTORY_DATA = new byte[]{(byte) 0xF2, (byte) 0x01, (byte) 0x00};
    //将设备恢复为出厂设置（移除设备时使用）
    public static final byte[] T9148_RECOVER_DEVICE = new byte[]{(byte) 0xF9, (byte) 0x01, (byte) 0x00};
    //心率
    public static final byte[] T9148_HEART_RATE_OPEN = new byte[]{(byte) 0xFB, (byte) 0x01, (byte) 0x00};
    public static final byte[] T9148_HEART_RATE_CLOSE = new byte[]{(byte) 0xFB, (byte) 0x01, (byte) 0x01};
    public static final byte[] T9148_HEART_RATE_STATE = new byte[]{(byte) 0xFB, (byte) 0x01, (byte) 0x02};
    //单位同步
    public static final byte[] T9148_SYNC_UNIT = new byte[]{(byte) 0xFD, (byte) 0x0A, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    //进入安全模式
    public static final byte[] T9148_OPEN_SAFE_MODE = new byte[]{(byte) 0xFD, (byte) 0x0A, (byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    //关闭安全模式
    public static final byte[] T9148_CLOSE_SAFE_MODE = new byte[]{(byte) 0xFD, (byte) 0x0A, (byte) 0x3F, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    //进入抱婴模式
    public static final byte[] T9148_ENTER_HOLD_BABY_MODE = new byte[]{(byte) 0xFD, (byte) 0x0A, (byte) 0x3B, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    //退出抱婴模式
    public static final byte[] T9148_EXIT_HOLD_BABY_MODE = new byte[]{(byte) 0xFD, (byte) 0x0A, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    //进入宠物模式
    public static final byte[] T9148_ENTER_PET_MODE = new byte[]{(byte) 0xFD, (byte) 0x0A, (byte) 0x3D, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    //退出宠物模式
    public static final byte[] T9148_EXIT_PET_MODE = new byte[]{(byte) 0xFD, (byte) 0x0A, (byte) 0x3E, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

    //配网开始
    public static final byte[] T9148_CONFIG_WIFI_START = new byte[]{(byte) 0x06, (byte) 0x01, (byte) 0x00};
    //下发配网code、uid、服务器域名
    public static final byte[] T9148_CONFIG_CODE_UID_URL = new byte[]{(byte) 0xF8, (byte) 0x01, (byte) 0x00};
    //下发域名证书
    public static final byte[] T9148_CONFIG_DOMAIN_CERTIFICATE = new byte[]{(byte) 0xF7, (byte) 0x01, (byte) 0x00};
    //域名证书下发完成（结束）ACK
    public static final byte[] T9148_CONFIG_FINISH = new byte[]{(byte) 0xF6, (byte) 0x01, (byte) 0x00};
    //删除WiFi参数
    public static final byte[] T9148_CONFIG_DELETE_WIFI = new byte[]{(byte) 0xF4, (byte) 0x01, (byte) 0x00};
    //查询WiFi参数
    public static final byte[] T9148_CONFIG_QUERY_WIFI = new byte[]{(byte) 0xF5, (byte) 0x01, (byte) 0x00};
    //更新WiFi参数(配网)-路由器名称
    public static final byte[] T9148_CONFIG_UPDATE_WIFI_NAME = new byte[]{(byte) 0x0A, (byte) 0x01, (byte) 0x00};
    //更新WiFi参数(配网)-路由器密码
    public static final byte[] T9148_CONFIG_UPDATE_WIFI_PASSWORD = new byte[]{(byte) 0x0A, (byte) 0x01, (byte) 0x00};
    //更新WiFi参数(配网)-结束
    public static final byte[] T9148_CONFIG_UPDATE_WIFI_FINISH = new byte[]{(byte) 0x0E, (byte) 0x01, (byte) 0x00};
    //获取wifi列表
    public static final byte[] T9148_CONFIG_GET_WIFI_LIST = new byte[]{(byte) 0x07, (byte) 0x01, (byte) 0x00};
    //取消配网
    public static final byte[] T9148_CANCEL_CONFIG = new byte[]{(byte) 0x0D, (byte) 0x01, (byte) 0x00};
    //秤亮灯指令
    public static final byte[] T9148_OPEN_LIGHT = new byte[]{(byte) 0xD1, (byte) 0x01, (byte) 0x00};
    //配网心跳包
    public static final byte[] T9148_CONFIG_NET_HEART = new byte[]{(byte) 0x10, (byte) 0x01, (byte) 0x00};
    //获取设备绑定状态
    public static final byte[] T9148_DEVICE_BIND_STATE = new byte[]{(byte) 0x0F, (byte) 0x01, (byte) 0x00};
    //获取设备Token 状态
    public static final byte[] T9148_DEVICE_TOKEN_STATE = new byte[]{(byte) 0xB0, (byte) 0x01, (byte) 0x00};
    //准备更新Token
    public static final byte[] T9148_DEVICE_PREPARE_UPDATE_TOKEN = new byte[]{(byte) 0xB1, (byte) 0x01, (byte) 0x00};
    //下发新Token
//    public static byte[] T9148_DEVICE_UPDATE_TOKEN = new byte[]{(byte) 0xB1, (byte) 0x01, (byte) 0x00};


    //设备模式查询
//    public static byte[] T9148_DEVICE_MODE = new byte[]{(byte) 0xFA, (byte) 0x06, (byte) 0xAF, (byte) 0xFC, (byte) 0xCF, (byte) 0x00, (byte) 0x00, (byte) 0x60};
    public static final byte[] T9148_DEVICE_MODE = new byte[]{(byte) 0xFA, (byte) 0x06, (byte) 0xAF, (byte) 0xFC, (byte) 0xCF, (byte) 0x00, (byte) 0x00};

    //开始ota升级
    public static final byte[] T9148_OTA_START = new byte[]{(byte) 0xE0, (byte) 0x01, (byte) 0x00};
    //获取ota的状态
    public static final byte[] T9148_DEVICE_OTA_STATE = new byte[]{(byte) 0xE3, (byte) 0x01, (byte) 0x00};
    //获取当前wifi Rssi
    public static final byte[] T9148_GET_CURR_WIFI_RSSI = new byte[]{(byte) 0xE4, (byte) 0x01, (byte) 0x00};


    //设备模式 用户模式，不加密
    public static final byte[] T9148_DEVICE_USER_MODE = new byte[]{(byte) 0xFA, (byte) 0x06, (byte) 0xAF, (byte) 0xFC, (byte) 0xCF, (byte) 0x01, (byte) 0x01};
    //设备模式 工厂模式，不加密
    public static final byte[] T9148_DEVICE_FACTORY_MODE = new byte[]{(byte) 0xFA, (byte) 0x06, (byte) 0xAF, (byte) 0xFC, (byte) 0xCF, (byte) 0x00, (byte) 0x01};

}
