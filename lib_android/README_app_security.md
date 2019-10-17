# app安全优化总结 

# 提高app安全性的方法? 

1. 软键盘输入安全：自定义软键盘，随机按键，并且对输出作加密处理。
2. 防界面劫持：有敏感信息录入的界面，有可能会出现被钓鱼，出现一模一样的界面覆盖于咱们的应用之上，导致用户误输入敏感信息，出现安全问题，
    那么我们要做的就是在应用退出前台时给用户提示，防止用户再输入敏感信息。
3. 本地数据安全：对存储于本地的数据作加密之后再存储，对于特别数据采取分段存储等措施，比如密钥可分段存储或存储于so等。
4. 通信安全：数据加密、证书验证（证书锁定（判断服务器证书公钥与本地证书公钥是否一致） +域名验证（））等
5. 重点逻辑代码安全：方案一实现代码混淆处理，方案二将逻辑代码由Java层转到jni层实现。
6. apk包安全：
(1)apk混淆加固:使用混淆保护,对APK代码进行基础的防护
(2)反二次打包，检验apk的签名指纹,可以通过在原生层验证签名来实现（其代码在JAVA层）。
(3)版本更新时校验apk文件的唯一性和完整性；
9. 处理编译后的二进制AndroidManifest.xml文件，添加无效的参数，使反编译得到错误的清单文件。
10. 第三方平台：爱加密（加壳技术，对dex文件做了一层保护壳，），360加固等。


# Android APP加固思路（同上）

原文链接：https://blog.csdn.net/makaisghr/article/details/73302696

### **1. 源码保护**

1. Dex文件保护

对源文件使用加壳技术实现Dex文件保护，即隐藏原本的Dex文件，然后生成一个Dex壳文件放到APK中

2. 防二次打包

在应用程序内加入代码验证自我的签名是否被篡改。常用验证策略：1.Java层签名验证，2.服务器验证（在Android的Java层获取签名信息，上传服务器在服务端进行签名然后返回验证结果），3.NDK技术底层获取签名和验证，4.自定义指纹验证

3. so文件保护

(1).对ELF文件加壳，

(2).对Android系统中so文件的加载、调用机制做处理

4. 资源文件保护

(1)对APK中资源文件名使用简短无意义名称进行替换 

(2)修改resources.arsc DataType数据类型来促使apktool无法直接反编译资源文件

### **2. 应用加密**

1. Log日志输出屏蔽

通过配置proguard，将类android.util.Log的方法置为为无效代码。

2. 清场技术

依赖于云端的黑白名单库，检测手机运行环境，发现异常则停止运行并提示用户

### **3. 数据安全**

1. 页面防劫持

在所有Activity的onPause()方法中，弹出提示用户的警告，延迟1-2秒执行；在所有Activity的onResume()方法中，取消在onPause中的提示

3. 本地数据保护

(1)将文件放在尽量安全的位置，比如内部存储而不是sd卡。(2)对文件内容使用对称加密或基于口令的加密。(3)对于数据库可用相关工具加密比如SQLCiper。(4)使用Android设备管理策略。(5)使用加密的SharePreference即Secure-Preferences



3. 截屏保护

(1)可以使用Hook技术监控截屏相关函数的调用。 (2)可以在界面中添加代码防止页面截屏。

4. 内存数据防查询

对当前进程的so进行排查，存在非本包名下的so即认为被注入了。当然，如果是存在合作伙伴的so，也可以加入白名单机制。

5. 协议加密

使用TLS

蓝牙端可使用的SDK http://www.btsdk.com/develop/index.html

推荐使用加固方案：通付盾、 梆梆安全、爱加密、阿里聚、360。


# 安卓的app加固如何做? 

对App进行加固，可以有效防止移动应用被破解、盗版、二次打包、注入、反编译等，保障程序的安全性、稳定性。对于金融类App，尤其重要。

### 对App dex进行加固的基本步骤如下：
 
1. 从App原始apk文件里获取到原始dex文件 
2. 对原始dex文件进行加密，并将加密后的dex文件和相关的存放到assert目录里 
3. 用脱壳dex文件替换原始apk文件里的dex文件；脱壳dex文件的作用主要有两个，一个是解密加密后的dex文件；二是基于自定义dexclassloader动态加载解密后的dex文件 
4. 因为原始apk文件已经被修改，所以需要删除原始apk的签名信息，即删除META-INF目录下的.RSA、.SF 和MANIFEST.MF文件 
5. 生成加固后的apk文件 
6. 对加固后的apk文件进行签名，apk加固完成。

