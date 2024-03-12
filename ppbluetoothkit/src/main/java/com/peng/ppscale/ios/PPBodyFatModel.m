//
//  PPBodyFatModel.m
//  PPBlueToothDemo
//
//  Created by 彭思远 on 2020/7/31.
//  Copyright © 2020 彭思远. All rights reserved.
//

#import "PPBodyFatModel.h"
#import "BhTwoLegs140.h"
#import "BhBody270.h"
#import "BhTwoArms140.h"
#import "abyonCalculate.h"
#import "PPBluetoothDeviceSettingModel.h"
#import "PPBluetoothAdvDeviceModel.h"
#import "PPAstonExtension.h"
#import "PPBodyDetailStandardArray.h"
#import "Bh1Body270.h"
#import "Bh1TwoArms140.h"
#import "PPLog.h"

@interface PPBodyFatModel()

@property (nonatomic,strong) PPBodyDetailStandardArray *standardArray;

@property (nonatomic, strong) Bh1Body270 *body270;

@property (nonatomic, strong) BhTwoLegs140 *bodyLegs140;

@property (nonatomic, strong) Bh1TwoArms140 *bodyArms140;

@property (nonatomic, strong) PPPeopleGeneral *bodyLegsD;

@property (nonatomic, assign) PPDeviceCalcuteType calcuteType;

@end


@implementation PPBodyFatModel

#pragma mark - 普通构造方法，用于秤端计算

