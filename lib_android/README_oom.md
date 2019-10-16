# 内存泄漏OOM(内存优化) 总结(LMK原理分析+LeakCanary原理详解)

## 内存泄漏分析工具：
1. as自带-静态代码分析工具 —— **Lint**（位置:Analyze--Inspec Code--选择检测模块，ok即可。）
2. **profiler** 的memory分析,具体分析某一个act是否泄漏，打开memory监听视图后，操作多次进入退出act，比较分析进入的内存信息即可。
3. **adb+leakCanary分析**：adb shell dumpsys meminfo com.thinkernote.ThinkerNote -d记录app登陆和退出的信息，比较views是不是为0，不为0用leakCanary定位问题。

## 内存泄漏的场景分析

1. 非静态内部类的静态实例

非静态内部类会持有外部类的引用，如果非静态内部类的实例是静态的，就会长期的维持着外部类的引用，组织被系统回收，解决办法是使用静态内部类

2. 多线程相关的匿名内部类和非静态内部类

匿名内部类同样会持有外部类的引用，如果在线程中执行耗时操作就有可能发生内存泄漏，导致外部类无法被回收，直到耗时任务结束，解决办法是在页面退出时结束线程中的任务

3. Handler内存泄漏

Handler导致的内存泄漏也可以被归纳为非静态内部类导致的，Handler内部message是被存储在MessageQueue中的，有些message不能马上被处理，存在的时间会很长，
导致handler无法被回收，如果handler是非静态的，就会导致它的外部类无法被回收，
解决办法是   

    1.使用静态handler，外部类引用使用弱引用处理
    
    2.在退出页面时移除消息队列中的消息

4. Context导致内存泄漏

根据场景确定使用Activity的Context还是Application的Context,因为二者生命周期不同，对于不必须使用Activity的Context的场景（Dialog）,
一律采用Application的Context,单例模式是最常见的发生此泄漏的场景，比如传入一个Activity的Context被静态类引用，导致无法回收

5. 静态View导致泄漏

使用静态View可以避免每次启动Activity都去读取并渲染View，但是静态View会持有Activity的引用，导致无法回收，
解决办法是在Activity销毁的时候将静态View设置为null
（View一旦被加载到界面中将会持有一个Context对象的引用，在这个例子中，这个context对象是我们的Activity，声明一个静态变量引用这个View，也就引用了activity）

6. WebView导致的内存泄漏

WebView只要使用一次，内存就不会被释放，所以WebView都存在内存泄漏的问题，通常的解决办法是为WebView单开一个进程，使用AIDL进行通信，根据业务需求在合适的时机释放掉

7. 资源对象未关闭导致

如(数据库)Cursor，File,IO等，内部往往都使用了缓冲，会造成内存泄漏，一定要确保先关闭它再将引用置为null


8. 集合中的对象未清理

我们通常把一些对象的引用加入到了集合中，当我们不需要该对象时，并没有把它的引用从集合中清理 掉，这样这个集合就会越来越大。如果这个集合是static的话，那情况就更严重了。

9.Bitmap导致内存泄漏

bitmap是比较占内存的，所以一定要在不使用的时候及时进行recycle()释放内存清理，避免静态变量持有大的bitmap对象

10. 监听器(注册)未关闭

很多需要register和unregister的系统服务要在合适的时候进行unregister（registerReceiver后调用unregisterReceiver）,手动添加的listener也需要及时移除

11. 构造Adapter时，没有使用缓存的convertView


## 如何避免OOM

1. 使用更加轻量的数据结构：
    
    如使用ArrayMap/SparseArray替代HashMap,HashMap更耗内存，因为它需要额外的实例对象来记录Mapping操作，SparseArray更加高效，因为它避免了Key Value的自动装箱，和装箱后的解箱操作
    
    增强for循环
    
2. 减少面枚举的使用，可以用静态常量或者注解@IntDef替代
3. Bitmap优化:

    a.尺寸压缩：通过InSampleSize设置合适的缩放（降低图片大小，降低采样率）
    b.颜色质量：设置模式，存在很大差异（**RGB_8888--->RGB_565**）
    c.inBitmap:使用inBitmap属性可以告知Bitmap解码器去尝试使用已经存在的内存区域，新解码的Bitmap会尝试去使用之前那张Bitmap在Heap中
    所占据的pixel data内存区域，而不是去问内存重新申请一块区域来存放Bitmap。利用这种特性，即使是上千张的图片，也只会仅仅只需要占
    用屏幕所能够显示的图片数量的内存大小，但复用存在一些限制，具体体现在：在Android 4.4之前只能重用相同大小的Bitmap的内存，
    而Android 4.4及以后版本则只要后来的Bitmap比之前的小即可。使用inBitmap参数前，每创建一个Bitmap对象都会分配一块内存供其使用，
    而使用了inBitmap参数后，多个Bitmap可以复用一块内存，这样可以提高性能
    
4. 避免创建不必要的对象：StringBuilder StringBuffer替代String
5. 避免在类似onDraw这样的方法中创建对象，因为它会迅速占用大量内存，引起频繁的GC甚至内存抖动
6. 尽量使用基本数据类型代替封装类型

## 内存抖动(代码注意事项):
内存抖动是由于短时间内有大量对象进出新生区导致的，它伴随着频繁的GC，gc会大量占用ui线程和 cpu资源，会导致app整体卡顿。

避免发生内存抖动的几点建议:

1. 尽量避免在循环体内创建对象，应该把对象创建移到循环体外。 
2. 注意自定义View的onDraw()方法会被频繁调用，所以在这里面不应该频繁的创建对象。 
3. 当需要大量使用Bitmap的时候，试着把它们缓存在数组或容器中实现复用。 
4. 对于能够复用的对象，同理可以使用对象池将它们缓存起来。


## Oom 是否可以try catch ? 

只有在一种情况下，这样做是可行的:

在try语句中声明了很大的对象，导致OOM，并且可以确认OOM是由try语句中的对象声明导致的，那 么在catch语句中，可以释放掉这些对象，解决OOM的问题，继续执行剩余语句。

**但是这通常不是合适的做法**

Java中管理内存除了显式地catch OOM之外还有更多有效的方法:比如SoftReference, WeakReference, 硬盘缓存等。 

在JVM用光内存之前，会多次触发GC，这些GC会降低程序运行的效率。 **如果OOM的原因不是try语句中的对象(比如内存泄漏)，那么在catch语句中会继续抛出OOM**。

## LeakCanary原理

## LeakCanary及原理

LeakCanary是Square公司基于MAT开发的一款监控Android内存泄漏的开源框架。

其工作的原理是：

监测机制利用了Java的WeakReference和ReferenceQueue，通过将Activity包装到WeakReference中，被WeakReference包装过的Activity对象如果被回收，
该WeakReference引用会被放到ReferenceQueue中，通过监测ReferenceQueue里面的内容就能检查到Activity是否能够被回收（在ReferenceQueue中说明可以被回收，
不存在泄漏；否则，可能存在泄漏，LeakCanary是执行一遍GC，若还未在ReferenceQueue中，就会认定为泄漏）。如果Activity被认定为泄露了，
就抓取内存dump文件(Debug.dumpHprofData)；之后通过HeapAnalyzerService.runAnalysis进行分析内存文件分析；
接着通过HeapAnalyzer (checkForLeak—findLeakingReference---findLeakTrace)来进行内存泄漏分析。
最后通过DisplayLeakService进行内存泄漏的展示。




