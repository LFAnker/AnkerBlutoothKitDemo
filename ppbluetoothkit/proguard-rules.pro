# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class *.R
-keepclasseswithmembers class **.R$* { public static <fields>;}

-keep class com.lefu.body_sl.** {*;}
-keep enum com.lefu.body_sl.** {*;}
-keep class com.peng.ppscale.vo.** {*;}
-keep class com.peng.ppscale.xhscale.vo.** {*;}
-keep class com.peng.ppscale.util.** {*;}

-keep class com.peng.ppscale.business.device.** {*;}
-keep class com.peng.ppscale.business.ble.bmdj.** {*;}
-keep class com.peng.ppscale.business.ble.configWifi.** {*;}
-keep class com.peng.ppscale.business.ble.listener.** {*;}
-keep class com.peng.ppscale.business.state.** {*;}
-keep class com.peng.ppscale.search.PPSearchManager** {*;}
-keep class com.peng.ppscale.search.DeviceFilterHelper** {*;}

-keep class com.peng.ppscale.business.ota.OnOTAStateListener** {*;}
-keep class com.peng.ppscale.business.ota.OTAManager** {*;}
-keep class com.peng.ppscale.business.torre.TorreDeviceManager** {*;}
-keep class com.peng.ppscale.business.torre.listener.** {*;}
-keep class com.peng.ppscale.business.torre.vo.** {*;}

-dontwarn com.inuker.**
-keep class com.inuker.bluetooth.library.** {*;}
-keep class com.besthealth.bhBodyComposition.** {*;}
-keep class com.besthealth.bh1BodyComposition.** {*;}

-keep class com.lefu.gson.** {*;}

-keep class com.peng.ppscale.util.json.** {*;}
-keep class com.peng.ppscale.data.** {*;}
-keep class com.peng.ppscale.business.v4.WifiConfigStep** {*;}

-keep class com.peng.ppscale.PPBlutoothKit** {*;}

-keep class com.peng.ppscale.device.** {
    <methods>;
}

-keep class com.peng.ppscale.business.ble.PPScaleHelper** {*;}




