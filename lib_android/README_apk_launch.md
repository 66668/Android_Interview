# android app启动流程总结

（不要回答生命周期）
## 流程简介

说法1：

（1）启动的起点发生在Launcher活动中，启动一个app说简单点就是启动一个Activity，那么我们说过所有组件的启动，切换，调度都由AMS来负责的，所以第一步就是Launcher响应了用户的点击事件，然后通知AMS；

（2）AMS得到Launcher的通知，就需要响应这个通知，主要就是新建一个Task去准备启动Activity，并且告诉Launcher你可以休息了（Paused）；

（3）Launcher得到AMS让自己“休息”的消息，那么就直接挂起，并告诉AMS我已经Paused了；

（4）AMS知道了Launcher已经挂起之后，就可以放心的为新的Activity准备启动工作了，首先，APP肯定需要一个新的进程去进行运行，所以需要创建一个新进程，这个过程是需要Zygote参与的，AMS通过Socket去和Zygote协商，如果需要创建进程，那么就会fork自身，创建一个线程，新的进程会导入ActivityThread类，这就是每一个应用程序都有一个ActivityThread与之对应的原因；

（5）进程创建好了，通过调用上述的ActivityThread的main方法，这是应用程序的入口，在这里开启消息循环队列，这也是主线程默认绑定Looper的原因；

（6）这时候，App还没有启动完，要永远记住，四大组建的启动都需要AMS去启动，将上述的应用进程信息注册到AMS中，AMS再在堆栈顶部取得要启动的Activity，通过一系列链式调用去完成App启动；


说法2：http://blog.csdn.net/luoshengyang/article/details/6689748

 整个应用程序的启动过程要执行很多步骤，但是整体来看，主要分为以下五个阶段：

       一. Step1 - Step 11：Launcher通过Binder进程间通信机制通知ActivityManagerService，它要启动一个Activity；

       二. Step 12 - Step 16：ActivityManagerService通过Binder进程间通信机制通知Launcher进入Paused状态；

       三. Step 17 - Step 24：Launcher通过Binder进程间通信机制通知ActivityManagerService，它已经准备就绪进入Paused状态，于是ActivityManagerService就创建一个新的进程，用来启动一个ActivityThread实例，即将要启动的Activity就是在这个ActivityThread实例中运行；

       四. Step 25 - Step 27：ActivityThread通过Binder进程间通信机制将一个ApplicationThread类型的Binder对象传递给ActivityManagerService，以便以后ActivityManagerService能够通过这个Binder对象和它进行通信；

       五. Step 28 - Step 35：ActivityManagerService通过Binder进程间通信机制通知ActivityThread，现在一切准备就绪，它可以真正执行Activity的启动操作了。
