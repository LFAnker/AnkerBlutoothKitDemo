//
//  PPBodyDetailModel.m
//  PPBluetoothKit
//
//  Created by  lefu on 2023/6/12.
//

#import "PPBodyDetailModel.h"
#import "PPBodyDetailStandardArray.h"

@interface PPBodyDetailModel()

@property (nonatomic,strong) PPBodyFatModel *fatModel;

@property (nonatomic,strong) NSDictionary *configDic;


@end

@implementation PPBodyDetailModel

- (instancetype)initWithBodyFatModel:(PPBodyFatModel*)fatModel{

    self = [super init];
    if (self)
    
    
    self.fatModel = fatModel;
    

    if ([fatModel.ppSDKVersion hasPrefix:@"HT_8"]){
        
        
        NSString *filePath = [[NSBundle bundleForClass:[self class]] pathForResource:@"BodyParam_HT_8" ofType:@"json"];
        NSData *jsonData = [NSData dataWithContentsOfFile:filePath];
        NSError *error;
        NSDictionary *jsonDic = [NSJSONSerialization JSONObjectWithData:jsonData options:kNilOptions error:&error];
        
        self.configDic = jsonDic;
        
        [self fetchHT8PPBodyDetailInfo];
        
    }else{

        NSString *filePath = [[NSBundle bundleForClass:[self class]] pathForResource:@"BodyParam" ofType:@"json"];
        NSData *jsonData = [NSData dataWithContentsOfFile:filePath];
        NSError *error;
        NSDictionary *jsonDic = [NSJSONSerialization JSONObjectWithData:jsonData options:kNilOptions error:&error];
        
        self.configDic = jsonDic;
        
        [self fetchHT4PPBodyDetailInfo];
    }
    

    self.data = @{
        
        @"errorType":[self PPErrorType2String:fatModel.errorType],
        @"lefuBodyData":[self showDataEnuArrayWithSDKVersion:fatModel.ppSDKVersion andErrorType:fatModel.errorType]
    };
    
    
    return self;
}

