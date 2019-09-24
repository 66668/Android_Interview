# android 桌面部件详解(PendingIntent act的显式/隐式启动)


## as自动创建的文件介绍:

file --> new --> Widget-->app widget,创建指定大小的部件，as会自动生成如下文件：

1. 包下生成 extends AppWidgetProvider的类（代码中详解）
   
2. res/drawable-nodpi 下生成部件使用的图标
3. res/layout 下生成布局
    
    
      说明：部件支持的布局有四种：
      （1）FrameLayout
      （2）LinearLayout
      （3）RelativeLayout
      （4）GridLayout
      
      支持的控件有13个：
      
      AnalogClock，
      Button，
      Chronometer，
      ImageButton，
      ImageView，
      ProgressBar，
      TextView，
      ViewFlipper，
      ListView，
      GridView，
      StackView，
      AdapterViewFlipper
          
4. res/values/strings 下生成相应名称
5. res/values/dimens 下生成相应尺寸
6. res/xml 下生成appwidget-provider的描述信息的xml,该xml需要绑定到AndroidManifest.xml中，先介绍xml的相应属性：
    
    
        android:initialLayout="@layout/app_widget"                 ：指向 Widget 的布局
        android:initialKeyguardLayout="@layout/app_widget"          ：用来定义锁屏应用小部件的布局，同上
        android:widgetCategory="keyguard|home_scree"                 ：指定了 widget 能显示的地方：能否显示在 home Screen 或 lock screen 或 两者都可以
        android:previewImage="@drawable/example_appwidget_preview"  :drawable-nodpi下显示的预览图片
        
        android:minHeight="40dp"
        android:minWidth="250dp"                                    :指定了部件的大小，大小以整格显示
        android:resizeMode="horizontal|vertical"                    ："horizontal|vertical|none" "horizontal"意味着widget可以水平拉伸，“vertical”意味着widget可以竖值拉伸，“none”意味着widget不能拉伸；默认值是"none"
        android:updatePeriodMillis="86400000"                       ： widget 的更新频率
        android:configure="com.android.interview.MainActivity"      :可选属性，定义了 widget 的配置 Activity。如果定义了该项，那么当 widget 创建时，会自动启动该 Activity
         android:autoAdvanceViewId=""                               :指定一个子view ID，表明该子 view 会自动更新

 7. src/main下的  AndroidManifest.xml中，自动生成 receiver:
 
 
                 <!--桌面小部件的配置-->
                 <receiver android:name="com.android.interview.app_widget.Test42">
                     <intent-filter>
                         <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
                         <!--  其他响应事件 -->
                         <action android:name="android.appwidget.action.APPONCLICK" />
                         <action android:name="android.appwidget.action.AROUTER" />
                     </intent-filter>
         
                     <meta-data
                         android:name="android.appwidget.provider"
                         android:resource="@xml/test42_info" />
                 </receiver>       
                
        
 至此，一个简单的桌面便可以执行，但想要widget关联app,还需要了解其他知识
 
 ## PendingIntent 详解
 PendingIntent 是 Android 提供的一种用于外部程序调起自身程序的能力，生命周期与主程序无关。
 
 **外部程序通过 PendingIntent 只能调用起三种组件:**
 
 1. Activity
 2. Service
 3. Broadcast
 
 **使用场景**
 
 1. 使用 AlarmManager 设定闹钟
 2. 在系统状态栏显示 Notification
 3. 在桌面显示 Widget
 
 **获取方式**
 
     // 获取 Broadcast 关联的 PendingIntent
     PendingIntent.getBroadcast(Context context, int requestCode, Intent intent, int flags)
      
     // 获取 Activity 关联的 PendingIntent
     PendingIntent.getActivity(Context context, int requestCode, Intent intent, int flags)
     PendingIntent.getActivity(Context context, int requestCode, Intent intent, int flags, Bundle options)
      
     // 获取 Service 关联的 PendingIntent
     PendingIntent.getService(Context context, int requestCode, Intent intent, int flags)

**flags参数意义**


    //如果新请求的 PendingIntent 发现已经存在时，取消已存在的，用新的 PendingIntent 替换
    int FLAG_CANCEL_CURRENT
     
    //如果新请求的 PendingIntent 发现已经存在时，忽略新请求的，继续使用已存在的。日常开发中很少使用
    int FLAG_NO_CREATE
     
    //表示 PendingIntent 只能使用一次，如果已使用过，那么 getXXX(...) 将会返回 NULL 
    //也就是说同类的通知只能使用一次，后续的通知单击后无法打开。
    int FLAG_ONE_SHOT
     
    //如果新请求的 PendingIntent 发现已经存在时, 如果 Intent 有字段改变了，这更新已存在的 PendingIntent
    int FLAG_UPDATE_CURRENT

             
## act的显式/隐式启动

**显式启动:**

1. Activity 直接跳转（应用内部直接跳转）


    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    intent.putExtra("key","value");//传递参数
    startActivity(intent);

2. 通过包名，类名（应用内部+应用外部）

   
    Intent intent = new Intent();
    //通过包名，类名进行跳转
    intent.setClassName("com.example.mvptest","com.example.mvptest.MainActivity");
    intent.putExtra("key","value");//传递参数
    startActivity(intent);

3. ComponentName跳转（应用内部+应用外部）


    Intent intent = new Intent();
    intent.setComponent(newComponentName("com.example.mvptest","com.example.mvptest.MainActivity"));
    intent.putExtra("key","value");//传递参数
    startActivity(intent);

**隐式启动:**

1. 在AndroidManifest中定义某个Activity的 intent-filter


            <activity android:name=".widget.WidgetActivity">
                <intent-filter>
                    <action android:name="com.example.widget" />
                    <category android:name="android.intent.category.DEFAULT" />
                </intent-filter>
            </activity>

2. 在Intent中通过 设置 action，添加category来进行匹配


            Intent intent = new Intent();
            intent.setAction("com.example.widget");
            intent.addCategory("android.intent.category.DEFAULT");//默认，可以不用写
            intent.putExtra("key","value");//传递参数
            startActivity(intent);
            this.finish();

 隐式启动，在启动的时候是不明确的。 如果intent-filter中还设置了 data 属性，则必须 action,category,data 完全匹配才能完成跳转
 
 
 ## 个人开发遇到的问题
 1. onUpdate创建点击事件，发送给onRecevice，让onRecevice去打开app,无响应。