- (instancetype)initWithUserModel:(PPBluetoothDeviceSettingModel *)userModel
                        andWeight:(CGFloat)weight
{
    
    self = [super init];
    if (self) {
        
        self.standardArray = [[PPBodyDetailStandardArray alloc] initWithHeightCm:userModel.height gender:userModel.gender age:userModel.age weight:weight standardType:PPBodyDetailStandardType4];
        self.calcuteType = PPDeviceCalcuteTypeInScale;
    }
    
    return self;
}
#pragma mark - 8电极
- (instancetype)initWithUserModel:(PPBluetoothDeviceSettingModel *)userModel
                        deviceMac:(NSString *)deviceMac
                           weight:(CGFloat)weight
                        heartRate:(NSInteger)heartRate
                        bhProduct:(NSInteger)bhProduct
              Z20KhzLeftArmEnCode:(NSInteger)z20KhzLeftArmEnCode
             Z20KhzRightArmEnCode:(NSInteger)z20KhzRightArmEnCode
              Z20KhzLeftLegEnCode:(NSInteger)z20KhzLeftLegEnCode
             Z20KhzRightLegEnCode:(NSInteger)z20KhzRightLegEnCode
                Z20KhzTrunkEnCode:(NSInteger)z20KhzTrunkEnCode
             Z100KhzLeftArmEnCode:(NSInteger)z100KhzLeftArmEnCode
            Z100KhzRightArmEnCode:(NSInteger)z100KhzRightArmEnCode
             Z100KhzLeftLegEnCode:(NSInteger)z100KhzLeftLegEnCode
            Z100KhzRightLegEnCode:(NSInteger)z100KhzRightLegEnCode
               Z100KhzTrunkEnCode:(NSInteger)z100KhzTrunkEnCode{
//    PP_Log(@"===============================================");
//
//    PP_Log(@"八电极计算库入参:");
//    PP_Log(@"userModel: %@", userModel);
//    PP_Log(@"deviceMac: %@", deviceMac);
//    PP_Log(@"weight: %f", weight);
//    PP_Log(@"heartRate: %ld", (long)heartRate);
//    PP_Log(@"bhProduct: %ld", (long)bhProduct);
//    PP_Log(@"Z20KhzLeftArmEnCode: %ld", (long)z20KhzLeftArmEnCode);
//    PP_Log(@"Z20KhzRightArmEnCode: %ld", (long)z20KhzRightArmEnCode);
//    PP_Log(@"Z20KhzLeftLegEnCode: %ld", (long)z20KhzLeftLegEnCode);
//    PP_Log(@"Z20KhzRightLegEnCode: %ld", (long)z20KhzRightLegEnCode);
//    PP_Log(@"Z20KhzTrunkEnCode: %ld", (long)z20KhzTrunkEnCode);
//    PP_Log(@"Z100KhzLeftArmEnCode: %ld", (long)z100KhzLeftArmEnCode);
//    PP_Log(@"Z100KhzRightArmEnCode: %ld", (long)z100KhzRightArmEnCode);
//    PP_Log(@"Z100KhzLeftLegEnCode: %ld", (long)z100KhzLeftLegEnCode);
//    PP_Log(@"Z100KhzRightLegEnCode: %ld", (long)z100KhzRightLegEnCode);
//    PP_Log(@"Z100KhzTrunkEnCode: %ld", (long)z100KhzTrunkEnCode);
//
//    PP_Log(@"===============================================");

    self = [super init];
    
    if (self) {
        

        self.ppUnit = userModel.unit;
        
#pragma mark - 8电极下使用双手
        // 8电极设备使用双手测量 z20KhzRightArmEnCode为非0 其他为0
        if (z20KhzRightArmEnCode > 0 && z20KhzLeftArmEnCode + z20KhzRightArmEnCode + z20KhzLeftLegEnCode + z20KhzRightLegEnCode + z20KhzTrunkEnCode + z100KhzLeftArmEnCode + z100KhzRightArmEnCode + z100KhzLeftLegEnCode + z100KhzRightLegEnCode + z100KhzTrunkEnCode == z20KhzRightArmEnCode){
            
            self.standardArray = [[PPBodyDetailStandardArray alloc] initWithHeightCm:userModel.height gender:userModel.gender age:userModel.age weight:weight standardType:PPBodyDetailStandardType4];
            self.calcuteType = PPDeviceCalcuteTypeAlternate;
            
            Bh1TwoArms140 *peopleModel = [[Bh1TwoArms140 alloc] init];
            BhSexType tsex = (BhSexType)userModel.gender;
            peopleModel.bhWeightKg = weight;
            peopleModel.bhPeopleType =  userModel.isAthleteMode;
            peopleModel.bhHeightCm = (int)userModel.height;
            peopleModel.bhSex = tsex;
            peopleModel.bhAge = (int)userModel.age;
            peopleModel.bhZTwoArmsEnCode = (int)z20KhzRightArmEnCode;
            
            BhErrorType errorType = [peopleModel getBhBodyComposition];
            self.errorType = [self bhErrorType2PPBodyfatErrorType:errorType];
            self.bodyArms140= peopleModel;

            self.ppSex = userModel.gender;
            self.ppHeightCm = userModel.height;
            self.ppWeightKg = weight;
            self.ppAge = userModel.age;
            self.ppDeviceMac = deviceMac;
            
            self.ppZTwoLegsEnCode = z20KhzRightArmEnCode;
        
            self.ppSDKVersion = [NSString stringWithFormat:@"HT_4_Arm_%@",peopleModel.bhVersionTime];
            self.ppBMI =  peopleModel.bhBMI < 10 ? 10: peopleModel.bhBMI;

     
            if (self.errorType == PP_ERROR_TYPE_NONE) {
                

                CGFloat difference = [self calculateBodyFatDiff:peopleModel.bhBodyFatRate withGender:userModel.gender andNeeds:YES];
                self.ppFat = peopleModel.bhBodyFatRate - difference < 5.0 ? 5.0:peopleModel.bhBodyFatRate - difference;

                self.ppMuscleKg =  peopleModel.bhMuscleKg;
                
                self.ppBMR = peopleModel.bhBMR;

                self.ppWaterPercentage = peopleModel.bhWaterRate;
                
                self.ppWaterKg = peopleModel.bhWaterRate / 100.0 * weight;

                self.ppHeartRate = heartRate;
                
                //涉及调整体脂率 bhBodyFatKg
                self.ppBodyfatKg = self.ppFat * 0.01 * weight;

                self.ppProteinPercentage = peopleModel.bhProteinRate;
                
                self.ppProteinKg = peopleModel.bhProteinRate / 100.0 * weight;

                //涉及调整体脂率 bhBodyFatFreeMassKg
                self.ppLoseFatWeightKg = weight - self.ppBodyfatKg;
                
                //涉及调整体脂率 bhBodyFatSubCutRate
                self.ppBodyFatSubCutPercentage = self.ppFat * 2 / 3;
                
                self.ppBodyFatSubCutKg = self.ppFat * 2 / 3 / 100.0 * weight;;

                self.ppBodySkeletal = peopleModel.bhSkeletalMuscleKg / weight * 100.0;
                
                self.ppBodySkeletalKg = peopleModel.bhSkeletalMuscleKg;
                
                self.ppVisceralFat = (peopleModel.bhVFAL == 0) ? 1 : peopleModel.bhVFAL;

                self.ppMusclePercentage = peopleModel.bhMuscleRate;

                self.ppBodyMuscleControl = [self calculateBodyMuscleControl:userModel.gender height:userModel.height muscleKg:peopleModel.bhMuscleKg standardWeight:peopleModel.bhIdealWeightKg];
                
                self.ppFatControlKg =  [self ppFatControlKgWithBodyfatKg:self.ppBodyfatKg sex:userModel.gender bmi:peopleModel.bhBMI andAge:userModel.age];
                
                self.ppBodyStandardWeightKg = peopleModel.bhIdealWeightKg;

                self.ppControlWeightKg = [self calculateControlWeightKg:weight standardWeight:peopleModel.bhIdealWeightKg];

                self.ppBoneKg = peopleModel.bhBoneKg;
                
                self.ppBodyType = [self bodyDetailTypeWithFatRate:self.ppFat standardFatRateList:self.ppFatList musKg:self.ppMuscleKg andMusKgList:self.ppMuscleKgList];
                
                self.ppFatGrade = [self BodyFatGrade:self.ppBMI];
                
                self.ppBodyHealth = [self HealthScore:peopleModel.bhBodyScore];

                self.ppBodyAge = [self calcuteBodyAge:userModel.age];

                self.ppBodyScore = peopleModel.bhBodyScore;

            }
            else{
                
                self.ppBMI = [self calculateBMIWithHeight:userModel.height andWeight:weight];
            }
            
            
        }else{
#pragma mark - 8电极下使用双手双脚
            
            
            self.standardArray = [[PPBodyDetailStandardArray alloc] initWithHeightCm:userModel.height gender:userModel.gender age:userModel.age weight:weight standardType:PPBodyDetailStandardType8];
            self.calcuteType = PPDeviceCalcuteTypeAlternate8;


            Bh1Body270 *peopleModel = [[Bh1Body270 alloc] init];
            BhSexType tsex = (BhSexType)userModel.gender;
            peopleModel.bhSex = tsex;
            peopleModel.bhAge = (int)userModel.age;
            peopleModel.bhHeightCm = (int)userModel.height;
            peopleModel.bhWeightKg = weight;
            peopleModel.bhZ20KhzLeftArmEnCode = (int)z20KhzLeftArmEnCode;
            peopleModel.bhZ20KhzRightArmEnCode = (int)z20KhzRightArmEnCode;
            peopleModel.bhZ20KhzLeftLegEnCode = (int)z20KhzLeftLegEnCode;
            peopleModel.bhZ20KhzRightLegEnCode = (int)z20KhzRightLegEnCode;
            peopleModel.bhZ20KhzTrunkEnCode = (int)z20KhzTrunkEnCode;
            peopleModel.bhZ100KhzLeftArmEnCode = (int)z100KhzLeftArmEnCode;
            peopleModel.bhZ100KhzRightArmEnCode = (int)z100KhzRightArmEnCode;
            peopleModel.bhZ100KhzLeftLegEnCode = (int)z100KhzLeftLegEnCode;
            peopleModel.bhZ100KhzRightLegEnCode = (int)z100KhzRightLegEnCode;
            peopleModel.bhZ100KhzTrunkEnCode = (int)z100KhzTrunkEnCode;
            peopleModel.bhProduct = (int)bhProduct; //CF577 是 1， CF597是 0
            peopleModel.bhTrimming = 1;
            peopleModel.bhIsHome = 0;
            peopleModel.bhIsEnhancedRepeat = 0;
            
            BhErrorType errorType = [peopleModel getBhBodyComposition];
            self.errorType = [self bhErrorType2PPBodyfatErrorType:errorType];
            self.body270 = peopleModel;
            
            self.ppSex = userModel.gender;
            self.ppHeightCm = userModel.height;
            self.ppWeightKg = weight;
            self.ppAge = userModel.age;
            self.ppDeviceMac = deviceMac;
            
            self.ppZ100KhzLeftArmEnCode = z100KhzLeftArmEnCode;
            self.ppZ100KhzLeftLegEnCode = z100KhzLeftLegEnCode;
            self.ppZ100KhzRightArmEnCode = z100KhzRightArmEnCode;
            self.ppZ100KhzRightLegEnCode = z100KhzRightLegEnCode;
            self.ppZ100KhzTrunkEnCode = z100KhzTrunkEnCode;
            self.ppZ20KhzLeftArmEnCode = z20KhzLeftArmEnCode;
            self.ppZ20KhzLeftLegEnCode = z20KhzLeftLegEnCode;
            self.ppZ20KhzRightArmEnCode = z20KhzRightArmEnCode;
            self.ppZ20KhzRightLegEnCode = z20KhzRightLegEnCode;
            self.ppZ20KhzTrunkEnCode = z20KhzTrunkEnCode;

            self.ppZ100KhzLeftArmDeCode = peopleModel.bhZ100KhzLeftArmDeCode;
            self.ppZ100KhzLeftLegDeCode = peopleModel.bhZ100KhzLeftLegDeCode;
            self.ppZ100KhzRightArmDeCode = peopleModel.bhZ100KhzRightArmDeCode;
            self.ppZ100KhzRightLegDeCode = peopleModel.bhZ100KhzRightLegDeCode;
            self.ppZ100KhzTrunkDeCode = peopleModel.bhZ100KhzTrunkDeCode;
            self.ppZ20KhzLeftArmDeCode = peopleModel.bhZ20KhzLeftArmDeCode;
            self.ppZ20KhzLeftLegDeCode = peopleModel.bhZ20KhzLeftLegDeCode;
            self.ppZ20KhzRightArmDeCode = peopleModel.bhZ20KhzRightArmDeCode;
            self.ppZ20KhzRightLegDeCode = peopleModel.bhZ20KhzRightLegDeCode;
            self.ppZ20KhzTrunkDeCode = peopleModel.bhZ20KhzTrunkDeCode;
            
            self.ppSDKVersion = [NSString stringWithFormat:@"HT_8_%@",peopleModel.bhVersionTime];
            self.ppBMI =  peopleModel.bhBMI < 10 ? 10: peopleModel.bhBMI;

            if (self.errorType == PP_ERROR_TYPE_NONE) {

                self.ppFat = peopleModel.bhBodyFatRate;

                self.ppMuscleKg =  peopleModel.bhMuscleKg;

                self.ppBMR = peopleModel.bhBMR;

                self.ppWaterPercentage = peopleModel.bhWaterRate;

                self.ppWaterKg = peopleModel.bhWaterKg;
                
                self.ppHeartRate = heartRate;

                self.ppBodyfatKg = peopleModel.bhBodyFatKg;
                
                self.ppProteinPercentage = peopleModel.bhProteinRate;

                self.ppProteinKg = peopleModel.bhProteinKg;

                self.ppLoseFatWeightKg = peopleModel.bhBodyFatFreeMassKg;
                
                self.ppBodyFatSubCutPercentage = peopleModel.bhBodyFatSubCutRate;
                
                self.ppBodyFatSubCutKg = peopleModel.bhBodyFatSubCutKg;
                
                self.ppBodySkeletal = peopleModel.bhSkeletalMuscleRate;

                self.ppBodySkeletalKg = peopleModel.bhSkeletalMuscleKg;

                self.ppVisceralFat = (peopleModel.bhVFAL == 0)? 1 : peopleModel.bhVFAL;

                self.ppMusclePercentage = peopleModel.bhMuscleRate;

                self.ppBodyMuscleControl = peopleModel.bhMuscleKgCon;

                self.ppFatControlKg =  peopleModel.bhBodyFatKgCon;

                self.ppBodyStandardWeightKg = peopleModel.bhIdealWeightKg;

                self.ppControlWeightKg = peopleModel.bhWeightKgCon;

                self.ppBoneKg = peopleModel.bhBoneKg;
                
                self.ppBodyType = [self bhBodyType2PPBodyDetailType:peopleModel.bhBodyType];

                self.ppFatGrade = [self BodyFatGrade:self.ppBMI];
                
                self.ppBodyHealth = [self HealthScore:peopleModel.bhBodyScore];

                self.ppBodyAge = [self calcuteBodyAge:userModel.age];

                self.ppBodyScore = peopleModel.bhBodyScore;
                
                // 8电极特有数据
                
                self.ppBodyFatSubCutKg = peopleModel.bhBodyFatSubCutKg;
                self.ppCellMassKg = peopleModel.bhCellMassKg;
                self.ppDCI = peopleModel.bhDCI;
                self.ppMineralKg = peopleModel.bhMineralKg;
                self.ppObesity = peopleModel.bhObesity;
                self.ppWaterECWKg = peopleModel.bhWaterECWKg;
                self.ppWaterICWKg = peopleModel.bhWaterICWKg;
                self.ppBodyFatKgLeftArm = peopleModel.bhBodyFatKgLeftArm;
                self.ppBodyFatKgLeftLeg = peopleModel.bhBodyFatKgLeftLeg;
                self.ppBodyFatKgRightArm = peopleModel.bhBodyFatKgRightArm;
                self.ppBodyFatKgRightLeg = peopleModel.bhBodyFatKgRightLeg;
                self.ppBodyFatKgTrunk = peopleModel.bhBodyFatKgTrunk;
                self.ppBodyFatRateLeftArm = peopleModel.bhBodyFatRateLeftArm;
                self.ppBodyFatRateLeftLeg = peopleModel.bhBodyFatRateLeftLeg;
                self.ppBodyFatRateRightArm = peopleModel.bhBodyFatRateRightArm;
                self.ppBodyFatRateRightLeg = peopleModel.bhBodyFatRateRightLeg;
                self.ppBodyFatRateTrunk = peopleModel.bhBodyFatRateTrunk;
                self.ppMuscleKgLeftArm = peopleModel.bhMuscleKgLeftArm;
                self.ppMuscleKgLeftLeg = peopleModel.bhMuscleKgLeftLeg;
                self.ppMuscleKgRightArm = peopleModel.bhMuscleKgRightArm;
                self.ppMuscleKgRightLeg = peopleModel.bhMuscleKgRightLeg;
                self.ppMuscleKgTrunk = peopleModel.bhMuscleKgTrunk;
                self.ppMuscleRateLeftArm = peopleModel.bhMuscleRateLeftArm;
                self.ppMuscleRateLeftLeg = peopleModel.bhMuscleRateLeftLeg;
                self.ppMuscleRateRightArm = peopleModel.bhMuscleRateRightArm;
                self.ppMuscleRateRightLeg = peopleModel.bhMuscleRateRightLeg;
                self.ppMuscleRateTrunk = peopleModel.bhMuscleRateTrunk;
                self.ppSmi = peopleModel.bhSmi;
                self.ppWHR = peopleModel.bhWHR;
                
            }else{
                self.ppBMI = [self calculateBMIWithHeight:userModel.height andWeight:weight];
            }
            
        }

    }
//    NSString *result = [NSString stringWithFormat:@"%@", self];
//    PP_Log(@"===============================================");
//    PP_Log(@"八电极计算库数据结果:\n%@",result);
//    PP_Log(@"===============================================");

    return self;
}


