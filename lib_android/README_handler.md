# handler 机制详解及相关面试 总结

## 常见面试题：
1. 为什么一个线程只有一个Looper、只有一个MessageQueue？
2. 如何获取当前线程的Looper？是怎么实现的？（理解ThreadLocal）
3. 是不是任何线程都可以实例化Handler？有没有什么约束条件？
4. Looper.loop是一个死循环，拿不到需要处理的Message就会阻塞，那在UI线程中为什么不会导致ANR？
5. Handler.sendMessageDelayed()怎么实现延迟的？消息分发机制？

    结合Looper.loop()循环中，Message=messageQueue.next()和MessageQueue.enqueueMessage()分析。
    
6. handler是如何完成子线程和主线程通讯的


## handler引起的问题：
   
1. 子线程更新UI+Looper创建
2. OOM
4. 空指针
5. 精密倒计时的处理,为什么postDelay/postAtTime倒计时ms级会不准确。

## handler的功能

1. 处理延时任务
2. 线程间通信

## 使用的设计模式

1. 生产者/消费者模式
2. 享元模式（Message）

## handler机制具体的类

1. **Message** ：

    封装的消息，各种参数，其中一个重要的参数是target，指的是Handler，和对应的Handler保持唯一性，发送的handler和处理的handler必须是同一个。
    
2. **MessageQueue**：

    消息队列，负责消息的存储与管理，负责管理由 Handler 发送过来的 Message。读取会自动删除消息，单链表维护，插入和删除上有优势。
    在其next()方法中会无限循 环，不断判断是否有消息，有就返回这条消息并移除。MessageQueue的具体实现是C++实现的。
    
1. **Handler** ：

   消息处理器，负责发送并处理消息，面向开发者，提供 API，并隐藏背后实现的细节；
   
   通过sendMessage()发送消息Message到消息队列MessageQueue，dispatchMessage处理从Loop.loop()中消息队列中取出的message
   
3. **Looper**：

    消息循环器，负责关联线程以及消息的分发，在该线程下从 MessageQueue获取 Message，分发给Handler，
    
    Looper创建的时候，调用Looper.prepare()，会创建一个MessageQueue，并用ThreadLocal实现Thread.currentThread的线程变量的隔离，
    
    调用loop()方法开启轮询，其中会不断调用messageQueue的next() 方法，当有消息就处理，否则阻塞在messageQueue的next()方法中。
    
    当Looper的quit()被调用的 时候会调用messageQueue的quit()，此时next()会返回null，然后loop()方法也就跟着退出
    
5. ThreadLoacal(作用：线程隔离)

### handler原理的具体流程：

（1） 四个类如何运作的。（2）细分主线程和子线程通讯的区别


Handler，Message，looper和MessageQueue构成了安卓的消息机制，（各个类的作用，上边的内容回答一遍）。

1. Handler创建后，通过sendMessage()发送消息Message到消息队列MessageQueue。
2. Looper通过loop()不断提取触发条件的Message，并将Message交给对应的target handler来处理。
3. target handler调用自身的handleMessage()方法来处理Message,从而实现线程的通信
4. 具体handler的使用分为主线程和子线程考虑：

第一在UI线程创建Handler,此时我们不需要手动开启looper，因为在应用启动时，在ActivityThread的main方法中就创建了一个当前主线程的looper，
并开启了消息队列，消息队列是一个无限循环，为什么无限循环不会ANR?因为可以说，应用的整个生命周期就是运行在这个消息循环中的，安卓是由事件驱动的，
Looper.loop不断的接收处理事件，每一个点击触摸或者Activity每一个生命周期都是在Looper.loop的控制之下的，looper.loop一旦结束，
应用程序的生命周期也就结束了。我们可以想想什么情况下会发生ANR，第一，事件没有得到处理，第二，事件正在处理，但是没有及时完成，而对事件进行处理的就是looper，
所以只能说事件的处理如果阻塞会导致ANR，而不能说looper的无限循环会ANR

另一种情况就是在子线程创建Handler,此时由于这个线程中没有默认开启的消息队列，所以我们需要手动调用looper.prepare(),并通过looper.loop开启消息

主线程Looper从消息队列读取消息，当读完所有消息时，主线程阻塞。子线程往消息队列发送消息，并且往管道文件写数据，主线程即被唤醒，从管道文件读取数据，
主线程被唤醒只是为了读取消息，当消息读取完毕，再次睡眠。因此loop的循环并不会对CPU性能有过多的消耗。



