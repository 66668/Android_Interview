# 线程学习 基础与进阶

## 内容概括

1. android 常用四种调用线程的方法,及特殊线程HandlerThread;

2. android线程间的通信

3. android线程同步

# Thread、AsyncTask、IntentService的使用场景与特点。
1. Thread线程，独立运行与于 Activity 的，当Activity 被 finish 后，如果没有主动停止 Thread或者 run 方法没有执行完，其会一直执行下去。
2. AsyncTask 封装了两个线程池和一个Handler(SerialExecutor用于排队， THREAD_POOL_EXECUTOR为真正的执行任务，Handler将工作线程切换到主线程)，其必须在 UI线程中创建，execute 方法必须在 UI线程中执行，一个任务实例只允许执行一次，执行多次抛 出异常，用于网络请求或者简单数据处理。
3. IntentService:处理异步请求，实现多线程，在onHandleIntent中处理耗时操作，多个耗时任务 会依次执行，执行完毕自动结束。

## 一Thread介绍：

###（1）Thread主要函数

run()//包含线程运行时所执行的代码 

start()//用于启动线程

sleep()/sleep(long millis)//线程休眠，交出CPU，让CPU去执行其他的任务，然后线程进入阻塞状态，sleep方法不会释放锁

yield()//使当前线程交出CPU，让CPU去执行其他的任务，但不会是线程进入阻塞状态，而是重置为就绪状态，yield方法不会释放锁

join()/join(long millis)/join(long millis,int nanoseconds)//等待线程终止，直白的说 就是发起该子线程的线程 只有等待该子线程运行结束才能继续往下运行

wait()//交出cpu，让CPU去执行其他的任务，让线程进入阻塞状态，同时也会释放锁

interrupt()//中断线程，自stop函数过时之后，我们通过interrupt方法和isInterrupted()方法来停止正在运行的线程，注意只能中断已经处于阻塞的线程

getId()//获取当前线程的ID

getName()/setName()//获取和设置线程的名字

getPriority()/setPriority()//获取和这是线程的优先级 一般property用1-10的整数表示，默认优先级是5，优先级最高是10，优先级高的线程被执行的机率高

setDaemon()/isDaemo()//设置和判断是否是守护线程

currentThread()//静态函数获取当前线程

###（2）Thread线程主要状态

（1） New  一旦被实例化之后就处于new状态

（2） Runnable 调用了start函数之后就处于Runnable状态

（3） Running 线程被cpu执行 调用run函数之后 就处于Running状态

 (4)   Blocked 调用join()、sleep()、wait()使线程处于Blocked状态

 (5)   Dead    线程的run()方法运行完毕或被中断或被异常退出，线程将会到达Dead状态
 
 ###（3）线程的3种启动方式
 开启线程后，子线程独自运行，不影响主线程的的运行，但是，当子线程运行完，需要将结果返回给UI线程，让UI线程更新ui，需要使用handler，这是android独有的方式
  handler的原生写法，需要再次封装成软饮用/弱引用：当子线程运行过程中，当前Act销毁，子线程结果返回给销毁的Act，会抛出异常使用弱引用后，销毁的act不会强制
  处理消息队列信息，避免异常 弱引用的handler,最好写在baseAct中，方便其他act使用。
  
  
线程的3种启动方式：
####（1）new Thread/自定义Thread

eg1：

        Thread thread0 = new Thread(new Runnable() {
            @Override
            public void run() {
                //耗时操作
              
            }
        });
        thread0.start();
        
 eg2:
 
         MyThread2 thread1 = new MyThread2("name1");
         thread1.start();  
         
              
 自定义Thread
  
         private class MyThread2 extends Thread {
     
             public MyThread2(String name) {
                 super(name);
             }
     
             @Override
             public void run() {
                 super.run();
         
             }
         } 
         
####（2）实现Runnable/自定义Runnable

  eg1：
    
        Thread thread1 = new Thread(this);
        thread1.start();
        
  而this，是指在Act类后，添加 implements Runnable,然后act类中重写 run方法：
  
      @Override
      public void run() {
          //
      }

