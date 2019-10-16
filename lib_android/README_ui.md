#  UI相关

## 更新UI方式

1. Activity.runOnUiThread(Runnable)
2. View.post(Runnable)，View.postDelay(Runnable, long)(可以理解为在当前操作视图UI线程添 加队列)
3. Handler
4. AsyncTask
5. Rxjava
6. LiveData

## Merge/ViewStub/include 的作用。 

1. Merge: 减少视图层级，可以删除多余的层级。
2. ViewStub: 按需加载，减少内存使用量、加快渲染速度、不支持 merge 标签,使用ViewStub处理整体相同，局部不同的情况
3. include:xml优化布局，可以公用公共布局，merge比include少一层布局。

ViewStub是一个轻量级的View，它是一个看不见的，并且不占布局位置，占用资源非常小的视图对象。可以为ViewStub指定一个布局，加载布局时，只有ViewStub会被初始化，
然后当ViewStub被设置为可见时，或是调用了ViewStub.inflate()时，ViewStub所指向的布局会被加载和实例化，然后ViewStub的布局属性都会传给它指向的布局。

    注意：

    ViewStub只能加载一次，之后ViewStub对象会被置为空。所以它不适用于需要按需显示隐藏的情况。

    ViewStub只能用来加载一个布局文件，而不是某个具体的View。

    ViewStub中不能嵌套Merge标签。


## activity的startActivity和context的startActivity区别?

1. 从Activity中启动新的Activity时可以直接mContext.startActivity(intent)就好 
2. 如果从其他Context中启动Activity则必须给intent设置Flag:
    
    
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
    mContext.startActivity(intent);
    
## Asset目录与res目录的区别?

1. assets:不会在 R 文件中生成相应标记，存放到这里的资源在打包时会打包到程序安装包中。(通过AssetManager 类访问这些文件)

2. res:会在 R 文件中生成 id 标记，资源在打包时如果使用到则打包到安装包中，未用到不会打入安装包
    中。
    res/anim:存放动画资源。
    res/raw:和 asset 下文件一样，打包时直接打入程序安装包中(会映射到 R 文件中)。
    
## Android怎么加速启动Activity?
1. onCreate() 中不执行耗时操作

把页面显示的 View 细分一下，放在 AsyncTask 里逐步显示，用 Handler 更好。这样用户的看到 的就是有层次有步骤的一个个的 View 的展示，不会是先看到一个黑屏，然后一下显示所有 View。最好做成动画，效果更自然。

2. 利用多线程的目的就是尽可能的减少 onCreate() 和 onReume() 的时间，使得用户能尽快看到页 面，操作页面。
3. 减少主线程阻塞时间。
4. 提高 Adapter 和 AdapterView 的效率。 
5. 优化布局文件。

## 程序A能否接收到程序B的广播?
 
能，使用全局的BroadCastRecevier能进行跨进程通信，但是注意它只能被动接收广播。此外，
LocalBroadCastRecevier只限于本进程的广播间通信。

## 下拉状态栏是不是影响activity的生命周期

Android下拉通知栏不会影响Activity的生命周期方法

## Android长连接，怎么处理心跳机制。
长连接:长连接是建立连接之后, 不主动断开. 双方互相发送数据, 发完了也不主动断开连接, 之后有需要发送的数据就继续通过这个连接发送.

心跳包:其实主要是为了防止NAT超时，客户端隔一段时间就主动发一个数据，探测连接是否断开。

服务器处理心跳包:假如客户端心跳间隔是固定的, 那么服务器在连接闲置超过这个时间还没收到心跳 时, 可以认为对方掉线, 关闭连接. 

如果客户端心跳会动态改变, 应当设置一个最大值, 超过这个最大值才认为对方掉线. 还有一种情况就是服务器通过TCP连接主动给客户端发消息出现写超时, 可以直接认为对方掉线.

## CrashHandler实现原理?

获取app crash的信息保存在本地然后在下一次打开app的时候发送到服务器。

具体实现在Application的onCreate()中初始化如下代码即可。

        //捕捉未catch的异常，
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, final Throwable ex) {
                    //自定义处理即可  
                }
            });
## 类的初始化顺序依次是? 

(静态变量、静态代码块)>(变量、代码块)>构造方法

