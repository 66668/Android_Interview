# 权限 总结

## 动态权限适配方案，权限组的概念
6.0版本引入动态权限，动态授权方案将系统权限进行了分级。简单点说可以分为普通权限和危险权限。

 正常权限：不会直接给用户隐私权带来风险。如果您的应用在其清单中列出了正常权限，系统将会自动授予该权限。

 危险权限：会授予应用访问用户机密数据的权限。如果您的应用在其清单中列出了危险权限，则用户必须明确批准您的应用使用这些权限。
 
 权限组：只要改组中有任何一个权限被授予，则改组中的其他权限也同样会被授予
 
 权限组有9组：
    
    
    <!--==================1-日历 android.permission-group.CALENDAR 2个==================-->
        <uses-permission android:name="android.permission.READ_CALENDAR" />
        <uses-permission android:name="android.permission.WRITE_CALENDAR" />
    
        <!--==================2-相机 android.permission-group.CAMERA 1个==================-->
        <uses-permission android:name="android.permission.CAMERA" />
    
        <!--==================3-通讯录 android.permission-group.CONTACTS 3个==================-->
    
        <uses-permission android:name="android.permission.READ_CONTACTS" />
        <uses-permission android:name="android.permission.WRITE_CONTACTS" />
        <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    
        <!--==================4-定位 android.permission-group.LOCATION 2个==================-->
        <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    
        <!--==================5-录音 android.permission-group.MICROPHONE 1个==================-->
        <uses-permission android:name="android.permission.RECORD_AUDIO" />
    
        <!--==================6-电话 android.permission-group.PHONE 7个==================-->
        <uses-permission android:name="android.permission.ADD_VOICEMAIL" />
        <uses-permission android:name="android.permission.CALL_PHONE" />
        <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
        <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />
        <uses-permission android:name="android.permission.READ_CALL_LOG" />
        <uses-permission android:name="android.permission.READ_PHONE_STATE" />
        <uses-permission android:name="android.permission.USE_SIP" />
        <uses-permission android:name="android.permission.WRITE_CALL_LOG" />
    
        <!--==================7-传感器 android.permission-group.SENSORS 1个==================-->
        <uses-permission android:name="android.permission.BODY_SENSORS" />
    
        <!--==================8-短信 android.permission-group.SMS 6个==================-->
        <uses-permission android:name="android.permission.READ_CELL_BROADCASTS" />
        <uses-permission android:name="android.permission.SEND_SMS" />
        <uses-permission android:name="android.permission.RECEIVE_MMS" />
        <uses-permission android:name="android.permission.RECEIVE_SMS" />
        <uses-permission android:name="android.permission.RECEIVE_WAP_PUSH" />
        <uses-permission android:name="android.permission.SEND_SMS" />
    
        <!--==================9-存储 android.permission-group.STORAGE 2个==================-->
        <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        

 
 ## 给App授予默认权限不需要权限弹框的方案
 
 参考：https://blog.csdn.net/wangjicong_215/article/details/72821916
 
 1. /system/etc/default-permissions/下添加自己的app和权限的xml文件
 
 在DefaultPermissionGrantPolicy中会去读这个文件，来给app赋予权限
 
 2. 手动安装的第三方app的权限默认开启是修改PackageManagerService.java
 在grantPermissionsLpw函数中添加如下代码:
 
 //Permissions for com.xxx.xxx
  if(pkg.packageName.contains("com.xxx.xxx")) {
           final int permsSize = pkg.requestedPermissions.size();
           for (int i=0; i<permsSize; i++) {
               final String name = pkg.requestedPermissions.get(i);
               final BasePermission bp = mSettings.mPermissions.get(name);
               if(null != bp && permissionsState.grantInstallPermission(bp) != PermissionsState.PERMISSION_OPERATION_FAILURE) {
                   Slog.d(TAG, "zrx--- grant permission " + name + " to package " + pkg.packageName);
                   changedInstallPermission = true;
               }
           }
       }

 
 
    


