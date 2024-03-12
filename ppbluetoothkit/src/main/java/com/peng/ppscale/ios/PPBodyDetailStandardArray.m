//
//  PPBodyDetailStandardArray.m
//  PPBluetoothKit
//
//  Created by 彭思远 on 2023/6/20.
//

#import "PPBodyDetailStandardArray.h"

@interface PPBodyDetailStandardArray()

@property (nonatomic, assign) NSInteger heightCm;

@property (nonatomic, assign) PPDeviceGenderType gender;

@property (nonatomic, assign) NSInteger age;

@property (nonatomic, assign) CGFloat weight;

@property (nonatomic, assign, readwrite) CGFloat idealWeight;

@property (nonatomic, strong) NSNumberFormatter *numberFormatter;

@end

@implementation PPBodyDetailStandardArray

- initWithHeightCm:(NSInteger)heightCm
            gender:(PPDeviceGenderType)gender
               age:(NSInteger)age
            weight:(CGFloat)weight
      standardType:(PPBodyDetailStandardType)standardType{
    
    self = [super init];
    if (self){
        
        self.heightCm = heightCm;
        self.gender = gender;
        self.age = age;
        self.weight = weight;
        
        NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
        [numberFormatter setNumberStyle:NSNumberFormatterDecimalStyle];
        [numberFormatter setMaximumFractionDigits:1];
        self.numberFormatter = numberFormatter;
        
        
        if (standardType == PPBodyDetailStandardType8){
            
            if (gender == PPDeviceGenderTypeMale){
                
                self.idealWeight = 22 * pow(heightCm / 100.0, 2);
            }else{
                
                self.idealWeight = 21 * pow(heightCm / 100.0, 2);
            }
            
        }else{
            
            self.idealWeight = 22 * pow(heightCm / 100.0, 2);
        }
    
    }
    
    return self;
}

+ (NSInteger)calculateCurrentStandardWithValue:(CGFloat)number andStandardArray:(NSArray *)array{
    
    NSInteger count = array.count;
    
    float first = [[array firstObject] floatValue];
    if (number < first) {
        return 0;
    }
    float last = [[array lastObject] floatValue];
    if (number >= last) {
        return count - 2;
    }
    for (NSInteger i = 1; i < count; i++) {
        float prev = [[array objectAtIndex:i-1] floatValue];
        float current = [[array objectAtIndex:i] floatValue];
        if (number >= prev && number < current) {
            return i - 1;
        }
    }
    return 0;
}


+ (NSArray *)descDic2Arr:(NSDictionary *)dictionary{
    
    NSArray *sortedValues = [dictionary.allValues sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
        
        if ([obj1 integerValue] < [obj2 integerValue]) {
            
            return NSOrderedAscending;
        } else if ([obj1 integerValue] > [obj2 integerValue]) {
            
            return NSOrderedDescending;
        } else {
            
            return NSOrderedSame;
        }
    }];
    
    return sortedValues;
}


- (NSArray *)intervalRange:(NSArray *)old addBoundary:(NSArray *)boundary{
    
    
    if (boundary.count == 2){
        
        NSMutableArray *standardArray = old.mutableCopy;
        [standardArray insertObject:boundary.firstObject atIndex:0];
        [standardArray insertObject:boundary.lastObject atIndex:standardArray.count];
        return standardArray.copy;
        
    }else{
        
        NSArray *standardArray = [self typeValueIncrementBoundary:old];
        return standardArray;
    }
}

- (NSArray *)typeValueIncrementBoundary:(NSArray *)old{
    
    NSMutableArray *temp = old.mutableCopy;
    
    if (old == nil){
        
        return @[];
        
    }else{
        
        if (old.count == 0){
            
            return @[];
            
        }else if (old.count == 1){
            
            [temp insertObject:@(0) atIndex:0];
            [temp insertObject:@([temp.lastObject floatValue] * 2) atIndex:temp.count];
            return temp.copy;
            
        }else{
            CGFloat min = [old.firstObject floatValue];
            CGFloat max = [old.lastObject floatValue];
            CGFloat mid = fabs(min - max) / 2.0;
            
            CGFloat first= min - mid;
            CGFloat last = max + mid;
            [temp insertObject:@(first < 0 ? 0:first) atIndex:0];
            [temp insertObject:@(last) atIndex:temp.count];
            return temp.copy;
        }
        
    }
}

