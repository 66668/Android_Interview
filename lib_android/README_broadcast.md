# android 广播 总结

## 广播原理

  1. 广播接收者BroadcastReceiver通过Binder机制向AMS(Activity Manager Service)进行注册；
  2. 广播发送者通过binder机制向AMS发送广播；
  3. AMS查找符合相应条件（IntentFilter/Permission等）的BroadcastReceiver
  4. AMS将广播发送到上述符合条件的BroadcastReceiver相应的消息循环队列中
  5. BroadcastReceiver通过消息循环执行拿到此广播，回调BroadcastReceiver中的onReceive()方法。
  

## 广播分类：主要分为一下四类：
 1. Normal Broadcast(普通广播/无需广播)：通常调用sendBroadcast(Intent)(Intent, String)方法发送
 2. System Broadcast(系统广播)：发生各种事件时，系统自动发送
 3. Ordered Broadcast(有序广播)：调用sendOrderedBroadcast(Intent, String)方法发送
 4. Local Broadcast(本地广播)：调用LocalBroadcastManager.sendBroadcast(intent)方法发送
 5.   Sticky Broadcast(粘性广播)：已弃用(API 21)

# 发送广播--基本使用和区别

## 1. 无序广播：

    /**
     * 发送无序广播
     */
    private void sendByNormal() {
        Intent intent = new Intent();
        intent.setAction("sjy_send_normal");
        //设置接收的包名（否则接收不到）
        intent.setPackage("com.sjy.broadcastdemo");
        //最普通的发送方式
        sendBroadcast(intent);
        //附带权限的发送方式，声明此权限的BroadcastReceiver才能接收此广播
        //        sendBroadcast(intent, "sjy_send_normal");

        //以下两种不常见，是因为只有预装在系统映像中的程序才能使用，否则无法使用
        //指明接收人的发送方式
        //        sendBroadcastAsUser(intent, null);
        //指明接收人以及对应权限的发送方式
        //        sendBroadcastAsUser(intent, null, "");

    }
    
    
    
    

## 2. 有序广播

1. 按顺序接收: priority值不同：由大到小排序; priority值不同：由大到小排序
2. 允许优先级高的BroadcastReceiver截断广播: abortBroadcast()
3. 允许优先级高的BroadcastReceiver修改广播

1. 主界面发送：
        
        
         /**
             * 发送有序广播
             */
            private void sendByOrdered() {
                Toast.makeText(this, "发送有序广播", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                //定义广播事件类型
                intent.setAction("sjy_send_order");
                //设置接收的包名（否则接收不到）
                intent.setPackage("com.sjy.broadcastdemo");
                // 发送有序广播
                sendOrderedBroadcast(intent, null);
            }
            
2. 创建接收器：

            
            public class OrderReceiverOne extends BroadcastReceiver {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d("SJY", "接收有序广播1");
                }
            }
            
            
            public class OrderReceiverTwo extends BroadcastReceiver {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d("SJY", "接收有序广播2");
                    abortBroadcast();
                    Log.d("SJY", "拦截有序广播");
                }
            }
            
            public class OrderReceiverThree extends BroadcastReceiver {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Log.d("SJY", "接收有序广播3");
                }
            }

3. AndroidManifest.xml添加注册：

        
        
          <!--有序广播示例-->
                <receiver
                    android:name=".order.OrderReceiverOne"
                    android:enabled="true"
                    android:exported="true">
                    <intent-filter android:priority="1000">
                        <action android:name="sjy_send_order" />
                    </intent-filter>
                </receiver>
        
                <receiver
                    android:name=".order.OrderReceiverTwo"
                    android:enabled="true"
                    android:exported="true">
                    <intent-filter android:priority="600">
                        <action android:name="sjy_send_order" />
                    </intent-filter>
        
                </receiver>
                <receiver
                    android:name=".order.OrderReceiverThree"
                    android:enabled="true"
                    android:exported="true">
                    <intent-filter android:priority="100">
                        <action android:name="sjy_send_order" />
                    </intent-filter>
        
                </receiver>
                
  


## 3. 本地广播

