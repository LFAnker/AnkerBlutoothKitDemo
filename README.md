[English Docs](README_EN.md)  |  [中文文档](README.md)


# AnkerBluetoothKit Android SDK

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
	api(name: 'ppblutoothkit-1.0.2-20240318.072651-2', ext: 'aar') 
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

#### 1.3 SDK初始化

```  
        //SDK日志打印控制，true会打印
        PPBlutoothKit.setDebug(BuildConfig.DEBUG)
        /**
         * SDK 初始化
         */
        PPBlutoothKit.initSdk(this)
 ```   

#### 1.4 集成常见问题
- 如果遇到集成后提示“AndroidManifest.xml”相关的报错，请尝试着在主module中加入以下代码解决：
 ```   
android {
	 packagingOptions {
		 exclude 'AndroidManifest.xml' 
	 } 
 }
 ```  

- 如果遇到“.so”类型的文件错误，请尝试清除缓存，并将集成sdk的方式改为api

- 如遇到其他集成的问题请咨询：yanfabu-5@lefu.cc 或联系我们的销售顾问

- 如你有好的建议或优秀的代码你可以在Gitee上提交你的请求，我们会非常感谢你


## Ⅱ .使用说明

#### 1.1 运行环境

由于需要蓝牙连接，Demo需要真机运行，Android手机6.0及以上或HarmonyOS2.0及以上

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

## Ⅲ. 计算体脂 - Caclulate - Calculate4ACActivitiy

### 1.1  体脂计算所需参数说明

根据蓝牙协议解析出的体重、阻抗，加上用户数据的身高、年龄、性别，计算出体脂率等多项体脂参数信息。

#### 1.1.1   PPBodyBaseModel

| 参数 | 注释| 说明 |  
| :--------  | :-----  | :----:  |  
| weight | 体重 | 实际体重*100取整|  
| impedance|4电极算法阻抗（加密） |四电极算法字段|     
| userModel|用户基础信息对象 |PPUserModel|  
| unit| 秤端的当前单位 |实时单位|  
| heartRate| 心率 |秤端支持心率生效|  


#### 1.1.2 用户基础信息说明 PPUserModel

| 参数 | 注释| 说明 |  
| :--------  | :-----  | :----:  |  
| height| 身高|所有体脂秤|  
| age| 年龄 |所有体脂秤|  
| gender| 性别 |所有体脂秤|


### 1.2  四电极交流体脂计算 - 4AC - Calculate4ACActivitiy

**四电极交流计算体脂示例:**

```
     // 计算结果类：PPBodyFatModel
     val sex = if (etSex.text?.toString()?.toInt() == 0) {
            PPUserGender.PPUserGenderFemale
        } else {
            PPUserGender.PPUserGenderMale
        }
        val height = etHeight.text?.toString()?.toInt() ?: 180
        val age = etAge.text?.toString()?.toInt() ?: 28
        val weight = etWeight.text?.toString()?.toDouble() ?: 70.00
        val impedance = etImpedance.text?.toString()?.toLong() ?: 4195332L

        val userModel = PPUserModel.Builder()
            .setSex(sex) //gender
            .setHeight(height)//height 100-220
            .setAge(age)//age 10-99
            .build()

        val bodyBaseModel = PPBodyBaseModel()
        bodyBaseModel.weight = UnitUtil.getWeight(weight)
        bodyBaseModel.impedance = impedance
        bodyBaseModel.userModel = userModel

        val ppBodyFatModel = PPBodyFatModel(bodyBaseModel)

        DataUtil.util().bodyDataModel = ppBodyFatModel
        Log.d("liyp_", ppBodyFatModel.toString())
```

## Ⅳ. 设备扫描 - Device-ScanDeviceListActivity

### 1.1 扫描周围支持的设备-PPSearchManager


**注意：**

- 如果你在多个页面之间需要启动扫描，建议把扫描逻辑，放到工具类中，并用单例进行包裹
- 如果有连续页面需要调用扫描时，请一定要确保上个页面的蓝牙已停止扫描后，再在第二个页面进行扫描，建议是第二个页面延迟1000ms再启动。
- 如果你需要一直扫描蓝牙，你要在monitorBluetoothWorkState方法中ppBleWorkState返回PPBleWorkState.PPBleWorkSearchTimeOut时重启扫描，以确保循环扫描


