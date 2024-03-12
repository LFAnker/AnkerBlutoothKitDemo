[English Docs](README_EN.md)  |  [中文文档](README.md)


# AnkerBluetoothKit iOS SDK

AnkerBluetoothKit是针对eufy T9148/eufy T9149进行封装的SDK，包含蓝牙连接逻辑、数据解析逻辑、体脂计算。

### 示例程序

为了让客户快速实现称重以及对应的功能而实现，提供了一个示例程序，示例程序中包含体脂计算模块和设备功能模块。

- 设备功能模块目前支持的设备包含：eufy T9148/eufy T9149系列蓝牙WiFi体脂秤。
- 体脂计算模块支持4电极交流算法。



## Ⅰ. 集成方式

#### 1.1 aar文件导入
- 在需要引入sdk的module下的build.gradle中加入(最新版本请查看ppscalelib的module下的libs)
``` 
dependencies { 
	//aar引入
	api(name: 'ppblutoothkit-1.0.0-20240312.130313-5', ext: 'aar') 
}  
```

#### 1.2 在`AndroidManifest.xml`文件中添加蓝牙权限

使用Demo过程中需要您打开蓝牙，打开定位开关，需确保开启和授权必要的权限: 精准定位权限和附近的设备权限 可以查看官方蓝牙权限文档，文档地址：[Google开发者网站关于Bluetooth permissions说明](https://developer.android.com/guide/topics/connectivity/bluetooth/permissions).

- 精准定位权限
- 附近设备权限
- 定位开关
- 蓝牙开关

``` 
<manifest>    
    <!-- Request legacy Bluetooth permissions on older devices. --> 
    <uses-permission android:name="android.permission.BLUETOOTH" android:maxSdkVersion="30" /> 
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" android:maxSdkVersion="30" />    
    <!-- Needed only if your app looks for Bluetooth devices. If your app doesn't use Bluetooth scan results to derive physical location information, you can strongly assert that your app doesn't derive physical location. --> 
    <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />    
    <!-- Needed only if your app makes the device discoverable to Bluetooth devices. --> 
    <uses-permission android:name="android.permission.BLUETOOTH_ADVERTISE" />    
    <!-- Needed only if your app communicates with already-paired Bluetooth devices. --> 
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />    
    <!-- Needed only if your app uses Bluetooth scan results to derive physical location. --> 
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> 
 ...
 </manifest>    
 ```   


## Ⅱ .使用说明

#### 1.1 运行环境

由于需要蓝牙连接，Demo需要在真机运行，支持iOS9.0及以上系统

### 1.2 测量身体数据相关约定

#### 1.2.1 称重测脂注意事项

- 秤支持测脂
- 光脚上称，并接触对应的电极片
- 称重接口返回体重(kg)和阻抗信息
- 人体参数身高、年龄输入正确

#### 1.2.2 体脂计算

##### 基础参数约定

| 类别 | 输入范围 | 单位 |    
|:----|:--------|:--:|    
| 身高 | 100-220 | cm |    
| 年龄 | 10-99 | 岁 |    
| 性别 | 0/1 | 女/男 |    
| 体重 | 10-200 | kg |

- 需要身高、年龄、性别和对应的阻抗，调用对应的计算库去获得
- 8电极所涉及的体脂数据项需要8电极的秤才可使用

## Ⅲ. 计算体脂 - calcute - CalcuteInfoViewController

### 1.1  体脂计算所需参数说明

根据蓝牙协议解析出的体重、阻抗，加上用户数据的身高、年龄、性别，计算出体脂率等多项体脂参数信息。

#### 1.1.1   AKBluetoothScaleBaseModel

| 参数 | 注释| 说明 |  
| :--------  | :-----  | :----:  |  
| weight | 体重 | 实际体重*100取整|  
| impedance|4电极算法阻抗（加密） |四电极算法字段|     
| isHeartRating| 是否正在测量心率 |心率测量状态|  
| unit| 秤端的当前单位 |实时单位|  
| heartRate| 心率 |秤端支持心率生效|  
| dataType| 数据类 |AKScaleDataTypeStable = 0, // 稳定的体重数据，AKScaleDataTypeDynamic = 1, // 动态的体重数据，AKScaleDataTypeOverweight = 2, // 超重的体重数据，AKScaleDataTypeFat = 3, // 带体脂率的锁定重量，AKScaleDataTypePetAndBaby = 4, // 宠物模式/抱婴模式下稳定的体重数据|


#### 1.1.2 用户基础信息说明 AKBluetoothDeviceSettingModel

| 参数 | 注释| 说明 |  
| :--------  | :-----  | :----:  |  
| height| 身高|所有体脂秤|  
| age| 年龄 |所有体脂秤|  
| gender| 性别 |所有体脂秤|


### 1.3  四电极交流体脂计算 - 4AC - CalcuelateResultViewController

**四电极交流计算体脂示例:**

```
// 计算结果类：AKBodyFatModel

var fatModel:AKBodyFatModel!
        
        fatModel =  AKBodyFatModel(userModel: userModel,
                                   deviceCalcuteType: PPDeviceCalcuteType.alternateNormal,
                                   deviceMac: mac,
                                   weight: weight,
                                   heartRate: heartRate,
                                   andImpedance: impedance)
                                       
// fatModel 为计算所得结果
if (fatModel.errorType == .ERROR_TYPE_NONE) {

	print("\(fatModel.description)")
} else {

	print("errorType:\(fatModel.errorType)")
}
```

## Ⅳ. 设备扫描 - Device-SearchDeviceViewController

### 1.2 扫描周围支持的设备-SearchDeviceViewController

`AKBluetoothConnectManager`是设备扫描和连接的核心类，主要实现以下功能：

- 蓝牙状态监听
- 扫描周边所支持的蓝牙设备
- 连接指定的蓝牙设备
- 断开指定设备的连接
- 停止扫描

```
@interface AKBluetoothConnectManager : NSObject

// 蓝牙状态代理
@property (nonatomic, weak) id<AKBluetoothUpdateStateDelegate> updateStateDelegate;

// 搜索设备代理
@property (nonatomic, weak) id<AKBluetoothSurroundDeviceDelegate> surroundDeviceDelegate;

// 连接设备代理
@property (nonatomic, weak) id<AKBluetoothConnectDelegate> connectDelegate;

@property (nonatomic, weak) id<AKBluetoothScaleDataDelegate> scaleDataDelegate;

// 搜索周边所支持的设备
- (void)searchSurroundDevice;

// 连接指定的设备
- (void)connectPeripheral:(CBPeripheral *)peripheral withDevice:(AKBluetoothAdvDeviceModel *)device;

// 停止搜索蓝牙设备
- (void)stopSearch;

// 断开连接指定的蓝牙设备
- (void)disconnect:(CBPeripheral *)peripheral;

@end
```

#### 1.2.1 创建AKBluetoothConnectManager实例

```
// 创建AKBluetoothConnectManager实例，并设置代理
let scaleManager:AKBluetoothConnectManager = AKBluetoothConnectManager()
self.scaleManager.updateStateDelegate = self;
self.scaleManager.surroundDeviceDelegate = self;
```

#### 1.2.2 实现蓝牙状态和搜索设备代理方法

```
extension SearchDeviceViewController:AKBluetoothUpdateStateDelegate,AKBluetoothSurroundDeviceDelegate{

    //蓝牙状态
    func centralManagerDidUpdate(_ state: AKBluetoothState) {
        
        if (state == .poweredOn){
            self.scaleManager.searchSurroundDevice()
        }
    }
    
    //搜索到所支持的设备
    func centralManagerDidFoundSurroundDevice(_ device: AKBluetoothAdvDeviceModel!, andPeripheral peripheral: CBPeripheral!) {

    }
}
```

#### 1.2.3 AKBluetoothUpdateStateDelegate和AKBluetoothSurroundDeviceDelegate代理方法说明

```
@protocol AKBluetoothUpdateStateDelegate <NSObject>

// 蓝牙状态
- (void)centralManagerDidUpdateState:(AKBluetoothState)state;

@end


@protocol AKBluetoothSurroundDeviceDelegate <NSObject>

// 搜索到所支持的设备
- (void)centralManagerDidFoundSurroundDevice:(AKBluetoothAdvDeviceModel *)device andPeripheral:(CBPeripheral *)peripheral;

@end
```

#### 1.2.4 蓝牙状态说明-AKBluetoothState

| 分类枚举 | 说明 | 备注 |  
|------|--------|--------|  
| AKBluetoothStateUnknown | 未知状态 |
| AKBluetoothStateResetting | 复位 |   
| AKBluetoothStateUnsupported | 不支持|
| AKBluetoothStateUnauthorized | 权限未授权| 
| AKBluetoothStatePoweredOff| 蓝牙已关闭 |
| AKBluetoothStatePoweredOn | 蓝牙已打开 |

#### 1.2.5 连接指定蓝牙设备

```
//连接蓝牙设备，并设置对应代理
self.scaleManager.connect(peripheral, withDevice: device)
self.scaleManager.connectDelegate = self
```

#### 1.2.6 设备连接状态代理实现-AKBluetoothConnectDelegate

```
extension DeviceAnkerViewController:AKBluetoothConnectDelegate{
    
    // 设备已连接
    func centralManagerDidConnect() {

    }
    
    // 设备断开连接
    func centralManagerDidDisconnect() {

    }
    
    // 设备连接失败
    func centralManagerDidFail(toConnect error: Error!) {
        
    }

}
```

#### 1.2.7搜索周边支持的设备

```
// 搜索周边支持的设备，调用前请判断蓝牙是否为“打开”状态
self.scaleManager.searchSurroundDevice()
```

#### 1.2.8 停止扫描

```
self.scaleManager.stopSearch()
```

#### 1.2.9 断开指定设备的连接

```
self.scaleManager.disconnect(peripheral)
```

# Ⅴ. 功能说明

### 2.1 功能说明 -DeviceAnkerViewController


#### 2.1.1 支持的功能

```
// AKBluetoothPeripheralAnker.h

@interface AKBluetoothPeripheralAnker : NSObject

@property (nonatomic, weak) id<AKBluetoothServiceDelegate> serviceDelegate;

@property (nonatomic, weak) id<AKBluetoothScaleDataDelegate> scaleDataDelegate;

@property (nonatomic, strong) CBPeripheral *peripheral;

@property (nonatomic, strong) AKBluetoothAdvDeviceModel *deviceAdv;

@property (nonatomic, strong) AKBatteryInfoModel *batteryInfo;


- (instancetype)initWithPeripheral:(CBPeripheral *)peripheral  andDevice:(AKBluetoothAdvDeviceModel *)device;


- (void)discoverFFF0Service;


/// 开始鉴权
/// - Parameter handler:  0 成功 1 失败
- (void)startAuth:(void(^)(NSInteger status))handler;


/// 获取电池电量
- (void)fetchDeviceBatteryInfo;

/// 发现180A设备信息服务
- (void)discoverDeviceInfoService:(void(^)(AKBluetooth180ADeviceModel *deviceModel))deviceInfoResponseHandler;

/// 设置单位
/// status 0 为成功 1为失败
- (void)changeUnit:(PPDeviceUnit)unit withHandler:(void(^)(NSInteger status))handler;

/// 进入孕妇模式（安全模式）
/// status 0 为成功 1为失败
- (void)enterPregnantWomanModeWithHandler:(void(^)(NSInteger status))handler;

/// 退出孕妇模式（安全模式）
/// status 0 为成功 1为失败
- (void)exitPregnantWomanModeWithHandler:(void(^)(NSInteger status))handler;

/// 进入婴儿模式
/// status 0 为成功 1为失败
- (void)enterBabyModeWithHandler:(void(^)(NSInteger status))handler;

/// 退出婴儿模式
/// status 0 为成功 1为失败
- (void)exitBabyModeWithHandler:(void(^)(NSInteger status))handler;

/// 进入宠物模式
/// status 0 为成功 1为失败
- (void)enterPetModeWithHandler:(void(^)(NSInteger status))handler;

/// 退出宠物模式
/// status 0 为成功  1为失败
- (void)exitPetModeWithHandler:(void(^)(NSInteger status))handler;

/// 查询孕妇模式（安全模式）开关
/// status 0 为开  1为关  2为异常
- (void)fetchPregnantWomanModeStatusWithHandler:(void(^)(NSInteger status))handler;

/// 切换模式（标定/内码）
/// status 0 为成功 1为失败
- (void)switchMode:(AnkerSwitchMode)mode withHandler:(void(^)(NSInteger status))handler;

/// 打开心率测量
/// - Parameter handler: 0设置成功 1设置失败
- (void)openHeartRateSwitch:(void(^)(NSInteger status))handler;


/// 关闭心率测量
/// - Parameter handler:  0设置成功 1设置失败
- (void)closeHeartRateSwitch:(void(^)(NSInteger status))handler;

/// 恢复出厂设置
/// status 0 为成功 1为失败
- (void)restoreFactoryWithHandler:(void(^)(NSInteger status))handler;

/// 获取心率开关状态
/// heartRateStatus 0开  1关 2异常
- (void)fetchHeartRateStatus:(void(^)(NSInteger heartRateStatus))handler;


/// 同步设备时间
///  status 0 为成功 1为失败
- (void)syncTime:(void(^)(NSInteger status))handler;


/// 查询设备时间
///  例：2017-08-17 21:04:48
- (void)fetchDeviceTime:(void(^)(NSString* deviceTime))handler;

/// 下发用户数据
/// status 0成功  1失败
- (void)syncUserInfo:(AKUserModel *)userModel withHandler:(void(^)(NSInteger status))handler;


/// 获取历史数据
/// - Parameter callBack:
- (void)fetchHistoryDataWithHandler:(void(^)(NSArray <AKBluetoothScaleBaseModel *>* history, NSError* error))handler;


/// 删除历史数据
- (void)deleteDeviceHistoryDataWithHandler:(void(^)(NSInteger status))handler;

/// 搜索附近WiFi列表
- (void)findSurroundWIFI:(void(^)(NSArray <AKWifiInfoModel *>*wifis, int status))handler;

/// 取消配网
/// status 0成功  1失败
- (void)cancelWifiConfigWithHandler:(void(^)(NSInteger status))handler;

/// 开始配网
/// status 0开始配网  1配网失败
- (void)startWifiWithHandler:(void(^)(NSInteger status))handler;

/// 下发配网code、uid、服务器域名
/// status 0成功  1失败
- (void)sendWifiCode:(NSString *)code uid:(NSString *)uid domain:(NSString *)domain handler:(void(^)(NSInteger status))handler;

/// 下发域名证书
/// status 0成功  1失败
- (void)sendCertificate:(NSString *)certificate withHandler:(void(^)(NSInteger status))handler;

/// 下发域名证书完成
/// status 0成功  1失败
- (void)sendCertificateCompleteWithHandler:(void(^)(NSInteger status))handler;

/// 更新WiFi参数(配网)-路由器名称
/// status 0成功  1失败
- (void)updateWifiSSID:(NSString *)ssid withHandler:(void(^)(NSInteger status))handler;

/// 更新WiFi参数(配网)-路由器密码
/// status 0成功  1失败
- (void)updateWifiPassword:(NSString *)password withHandler:(void(^)(NSInteger status))handler;

/// 更新WiFi参数(配网)-结束
/// status 0成功  1失败
/// step : 当前步骤：WIFI准备配网 - 0x11；下发device id - 0x12；下发product code - 0x13；配网开始指令 - 0x14；接收域名、uid - 0x15；接收证书 - 0x16；证书接收完成 - 0x17；WIFI list-  0x18；接收SSID - 0x19；接收password - 0x1A；配网完成指令 - 0x1B；删除WIFI参数 - 0x1C；连接路由器-0x25
/// errorType: 超时 - 0x01；接收到wifi错误码 - 0x02；接收到云端返回res_code错误码 - 0x0d
- (void)updateWifiInfoCompleteWithHandler:(void(^)(NSInteger status, Byte step, Byte errorType))handler;

@end
```

## Ⅵ .实体类对象及具体参数说明

### 1.1 AKBodyFatModel 体脂计算对象参数说明

四电极对应的24项数据


| 参数| 参数类型 | 说明 |数据类型|备注  
|------|--------|--------|--------|--------|  
|ppBodyBaseModel| AKBluetoothDeviceSettingModel |体脂计算的入参|基础入参|包含设备信息、用户基础信息、体重和心率|体脂秤  
|ppSDKVersion| String |计算库版本号|返回参数|  
|ppSex| PPUserGender|性别|返回参数| PPUserGenderFemale女PPUserGenderMale男  
|ppHeightCm|Int |身高|返回参数|cm  
|ppAge|Int |年龄|返回参数|岁  
|errorType|BodyFatErrorType |错误类型|返回参数|PP_ERROR_TYPE_NONE(0),无错误 PP_ERROR_TYPE_AGE(1), 年龄有误 PP_ERROR_TYPE_HEIGHT(2),身高有误 PP_ERROR_TYPE_WEIGHT(3),体重有误 PP_ERROR_TYPE_SEX(4) 性別有误 PP_ERROR_TYPE_PEOPLE_TYPE(5)  以下是阻抗有误 PP_ERROR_TYPE_IMPEDANCE_TWO_LEGS(6)  PP_ERROR_TYPE_IMPEDANCE_TWO_ARMS(7)PP_ERROR_TYPE_IMPEDANCE_LEFT_BODY(8) PP_ERROR_TYPE_IMPEDANCE_RIGHT_ARM(9)PP_ERROR_TYPE_IMPEDANCE_LEFT_ARM(10)  PP_ERROR_TYPE_IMPEDANCE_LEFT_LEG(11)  PP_ERROR_TYPE_IMPEDANCE_RIGHT_LEG(12)  PP_ERROR_TYPE_IMPEDANCE_TRUNK(13)  
|bodyDetailModel|PPBodyDetailModel|数据区间范围和介绍描述|  
|ppWeightKg|Float |体重|24&48|kg  
|ppBMI|Float|Body Mass Index|24&48|  
|ppFat|Float |脂肪率|24&48|%  
|ppBodyfatKg|Float |脂肪量|24&48|kg  
|ppMusclePercentage|Float |肌肉率|24&48|%  
|ppMuscleKg|Float |肌肉量|24&48|kg  
|ppBodySkeletal|Float |骨骼肌率|24&48|%  
|ppBodySkeletalKg|Float |骨骼肌量|24&48|kg  
|ppWaterPercentage|Float |水分率|24&48|%  
|ppWaterKg|Float |水分量|24&48|kg  
|ppProteinPercentage|Float |蛋白质率|24&48|%  
|ppProteinKg|Float |蛋白质量|24&48|kg  
|ppLoseFatWeightKg|Float |去脂体重|24&48|kg  
|ppBodyFatSubCutPercentage|Float |皮下脂肪率|24&48|%  
|ppBodyFatSubCutKg|Float |皮下脂肪量|24&48|kg  
|ppHeartRate|Int |心率|24&48|bmp该值与秤有关，且大于0为有效  
|ppBMR|Int |基础代谢|24&48|  
|ppVisceralFat|Int |内脏脂肪等级|24&48|  
|ppBoneKg|Float |骨量|24&48|kg  
|ppBodyMuscleControl|Float |肌肉控制量|24&48|kg  
|ppFatControlKg|Float |脂肪控制量|24&48|kg  
|ppBodyStandardWeightKg|Float |标准体重|24&48|kg  
|ppIdealWeightKg|Float |理想体重|24&48|kg  
|ppControlWeightKg|Float |控制体重|24&48|kg  
|ppBodyType|PPBodyDetailType |身体类型|24&48|PPBodyDetailType有单独说明  
|ppFatGrade|PPBodyFatGrade|肥胖等级|24&48|PPBodyGradeFatThin(0), //!< 偏瘦 PPBodyGradeFatStandard(1),//!< 标准 PPBodyGradeFatOverwight(2), //!< 超重 PPBodyGradeFatOne(3),//!< 肥胖1级 PPBodyGradeFatTwo(4),//!< 肥胖2级 PPBodyGradeFatThree(5);//!< 肥胖3级  
|ppBodyHealth|PPBodyHealthAssessment |健康评估|24&48|PPBodyAssessment1(0), //!< 健康存在隐患PPBodyAssessment2(1), //!< 亚健康 PPBodyAssessment3(2), //!< 一般 PPBodyAssessment4(3), //!< 良好  PPBodyAssessment5(4); //!< 非常好  
|ppBodyAge|Int|身体年龄|24&48|岁  
|ppBodyScore|Int |身体得分|24&48|分  


注意：在使用时拿到对象，请调用对应的属性来获取对应的值

### 1.2 身体类型-PPBodyDetailType

| 参数| 说明| type |  
|------|--------|--------|  
|PPBodyTypeThin|偏瘦型|0|  
|PPBodyTypeThinMuscle|偏瘦肌肉型|1|  
|PPBodyTypeMuscular|肌肉发达型|2|  
|PPBodyTypeLackofexercise|缺乏运动型|3|  
|PPBodyTypeStandard|标准型|4|  
|PPBodyTypeStandardMuscle|标准肌肉型|5|  
|PPBodyTypeObesFat|浮肿肥胖型|6|  
|PPBodyTypeFatMuscle|偏胖肌肉型|7|  
|PPBodyTypeMuscleFat|肌肉型偏胖|8|

### 1.3 设备对象-AKBluetoothAdvDeviceModel

| 属性名 | 类型 | 描述 |备注|  
| ------ | ---- | ---- | ---- |  
| deviceMac | String | 设备mac|设备唯一标识|  
| deviceName | String | 设备蓝牙名称 |设备名称标识|  
| rssi | Int | 蓝牙信号强度 |


### 1.4 设备单位-PPDeviceUnit

| 枚举类型 | type | 单位 |  
| -- | ---- |---- |  
|PPUnitKG| 0 | KG |  
|PPUnitLB| 1 | LB |  
|PPUnitSTLB| 2 | ST_LB |  
|PPUnitJin| 3 | 斤 |  
|PPUnitG| 4 | g |  
|PPUnitLBOZ| 5 | lb:oz |  
|PPUnitOZ| 6 | oz |  
|PPUnitMLWater| 7 | ml（水） |  
|PPUnitMLMilk| 8 | ml（牛奶） |  
|PPUnitFLOZWater| 9 | fl_oz（水） |  
|PPUnitFLOZMilk| 10 | fl_oz（牛奶） |  
|PPUnitST| 11 | ST |

