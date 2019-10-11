# android 打包流程总结

## 为什么要加签名机制。
1. 发送者的身份认证：由于开发商可能通过使用相同的 Package Name 来混淆替换已经安装的程序，以此保证签名不同的包不被替换
2. 保证信息传输的完整性：签名对于包中的每个文件进行处理，以此确保包中内容不被替换
3. 防止交易中的抵赖发生：Market（应用市场）对软件的要求

**给apk签名可以带来以下好处**：

1. 应用程序升级：能无缝升级到新的版本，必须要同一个证书进行签名并且包名称要相同。（如果证书不同，可能会被系统认为是不同的应用）
2. 应用程序模块化：Android系统可以允许同一个证书签名的多个应用程序在一个进程里运行（系统实际把他们作为一个单个的应用程序），此时就可以把我们的应用程序以模块的方式进行部署，而用户可以独立的升级其中的一个模块
3. 代码或者数据共享：Android提供了基于签名的权限机制，那么一个应用程序就可以为另一个以相同证书签名的应用程序公开自己的功能。 

## 安装的7大步骤

![apk打包流程图](https://github.com/66668/Android_Interview/blob/master/pictures/apk_package_01.png)

### 1. 打包资源文件，生成R.java文件

aapt来打包res资源文件，生成R.java、resources.arsc和res文件。

1. res目录

文件 	    描述

animator 	放属性动画

anim 	    放补间动画

color 	     颜色资源

drawable 	存放XML、Bitmap文件，或者.png, .9.png, .jpg, .gif文件等图像资源。打包过程中可能会被优化，比如色彩数可能会被优化，256色变8色。

layout 	    布局资源，布局的xml文件。

menu 	     应用程序菜单

raw 	  直接复制到设备中的任意文件。用参数是资源的ID调用，R.raw.somefilename。和assets 不同的是，raw下面不能有目录，而和assets可以有目录。

values 	   数值XML文件用来描述数组、颜色、尺寸、字符串和样式值等

xml 	   应用配置信息

2. R.java文件:R.java是我们在编写代码的时候会用到的，里面有静态内部类，资源等。

3. resources.arsc文件:resources.arsc这个文件记录了所有的应用程序资源目录的信息，包括每一个资源名称、类型、值、ID以及所配置的维度信息。是一个资源索引表，在给定资源ID和设备配置信息的情况下能快速找到资源。

### 2.处理aidl文件，生成相应的Java文件

aidl（Android Interface Definition Language，Android接口描述语言），位于android-sdk/platform-tools目录下。aidl工具解析接口定义文件然后生成相应的Java代码接口供程序调用。如果项目没用到aidl则跳过这一步。
### 3.编译项目源代码，生成class文件

Java Compiler阶段。项目中所有的Java代码，包括R.java和.aidl文件，都会变Java编译器（javac）编译成.class文件，生成的class文件位于工程中的bin/classes目录下。
### 4.转换所有的class文件，生成classes.dex文件

dex阶段。通过dx工具，将.class文件和第三方库中的.class文件处理生成classes.dex文件。该工具位于android-sdk/platform-tools 目录下。dx工具的主要工作是将Java字节码转成成Dalvik字节码、压缩常量池、消除冗余信息等。
### 5.打包生成APK文件

apkbuilder阶段。通过apkbuilder工具，将aapt生成的resources.arsc和res文件、assets文件和classes.dex一起打包生成apk。打包的工具apkbuilder位于 android-sdk/tools目录下。
### 6.对APK文件进行签名

Jarsigner阶段。通过Jarsigner工具，对上面的apk进行debug或release签名。
### 7. 对签名后的APK文件进行对齐处理

通过zipalign工具，将签名后的apk进行对齐处理。工具位于android-sdk/tools目录下。对齐的主要过程是将APK包中所有的资源文件距离文件起始偏移为4字节整数倍，这样通过内存映射访问apk文件时的速度会更快。对齐的作用就是减少运行时内存的使用。



## apk打包问题
1. 

## apk详细打包流程图

![apk详细打包流程图](https://github.com/66668/Android_Interview/blob/master/pictures/apk_package_02.png)