### 原理分析

1. 为什么要对原始dex进行加密，同时用脱壳dex文件替换原始dex文件？

大部分的apk反编译工具（dex2jar、apktools、jui等）都是对dex文件进行反编译，将dex文件反编译成smail，然后再转化成class文件进行阅读和修改。
用脱壳dex替换原始dex文件之后，用上面的反编译工具反编译apk文件，只能看到脱壳程序的class文件，看不到apk本身的class文件。对dex文件进行加密，
这样即使第三方拿到了dex文件，因为无法解密，也就无法对其进行解析和分析。 

2. 怎么确保apk功能正常运行？加固后的apk启动之后，脱壳dex文件会对加密后的dex文件进行解密，然后基于自定义dexclassloader动态加载解密后的dex文件。
从用户的角度，加固前后App的功能和体验基本是一样的。这个和插件化的原理是一样的。

可以参见 http://blog.csdn.net/jiangwei0910410003/article/details/48104581 

3. dex加固主要是防止被静态反编译，进而获取源码并修改
            
            
            1.app加固需要3个对象：
            
            1）需要加密的源apk
            
            2）壳程序apk(负责解密源apk)
            
            3）加密工具（将源apk进行加密；和壳Dex合成新的Dex）
            
             
            
            2.app加固需要用到的知识点：
            
            1）对dex文件格式的理解和熟悉
            
            2）熟悉apk打包流程
            
            3）熟悉反编译的流程
            
            4）对android虚拟机底层有一定的理解和apk在android平台上的加载机制
   

# app混淆原理

混淆介绍

Proguard是一个Java类文件压缩器、优化器、混淆器、预校验器。压缩环节会检测以及移除没有用到的类、字段、方法以及属性。优化环节会分析以及优化方法的字节码。混淆环节会用无意义的短变量去重命名类、变量、方法。这些步骤让代码更精简，更高效，也更难被逆向（破解）。

混淆后默认会在工程目录app/build/outputs/mapping/release（debug）下生成一个mapping.txt文件，这就是混淆规则，我们可以根据这个文件把混淆后的代码反推回源本的代码，所以这个文件很重要，注意保护好。原则上，代码混淆后越乱越无规律越好，但有些地方我们是要避免混淆的，否则程序运行就会出错。
ProGuard常用操作
压缩(Shrinking)

压缩(Shrinking)：默认开启，用以减小应用体积，移除未被使用的类和成员，并且会在优化动作执行之后再次执行（因为优化后可能会再次暴露一些未被使用的类和成员）。

#关闭压缩
-dontshrink

优化（Optimization）

优化（Optimization）：默认开启，在字节码级别执行优化，让应用运行的更快。

                
                #关闭优化
                #-dontoptimize  
                
                #表示proguard对代码进行迭代优化的次数，Android一般为5
                -optimizationpasses n  

混淆（Obfuscation）

混淆（Obfuscation）：默认开启，增大反编译难度，类和类成员会被随机命名，除非用keep保护。


                -dontobfuscate  #关闭混淆

-Keep

一颗星表示只是保持该包下的类名，而子包下的类名还是会被混淆；

-keep class pr.tongson.bean.*

两颗星表示把本包和所含子包下的类名都保持；

-keep class pr.tongson.bean.**

（上面两种方式保持类后，会发现类名虽然未混淆，但里面的具体方法和变量命名还是变了）

既可以保持该包下的类名，又可以保持类里面的内容不被混淆;

-keep class pr.tongson.bean.*{*;}

既可以保持该包及子包下的类名，又可以保持类里面的内容不被混淆;

-keep class pr.tongson.bean.**{*;}

保持某个类名不被混淆（但是内部内容会被混淆）

-keep class pr.tongson.bean.KeyBoardBean

保持某个类的 类名及内部的所有内容不会混淆

-keep class pr.tongson.bean.KeyBoardBean{*;}

保持类中特定内容，而不是所有的内容可以使用如下：

-keep class pr.tongson.bean.KeyBoardBean{
  #匹配所有构造器
  <init>;
  #匹配所有域
  <fields>;
  #匹配所有方法
  <methods>;
}

（上面就保持住了KeyBoardBean这个类中的所有的构造方法、变量、和方法）

