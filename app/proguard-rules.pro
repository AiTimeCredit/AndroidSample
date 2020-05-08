# --------------------------------------------基本指令区--------------------------------------------#
-optimizationpasses 5                                                   # 指定代码的压缩级别(在0~7之间，默认为5)
-dontusemixedcaseclassnames                                             # 是否使用大小写混合(windows大小写不敏感，建议加入)
-dontskipnonpubliclibraryclasses                                        # 是否混淆非公共的库的类
-dontskipnonpubliclibraryclassmembers                                   # 是否混淆非公共的库的类的成员
-dontpreverify                                                          # 混淆时是否做预校验(Android不需要预校验，去掉可以加快混淆速度)
-verbose                                                                # 混淆时是否记录日志(混淆后会生成映射文件)

#指定外部模糊字典
#-obfuscationdictionary dictionary.txt
#指定class模糊字典
#-classobfuscationdictionary dictionary.txt
#指定package模糊字典
#-packageobfuscationdictionary dictionary.txt

# 混淆时所采用的算法(谷歌推荐算法)
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable

# 将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile

# 保持注解不被混淆
-keepattributes *Annotation*
-keep public class * extends java.lang.annotation.Annotation {*;}

# 保持泛型不被混淆
-keepattributes Signature
# 保持反射不被混淆
-keepattributes EnclosingMethod
# 保持异常不被混淆
-keepattributes Exceptions
# 保持内部类不被混淆
-keepattributes InnerClasses
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# --------------------------------------------默认保留区--------------------------------------------#
# 保持基本组件不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.os.IInterface
-keep public class * extends android.os.ResultReceiver
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.appwidget.AppWidgetProvider
-keep public class * extends android.webkit.*{*;}
-keep public class * extends android.widget.*{*;}
-keep public class * extends android.app.*{*;}

# 保留 NotProguard 注解
-keep class com.proguard.annotation.NotProguard
-keep @com.proguard.annotation.NotProguard class * {*;}
-keepclasseswithmembers class * {
    @com.proguard.annotation.NotProguard <methods>;
}
-keepclasseswithmembers class * {
    @com.proguard.annotation.NotProguard <fields>;
}
-keepclasseswithmembers class * {
    @com.proguard.annotation.NotProguard <init>(...);
}

# 保持 Google 原生服务需要的类不被混淆
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# Support 包规则
-dontwarn android.support.**
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留自定义控件(继承自View)不被混淆
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留指定格式的构造方法不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留在Activity中的方法参数是view的方法(避免布局文件里面onClick被影响)
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持R(资源)下的所有类及其方法不能被混淆
-keep class **.R$* {*;}

# 保持BuldConfig下的所有属性不能被混淆
-keep class **.BuildConfig {*;}

# 保持GlideModelConfig不被混淆
-keep class * extends com.bumptech.glide.module.AppGlideModule
-keep class * extends com.bumptech.glide.module.LibraryGlideModule

# 保持 Parcelable 序列化的类不被混淆(注：aidl文件不能去混淆)
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 需要序列化和反序列化的类不能被混淆(注：Java反射用到的类也不能被混淆)
-keepnames class * implements java.io.Serializable

# 保持 Serializable 序列化的类成员不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保持使用 @Keep 注解的类不被混淆
-dontwarn android.support.annotation.Keep
-keep @android.support.annotation.Keep class *  {*;}

# 保持 BaseAdapter 类不被混淆
-keep public class * extends android.widget.BaseAdapter {*;}
# 保持 CusorAdapter 类不被混淆
-keep public class * extends android.widget.CusorAdapter{*;}

# --------------------------------------------webView区--------------------------------------------#
# WebView处理，项目中没有使用到webView忽略即可
# 保持Android与JavaScript进行交互的类不被混淆
-keep class **.AndroidJavaScript {*;}
-keep public class android.webkit.**
-keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
     public boolean *(android.webkit.WebView,java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebChromeClient {
     public void *(android.webkit.WebView,java.lang.String);
}

# 网络请求相关
-keep public class android.net.http.SslError

# --------------------------------------------JNI/JRI区--------------------------------------------#
-keep class **JNI* {*;}
# 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
-keepclasseswithmembers class * {
    ... *JNI*(...);
}
# 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除
-keepclasseswithmembernames class * {
	... *JRI*(...);
}

# --------------------------------------------实体--------------------------------------------#
 -keep class com.android.sample.entity.** {*;}

# --------------------------------------------删除代码区--------------------------------------------#
# 删除代码中Log相关的代码
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# --------------------------------------------可定制化区--------------------------------------------#

# --------------------------------------------第三方依赖开始--------------------------------------------#
# DataBinding
-keep class android.databinding.** {*;}
-keep class *.databinding.** {*;}
-dontwarn android.databinding.**

# Okio
-dontwarn okio.**

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Retrofit
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn retrofit2.**
-keep class retrofit2.** {*;}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode {*;}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Gson
-dontwarn sun.misc.**
-keep class sun.misc.Unsafe {*;}
-keep class com.google.gson.stream.** {*;}
-keep class com.google.gson.examples.android.model.** {*;}
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# fastjson
-keepattributes Signature
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** {*;}

# ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider

# RxJava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


# ImmersionBar
 -keep class com.gyf.barlibrary.** {*;}

# Razorpay
-dontwarn com.razorpay.**
-keep class com.razorpay.** {*;}
-optimizations !method/inlining/*
-keepclasseswithmembers class * {
    public void onPayment*(...);
}

# HyperSnapSDK
-dontwarn  co.hyperverge.**
-keepclassmembers class * implements javax.net.ssl.SSLSocketFactory {
    private javax.net.ssl.SSLSocketFactory delegate;
}

# InstallReferrer
-dontwarn com.android.installreferrer
# InstallReferrer
-keep class com.appsflyer.** {*;}

# --------------------------------------------第三方依赖结束--------------------------------------------#