- (void)fetchHT8PPBodyDetailInfo{
    
    self.PPBodyParam_Weight = [self fetchPPBodyParam_Weight];
    self.PPBodyParam_Bodystandard = [self fetchPPBodyParam_Bodystandard];
    self.PPBodyParam_BodyControl = [self fetchPPBodyParam_BodyControl];
    self.PPBodyParam_BodyLBW = [self fetchPPBodyParam_BodyLBW];
    self.PPBodyParam_BodyFat = [self fetchPPBodyParam_BodyFat];
    self.PPBodyParam_BodyFatKg = [self fetchPPBodyParam_BodyFatKg];
    self.PPBodyParam_Water = [self fetchPPBodyParam_Water];
    self.PPBodyParam_MusRate = [self fetchPPBodyParam_MusRate];
    self.PPBodyParam_Mus = [self fetchPPBodyParam_Mus];
    self.PPBodyParam_Bone = [self fetchPPBodyParam_Bone];
    self.PPBodyParam_BMR = [self fetchPPBodyParam_BMR];
    self.PPBodyParam_BMI = [self fetchPPBodyParam_BMI];
    self.PPBodyParam_VisFat = [self fetchPPBodyParam_VisFat];
    self.PPBodyParam_physicalAgeValue = [self fetchPPBodyParam_physicalAgeValue];
    self.PPBodyParam_proteinPercentage = [self fetchPPBodyParam_proteinPercentage];
    self.PPBodyParam_BodyType = [self fetchPPBodyParam_BodyType];
    self.PPBodyParam_BodyScore = [self fetchPPBodyParam_BodyScore];
    self.PPBodyParam_BodySubcutaneousFat = [self fetchPPBodyParam_BodySubcutaneousFat];
    self.PPBodyParam_BodySkeletal = [self fetchPPBodyParam_BodySkeletal];
    self.PPBodyParam_MuscleControl = [self fetchPPBodyParam_MuscleControl];
    self.PPBodyParam_BodyControlLiang = [self fetchPPBodyParam_BodyControlLiang];
    self.PPBodyParam_FatGrade = [self fetchPPBodyParam_FatGrade];
    self.PPBodyParam_BodyHealth = [self fetchPPBodyParam_BodyHealth];
    self.PPBodyParam_heart = [self fetchPPBodyParam_heart];
    self.PPBodyParam_waterKg = [self fetchPPBodyParam_waterKg];
    self.PPBodyParam_proteinKg = [self fetchPPBodyParam_proteinKg];
    self.PPBodyParam_bodyFatSubCutKg = [self fetchPPBodyParam_bodyFatSubCutKg];
    self.PPBodyParam_cellMassKg = [self fetchPPBodyParam_cellMassKg];
    self.PPBodyParam_dCI = [self fetchPPBodyParam_dCI];
    self.PPBodyParam_mineralKg = [self fetchPPBodyParam_mineralKg];
    self.PPBodyParam_obesity = [self fetchPPBodyParam_obesity];
    self.PPBodyParam_waterECWKg = [self fetchPPBodyParam_waterECWKg];
    self.PPBodyParam_waterICWKg = [self fetchPPBodyParam_waterICWKg];
    self.PPBodyParam_bodyFatKgLeftArm = [self fetchPPBodyParam_bodyFatKgLeftArm];
    self.PPBodyParam_bodyFatKgLeftLeg = [self fetchPPBodyParam_bodyFatKgLeftLeg];
    self.PPBodyParam_bodyFatKgRightArm = [self fetchPPBodyParam_bodyFatKgRightArm];
    self.PPBodyParam_bodyFatKgRightLeg = [self fetchPPBodyParam_bodyFatKgRightLeg];
    self.PPBodyParam_bodyFatKgTrunk = [self fetchPPBodyParam_bodyFatKgTrunk];
    self.PPBodyParam_bodyFatRateLeftArm = [self fetchPPBodyParam_bodyFatRateLeftArm];
    self.PPBodyParam_bodyFatRateLeftLeg = [self fetchPPBodyParam_bodyFatRateLeftLeg];
    self.PPBodyParam_bodyFatRateRightArm = [self fetchPPBodyParam_bodyFatRateRightArm];
    self.PPBodyParam_bodyFatRateRightLeg = [self fetchPPBodyParam_bodyFatRateRightLeg];
    self.PPBodyParam_bodyFatRateTrunk = [self fetchPPBodyParam_bodyFatRateTrunk];
    self.PPBodyParam_muscleKgLeftArm = [self fetchPPBodyParam_muscleKgLeftArm];
    self.PPBodyParam_muscleKgLeftLeg = [self fetchPPBodyParam_muscleKgLeftLeg];
    self.PPBodyParam_muscleKgRightArm = [self fetchPPBodyParam_muscleKgRightArm];
    self.PPBodyParam_muscleKgRightLeg = [self fetchPPBodyParam_muscleKgRightLeg];
    self.PPBodyParam_muscleKgTrunk = [self fetchPPBodyParam_muscleKgTrunk];
    self.PPBodyParam_muscleRateLeftArm = [self fetchPPBodyParam_muscleRateLeftArm];
    self.PPBodyParam_muscleRateLeftLeg = [self fetchPPBodyParam_muscleRateLeftLeg];
    self.PPBodyParam_muscleRateRightArm = [self fetchPPBodyParam_muscleRateRightArm];
    self.PPBodyParam_muscleRateRightLeg = [self fetchPPBodyParam_muscleRateRightLeg];
    self.PPBodyParam_muscleRateTrunk = [self fetchPPBodyParam_muscleRateTrunk];
    

    self.PPBodyParam_SkeletalMuscleKg = [self fetchPPBodyParam_SkeletalMuscleKg];
    self.PPBodyParam_smi = [self fetchPPBodyParam_Smi];
    self.PPBodyParam_WHR = [self fetchPPBodyParam_WHR];
}

