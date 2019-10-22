# LeakCanary 总结
## 用途和使用场景

直接从application中拿到全局的 refWatcher 对象，在Fragment或其他组件的销毁回调中使用 refWatcher.watch(this)检测是否发生内存泄漏。

## 优点
1. 针对Android Activity组件完全自动化的内存泄漏检查，在最新的版本中，还加入了 android.app.fragment的组件自动化的内存泄漏检测。
2. 易用集成，使用成本低。
3. 友好的界面展示和通知。

## 缺点

检测结果并不是特别的准确，因为内存的释放和对象的生命周期有关也和GC的调度有关。


## 核心实现原理?怎么去实现?

主要分为如下7个步骤:

1. RefWatcher.watch()创建了一个KeyedWeakReference用于去观察对象。
2. 然后，在后台线程中，它会检测引用是否被清除了，并且是否没有触发GC。 
3. 如果引用仍然没有被清除，那么它将会把堆栈信息保存在文件系统中的.hprof文件里。 
4. HeapAnalyzerService被开启在一个独立的进程中，并且HeapAnalyzer使用了HAHA开源库解 析了指定时刻的堆栈快照文件heap dump。(利用haha库去输出dump信息)
5. 从heap dump中，HeapAnalyzer根据一个独特的引用key找到了KeyedWeakReference，并 且定位了泄露的引用。
6. HeapAnalyzer为了确定是否有泄露，计算了到GC Roots的最短强引用路径，然后建立了导致 泄露的链式引用。 
7. 这个结果被传回到app进程中的DisplayLeakService，然后一个泄露通知便展现出来了。

简单来说就是:

在一个Activity执行完onDestroy()之后，将它放入WeakReference中，然后将这个WeakReference类型 的Activity对象与ReferenceQueque关联。这时再从ReferenceQueque中查看是否有该对象，如果没 有，执行gc，再次查看，还是没有的话则判断发生内存泄露了。最后用HAHA这个开源库去分析dump 之后的heap内存(主要就是创建一个HprofParser解析器去解析出对应的引用内存快照文件 snapshot)。

流程图:

![LeakCanary](https://github.com/66668/Android_Interview/blob/master/pictures/leakcanary_01.png)

## LeakCanary2.0版本原理
### 1.6版本配置：
1. 依赖：
        
        
        dependencies {
          debugImplementation 'com.squareup.leakcanary:leakcanary-android:1.6.3'
          releaseImplementation 'com.squareup.leakcanary:leakcanary-android-no-op:1.6.3'
          // Optional, if you use support library fragments:
          debugImplementation 'com.squareup.leakcanary:leakcanary-support-fragment:1.6.3'
        }	

2. 初始化：

        
        public class ExampleApplication extends Application {
        	
        	  @Override public void onCreate() {
        	    super.onCreate();
        	    if (LeakCanary.isInAnalyzerProcess(this)) {
        	      // This process is dedicated to LeakCanary for heap analysis.
        	      // You should not init your app in this process.
        	      return;
        	    }
        	    LeakCanary.install(this);
        	    // Normal app init code...
        	  }
        	}
        	
### 2.0版本配置：

1. 只需要添加一行依赖即可，而且不需要
dependencies {
  debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.0-alpha-1'
}

### 原理：
在自定义ContentProvider类LeakSentryInstaller的onCreate中初始化

        
        internal class LeakSentryInstaller : ContentProvider() {
        
          override fun onCreate(): Boolean {
            CanaryLog.logger = DefaultCanaryLog()
            //初始化
            val application = context!!.applicationContext as Application
            InternalLeakSentry.install(application)  
            //骚操作在这里，利用系统自动调用CP的onCreate方法来做初始化
            return true
          }
          ...