eg2：

        Thread thread1 = new Thread(new MyRunnable());
        thread1.start();
        
这是自定义的Runnable 类

            private class MyRunnable implements Runnable {
        
                @Override
                public void run() {
                   
               }
            }
####（3）实现Callable/自定义Callable    

定义一个自定义Callable
     /**
         * 实现callable接口，又返回值
         */
        class MyCallable implements Callable<String> {
            @Override
            public String call() throws Exception {
    
                Log.d("SJY", "这是自定义Callable回调");
                int num = (int) ((Math.random()) * 2000 + 1000);
                try {
                    Thread.sleep(num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                builder.append("\n当前线程名称=" + Thread.currentThread().getName() + "-->睡眠" + num + "ms"+"这是自定义Callable回调");
                return "这是自定义Callable回调";
            }
        }
        
调用：
    
        MyCallable myCallable = new MyCallable();
         FutureTask<String> mFutrueTask = new FutureTask<String>(myCallable);
         ziThread mThread = new Thread(mFutrueTask);
         mThread.start();
                        


###（4）线程的构造解读：
总共有8个构造重载，05--08可以理解成一个重载，使用方式见代码，

####注：当自定义Thread(Runnable run1)类中还有重写的run方法，执行顺序是；先执行参数run1，再执行重写的run方法

        /**
         * 01 new 自定义无参thread,需在该线程中重写run方法。
         */
        public MyThread() {

        }

        /**
         * 02 给自定义的thread线程起一个名称
         *
         * @param name
         */
        public MyThread(String name) {
            super(name);
        }
        
        /**
         * 03 implement Runnable形式,该Runnable可以自定义
         *
         * @param target
         */
        public MyThread2(Runnable target) {
            super(target);
        }

        /**
         * 04 有名称的线程，使用Runnable形式
         */
        public MyThread2(Runnable target, String name) {
            super(target, name);
        }
        /**
         * 05
         *
         * @param group
         * @param target
         */
        public MyThread3(ThreadGroup group, Runnable target) {
            super(group, target);
        }

        /**
         * 06
         *
         * @param group
         * @param name
         */
        public MyThread3(ThreadGroup group, String name) {
            super(group, name);
        }

        /**
         * 07
         *
         * @param group
         * @param target
         * @param name
         */
        public MyThread3(ThreadGroup group, Runnable target, String name) {
            super(group, target, name);
        }

        /**
         * 08
         *
         * @param group     指定当前线程的线程组
         * @param target    需要指定，或者 在自定义线程中重写run
         * @param name      线程的名称，不指定自动生成
         * @param stackSize 预期堆栈大小，不指定默认为0
         */
        public MyThread3(ThreadGroup group, Runnable target, String name, long stackSize) {
            super(group, target, name, stackSize);
        }



 ## 二 HandlerThread介绍：
 
 **原理**：
 
 当系统有多个耗时任务需要执行时，每个任务都会开启个新线程去执行耗时任务，这样会导致系统多次 创建和销毁线程，从而影响性能。为了解决这一问题，Google提出了HandlerThread
 
 HandlerThread本质上是一个线程类，它继承自Thread，start开启线程后，会在其run方法中会通过Looper创建消息队列并开启消息循环，这个消息队列运行在子线程中，
 
 所以可以将HandlerThread中的Looper实例传递给一个Handler，从而保证这个Handler的handleMessage方法运行在子线程中，当有耗时任务进入队列时，则不需要开启新线程，在原有的线程中执行耗时任务即可，否则线程阻塞
 
 由于HanlderThread的run()方法是一个无限循环，因此当明确不需要再使用 HandlerThread时，可以通过它的quit或者quitSafely方法来终止线程的执行。
 
 Android中使用HandlerThread的一个场景就是IntentService
 
 
 HandlerThread特点:
 
 1. 本质是线程，继承Thread
 
 2. HandlerThread内部有自己的Looper对象,可以在当前线程中处理分发消息
 
3. 通过获取HandlerThread的looper对象传递给Handler对象，可以在handleMessage方法中执行异步任务

4. 可以实现主线程向子线程发送消息

5. HandlerThread**优点是异步不会堵塞，减少对性能的消耗,避免频繁的new Thread操作**。 HandlerThread**缺点是不能同时继续进行多任务处理，要等待进行处理，处理效率较低**。
 HandlerThread与线程池不同，HandlerThread是一个串队列，背后只有一个线程。
 
优点：

1.开发中如果多次使用类似new Thread(){}.start()这种方式开启子线程，会创建多个匿名线程，使得程序运行起来越来越慢，
而HandlerThread自带Looper使他可以通过消息机制来多次重复使用当前线程，节省开支。
 
 2.Handler类内部的Looper默认绑定的是UI线程的消息队列，对于非UI线程如果需要使用消息机制，
自己去创建Looper较繁琐，由于HandlerThread内部已经自动创建了Looper，直接使用HandlerThread更方便



### 常见用法：
用法1：Handler(Looper looper,Callback callback)执行异步

用法2：Handler(Looper looper）{handleMessage}执行异步

用法3：handler.post(runnable)执行异步

用法4:验证handler的post和重写handleMessage的执行顺序

## 三 IntentService 异步介绍：
原理：

回答1：

IntentService是一种特殊的Service，它继承了Service并且它是一个抽象类，因此必须创建它的子类才
能使用IntentService。

继承自Service，它的内部封装了HandlerThread和Handler，可以执行耗时任务，同时因为它是一个服务，优先级比普通线程高很多，
所以更适合执行一些高优先级的后台任务，HandlerThread底层通过Looper消息队列实现的，所以它是顺序的执行每一个任务。
可以通过Intent的方式开启IntentService，IntentService通过handler将每一个intent加入HandlerThread子线程中的消息队列，
通过looper按顺序一个个的取出并执行，执行完成后自动结束自己，不需要开发者手动关闭



它本质是一种特殊的Service,继承自Service并且本身就是一个抽象类

它可以用于在后台执行耗时的异步任务，当任务完成后会自动停止

它拥有较高的优先级，不易被系统杀死（继承自Service的缘故），因此比较适合执行一些高优先级的异步任务

它内部通过HandlerThread和Handler实现异步操作

创建IntentService时，只需实现onHandleIntent和 空构造方法这两步骤即可，onHandleIntent为异步方法，可以执行耗时操作


回答2：


在实现上，IntentService封装了HandlerThread和Handler。当IntentService被第一次启动时，它的 onCreate()方法会被调用，onCreat()方法会创建一个HandlerThread，
然后使用它的Looper来构造一 个Handler对象mServiceHandler，这样通过mServiceHandler发送的消息最终都会在HandlerThread 中执行。

生成一个默认的且与主线程互相独立的工作者线程来执行所有传送至onStartCommand()方法的 Intetnt。

生成一个工作队列来传送Intent对象给onHandleIntent()方法，同一时刻只传送一个Intent对象，这样 一来，你就不必担心多线程的问题。
在所有的请求(Intent)都被执行完以后会自动停止服务，所以，你 不需要自己去调用stopSelf()方法来停止。

该服务提供了一个onBind()方法的默认实现，它返回null。
 
提供了一个onStartCommand()方法的默认实现，它将Intent先传送至工作队列，然后从工作队列中每次取出一个传送至onHandleIntent()方法，在该方法中对Intent做相应的处理。

### 为什么在mServiceHandler的handleMessage()回调方法中执行完onHandlerIntent()方法后要使用带参数的stopSelf()方法?

因为stopSel()方法会立即停止服务，

而stopSelf(int startId)会等待所有的消息都处理完毕后才终止服务，

一般来说，stopSelf(int startId)在尝试停止服务之前会判断最近启动服务的次数是否和startId相等，如果相等就立刻停止服务，不相等则不停止服务。

### 使用步骤：

 1:自定义的ItnentService重写onHandleIntent方法
 
 2：在Manifest.xml注册自定义的ItnentService
 
 3: startService(intent);启动服务
 
 
### 常见用法：

方式1：自定义回调UI

方式2：LocalBroadcastManager触发广播回调UI（内部封装了）

方式3：act触发广播回调UI

## 四 线程池介绍：

Android中的线程池都是直接或间接通过配置ThreadPoolExecutor来实现不同特性的线程池.

Android中最常见的类具有不同特性的线程池分别为FixThreadPool、CachedhreadPool、SingleThreadPool、 ScheduleThreadExecutr.

1. FixThreadPool: 

    只有核心线程,并且数量固定的,也不会被回收,所有线程都活动时,因为队列没有限制大小,新任务会等待执行.

    优点:更快的响应外界请求.
2. SingleThreadPool: 

    只有一个核心线程,确保所有的任务都在同一线程中按序完成.因此不需要处理线程同步的问题.
    
3. CachedThreadPool

只有非核心线程,最大线程数非常大,所有线程都活动时会为新任务创建新线程,否则会利用空闲线程(60s 空闲时间,过了就会被回收,所以线程池中有0个线程的可能)处理任务.

优点:任何任务都会被立即执行(任务队列SynchronousQuue相当于一个空集合);比较适合执行大量的耗时较少的任务.

4. ScheduledThreadPool:

核心线程数固定,非核心线程(闲着没活干会被立即回收数)没有限制. 

优点:执行定时任务以及有固定周期的重复任务

**线程池的工作原理**：

线程池可以减少创建和销毁线程的次数，从而减少系统资源的消耗，当一个任务提交到线程池时
    首先判断核心线程池中的线程是否已经满了，如果没满，则创建一个核心线程执行任务，否则进入下一步
    判断工作队列是否已满，没有满则加入工作队列，否则执行下一步
    判断线程数是否达到了最大值，如果不是，则创建非核心线程执行任务，否则执行饱和策略，默认抛出异常


### 1Executors类详解：

 使用说明，其下有6类线程创建方式，还有其他三个辅助方法

  =========================================6类线程创建=======================================
  
  (1)
  
  public static ExecutorService newSingleThreadExecutor()
  
  public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory)
  
  用于创建只有一个线程的线程池
  hreadFactory，自定义创建线程时的行为

  (2)
  
  public static ExecutorService newCachedThreadPool()
  
  public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory)
  
  用于创建线程数数目可以随着实际情况自动调节的线程池
  当线程池有很多任务需要处理时，会不断地创建新线程，当任务处理完毕之后，如果某个线程空闲时间大于60s，则该线程将会被销毁。
  因为这种线程池能够自动调节线程数量，所以比较适合执行大量的短期的小任务
 
  (3)
  
  public static ScheduledExecutorService newSingleThreadScheduledExecutor()
  
  public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory)
  
  用于创建只有一个线程的线程池，并且该线程定时周期性地执行给定的任务
  说明：线程在周期性地执行任务时如果遇到Exception，则以后将不再周期性地执行任务
 
  (4)
  
  public static ExecutorService newFixedThreadPool(int nThreads)
  
  public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory)
  
  用于创建一个最大线程数的固定的线程池，多余任务存储在队列中，等待线程池某一个线程完成任务，后续任务补充进去继续使用，总之，保持最大线程数不变
  threadFactory是线程工厂类，主要用于自定义线程池中创建新线程时的行为
  
  （5）
  
  public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
  
  public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory)
  
  用于创建一个线程池，线程池中得线程能够周期性地执行给定的任务

  (6)
  
  public static ExecutorService newWorkStealingPool(int parallelism)
  
  public static ExecutorService newWorkStealingPool()
  
  用于创建ForkJoin框架中用到的ForkJoinPool线程池，参数parallelism用于指定并行数，默认使用当前机器可用的CPU个数作为并行数。
  
  =============================================其他方法==============================================
  
  (7)
  
  public static ExecutorService unconfigurableExecutorService(ExecutorService executor)
  
  用于包装现有的线程池，包装之后的线程池不能修改，相当于final
  
  (8)
  
  public static ScheduledExecutorService unconfigurableScheduledExecutorService(ScheduledExecutorService executor)
  
  用于包装可以周期性执行任务的线程池，包装之后的线程池不能修改，相当于final
  
  (9)
  
  public static ThreadFactory defaultThreadFactory()
  
  返回默认的工厂方法类，默认的工厂方法为线程池中新创建的线程命名为：pool-[虚拟机中线程池编号]-thread-[线程编号
  
  (10)
  
  public static ThreadFactory privilegedThreadFactory()
  
  返回用于创建新线程的线程工厂，这些新线程与当前线程具有相同的权限。此工厂创建具有与 defaultThreadFactory() 相同设置的线程，
    新线程的 AccessControlContext 和 contextClassLoader 的其他设置与调用此 privilegedThreadFactory 方法的线程相同。可以在 AccessController.doPrivileged(java.security.PrivilegedAction) 操作中创建一个新 privilegedThreadFactory，设置当前线程的访问控制上下文，以便创建具有该操作中保持的所选权限的线程。
    注意，虽然运行在此类线程中的任务具有与当前线程相同的访问控制和类加载器，但是它们无需具有相同的 ThreadLocal
    或 InheritableThreadLocal 值。如有必要，使用 ThreadPoolExecutor.beforeExecute(java.lang.Thread, java.lang.Runnable)
    在 ThreadPoolExecutor 子类中运行任何任务前，可以设置或重置线程局部变量的特定值。
    另外，如果必须初始化 worker 线程，以具有与某些其他指定线程相同的 InheritableThreadLocal 设置，
    则可以在线程等待和服务创建请求的环境中创建自定义的 ThreadFactory，而不是继承其值。
    
 (11)
 
        public static Callable <Object> callable(Runnable task) 
  运行给定的任务并返回 null
   
        public static <T> Callable<T> callable(Runnable task,T result) 
  运行给定的任务并返回给定的结果。这在把需要 Callable 的方法应用到其他无结果的操作时很有用
   
        public static Callable<Object> callable(PrivilegedAction<?> action) 
  运行给定特权的操作并返回其结果
  

## 五 AsyncTask：

### 内部原理

1. 内部是Handler和两个线程池实现的，是一种轻量级的异步任务类，Handler用于将线程切换到主线程，两个线程池一个用于任务的排队，一个用于执行任务。

2. 当AsyncTask执行execute方法时会封装出一个FutureTask对象，将这个对象加入队列中，如果此时没有正在执行的任务，就执行它，
执行完成之后继续执行队列中下一个任务，执行完成通过Handler将事件发送到主线程。
AsyncTask必须在主线程初始化，因为内部的Handler是一个静态对象，在AsyncTask类加载的时候他就已经被初始化了。
在Android3.0开始，execute方法串行执行任务的，一个一个来，3.0之前是并行执行的。如果要在3.0上执行并行任务，可以调用executeOnExecutor方法


        AsyncTask中有两个线程池(SerialExecutor和THREAD_POOL_EXECUTOR)和一个 Handler(InternalHandler)，
        其中线程池SerialExecutor用于任务的排队，而线程池 THREAD_POOL_EXECUTOR用于真正地执行任务，InternalHandler用于将执行环境从线程池切换 到主线程)
        
        sHandler是一个静态的Handler对象(InternalHandler)，为了能够将执行环境切换到主线程，这就要求sHandler这 个对象必须在主线程创建。由于静态成员会在加载类的时候进行初始化，
        因此这就变相要求 AsyncTask的类必须在主线程中加载，否则同一个进程中的AsyncTask都将无法正常工作
        
        AsyncTask是一个抽象的泛型类，它提供了Params、Progress和Result这三个泛型参数，
        其中Params 表示参数的类型，Progress表示后台任务的执行进度和类型，而Result则表示后台任务的返回结果的类型，
        如果AsyncTask不需要传递具体的参数，那么这三个泛型参数可以用Void来代替。


