[English Docs](README_EN.md) | [Chinese Docs](README.md)


# AnkerBluetoothKit iOS SDK

AnkerBluetoothKit is an SDK packaged for eufy T9148/eufy T9149, including Bluetooth connection logic, data analysis logic, and body fat calculation.

### Sample program

In order to allow customers to quickly implement weighing and corresponding functions, a sample program is provided, which includes a body fat calculation module and a device function module.

- The devices currently supported by the device function module include: eufy T9148/eufy T9149 series Bluetooth WiFi body fat scale.
- The body fat calculation module supports 4-electrode AC algorithm.



## Ⅰ. Integration method

#### 1.1 aar file import
- Add it to build.gradle under the module where sdk needs to be introduced (for the latest version, please check the libs under the module of ppscalelib)
```
dependencies {
//aar introduction
api(name: 'ppblutoothkit-1.0.0-20240312.130313-5', ext: 'aar')
}
```

#### 1.2 Add Bluetooth permissions in the `AndroidManifest.xml` file

During the use of the Demo, you need to turn on Bluetooth and turn on the positioning switch. Make sure to enable and authorize the necessary permissions: For precise positioning permissions and nearby device permissions, you can view the official Bluetooth permissions document. The document address is: [Instructions on Bluetooth permissions on the Google Developer Website] (https://developer.android.com/guide/topics/connectivity/bluetooth/permissions).

- Accurate positioning permissions
- Nearby device permissions
- Position switch
- Bluetooth switch

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


## Ⅱ .Instructions for use

#### 1.1 Operating environment

Due to the need for Bluetooth connection, the Demo needs to be run on a real machine and supports iOS9.0 and above systems.

### 1.2 Conventions related to measuring body data

#### 1.2.1 Precautions for weighing and fat measurement

- The scale supports fat measurement
- Weigh on your bare feet and touch the corresponding electrode pads
- The weighing interface returns weight (kg) and impedance information
- Human body parameters height and age are entered correctly

#### 1.2.2 Body fat calculation

##### Basic parameter agreement

| Category | Input Range | Unit |
|:----|:--------|:--:|
| Height | 100-220 | cm |
| Age | 10-99 | Years |
| Gender | 0/1 | Female/Male |
| Weight | 10-200 | kg |

- Height, age, gender and corresponding impedance are required, and the corresponding calculation library is called to obtain them.
- The body fat data items involved in 8-electrode require an 8-electrode scale to be used.

## Ⅲ. Calculate body fat - calculate - CalcuteInfoViewController

### 1.1 Description of parameters required for body fat calculation

Based on the weight and impedance parsed by the Bluetooth protocol, plus the height, age, and gender of the user data, multiple body fat parameter information such as body fat rate is calculated.

#### 1.1.1 AKBluetoothScaleBaseModel

| Parameters | Comments | Description |
| :-------- | :----- | :----: |
| weight | weight | actual weight * rounded to 100 |
|impedance|4-electrode algorithm impedance (encryption) |4-electrode algorithm field|
| isHeartRating| Whether heart rate is being measured |Heart rate measurement status|
| unit| The current unit of the scale |Real-time unit|
| heartRate| Heart rate |The scale supports heart rate validation|
| dataType| Data class|AKScaleDataTypeStable = 0, // Stable weight data, AKScaleDataTypeDynamic = 1, // Dynamic weight data, AKScaleDataTypeOverweight = 2, // Overweight weight data, AKScaleDataTypeFat = 3, // with body fat percentage Lock weight, AKScaleDataTypePetAndBaby = 4, // Stable weight data in pet mode/baby mode |


#### 1.1.2 Basic user information description AKBluetoothDeviceSettingModel

| Parameters | Comments | Description |
| :-------- | :----- | :----: |
| height| height|all body fat scales|
| age| age |all body fat scales|
| gender| Gender |All body fat scales|


### 1.3 Four-electrode AC body fat calculation - 4AC - CalcuelateResultViewController

**Four-electrode AC body fat calculation example:**

```
// Calculation result class: AKBodyFatModel

var fatModel:AKBodyFatModel!
        
         fatModel = AKBodyFatModel(userModel: userModel,
                                    deviceCalcuteType: PPDeviceCalcuteType.alternateNormal,
                                    deviceMac: mac,
                                    weight: weight,
                                    heartRate: heartRate,
                                    andImpedance: impedance)
                                       
// fatModel is the calculated result
if (fatModel.errorType == .ERROR_TYPE_NONE) {

print("\(fatModel.description)")
} else {

print("errorType:\(fatModel.errorType)")
}
```

## Ⅳ. Device scanning - Device-SearchDeviceViewController

### 1.2 Scan surrounding supported devices-SearchDeviceViewController

`AKBluetoothConnectManager` is the core class for device scanning and connection. It mainly implements the following functions:

- Bluetooth status monitoring
- Scan for supported Bluetooth devices in the surrounding area
- Connect to designated Bluetooth devices
- Disconnect the specified device
- Stop scanning

```
@interface AKBluetoothConnectManager : NSObject

// Bluetooth status proxy
@property (nonatomic, weak) id<AKBluetoothUpdateStateDelegate> updateStateDelegate;

//Search for device agents
@property (nonatomic, weak) id<AKBluetoothSurroundDeviceDelegate> surroundDeviceDelegate;

//Connect device agent
@property (nonatomic, weak) id<AKBluetoothConnectDelegate> connectDelegate;

@property (nonatomic, weak) id<AKBluetoothScaleDataDelegate> scaleDataDelegate;

// Search for peripheral supported devices
- (void)searchSurroundDevice;

//Connect to the specified device
- (void)connectPeripheral:(CBPeripheral *)peripheral withDevice:(AKBluetoothAdvDeviceModel *)device;

// Stop searching for Bluetooth devices
- (void)stopSearch;

// Disconnect the specified Bluetooth device
- (void)disconnect:(CBPeripheral *)peripheral;

@end
```

#### 1.2.1 Create AKBluetoothConnectManager instance

```
//Create an AKBluetoothConnectManager instance and set the proxy
let scaleManager:AKBluetoothConnectManager = AKBluetoothConnectManager()
self.scaleManager.updateStateDelegate = self;
self.scaleManager.surroundDeviceDelegate = self;
```

[English Docs](README_EN.md) | [Chinese Docs](README.md)


# AnkerBluetoothKit iOS SDK

AnkerBluetoothKit is an SDK packaged for eufy T9148/eufy T9149, including Bluetooth connection logic, data analysis logic, and body fat calculation.

### Sample program

In order to allow customers to quickly implement weighing and corresponding functions, a sample program is provided, which includes a body fat calculation module and a device function module.

- The devices currently supported by the device function module include: eufy T9148/eufy T9149 series Bluetooth WiFi body fat scale.
- The body fat calculation module supports 4-electrode AC algorithm.



## Ⅰ. Integration method

#### 1.1 aar file import
- Add it to build.gradle under the module where sdk needs to be introduced (for the latest version, please check the libs under the module of ppscalelib)
```
dependencies {
//aar introduction
api(name: 'ppblutoothkit-1.0.0-20240312.130313-5', ext: 'aar')
}
```

#### 1.2 Add Bluetooth permissions in the `AndroidManifest.xml` file

During the use of the Demo, you need to turn on Bluetooth and turn on the positioning switch. Make sure to enable and authorize the necessary permissions: For precise positioning permissions and nearby device permissions, you can view the official Bluetooth permissions document. The document address is: [Instructions on Bluetooth permissions on the Google Developer Website] (https://developer.android.com/guide/topics/connectivity/bluetooth/permissions).

- Accurate positioning permissions
- Nearby device permissions
- Position switch
- Bluetooth switch

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


## Ⅱ .Instructions for use

#### 1.1 Operating environment

Due to the need for Bluetooth connection, the Demo needs to be run on a real machine and supports iOS9.0 and above systems.

### 1.2 Conventions related to measuring body data

#### 1.2.1 Precautions for weighing and fat measurement

- The scale supports fat measurement
- Weigh on your bare feet and touch the corresponding electrode pads
- The weighing interface returns weight (kg) and impedance information
- Human body parameters height and age are entered correctly

#### 1.2.2 Body fat calculation

##### Basic parameter agreement

| Category | Input Range | Unit |
|:----|:--------|:--:|
| Height | 100-220 | cm |
| Age | 10-99 | Years |
| Gender | 0/1 | Female/Male |
| Weight | 10-200 | kg |

- Height, age, gender and corresponding impedance are required, and the corresponding calculation library is called to obtain them.
- The body fat data items involved in 8-electrode require an 8-electrode scale to be used.

## Ⅲ. Calculate body fat - calculate - CalcuteInfoViewController

### 1.1 Description of parameters required for body fat calculation

Based on the weight and impedance parsed by the Bluetooth protocol, plus the height, age, and gender of the user data, multiple body fat parameter information such as body fat rate is calculated.

#### 1.1.1 AKBluetoothScaleBaseModel

| Parameters | Comments | Description |
| :-------- | :----- | :----: |
| weight | weight | actual weight * rounded to 100 |
|impedance|4-electrode algorithm impedance (encryption) |4-electrode algorithm field|
| isHeartRating| Whether heart rate is being measured |Heart rate measurement status|
| unit| The current unit of the scale |Real-time unit|
| heartRate| Heart rate |The scale supports heart rate validation|
| dataType| Data class|AKScaleDataTypeStable = 0, // Stable weight data, AKScaleDataTypeDynamic = 1, // Dynamic weight data, AKScaleDataTypeOverweight = 2, // Overweight weight data, AKScaleDataTypeFat = 3, // with body fat percentage Lock weight, AKScaleDataTypePetAndBaby = 4, // Stable weight data in pet mode/baby mode |


#### 1.1.2 Basic user information description AKBluetoothDeviceSettingModel

| Parameters | Comments | Description |
| :-------- | :----- | :----: |
| height| height|all body fat scales|
| age| age |all body fat scales|
| gender| Gender |All body fat scales|


### 1.3 Four-electrode AC body fat calculation - 4AC - CalcuelateResultViewController

**Four-electrode AC body fat calculation example:**

```
// Calculation result class: AKBodyFatModel

var fatModel:AKBodyFatModel!
        
         fatModel = AKBodyFatModel(userModel: userModel,
                                    deviceCalcuteType: PPDeviceCalcuteType.alternateNormal,
                                    deviceMac: mac,
                                    weight: weight,
                                    heartRate: heartRate,
                                    andImpedance: impedance)
                                       
// fatModel is the calculated result
if (fatModel.errorType == .ERROR_TYPE_NONE) {

print("\(fatModel.description)")
} else {

print("errorType:\(fatModel.errorType)")
}
```

## Ⅳ. Device scanning - Device-SearchDeviceViewController

### 1.2 Scan surrounding supported devices-SearchDeviceViewController

`AKBluetoothConnectManager` is the core class for device scanning and connection. It mainly implements the following functions:

- Bluetooth status monitoring
- Scan for supported Bluetooth devices in the surrounding area
- Connect to designated Bluetooth devices
- Disconnect the specified device
- Stop scanning

```
@interface AKBluetoothConnectManager : NSObject

// Bluetooth status proxy
@property (nonatomic, weak) id<AKBluetoothUpdateStateDelegate> updateStateDelegate;

//Search for device agents
@property (nonatomic, weak) id<AKBluetoothSurroundDeviceDelegate> surroundDeviceDelegate;

//Connect device agent
@property (nonatomic, weak) id<AKBluetoothConnectDelegate> connectDelegate;

@property (nonatomic, weak) id<AKBluetoothScaleDataDelegate> scaleDataDelegate;

// Search for peripheral supported devices
- (void)searchSurroundDevice;

//Connect to the specified device
- (void)connectPeripheral:(CBPeripheral *)peripheral withDevice:(AKBluetoothAdvDeviceModel *)device;

// Stop searching for Bluetooth devices
- (void)stopSearch;

// Disconnect the specified Bluetooth device
- (void)disconnect:(CBPeripheral *)peripheral;

@end
```

#### 1.2.1 Create AKBluetoothConnectManager instance

```
//Create an AKBluetoothConnectManager instance and set the proxy
let scaleManager:AKBluetoothConnectManager = AKBluetoothConnectManager()
self.scaleManager.updateStateDelegate = self;
self.scaleManager.surroundDeviceDelegate = self;
```

## Ⅵ. Entity class objects and specific parameter descriptions

### 1.1 AKBodyFatModel body fat calculation object parameter description

24 items of data corresponding to four electrodes


| Parameters | Parameter type | Description | Data type | Remarks
|------|--------|--------|--------|--------|
|ppBodyBaseModel| AKBluetoothDeviceSettingModel |Input parameters for body fat calculation|Basic input parameters|Contains device information, user basic information, weight and heart rate|Body fat scale
|ppSDKVersion| String |Computation library version number|Return parameters|
|ppSex| PPUserGender|Gender|Return parameters| PPUserGenderFemale female PPUserGenderMale male
|ppHeightCm|Int |Height|Return parameters|cm
|ppAge|Int |Age|Return Parameters|Years
|errorType|BodyFatErrorType |Error type|Return parameters|PP_ERROR_TYPE_NONE(0), no error PP_ERROR_TYPE_AGE(1), age error PP_ERROR_TYPE_HEIGHT(2), height error PP_ERROR_TYPE_WEIGHT(3), weight error PP_ERROR_TYPE_SEX(4) gender error PP_ERROR_TYPE_PEOPLE_TYPE (5) The following impedance is incorrect PP_ERROR_TYPE_IMPEDANCE_TWO_LEGS(6) PP_ERROR_TYPE_IMPEDANCE_TWO_ARMS(7)PP_ERROR_TYPE_IMPEDANCE_LEFT_BODY(8) PP_ERROR_TYPE_IMPEDANCE_RIGHT_ARM(9)PP_ERROR_TYPE_IMPEDANCE_LEFT_ARM(10) PP_ER ROR_TYPE_IMPEDANCE_LEFT_LEG(11) PP_ERROR_TYPE_IMPEDANCE_RIGHT_LEG(12) PP_ERROR_TYPE_IMPEDANCE_TRUNK(13)
|bodyDetailModel|PPBodyDetailModel|Data interval range and introduction description|
|ppWeightKg|Float |Weight|24&48|kg
|ppBMI|Float|Body Mass Index|24&48|
|ppFat|Float |Fat rate|24&48|%
|ppBodyfatKg|Float |Fat mass|24&48|kg
|ppMusclePercentage|Float |Muscle Percentage|24&48|%
|ppMuscleKg|Float |Muscle mass|24&48|kg
|ppBodySkeletal|Float |Skeletal muscle rate|24&48|%
|ppBodySkeletalKg|Float |Skeletal muscle mass|24&48|kg
|ppWaterPercentage|Float |Moisture percentage|24&48|%
|ppWaterKg|Float |Moisture content|24&48|kg
|ppProteinPercentage|Float |Protein rate|24&48|%
|ppProteinKg|Float |Protein mass|24&48|kg
|ppLoseFatWeightKg|Float |Lean body mass|24&48|kg
|ppBodyFatSubCutPercentage|Float |Subcutaneous fat rate|24&48|%
|ppBodyFatSubCutKg|Float |Subcutaneous fat mass|24&48|kg
|ppHeartRate|Int |Heart rate|24&48|bmp This value is related to the scale, and is valid if it is greater than 0
|ppBMR|Int |Basal Metabolism|24&48|
|ppVisceralFat|Int |Visceral fat level|24&48|
|ppBoneKg|Float |Bone mass|24&48|kg
|ppBodyMuscleControl|Float |Muscle control volume|24&48|kg
|ppFatControlKg|Float |Fat control volume|24&48|kg
|ppBodyStandardWeightKg|Float |Standard weight|24&48|kg
|ppIdealWeightKg|Float |Ideal Weight|24&48|kg
|ppControlWeightKg|Float |Control weight|24&48|kg
|ppBodyType|PPBodyDetailType |Body type|24&48|PPBodyDetailType has a separate description
|ppFatGrade|PPBodyFatGrade|Obesity grade|24&48|PPBodyGradeFatThin(0), //!< Thin PPBodyGradeFatStandard(1),//!< Standard PPBodyGradeFatOverwight(2), //!< Overweight PPBodyGradeFatOne(3),//!< Obesity level 1PPBodyGradeFatTwo(4),//!< Obesity level 2PPBodyGradeFatThree(5);//!< Obesity level 3
|ppBodyHealth|PPBodyHealthAssessment |Health Assessment|24&48|PPBodyAssessment1(0), //!< Hidden health risks PPBodyAssessment2(1), //!< Sub-health PPBodyAssessment3(2), //!< General PPBodyAssessment4(3), // !< GoodPPBodyAssessment5(4); //!< Very good
|ppBodyAge|Int|Body Age|24&48|Years
|ppBodyScore|Int |Body Score|24&48|Points


Note: To get the object when using it, please call the corresponding attribute to get the corresponding value.

### 1.2 Body type-PPBodyDetailType

| Parameters | Description | type |
|------|--------|--------|
|PPBodyTypeThin|Thin type|0|
|PPBodyTypeThinMuscle|Thin Muscle|1|
|PPBodyTypeMuscular|Muscular|2|
|PPBodyTypeLackofexercise|Lack of exercise|3|
|PPBodyTypeStandard|Standard type|4|
|PPBodyTypeStandardMuscle|Standard muscle type|5|
|PPBodyTypeObesFat|Puffy and obese type|6|
|PPBodyTypeFatMuscle|Fat muscular type|7|
|PPBodyTypeMuscleFat|Muscle type fat|8|

### 1.3 Device object-AKBluetoothAdvDeviceModel

|Attribute name |Type |Description |Remarks|
| ------ | ---- | ---- | ---- |
| deviceMac | String | device mac|device unique identifier|
| deviceName | String | Device Bluetooth name | Device name identification |
| rssi | Int | Bluetooth signal strength |


### 1.4 Device unit-PPDeviceUnit

| enumeration type | type | unit |
| -- | ---- |---- |
|PPUnitKG| 0 | KG |
|PPUnitLB| 1 | LB |
|PPUnitSTLB| 2 | ST_LB |
|PPUnitJin| 3 | Jin |
|PPUnitG| 4 | g |
|PPUnitLBOZ| 5 | lb:oz |
|PPUnitOZ| 6 | oz |
|PPUnitMLWater| 7 | ml (water) |
|PPUnitMLMilk| 8 | ml (milk) |
|PPUnitFLOZWater| 9 | fl_oz (water) |
|PPUnitFLOZMilk| 10 | fl_oz (milk) |
|PPUnitST| 11 | ST |