## 深层解析，looper.loop()为什么不会阻塞主线程/ Looper 死循环为什么不会导致应用卡死？
参考 https://blog.csdn.net/xinzhou201/article/details/62437786

对于线程即是一段可执行的代码，当可执行代码执行完成后，线程生命周期便该终止了，线程退出。而对于主线程，我们是绝不希望会被运行一段时间，自己就退出，
那么如何保证能一直存活呢？简单做法就是可执行代码是能一直执行下去的，死循环便能保证不会被退出，例如，binder线程也是采用死循环的方法，
通过循环方式不同与Binder驱动进行读写操作，当然并非简单地死循环，无消息时会休眠。但这里可能又引发了另一个问题，既然是死循环又如何去处理其他事务呢？
通过创建新线程的方式，thread.attach(false)方法函数中便会创建一个Binder线程（具体是指ApplicationThread，Binder的服务端，
用于接收系统服务AMS发送来的事件），该Binder线程通过Handler将Message发送给主线程。主线程的消息又是哪来的呢？当然是App进程中的其他线程通过Handler发送给主线程


如果你了解下linux的epoll你就知道为什么不会被卡住了，先说结论：阻塞是有的，但是不会卡住 
主要原因有2个

    epoll模型 
    当没有消息的时候会epoll.wait，等待句柄写的时候再唤醒，这个时候其实是阻塞的。

    所有的ui操作都通过handler来发消息操作。 
    比如屏幕刷新16ms一个消息，你的各种点击事件，所以就会有句柄写操作，唤醒上文的wait操作，所以不会被卡死了。
    
（标准回答）    
其实不然，这里就涉及到Linux pipe/epoll机制，简单说就是在主线程的MessageQueue没有消息时，便阻塞在loop的queue.next()中的nativePollOnce()方法里，
此时主线程会释放CPU资源进入休眠状态，直到下个消息到达或者有事务发生，通过往pipe管道写端写入数据来唤醒主线程工作。这里采用的epoll机制，
是一种IO多路复用机制，可以同时监控多个描述符，当某个描述符就绪(读或写就绪)，则立刻通知相应程序进行读或写操作，本质同步I/O，即读写是阻塞的。
 所以说，主线程大多数时候都是处于休眠状态，并不会消耗大量CPU资源。


system_server进程

    system_server进程是系统进程，java framework框架的核心载体，里面运行了大量的系统服务，比如这里提供ApplicationThreadProxy（简称ATP），
    ActivityManagerService（简称AMS），这个两个服务都运行在system_server进程的不同线程中，由于ATP和AMS都是基于IBinder接口，都是binder线程，
    binder线程的创建与销毁都是由binder驱动来决定的。

App进程

    App进程则是我们常说的应用程序，主线程主要负责Activity/Service等组件的生命周期以及UI相关操作都运行在这个线程； 
    另外，每个App进程中至少会有两个binder线程 ApplicationThread(简称AT)和ActivityManagerProxy（简称AMP），除了图中画的线程，其中还有很多线程

ActivityThread通过ApplicationThread和AMS进行进程间通讯，AMS以进程间通信的方式完成ActivityThread的请求后会回调ApplicationThread中的Binder方法，
然后ApplicationThread会向H发送消息，H收到消息后会将ApplicationThread中的逻辑切换到ActivityThread中去执行，即切换到主线程中去执行，
这个过程就是。主线程的消息循环模型

1个app运行时前首先创建一个进程，该进程是由Zygote fork出来的，用于承载App上运行的各种Activity/Service等组件



### 如果在当前线程内使用Handler postdelayed 两个消息，一个 延迟5s，一个延迟10s，然后使当前线程sleep 5秒，以上消息的执行 时间会如何变化?

答:照常执行

扩展:sleep时间<=5 对两个消息无影响，5< sleep时间 <=10 对第一个消息有影响，第一个消息会延迟
到sleep后执行，sleep时间>10 对两个时间都有影响，都会延迟到sleep后执行。

### 在当前线程内使用Handler postdelayed 两个消息，一个 延迟10s，一个延迟1s，MessageQueue里Message的执行顺序？（很重要）
不管调用postdelayed还是sendMessage，Handler最终调用sendMessageAtTime(Message,SystemClock.uptimeMillis() + delayMillis)。

SystemClock.uptimeMillis()表示从开机到现在的毫秒时间。

