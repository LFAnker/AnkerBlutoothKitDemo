//
//  XM_UnitTool.swift
//  XMProject
//
//  Created by  lefu on 2023/3/11.
//

import UIKit
import PPBluetoothKit

class XM_UnitTool{
    
    class func XM_HeightConvert(feet:Int, inch:Int) -> Double{
        
        let feet = feet * 12 + inch
        
        let height = Double(feet)  * 0.0254
    
        return height * 100
        
    }
    
    
}



extension String{
    
    
}

extension Float{

}

extension Float{
    
    
    /// 把cm转换为英尺英寸格式
    /// - Returns: x'xx''
    func convertToFeetInchesString() -> String {
        let heightInInches = self / 2.54 // 将身高厘米数转换为英寸数
        let totalInches = Int(heightInInches.rounded()) // 取出总英寸数并将其四舍五入为整数
        let feet = totalInches / 12 // 取出英尺部分
        let inches = totalInches % 12 // 取出英寸部分
        let heightString = "\(feet)'\(inches)\"" // 将英尺和英寸部分组合成字符串
        return heightString
    }
    
    /// 把cm转换为英尺英寸格式
    /// - Returns: (x,xx)
    func convertToFeetInchesNumber() -> (Int, Int) {
        let heightInInches = self / 2.54 // 将身高厘米数转换为英寸数
        let totalInches = Int(heightInInches.rounded()) // 取出总英寸数并将其四舍五入为整数
        let feet = totalInches / 12 // 取出英尺部分
        let inches = totalInches % 12 // 取出英寸部分
        return (feet, inches)
    }
    
    func toUnitStr(unitType:Int, accuracyType:Int, forWeight:Bool = false)->String{
        
        let unit = UnitType.init(rawValue: unitType)
        
        let deviceType = PPDeviceAccuracyType(rawValue: UInt(accuracyType))
        
        switch(unit){
            
        case .Unit_KG:
            
            if(deviceType == .point005){
                
                if forWeight{
                    
                    if self > 100{
                        return String.init(format: "%0.1f", floor(self *   10) / 10)
                    }
                    
                    return String.init(format: "%0.2f", self)
                }else{
                    
                    return String.init(format: "%0.1f", self)
                }
          
            }else{
                return String.init(format: "%0.1f", self)
                
            }
            
        case .Unit_lb:
            if(deviceType == .point005){
                
                
                let weight = roundf(self * 100 + 5) / 10
                
                let  roundWeightInt = Int(weight)
                
                let floorWeightFloat = Float(roundWeightInt) / 10
                
                
                let floorWeight =  floorf(floorWeightFloat * 10 * 22046 / 10000)
                
                let floorWeightInt = Int(floorWeight)
                
                let floorWeightF = Float(floorWeightInt) / 10
                
                return String.init(format: "%0.1f", floorWeightF)
                
                
            }else{
                
                var floorWeight = self * 10 * 22046 / 10000
                
                
                let intFloor = Int(floorf(floorWeight))
                
                if(intFloor % 2 > 0){
                    floorWeight = floorWeight + 1
                }
                
                let weight = Float(floorWeight) / 10
                
                return String.init(format: "%0.1f", weight)
                
            }

        case .Unit_st:
            
            if(deviceType == .point005){
              
                let weight = roundf(self * 100 + 5)/10
                
                let  roundWeightInt = Int(weight)
                
                let floorWeightFloat = Float(roundWeightInt) / 10
                
                
                let lb =  floorWeightFloat * 10 * 22046 / 1000
                
                
                let st = Int(floorf(lb / 14))
                
                let stFloat = Float(st) / 100
                
                
                return String.init(format: "%0.2f", stFloat)
                
            }else{
                let lb = self * 10 * 22046 / 10000
                
                var st = Int(floor(lb / 14))
                
                
                if(st % 2 != 0){
                    st = st + 1
                }
                
                let stFloat = Float(st) / 100
                
                return String.init(format: "%0.2f", stFloat)
            }
            
        case .Unit_Jin:
            if(deviceType == .point005){
                
                return String.init(format: "%0.2f", self*2)
            }else{
                return String.init(format: "%0.1f", self*2)
                
            }
        case .Unit_stlb:
            
            if(deviceType == .point005){
                
                let weight = roundf(self * 100 + 5) / 10
                
                let  roundWeightInt = Int(weight)
                
                let floorWeightFloat = Float(roundWeightInt) / 10
                
                let floorWeight =  floorf(floorWeightFloat * 10 * 22046 / 10000)
                
                let floorWeightInt = Int(floorWeight)
                
                let stPart = floorWeightInt / 140
                
                let lbPart =  Float(floorWeightInt).truncatingRemainder(dividingBy: 140.0) / 10.0

                return String.init(format: "%ld:%.1f", stPart,lbPart )
                
            }else{
                
                var floorWeight = self * 10 * 22046 / 10000

                let intFloor = Int(floorf(floorWeight))
                
                if(intFloor % 2 > 0){
                    
                    floorWeight = floorWeight + 1
                }
                
                let floorWeightInt = Int(floorWeight)
                
                let stPart = floorWeightInt / 140
                
                let lbPart =  Float(floorWeightInt).truncatingRemainder(dividingBy: 140.0) / 10.0

                return String.init(format: "%ld:%.1f", stPart,lbPart )
            }
            
        case .none:
            
            return ""
        }
    }
        