- (void)fetchHT4PPBodyDetailInfo{
    
    self.PPBodyParam_Weight = [self fetchPPBodyParam_Weight];
    self.PPBodyParam_Bodystandard = [self fetchPPBodyParam_Bodystandard];
    self.PPBodyParam_BodyControl = [self fetchPPBodyParam_BodyControl];
    self.PPBodyParam_BodyLBW = [self fetchPPBodyParam_BodyLBW];
    self.PPBodyParam_BodyFat = [self fetchPPBodyParam_BodyFat];
    self.PPBodyParam_BodyFatKg = [self fetchPPBodyParam_BodyFatKg];
    self.PPBodyParam_Water = [self fetchPPBodyParam_Water];
    self.PPBodyParam_MusRate = [self fetchPPBodyParam_MusRate];
    self.PPBodyParam_Mus = [self fetchPPBodyParam_Mus];
    self.PPBodyParam_Bone = [self fetchPPBodyParam_Bone];
    self.PPBodyParam_BMR = [self fetchPPBodyParam_BMR];
    self.PPBodyParam_BMI = [self fetchPPBodyParam_BMI];
    self.PPBodyParam_VisFat = [self fetchPPBodyParam_VisFat];
    self.PPBodyParam_physicalAgeValue = [self fetchPPBodyParam_physicalAgeValue];
    self.PPBodyParam_proteinPercentage = [self fetchPPBodyParam_proteinPercentage];
    self.PPBodyParam_BodyType = [self fetchPPBodyParam_BodyType];
    self.PPBodyParam_BodyScore = [self fetchPPBodyParam_BodyScore];
    self.PPBodyParam_BodySubcutaneousFat = [self fetchPPBodyParam_BodySubcutaneousFat];
    self.PPBodyParam_BodySkeletal = [self fetchPPBodyParam_BodySkeletal];
    self.PPBodyParam_MuscleControl = [self fetchPPBodyParam_MuscleControl];
    self.PPBodyParam_BodyControlLiang = [self fetchPPBodyParam_BodyControlLiang];
    self.PPBodyParam_FatGrade = [self fetchPPBodyParam_FatGrade];
    self.PPBodyParam_BodyHealth = [self fetchPPBodyParam_BodyHealth];
    self.PPBodyParam_heart = [self fetchPPBodyParam_heart];
    self.PPBodyParam_waterKg = [self fetchPPBodyParam_waterKg];
    self.PPBodyParam_proteinKg = [self fetchPPBodyParam_proteinKg];
    self.PPBodyParam_bodyFatSubCutKg = [self fetchPPBodyParam_bodyFatSubCutKg];
    
    self.PPBodyParam_SkeletalMuscleKg = [self fetchPPBodyParam_SkeletalMuscleKg];
}




#pragma mark - property