## 多进程场景遇见过么?
1. 在新的进程中，启动前台Service，播放音乐。
2. 一个成熟的应用一定是多模块化的。首先多进程开发能为应用解决了OOM问题，因为Android对内 存的限制是针对于进程的，所以，当我们需要加载大图之类的操作，
可以在新的进程中去执行，避免主 进程OOM。而且假如图片浏览进程打开了一个过大的图片，java heap 申请内存失败，该进程崩溃并不 影响我主进程的使用。

## 编译期注解跟运行时注解 

1. 运行期注解(RunTime)利用反射去获取信息还是比较损耗性能的，对应
@Retention(RetentionPolicy.RUNTIME)。

2. 编译期(Compile time)注解，以及处理编译期注解的手段APT和Javapoet，对应 @Retention(RetentionPolicy.CLASS)。
 其中apt+javaPoet目前也是应用比较广泛，在一些大的开源库，如EventBus3.0+,页面路由 ARout、 Dagger、Retrofit,ButterKnife等均有使用的身影，
 注解不仅仅是通过反射一种方式来使用，也可以使用APT在编译 期处理
 
## 直接在Activity中创建一个thread跟在service中创建一个thread之间的区别?

1. 在Activity中被创建:该Thread的就是为这个Activity服务的，完成这个特定的Activity交代的任务，主动通知该Activity一些消息和事件，Activity销毁后，该Thread也没有存活的意义了。
2. 在Service中被创建:这是保证最长生命周期的Thread的唯一方式，只要整个Service不退出，Thread就可以一直在后台执行，一般在Service的onCreate()中创建，在onDestroy()中销毁。
    所以，在Service中 创建的Thread，适合长期执行一些独立于APP的后台任务，比较常见的就是:在Service中保持与服务器端的长连接。
    
##  我们常说，一部Android设备上不能同时安装2个相同包名的app，指的是applicationId不能一样。

## LaunchMode应用场景
有四种模式：standard/singleTop/singleTask/singleInstance

1. standard，创建一个新的Activity。默认的模式
2. singleTop，栈顶不是该类型的Activity，创建一个新的Activity。否则，onNewIntent。 
3. singleTask，回退栈中没有该类型的Activity，创建Activity，否则，onNewIntent+ClearTop。 

        
        注意:
        设置了"singleTask"启动模式的Activity，它在启动的时候，会先在系统中查找属性值affinity等于它的属性值taskAffinity的Task是否存在;
        如果存在这样的Task，它就会在这个Task中启动，否则就会在新的任务栈中启动。因此， 如果我们想要设置了"singleTask"启动模式的Activity在
        新的任务中启动，就要为它设置 一个独立的taskAffinity属性值。
        
        如果设置了"singleTask"启动模式的Activity不是在新的任务中启动时，它会在已有的任务中查看是否已 经存在相应的Activity实例， 如果存在，
        就会把位于这个Activity实例上面的Activity全部结束掉，即最终 这个Activity 实例会位于任务的Stack顶端中。
        
        在一个任务栈中只有一个”singleTask”启动模式的Activity存在。他的上面可以有其他的Activity。这点与 singleInstance是有区别的。

4. singleInstance，回退栈中，只有这一个Activity，没有其他Activity。 

**应用场景**：

singleTop适合接收通知启动的内容显示页面。

singleTask适合作为程序入口点,主界面等。 例如浏览器的主界面。不管从多少个应用启动浏览器，只会启动主界面一次，其余情况都会走onNewIntent，并且会清空主界面上面的其他页面。

singleInstance应用场景:单独的应用功能界面：闹铃的响铃界面

## 说说Activity、Intent、Service 是什么关系
1. 他们都是 Android 开发中使用频率最高的类。其中 Activity 和 Service 都是 Android 四大组件之一。
2. 他俩都是Context 类的子类 ContextWrapper 的子类
3. Activity负责用户界面的显示和交互，Service 负责后台任务的处理。Activity 和 Service 之间可以通过 Intent 传 递数据，因此可以把 Intent 看作是通信使者。

## Base64、MD5是加密方法么? 

1. 加密指的是对数据进行转换以后，数据变成了另一种格式，并且除了拿到解密方法的人，没人能把数 据转换回来。 

2. **MD5是一种信息摘要算法，它是不可逆的，不可以解密**。所以它只能算的上是一种单向加密算法。
 
