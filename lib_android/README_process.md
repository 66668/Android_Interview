# 进程 总结


## android中进程的优先级

1. 前台进程:

     (1).当前进程activity正在与用户进行交互（onResume）
     
     (2).当前进程service正在与activity进行交互或者当前service调用了startForground()属于前台进程或者当前service正在执行生命周期（onCreate(),onStart(),onDestory()）
     
     (3).进程持有一个BroadcostReceiver,这个BroadcostReceiver正在执行onReceive()方法
     
2. 可见进程: 可以是处于暂停状态(onPause)的Activity或者绑定在其上的Service，即被用户可见，但由于失了焦点而不能与用户交互
3. 服务进程:其中运行着使用startService方法启动的Service，虽然不被用户可见，但是却是用户关心的，例如用户 正在非音乐界面听的音乐或者正在非下载页面下载的文件等;当系统要空间运行，前两者进程才会被终 止
4. 后台进程: 其中运行着执行onStop方法,但是onDestroy()没有调用的状态的程序，但是却不是用户当前关心的，例如后台挂着的QQ，这时的进程系统一旦没了有内存就首先被杀死
5. 空进程: 不包含任何应用程序的进程，这样的进程系统是一般不会让他存在的

## Android中进程内存的分配，能不能自己分配定额内存?

在Android运行机制里面, 不同分辨率不同RAM大小的设备当然会被分配不同的运行内存.高分辨率的设备也肯定比更低分辨率设备需要更多的内存.具体分配多少,

可以通过查看自己设备当中/system/build.prop 文件,里面有说明.google原生OS的默认值是16M，但是各个厂家的OS会对这个值进行修改

heapgrowthlimit 是一个普通应用的内存限制 ,这个值可以通过ActivityManager.getLargeMemoryClass() 方法得到.

heapstartsize 是初始内存,应用随着使用,内存不断自动的增加,会慢慢达到上限的最大内存


## 广播传输的数据是否有限制，是多少，为什么要限制?

 Intent在传递数据时是有大小限制的，大约限制在1MB之内，你用Intent传递数据，实际上走的是跨进 程通信(IPC)，跨进程通信需要把数据从内核copy到进程中，
 每一个进程有一个接收内核数据的缓冲 区，默认是1M;如果一次传递的数据超过限制，就会出现异常。
 不同厂商表现不一样有可能是厂商修改了此限制的大小，也可能同样的对象在不同的机器上大小不一样。
 
 传递大数据，不应该用Intent;考虑使用ContentProvider或者直接匿名共享内存。简单情况下可以考虑分段传输。

## 后台杀死与恢复机制

后台杀死与LowmemoryKiller的工作机制

https://www.jianshu.com/p/cd0d8594abc7

## FragmentActivity被后台杀死后恢复机制（onSaveInstanceState与OnRestoreInstance）

模拟后台杀死工具:RogueKiller

场景举例：

### 1. 我们创建一个Activity，并且在onCreate函数中新建并show一个DialogFragment，之后通过某种方式将APP异常杀死，或旋转屏幕，被恢复的DialogFragmentActivity时会出现两个FragmentDialog，再次从最近的任务唤起App的时候，会发现显示了两个DialogFragment。

原因：就有一个DialogFragment是通过Android自身的恢复重建机制重建出来，一个被系统恢复的，一个新建的。在异常杀死的情况下onCreate(Bundle savedInstanceState)函数
的savedInstanceState参数也不是null，而是包含了被杀死时所保存的场景信息。

### 2. Act+Fragment的模式下，fragment没有添加无参数构造函数，在模拟后台杀死后，重新打开app会崩溃。

原因：这里就牵扯到恢复时重建逻辑，在被后台异常杀死前，或者说在Activity的onStop执行前，Activity的现场以及Fragment的现场都是已经被保存过的，
其实是被保存在ActivityManagerService中，保存的格式FragmentState，重建的时候，会采用反射机制重新创Fragment，其实就是调用FragmentState的instantiate，
进而调用Fragment的instantiate，最后通过反射，构建Fragment，也就是，被加到FragmentActivity的Fragment在恢复的时候，会被自动创建，
并且采用Fragment的默认无参构造方法，如果没有这个方法，就会抛出InstantiationException异常导致崩溃。
                                                                           
## onSaveInstanceState与OnRestoreInstance的调用时机
onSaveInstanceState函数是Android针对可能被后台杀死的Activity做的一种预防，Activity失去焦点后，可能会由于内存不足，
被回收的情况下，都会去执行onSaveInstanceState。如果Activity没有被后台杀死，那么自然也就不需要进行现场的恢复，也就不会调用OnRestoreInstance


## App开发时针对后台杀死处理方式（暴力方法）

最简单的方式，但是效果一般：取消系统恢复

比如：针对FragmentActivity ，不重建：

        
        protected void onCreate(Bundle savedInstanceState) {
             if (savedInstanceState != null) {
             savedInstanceState.putParcelable(“android:support:fragments”, null);}
             super.onCreate(savedInstanceState);
        }  
如果是系统的Actvity改成是“android:fragments"，不过这里需要注意：对于ViewPager跟FragmentTabHost不需要额外处理，处理了可能反而有反作用。
针对Window，如果不想让View使用恢复逻辑，在基类的FragmentActivity中覆盖onRestoreInstanceState函数即可：
        
        
     protected void onRestoreInstanceState(Bundle savedInstanceState) {
     }   
        
##                                                               