#pragma mark - 四电极
- (instancetype)initWithUserModel:(PPBluetoothDeviceSettingModel *)userModel
                deviceCalcuteType:(PPDeviceCalcuteType)deviceCalcuteType
                        deviceMac:(NSString *)deviceMac
                           weight:(CGFloat)weight
                        heartRate:(NSInteger)heartRate
                     andImpedance:(NSInteger)impedance{
    
//    PP_Log(@"===============================================");
//
//    PP_Log(@"四电极计算库入参:");
//    PP_Log(@"userModel: %@", userModel);
//    PP_Log(@"deviceCalcuteType: %ld", (long)deviceCalcuteType);
//    PP_Log(@"deviceMac: %@", deviceMac);
//    PP_Log(@"weight: %f", weight);
//    PP_Log(@"heartRate: %ld", (long)heartRate);
//    PP_Log(@"impedance: %ld", (long)impedance);
//    PP_Log(@"===============================================");

    self = [super init];
    if (self) {
        self.calcuteType = deviceCalcuteType;

        self.ppUnit = userModel.unit;
        self.standardArray = [[PPBodyDetailStandardArray alloc] initWithHeightCm:userModel.height gender:userModel.gender age:userModel.age weight:weight standardType:PPBodyDetailStandardType4];

        if (deviceCalcuteType == PPDeviceCalcuteTypeAlternate || deviceCalcuteType == PPDeviceCalcuteTypeAlternateNormal) {
#pragma mark - 四电极合泰交流
            BhTwoLegs140 *peopleModel = [[BhTwoLegs140 alloc] init];
            BhSexType tsex = (BhSexType)userModel.gender;
            peopleModel.bhWeightKg = weight;
            peopleModel.bhPeopleType =  userModel.isAthleteMode;
            peopleModel.bhHeightCm = (int)userModel.height;
            peopleModel.bhSex = tsex;
            peopleModel.bhAge = (int)userModel.age;
            peopleModel.bhZTwoLegsEnCode = (int)impedance;
            
            BhErrorType errorType = [peopleModel getBhBodyComposition];
            self.errorType = [self bhErrorType2PPBodyfatErrorType:errorType];
            self.bodyLegs140 = peopleModel;

            self.ppSex = userModel.gender;
            self.ppHeightCm = userModel.height;
            self.ppWeightKg = weight;
            self.ppAge = userModel.age;
            self.ppDeviceMac = deviceMac;
            
            self.ppZTwoLegsEnCode = impedance;
            self.ppZTwoLegsDeCode = peopleModel.bhZTwoLegsDeCode;
            self.ppSDKVersion = [NSString stringWithFormat:@"HT_4_Leg_%@",peopleModel.bhVersionTime];

            self.ppBMI =  peopleModel.bhBMI < 10 ? 10: peopleModel.bhBMI;


            if (self.errorType == PP_ERROR_TYPE_NONE) {
                
                CGFloat difference = [self calculateBodyFatDiff:peopleModel.bhBodyFatRate withGender:userModel.gender andNeeds:deviceCalcuteType == PPDeviceCalcuteTypeAlternateNormal ? NO:YES];
                self.ppFat = peopleModel.bhBodyFatRate - difference < 5.0 ? 5.0:peopleModel.bhBodyFatRate - difference;


                self.ppMuscleKg =  peopleModel.bhMuscleKg;
                
                self.ppBMR = peopleModel.bhBMR;

                self.ppWaterPercentage = peopleModel.bhWaterRate;
                
                self.ppWaterKg = peopleModel.bhWaterRate / 100.0 * weight;

                self.ppHeartRate = heartRate;
                
                //涉及调整体脂率 bhBodyFatKg
                self.ppBodyfatKg = self.ppFat * 0.01 * weight;

                self.ppProteinPercentage = peopleModel.bhProteinRate;
                
                self.ppProteinKg = peopleModel.bhProteinRate / 100.0 * weight;

                //涉及调整体脂率 bhBodyFatFreeMassKg
                self.ppLoseFatWeightKg = weight - self.ppBodyfatKg;
                
                //涉及调整体脂率 bhBodyFatSubCutRate
                self.ppBodyFatSubCutPercentage = self.ppFat * 2 / 3;
                
                self.ppBodyFatSubCutKg = self.ppFat * 2 / 3 / 100.0 * weight;;

                self.ppBodySkeletal = peopleModel.bhSkeletalMuscleKg / weight * 100.0;
                
                self.ppBodySkeletalKg = peopleModel.bhSkeletalMuscleKg;
                
                self.ppVisceralFat = peopleModel.bhVFAL;

                self.ppMusclePercentage = peopleModel.bhMuscleRate;

                self.ppBodyMuscleControl = [self calculateBodyMuscleControl:userModel.gender height:userModel.height muscleKg:peopleModel.bhMuscleKg standardWeight:peopleModel.bhIdealWeightKg];

                self.ppFatControlKg =  [self ppFatControlKgWithBodyfatKg:self.ppBodyfatKg sex:userModel.gender bmi:peopleModel.bhBMI andAge:userModel.age];

                self.ppBodyStandardWeightKg = peopleModel.bhIdealWeightKg;

                self.ppControlWeightKg = [self calculateControlWeightKg:weight standardWeight:peopleModel.bhIdealWeightKg];

                self.ppBoneKg = peopleModel.bhBoneKg;
                
                self.ppBodyType = [self bodyDetailTypeWithFatRate:self.ppFat standardFatRateList:self.ppFatList musKg:self.ppMuscleKg andMusKgList:self.ppMuscleKgList];

                self.ppFatGrade = [self BodyFatGrade:self.ppBMI];
                
                self.ppBodyHealth = [self HealthScore:peopleModel.bhBodyScore];

                self.ppBodyAge = [self calcuteBodyAge:userModel.age];

                self.ppBodyScore = peopleModel.bhBodyScore;
               
            }
            else{
                
                self.ppBMI = [self calculateBMIWithHeight:userModel.height andWeight:weight];

            }
            
        }
        
        else if (deviceCalcuteType == PPDeviceCalcuteTypeDirect) {
#pragma mark - 四电极乐福直流
            PPPeopleGeneral *peopleModel = [[PPPeopleGeneral alloc] init];
            PPDeviceGenderType gender = userModel.gender;
            PBodyfatErrorType errorType = [peopleModel getBodyfatWithweightKg:weight heightCm:userModel.height sex:gender age:userModel.age impedance:impedance sportModel:userModel.isAthleteMode];
            
            self.errorType = [self pBodyfatErrorType2PPBodyfatErrorType:errorType];
            self.bodyLegsD = peopleModel;
            
            self.ppSex = userModel.gender;
            self.ppHeightCm = userModel.height;
            self.ppWeightKg = weight;
            self.ppAge = userModel.age;
            self.ppDeviceMac = deviceMac;
            self.ppZTwoLegsEnCode = impedance;
            
            //8电极     HT_8_BHLefu_2023-05-15_V1.5.5
            //4电极     HT_4_BHLefu_2023-05-15_V5.0.1
            //4电极直流  LF_4_ZLefu_2023-05-15_V1.0.0
            self.ppSDKVersion = @"LF_4_Z_Lefu_2023-05-15_V1.0.0";
            self.ppBMI =  peopleModel.htBMI < 10 ? 10: peopleModel.htBMI;


            if (self.errorType == PP_ERROR_TYPE_NONE) {
                
                self.ppFat = peopleModel.htBodyfatPercentage;
            

                self.ppMuscleKg =  ((int)(peopleModel.htMuscleKg*100+5))/100.0;
                
                self.ppBMR = peopleModel.htBMR;
                
                self.ppWaterPercentage = peopleModel.htWaterPercentage;
                
                self.ppWaterKg = peopleModel.htWaterPercentage / 100.0 * weight;
                
                self.ppHeartRate = heartRate;
                
                self.ppBodyfatKg = self.ppFat * 0.01 * weight;

                self.ppProteinPercentage = peopleModel.htproteinPercentage;
                
                self.ppProteinKg = peopleModel.htproteinPercentage / 100.0 * weight;
                
                self.ppLoseFatWeightKg = weight - self.ppBodyfatKg;

                self.ppBodyFatSubCutPercentage = peopleModel.htSUBFAT;
                
                self.ppBodyFatSubCutKg = peopleModel.htSUBFAT / 100.0 * weight;

                self.ppBodySkeletal = peopleModel.htSKELETAL;
                
                self.ppBodySkeletalKg = peopleModel.htSKELETAL / 100.0 * weight;

                self.ppVisceralFat = peopleModel.htVFAL;
                
                self.ppMusclePercentage = peopleModel.htMusclePercentage;
                
                self.ppBodyMuscleControl = [self calculateBodyMuscleControl:userModel.gender height:userModel.height muscleKg:peopleModel.htMuscleKg standardWeight:peopleModel.htIdealWeightKg];

                self.ppFatControlKg =  [self ppFatControlKgWithBodyfatKg:self.ppBodyfatKg sex:userModel.gender bmi:peopleModel.htBMI andAge:userModel.age];

                self.ppBodyStandardWeightKg = peopleModel.htIdealWeightKg;

                self.ppControlWeightKg = [self calculateControlWeightKg:weight standardWeight:peopleModel.htIdealWeightKg];

                self.ppBoneKg = peopleModel.htBoneKg;
                
                self.ppBodyType = [self bodyDetailTypeWithFatRate:self.ppFat standardFatRateList:self.ppFatList musKg:self.ppMuscleKg andMusKgList:self.ppMuscleKgList];

                self.ppFatGrade = [self BodyFatGrade:self.ppBMI];
                
                self.ppBodyHealth = [self HealthScore:peopleModel.htBodyScore];

                self.ppBodyAge =  peopleModel.htBodyAge;

                self.ppBodyScore = peopleModel.htBodyScore;
            
            }else{
                
                self.ppBMI = [self calculateBMIWithHeight:userModel.height andWeight:weight];
            }
        }
        
        
       else if ( deviceCalcuteType == PPDeviceCalcuteTypeNeedNot) {
#pragma mark - 四电极无需计算

            self.ppWeightKg = weight;
            self.ppBMI = [self calculateBMIWithHeight:userModel.height andWeight:weight];
       }
        
       else if (deviceCalcuteType == PPDeviceCalcuteTypeInScale){
#pragma mark - 四电极设备端计算

           self.ppWeightKg = weight;
           self.ppBMI = [self calculateBMIWithHeight:userModel.height andWeight:weight];
       }
    
       else {
#pragma mark - 四电极异常处理

            self.ppWeightKg = weight;
            self.ppBMI = [self calculateBMIWithHeight:userModel.height andWeight:weight];
       }
    }
//    NSString *result = [NSString stringWithFormat:@"%@", self];
//    PP_Log(@"===============================================");
//    PP_Log(@"四电极计算库数据结果:\n%@",result);
//    PP_Log(@"===============================================");

    return self;
}

