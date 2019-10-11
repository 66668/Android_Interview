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
2. ViewStub: 按需加载，减少内存使用量、加快渲染速度、不支持 merge 标签
3. include:xml优化布局，可以公用公共布局，merge比include少一层布局。

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