`PPSearchManager`是设备扫描和连接的核心类，主要实现以下功能：

```
    /**
     * 扫描周围蓝牙秤列表
     * @parm 蓝牙扫描返回监听-PPSearchDeviceInfoInterface
     * @parm 蓝牙相关状态监听-PPBleStateInterface
     */
    public void startSearchDeviceList(int scanTimes, PPSearchDeviceInfoInterface searchDeviceInfoInterface, PPBleStateInterface bleStateInterface) {
    }

    /**
     * 停止搜索
     */
    public void stopSearch() {
    }


    /**
     * 是否正在扫描
     * @return
     */
    public boolean isSearching() {}
    
```

#### 1.1.1 蓝牙状态PPBleWorkState

| 分类枚举 | 说明 | 备注 |  
|------|--------|--------|  
| PPBleWorkStateSearching | 扫描中 |
| PPBleWorkSearchTimeOut| 扫描超时 | 如有需要可重启扫描 |  
| PPBleWorkSearchFail | 扫描失败| 如有需要可重启扫描 |  
| PPBleStateSearchCanceled| 停止扫描|主动调用停止扫描 |  
| PPBleWorkStateConnecting| 设备连接中 | |  
| PPBleWorkStateConnected | 设备已连接 | 连接上后，建议在PPBleWorkStateWritable中下发数据 |  
| PPBleWorkStateConnectFailed| 连接失败 | |  
| PPBleWorkStateDisconnected| 设备已断开 | |  
| PPBleWorkStateWritable | 可写 | 连接后如有需要给设备发送信息，可在此依次发送 |  



#### 1.1.4 停止扫描

``` 
ppScale.stopSearch();   
``` 
#### 1.1.5 重启扫描

重启扫描建议延迟1-2s再启动，防止触发Android系统的频繁扫描

```    
 public void delayScan() {  
    new Handler(getMainLooper()).postDelayed(new Runnable() {
     @Override 
     public void run() { 
        if (isOnResume) { 
            startScanDeviceList();     
                }   
            }   
     }, 1000);    
 }  
 ```   

# Ⅴ. 功能说明

### 2.1 功能说明 -PPBlutoothPeripheralIceController

