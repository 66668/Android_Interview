# 进程简单 总结

后台杀死与恢复机制

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