由于之前的广播都是全局的，所有应用程序都可以接收到，这样就很容易会引起安全性的问题，比如说我们发送一些携带关键性数据的广播有可能被其他的应用程序截获，
或者其他的程序不停地向我们的广播接收器里发送各种垃圾广播。为了能够简单地解决广播的安全性问题，Android引入了一套本地广播机制，
**使用这个机制发出的广播只能够在应用程序的内部进行传递，并且广播接收器也只能接收来自应用程序发出的广播，这样所有的安全性问题就都不存在了**。
            
                LocalBroadcastManager localBroadcastManager;
                LocalReceiver localReceiver;
            
                private void sendByLocal() {
                    //（1）发送设置
            
                    //LocalBroadcastManager实例化
                    localBroadcastManager = LocalBroadcastManager.getInstance(this);
                    Intent intent = new Intent("com.nyl.orderlybroadcast.AnotherBroadcastReceiver");
                    //发送本地广播
                    localBroadcastManager.sendBroadcast(intent);
            
            
                    //（2）接收设置
                    IntentFilter intentFilter = new IntentFilter();
                    //设置接收广播的类型
                    intentFilter.addAction("com.nyl.orderlybroadcast.AnotherBroadcastReceiver");
                    localReceiver = new LocalReceiver();
                    // 动态注册注册本地接收器
                    localBroadcastManager.registerReceiver(localReceiver, intentFilter);
            
            
                    //(3)取消注册(onDestory设置)
            //        localBroadcastManager.unregisterReceiver(localReceiver);
                }
            
                private class LocalReceiver extends BroadcastReceiver {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Toast.makeText(context, "收到本地广播", Toast.LENGTH_SHORT).show();
                    }
                }


## 4. 系统广播
发生各种事件时，系统自动发送
监听系统广播，需要在AndroidManifest中申请权限，此外，Android高版本系统对于一些重要的系统广播，比如开机启动，
网络连接，电量变化，锁屏等做了限制，如果需要监听这些广播，需要做系统兼容性处理。



###  注意事项
    
1. 本地广播无法通过静态注册来接收，**只能动态注册**，相比起系统全局广播更加高效
2. 在广播中启动activity的话，需要为intent加入FLAG_ACTIVITY_NEW_TASK的标记，不然会报错，因为需要一个栈来存放新打开的activity。
3. 广播中弹出AlertDialog的话，需要设置对话框的类型为:TYPE_SYSTEM_ALERT不然是无法弹出的。




## 不同注册方式的广播接收器回调onReceive(context, intent)中的context具体类型

1. 静态注册(全局+本地)：
    回调onReceive(context, intent)中的context具体指的是**ReceiverRestrictedContext**
    
2. 全局动态注册：
    回调onReceive(context, intent)中的context具体指的是**Activity Context**；
    
3. LocalBroadcastManager动态注册
    回调onReceive(context, intent)中的context具体指的是**Application Context**。 


## 出于安全考虑的广播使用最佳实践

    如不需要向应用程序之外的组件发送广播，则可以使用支持库Support Library中LocalBroadcastManager发送和接收本地广播。

    如果许多应用程序清单中注册接收相同的广播，它会导致系统启动大量的应用程序，从而对设备性能和用户体验产生重大影响。为了避免这种情况，请使用动态注册而不是Manifest声明。有时，Android系统本身会强制使用上下文注册的接收器。例如，CONNECTIVITY_ACTION广播只允许动态注册。

    onReceive(Context, Intent)运行在UI线程，不要进行耗时操作
        如耗时操作必不可少，生成子线程。

    不要使用隐含的意图传播敏感信息。这些信息可以被任何注册的应用程序读取。
        解决方案 ： permission / setPackage(String) / LocalBroadcastManager.

    当注册一个BroadcastReceiver，任何应用程序都可以发送潜在的恶意广播到你的应用的BroadcastReceiver。
        解决方案 ： permission / android:exported = "false" / LocalBroadcastManager.

    广播操作的命名空间是全局的。确保操作名称和其他字符串都是在您自己的名称空间中编写的，否则您可能会无意中与其他应用程序发生冲突。

    不要从BroadcastReceiver开始活动，这么做会导致用户体验很差，特别是如果有不止一个BroadcastReceiver。相反，考虑使用Notification。
    
## 动态注册和静态注册的区别（优缺点）  

1. 动态注册的广播接收器**不是常驻型的**，会随着所注册的Activity的结束而结束，如果所在的Activity已经destroy了，那么该广播接收器也就不能再继续接收广播了。
注意：在Activity结束前，要取消注册广播接收器，不然会导致内存泄露；

优点: 在android的广播机制中，动态注册优先级高于静态注册优先级，因此在必要情况下，是需要动态注册 广播接收者的。

缺点: 当用来注册的 Activity 关掉后，广播也就失效了。


2. 静态注册的广播接收器**是常驻型的**，即使所在的APP被关闭了，也是可以接收到广播的。

优点: 无需担忧广播接收器是否被关闭，只要设备是开启状态，广播接收器就是打开着的

## Android 7.0以后的新特性

以上我们讨论了老生常谈的内容，下面我们谈一谈Android 7.0以后的新变化

    Android 7.0起，系统不再发送以下系统广播：
        ACTION_NEW_PICTURE
        ACTION_NEW_VIDEO

    针对Android 7.0 (API级别24)和更高版本的应用程序必须通过registerReceiver()注册以下广播。在AndroidManifest中声明<receiver>起作用。
        CONNECTIVITY_ACTION

    Android 8.0起，应用无法在Manifest中注册大部分隐式系统广播(即，并非专门针对此应用的广播)，此意也是在于降低随Android同时运行的应用增多，发生性能变差的几率。