#pragma mark - 计算项指标
#pragma mark -脂肪控制量

- (CGFloat)ppFatControlKgWithBodyfatKg:(CGFloat)bodyfatKg sex:(PPDeviceGenderType)sex bmi:(CGFloat)bmi andAge:(NSInteger)age
{

    CGFloat control;
    //女性
    if (sex == PPDeviceGenderTypeFemale){
        
        float B0 = 67.2037;
        float B1 = 0.6351;
        float B2 = -2.6706;
        float B3 = -18.1146;
        float B4 = -0.2374;
        
        if(bmi <= 21){
            if(bodyfatKg < 10.5 ){
                
                return fabs(10.2 - bodyfatKg);
            }else{
                
                return 0;
            }
        }
        
        control = B0 + B1 * age + B2 * bmi + B3 * age / bmi + B4 * bmi * bmi / age;
    }else{
        
        float B0 = 24.1589;
        float B1 = -0.6282;
        float B2 = -0.5865;
        float B3 = 9.8772;
        float B4 = -0.368;
        if(bmi <= 22.5){
            
            if(bodyfatKg < 8.5 ){
                
                return fabs(9 - bodyfatKg);
            }else{
                
                return 0;

            }
        }
        control = B0 + B1 * age + B2 * bmi + B3 * age / bmi + B4 * bmi * bmi / age;
    }
    
    return fabs(control);
}