postDelay()一个10秒钟的Runnable A、消息进队，MessageQueue调用nativePollOnce()阻塞，Looper阻塞；
紧接着post()一个Runnable B、消息进队，判断现在A时间还没到、正在阻塞，把B插入消息队列的头部（A的前面），然后调用nativeWake()方法唤醒线程；
MessageQueue.next()方法被唤醒后，重新开始读取消息链表，第一个消息B无延时，直接返回给Looper；
Looper处理完这个消息再次调用next()方法，MessageQueue继续读取消息链表，第二个消息A还没到时间，计算一下剩余时间（假如还剩9秒）继续调用nativePollOnce()阻塞；
直到阻塞时间到或者下一次有Message进队；
这样，基本上就能保证Handler.postDelayed()发布的消息能在相对精确的时间被传递给Looper进行处理而又不会阻塞队列了。

MessageQueue会根据post delay的时间排序放入到链表中，链表头的时间小，尾部时间最大。因此能保证时间Delay最长的不会block住时间短的。
当每次post message的时候会进入到MessageQueue的next()方法，会根据其delay时间和链表头的比较，如果更短则，放入链表头，并且看时间是否有delay，
如果有，则block，等待时间到来唤醒执行，否则将唤醒立即执行。

所以handler.postDelay并不是先等待一定的时间再放入到MessageQueue中，而是直接进入MessageQueue，以MessageQueue的时间顺序排列和唤醒的方式结合实现的。
使用后者的方式，我认为是集中式的统一管理了所有message，而如果像前者的话，有多少个delay message，则需要起多少个定时器。前者由于有了排序，
而且保存的每个message的执行时间，因此只需一个定时器按顺序next即可。

### handler postDelay这个延迟是怎么实现的? 
handler.postDelay并不是先等待一定的时间再放入到MessageQueue中，而是直接进入
MessageQueue，以MessageQueue的时间顺序排列和唤醒的方式结合实现的。

### Handler是如何能够线程切换-looper+ThreadLocal

Handler创建的时候会采用当前线程的Looper来构造消息循环系统Looper在哪个线程创建，就跟哪个线程绑定，并且Handler是在他关联的Looper对应的线程中处理消息的。

那么Handler内部如何获取到当前线程的Looper呢—–ThreadLocal。ThreadLocal可以在不同的线程中互不干扰的存储并提供数据，通过ThreadLocal可以轻松获取每个线程的Looper。
当然需要注意的是

①线程是默认没有Looper的，如果需要使用Handler，就必须为线程创建Looper。我们经常提到的主线程，也叫UI线程，它就是ActivityThread，

②ActivityThread被创建时就会初始化Looper，这也是在主线程中默认可以使用Handler的原因。


## Handler其他面试问题


### Handler 引起的内存泄露原因以及最佳解决方案

原因：

Handler 允许我们发送延时消息，如果在延时期间用户关闭了 Activity，那么该 Activity 会泄露。 这个泄露是因为 Message 会持有 Handler，
而又因为 Java 的特性，在java中，非静态的内部类和匿名内部类都会隐式的持有一个外部类的引用。静态内部类则不会持有外部类的引用，
使得 Activity 会被 Handler 持有，这样最终就导致 Activity 泄露。

解决:

**方案1**：在act的onDestory时，调用handler.removeCallbacksAndMessages(null)移除所有消息。

**方案2**：将Handler的声明静态类+弱引用。

Handler的声明静态类，Handler不再持有外部类对象的引用，导致程序不允许你在Handler中操作Activity中的对象了，所以就没法在Hander中操作UI了。
Handler中增加一个对Activity的弱引用，当activity走finish()时，handler持有的是弱引用不是强引用，GC回收时会弱引用对象act会被回收掉

    
     public static class WeakRefHandler extends Handler {
     
             private final WeakReference<TNActBase> mBase;
     
             public WeakRefHandler(TNActBase activity) {
                 mBase = new WeakReference<TNActBase>(activity);
             }
     
             @Override
             public void handleMessage(Message msg) {
                 final TNActBase activity = mBase.get();
                 if (activity != null) {
                     try {
                         activity.handleMessage(msg);
                     } catch (Exception e) {
                         MLog.e("SJY", e.toString());
                     }
     
                 }
             }
         }
     
         /**
          * @param msg
          */
         protected void handleMessage(Message msg) {
             switch (msg.what) {
                 default:
                     break;
             }
         }
### 创建 Message 实例的最佳方式

为了节省开销，Android 给 Message 设计了回收机制，所以我们在使用的时候尽量复用 Message ，减少内存消耗:

通过 Message 的静态方法 Message.obtain();
通过 Handler 的公有方法 handler.obtainMessage()。

### Handler创建的参数Callback用途?
Handler.Callback 有优先处理消息的权利 ：Handler(Callback callback, boolean async)

当一条消息被 Callback 处理并拦截(返回 true)，那么 Handler 的 handleMessage(msg) 方法就不会被调用了;

如果 Callback 处理了消息，但是并没有拦 截，那么就意味着一个消息可以同时被 Callback 以及 Handler 处理。

### 系统为什么不允许在子线程中访问UI？
 这是因为Android的UI控件不是线程安全的，如果在多线程中并发访问可能会导致UI控件处于不可预期的状态，
 那么为什么系统不对UI控件的访问加上锁机制呢？缺点有两个： 
 
 ①首先加上锁机制会让UI访问的逻辑变得复杂 
 
 ②锁机制会降低UI访问的效率，因为锁机制会阻塞某些线程的执行。 
 
 所以最简单且高效的方法就是采用单线程模型来处理UI操作。
 
### handler子线程与主线程通讯？

1. 子线程向主线程通讯：最简单，主线程的handler已经处理了Looper的封装，子线程直接调用handler.sendMessage即可。
2. 主线程向子线程通讯：子线程需要手动封装Looper.prepare()和Looper.looper()方法。同时new Handler(子线程的looper)指定子线程的looper,
   
   android有封装类HandlerThread
    
        
        public class ThreadHandlerActivity extends Activity{     
             //创建子线程
             class MyThread extends Thread{
                 private Looper looper;//取出该子线程的Looper
                 public void run() {
         
                     Looper.prepare();//创建该子线程的Looper
                     looper = Looper.myLooper();//取出该子线程的Looper
                     Looper.loop();//只要调用了该方法才能不断循环取出消息
                 }
             }
         
             private Handler mHandler;//将mHandler指定轮询的Looper
         
             protected void onCreate(Bundle savedInstanceState) {
                     super.onCreate(savedInstanceState);
                     setContentView(R.layout.main);
                     thread = new MyThread();
                     thread.start();
                     //下面是主线程发送消息,注意thread是否启动否则抛异常
                  mHandler = new Handler(thread.looper){
                      public void handleMessage(android.os.Message msg) {
                   Log.d("当前子线程是----->",Thread.currentThread()+"");
                         };
                     };
                     mHandler.sendEmptyMessage(1);
             }
         
         }
3. 子线程和子线程的通讯：在一个子线程中创建一个Handler+Looper,它的回调自然就在此子线程中，然后在另一个子线程中调用此handler来发送消息就可以了


### 框架类对应关系

1. Handler的处理过程运行在创建Handler的线程里
2.  一个线程最多只能对应一个Looper和一个MessageQueue，一个looper可以对应多个handler,用message.target区分handler
3. 一个Thread只能有且只能一个Looper。一个Looper只能对应一个MessageQueue。一个Looper和MessageQueue的绑定体可以对应多个Handler（一个线程可以有多个handler）。
4. 不确定当前线程时，更新UI时尽量调用post方法

## 对handler使用的封装
1. HandlerThread
2. AsyncTask




## 总结及其他引申问题：
1. 子线程一定不能执行UI更新吗？（不是，surfaceView（王者荣耀））
2. 设计模式：（1）生产者消费者模式 （2）享元模式
3. ThreadLocal原理
4. handler.removeCallbacksAndMessages(),消除的是什么(消除的是当前handler对应的消息队列信息)
5. handler.postDelay()倒计时不准确，如何解决（创建新的handelr+消息队列，使该handler只做倒计时处理）

# ThreadLocal详解

参考：https://www.jianshu.com/p/2a34d30806d4

1. ThreadLocal本质是操作线程中ThreadLocalMap来实现本地线程变量的存储的
2. ThreadLocalMap是采用数组的方式来存储数据，其中key(弱引用)指向当前ThreadLocal对象，value为设的值
3. ThreadLocal为内存泄漏采取了处理措施，在调用ThreadLocal的get(),set(),remove()方法的时候都会清除线程ThreadLocalMap里所有key为null的Entry
4. 在使用ThreadLocal的时候，我们仍然需要注意，避免使用static的ThreadLocal，分配使用了ThreadLocal后，
一定要根据当前线程的生命周期来判断是否需要手动的去清理ThreadLocalMap中清key==null的Entry。





