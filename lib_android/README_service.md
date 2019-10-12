# android service 总结 (包含不死进程)



在Android Services (后台服务) 里面，我们了解了Android四大组件之一的Service，知道如何使用后台服务进行来完成一些特定的任务。
但是后台服务在系统内存不足的时候，可能会被系统杀死。那么如何让后台服务尽量不被杀死呢？基本的解决思路主要有以下几种：

## 如何保证Service不被杀死?
Android 进程不死从3个层面入手

1. **提高Service的优先级**

**方法一:监控手机锁屏解锁事件**，在屏幕锁屏时启动1个像素的 Activity，在用户解锁时将 Activity 销毁掉。

**方法二:启动前台service（白色保活）**。 

注：一般前台服务会在状态栏显示一个通知，最典型的应用就是音乐播放器，只要在播放状态下，就算休眠也不会被杀，如果不想显示通知，只要把参数里的int设为0即可。 

同时，对于通过startForeground启动的service，onDestory方法中需要通过stopForeground(true)来取消前台运行状态。
 
Service中onStartCommand方法return START_STICKY; 通过return START_STICKY;系统会在内容不足杀死进程之后，会重新启动该进程。这种方式比较靠谱，作为补充手段

**方法三:提升service优先级**:

为防止Service被系统回收，可以尝试通过提高服务的优先级解决，1000是最高优先级，数字越小，优先级越低 

android:priority="1000" 


2. **在进程被杀死后，进行拉活**

**方法一:监听系统广播来自启动**，利用ANDROID的系统广播（开机，解锁屏、网络变化、安装卸载应用）或高频广播接收器，唤起进程。检查Service的运行状态，判断进程状态，如果进程不存在，就重新启动，

**方法二:onDestroy方法里重启service**:service + broadcast 方式，就是当service走ondestory的时 候，发送一个自定义的广播，当收到广播的时候，重新启动service;

**方法三:双进程互相守护**：

同时开启了两个Service，分别是A和B，互相守护， 如果A守护B，当B挂掉，A就立刻把B启动起来，所以A和B互相守护，无论谁被杀掉，对方就把它拉起来。
具体通过bindService方式来互相绑定对方，分别在不同的进程中避免同时被杀死。 
定义两个Service，分别是LocalService和RemoteService，其中的RemoteService我们通过属性配置android:process=”:remote”代码如下：

    
    public class LocalService extends Service {
    
        private MyBinder mBinder;    private PendingIntent mPintent;    private MyServiceConnection mServiceConnection;
    
        @Override    public void onCreate() {        super.onCreate();        if (mBinder == null) {
                mBinder = new MyBinder();
            }
            mServiceConnection = new MyServiceConnection();
        }
    
        @Override    public int onStartCommand(Intent intent, int flags, int startId) {
            bindService(new Intent(this, RemoteService.class), myServiceConnection, Context.BIND_IMPORTANT);
            Notification notification = new Notification(R.drawable.ic_launcher, "", System.currentTimeMillis());
            mPintent = PendingIntent.getService(this, 0, intent, 0);
            notification.setLatestEventInfo(this, "", "自启", pintent);
    
            startForeground(startId, notification);        return START_STICKY;
        }    class MyServiceConnection implements ServiceConnection {
    
            @Override        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
    
            }
    
            @Override        public void onServiceDisconnected(ComponentName arg0) {            // 连接出现了异常断开了，被杀掉了
                Toast.makeText(LocalService.this, "远程服务Remote被干掉", Toast.LENGTH_LONG).show();
                startService(new Intent(LocalService.this, RemoteService.class));
                bindService(new Intent(LocalService.this, RemoteService.class),
                        mServiceConnection, Context.BIND_IMPORTANT);
            }
    
        }    class MyBinder extends Connection.Stub {
    
            @Override        public String getProName() throws RemoteException {            return "";
            }
    
        }
    
        @Override    public IBinder onBind(Intent arg0) {        return mBinder;
        }
    }
    
    public class RemoteService extends Service {
        private  MyBinder mBinder;    private PendingIntent mPintent;    private MyServiceConnection mServiceConnection;
    
        @Override    public void onCreate() {        super.onCreate();        if (myBinder == null) {
                mBinder = new MyBinder();
            }
            mServiceConnection = new MyServiceConnection();
        }
    
        @Override    public int onStartCommand(Intent intent, int flags, int startId) {        this.bindService(new Intent(this,LocalService.class), mServiceConnection, Context.BIND_IMPORTANT);
            Notification notification = new Notification(R.drawable.ic_launcher,                "",
                    System.currentTimeMillis());
            mPintent=PendingIntent.getService(this, 0, intent, 0);
            notification.setLatestEventInfo(this, "",                "防止被杀掉！", pintent);
            startForeground(startId, notification);        return START_STICKY;
        }    class MyServiceConnection implements ServiceConnection {
    
            @Override        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
    
            }
    
            @Override        public void onServiceDisconnected(ComponentName arg0) {
                Toast.makeText(RemoteCastielService.this, "本地服务Local被干掉", Toast.LENGTH_LONG).show();
                startService(new Intent(RemoteService.this,LocalService.class));
               bindService(new Intent(RemoteService.this,LocalService.class), mServiceConnection, Context.BIND_IMPORTANT);
            }
    
        }    class MyBinder extends Connection.Stub {
    
            @Override        public String getProName() throws RemoteException {            return "";
            }
    
        }
    
        @Override    public IBinder onBind(Intent arg0) {        return mBinder;
        }
    }

