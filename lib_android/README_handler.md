# handler机制详解及相关封装

对个人要求：必须熟悉源码，会手写handler机制的框架源码，对处理逻辑必须会手写。
四、消息分发机制
这个考得非常常见。一定要看源码，代码不多。带着几个问题去看：
1.为什么一个线程只有一个Looper、只有一个MessageQueue？
2.如何获取当前线程的Looper？是怎么实现的？（理解ThreadLocal）
3.是不是任何线程都可以实例化Handler？有没有什么约束条件？
4.Looper.loop是一个死循环，拿不到需要处理的Message就会阻塞，那在UI线程中为什么不会导致ANR？
5.Handler.sendMessageDelayed()怎么实现延迟的？结合Looper.loop()循环中，Message=messageQueue.next()和MessageQueue.enqueueMessage()分析。


## 常见面试题：

1. handler是如何完成子线程和主线程通讯的

## handler引起的问题：
   
1. 子线程更新UI+Looper创建
2. OOM
3. Message优化
4. 空指针
5. 精密倒计时的处理,为什么postDelay/postAtTime倒计时ms级会不准确。

## handler的功能

1. 处理延时任务
2. 线程间通信

## 使用的设计模式

1. 生产者/消费者模式
2. 享元模式（Message）

## 支撑handler框架的相关类和整体操作逻辑

1. Handler
2. MessageQueue
3. Looper
4. Message
5. ThreadLoacal(作用：线程隔离)

### handler原理的回答流程：

（1） 四个类如何运作的。（2）细分主线程和子线程通讯的区别

回答1：

Handler，Message，looper和MessageQueue构成了安卓的消息机制，Handler创建后，将要处理的内容封装到Message，再将这个Message对象发送给消息队列Messagequeue保存，Looper.loop()一直轮询，从消息队列中取出消息，
回调到之前创建的handler的handleMessage方法中处理。handler还需要区分UI线程和子线程的区别：

情况一：在UI线程中，创建的Handler对象不需要再创建Looper轮询。android系统已经在ActivityThread的Main方法中处理了（Looper.prepareMainLooper()和Looper.loop()），让轮询在应用的整个生命周期中循环。

情况二：在子线程中创建handler时，需要手动添加Looper轮询（Looper.prepare()和Looper.loop()）,主线程Looper从消息队列读取消息，当读完所有消息时，主线程阻塞。
子线程往消息队列发送消息，并且往管道文件写数据，主线程即被唤醒，从管道文件读取数据，主线程被唤醒只是为了读取消息，当消息读取完毕，再次睡眠。
因此loop的循环并不会对CPU性能有过多的消耗.

回答2：

Handler，Message，looper和MessageQueue构成了安卓的消息机制，handler创建后可以通过sendMessage将消息加入消息队列，然后looper不断的将消息从MessageQueue中取出来，回调到Hander的handleMessage方法，从而实现线程的通信。

从两种情况来说，第一在UI线程创建Handler,此时我们不需要手动开启looper，因为在应用启动时，在ActivityThread的main方法中就创建了一个当前主线程的looper，
并开启了消息队列，消息队列是一个无限循环，为什么无限循环不会ANR?因为可以说，应用的整个生命周期就是运行在这个消息循环中的，安卓是由事件驱动的，
Looper.loop不断的接收处理事件，每一个点击触摸或者Activity每一个生命周期都是在Looper.loop的控制之下的，looper.loop一旦结束，
应用程序的生命周期也就结束了。我们可以想想什么情况下会发生ANR，第一，事件没有得到处理，第二，事件正在处理，但是没有及时完成，而对事件进行处理的就是looper，
所以只能说事件的处理如果阻塞会导致ANR，而不能说looper的无限循环会ANR

另一种情况就是在子线程创建Handler,此时由于这个线程中没有默认开启的消息队列，所以我们需要手动调用looper.prepare(),并通过looper.loop开启消息

主线程Looper从消息队列读取消息，当读完所有消息时，主线程阻塞。子线程往消息队列发送消息，并且往管道文件写数据，主线程即被唤醒，从管道文件读取数据，
主线程被唤醒只是为了读取消息，当消息读取完毕，再次睡眠。因此loop的循环并不会对CPU性能有过多的消耗。