- (NSArray *)percent2kg:(NSArray *)source{
    
    NSMutableArray *temp = @[].mutableCopy;
    
    CGFloat weight = (NSInteger)(self.weight * 10) / 10.0;
    
    for (NSNumber *n in source) {
        
        CGFloat kg = weight * n.floatValue / 100.0;
        [temp addObject:@(kg)];
    }
    
    NSArray *standardArray = temp.copy;
    return standardArray;
}

- (NSArray *)kg2percent:(NSArray *)source{
    
    CGFloat weight = (NSInteger)(self.weight * 10) / 10.0;

    NSMutableArray *temp = @[].mutableCopy;
    for (NSNumber *n in source) {
        
        CGFloat percent = n.floatValue / weight * 100;
        [temp addObject:@(percent)];
    }
    
    NSArray *standardArray = temp.copy;
    return standardArray;
}

- (NSArray *)formatter:(NSArray *)source{

    NSMutableArray *formattedArray = [NSMutableArray array];
    
    for (NSNumber *number in source) {
        NSString *formattedNumber = [self.numberFormatter stringFromNumber:number];
        [formattedArray addObject:@(formattedNumber.doubleValue)];
    }
    return formattedArray;
}


#pragma mark - StandartArray
#pragma mark - 体重 【3段】【量】

- (NSArray *)fetchPPBodyParam_Weight_StandartArray_4{

    NSArray *old = [self constWeightKg];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[]];
    return [self formatter:standardArray];
}

- (NSArray *)fetchPPBodyParam_Weight_StandartArray_8:(NSDictionary *)dic{
    
    NSArray *old = [PPBodyDetailStandardArray descDic2Arr:dic];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[]];
    return [self formatter:standardArray];
}


#pragma mark - 体脂 【4电极5段 8电极3段】【量、率】

- (NSArray *)fetchPPBodyParam_BodyFat_StandartArray_4_D_typeWeight:(BOOL)isWeight{

  
    NSArray *old = [self constBodyFatRate];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(5), @(75)]];

    if (isWeight){
        
        NSArray *kgArr = [self percent2kg:standardArray];
        return [self formatter:kgArr];
    }else {
        
        return [self formatter:standardArray];
    }
}


- (NSArray *)fetchPPBodyParam_BodyFat_StandartArray_percent_4_A:(NSDictionary *)dic typeWeight:(BOOL)isWeight{

    NSArray *old = [PPBodyDetailStandardArray descDic2Arr:dic];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(5), @(75)]];
    
    if (isWeight){
        
        NSArray *kgArr = [self percent2kg:standardArray];
        return [self formatter:kgArr];
    }else {
        
        return [self formatter:standardArray];
    }
}

#pragma mark - 水分 【3段】【量、率】

- (NSArray *)fetchPPBodyParam_Water_StandartArray_4_D_typeWeight:(BOOL)isWeight{

    NSArray *old = [self constWaterRate];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(35), @(75)]];
    if (isWeight){
        
        NSArray *kgArr = [self percent2kg:standardArray];
        return [self formatter:kgArr];
    }else {
        
        return [self formatter:standardArray];
    }
}

- (NSArray *)fetchPPBodyParam_Water_StandartArray_percent_4_A:(NSDictionary *)dic typeWeight:(BOOL)isWeight{
    
    NSArray *old = [PPBodyDetailStandardArray descDic2Arr:dic];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(35), @(75)]];

    if (isWeight){
        
        NSArray *kgArr = [self percent2kg:standardArray];
        return [self formatter:kgArr];
    }else {
        
        return [self formatter:standardArray];
    }
}

#pragma mark -肌肉 【3段】【量、率】

- (NSArray *)fetchPPBodyParam_Mus_StandartArray_kg_4_D_typeWeight:(BOOL)isWeight{
    
    NSArray *old = [self constMuscleWeightKg];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(10),@(120)]];

    if (isWeight){
        
        return [self formatter:standardArray];
    }else {
        NSArray *kgArr = [self kg2percent:standardArray];
        return [self formatter:kgArr];
    }
}


