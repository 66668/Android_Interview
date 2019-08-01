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

handler原理的回答流程：
（1） 四个类如何运作的。（2）细分主线程和子线程通讯的区别

Handler，Message，looper和MessageQueue构成了安卓的消息机制，Handler创建后，将要处理的内容封装到Message，在将这个Message对象发送给消息队列Messagequeue保存，Looper.loop()一直轮询，从消息队列中取出消息，
回调到之前创建的handler的handleMessage方法中处理。handler还需要区分UI线程和子线程的区别：

情况一：在UI线程中，创建的Handler对象不需要再创建Looper轮询。android系统已经在ActivityThread的Main方法中处理了（Looper.prepareMainLooper()和Looper.loop()），让轮询在应用的整个生命周期中循环。

情况二：在子线程中创建handler时，需要手动添加Looper轮询（Looper.prepare()和Looper.loop()）,主线程Looper从消息队列读取消息，当读完所有消息时，主线程阻塞。
子线程往消息队列发送消息，并且往管道文件写数据，主线程即被唤醒，从管道文件读取数据，主线程被唤醒只是为了读取消息，当消息读取完毕，再次睡眠。
因此loop的循环并不会对CPU性能有过多的消耗.


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
2. next(): 从队列中取消息，难点，也是逻辑核心，必须记住。

### Message
封装信息，各种参数，其中一个重要的参数是target，指的是Handler，和对应的Handler保持唯一性，发送的handler和处理的handler必须是同一个。


### 框架类对应关系

1. Handler的处理过程运行在创建Handler的线程里
2.  一个Looper对应一个MessageQueue
3. 一个线程对应一个Looper，
4. 一个Looper可以对应多个Handler
5. 不确定当前线程时，更新UI时尽量调用post方法
6. 一个Thread只能有且只能一个Looper。一个Looper只能对应一个MessageQueue。一个Looper和MessageQueue的绑定体可以对应多个Handler（一个线程可以有多个handler）。

## 对handler使用的封装
1. HandlerThread




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