3. **Base64也不是加密算法，它是一种数据编码方式，虽然是可逆的，但是它的编码方式是公开的，无所 谓加密。**

            
            **Base64是什么?**：
            
            Base64是用文本表示二进制的编码方式，它使用4个字节的文本来表示3个字节的原始二进制数据。
            
            它将二进制数据转换成一个由64个可打印的字符组成的序列:A-Za-z0-9+/
            
            **MD5是什么?**：
            
            MD5是哈希算法的一种，可以将任意数据产生出一个128位(16字节)的散列值，用于确保信息传输完 整一致。
            
            我们常在注册登录模块使用MD5，用户密码可以使用MD5加密的方式进行存储。
            
            如: md5(hello world,32) = 5eb63bbbe01eeed093cb22bb8f5acdc3

## 如何选择第三方，从那些方面考虑?

大方向:从软件环境做判断

**性能**是开源软件第一解决的问题。

一个好的**生态**，是一个优秀的开源库必备的，取决标准就是观察它是否一直在持续更新迭代，是否能及
时处理github上用户提出来的问题。大家在社区针对这个开源库是否有比较活跃的探讨。 

**背景**，该开源库由谁推出，由哪个公司推出来的。

**用户数**和有哪些知名的企业**用户落地**使用

小方向:

从软件开发者的角度做判断 **是否解决了我们现有问题或长期来看带来的维护成本**。

公司有多少人会。

**学习成本**

## 单例实现线程的同步的要求: 

1. 单例类确保自己只有一个实例(**构造函数私有:不被外部实例化,也不被继承**)。

2. **单例类必须自己创建自己的实例**。 

3. 单例类必须**为其他对象提供唯一的实例**。

## LinearLayout、FrameLayout、RelativeLayout性能对比，为什么?
 
RelativeLayout会让子View调用2次onMeasure，
 
LinearLayout 在有weight时，也会调用子 View 2次
 
onMeasure RelativeLayout的子View如果高度和RelativeLayout不同，则会引发效率问题，当子View很复杂时，这个问题会更加严重。如果可以，尽量使用padding代替margin。 

在不影响层级深度的情况下,使用LinearLayout和FrameLayout而不是RelativeLayout。

## 为什么Google给开发者默认新建了个RelativeLayout，而自己却在DecorView中 用了个LinearLayout?

因为DecorView的层级深度是已知而且固定的，上面一个标题栏，下面一个内容栏。采用 RelativeLayout并不会降低层级深度，

所以此时在根节点上用LinearLayout是效率最高的。而之所以给 开发者默认新建了个RelativeLayout是希望开发者能采用尽量少

的View层级深度来表达布局以实现性能最优，因为复杂的View嵌套对性能的影响会更大一些。

## 显示Intent与隐式Intent的区别
对明确指出了目标组件名称的Intent，我们称之为“显式Intent”。 

对于没有明确指出目标组件名称的Intent，则称之为“隐式 Intent”。

