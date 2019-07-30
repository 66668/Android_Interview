# android service(不死进程)总结

在Android Services (后台服务) 里面，我们了解了Android四大组件之一的Service，知道如何使用后台服务进行来完成一些特定的任务。
但是后台服务在系统内存不足的时候，可能会被系统杀死。那么如何让后台服务尽量不被杀死呢？基本的解决思路主要有以下几种：

## 保活1. **提高Service的优先级**

<!-- 为防止Service被系统回收，可以尝试通过提高服务的优先级解决，1000是最高优先级，数字越小，优先级越低 -->  
android:priority="1000" 

## 保活2. **START_STICKY**

1、Service中onStartCommand方法return START_STICKY; 
通过return START_STICKY;系统会在内容不足杀死进程之后，会重新启动该进程。这种方式比较靠谱，作为补充手段

## 保活3. **把service写成系统服务，将不会被回收**

在Manifest.xml文件中设置persistent属性为true，则可使该服务免受out-of-memory killer的影响。但是这种做法一定要谨慎，系统服务太多将严重影响系统的整体运
行效率。 

## 保活4. **将服务改成前台服务foreground service（白色保活）**

重写onStartCommand方法，使用StartForeground(int,Notification)方法来启动service。  

注：一般前台服务会在状态栏显示一个通知，最典型的应用就是音乐播放器，只要在播放状态下，就算休眠也不会被杀，如果不想显示通知，只要把参数里的int设为0即可。 

同时，对于通过startForeground启动的service，onDestory方法中需要通过stopForeground(true)来取消前台运行状态。 

这个方案也是本文目前准备详细介绍的。

## 保活5. **监听系统广播来自启动**

利用ANDROID的系统广播（开机，解锁屏、网络变化、安装卸载应用）检查Service的运行状态，判断进程状态，如果进程不存在，就重新启动，
系统广播是Intent.ACTION_TIME_TICK，这个广播每分钟发送一次，我们可以每分钟检查一次Service的运行状态，如果已经被结束了，就重新启动Service。

xml代码：
        
        <receiver android:name="com.dbjtech.acbxt.waiqin.BootReceiver" > 
            <intent-filter>  
                <action android:name="android.intent.action.USER_PRESENT" /> 
                <action android:name="android.intent.action.PACKAGE_RESTARTED" /> 
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" /> 
            </intent-filter> 
            <intent-filter>  
                        <action android:name="android.intent.action.PACKAGE_ADDED" />  
                        <action android:name="android.intent.action.PACKAGE_REMOVED" />  
        
                        <data android:scheme="package" />  
                    </intent-filter> </receiver> 
                    
## 保活6. **双进程互相守护** 
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