- (NSArray *)fetchPPBodyParam_Mus_StandartArray_kg_4_A_:(NSDictionary *)dic typeWeight:(BOOL)isWeight{
    
    NSArray *old = [PPBodyDetailStandardArray descDic2Arr:dic];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(10),@(120)]];
    
    if (isWeight){
        
        return [self formatter:standardArray];
    }else {
        NSArray *kgArr = [self kg2percent:standardArray];
        return [self formatter:kgArr];
    }
}


#pragma mark - 骨量 【3段】【量】

- (NSArray *)fetchPPBodyParam_Bone_StandartArray_kg_4_D{
    
    NSArray *old = [self constBoneWeightKg];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(0.5), @(8)]];
    return [self formatter:standardArray];

}

- (NSArray *)fetchPPBodyParam_Bone_StandartArray_kg_4_A:(NSDictionary *)dic{
    
    NSArray *old = [PPBodyDetailStandardArray descDic2Arr:dic];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(0.5), @(8)]];
    return [self formatter:standardArray];

}



#pragma mark - BMR 【4电极2段】【8电极3段】【kcal】

- (NSArray *)fetchPPBodyParam_BMR_StandartArray_kcal_4_D{
    
    NSArray *old = [self constBMR];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(500),@(5000)]];
    
    return standardArray;
}

- (NSArray *)fetchPPBodyParam_BMR_StandartArray_kcal_4_A:(NSDictionary *)dic{
    
    NSArray *old = [PPBodyDetailStandardArray descDic2Arr:dic];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(500),@(5000)]];
    return standardArray;
}

#pragma mark - BMI 【4电极4段】【8电极3段】

- (NSArray *)fetchPPBodyParam_BMI_StandartArray_4{
 
    return @[@(10),@(18.5),@(25),@(30),@(50)];
}


- (NSArray *)fetchPPBodyParam_BMI_StandartArray_8{
    
    return @[@(10),@(18.5),@(23),@(50)];
}


#pragma mark - 内脏脂肪等级 【3段】

- (NSArray *)fetchPPBodyParam_VisFat_StandartArray_4{
    
    return @[@(1),@(10),@(15),@(50)];

}




#pragma mark - 蛋白质 【3段】【量、率】

- (NSArray *)fetchPPBodyParam_Protein_StandartArray_4_D_typeWeight:(BOOL)isWeight{
    
    NSArray *old = [self constproteinPercentage];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(2),@(30)]];
    
    if (isWeight){
        
        NSArray *kgArr = [self percent2kg:standardArray];
        return [self formatter:kgArr];
    }else {
        
        return [self formatter:standardArray];
    }
}

- (NSArray *)fetchPPBodyParam_Protein_StandartArray_percent_4_A:(NSDictionary *)dic typeWeight:(BOOL)isWeight{
    
    NSArray *old = [PPBodyDetailStandardArray descDic2Arr:dic];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(2), @(30)]];

    if (isWeight){
        
        NSArray *kgArr = [self percent2kg:standardArray];
        return [self formatter:kgArr];
    }else {
        
        return [self formatter:standardArray];
    }
}



#pragma mark - 皮下脂肪 【3段】【量、率】
- (NSArray *)fetchPPBodyParam_BodySubcutaneousFat_StandartArray_4_D_typeWeight:(BOOL)isWeight{
    
    NSArray *old = [self subcutaneousFatPercentage];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(0.1),@(60)]];
    
    if (isWeight){
        
        NSArray *kgArr = [self percent2kg:standardArray];
        return [self formatter:kgArr];
    }else {
        
        return [self formatter:standardArray];
    }
}

- (NSArray *)fetchPPBodyParam_BodySubcutaneousFat_StandartArray_percent_4_A:(NSDictionary *)dic typeWeight:(BOOL)isWeight{
    
    NSArray *old = [PPBodyDetailStandardArray descDic2Arr:dic];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(0.1),@(60)]];
    
    if (isWeight){
        
        NSArray *kgArr = [self percent2kg:standardArray];
        return [self formatter:kgArr];
    }else {
        
        return [self formatter:standardArray];
    }
}


