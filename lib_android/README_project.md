# 项目难点总结

##  录像帧数据处理

## 应用如何开启自动启动

当Android启动时，会发出一个系统广播，内容为ACTION_BOOT_COMPLETED，字符串"android.intent.action.BOOT_COMPLETED"。只要在程序中“捕捉”到这个消息，再启动之即可
具体步骤：
1. 对应的**Manifest中配置receiver,receiver中添加action=android.intent.action.BOOT_COMPLETED**

权限也要配置：<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"></uses-permission>

        
        <?xml version="1.0" encoding="utf-8"?>
        <!-- 这是一个开机自启动程序 -->
        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
              package="com.ajie.bootstartdemo"
              android:versionCode="1"
              android:versionName="1.0">
            <application android:icon="@drawable/icon" android:label="@string/app_name">
                <activity android:name=".BootStartDemo"
                          android:label="@string/app_name">
                    <intent-filter>
                        <action android:name="android.intent.action.MAIN" />
                        <category android:name="android.intent.category.LAUNCHER" />
                    </intent-filter>
                </activity>
            <receiver android:name=".BootBroadcastReceiver">
                <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <category android:name="android.intent.category.HOME" />
                </intent-filter>
            </receiver>
            </application>
        <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"></uses-permission>
        </manifest> 
        
2. **自定义BroadcastReceiver，在onReceiver中进行监听action,开启对应应用即可**

    