```  
    //连接设备
    override fun startConnect(deviceModel: PPDeviceModel, bleStateInterface: PPBleStateInterface?) {}

    /**
     * 注册数据变化监听
     */
    fun registDataChangeListener(dataChangeListener: PPDataChangeListener) {}

    /**
     * 读取设备信息
     */
    fun readDeviceInfo(deviceInfoInterface: PPDeviceInfoInterface?) {}

    /**
     * 普通体脂秤同步时间
     */
    fun syncTime(sendResultCallBack: PPBleSendResultCallBack?) {}

    /**
     * 切换单位
     */
    fun syncUnit(userUnit: PPUnitType?, sendResultCallBack: PPBleSendResultCallBack?) {}

    /**
     * 获取历史数据
     */
    fun getHistory(historyDataInterface: PPHistoryDataInterface) {}


    /**
     * 清除历史数据
     */
    fun deleteHistoryData(bleSendListener: PPBleSendResultCallBack?) {}

    /**
     * 测值模式
     *
     * @param state 1打开孕妇模式 0关闭孕妇模式
     */
    fun controlImpendance(state: Int, modeChangeInterface: PPTorreDeviceModeChangeInterface?) {}
    
    /**
     * @param  state   心率 0打开 1关闭
     */
    fun controlHeartRate(state: Int, modeChangeInterface: PPTorreDeviceModeChangeInterface?) {}
    
    /**
     * 切换婴儿模式
     *
     * @param mode 00使能抱婴模式 01退出抱婴模式
     */
    fun exitBaby(modeChangeInterface: PPTorreDeviceModeChangeInterface?) {}

    /**
     * 切换婴儿模式
     *
     * @param mode 00使能抱婴模式 01退出抱婴模式
     */
    fun startBaby(modeChangeInterface: PPTorreDeviceModeChangeInterface?) {}

    /**
     * 切换婴儿模式
     *
     * @param mode 00使能抱婴模式 01退出抱婴模式
     */
    fun startPet(modeChangeInterface: PPTorreDeviceModeChangeInterface?) {}

    /**
     * 切换婴儿模式
     *
     * @param mode 00使能抱婴模式 01退出抱婴模式
     */
    fun exitPet(modeChangeInterface: PPTorreDeviceModeChangeInterface?) {}

    /**
     * 启动配网
     */
    fun configWifiStart(configWifiInfoInterface: PPConfigWifiInfoInterface) {}

    /**
     * 下发配网code、uid、服务器域名
     *
     * @param code
     * @param uid
     * @param url
     * @param configWifiInfoInterface
     */
    fun configNewCodeUidUrl(code: String, uid: String, url: String, configWifiInfoInterface: PPConfigWifiInfoInterface?) {}

    /**
     * 下发域名证书
     */
    fun configDomainCertificate(domainCertificate: String, configWifiInfoInterface: PPConfigWifiInfoInterface?) {}

    /**
     * 删除WiFi参数
     *
     * @param configWifiInfoInterface
     */
    fun configDeleteWifi(configWifiInfoInterface: PPConfigWifiInfoInterface?) {}

    /**
     * 查询WiFi参数
     */
    fun getWifiInfo(configWifiInfoInterface: PPConfigWifiInfoInterface?) {}

    /**
     * 更新WiFi参数(配网)-路由器名称
     *
     * @param ssid
     * @param configWifiInfoInterface
     */
    fun configUpdateWifiSSID(ssid: String?, configWifiInfoInterface: PPConfigWifiInfoInterface?) {}

    /**
     * 更新WiFi参数(配网)-路由器密码
     *
     * @param pwd
     * @param configWifiInfoInterface
     */
    fun configUpdateWifiPassword(pwd: String?, configWifiInfoInterface: PPConfigWifiInfoInterface?) {}

    /**
     * 更新WiFi参数(配网)-结束
     *
     * @param configWifiInfoInterface
     */
    fun startConnectRouter(configWifiInfoInterface: PPConfigWifiInfoInterface?) {}

    /**
     * 获取wifi列表
     */
    fun getWifiList(configWifiInfoInterface: PPConfigWifiInfoInterface) {}

    /**
     * 退出配网
     */
    fun exitConfigWifi() {}
    
    //获取设备绑定状态
    fun getDeviceBindState() {}

    //获取设备Token 状态
    fun getDeviceTokenState() {}

    //准备更新Token
    fun prepareUpdateToken() {}
    
    //设备模式查询
    fun getCurrDeviceModel() {}
    
    //获取当前wifi Rssi
    fun getCurrWifiRSSI() {}

    /**
     * 恢复出厂
     */
    fun resetDevice(ppDeviceSetInfoInterface: PPDeviceSetInfoInterface?) {}

    /**
     * 读取设备电量
     */
    fun readDeviceBattery(ppDeviceInfoInterface: PPDeviceInfoInterface?) {     }

    //配网心跳包
    fun startKeepAlive() {
        Logger.d("$tag startKeepAlive")
    }

    /**
     * 停止心跳
     */
    fun stopKeepAlive() {
        Logger.d("$tag stopKeepAlive")
      
    }
 ```   

## Ⅵ .实体类对象及具体参数说明

### 1.1 PPBodyFatModel 体脂计算对象参数说明

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

### 1.3 设备对象-PPDeviceModel

| 属性名 | 类型 | 描述 |备注|  
| ------ | ---- | ---- | ---- |  
| deviceMac | String | 设备mac|设备唯一标识|  
| deviceName | String | 设备蓝牙名称 |设备名称标识|  
| devicePower | Int | 电量 |-1标识不支持 >0为有效值|  
| rssi | Int | 蓝牙信号强度 |
| firmwareVersion | String? | 固件版本号 |要在连接后主动调用readDeviceInfo |  
| hardwareVersion | String? | 硬件版本号 |要在连接后主动调用readDeviceInfo |  
| manufacturerName | String? | 制造商 |要在连接后主动调用readDeviceInfo |  
| softwareVersion | String? | 软件版本号 |要在连接后主动调用readDeviceInfo |  
| serialNumber | String? | 序列号 |要在连接后主动调用readDeviceInfo |  
| modelNumber | String? | 时区编号 |要在连接后主动调用readDeviceInfo |  


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