protected void onPreExecute() {}   //预执行
protected abstract Result doInBackground(Params... params); //执行后台任务
protected void onProgressUpdate(Progress... values) {}//执行进度反馈
protected void onPostExecute(Result result) {} //执行完毕
protected void cancel() {} //终止执行


### AsyncTask的缺点及注意点

必须在主线程中加载，不然在API 16以下不可用，但目前来说，大部分app最低版本也到16了，这个缺点可以忽略了
1. 内存泄露
如果AsyncTask被声明为Activity的非静态内部类，那么AsyncTask会保留一个对Activity的引用。如果 Activity已经被销毁，
AsyncTask的后台线程还在执行，它将继续在内存里保留这个引用，导致Activity 无法被回收，引起内存泄漏
-->改为静态内部类
2. Activity 已被销毁，doInBackground还没有执行完，执行完后再执行 onPostResult, 导致产生异常 
-->记得在Activity的 onDestroy 方法中调 cancel方法取消 AsyncTask
（1.内存泄漏 2.生命周期没有跟Activity同步，建议用线程池）

3. 每个AsyncTask实例只能执行一次。

4. 结果丢失 屏幕旋转或Activity在后台被系统杀掉等情况会导致Activity的重新创建，之前运行的AsyncTask会持有
一个之前Activity的引用，这个引用已经无效，这时调用onPostExecute()再去更新界面将不再生效。

