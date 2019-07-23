# Rxjava 总结

## 准备
1. 事件/事件类型：

onNext 观察者会回调对应的onNext

onError  onError事件发送后，其他事件就不发送

onComplete   onComplete事件发送后，其他事件就不发送
   

## 基础/理解
 
 1. 五种观察者：Observable/Single/Maybe/Completable/Flowable
 2. Hot/Cold Observable
 3. Rx规范
 4. 操作符
 5. 线程调度
 6. Transformer
 7. Flowable背压

## 发布订阅模式vs观察者模式
1. 被观察者对象中有观察者对象清单
2. 发布订阅模式中，发布对象向队列管道仍数据，订阅对象从队列管道中取对象，实现解耦操作。

## <1> 五组观察者模式详解

1. Observable/Observer:能发射0或n个数据，并以成功或失败事件终止
2. Flowable/Subscriber：能够发射0或n个数据，并以成功或失败事件终止/支持Backpressure(背压)/可以控制发射源发射速度
3. Single/SingleObserver：只能发射但个数据或失败事件
4. Completable/CompletableObserver：从不发射数据，只处理onComplete和onError事件，可以看成Rx的Runnable
5. Maybe/MaybeOberver：能发射0或1个数据，要么成功，要么失败（类似Optional）

## <2> Rx规范
1. Publisher
2. Subscriber
3. Subscroption
4. Processor

## <3> 操作符详解（2.x）
Rxjava的2.x版本，操作符的核心原理是extends AbstractObservableWithUpstream<T, U>，
并重写 public void subscribeActual(Observer<? super U> t)方法，在该方法中实现自己的逻辑

### 1. 创建操作符
1. 复杂数据遍历：just,fromArray,fromIterable,range
2. 定时任务：interval,intervalRange
3. 嵌套回调异步事件（常用，网络异步请求）：create
4. 延迟任务： defer,timer
### 2. 变换操作符
包括：
1. map
2. flatmap:无法保证发送顺序
3. concatMap：可以保证发送顺序

### 3. 过滤操作符

1. 指定过滤条件，接受需要的事件：filter
2. 过滤指定类型的事件：ofType
3. 过滤条件不满足的事件：skip
4. 过滤重复的事件：distinct,distinctUntilChanged
5. 按时间或数量过滤事件：take
6. 过滤指定位置的事件，特定位置的事件：elementAt，firstElement，elementAtOrError
7. 发射第一项数据，或者是满足条件的第一个数据：first
8. 只发送最后一项数据，或者满足条件的最后一项数据:last 
9. 防止抖动：debounce/throttleWithTimeout
10. 在某段时间内，只发送该段时间内第1次事件 / 最后1次事件:throttleFirst/ throttleLast
11. 在某段时间内，只发送该段时间内最新（最后）1次事件(类似throttleLast):Sample

### 4. 条件操作符
1. 所有事件是否满足:all
2. 当事件满足设定的条件时，该事件的下一个事件不会被发送了:takeUntil
3. 满足条件之外的事件：skipUntil
4. 满足条件的事件，就终止后续事件：takeWhile
5. 当某个数据满足条件时不发送该数据，反之则发送:skipeWhile
6. 判断两个 Observable 发送的事件是否相同:sequenceEqual
7. 是否包含某事件：contains
8. 事件是否为空: isEmpty
9. 如果观察者只发送一个 onComplete() 事件，则可以利用这个方法发送一个值:defaultIfEmpty
10. 被观察者集合中，只接受第一个成功发射事件的被观察者，其余被丢弃：amb()

### 5. 合并操作符
1. (1). 组合多个观察者，合并事件：concat,concatArray(发送事件-串行)(应用：数据获取的多级缓存获取)
(2). concatDelayError,mergeDelayError
(3). merge,mergeArray(发送事件--并行)
(4). mergeArrayDelayError
2. 组合多个被观察者，合并成一个被观察者:zip,combineLatest(应用：登陆条件都满足，设置按钮可点击)
3. 发送事件前，追加其他事件:startWithArray,startWith
4. 组合多个事件为一个事件：reduce,collect
5. 汇总发送事件数量：count

### 6. do操作符（Flowable）

1. doAfterNext：
2. doAfterTerminate：
3. doFinally：
4. doOnCancel：
5. doOnComplete：
6. doOnEach：在执行观察者的每一个方法之前，都会先执行一遍doOnNext
7. doOnError： 修改源Publisher，以便在调用onError时调用它。
8. doOnLifecycle：
9. doOnNext：
10. doOnRequest：
11. doOnSubscribe：
12. doOnTerminate：

### 7. 其他操作符
1. 事件重发：repeat,repeatWhen,repeatUntil
2. 延迟发送：delay
3. 超时：timeout
4. 异常：
（1）抓捕异常并返回一个事件：onErrorResumeNext
(2)抓捕异常并返回一个特殊结果,属于正常终止：onErrorReturn


## <4> 线程调度与线程切换原理详解
1. Single
2. newThread
3. computation
4. io
5. trampoline
6. Schesulers.from

### 线程切换--线程调度器Sheduler分类

1. Schedulers.computation()：默认线程：
2. Schedulers.io()：IO密集型
3. Schedulers.from(Executor):使用指定的Executor作为调度器
4. Schedulers.newThread():为每一个任务创建新线程
5. Schedulers.Single():
6. Schedulers.trampoline():在其他排队任务完成后，在当前排队开始执行
7. AndroidSchedulers.mainThread():UI主线程