#pragma mark - 骨骼肌 【3段】【量、率】
- (NSArray *)fetchPPBodyParam_BodySkeletal_StandartArray_4_D_typeWeight:(BOOL)isWeight{
    
    NSArray *old = [self skeletalMuscleKg];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(8),@(100)]];
    
    if (isWeight){
        
        return [self formatter:standardArray];
    }else {
        NSArray *kgArr = [self kg2percent:standardArray];
        return [self formatter:kgArr];
    }
}

- (NSArray *)fetchPPBodyParam_BodySkeletal_StandartArray_kg_4_A:(NSDictionary *)dic typeWeight:(BOOL)isWeight{
    
    NSArray *old = [PPBodyDetailStandardArray descDic2Arr:dic];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[@(8),@(100)]];
    
    if (isWeight){
        
        return [self formatter:standardArray];
    }else {
        NSArray *kgArr = [self kg2percent:standardArray];
        return [self formatter:kgArr];
    }
}

#pragma mark - 心率
- (NSArray *)fetchPPBodyParam_heart_StandartArray{
    
    return @[@(0),@(55),@(80),@(100),@(200)];

}


#pragma mark - 身体年龄
- (NSArray *)fetchPPBodyParam_physicalAgeValue_StandartArray{
    
    NSInteger standValue = self.age;
    NSInteger point0 = 0;
    NSInteger point1 = standValue;
    NSInteger point2 = standValue * 2;

    return @[@(point0),@(point1),@(point2)];
}

#pragma mark - 身体得分
- (NSArray *)fetchPPBodyParam_BodyScore_StandartArray{

    return @[@(60),@(70),@(80),@(90),@(100)];

}

#pragma mark - 8电极通用
- (NSArray *)fetchPPBodyParam_StandartArray_8:(NSDictionary *)dic{
    
    NSArray *old = [PPBodyDetailStandardArray descDic2Arr:dic];
    NSArray *standardArray = [self intervalRange:old addBoundary:@[]];
    
    return [self formatter:standardArray];
}


#pragma mark -  数据常量

- (NSArray *)constWeightKg{
    
    CGFloat point1 = self.idealWeight * 0.9f;
    CGFloat point2 = self.idealWeight * 1.1f;

    return @[@(point1),@(point2)];
}

- (NSArray *)constBodyFatRate{
    NSArray *standardArray = @[];
    PPDeviceGenderType gender = self.gender;
    NSInteger age = self.age;
 
    if (gender == PPDeviceGenderTypeMale){
        
        if (age <= 7 ){
            
            standardArray = @[@(7),@(16),@(25),@(30)];
        }else if (age <= 11){
            
            standardArray = @[@(7),@(16),@(26),@(30)];
        }else if (age <= 13){
            
            standardArray = @[@(7),@(16),@(25),@(30)];
        }else if (age <= 14){
            
            standardArray = @[@(7),@(15),@(25),@(29)];
        }else if (age <= 15){
            
            standardArray = @[@(8),@(15),@(24),@(29)];
        }else if (age <= 16){
            
            standardArray = @[@(8),@(16),@(24),@(28)];
        }else if (age <= 17){
            
            standardArray = @[@(9),@(16),@(23),@(28)];
        }else if (age <= 39){
            
            standardArray = @[@(11),@(17),@(22),@(27)];
        }else if (age <= 59){
            
            standardArray = @[@(12),@(18),@(23),@(28)];
        }else{
            
            standardArray = @[@(14),@(20),@(25),@(30)];
        }
        
    }else{
        if (age <= 6 ){
            
            standardArray = @[@(8),@(16),@(25),@(29)];
        }else if (age <= 7){
            
            standardArray = @[@(9),@(17),@(25),@(30)];
        }else if (age <= 8){
            
            standardArray = @[@(10),@(18),@(26),@(31)];
        }else if (age <= 9){
            
            standardArray = @[@(10),@(19),@(28),@(32)];
        }else if (age <= 10){
            
            standardArray = @[@(11),@(20),@(29),@(33)];
        }else if (age <= 11){
            
            standardArray = @[@(13),@(22),@(31),@(35)];
        }else if (age <= 12){
            
            standardArray = @[@(14),@(23),@(32),@(36)];
        }else if (age <= 13){
            
            standardArray = @[@(15),@(25),@(34),@(38)];
        }else if (age <= 14){
            
            standardArray = @[@(17),@(26),@(35),@(39)];
        }else if (age <= 15){
            
            standardArray = @[@(18),@(27),@(36),@(40)];
        }else if (age <= 16){
            
            standardArray = @[@(19),@(28),@(37),@(41)];
        }else if (age <= 17){
            
            standardArray = @[@(20),@(28),@(37),@(41)];
        }else if (age <= 39){
            
            standardArray = @[@(21),@(28),@(35),@(40)];
        }else if (age <= 59){
            
            standardArray = @[@(22),@(29),@(36),@(41)];
        }else{
            
            standardArray = @[@(23),@(30),@(37),@(42)];
        }
        
    }
    return standardArray;
}