#pragma mark -健康评估 @[@(0),@(60),@(70),@(80),@(90),@(100)];

- (PPBodyHealthAssessment)HealthScore:(NSInteger)htBodyScore
{

    if (htBodyScore <= 60) {
        
        return PPBodyAssessment1;
    }else if ( 60 < htBodyScore && htBodyScore <=70){
        
        return PPBodyAssessment2;
    }else if ( 70 < htBodyScore && htBodyScore <= 80){
        
        return PPBodyAssessment3;
    }else if (80 < htBodyScore && htBodyScore <= 90){
        
        return PPBodyAssessment4;
    }else{
        
        return PPBodyAssessment5;
    }
}

#pragma mark -肥胖等级 @[@(0),@(18.5),@(25),@(30),@(35),@(40),@(50)];
- (PPBodyFatGrade)BodyFatGrade :(CGFloat)bmi
{
    
    if ( bmi <= 18.5) {
        
        return PPBodyGradeFatThin;
    }else if (18.5 < bmi && bmi <= 25){
        
        return PPBodyGradeFatStandard;
    }else if (25 < bmi && bmi <= 30){
        
        return PPBodyGradeFatOverwight;
    }else if (30 < bmi && bmi < 35) {
        
        return PPBodyGradeFatOne;
    }else if (35 < bmi && bmi <= 40){
        
        return PPBodyGradeFatTwo;
    }else{
        
        return PPBodyGradeFatThree;
    }
}