对于隐式意图，在定义Activity时，指定一个intent-filter，当一个隐式意图对象被一个意图过滤器进行 匹配时，
将有三个方面会被参考到:
1. 动作(Action)
2. 类别(Category ['kætɪg(ə)rɪ] ) 
3. 数据(Data )

    
        <activity android:name=".MainActivity"  android:label="@string/app_name">
                    <intent-filter>
        </activity>
            <action android:name="com.wpc.test" />
            <category android:name="android.intent.category.DEFAULT" />
            <data android:mimeType="image/gif"/>
        </intent-filter>

## 如何查看android SQL数据库文件和SP文件？
1. sql数据库位置：/data/data/应用包名下，可以看到.db后缀的文件，就是数据库文件
2. SP位置：/data/data/应用包名下/shared_prefs文件夹下,adb命令：run-as+包名就可以打开对应debug版的sp文件

## 各大平台打包上线的流程与审核时间，常见问题(主流的应用市场说出3-4个)

1. 需要公司资质，发布人的身份信息，软著
2. 平台内部测试，1--3天
3. apk包认领，签名包，

##  面向对象的基本特征：

**封装 继承 多态**

##  如何中断一个线程：
1. 线程中添加标记位：满足直接退出执行
2. 在线程执行到sleep()的时机，使用interrupt()终止
3. onStop()强制结束，不推荐，不安全   

## 怎么去除重复代码?

1. **布局去重**

（1）include标签重用布局代码 （2）style设置重复组件样式 （3）资源（尺寸，颜色等）统一引用，方便统一管理（4）使用ViewStub处理整体相同，局部不同的情况

2. **代码去重**：重构技巧，比如提炼方法，抽象基类，提炼常量等去重


## Activity调用finish方法，会回调哪些生命周期方法？

ActivityThread中查看performXXX方法即可

finish执行位置：

1. 在onCreate中：    onCreate->onDestroy
2. 在onStart中：     onCreate->onStart->onStop->onDestroy
3. 在onResume中：    onCreate->onStart->onResume->onPause->onStop->onDestroy
4. 在onPause(同上):  onCreate->onStart->onResume->onPause->onStop->onDestroy
5. 在onStop:        onCreate->onStart->onResume->onPause->onStop->onDestroy
6. 在onDestroy:     onCreate->onStart->onResume->onPause->onStop
7. 在onRestart:     onCreate->onStart->onResume->onPause->onStop->onReStart->onStart->onResume->onPause->onStop->onDestroy
    
## Debug和Release状态的不同   

Debug可以称为调试版本，它包含**调试信息**，对代码不作任何优化，便于程序员调试程序。Release称为发布版本，它往往是进行了各种优化，
使得程序在代码大小和运行速度上都是最优方案，所以在规则的检查上面也更加严格。

releas版本与debug版本的**applicationId不一样**，因此可以同时安装在一台设备上，集成各种第三方SDK的功能,分享,推送等,要注意appid替换所带来的麻烦


## RemoteViews实现和使用场景

RemoteViews 的作用是在其他进程中显示并更新 View 界面。主要用于通知栏和桌面小部件上。

## Android中如何查看一个对象的回收情况?

1. 引用计数法
2. 跟搜索算法
3. profiler 查看内存监控，一直增长就有内存泄漏

## Activity正常和异常情况下的生命周期

1. 描述正常情况的生命周期

2. 异常情况下的生命周期分析

(1)情况一：资源相关的系统配置发生改变导致Activity被杀死并重新创建

当系统配置发生改变后，如从横屏手机切换到了竖屏，Activity会被销毁，其onPause,onStop,onDestroy方法均会被调用，同时由于是在异常情况下被终止的，系统会调用onSavedInstanceState来保存当前Activity的状态(正常情况下不会调用此方法)，这个方法的调用时机是在onStop之前，当Activity重新被创建后，系统调用会调用

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    1
    2
    3
    4

方法，并且把Activity销毁时onSavedInstanceState方法所保存的Bundle对象作为参数同时传递给onRestoreInstanceState和onCreate方法，onRestoreInstanceState是在onStart之后调用。
Note:我们知道，当Activity被异常终止后被恢复时，系统会自动的帮我们恢复数据和一些状态，如文本框用户输入的数据，listview的滚动位置等，关于保存和恢复View的层次结构和数据，系统的工作流程是这样的，首先Activity被意外终止时，Activity会调用onSavedInstanceState去保存数据，然后Activity会委托Window去保存数据，接着Window再委托它上面的顶级容器去保存数据，顶层容器是一个ViewGroup，一般来说它很可能是一个DecorView.最后顶层容器再去一一通知它的子元素来保存数据，这样整个数据保存过程就完成了，可以发现，这是一种典型的委托思想，上层委托下层，父容器委托子容器去处理一些事情。

(2)情况二：资源内存不足导致低优先级的Activity被杀死

Activity按照优先级我们可以分为以下的三种：
a.前台Activity—正在和用户交互的Activity，优先级最高。
b.可见但非前台Activity,如处于onPause状态的Activity，Activity中弹出了一个对话框，导致Activity可见但是位于后台无法和用户直接交互。
c.后台Activity—已经被暂停的Activity,比如执行了onStop方法，优先级最低。
当系统内存不足时，系统就会按照上述的优先级顺序选择杀死Activity所在的进程，并在后续通过onSaveInstanceState缓存数据和onRestoreInstanceState恢复数据。
Note:如果一个进程中没有四大组件在执行，那么这个进程将很快被杀死，因此，一些后台工作不适合脱离了四大组件工作，比较好的方法是将后台工作放入Service中从而保证进程有一定的优先级，这样就不会轻易的被系统杀死。
Note:系统只恢复那些被开发者指定过id的控件，如果没有为控件指定id,则系统就无法恢复了

## android 布局加载原理

LayoutInflater通过pull解析（xmlPullPaser）方式解析xml各个节点，再通过反射创建出View对象
（调用clazz.getConstructor()通过反射获取构造方法,并通过暴力反射，将其设置为可访问，并存入hasmap集合中）