- (NSArray<PPBodyDetailInfoModel *> *)showDataEnuArrayWithSDKVersion:(NSString *)version andErrorType:(PPBodyfatErrorType)errorType{
    
    NSArray<PPBodyDetailInfoModel *> *showDataEnuArray = @[];
    if (errorType != PP_ERROR_TYPE_NONE){
        
        showDataEnuArray = @[
            self.PPBodyParam_Weight,
            self.PPBodyParam_BMI
        ];
    }else{
        
        if ([version hasPrefix:@"HT_4"]){
            
            showDataEnuArray = @[
//                体重
//                脂肪率
                
                self.PPBodyParam_Weight,
                self.PPBodyParam_BodyFat,
//                BMI
//                肌肉量
              
                self.PPBodyParam_BMI,
                self.PPBodyParam_Mus,

//                基础代谢（BMR）
//                体水分率
              
                self.PPBodyParam_BMR,
                self.PPBodyParam_Water,

//                心率
//                脂肪量
               
                self.PPBodyParam_heart,
                self.PPBodyParam_BodyFatKg,

//                蛋白质率
//                去脂体重
               
                self.PPBodyParam_proteinPercentage,
                self.PPBodyParam_BodyLBW,
//                皮下脂肪率
//                 骨骼肌率
              
                self.PPBodyParam_BodySubcutaneousFat,
                self.PPBodyParam_BodySkeletal,
//                内脏脂肪
//                肌肉率
              
                self.PPBodyParam_VisFat,
                self.PPBodyParam_MusRate,
//                肌肉控制量
//                脂肪控制量
              
                self.PPBodyParam_MuscleControl,
                self.PPBodyParam_BodyControlLiang,
//                理想体重
//                体重控制
              
                self.PPBodyParam_Bodystandard,
                self.PPBodyParam_BodyControl,
//                骨量
//                身体类型
               
                self.PPBodyParam_Bone,
                self.PPBodyParam_BodyType,
//                肥胖等级
//                健康评估
//
                self.PPBodyParam_FatGrade,
                self.PPBodyParam_BodyHealth,
//                身体年龄
//                身体得分
                self.PPBodyParam_physicalAgeValue,
                self.PPBodyParam_BodyScore,
                
//                骨骼肌量
                self.PPBodyParam_SkeletalMuscleKg
            ];
            
        }
        else if ([version hasPrefix:@"LF_4"]) {
            
            showDataEnuArray = @[
//                体重
//                脂肪率
                self.PPBodyParam_Weight,
                self.PPBodyParam_BodyFat,
//                BMI
//                肌肉量
              
                self.PPBodyParam_BMI,
                self.PPBodyParam_Mus,

//                基础代谢（BMR）
//                体水分率
              
                self.PPBodyParam_BMR,
                self.PPBodyParam_Water,

//                心率
//                脂肪量
               
                self.PPBodyParam_heart,
                self.PPBodyParam_BodyFatKg,

//                蛋白质率
//                去脂体重
               
                self.PPBodyParam_proteinPercentage,
                self.PPBodyParam_BodyLBW,
//                皮下脂肪率
//                 骨骼肌率
              
                self.PPBodyParam_BodySubcutaneousFat,
                self.PPBodyParam_BodySkeletal,
//                内脏脂肪
//                肌肉率
              
                self.PPBodyParam_VisFat,
                self.PPBodyParam_MusRate,
//                肌肉控制量
//                脂肪控制量
              
                self.PPBodyParam_MuscleControl,
                self.PPBodyParam_BodyControlLiang,
//                理想体重
//                体重控制
              
                self.PPBodyParam_Bodystandard,
                self.PPBodyParam_BodyControl,
//                骨量
//                身体类型
               
                self.PPBodyParam_Bone,
                self.PPBodyParam_BodyType,
//                肥胖等级
//                健康评估
//
                self.PPBodyParam_FatGrade,
                self.PPBodyParam_BodyHealth,
//                身体年龄
//                身体得分
                self.PPBodyParam_physicalAgeValue,
                self.PPBodyParam_BodyScore,
                
//                骨骼肌量
                self.PPBodyParam_SkeletalMuscleKg
            ];
        }
        else if ([version hasPrefix:@"HT_8"]) {
            
            
            showDataEnuArray = @[
//                体重
//                脂肪率
                self.PPBodyParam_Weight,
                self.PPBodyParam_BodyFat,
//                BMI
//                肌肉量
                self.PPBodyParam_BMI,
                self.PPBodyParam_Mus,
//                基础代谢（BMR）
//                建议卡路里摄入量
                
                self.PPBodyParam_BMR,
                self.PPBodyParam_dCI,
//                心率
//                体水分率
                
                self.PPBodyParam_heart,
                self.PPBodyParam_Water,
//                蛋白质率
//                水分量
                
                self.PPBodyParam_proteinPercentage,
                self.PPBodyParam_waterKg,
//                蛋白质量
//                脂肪量
                
                self.PPBodyParam_proteinKg,
                self.PPBodyParam_BodyFatKg,
//                去脂体重
//                皮下脂肪率
                 
                self.PPBodyParam_BodyLBW,
                self.PPBodyParam_BodySubcutaneousFat,
//                骨骼肌率
//               内脏脂肪
               
                self.PPBodyParam_BodySkeletal,
                self.PPBodyParam_VisFat,
//                骨量
//                皮下脂肪量
                
                self.PPBodyParam_Bone,
                self.PPBodyParam_bodyFatSubCutKg,
//                肌肉率
//                左手脂肪率
               
                self.PPBodyParam_MusRate,
                self.PPBodyParam_bodyFatRateLeftArm,
//                身体细胞量
//                右手脂肪率
                
                self.PPBodyParam_cellMassKg,
                self.PPBodyParam_bodyFatRateRightArm,
//                细胞内水量
//                躯干脂肪率
                
                self.PPBodyParam_waterICWKg,
                self.PPBodyParam_bodyFatRateTrunk,
//                细胞外水量
//                左脚脂肪率
                
                self.PPBodyParam_waterECWKg,
                self.PPBodyParam_bodyFatRateLeftLeg,
//                无机盐量
//                右脚脂肪率
                
                self.PPBodyParam_mineralKg,
                self.PPBodyParam_bodyFatRateRightLeg,
//                左手肌肉量
//                左手脂肪量
                
                self.PPBodyParam_muscleKgLeftArm,
                self.PPBodyParam_bodyFatKgLeftArm,
//                右手肌肉量
//                右手脂肪量
                
                self.PPBodyParam_muscleKgRightArm,
                self.PPBodyParam_bodyFatKgRightArm,
//                躯干肌肉量
//                躯干脂肪量
                
                self.PPBodyParam_muscleKgTrunk,
                self.PPBodyParam_bodyFatKgTrunk,
//                左脚肌肉量
//                左脚脂肪量
                
                self.PPBodyParam_muscleKgLeftLeg,
                self.PPBodyParam_bodyFatKgLeftLeg,
//                右脚肌肉量
//                右脚脂肪量
                
                self.PPBodyParam_muscleKgRightLeg,
                self.PPBodyParam_bodyFatKgRightLeg,
//                肌肉控制量
//                脂肪控制量
                
                self.PPBodyParam_MuscleControl,
                self.PPBodyParam_BodyControlLiang,
                
//                理想体重
//                体重控制
                self.PPBodyParam_Bodystandard,
                self.PPBodyParam_BodyControl,
                
//                肥胖度
//                身体类型
                self.PPBodyParam_obesity,
                self.PPBodyParam_BodyType,

//                肥胖等级
//                健康评估
                self.PPBodyParam_FatGrade,
                self.PPBodyParam_BodyHealth,
                
//                身体年龄
//                身体得分
                self.PPBodyParam_physicalAgeValue,
                self.PPBodyParam_BodyScore,
                
//                骨骼肌量
//                骨骼肌质量指数Smi
                self.PPBodyParam_SkeletalMuscleKg,
                self.PPBodyParam_smi,
                
//                腰臀比
                self.PPBodyParam_WHR
            ];
            

        }
        else if ([version hasPrefix:@"LF_X"]) {
            
            
            showDataEnuArray = @[

//                体重
//                脂肪率
                
                self.PPBodyParam_Weight,
                self.PPBodyParam_BMI,

//                BMI
//                骨量
                self.PPBodyParam_BodyFat,
                self.PPBodyParam_Bone,
                
//                肌肉量
//                内脏脂肪
                self.PPBodyParam_Mus,
                self.PPBodyParam_VisFat,

//                体水分率
//                基础代谢（BMR）
              
                self.PPBodyParam_Water,
                self.PPBodyParam_BMR



            ];
        }
        else {
            showDataEnuArray = @[
                self.PPBodyParam_Weight,
                self.PPBodyParam_BMI
            ];
        }
        
    }
    return showDataEnuArray;
}