#pragma mark -身体年龄
- (NSInteger)calcuteBodyAge:(NSInteger)bodyAge{
    
    CGFloat physicAge = 0;
    NSInteger age = bodyAge;
    CGFloat bmi = self.ppBMI;
    
    if (bmi < 22) {
        
        CGFloat temp = (age - 5) + ((22 - bmi) * (5 / (22 - 18.5f)));
            if (fabs(temp - age) >= 5) {
                
           physicAge = age + 5;
        } else {
            
           physicAge = temp;
        }
        
    } else if (bmi == 22) {
        
        physicAge = age - 5;
        
    } else if (bmi > 22) {
        
        float temp = (age - 5) + ((bmi - 22) * (5 / (24.9f - 22)));
        if (fabs(temp - age) >= 8) {
            
           physicAge = age + 8;
        } else {
            
           physicAge = temp;
        }
    }
    
    return (NSInteger)(physicAge + 0.005);
    
}

#pragma mark -身体类型



- (PPBodyDetailType)bodyDetailTypeWithFatRate:(CGFloat)fat standardFatRateList:(NSArray *)standardFat musKg:(CGFloat)mus andMusKgList:(NSArray *)standardMus{
    
    NSInteger xValue = [PPBodyDetailStandardArray calculateCurrentStandardWithValue:mus andStandardArray:standardMus];

    
    NSInteger yValue = [PPBodyDetailStandardArray calculateCurrentStandardWithValue:fat andStandardArray:standardFat];
    
    
    if (xValue == 0 && yValue == 0 ) {
        
        return PPBodyTypeThin;
    } else if (xValue == 0 && (yValue == 1 || yValue == 2)){
        
        return PPBodyTypeLackofexercise;
    } else if (xValue == 0 && (yValue == 3 || yValue == 4)){
        
        return PPBodyTypeObesFat;
    } else if (xValue == 1 && yValue == 0){
        
        return PPBodyTypeThinMuscle;
    } else if (xValue == 1 && (yValue == 1 || yValue == 2)){
        
        return PPBodyTypeStandard;
    } else if (xValue == 1 && (yValue == 3 || yValue == 4)){
        
        return PPBodyTypeFatMuscle;
        
    } else if (xValue == 2 && yValue == 0){
        
        return PPBodyTypeMuscular;
    } else if (xValue == 2 && (yValue == 1 || yValue == 2)){
        
        return PPBodyTypeStandardMuscle;
    } else if (xValue == 2 && (yValue == 3 || yValue == 4)){
        
        return PPBodyTypeMuscleFat;
    }else{
        
        return PPBodyTypeStandard;
    }

}
#pragma mark -BMI
- (CGFloat)calculateBMIWithHeight:(NSInteger)height andWeight:(CGFloat)weight{
    
    CGFloat heightM = height /100.0;
    CGFloat bmi = weight / pow(heightM, 2);
    CGFloat bmiCeil = (NSInteger)(bmi * 10) / 10.0;
    
    return bmiCeil < 10 ? 10:bmiCeil;
    
}


#pragma mark - 肌肉控制量
- (CGFloat)calculateBodyMuscleControl:(PPDeviceGenderType)gender height:(NSInteger)height muscleKg:(CGFloat)muscleKg standardWeight:(CGFloat)standardWeight{
    
    CGFloat standMuscle = [self calculateStandMuscleWithHeight:height standardWeight:standardWeight andGender:gender];
    
    CGFloat muscleControl = muscleKg - standMuscle;
    
    return fabs(muscleControl);
    
}


- (CGFloat)calculateStandMuscleWithHeight:(NSInteger)height standardWeight:(CGFloat)standardWeight  andGender:(PPDeviceGenderType)gender{
    
    
    if (gender == PPDeviceGenderTypeFemale)//女性
    {
        
        CGFloat standMuscle = standardWeight * 0.724;
        return standMuscle;
    }else{
        
        CGFloat standMuscle = standardWeight * 0.797;
        return standMuscle;

    }
}

#pragma mark - 体重控制量
- (CGFloat)calculateControlWeightKg:(CGFloat)weight standardWeight:(CGFloat)standardWeight{
    
    CGFloat controlWeightKg = weight - standardWeight;

    return fabs(controlWeightKg);
}


#pragma mark - 类型转换映射

- (PPBodyDetailType)bhBodyType2PPBodyDetailType:(BhBodyType)bodyType{
    
    switch (bodyType) {

        case BH_BODY_TYPE_THIN:
            
            return PPBodyTypeThin;

        case BH_BODY_TYPE_THIN_MUSCLE:
            
            return PPBodyTypeThinMuscle;

        case BH_BODY_TYPE_MUSCULAR:
            
            return PPBodyTypeMuscular;

        case BH_BODY_TYPE_OBESE_FAT:
            
            return PPBodyTypeObesFat;

        case BH_BODY_TYPE_FAT_MUSCLE:
            
            return PPBodyTypeFatMuscle;

        case BH_BODY_TYPE_MUSCLE_FAT:
            
            return PPBodyTypeMuscleFat;

        case BH_BODY_TYPE_LACK_EXERCISE:
            
            return PPBodyTypeLackofexercise;

        case BH_BODY_TYPE_STANDARD:
            
            return PPBodyTypeStandard;

        case BH_BODY_TYPE_STANDARD_MUSCLE:
            
            return PPBodyTypeStandardMuscle;
            
    }
    
}

- (PPBodyfatErrorType)pBodyfatErrorType2PPBodyfatErrorType:(PBodyfatErrorType)errorType{
    
    switch (errorType) {
        case PBodyfatErrorTypeNone:
            
            return PP_ERROR_TYPE_NONE;

        case PBodyfatErrorTypeImpedance:
            
            return PP_ERROR_TYPE_IMPEDANCE_TWO_LEGS;

        case PBodyfatErrorTypeAge:
            
            return PP_ERROR_TYPE_AGE;

        case PBodyfatErrorTypeWeight:
            
            return PP_ERROR_TYPE_WEIGHT;

        case PBodyfatErrorTypeHeight:
            
            return PP_ERROR_TYPE_HEIGHT;

        case PBodyfatErrorTypeSex:
            
            return PP_ERROR_TYPE_SEX;

        case PBodyfatErrorTypeSportMan:
            
            return PP_ERROR_TYPE_PEOPLE_TYPE;

    }
}

