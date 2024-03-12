//
//  PPBodyDetailInfoModel.m
//  PPBluetoothKit
//
//  Created by 彭思远 on 2023/6/15.
//

#import "PPBodyDetailInfoModel.h"
#import "PPAstonExtension.h"
#import "PPBodyDetailStandardArray.h"


@interface PPBodyDetailInfoModel()



// 对当前所在区间的评价(BMI_leve1_evaluation,BMI_leve2_evaluation,BMI_leve3_evaluation,BMI_leve4_evaluation)
@property (nonatomic,strong) NSArray <NSString *> *evaluationArray;

// 对当前所在区间的建议(BMI_leve1_suggestion,BMI_leve2_suggestion,BMI_leve3_suggestion,BMI_leve4_suggestion)
@property (nonatomic,strong) NSArray <NSString *> *suggestionArray;

@end

@implementation PPBodyDetailInfoModel



- (instancetype)initWithDic:(NSDictionary *)dic
                 andKeyName:(NSString *)key
              standardArray:(NSArray <NSNumber *>*)standardArray
               currentValue:(CGFloat)currentValue
{
    
    self = [super init];
    
    if (self){
        
        NSDictionary *d = [dic valueForKey:key];
        self.bodyParamKey = [d valueForKey:@"bodyParamKey"];;
        self.suggestionArray = [d valueForKey:@"suggestionArray"];
        self.evaluationArray = [d valueForKey:@"evaluationArray"];
        self.standardTitleArray = [d valueForKey:@"standardTitleArray"];
        self.introductionString = [d valueForKey:@"introductionString"];
        self.bodyParamNameString = [d valueForKey:@"bodyParamNameString"];
        self.colorArray = [d valueForKey:@"colorArray"];
        self.watchfulArray = [d valueForKey:@"watchfulArray"];
        self.currentUnit = [d valueForKey:@"currentUnit"];
        
        //当前值
        self.currentValue = currentValue;
        // 是否有区间标准
        BOOL hasStandard = standardArray.count > 0 ? YES:NO;
        
        if (hasStandard){
            
            [self fillWithStandardArray:standardArray currentValue:currentValue];

        }else{
            self.watchfulArray = @[];
            self.standardArray = @[];
            self.currentStandard = 0;
            self.standardTitle = @"";
            self.standColor = @"";
            self.standSuggestion = @"";
            self.standeValuation = @"";
        }
        
    }
    
    return self;
    
}

- (void)fillWithStandardArray:(NSArray <NSNumber *>*)standardArray
                        currentValue:(CGFloat)currentValue{
 
    //范围节点
    self.standardArray = standardArray;
   
    // 当前值坐落的区间
    NSInteger currentStandard = [PPBodyDetailStandardArray calculateCurrentStandardWithValue:currentValue andStandardArray:standardArray];
    self.currentStandard = currentStandard;
    // 对节点区间的描述
    self.standardTitle = [self safeFetchListValueByIndex:currentStandard andList:self.standardTitleArray];
    // 节点区间的颜色
    self.standColor = [self safeFetchListValueByIndex:currentStandard andList:self.colorArray];
    // 对当前所在区间的评价
    self.standSuggestion = [self safeFetchListValueByIndex:currentStandard andList:self.suggestionArray];
    // 对当前所在区间的建议
    self.standeValuation = [self safeFetchListValueByIndex:currentStandard andList:self.evaluationArray];
    
    
}

- (NSString *)safeFetchListValueByIndex:(NSInteger)index andList:(NSArray <NSString *>*)list{
    
    if (index < list.count){
        return [list objectAtIndex:index];
    }

    return list.firstObject;
}

- (NSString *)description{
    
    return [self PPAston_JSONString];
}

@end