    func toUnitNumber(unitType:Int)->(Float, String){
        
        let unit = UnitType.init(rawValue: unitType)
                
        switch(unit){
            
        case .Unit_KG:
                
            return (self, "")
            
            
        case .Unit_lb:
         
            return (self * 2.2046, "")
            
        case .Unit_st:
          
            return (self * 0.157473, "")

            
        case .Unit_Jin:
        
            return (self * 2, "")
      
        case .Unit_stlb:
            
            let lb:Float = self * 2.2046
            
            let stPart:Int = Int(lb / 14)
            
            let lbPart = lb - Float(stPart * 14)
            
            let lbStr = String.init(format: "%.1f", lbPart)
            
            if lbStr.contains("14"){
            
                let stlbStr = String.init(format: "%ld:0.0", stPart + 1)
                return (-1, stlbStr)
            }else{
                
                let stlbStr = String.init(format: "%ld:%@", stPart,lbStr)
                return (-1, stlbStr)
            }

        case .none:
            return (self, "")
        }
    }
    
    func toKg(unitType:Int, stlb:String = "")->Float{
        
        let unit = UnitType.init(rawValue: unitType)
                
        switch(unit){
            
        case .Unit_KG:
                
            return self
            
            
        case .Unit_lb:
                
            return self / 2.2046
            
            
        case .Unit_st:
          
            return self / 0.157473
            
        case .Unit_Jin:
        
            return self / 2
      
        case .Unit_stlb:
            
            if stlb.contains(":"){
                
                let components = stlb.components(separatedBy: ":")
                if components.count == 2 {
                    if let stones = Float(components[0]), let pounds = Float(components[1]) {
                        let kg = (stones * 14 + pounds) / 2.2046
                        return kg
                    }
                }
            }
            
            return self * 0.157473

        case .none:
            return self
        }
    }
}


extension Float{
    ///当前用户体重转换显示，不带单位
    func toCurrentUserString(accuracyType : Int, unitType:Int = CommonDataManager.shared.commonData.unitType, forWeight:Bool = false)->String{
        
        if UserManager.shared.currentUser != nil{
            return self.toUnitStr(unitType: unitType, accuracyType: accuracyType, forWeight: forWeight)

        }
        
        return ""
    }
    ///当前用户体重转换显示，带单位
    func toCurrentUserStringWithUnit(accuracyType : Int, forWeight:Bool = false)->String{
        
        if let user = UserManager.shared.currentUser{
            
            let unitType = CommonDataManager.shared.commonData.unitType
            
            let ss = self.toUnitStr(unitType: unitType, accuracyType: accuracyType, forWeight: forWeight) + " " + user.unitStr
            return ss
        }
        
        return ""

    }
    
}