- (PPBodyfatErrorType)bhErrorType2PPBodyfatErrorType:(BhErrorType)errorType{
    
    switch (errorType) {
        case BH_ERROR_TYPE_NONE:
            
            return PP_ERROR_TYPE_NONE;

        case BH_ERROR_TYPE_AGE:
            
            return PP_ERROR_TYPE_AGE;

        case BH_ERROR_TYPE_HEIGHT:
            return PP_ERROR_TYPE_HEIGHT;

        case BH_ERROR_TYPE_WEIGHT:
            return PP_ERROR_TYPE_WEIGHT;

        case BH_ERROR_TYPE_SEX:
            return PP_ERROR_TYPE_SEX;

        case BH_ERROR_TYPE_PEOPLE_TYPE:
            return PP_ERROR_TYPE_PEOPLE_TYPE;

        case BH_ERROR_TYPE_IMPEDANCE_TWO_LEGS:
            return PP_ERROR_TYPE_IMPEDANCE_TWO_LEGS;

        case BH_ERROR_TYPE_IMPEDANCE_TWO_ARMS:
            return PP_ERROR_TYPE_IMPEDANCE_TWO_ARMS;

        case BH_ERROR_TYPE_IMPEDANCE_LEFT_BODY:
            return PP_ERROR_TYPE_IMPEDANCE_LEFT_BODY;

        case BH_ERROR_TYPE_IMPEDANCE_LEFT_ARM:
            return PP_ERROR_TYPE_IMPEDANCE_LEFT_ARM;

        case BH_ERROR_TYPE_IMPEDANCE_RIGHT_ARM:
            return PP_ERROR_TYPE_IMPEDANCE_RIGHT_ARM;

        case BH_ERROR_TYPE_IMPEDANCE_LEFT_LEG:
            return PP_ERROR_TYPE_IMPEDANCE_LEFT_LEG;

        case BH_ERROR_TYPE_IMPEDANCE_RIGHT_LEG:
            return PP_ERROR_TYPE_IMPEDANCE_RIGHT_LEG;

        case BH_ERROR_TYPE_IMPEDANCE_TRUNK:
            return PP_ERROR_TYPE_IMPEDANCE_TRUNK;

        case BH_ERROR_TYPE_HOME:
            
            return PP_ERROR_TYPE_HOME;
        case BH_ERROR_TYPE_PRODUCT:
            
            return PP_ERROR_TYPE_PRODUCT;
    }

}

#pragma mark - 体脂率下降
- (CGFloat)calculateBodyFatDiff:(CGFloat)fat withGender:(PPDeviceGenderType)tsex andNeeds:(BOOL)isNeed{
    
    CGFloat difference = 0;

    if (isNeed){
        if (tsex == PPDeviceGenderTypeFemale) {
            if (fat < 14) {
                difference = 0;
            }else if (fat < 21){
                difference = 1.0;
            }else if (fat < 25){
                difference = 2.0;
            }else if (fat < 32){
                difference = 3.0;
            }else{
                difference = 4.0;
            }
        }else{
            if (fat < 6) {
                difference = 0;
            }else if (fat < 14){
                difference = 1.0;
            }else if (fat < 18){
                difference = 2.0;
            }else if (fat < 26){
                difference = 3.0;
            }else{
                difference = 4.0;
            }
        }

    }
    return difference;
}


#pragma mark - 格式化输出
- (NSString *)description{
    
    return [self PPAston_JSONString];
}

#pragma mark - 体重
- (NSArray<NSNumber *> *)ppWeightKgList{
    
    if (self.body270){
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_Weight_StandartArray_8:self.body270.bhWeightKgList];
        
        return standardArray;
        
    }else{
        
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_Weight_StandartArray_4];
        
        return standardArray;
    }
 

}


#pragma mark - 体脂量
- (NSArray<NSNumber *> *)ppBodyfatKgList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhBodyFatKgList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_BodyFat_StandartArray_percent_4_A:self.bodyLegs140.bhBodyFatRateList typeWeight:YES];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_BodyFat_StandartArray_percent_4_A:self.bodyArms140.bhBodyFatRateList typeWeight:YES];
    }else{
        
        return [self.standardArray fetchPPBodyParam_BodyFat_StandartArray_4_D_typeWeight:YES];
    }
}

#pragma mark - 体脂率
- (NSArray<NSNumber *> *)ppFatList{
    
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhBodyFatRateList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_BodyFat_StandartArray_percent_4_A:self.bodyLegs140.bhBodyFatRateList typeWeight:NO];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_BodyFat_StandartArray_percent_4_A:self.bodyArms140.bhBodyFatRateList typeWeight:NO];
    }else{
        
        return [self.standardArray fetchPPBodyParam_BodyFat_StandartArray_4_D_typeWeight:NO];
    }
}


#pragma mark - 水分量
- (NSArray<NSNumber *> *)ppWaterKgList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhWaterKgList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_Water_StandartArray_percent_4_A:self.bodyLegs140.bhWaterRateList typeWeight:YES];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_Water_StandartArray_percent_4_A:self.bodyArms140.bhWaterRateList typeWeight:YES];
    }else{
        
        return [self.standardArray fetchPPBodyParam_Water_StandartArray_4_D_typeWeight:YES];
    }
}
#pragma mark - 水分率
- (NSArray<NSNumber *> *)ppWaterPercentageList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhWaterRateList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_Water_StandartArray_percent_4_A:self.bodyLegs140.bhWaterRateList typeWeight:NO];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_Water_StandartArray_percent_4_A:self.bodyArms140.bhWaterRateList typeWeight:NO];
    }else{
        
        return [self.standardArray fetchPPBodyParam_Water_StandartArray_4_D_typeWeight:NO];
    }
    
}

#pragma mark - 肌肉量
- (NSArray<NSNumber *> *)ppMuscleKgList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhMuscleKgList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_Mus_StandartArray_kg_4_A_:self.bodyLegs140.bhMuscleKgList typeWeight:YES];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_Mus_StandartArray_kg_4_A_:self.bodyArms140.bhMuscleKgList typeWeight:YES];
    }else{
        
        return [self.standardArray fetchPPBodyParam_Mus_StandartArray_kg_4_D_typeWeight:YES];
    }
}
#pragma mark - 肌肉率
- (NSArray<NSNumber *> *)ppMusclePercentageList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhMuscleRateList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_Mus_StandartArray_kg_4_A_:self.bodyLegs140.bhMuscleKgList typeWeight:NO];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_Mus_StandartArray_kg_4_A_:self.bodyArms140.bhMuscleKgList typeWeight:NO];
    }else{
        
        return [self.standardArray fetchPPBodyParam_Mus_StandartArray_kg_4_D_typeWeight:NO];
    }

}

#pragma mark - 骨量
- (NSArray<NSNumber *> *)ppBoneKgList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhBoneKgList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_Bone_StandartArray_kg_4_A:self.bodyLegs140.bhBoneKgList ];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_Bone_StandartArray_kg_4_A:self.bodyArms140.bhBoneKgList];
    }else{
        
        return [self.standardArray fetchPPBodyParam_Bone_StandartArray_kg_4_D];
    }
    


}

#pragma mark - BMR
- (NSArray<NSNumber *> *)ppBMRList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhBMRList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_BMR_StandartArray_kcal_4_A:self.bodyLegs140.bhBMRList ];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_BMR_StandartArray_kcal_4_A:self.bodyArms140.bhBMRList];
    }else{
        
        return [self.standardArray fetchPPBodyParam_BMR_StandartArray_kcal_4_D];
    }
    
    
}