可以在<fields>或<methods>前面加上private 、public、native等来进一步指定不被混淆的内容

-keep class pr.tongson.algorithm.Calculate{
  #保持该类下所有的共有方法不被混淆
  public <methods>;
  #保持该类下所有的共有内容不被混淆
  public *;
  #保持该类下所有的私有方法不被混淆
  private <methods>;
  #保持该类下所有的私有内容不被混淆
  private *;
  #保持该类的String类型的构造方法
  public <init>(java.lang.String);
}

在方法后加入参数，限制特定的方法(经测试：仅限于构造方法可以混淆)

-keep class pr.tongson.algorithm.Calculate{
      public <init>(String);
}

要保留一个类中的内部类不被混淆需要用 $ 符号

#保持Calculate中的MyClass不被混淆
-keep class pr.tongson.algorithm.Calculate$MyClass{*;}

使用Java的基本规则来保护特定类不被混淆，比如用extends，implement等这些Java规则，如下：保持Android底层组件和类不要混淆

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View

如果不需要保持类名，只需要保持该类下的特定方法保持不被混淆，需要使用keepclassmembers，而不是keep，因为keep方法会保持类名。

#保持ProguardTest类下test(String)方法不被混淆
-keepclassmembernames class pr.tongson.algorithm.Calculate{
  public void test(java.lang.String);
}

如果拥有某成员，保留类和类成员

-keepclasseswithmembernames class pr.tongson.algorithm.Calculate

注意事项
jni方法

jni方法不可混淆，因为native方法是要完整的包名类名方法名来定义的，不能修改，否则找不到；

#保持native方法不被混淆
-keepclasseswithmembernames class * {    
  native <methods>; 
}

反射

反射用到的类混淆时需要注意：只要保持反射用到的类名和方法即可，并不需要将整个被反射到的类都进行保持
AndroidMainfest中的类

AndroidMainfest中的类不混淆，所以四大组件和Application的子类和Framework层下所有的类默认不要进行混淆。
自定义的View

自定义的View默认也不会被混淆
JSON对象类

与服务端交互时，使用GSON、fastjson等框架解析服务端数据时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象；
第三方

使用第三方开源库或者引用其他第三方的SDK包时，如果有特别要求，也需要在混淆文件中加入对应的混淆规则；官方文档一般都有混淆规则的，复制粘贴下即可。
有用到WebView的JS

有用到WebView的JS调用也需要保证写的接口方法不混淆，原因和第一条一样；
Parcelable的子类和Creator静态成员变量

Parcelable的子类和Creator静态成员变量不混淆，否则会产生Android.os.BadParcelableException异常；

-keep class * implements Android.os.Parcelable { 
  # 保持Parcelable不被混淆            
  public static final Android.os.Parcelable$Creator *;
}

enum

使用enum类型时需要注意避免以下两个方法混淆，因为enum类的特殊性，以下两个方法会被反射调用，见第二条规则。

-keepclassmembers enum * {  
  public static **[] values();  
  public static ** valueOf(java.lang.String);  
}

注解不能混淆
建议

建议：
发布一款应用除了设minifyEnabled为ture，你也应该设置zipAlignEnabled为true，像Google Play强制要求开发者上传的应用必须是经过zipAlign的，zipAlign可以让安装包中的资源按4字节对齐，这样可以减少应用在运行时的内存消耗。


# 签名理解（README_apk_package.md）

## 为什么要加签名机制。
1. 发送者的身份认证：由于开发商可能通过使用相同的 Package Name 来混淆替换已经安装的程序，以此保证签名不同的包不被替换
2. 保证信息传输的完整性：签名对于包中的每个文件进行处理，以此确保包中内容不被替换
3. 防止交易中的抵赖发生：Market（应用市场）对软件的要求

**给apk签名可以带来以下好处**：

1. 应用程序升级：能无缝升级到新的版本，必须要同一个证书进行签名并且包名称要相同。（如果证书不同，可能会被系统认为是不同的应用）
2. 应用程序模块化：Android系统可以允许同一个证书签名的多个应用程序在一个进程里运行（系统实际把他们作为一个单个的应用程序），此时就可以把我们的应用程序以模块的方式进行部署，而用户可以独立的升级其中的一个模块
3. 代码或者数据共享：Android提供了基于签名的权限机制，那么一个应用程序就可以为另一个以相同证书签名的应用程序公开自己的功能。 