### 线程切换--Sheduler执行流程
1. schedulers执行各种线程切换，各个线程都执行基于Scheduler的子类，子类包括：SingleScheduler，IoScheduler等等，各个子类又实现了基于ThreadFactory的RxThreadFactory实例。
2. Scheduler类通过抽象方法createWorker,创建基于Worker的具体子类，包括：NewThreadWorker，EventLoopWorker，ScheduledWorker等，
不同的Scheduler子类，创建不同的worker线程池,由worker的task执行具体线程。
Worker类implements Disposable接口
![okhttp3流程图](https://github.com/66668/Android_Interview/blob/master/pictures/rxjava_schedule_01.png)

### 线程切换--subcribeOn/observerOn
1. subcribeOn连续切换多次线程，只执行最外层的线程（原因是scheduler被最外层的替换，只保留最外层的线程调用，所以只执行一次），

2. observerOn每一次调用的线程切换，都会执行(原因是，每一次执行，都会创建ObserveOnObserver对象，而且，每一个ObserveOnObserver对象都执行)

## <5> Transformer
 1. ObservableTransformer
 2. FlowableTransformer
 3. SingleTransformer
 4. MaybeTransformer
 
## <6>背压(backPressure)详解

概念：被观察者发送数据过快，导致操作符或订阅者不能及时处理信息，从而导致阻塞的现象。
下游不能快速处理上游发送的事件，从而导致的阻塞现象。

### 背压解决方案：使用Flowable（支持背压操作）
说明：2.x版本 Observable已经不支持背压操作。Flowable支持背压操作

如下是Flowable的背压策略：
执行Flowable.create(FlowableOnSubscribe<T> source, BackpressureStrategy mode),方法
 1. Miss：如果流的速度无法同步，会抛出异常：MissingBackpressureException
 2. Buffer:上游不断发送onNext处理请求给下游，直到下游处理完，待处理的请求缓存到无限量的线程池中，直到崩溃
 3. Error：出现背压时，跑异常：MissingBackpressureException（和Miss区别不大）
 4. Drop：将跟不上处理的事件请求抛弃掉（先处理旧的，最后处理新的）
 5. Latest：线程池保留最新的事件给下游处理，旧的事件请求抛弃掉（只处理新的，旧的抛弃掉）
 
## <7> Hot Observable 与 Cold Observable

Observable分为Hot ～ 和 Cold ~,

概念

1. Cold Observable：观察者订阅了，才会开始执行发射数据流的代码。Observable和Observer是一对一的关系，如果有多个Observer，他们各自的事件是独立的
    Observable的 just/create/range/fromxxx等生成的Observable,都是cold
2. Hot Observable：无论观察者有无订阅，事件始终都会发射数据流。多个观察者订阅时，信息是共享的。
3. Cold转换成hot： publish()+Observable.connect()方法实现
4. 场景：hot场景：大量数据库操作，重量级接口频繁使用（注意内存泄漏的问题）


## 8. subject的分类

1. AsyncSubject : 无论订阅发生在什么时候，只发送最后一个数据
2. BehaviorSubject：发送 订阅之前的一个数据，和之后的全部数据
3. ReplaySubject ：无论订阅发生在什么时候，都发射全部数据
4. PublishSubject ：发射订阅后的全部数据

说明：Subject是Observer,也是Observable,可以看作一个桥梁或者代理

用途：预加载机制。需要理解后谨慎使用

## 9. subject与Processor的区别
Processor与Subject类似，不过Processor还支持背压操作

## Rxjava的装饰器模型封装

1. 核心：在使用时，observable.subscribe(observer)的subscribe方法，其实是调用装饰器中，最外层的封装的对象的subscribe,层层向里
，最终又调用的最里头的Observable对象的subscribe方法。

## Rxjava的应用
1. 网络请求：轮询，嵌套，重试
   (1)线程切换的封装：使用ObservableTransformer
   (2)嵌套请求：flatmap 
   (3)获取验证码倒计时：interval+take
   (4)自动搜索:RxTextView.textChanged() +debounce+skip(避免输入一个数据查询一次，防抖加过滤)
   （5）retry:出错重试
2. 功能防抖
 (1)功能防抖：Rxbinding及自定义实现
 
3. 从多级缓存获取数据
   （1）concat()合并三个缓存（内存，disk,net三个观察者对象）按顺序串联成队列
   （2）+firstElement()拿队列中的有效next事件处理

4. 合并数据源
    (1)将多个数据源（网络或本地等）合并处理
5. 自定义Rxbinding/Rxbus

## 面试
1. rxjava如何通过观察者模式实现的？
2. rxjava的观察者和不同观察者的区别？
（1） 被观察者对象中有观察者对象清单
（2）发布订阅模式中，发布对象向队列管道仍数据，订阅对象从队列管道中取对象，实现解耦操作。
2. rxjava如何实现线程切换的？/subsribeOn为什么只执行第一次，而observerOn可以执行多次线程切换？


    subsribeOn思考：
    1.是哪个对象在什么时候创建了子线程，是一种怎样的方式创建的？
    2.子线程又是如何启动的？
    3.上游事件是怎么跑到子线程里执行的？
    4.多次用 subscribeOn 指定上游线程为什么只有第一次有效 ?


3. rxjava如何实现轮询？
4. hot Observable 与cold Observable如何互相转换
5. 五种观察者及详解（见上）