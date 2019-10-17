# apk安装流程总结

## 流程简介


1. 复制APK安装包到/data/app目录下，解压缩并扫描安装包，
2. 资源管理器解析APK里的资源文件/向资源管理器注入APK资源
3. 解析AndroidManifest文件，并在/data/data目录下创建对应的应用数据目录，
4. 然后针对Dalvik/ART环境优化dex文件，保存到dalvik-cache目录，
5. 将AndroidManifest文件解析出的组件、权限注册到PackageManagerService并**发送广播**。


###（1）拷贝apk到指定的目录：

默认情况下，用户安装的apk首先会拷贝到/data/app下，用户有访问/data/app目录的权限，但系统出厂的apk文件会被放到/system分区下，
包括/system/app，/system/vendor/app，以及/system/priv-app等，该分区需要root权限的用户才能访问。

###（2）加载apk、拷贝文件、创建应用的数据目录：

为了加快APP的启动速度，apk在安装的时候，会首先将APP的可执行文件（dex）拷贝到/data/dalvik-cache目录下，缓存起来。
再在/data/data/目录下创建应用程序的数据目录（以应用包名命令），用来存放应用的数据库、xml文件、cache、二进制的so动态库等。

###（3）解析apk的AndroidManifest.xml文件：

在安装apk的过程中，会解析apk的AndroidManifest.xml文件，将apk的权限、应用包名、apk的安装位置、版本、userID等重要信息保存在/data/system/packages.xml文件中。
这些操作都是在PackageManagerService中完成的。

###（4）显示icon图标：

应用程序经过PMS中的逻辑处理后，相当于已经注册好了，如果想要在Android桌面上看到icon图标，则需要Launcher将系统中已经安装的程序展现在桌面上。

## apk安装流程图

![apk安装流程图](https://github.com/66668/Android_Interview/blob/master/pictures/apk_install_03.png)

![apk安装流程图](https://github.com/66668/Android_Interview/blob/master/pictures/apk_install_01.png)


![apk安装流程图](https://github.com/66668/Android_Interview/blob/master/pictures/apk_install_02.png)




## APK安装方法

APK有下面4种安装方法：

1. 开机过程中安装 	开机时完成，没有安装界面，如系统应用、其它预置应用
2. adb工具安装 	没有安装界面，adb install/push xxxx.apk
3. 第三方应用安装 	通过packageinstaller.apk进行安装，有安装界面，如打开文件管理器并点击sdk卡里APK文件
4. 网络下载应用安装 	通过google market应用完成，没有安装界面

## APK安装目录介绍

1. 系统安装目录有：


    /system/framework 系统库；

    /system/app 默认的系统应用；

    system/vendor/app 厂商定制的应用；

2. 非系统apk信息目录有：


    /data/app/；

    /system/priv-app/；

    /data/app-private/；

