# 内存泄漏OOM的分析总结(LMK原理分析+LeakCanary原理详解)

## 使用as内存分析
https://developer.android.google.cn/studio/profile/memory-profiler

## 分析工具总结：

### 1 as自带-静态代码分析工具 —— Lint
  
1. 位置:Analyze--Inspec Code--选择检测模块，ok即可。

### adb shell 命令检测

使用 adb shell dumpsys meminfo [PackageName]，可以打印出指定包名的应用内存信息
只需要关注Activities和Views两个信息即可，打开act和关闭act，查看act和view的对应关系是否变化异常，判断是否内存泄漏

## 内存泄漏的场景和解决办法

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
如Cursor，File,IO等，内部往往都使用了缓冲，会造成内存泄漏，一定要确保关闭它并将引用置为null

8. 集合中的对象未清理

集合用于保存对象，如果集合越来越大，不进行合理的清理，尤其是入股集合是静态的

9.Bitmap导致内存泄漏

bitmap是比较占内存的，所以一定要在不使用的时候及时进行清理，避免静态变量持有大的bitmap对象

10. 监听器未关闭

很多需要register和unregister的系统服务要在合适的时候进行unregister,手动添加的listener也需要及时移除


## 如何避免OOM

1. 使用更加轻量的数据结构：如使用ArrayMap/SparseArray替代HashMap,HashMap更耗内存，因为它需要额外的实例对象来记录Mapping操作，SparseArray更加高效，因为它避免了Key Value的自动装箱，和装箱后的解箱操作
2. 减少面枚举的使用，可以用静态常量或者注解@IntDef替代
3. Bitmap优化:

    a.尺寸压缩：通过InSampleSize设置合适的缩放
    b.颜色质量：设置合适的format，ARGB_6666/RBG_545/ARGB_4444/ALPHA_6，存在很大差异
    c.inBitmap:使用inBitmap属性可以告知Bitmap解码器去尝试使用已经存在的内存区域，新解码的Bitmap会尝试去使用之前那张Bitmap在Heap中所占据的pixel data内存区域，而不是去问内存重新申请一块区域来存放Bitmap。利用这种特性，即使是上千张的图片，也只会仅仅只需要占用屏幕所能够显示的图片数量的内存大小，但复用存在一些限制，具体体现在：在Android 4.4之前只能重用相同大小的Bitmap的内存，而Android 4.4及以后版本则只要后来的Bitmap比之前的小即可。使用inBitmap参数前，每创建一个Bitmap对象都会分配一块内存供其使用，而使用了inBitmap参数后，多个Bitmap可以复用一块内存，这样可以提高性能
    
4. StringBuilder替代String: 在有些时候，代码中会需要使用到大量的字符串拼接的操作，这种时候有必要考虑使用StringBuilder来替代频繁的“+”
5. 避免在类似onDraw这样的方法中创建对象，因为它会迅速占用大量内存，引起频繁的GC甚至内存抖动
6. 减少内存泄漏也是一种避免OOM的方法

## LeakCanary原理