#pragma mark - BMI
- (NSArray<NSNumber *> *)ppBMIList{
    

    if (self.body270){
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_BMI_StandartArray_8];
        
        return standardArray;
        
    }else{
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_BMI_StandartArray_4];

        return standardArray;

    }
}

#pragma mark - 内脏脂肪等级
- (NSArray<NSNumber *> *)ppVisceralFatList{
    
    if (self.body270){
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhVFALList];
        
        return standardArray;
        
    }else{
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_VisFat_StandartArray_4];

        return standardArray;

    }
}

#pragma mark - 蛋白质量
- (NSArray<NSNumber *> *)ppProteinKgList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhProteinKgList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_Protein_StandartArray_percent_4_A:self.bodyLegs140.bhProteinRateList typeWeight:YES];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_Protein_StandartArray_percent_4_A:self.bodyArms140.bhProteinRateList typeWeight:YES];
    }else{
        
        return [self.standardArray fetchPPBodyParam_Protein_StandartArray_4_D_typeWeight:YES];
    }
}


#pragma mark - 蛋白质率
- (NSArray<NSNumber *> *)ppProteinPercentageList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhProteinRateList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_Protein_StandartArray_percent_4_A:self.bodyLegs140.bhProteinRateList typeWeight:NO];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_Protein_StandartArray_percent_4_A:self.bodyArms140.bhProteinRateList typeWeight:NO];
    }else{
        
        return [self.standardArray fetchPPBodyParam_Protein_StandartArray_4_D_typeWeight:NO];
    }
    
}

#pragma mark - 皮下脂肪量
- (NSArray<NSNumber *> *)ppBodyFatSubCutKgList{
    
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhBodyFatSubCutKgList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_BodySubcutaneousFat_StandartArray_percent_4_A:self.bodyLegs140.bhBodyFatSubCutRateList typeWeight:YES];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_BodySubcutaneousFat_StandartArray_percent_4_A:self.bodyArms140.bhBodyFatSubCutRateList typeWeight:YES];
    }else{
        
        return [self.standardArray fetchPPBodyParam_BodySubcutaneousFat_StandartArray_4_D_typeWeight:YES];
    }
}


#pragma mark - 皮下脂肪率
- (NSArray<NSNumber *> *)ppBodyFatSubCutPercentageList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhBodyFatSubCutRateList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_BodySubcutaneousFat_StandartArray_percent_4_A:self.bodyLegs140.bhBodyFatSubCutRateList typeWeight:NO];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_BodySubcutaneousFat_StandartArray_percent_4_A:self.bodyArms140.bhBodyFatSubCutRateList typeWeight:NO];
    }else{
        
        return [self.standardArray fetchPPBodyParam_BodySubcutaneousFat_StandartArray_4_D_typeWeight:NO];
    }
}


#pragma mark - 骨骼肌量
- (NSArray<NSNumber *> *)ppBodySkeletalKgList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhSkeletalMuscleKgList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_BodySkeletal_StandartArray_kg_4_A:self.bodyLegs140.bhSkeletalMuscleKgList typeWeight:YES];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_BodySkeletal_StandartArray_kg_4_A:self.bodyArms140.bhSkeletalMuscleKgList typeWeight:YES];
    }else{
        
        return [self.standardArray fetchPPBodyParam_BodySkeletal_StandartArray_4_D_typeWeight:YES];
    }
}

#pragma mark - 骨骼肌率
- (NSArray<NSNumber *> *)ppBodySkeletalList{
    
    if (self.body270){
        
        return [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhSkeletalMuscleRateList];

    }else if (self.bodyLegs140){
        
        return [self.standardArray fetchPPBodyParam_BodySkeletal_StandartArray_kg_4_A:self.bodyLegs140.bhSkeletalMuscleKgList typeWeight:NO];
    }else if (self.bodyArms140){
        
        return [self.standardArray fetchPPBodyParam_BodySkeletal_StandartArray_kg_4_A:self.bodyArms140.bhSkeletalMuscleKgList typeWeight:NO];
    }else{
        
        return [self.standardArray fetchPPBodyParam_BodySkeletal_StandartArray_4_D_typeWeight:NO];
    }
}

#pragma mark - 心律

- (NSArray<NSNumber *> *)ppHeartRateList{
    
    NSArray *standardArray = [self.standardArray fetchPPBodyParam_heart_StandartArray];
    
    return standardArray;

}


#pragma mark - 身体年龄

- (NSArray<NSNumber *> *)ppBodyAgeList{
    
    NSArray *standardArray = [self.standardArray fetchPPBodyParam_physicalAgeValue_StandartArray];

    return standardArray;

}


#pragma mark - 身体得分

- (NSArray<NSNumber *> *)ppBodyScoreList{
    
    NSArray *standardArray = [self.standardArray fetchPPBodyParam_BodyScore_StandartArray];

    return standardArray;

}


#pragma mark - 去脂体重
- (NSArray<NSNumber *> *)ppLoseFatWeightKgList{
    
    if (self.body270){
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhBodyFatFreeMassKgList];
        
        return standardArray;

    }else{
        
        return @[];
    }
    
}


#pragma mark - 身体细胞量
- (NSArray<NSNumber *> *)ppCellMassKgList{
    
    if (self.body270){
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhCellMassKgList];


        return standardArray;
    }else{
        return @[];
        
    }
}

#pragma mark - 细胞内水量
- (NSArray<NSNumber *> *)ppWaterICWKgList{
    
    if (self.body270){
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhWaterICWKgList];

        return standardArray;
    }else{
        return @[];
        
    }
}

#pragma mark - 细胞外水量
- (NSArray<NSNumber *> *)ppWaterECWKgList{
    
    if (self.body270){
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhWaterECWKgList];

        return standardArray;
    }else{
        return @[];
        
    }
    
}

#pragma mark - 无机盐量
- (NSArray<NSNumber *> *)ppMineralKgList{
    
    if (self.body270){
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhMineralKgList];

        return standardArray;
    }else{
        return @[];
        
    }
    
}
#pragma mark - 肥胖度

- (NSArray<NSNumber *> *)ppObesityList{
    
    
    if (self.body270){
        
        NSArray *standardArray = [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhObesityList];

        return standardArray;
    }else{
        
        return @[];
    }
}

#pragma mark - 腰臀比

- (NSArray<NSNumber *> *)ppWHRList{
    
    if (self.body270){
    
    NSArray *standardArray = [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhWHRList];

        return standardArray;
    }else{
    
        return @[];
    }
}

#pragma mark -骨骼肌质量指数

- (NSArray<NSNumber *> *)ppSmiList{
    
    if (self.body270){
    
    NSArray *standardArray = [self.standardArray fetchPPBodyParam_StandartArray_8:self.body270.bhSmiList];

        return standardArray;
    }else{
    
        return @[];
    }
}

@end