- (NSString *)PPErrorType2String:(PPBodyfatErrorType)errorType{
    

    NSString *errorTypeString;
    switch (errorType) {
        case PP_ERROR_TYPE_NONE:
            errorTypeString = @"PP_ERROR_TYPE_NONE";
            break;
        case PP_ERROR_TYPE_AGE:
            errorTypeString = @"PP_ERROR_TYPE_AGE";
            break;
        case PP_ERROR_TYPE_HEIGHT:
            errorTypeString = @"PP_ERROR_TYPE_HEIGHT";
            break;
        case PP_ERROR_TYPE_WEIGHT:
            errorTypeString = @"PP_ERROR_TYPE_WEIGHT";
            break;
        case PP_ERROR_TYPE_SEX:
            errorTypeString = @"PP_ERROR_TYPE_SEX";
            break;
        case PP_ERROR_TYPE_PEOPLE_TYPE:
            errorTypeString = @"PP_ERROR_TYPE_PEOPLE_TYPE";
            break;
        case PP_ERROR_TYPE_IMPEDANCE_TWO_LEGS:
            errorTypeString = @"PP_ERROR_TYPE_IMPEDANCE_TWO_LEGS";
            break;
        case PP_ERROR_TYPE_IMPEDANCE_TWO_ARMS:
            errorTypeString = @"PP_ERROR_TYPE_IMPEDANCE_TWO_ARMS";
            break;
        case PP_ERROR_TYPE_IMPEDANCE_LEFT_BODY:
            errorTypeString = @"PP_ERROR_TYPE_IMPEDANCE_LEFT_BODY";
            break;
        case PP_ERROR_TYPE_IMPEDANCE_LEFT_ARM:
            errorTypeString = @"PP_ERROR_TYPE_IMPEDANCE_LEFT_ARM";
            break;
        case PP_ERROR_TYPE_IMPEDANCE_RIGHT_ARM:
            errorTypeString = @"PP_ERROR_TYPE_IMPEDANCE_RIGHT_ARM";
            break;
        case PP_ERROR_TYPE_IMPEDANCE_LEFT_LEG:
            errorTypeString = @"PP_ERROR_TYPE_IMPEDANCE_LEFT_LEG";
            break;
        case PP_ERROR_TYPE_IMPEDANCE_RIGHT_LEG:
            errorTypeString = @"PP_ERROR_TYPE_IMPEDANCE_RIGHT_LEG";
            break;
        case PP_ERROR_TYPE_IMPEDANCE_TRUNK:
            errorTypeString = @"PP_ERROR_TYPE_IMPEDANCE_TRUNK";
            break;
        case PP_ERROR_TYPE_HOME:
            errorTypeString = @"PP_ERROR_TYPE_HOME";
            break;
        case PP_ERROR_TYPE_PRODUCT:
            errorTypeString = @"PP_ERROR_TYPE_PRODUCT";
            break;
        default:
            errorTypeString = @"Unknown Error Type";
            break;
    }

    return errorTypeString;
}