3. **依靠第同系列的三方应用唤起**，阿里系，小米系，360系的自家应用唤起。
根据终端不同，在小米手机(包括 MIUI)接入小米推送、华为手机接入华为推送;其他手机可以考虑。接入腾讯信鸽或极光推送与小米推送做 A/B Test。


    
    
# Service 使用示例详解

## startService 和 bindService 有什么不同

1. 生命周期不同
2. 结束方式不同，交互方式不同。

## 为什么bindService可以跟Activity生命周期联动? 

1. bindService 方法执行时，LoadedApk 会记录 ServiceConnection 信息。
2. Activity 执行 finish 方法时，会通过LoadedApk 检查 Activity 是否存在未注销/解绑的 BroadcastReceiver 和 ServiceConnection，

如果有，那么会通知 AMS 注销/解绑对应的 BroadcastReceiver 和 Service，并打印异常信息，告诉用户应该主动执行注销/解绑的操作。

## 后台服务启动方式1：startService(Intent)/stopService(Intent)
流程：

1. 创建简单的自定义Service
2. 在Manifest中注册Service 
3. (默认)act中，使用：
    
    
     //start
     Intent startIntent = new Intent(MainActivity.this, MyStartService.class);
     startService(startIntent);
     //stop
     Intent stopIntent = new Intent(MainActivity.this, MyStartService.class);
     stopService(stopIntent);
4. 调用生命周期：startService--> onCreate()--->onStartCommand() ---> onDestory()

5. 说明：如果服务已经开启，不会重复的执行onCreate()， 而是会调用onStartCommand()。一旦服务开启跟调 用者(开启者)就没有任何关系了。
   开启者退出了，开启者挂了，服务还在后台长期的运行。
   开启者不能调用服务里面的方法。

## 后台服务启动方式2：bindService()/unBindService()

流程：

1. 创建简单的自定义Service
2. 在service中自定义Binder,给自定义Service的onBind使用

       
        //myBinder为自定义Binder实例
         @Override
            public IBinder onBind(Intent intent) {
                Logger.d("MyBindService--onBind");
                return myBinder;
            }

3. 在Manifest中注册Service 
4.  (默认)act中，创建ServiceConnection的内部类实例，给bindService这个方法使用

            
            //准备ServiceConnection对象
            MyBindService.MyBBinder myBBinder;
            private ServiceConnection myBBinderCon = new ServiceConnection() {
        
                @Override
                public void onServiceDisconnected(ComponentName name) {
                }
        
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    myBBinder = (MyBindService.MyBBinder) service;
                    //act调用service方法
                    myBBinder.sjyToDo();
                }
            };
            
            //使用bind
            Intent bindIntent = new Intent(MainActivity.this, MyBindService.class);
            bindService(bindIntent, myBBinderCon, BIND_AUTO_CREATE);
            //unbind
            unbindService(myBBinderCon);
            
5. 生命周期:
  onCreate() ---> onBind()--->onunbind()--->onDestory()
  
 (完整版)bindService---> onCreate() ---> onBind()--->(linkToDeath)---->(unbindService-->unlinkToDeath)--->onunbind()--->onDestory()
 
6. 说明：bind的方式开启服务，绑定服务，调用者挂了，服务也会跟着挂掉。 绑定者可以调用服务里面的方法。

   通信: 1、通过Binder对象。 2、通过broadcast(广播)。
                   
## 前台服务 

1. 状态栏必须提供通知
2. 内存不足，不会被回收
3. 
### 前台服务和后台服务的区别

1. 前台Service的系统优先级更高、不易被回收；