### 1. Handler
1. 发送消息： 将各种信息封装到Message中，最终，handler通过enqueeuMessage方法，将message保存到消息队列MessageQueue中。
2. 处理消息dispatchMessage: Loop.loop()不断从消息队列中取出message，通过message里的参数handler标记，将消息交给dispatchMessage处理。
3. 其他处理

### Looper
1. 初始化Looper对象：（1）通过ThreadLocal实现线程变量的隔离。（2）这里初始化一般是初始化子线程Looper.prepare(),主线程已经系统处理了。
    初始化的内容包括new MessageQueue()/mThread=Thread.currentThread
2. 开启轮询Looper.loop: 该处是处理的难点，核心逻辑必须记住（当消息队列为空时，进入等待状态，避免阻塞主线程）
3. 其他处理

### MessageQueue
1. enqueueMessage()：将message发送到消息队列
2. next(): 从队列中取消息,next()在,等待的实现不是在java层实现的，而是在linux底层实现的


### Message
封装信息，各种参数，其中一个重要的参数是target，指的是Handler，和对应的Handler保持唯一性，发送的handler和处理的handler必须是同一个。

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
    
其实不然，这里就涉及到Linux pipe/epoll机制，简单说就是在主线程的MessageQueue没有消息时，便阻塞在loop的queue.next()中的nativePollOnce()方法里，
此时主线程会释放CPU资源进入休眠状态，直到下个消息到达或者有事务发生，通过往pipe管道写端写入数据来唤醒主线程工作。这里采用的epoll机制，
是一种IO多路复用机制，可以同时监控多个描述符，当某个描述符就绪(读或写就绪)，则立刻通知相应程序进行读或写操作，本质同步I/O，即读写是阻塞的。
 所以说，主线程大多数时候都是处于休眠状态，并不会消耗大量CPU资源。

system_server进程

    system_server进程是系统进程，java framework框架的核心载体，里面运行了大量的系统服务，比如这里提供ApplicationThreadProxy（简称ATP），ActivityManagerService（简称AMS），这个两个服务都运行在system_server进程的不同线程中，由于ATP和AMS都是基于IBinder接口，都是binder线程，binder线程的创建与销毁都是由binder驱动来决定的。

App进程

    App进程则是我们常说的应用程序，主线程主要负责Activity/Service等组件的生命周期以及UI相关操作都运行在这个线程； 另外，每个App进程中至少会有两个binder线程 ApplicationThread(简称AT)和ActivityManagerProxy（简称AMP），除了图中画的线程，其中还有很多线程

ActivityThread通过ApplicationThread和AMS进行进程间通讯，AMS以进程间通信的方式完成ActivityThread的请求后会回调ApplicationThread中的Binder方法，
然后ApplicationThread会向H发送消息，H收到消息后会将ApplicationThread中的逻辑切换到ActivityThread中去执行，即切换到主线程中去执行，
这个过程就是。主线程的消息循环模型

个app运行时前首先创建一个进程，该进程是由Zygote fork出来的，用于承载App上运行的各种Activity/Service等组件

### Handler是如何能够线程切换-looper+ThreadLocal
Handler创建的时候会采用当前线程的Looper来构造消息循环系统Looper在哪个线程创建，就跟哪个线程绑定，并且Handler是在他关联的Looper对应的线程中处理消息的。

那么Handler内部如何获取到当前线程的Looper呢—–ThreadLocal。ThreadLocal可以在不同的线程中互不干扰的存储并提供数据，通过ThreadLocal可以轻松获取每个线程的Looper。
当然需要注意的是

①线程是默认没有Looper的，如果需要使用Handler，就必须为线程创建Looper。我们经常提到的主线程，也叫UI线程，它就是ActivityThread，

②ActivityThread被创建时就会初始化Looper，这也是在主线程中默认可以使用Handler的原因。

### 系统为什么不允许在子线程中访问UI？
 这是因为Android的UI控件不是线程安全的，如果在多线程中并发访问可能会导致UI控件处于不可预期的状态，
 那么为什么系统不对UI控件的访问加上锁机制呢？缺点有两个： 
 
 ①首先加上锁机制会让UI访问的逻辑变得复杂 
 
 ②锁机制会降低UI访问的效率，因为锁机制会阻塞某些线程的执行。 
 
 所以最简单且高效的方法就是采用单线程模型来处理UI操作。

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





