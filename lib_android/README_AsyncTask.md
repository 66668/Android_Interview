# AsyncTask 总结

## 内部原理

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


## AsyncTask的缺点及注意点

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