2. 前台Service会一直有一个正在运行的图标在系统的状态栏显示，下拉状态栏后可以看到更加详细的信息，
    非常类似于通知的效果。(只有在这个服务被终止或从前台主动移除通知后才能被解除。)

## 源码流程

## 代码流程
流程 ：

1. 创建简单的自定义Service
2. 在Service的onCreate()中，**设置前台服务的代码(注意api>26)的写法**

    
     @Override
        public void onCreate() {
            super.onCreate();
            Logger.d("MyForegroundService--onCreate--开启前台Service");
            if (Build.VERSION.SDK_INT >= 26) {
                setForeground();
            }
        }
    
        @TargetApi(26)
        private void setForeground() {
            //写法1：简单的前台
    //        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    //        NotificationChannel channel = new NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_HIGH);
    //        manager.createNotificationChannel(channel);
    //        Notification notification = new Notification.Builder(this, ID)
    //                .setContentTitle("前台服务Demo-主题")
    //                .setContentText("前台服务demo-内容")
    //                .setSmallIcon(R.mipmap.ic_launcher)
    //                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
    //                .build();
    //        startForeground(1, notification);
    
            //写法2：通知栏支持跳转的前台
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, intent, 0);
            Notification notification2 = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Uri mUri = Settings.System.DEFAULT_NOTIFICATION_URI;
                NotificationChannel mChannel = new NotificationChannel("channel_id2", "driver", NotificationManager.IMPORTANCE_LOW);//CHANNEL_ONE_ID自定义
    
                mChannel.setSound(mUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
                mChannel.enableLights(true); //设置开启指示灯，如果设备有的话
                mChannel.setLightColor(Color.RED); //设置指示灯颜色
                mChannel.setShowBadge(true); //设置是否显示角标
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);//设置是否应在锁定屏幕上显示此频道的通知
                mChannel.setDescription("设置渠道描述");//设置渠道描述
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 600});//设置震动频率
                mChannel.setBypassDnd(true);//设置是否绕过免打扰模式
                //api26更改
                NotificationManager manager2 = (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);
                manager2.createNotificationChannel(mChannel);
                notification2 = new Notification.Builder(this, "channel_id2")
                        .setChannelId("channel_id2")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("前台服务Demo-主题2")
                        .setContentText("前台服务demo-内容2")
                        .setContentIntent(pendingIntent2)
                        .build();
            } else {
                // 提升应用权限
                notification2 = new Notification.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("hello world")
                        .setContentIntent(pendingIntent2)
                        .build();
            }
            notification2.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR | Notification.FLAG_FOREGROUND_SERVICE;
            startForeground(2, notification2);
        }
        
3. 在Manifest中注册Service 
4. (默认)act中调用：
    
            
          // start
          Intent startForegroundIntent = new Intent(this, MyForegroundService.class);
          if (Build.VERSION.SDK_INT >= 26) {
                        startForegroundService(startForegroundIntent);
          } else {
                        startService(startForegroundIntent);
          }
          
          //stop
          Intent stopForeground = new Intent(this, MyForegroundService.class);
          stopService(stopForeground);
            
## 远程服务  
 
远程服务和本地服务的区别：
1. 最大区别是服务和调用者可以不在一个进程中

| 服务| 特点 |优点|缺点 |
| ---------- | -------------| -------------| -------------| 
| 本地服务|  1.运行在主线程。2 主进程终止，服务也终止|节约资源，通讯方便|主进程终止，服务终止，限制大|
| 远程服务|  1.独立进程。2 服务常驻后台，不受act影响|灵活：独立进程，不受act影响|耗费资源（单独进程），要使用aidl|

## 示例

1. 新建项目,aidl(服务端)，并将aidl绑定给自定义的服务，并在manefest中将service设置成可被别的app调用的属性
2. 新建项目,aidl(调用端)


    /**
     * 远程服务 aidl
     */
    IAidlDemoInterface iAidlDemoInterface;
    private ServiceConnection remoteConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iAidlDemoInterface = IAidlDemoInterface.Stub.asInterface(iBinder);

            //使用自定义方法
            try {
                Logger.d("远程服务--调用者调用自定义");
                iAidlDemoInterface.myAdd();
            } catch (Exception e) {

            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            Logger.d("远程服务关闭");
        }
    };
    
    
    
    //使用
    Intent remoteIntent = new Intent();
    remoteIntent.setPackage("com.sjy.servicedemo2");
    remoteIntent.setAction("com.sjy.remoteService.demo");
    remoteIntent.setComponent(new ComponentName("com.sjy.servicedemo2","com.sjy.servicedemo2.RemoteService"));
    bindService(remoteIntent, remoteConn, BIND_AUTO_CREATE);             






