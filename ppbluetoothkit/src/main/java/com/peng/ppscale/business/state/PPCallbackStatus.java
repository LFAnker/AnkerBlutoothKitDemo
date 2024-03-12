package com.peng.ppscale.business.state;

public class PPCallbackStatus {
    // 通用
    // 成功
    public static final int SUCCESS = 0;
    // 失败
    public static final int ERROR = 1;
    // 内部错误
    public static final int INNER_ERROR = 2;
    // 参数不合法
    public static final int ERROR_ILLEGAL_ARGUMENT = 3;
    // 未知
    public static final int UNKNOWN = 4;
    // 不支持此方法
    public static final int METHOD_NOT_SUPPORTED = 5;
    // 蓝牙异常
    public static final int BT_ERROR = 6;

    // 连接中
    public static final int CONNECT_STATUS_CONNECTING = 11;
    // 已连接
    public static final int CONNECT_STATUS_CONNECTED = 12;
    // 断连中
    public static final int CONNECT_STATUS_DISCONNECTING = 13;
    // 已断连
    public static final int CONNECT_STATUS_DISCONNECTED = 14;
    // 连接失败
    public static final int CONNECT_STATUS_CONNECT_FAILED = 15;
    // 断连失败
    public static final int CONNECT_STATUS_DISCONNECT_FAILED = 16;

    // 测量未开始
    public static final int MEASURE_NOT_STARTED = 21;
    // 正在测量
    public static final int MEASURING = 22;
    // 测量超时
    public static final int MEASURE_TIMEOUT = 23;
    // 测量成功
    public static final int MEASURE_SUCCESS = 24;
    // 测量失败
    public static final int MEASURE_FAILED = 25;

    // 未配对
    public static final String BOND_NONE = "BOND_NONE";
    // 配对中
    public static final String BOND_BONDING = "BOND_BONDING";
    // 已配对
    public static final String BOND_BONDED = "BOND_BONDED";

    // 离秤
    public static final int SCALE_STATE_OFF = 0;
    //上称
    public static final int SCALE_STATE_UP = 1;

    // 屏幕亮
    public static final int SCREEN_STATE_ON = 0;
    //屏幕灭
    public static final int SCREEN_STATE_OFF = 1;
    /**
     * 不超重
     */
    public static final int OVER_WEIGHT_OFF = 0;
    //超重
    public static final int OVER_WEIGHT_ON = 1;




}