5. 并行还是串行
在Android1.6之前的版本，AsyncTask是串行的
在1.6之后的版本，采用线程池处理并行任务，
从 Android 3.0开始，为了避免AsyncTask所带来的并发错误，又采用一个线程来串行执行任务。可以使用 executeOnExecutor()方法来并行地执行任务。

### 关于线程池:
AsyncTask对应的线程池ThreadPoolExecutor都是进程范围内共享的，且都是static的，所以是 Asynctask控制着进程范围内所有的子类实例。由于这个限制的存在，当使用默认线程池时，如果线程 数超过线程池的最大容量，线程池就会爆掉(3.0后默认串行执行，不会出现个问题)。针对这种情况，可 以尝试自定义线程池，配合Asynctask使用。
### 关于默认线程池:
AsyncTask里面线程池是一个核心线程数为CPU + 1，最大线程数为CPU * 2 + 1，工作队列长度为128 的线程池，线程等待队列的最大等待数为28，但是可以自定义线程池。线程池是由AsyncTask来处理 的，线程池允许tasks并行运行，需要注意的是并发情况下数据的一致性问题，新数据可能会被老数据 覆盖掉。所以希望tasks能够串行运行的话，使用SERIAL_EXECUTOR。
### AsyncTask在不同的SDK版本中的区别:
调用AsyncTask的execute方法不能立即执行程序的原因及改善方案通过查阅官方文档发现，AsyncTask 首次引入时，异步任务是在一个独立的线程中顺序的执行，也就是说一次只执行一个任务，不能并行的 执行，从1.6开始，AsyncTask引入了线程池，支持同时执行5个异步任务，也就是说只能有5个线程运 行，超过的线程只能等待，等待前的线程直到某个执行完了才被调度和运行。换句话说，如果进程中的 AsyncTask实例个数超过5个，那么假如前5都运行很长时间的话，那么第6个只能等待机会了。这是
AsyncTask的一个限制，而且对于2.3以前的版本无法解决。如果你的应用需要大量的后台线程去执行任 务，那么只能放弃使用AsyncTask，自己创建线程池来管理Thread。不得不说，虽然AsyncTask较 Thread使用起来方便，但是它最多只能同时运行5个线程，这也大大局限了它的作用，你必须要小心设 计你的应用，错开使用AsyncTask时间，尽力做到分时，或者保证数量不会大于5个，否就会遇到上面提 到的问题。可能是Google意识到了AsynTask的局限性了，从Android 3.0开始对AsyncTask的API做出了 一些调整:每次只启动一个线程执行一个任务，完了之后再执行第二个任务，也就是相当于只有一个后 台线程在执行所提交的任务。