- (NSArray *)constWaterRate{
    
    PPDeviceGenderType gender = self.gender;
    NSArray *standardArray = @[];

    if (gender == PPDeviceGenderTypeMale){
        
        standardArray = @[@(55),@(65)];
    }else{
        
        standardArray = @[@(45),@(60)];
    }
    return standardArray;
}

- (NSArray *)constMuscleWeightKg{
    
    PPDeviceGenderType gender = self.gender;
    NSInteger height = self.heightCm;
    CGFloat standValue = 0;
    CGFloat difference = 0;
    if (gender == PPDeviceGenderTypeMale) {
        if (height < 160) {
            
            standValue = 42.5;
            difference = 4.0;
            
        }else if (height > 170){
            
            standValue = 54.5;
            difference = 5.0;
            
        }else{
            
            standValue = 48.2;
            difference = 4.2;
        }
    }else{
        if (height < 150) {
            
            standValue = 31.9;
            difference = 2.8;

        }else if (height > 160){
            
            standValue = 39.5;
            difference = 3.0;

        }else{
     
            standValue = 35.2;
            difference = 2.3;
        }
    }
    
    CGFloat point1 = standValue - difference;
    CGFloat point2 = standValue + difference;
    
    return @[@(point1),@(point2)];
}

- (NSArray *)constBoneWeightKg{
    
    PPDeviceGenderType gender = self.gender;
    CGFloat weight  = self.weight;
    
    CGFloat standValue = 0;
    CGFloat difference = 0.1;
    if (gender == PPDeviceGenderTypeMale) {
        
        if (weight < 60.0) {
            
            standValue = 2.5;
        }else if (weight > 75.0){
            
            standValue = 3.2;
            
        }else{
            standValue = 2.9;
        }
    }
    else{
        if (weight < 45.0) {
            
            standValue = 1.8;
            
        }else if (weight > 60.0){
            
            standValue = 2.5;
            
        }else{
            standValue = 2.2;
        }
    }
    

    CGFloat point1 = standValue - difference;
    CGFloat point2 = standValue + difference;
    
    return @[@(point1),@(point2)];
}

- (NSArray *)constBMR{
    
    PPDeviceGenderType gender = self.gender;
    CGFloat weight  = self.weight;
    CGFloat age  = self.age;

    CGFloat standValue = 0;

    if (gender == PPDeviceGenderTypeMale) {
        
        if (age < 29) {
            
            standValue = weight * 24;
        }
        else if (age < 49){
            
            standValue = weight * 22.3;
        }
        else if (age < 69){
            
            standValue = weight * 21.5;
        }
        else{
            
            standValue = weight * 21.5;
        }
    }
    else{
        if (age < 29) {
            
            standValue = weight * 23.6;
        }
        else if (age < 49){
            
            standValue = weight * 21.7;
        }
        else if (age < 69){
            
            standValue = weight * 20.7;
        }
        else{
            standValue = weight * 20.7;
        }
    }
    
    CGFloat point1 = standValue;
  
    
    return @[@(point1)];
}

- (NSArray *)constproteinPercentage{
    
    return @[@(16),@(18)];
}

- (NSArray *)subcutaneousFatPercentage{
    
    PPDeviceGenderType gender = self.gender;
    
    if (gender == PPDeviceGenderTypeMale){
        
        return @[@(8.6),@(16.7)];
        
    }else{
        
        return @[@(18.5),@(26.7)];
    }
}


- (NSArray *)skeletalMuscleKg{
    
    return @[@(20),@(35)];
}
@end
