# android 启动流程 总结(app启动和系统启动)

## Android系统启动流程

init进程 -> Zygote进程 –> SystemServer进程 –> 各种系统服务 –> 应用进程

android系统启动的核心流程如下:

1. 启动电源以及系统启动:当电源按下时引导芯片从预定义的地方(固化在ROM)开始执行，加 载引导程序BootLoader到RAM，然后执行。 
2. 引导程序BootLoader:BootLoader是在Android系统开始运行前的一个小程序，主要用于把系统OS拉起来并运行。 
3. Linux内核启动:当内核启动时，设置缓存、被保护存储器、计划列表、加载驱动。当其完成系统设置时，会先在系统文件中寻找init.rc文件，并启动init进程。
4. init进程启动:初始化和启动属性服务，并且启动Zygote进程。
5. Zygote进程启动:创建JVM并为其注册JNI方法，创建服务器端Socket，启动SystemServer进程。 
6. SystemServer进程启动:启动Binder线程池和SystemServiceManager，并且启动各种系统服务。 
7. Launcher启动:被SystemServer进程启动的AMS会启动Launcher，Launcher启动后会将已安装应用的快捷图标显示到系统桌面上。


**详细介绍** https://jsonchao.github.io/categories/Android核心源码分析/

## app启动流程(冷启动)

（不要回答生命周期）
点击应用图标后会去启动应用的Launcher Activity，如果Launcer Activity所在的进程没有创建，还会创建新进程，整体的流程就是一个Activity的启动流程。

**流程**:
1. 点击桌面应用图标，Launcher进程将启动Activity(MainActivity)的请求以Binder的方式发 送给了AMS。
2. AMS接收到启动请求后，交付ActivityStarter处理Intent和Flag等信息，然后再交给 ActivityStackSupervisior/ActivityStack 处理Activity进栈相关流程。同时以Socket方式请求Zygote 进程fork新进程。
3. Zygote接收到新进程创建请求后fork出新进程。
4. 在新进程里创建ActivityThread对象，新创建的进程就是应用的主线程，在主线程里开启 Looper消息循环，开始处理创建Activity。
5. ActivityThread利用ClassLoader去加载Activity、创建Activity实例，并回调Activity的 onCreate()方法，这样便完成了Activity的启动。

**整个流程涉及的主要角色有**:

1. Instrumentation: 监控应用与系统相关的交互行为。 
2. AMS:组件管理调度中心，什么都不干，但是什么都管。 
3. ActivityStarter:Activity启动的控制器，处理Intent与Flag对Activity启动的影响，
    具体说来有:
    
    (1)寻找符合启动条件的Activity，如果有多个，让用户选择;
    
    (2) 校验启动参数的合法性;3 返回int参 数，代表Activity是否启动成功。
4. ActivityStackSupervisior:这个类的作用你从它的名字就可以看出来，它用来管理任务栈。 
5. ActivityStack:用来管理任务栈里的Activity。 
6. ActivityThread:最终干活的人，Activity、Service、BroadcastReceiver的启动、切换、调度等各 种操作都在这个类里完成。

注:这里单独提一下ActivityStackSupervisior，这是高版本才有的类，它用来管理多个ActivityStack， 早期的版本只有一个ActivityStack对应着手机屏幕，后来高版本支持多屏以后，就有了多个 ActivityStack，于是就引入了ActivityStackSupervisior用来管理多个ActivityStack。 

**整个流程主要涉及四个进程**:

1. 调用者进程，如果是在桌面启动应用就是Launcher应用进程。 
2. ActivityManagerService等待所在的System Server进程，该进程主要运行着系统服务组件。 
3. Zygote进程，该进程主要用来fork新进程。 
4. 新启动的应用进程，该进程就是用来承载应用运行的进程了，它也是应用的主线程(新创建的进程 就是主线程)，处理组件生命周期、界面绘制等相关事情。

Activity的启动流程图(放大可查看)如下所示:

![启动流程图](https://github.com/66668/Android_Interview/blob/master/pictures/app_launcher_01.png)


![启动流程图](https://github.com/66668/Android_Interview/blob/master/pictures/app_launcher_02.png)



## 启动一个程序，可以主界面点击图标进入，也可以从一个程序中跳转过去，二者有什么区别?

1. 当用户点击Lancher的icon时，发出一个Intent,指向单一确定的程序。

        
        Intent intent =mActivity.getPackageManager().getLaunchIntentForPackage(packageName);
        mActivity.startActivity(intent);
        
2. 启动第三方程序时，构造一个Intent，startActivity。这个intent中添加action过滤，这时可能启动多个符合要求的程序

3. 本质是相同的。

## AMS家族重要术语解释。
1. ActivityManagerServices，简称AMS，服务端对象，负责系统中所有Activity的生命周期。
2. ActivityThread，App的真正入口。当开启App之后，调用main()开始运行，开启消息循环队列，这就 是传说的UI线程或者叫主线程。与ActivityManagerService一起完成Activity的管理工作。
3. ApplicationThread，用来实现ActivityManagerServie与ActivityThread之间的交互。在 ActivityManagerSevice需要管理相关Application中的Activity的生命周期时，通过ApplicationThread 的代理对象与ActivityThread通信。
4. ApplicationThreadProxy，是ApplicationThread在服务器端的代理，负责和客户端的 ApplicationThread通信。AMS就是通过该代理与ActivityThread进行通信的。
5. Instrumentation，每一个应用程序只有一个Instrumetation对象，每个Activity内都有一个对该对象 的引用，Instrumentation可以理解为应用进程的管家，ActivityThread要创建或暂停某个Activity时， 都需要通过Instrumentation来进行具体的操作。
6. ActivityStack，Activity在AMS的栈管理，用来记录经启动的Activity的先后关系，状态信息等。通过 ActivtyStack决定是否需要启动新的进程。
7. ActivityRecord，ActivityStack的管理对象，每个Acivity在AMS对应一个ActivityRecord，来记录 Activity状态以及其他的管理信息。其实就是服务器端的Activit对象的映像。
8. TaskRecord，AMS抽象出来的一个“任务”的概念，是记录ActivityRecord的栈，一个“Task”包含若干个 ActivityRecord。AMS用TaskRecord确保Activity启动和退出的顺序。如果你清楚Activity的4种 launchMode，那么对这概念应该不陌生。