#pragma mark -体重

- (PPBodyDetailInfoModel *)fetchPPBodyParam_Weight{
    
    NSArray *standardArray = self.fatModel.ppWeightKgList;
    CGFloat currentValue = self.fatModel.ppWeightKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_Weight" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -标准体重

- (PPBodyDetailInfoModel *)fetchPPBodyParam_Bodystandard{
    
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyStandardWeightKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_Bodystandard" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -体重控制
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BodyControl{
    
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppControlWeightKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BodyControl" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -去脂体重
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BodyLBW{
    
    NSArray *standardArray = self.fatModel.ppLoseFatWeightKgList;
    CGFloat currentValue = self.fatModel.ppLoseFatWeightKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BodyLBW" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -体脂率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BodyFat{
    
    NSArray *standardArray = self.fatModel.ppFatList;
    CGFloat currentValue = self.fatModel.ppFat;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BodyFat" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -脂肪量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BodyFatKg{
    

    NSArray *standardArray = self.fatModel.ppBodyfatKgList;
    CGFloat currentValue = self.fatModel.ppBodyfatKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BodyFatKg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -水分率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_Water{
    
    NSArray *standardArray = self.fatModel.ppWaterPercentageList;
    CGFloat currentValue = self.fatModel.ppWaterPercentage;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_Water" standardArray:standardArray currentValue:currentValue];

    return infoModel;
    
}

#pragma mark -肌肉率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_MusRate{

    NSArray *standardArray =self.fatModel.ppMusclePercentageList;
    CGFloat currentValue = self.fatModel.ppMusclePercentage;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_MusRate" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark -肌肉量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_Mus{
    
    NSArray *standardArray = self.fatModel.ppMuscleKgList;
    CGFloat currentValue = self.fatModel.ppMuscleKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_Mus" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -骨量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_Bone{
    
    NSArray *standardArray = self.fatModel.ppBoneKgList;
    CGFloat currentValue = self.fatModel.ppBoneKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_Bone" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark -BMR
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BMR{
  
    NSArray *standardArray = self.fatModel.ppBMRList;
    CGFloat currentValue = self.fatModel.ppBMR;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BMR" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark -BMI
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BMI{

    NSArray *standardArray = self.fatModel.ppBMIList;
    CGFloat currentValue = self.fatModel.ppBMI;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BMI" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark -内脏脂肪等级
- (PPBodyDetailInfoModel *)fetchPPBodyParam_VisFat{

    NSArray *standardArray =self.fatModel.ppVisceralFatList;
    CGFloat currentValue = self.fatModel.ppVisceralFat;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_VisFat" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -身体年龄
- (PPBodyDetailInfoModel *)fetchPPBodyParam_physicalAgeValue{

    NSArray *standardArray = self.fatModel.ppBodyAgeList;
    CGFloat currentValue = self.fatModel.ppBodyAge;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_physicalAgeValue" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -蛋白质率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_proteinPercentage{

    NSArray *standardArray = self.fatModel.ppProteinPercentageList;
    CGFloat currentValue = self.fatModel.ppProteinPercentage;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_proteinPercentage" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -身体类型
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BodyType{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyType;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BodyType" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -身体得分
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BodyScore{

    NSArray *standardArray = self.fatModel.ppBodyScoreList;
    CGFloat currentValue = self.fatModel.ppBodyScore;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BodyScore" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -皮下脂肪率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BodySubcutaneousFat{

    NSArray *standardArray = self.fatModel.ppBodyFatSubCutPercentageList;
    CGFloat currentValue = self.fatModel.ppBodyFatSubCutPercentage;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BodySubcutaneousFat" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark -骨骼肌率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BodySkeletal{
   

    NSArray *standardArray = self.fatModel.ppBodySkeletalList;
    
    CGFloat currentValue = self.fatModel.ppBodySkeletal;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BodySkeletal" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark -肌肉控制量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_MuscleControl{
    
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyMuscleControl;
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_MuscleControl" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -脂肪控制量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BodyControlLiang{
    
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppFatControlKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BodyControlLiang" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -肥胖等级
- (PPBodyDetailInfoModel *)fetchPPBodyParam_FatGrade{
    
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppFatGrade;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_FatGrade" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -健康评估
- (PPBodyDetailInfoModel *)fetchPPBodyParam_BodyHealth{
    
    NSArray *standardArray = @[];
    
    // 为了在进度计算中刚好指在区间中间
    CGFloat currentValue = self.fatModel.ppBodyHealth;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_BodyHealth" standardArray:standardArray currentValue:currentValue];

    return infoModel;
    
}


#pragma mark -心率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_heart{
    
    NSArray *standardArray = self.fatModel.ppHeartRateList;
    CGFloat currentValue = self.fatModel.ppHeartRate;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_heart" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -体水分量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_waterKg{
    
    NSArray *standardArray = self.fatModel.ppWaterKgList;
    CGFloat currentValue = self.fatModel.ppWaterKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_waterKg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark -蛋白质量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_proteinKg{
        
    NSArray *standardArray =  self.fatModel.ppProteinKgList;
    CGFloat currentValue = self.fatModel.ppProteinKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_proteinKg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -皮下脂肪量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatSubCutKg{

    NSArray *standardArray =  self.fatModel.ppBodyFatSubCutKgList;
    CGFloat currentValue = self.fatModel.ppBodyFatSubCutKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatSubCutKg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -身体细胞量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_cellMassKg{

    NSArray *standardArray = self.fatModel.ppCellMassKgList;
    CGFloat currentValue = self.fatModel.ppCellMassKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_cellMassKg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -建议卡路里摄入量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_dCI{
    
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppDCI;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_dCI" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -无机盐量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_mineralKg{

    NSArray *standardArray = self.fatModel.ppMineralKgList;
    CGFloat currentValue = self.fatModel.ppMineralKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_mineralKg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark - 肥胖度
- (PPBodyDetailInfoModel *)fetchPPBodyParam_obesity{

    NSArray *standardArray = self.fatModel.ppObesityList;
    CGFloat currentValue = self.fatModel.ppObesity;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_obesity" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -细胞外水量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_waterECWKg{

    NSArray *standardArray = self.fatModel.ppWaterECWKgList;
    CGFloat currentValue = self.fatModel.ppWaterECWKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_waterECWKg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -细胞内水量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_waterICWKg{

    NSArray *standardArray = self.fatModel.ppWaterICWKgList;
    CGFloat currentValue = self.fatModel.ppWaterICWKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_waterICWKg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -左手脂肪量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatKgLeftArm{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyFatKgLeftArm;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatKgLeftArm" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -左脚脂肪量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatKgLeftLeg{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyFatKgLeftLeg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatKgLeftLeg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -右手脂肪量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatKgRightArm{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyFatKgRightArm;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatKgRightArm" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -右脚脂肪量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatKgRightLeg{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyFatKgRightLeg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatKgRightLeg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -躯干脂肪量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatKgTrunk{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyFatKgTrunk;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatKgTrunk" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -左手脂肪率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatRateLeftArm{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyFatRateLeftArm;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatRateLeftArm" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -左脚脂肪率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatRateLeftLeg{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyFatRateLeftLeg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatRateLeftLeg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -右手脂肪率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatRateRightArm{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyFatRateRightArm;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatRateRightArm" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -右脚脂肪率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatRateRightLeg{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyFatRateRightLeg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatRateRightLeg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -躯干脂肪率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_bodyFatRateTrunk{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppBodyFatRateTrunk;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_bodyFatRateTrunk" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -左手肌肉量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_muscleKgLeftArm{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppMuscleKgLeftArm;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_muscleKgLeftArm" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -左脚肌肉量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_muscleKgLeftLeg{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppMuscleKgLeftLeg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_muscleKgLeftLeg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -右手肌肉量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_muscleKgRightArm{
    
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppMuscleKgRightArm;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_muscleKgRightArm" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -右脚肌肉量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_muscleKgRightLeg{
  
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppMuscleKgRightLeg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_muscleKgRightLeg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
    
}

#pragma mark -躯干肌肉量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_muscleKgTrunk{
    
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppMuscleKgTrunk;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_muscleKgTrunk" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark -左手肌肉率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_muscleRateLeftArm{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppMuscleRateLeftArm;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_muscleRateLeftArm" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -左脚肌肉率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_muscleRateLeftLeg{

    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppMuscleRateLeftLeg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_muscleRateLeftLeg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -右手肌肉率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_muscleRateRightArm{
    
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppMuscleRateRightArm;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_muscleRateRightArm" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -右脚肌肉率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_muscleRateRightLeg{
  
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppMuscleRateRightLeg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_muscleRateRightLeg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
    
}

#pragma mark -躯干肌肉率
- (PPBodyDetailInfoModel *)fetchPPBodyParam_muscleRateTrunk{
    
    NSArray *standardArray = @[];
    CGFloat currentValue = self.fatModel.ppMuscleRateTrunk;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_muscleRateTrunk" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}


#pragma mark -骨骼肌量
- (PPBodyDetailInfoModel *)fetchPPBodyParam_SkeletalMuscleKg{
    
    NSArray *standardArray = self.fatModel.ppBodySkeletalKgList;
    CGFloat currentValue = self.fatModel.ppBodySkeletalKg;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_SkeletalMuscleKg" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark -骨骼肌质量指数Smi
- (PPBodyDetailInfoModel *)fetchPPBodyParam_Smi{
    
    NSArray *standardArray = self.fatModel.ppSmiList;
    CGFloat currentValue = self.fatModel.ppSmi;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_smi" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

#pragma mark -腰臀比
- (PPBodyDetailInfoModel *)fetchPPBodyParam_WHR{
    
    NSArray *standardArray = self.fatModel.ppWHRList;
    CGFloat currentValue = self.fatModel.ppWHR;
    
    PPBodyDetailInfoModel *infoModel = [[PPBodyDetailInfoModel alloc] initWithDic:self.configDic andKeyName:@"BodyParam_WHR" standardArray:standardArray currentValue:currentValue];

    return infoModel;
}

@end
